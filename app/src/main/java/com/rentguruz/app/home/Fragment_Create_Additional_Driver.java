package com.rentguruz.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.ScanDrivingLicense;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCreateAdditionalDriverBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

public class Fragment_Create_Additional_Driver extends BaseFragment {

    FragmentCreateAdditionalDriverBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAdditionalDriverBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.lblEnterDetails.setOnClickListener(this);
        binding.scanimglayout.setOnClickListener(this);

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

        if (getArguments().getString("dropdate") != null){
            ReservationSummarry reservationSummarry = new ReservationSummarry();
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
            preference.storeData("reservationSum", loginRes.modeltostring(TAG,reservationSummarry));
            VehicleModel vehicleModel = new VehicleModel();
            vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
            preference.storeData("Model", loginRes.modeltostring(TAG,vehicleModel));
            LocationList models = new LocationList();
            models = (LocationList) getArguments().getSerializable("models");
            preference.storeData("models", loginRes.modeltostring(TAG,models));
            LocationList model = new LocationList();
            model = (LocationList) getArguments().getSerializable("model");
            preference.storeData("model", loginRes.modeltostring(TAG,model));
            ReservationTimeModel timemodel = new ReservationTimeModel();
            timemodel = (ReservationTimeModel) getArguments().getSerializable("timemodel");
            preference.storeData("timemodel", loginRes.modeltostring(TAG,timemodel));
            Customer customer= new Customer();
            customer = (Customer) getArguments().getSerializable("customer");
            preference.storeData("customer", loginRes.modeltostring(TAG,customer));
            preference.storeData("pickupdate",getArguments().getString("pickupdate"));
            preference.storeData("dropdate", getArguments().getString("dropdate"));
            preference.storeData("droptime", getArguments().getString("droptime"));
            preference.storeData("pickuptime", getArguments().getString("pickuptime"));
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lbl_enter_details:
                if(getArguments().getBoolean("updateLic")) {
                    bundle.putBoolean("updateLic", true);
                } else {
                    bundle.putBoolean("updateLic", false);
                }
                NavHostFragment.findNavController(Fragment_Create_Additional_Driver.this).navigate(R.id.drivercreate_to_adddriver1,bundle);
                break;

            case R.id.scanimglayout:
                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                Helper.screenNumber = 2;
                i.putExtra("afterScanBackTo", 4);
                startActivity(i);
                break;

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Create_Additional_Driver.this).popBackStack();
                break;
        }
    }
}
