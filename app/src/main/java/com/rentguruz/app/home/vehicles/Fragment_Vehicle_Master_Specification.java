package com.rentguruz.app.home.vehicles;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleMasterSpecificationBinding;
import com.rentguruz.app.model.display.ThemeColors;
import com.rentguruz.app.model.response.VehicleModel;

public class Fragment_Vehicle_Master_Specification extends BaseFragment {

    FragmentVehicleMasterSpecificationBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterSpecificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.specification));

        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

        binding.vehicledetail.setVehicle(vehicleModel);
        binding.vehDescription.setText(vehicleModel.VehDescription);
        CustomBindingAdapter.loadImage(binding.car.VehImage, vehicleModel.DefaultImagePath);
        binding.car.txtvehicleName.setText(vehicleModel.VehicleName);
        binding.car.txtVehicleNumber.setText(vehicleModel.VinNumber);

        binding.image.setOnClickListener(this);
        binding.detailed.setOnClickListener(this);
        binding.report.setOnClickListener(this);
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
            case R.id.image:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification.this).navigate(R.id.vehicle_specification_to_vehicle_image,bundle);
                break;

            case R.id.detailed:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification.this).navigate(R.id.vehicle_specification_to_vehicle_additional,bundle);
                break;

            case R.id.basic:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification.this).navigate(R.id.vehicle_specification_to_vehicle_specification,bundle);
                break;

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification.this).navigate(R.id.vehicle_specification_to_home,bundle);
                break;

            case R.id.report:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification.this).navigate(R.id.vehicle_specification_to_vehicle_rate,bundle);
                break;
        }
    }


}
