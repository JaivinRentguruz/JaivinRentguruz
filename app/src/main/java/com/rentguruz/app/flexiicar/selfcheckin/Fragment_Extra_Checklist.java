package com.rentguruz.app.flexiicar.selfcheckin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.ChkHeaderBinding;
import com.rentguruz.app.databinding.FragmentExtraChecklistBinding;
import com.rentguruz.app.R;
import com.rentguruz.app.databinding.ListCheckEquipmentBinding;
import com.rentguruz.app.databinding.SingleCheckboxBinding;
import com.rentguruz.app.model.AttachmentsModel;
import com.rentguruz.app.model.CheckOutList2;
import com.rentguruz.app.model.ReservationCheckListModels;
import com.rentguruz.app.model.ReservationCheckin;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.reservation.ReservationEquipment;
import com.rentguruz.app.model.response.ReservationSummarry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKINN;
import static com.rentguruz.app.apicall.ApiEndPoint.EQUIPMENT;
import static com.rentguruz.app.apicall.ApiEndPoint.FlEETCHECKLIST;

public class Fragment_Extra_Checklist extends BaseFragment {

    FragmentExtraChecklistBinding binding;
    List<AttachmentsModel> attachmentsModelList = new ArrayList<>();
    ReservationSummarry reservationSummarry = new ReservationSummarry();
    SingleCheckboxBinding checkboxBinding;
    ChkHeaderBinding chkHeaderBinding;
    ListCheckEquipmentBinding checkEquipmentBinding;
    ReservationEquipment[] equipment;
    boolean checking = false;
    ReservationCheckin reservationCheckout;
    int id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExtraChecklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(companyLabel.Equipment + " ( Extra List )" );
        bundle.putInt("Id", getArguments().getInt("Id"));
        Log.e("TAG", "RID " + getArguments().getInt("Id"));
        bundle.putSerializable("image", (Serializable) attachmentsModelList);
        bundle.putInt("length", 10);
        bundle.putInt("temp",1);
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("resrvation");
        bundle.putSerializable("resrvation",getArguments().getSerializable("resrvation"));
        bundle.putSerializable("reservation",getArguments().getSerializable("reservation"));
        bundle.putInt( "Id", getArguments().getInt("Id"));
        reservationCheckout = (ReservationCheckin) getArguments().getSerializable("checkoutmodel");
        id = reservationSummarry.Id;
        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            JSONArray resultSet = responseJSON.getJSONArray("Data");

                            CheckOutList2[] list2s ;
                            list2s = loginRes.getModel(resultSet.toString(), CheckOutList2[].class);
                            for (int i = 0; i <list2s.length ; i++) {
                                getSubview(i);
                                chkHeaderBinding = ChkHeaderBinding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                chkHeaderBinding.getRoot().setId(200 + i);
                                chkHeaderBinding.getRoot().setLayoutParams(subparams);
                                chkHeaderBinding.chkheader.setText(list2s[i].CheckListHeaderName);

                                for (int j = 0; j <list2s[i].items.size(); j++) {
                                    getSubview(j);
                                    checkboxBinding = SingleCheckboxBinding.inflate(subinflater,
                                            getActivity().findViewById(android.R.id.content), false);
                                    checkboxBinding.getRoot().setId(200 + j);
                                    checkboxBinding.getRoot().setLayoutParams(subparams);
                                    checkboxBinding.chkOptionName.setText(list2s[i].items.get(j).Name);
                                    checkboxBinding.setUiColor(UiColor);
                                    chkHeaderBinding.checkboxdata.addView(checkboxBinding.getRoot());

                                   /* if (list2s[i].items.get(j).IsMandatory){

                                    }*/

                                    int finalI = i;
                                    int finalJ = j;
                                    checkboxBinding.chkRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                checking = true;
                                                checkList(list2s[finalI].items.get(finalJ).Id,1);
                                            }

                                        }
                                    });


                                }
                                binding.extrachecklist.addView(chkHeaderBinding.getRoot());

                            }


                            Log.e(TAG, "run: " + resultSet );
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, FlEETCHECKLIST, BASE_URL_LOGIN, header,
                params.getCheckListAccessories(reservationSummarry.ReservationVehicleModel.VehicleId, reservationSummarry.ReservationVehicleModel.VehicleTypeId, false));

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");

                            if (status) {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                JSONArray array = resultSet.getJSONArray("Data");
                                equipment = loginRes.getModel(array.toString(), ReservationEquipment[].class);

                                for (int i = 0; i <equipment.length ; i++) {
                                    getSubview(i);
                                    checkEquipmentBinding = ListCheckEquipmentBinding.inflate(subinflater,
                                            getActivity().findViewById(android.R.id.content), false);
                                    checkEquipmentBinding.getRoot().setId(200 + i);
                                    checkEquipmentBinding.getRoot().setLayoutParams(subparams);
                                    checkEquipmentBinding.setUiColor(UiColor);
                                    checkEquipmentBinding.txtChkOptionName.setText(equipment[i].Name);

                                    try {
                                        if (reservationSummarry.ReservationEquipmentInventoryModel.size()>0){
                                            for (int j = 0; j <reservationSummarry.ReservationEquipmentInventoryModel.size() ; j++) {
                                                if (reservationSummarry.ReservationEquipmentInventoryModel.get(j).EquipInventId == equipment[i].Id){
                                                    checkEquipmentBinding.text.setText(String.valueOf(reservationSummarry.ReservationEquipmentInventoryModel.get(j).Quantity));
                                                    checkEquipmentBinding.chkRed.setChecked(true);
                                                }
                                            }
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }



                                    binding.equipment.addView(checkEquipmentBinding.getRoot());
                                }
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());

        binding.bottom.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom:
                if (checking) {
                    //reservationCheckin.ReservationCheckListModels.add()
                    NavHostFragment.findNavController(Fragment_Extra_Checklist.this)
                            .navigate(R.id.payment_sucess_to_vehicleimage, bundle);
                    reservationCheckout.IsSaveFinalCheckInProcess = true;
                    reservationCheckout.ReservationOriginDataModels = reservationSummarry.ReservationOriginDataModels;
                    ApiService2 apiService2 = new ApiService2(ReservatioCheckouts, RequestType.POST, CHECKINN, BASE_URL_LOGIN, header ,reservationCheckout);

                } else {
                    CustomToast.showToast(getActivity(),"Please Checked Checkbox ",1);
                }
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Extra_Checklist.this).popBackStack();
                break;
        }
    }

    OnResponseListener ReservatioCheckouts = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                UserData.reservationCheckin =   loginRes.getModel(resultSet.toString(), ReservationCheckin.class);
                                //UserData.reservationCheckout =  loginRes.getModel(resultSet.toString(), ReservationCheckout.class);

                                /*NavHostFragment.findNavController(Fragment_Check_in.this)
                                        .navigate(R.id.action_Self_check_Out_to_Vehicle_Image_1,bundle);*/

                              /*  NavHostFragment.findNavController(Fragment_Check_in.this)
                                        .navigate(R.id.payment_sucess_to_vehicleimage,bundle);*/

                                NavHostFragment.findNavController(Fragment_Extra_Checklist.this)
                                        .navigate(R.id.action_Self_check_Out_Extra_to_Vehicle_Image_1, bundle);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public void onError(String error) {

        }
    };


    public void checkList(int idd,int i){
        Boolean value = false;
        for (int j = 0; j < reservationCheckout.ReservationCheckListModels.size() ; j++) {
            if (reservationCheckout.ReservationCheckListModels.get(j).CheckListId==idd){
                reservationCheckout.ReservationCheckListModels.get(j).Condition = i;
                value  =  true;

            }
        }

        // reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id, idd, i,i));
        if (!value){
            reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id, idd, i,0));
        }
    }
}
