package com.abel.app.b2b.home.vehicles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleBasicDetailsBinding;
import com.abel.app.b2b.databinding.FragmentVehicleMasterBinding;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.RateModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.RATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Vehicle_master extends BaseFragment {

    FragmentVehicleMasterBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.header.screenHeader.setText(companyLabel.Vehicle + " Master");

        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);
        bundle.putInt("Idd",getArguments().getInt("Idd"));
        bundle.putInt("Idd", vehicleModel.Id);
        CustomBindingAdapter.loadImage(binding.carImgview, vehicleModel.DefaultImagePath);
        CustomBindingAdapter.camelcase(binding.carname,vehicleModel.VehicleName);


        binding.lblReservation.setOnClickListener(this);
        binding.lblAgreements.setOnClickListener(this);
        binding.currentRantal.setOnClickListener(this);
        binding.lblBasicinfo.setOnClickListener(this);
       // binding.lblRentalRate.setOnClickListener(this);
        binding.lblVehicleImage.setOnClickListener(this);
        binding.lblProfileDetails.setOnClickListener(this);

        binding.header.back.setOnClickListener(this);

       /* new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            if (status) {
                                final JSONObject data = responseJSON.getJSONObject("Data");
                                //rateModel = loginRes.getModel(data.toString(), RateModel.class);
                                //binding.test.ratecode.setText( data.get("RateCode").toString());

                            } else {
                                CustomToast.showToast(getActivity(),responseJSON.getString("Message"),0);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, RATE, BASE_URL_LOGIN, header, params.getRate(26, vehicleModel.VehicleTypeId, 63,
                Helper.setPostDate(DateType.defaultdate, new Date().toString()),
                        Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() + 1000*60*60*24 ).toString())));*/


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lbl_Agreements:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_master_agreement,bundle);
                break;
            case R.id.lbl_Reservation:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_master_reservation, bundle);
                break;

            case R.id.current_rantal:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_current_rental,bundle);
                break;

            case R.id.lbl_basicinfo:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_Vehicles_to_update_vehicle,bundle);
                break;

            case R.id.lbl_rental_rate:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_Vehicles_to_vehicle_specification,bundle);
                break;

            case R.id.lbl_vehicle_image:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_Vehicles_to_vehicle_images,bundle);
                break;

            case R.id.lbl_profileDetails:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_owner,bundle);
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).popBackStack();
                break;
        }
    }
}
