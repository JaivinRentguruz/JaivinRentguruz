package com.abel.app.b2b.home.vehicles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleMasterSpecificationBinding;
import com.abel.app.b2b.databinding.FragmentVehicleMasterUploadimageBinding;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETIMAGE;

public class Fragment_Vehicle_Master_Images extends BaseFragment {

    FragmentVehicleMasterUploadimageBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterUploadimageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.header.screenHeader.setText(getResources().getString(R.string.specification));

        JSONObject object = new JSONObject();
        try {
            object.accumulate("fileUploadType","8");
            object.accumulate("CompanyId", Helper.id);
        } catch (Exception e){
            e.printStackTrace();
        }


        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {

            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST,GETIMAGE,BASE_URL_LOGIN,header,object);


        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

        CustomBindingAdapter.loadImage(binding.carimage, vehicleModel.DefaultImagePath);
        //CustomBindingAdapter.loadImage(binding.images.carImage1,vehicleModel.AttachmentsModels.get(0).AttachmentPath);
       // binding.images.setVehicle(vehicleModel);

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
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {

    }
}
