package com.rentguruz.app.home.vehicles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleLicenseAndCertificationsBinding;
import com.rentguruz.app.model.response.VehicleModel;


public class Fragment_Vehicle_License_And_Certificate extends BaseFragment {

    FragmentVehicleLicenseAndCertificationsBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVehicleLicenseAndCertificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(companyLabel.Vehicle + " " + companyLabel.License);
        binding.lblLicensePlate.setOnClickListener(this);

        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);

        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

        CustomBindingAdapter.loadImage(binding.car.VehImage, vehicleModel.DefaultImagePath);
        binding.car.txtvehicleName.setText(vehicleModel.VehicleName);
        binding.car.txtVehicleNumber.setText(vehicleModel.VinNumber);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.lblLicensePlate:
                NavHostFragment.findNavController(Fragment_Vehicle_License_And_Certificate.this)
                        .navigate(R.id.action_VehiclesLicenceCerti_to_VehiclesInspection);
                break;*/

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_License_And_Certificate.this).popBackStack();
                break;
        }
    }
}

