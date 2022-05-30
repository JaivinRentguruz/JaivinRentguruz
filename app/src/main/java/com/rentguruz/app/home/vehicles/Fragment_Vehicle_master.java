package com.rentguruz.app.home.vehicles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleMasterBinding;
import com.rentguruz.app.model.DoVehicle;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWN;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEGETBYID;

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
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(companyLabel.Vehicle + " Master");
        binding.setLabel(companyLabel);
        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);
        bundle.putInt("Idd",getArguments().getInt("Idd"));
        bundle.putInt("Idd", vehicleModel.Id);
        CustomBindingAdapter.loadImage(binding.carImgview, vehicleModel.DefaultImagePath);
        CustomBindingAdapter.camelcase(binding.carname,vehicleModel.VehicleName);
        binding.vehicleInspection.setText(companyLabel.Vehicle + " Inspection");
        binding.vehicleLicence.setText(companyLabel.Vehicle + " " + companyLabel.License + " " + companyLabel.Certification);
        binding.lblaccountStatement.setOnClickListener(this);
        binding.lblReservation.setOnClickListener(this);
        binding.lblAgreements.setOnClickListener(this);
        binding.currentRantal.setOnClickListener(this);
        binding.lblBasicinfo.setOnClickListener(this);
        binding.lblRentalRate.setOnClickListener(this);
        binding.lblVehicleImage.setOnClickListener(this);
        binding.lblProfileDetails.setOnClickListener(this);
        binding.lblActivityTimeline.setOnClickListener(this);
        binding.inspection.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);

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
        String string = "?id=" + vehicleModel.Id + "&isActive=false";

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                            vehicleModel = loginRes.getModel(getReservationList.toString(),VehicleModel.class);
                            DoVehicle vehicle = loginRes.getModel(getReservationList.toString(),DoVehicle.class);
                            bundle.putSerializable("Model", vehicleModel);
                            bundle.putSerializable("Models", vehicle);
                            loginRes.testingLog(TAG,vehicleModel);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.GET, VEHICLEGETBYID, BASE_URL_LOGIN, header, string);

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
               // NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_current_rental,bundle);
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_licence_certificate,bundle);
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

            case R.id.lblActivity_timeline:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_master_timeline,bundle);
                break;

            case R.id.lblaccount_statement:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_master_statment,bundle);
                    break;

            case R.id.inspection:
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicle_inspection,bundle);
                break;

            case R.id.discard:
            case R.id.back:
                //NavHostFragment.findNavController(Fragment_Vehicle_master.this).popBackStack();
                NavHostFragment.findNavController(Fragment_Vehicle_master.this).navigate(R.id.action_vehicle_master_to_vehicles);
                break;
        }
    }
}
