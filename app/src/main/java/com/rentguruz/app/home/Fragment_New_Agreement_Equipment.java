package com.rentguruz.app.home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementEquipmentBinding;
import com.rentguruz.app.databinding.RowEquipmentBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.reservation.ReservationEquipment;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RIequipment;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.EQUIPMENT;

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
        binding.setUiColor(UiColor);
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
                            rowEquipmentBinding.setUiColor(UiColor);
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
                                        Drawable squareline = getResources().getDrawable(R.drawable.round_image);
                                        squareline.setTint(Color.parseColor(UiColor.primary));
                                        squareline.setTintMode(PorterDuff.Mode.LIGHTEN);

                                        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.round_image);
                                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                                        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(UiColor.primary));

                                        // squareline.setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.CLEAR);
                                        rowEquipmentBinding.line.setBackground(wrappedDrawable);
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
                                   Drawable squareline = getResources().getDrawable(R.drawable.round_image);
                                   squareline.setTint(Color.parseColor(UiColor.primary));
                                   //squareline.setTintMode(PorterDuff.Mode.LIGHTEN);
                                   rowEquipmentBinding.line.setBackground(squareline);
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
