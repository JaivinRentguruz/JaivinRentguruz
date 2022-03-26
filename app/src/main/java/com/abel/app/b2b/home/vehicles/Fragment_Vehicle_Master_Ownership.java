package com.abel.app.b2b.home.vehicles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleCurrentRentalBinding;
import com.abel.app.b2b.databinding.FragmentVehicleMasterOwnershipBinding;
import com.abel.app.b2b.model.response.VehicleModel;

public class Fragment_Vehicle_Master_Ownership  extends BaseFragment {

     FragmentVehicleMasterOwnershipBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterOwnershipBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

        binding.header.back.setOnClickListener(this);
    }

        @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Ownership.this).popBackStack();
                break;
        }

    }
}
