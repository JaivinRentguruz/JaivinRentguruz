package com.abel.app.b2b.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.DigitConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementEquipmentBinding;
import com.abel.app.b2b.databinding.RowEquipmentBinding;
import com.abel.app.b2b.databinding.TaxDetailsListBinding;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.reservation.ReservationEquipment;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.RIequipment;
import com.abel.app.b2b.model.response.ReservationOriginDataModels;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.EQUIPMENT;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_New_Agreement_Equipment extends BaseFragment {

    FragmentNewAgreementEquipmentBinding binding;
    ReservationEquipment[] equipment;
    ReservationSummarry reservationSummarry;
    public static HashMap<Integer, String> activationequipment = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementEquipmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.add.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);

        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Equipment);
       // binding.save.setVisibility(View.GONE);
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer",(Customer) getArguments().getSerializable("customer"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));

        /*{"limit":0,"orderDir":"desc","pageSize":0,"pageLimits":[10,20,30,40,50],
        "filterObj":{"IsActive":true,"CompanyId":1,"GetForReservation":true,"fStartDate":"2021-12-20T05:05:41.772Z","fEndDate":"2021-12-23T05:05:41.772Z",
        "ReservationSummaryDetailModel":{"TotalTime":3,"ReservationSummaryDetailType":9}}}*/

        reservationSummarry = new ReservationSummarry();
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");

        apiService = new ApiService(getEquipment, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                NavHostFragment.findNavController(Fragment_New_Agreement_Equipment.this).navigate(R.id.equipment_to_addequipment);
                break;
            case R.id.discard:
            case R.id.back:
            case R.id.save:
                bundle.putSerializable("reservationSum", reservationSummarry);
                NavHostFragment.findNavController(Fragment_New_Agreement_Equipment.this).navigate(R.id.equipment_to_booking,bundle);
                break;

        }
    }

    OnResponseListener getEquipment = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        equipment = loginRes.getModel(array.toString(), ReservationEquipment[].class);
                        UserData.equipment = equipment;
                        Log.d(TAG, "onSuccess: " + resultSet);
                        for (int i = 0; i <equipment.length; i++) {
                            getSubview(i);
                            RowEquipmentBinding rowEquipmentBinding = RowEquipmentBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            rowEquipmentBinding.setEquipment(equipment[i]);
                            rowEquipmentBinding.getRoot().setId(200+i);
                            rowEquipmentBinding.getRoot().setLayoutParams(subparams);
                            binding.adddata.addView(rowEquipmentBinding.getRoot());
                          /*  int data = reservationSummarry.ReservationEquipmentInventoryModel.size();
                            for (int j = 0; j <reservationSummarry.ReservationEquipmentInventoryModel.size() ; j++) {
                                if (reservationSummarry.ReservationEquipmentInventoryModel.get(i).EquipInventId == equipment[i].Id){
                                    rowEquipmentBinding.selectEquip.setChecked(true);
                                }
                            }


                            rowEquipmentBinding.selectEquip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){
                                        updateReservationSummarry(equipment[finalI]);
                                    } else {
                                        updateReservationSummarry(equipment[finalI]);
                                    }
                                }
                            });*/

                            try {
                                for (int j = 0; j <reservationSummarry.ReservationEquipmentInventoryModel.size() ; j++) {
                                    if (reservationSummarry.ReservationEquipmentInventoryModel.get(j).EquipInventId == equipment[i].Id){
                                        rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                                        rowEquipmentBinding.line.setPadding(10,10,10,10);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int finalI = i;
                            rowEquipmentBinding.getRoot().setOnClickListener(v -> {
                                               // if (equipment[finalI].SerialNo == null) {
                                                 /*   equipment[finalI].SerialNo = "001";
                                                    reservationSummarry.ReservationEquipmentInventoryModel.add(new RIequipment(1, equipment[finalI].SerialNo, equipment[finalI].Id,
                                                            1, Integer.valueOf(equipment[finalI].Price.intValue()), Integer.valueOf(equipment[finalI].MaxPrice.intValue())));*/
                                             //   }

                               /* reservationSummarry.ReservationEquipmentInventoryModel.add(new RIequipment(1, equipment[finalI].SerialNo, equipment[finalI].Id,
                                        1,Integer.valueOf(equipment[finalI].Price.intValue()),Integer.valueOf(equipment[finalI].MaxPrice.intValue())));

                                reserversationSummary.ReservationEquipmentInventoryModel.add( new RIequipment(1,obj.getString("SerialNo"), obj.getInt("Id"),
                                        1,obj.getInt("Price"),obj.getInt("MaxPrice")));*/

                                /*Log.e(TAG, "onSuccess: " +  reservationSummarry.ReservationEquipmentInventoryModel.size());
                                if (reservationSummarry.ReservationEquipmentInventoryModel.size()!=0) {
                                    for (int j = 0; j < reservationSummarry.ReservationEquipmentInventoryModel.size(); j++) {
                                        if (reservationSummarry.ReservationEquipmentInventoryModel.get(j).EquipInventId == equipment[finalI].Id) {
                                            rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                                            rowEquipmentBinding.line.setPadding(10, 10, 10, 10);
                                            //updateReservationSummarry(equipment[finalI]);
                                            removeReservationSummarry(equipment[finalI]);
                                            bundle.putSerializable("reservationSum", reservationSummarry);
                                        } else {
                                            rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.curve_box_gray_box_bg));
                                            rowEquipmentBinding.line.setPadding(10, 10, 10, 10);
                                            updateReservationSummarry(equipment[finalI]);
                                            bundle.putSerializable("reservationSum", reservationSummarry);
                                        }
                                    }
                                } else {
                                    rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                                    rowEquipmentBinding.line.setPadding(10, 10, 10, 10);
                                    updateReservationSummarry(equipment[finalI]);
                                    bundle.putSerializable("reservationSum", reservationSummarry);
                                }*/
                                equipment[finalI].SerialNo = "001";
                               if (validation(equipment[finalI].Id)){
                                   rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.curve_box_gray_box_bg));
                                   rowEquipmentBinding.line.setPadding(10, 10, 10, 10);
                                   /*reservationSummarry.ReservationEquipmentInventoryModel.remove(new RIequipment(1, equipment[finalI].SerialNo, equipment[finalI].Id,
                                           1, Integer.valueOf(equipment[finalI].Price.intValue()), Integer.valueOf(equipment[finalI].MaxPrice.intValue())));*/

                                   /*Iterator itr = reservationSummarry.ReservationEquipmentInventoryModel.iterator();
                                   while (itr.hasNext()){

                                       if (reservationSummarry.ReservationEquipmentInventoryModel.get(itr.next()).)
                                   }
*/
                                   removeData(equipment[finalI].Id);

                                } else {
                                   rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                                   rowEquipmentBinding.line.setPadding(10,10,10,10);
                                   updateReservationSummarry(equipment[finalI]);
                                   bundle.putSerializable("reservationSum", reservationSummarry);
                               }


                                /*rowEquipmentBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                                rowEquipmentBinding.line.setPadding(10,10,10,10);
                                updateReservationSummarry(equipment[finalI]);
                                bundle.putSerializable("reservationSum", reservationSummarry);*/
                               // NavHostFragment.findNavController(Fragment_New_Agreement_Equipment.this).navigate(R.id.equipment_to_booking,bundle);


                            });
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            });

        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    public void updateReservationSummarry(ReservationEquipment key){
        int data = reservationSummarry.ReservationEquipmentInventoryModel.size();
        Log.e(TAG, "updateReservationSummarry: " + data );
        key.SerialNo = "001";
        for (int i = 0; i <data; i++) {
            if (reservationSummarry.ReservationEquipmentInventoryModel.get(i).EquipInventId == key.Id) {
                reservationSummarry.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reservationSummarry.ReservationEquipmentInventoryModel.add(new RIequipment(1, key.SerialNo, key.Id,
                1, Integer.valueOf(key.Price.intValue()), Integer.valueOf(key.MaxPrice.intValue())));
    }

    public void addReservationSummarry(ReservationEquipment key){
        int data = reservationSummarry.ReservationEquipmentInventoryModel.size();
        Log.e(TAG, "updateReservationSummarry: " + data );
        key.SerialNo = "001";
        for (int i = 0; i <data; i++) {
            if (reservationSummarry.ReservationEquipmentInventoryModel.get(i).EquipInventId == key.Id) {
                reservationSummarry.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reservationSummarry.ReservationEquipmentInventoryModel.add(new RIequipment(1, key.SerialNo, key.Id,
                1, Integer.valueOf(key.Price.intValue()), Integer.valueOf(key.MaxPrice.intValue())));
    }


    public void removeReservationSummarry(ReservationEquipment key){
        int data = reservationSummarry.ReservationEquipmentInventoryModel.size();
        Log.e(TAG, "updateReservationSummarry: " + data );
        key.SerialNo = "001";
        for (int i = 0; i <data; i++) {
            if (reservationSummarry.ReservationEquipmentInventoryModel.get(i).EquipInventId == key.Id) {
                reservationSummarry.ReservationOriginDataModels.remove(i);
                break;
            }
        }
    }
    public Boolean validation(int id) {
        Boolean value = false;
        Log.e(TAG, "validation: " + reservationSummarry.ReservationEquipmentInventoryModel.size() );
        for (int j = 0; j <reservationSummarry.ReservationEquipmentInventoryModel.size() ; j++) {
            if (reservationSummarry.ReservationEquipmentInventoryModel.get(j).EquipInventId == id){
                value = true;
            }
        }

        return value;
    }

    public void removeData(int id){

        Iterator itr = reservationSummarry.ReservationEquipmentInventoryModel.iterator();
        while (itr.hasNext()){
           /* ArrayList<RIequipment> EquipmentInventoryModel = new ArrayList<RIequipment>();
            EquipmentInventoryModel  =   itr.next();*/

            RIequipment rIequipment = new RIequipment();
            rIequipment = (RIequipment) itr.next();
            if (rIequipment.EquipInventId == id){
                itr.remove();
            }
        }
    }
}
