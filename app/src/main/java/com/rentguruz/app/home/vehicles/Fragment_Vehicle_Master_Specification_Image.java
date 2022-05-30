package com.rentguruz.app.home.vehicles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleMasterSpecificationImageBinding;
import com.rentguruz.app.model.response.VehicleModel;

public class Fragment_Vehicle_Master_Specification_Image extends BaseFragment {

     FragmentVehicleMasterSpecificationImageBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterSpecificationImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.header.screenHeader.setText(getResources().getString(R.string.specification));
        binding.setUiColor(UiColor);
        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

            try {
                CustomBindingAdapter.loadImage(binding.images.carImage1, vehicleModel.AttachmentsModels.get(1).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage2, vehicleModel.AttachmentsModels.get(2).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage3, vehicleModel.AttachmentsModels.get(3).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage4, vehicleModel.AttachmentsModels.get(4).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage5, vehicleModel.AttachmentsModels.get(5).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage6, vehicleModel.AttachmentsModels.get(6).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage7, vehicleModel.AttachmentsModels.get(7).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage8, vehicleModel.AttachmentsModels.get(8).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage9, vehicleModel.AttachmentsModels.get(9).AttachmentPath);
                CustomBindingAdapter.loadImage(binding.images.carImage10, vehicleModel.AttachmentsModels.get(10).AttachmentPath);
            } catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            try {
                binding.images.imgg1.setBackground(userDraw.getImageUpload());
                binding.images.imgg2.setBackground(userDraw.getImageUpload());
                binding.images.imgg3.setBackground(userDraw.getImageUpload());
                binding.images.imgg4.setBackground(userDraw.getImageUpload());
                binding.images.imgg5.setBackground(userDraw.getImageUpload());
                binding.images.imgg6.setBackground(userDraw.getImageUpload());
                binding.images.imgg7.setBackground(userDraw.getImageUpload());
                binding.images.imgg8.setBackground(userDraw.getImageUpload());
                binding.images.imgg9.setBackground(userDraw.getImageUpload());
                binding.images.imgg10.setBackground(userDraw.getImageUpload());
            } catch (Exception e){
                e.printStackTrace();
            }

        //binding.vehDescription.setText(vehicleModel.VehDescription);
        CustomBindingAdapter.loadImage(binding.car.VehImage, vehicleModel.DefaultImagePath);
        binding.car.txtvehicleName.setText(vehicleModel.VehicleName);
        binding.car.txtVehicleNumber.setText(vehicleModel.VinNumber);

        binding.basic.setOnClickListener(this);
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
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification_Image.this).navigate(R.id.vehicle_specification_to_vehicle_image,bundle);
                break;

            case R.id.detailed:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification_Image.this).navigate(R.id.vehicle_specification_to_vehicle_additional,bundle);
                break;

            case R.id.basic:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification_Image.this).navigate(R.id.vehicle_specification_to_vehicle_specification,bundle);
                break;

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification_Image.this).navigate(R.id.vehicle_specification_to_home,bundle);
                break;

            case R.id.report:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Specification_Image.this).navigate(R.id.vehicle_specification_to_vehicle_rate,bundle);
                break;
        }
    }
}
