package com.abel.app.b2b.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAdditionalDriverDetailBinding;
import com.abel.app.b2b.databinding.FragmentAdditionalDriverProfile1Binding;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.UpdateDL;
import com.abel.app.b2b.model.response.VehicleModel;

public class Fragment_Additional_Driver_Details extends BaseFragment {

    FragmentAdditionalDriverDetailBinding binding;
    UpdateDL updateDL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdditionalDriverDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        updateDL = new UpdateDL();
        updateDL = (UpdateDL) getArguments().getSerializable("drivinglist");

        binding.txtFName.setText(updateDL.FName + " " + updateDL.LName);
        binding.licenceNumber.setText(updateDL.DrivingLicenceModel.Number);
        binding.expiryate.setText(updateDL.DrivingLicenceModel.ExpiryDate);
        binding.birthdate.setText(updateDL.DrivingLicenceModel.DOB);
        CustomBindingAdapter.caps(binding.txtFName,updateDL.FName);
        CustomBindingAdapter.caps(binding.txtLName,updateDL.LName);

        binding.save.setOnClickListener(this);

        ReservationSummarry reservationSummarry = new ReservationSummarry();
        reservationSummarry =  loginRes.callFriend("reservationSum", ReservationSummarry.class);
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel =  loginRes.callFriend("Model", VehicleModel.class);
        LocationList models = new LocationList();
        models =  loginRes.callFriend("models", LocationList.class);
        LocationList model = new LocationList();
        model =  loginRes.callFriend("model", LocationList.class);
        ReservationTimeModel timemodel = new ReservationTimeModel();
        timemodel =  loginRes.callFriend("timemodel", ReservationTimeModel.class);
        Customer customer= new Customer();
        customer =  loginRes.callFriend("customer", Customer.class);

        bundle.putSerializable("reservationSum", reservationSummarry);
        bundle.putSerializable("Model", vehicleModel);
        bundle.putSerializable("models",models);
        bundle.putSerializable("model",model);
        bundle.putSerializable("timemodel",timemodel);
        bundle.putSerializable("customer",customer);
        bundle.putString("pickupdate", preference.getdata("pickupdate"));
        bundle.putString("dropdate", preference.getdata("dropdate"));
        bundle.putString("droptime", preference.getdata("droptime"));
        bundle.putString("pickuptime",  preference.getdata("pickuptime"));


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                NavHostFragment.findNavController(Fragment_Additional_Driver_Details.this).navigate(R.id.driverdetails_to_driver,bundle);
                break;
        }


    }
}
