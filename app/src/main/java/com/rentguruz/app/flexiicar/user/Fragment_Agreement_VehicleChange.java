package com.rentguruz.app.flexiicar.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAgreementVehiclechangeBinding;
import com.rentguruz.app.model.reservation.ReservationVehicleChange;
import com.rentguruz.app.model.reservation.ReservationVehicleType;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationOriginDataModels;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLEVEHICLETYPE;
import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLEVICHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.BusinessSourceMaster;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONVEHICLECHANGE;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Agreement_VehicleChange extends BaseFragment {

    FragmentAgreementVehiclechangeBinding binding;
    Reservation reservations;
    SummaryDisplay summaryDisplay;
    private static Boolean changeDate = false;
    ReservationSummarry  reservationSummarry;
    ReservationSummarry  changereservationSummarry;
    List<ReservationVehicleType> vehicleTypes;
    int vehicleTypeId, vehicecleId;
    ReservationVehicleChange reservationVehicleChange;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementVehiclechangeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vehicleTypes = new ArrayList<>();
        binding.setUiColor(UiColor);
        changeDate = false;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reservationVehicleChange = new ReservationVehicleChange();
        changereservationSummarry = new ReservationSummarry();

        binding.header.screenHeader.setText(getResources().getString(R.string.vehiclechange));
        binding.header.back.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        reservationSummarry = new ReservationSummarry();
        summaryDisplay = new SummaryDisplay(getActivity());
        try {
            reservations = new Reservation();
            reservations = (Reservation) getArguments().getSerializable("reservation");

            binding.reservation.setReservation(reservations);


            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");
            changereservationSummarry = reservationSummarry;
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

                                String vehicle = getReservationList.getString("VehicleTypeMasterIds");
                                JSONArray vehicleId = new JSONArray();

                                for (int i = 0; i < vehicle.split(",").length; i++) {
                                    vehicleId.put(Integer.valueOf(vehicle.split(",")[i].trim()));
                                }
                                // loginRes.storedata("VehicleTypeMasterIds", String.valueOf(vehicleId));
                                loginRes.storedata("VehicleTypeMasterIds", vehicle);
                                JSONArray vehicletype = new JSONArray();
                                for (int i = 0; i < vehicle.split(",").length; i++) {
                                    vehicletype.put(Integer.valueOf(vehicle.split(",")[i].trim()));
                                }
                                new ApiService(getVehicleTypeList,RequestType.POST,AVAILABLEVEHICLETYPE, BASE_URL_LOGIN, header,
                                        params.getVehicleType(reservationSummarry.PickUpLocation,
                                                reservationSummarry.CheckOutDate,
                                                reservationSummarry.CheckInDate
                                                , vehicletype));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, BusinessSourceMaster, BASE_URL_LOGIN, header, params.getBusinessSource(reservationSummarry.BusinessSourceId));
            new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
        } catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                reservationVehicleChange.IsNoExtraCharge = true;
                reservationVehicleChange.SendNotificationToCustomer = false;
                reservationVehicleChange.ReservationId = reservationSummarry.Id;
                reservationVehicleChange.VehicleId = reservationSummarry.ReservationVehicleModel.VehicleId;
                reservationVehicleChange.VehicleTypeId = reservationSummarry.ReservationVehicleModel.VehicleTypeId;
                reservationVehicleChange.ReservationOriginDataModels.add(new ReservationOriginDataModels(36,loginRes.modeltostring(TAG,reservationSummarry)));

                new ApiService2(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try {
                                    System.out.println("Success");
                                    System.out.println(response);

                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");

                                    if (status)
                                    {
                                        NavHostFragment.findNavController(Fragment_Agreement_VehicleChange.this).popBackStack();
                                    } else {
                                        CustomToast.showToast(getActivity(),responseJSON.getString("Message"), 1);
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
                }, RequestType.POST, RESERVATIONVEHICLECHANGE, BASE_URL_LOGIN, header, reservationVehicleChange);

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_VehicleChange.this).popBackStack();
                break;
            /*case R.id.next:
                NavHostFragment.findNavController(Fragment_Agreement_VehicleChange.this).navigate(R.id.test);
                break;*/
        }
    }


    OnResponseListener SummaryCharge = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray summarry = resultSet.getJSONArray("ReservationSummaryModels");
                        JSONObject time = resultSet.getJSONObject("ReservationTimeModel");
                        ReservationTimeModel reservationTimeModel = (ReservationTimeModel) loginRes.getModel(time.toString(), ReservationTimeModel.class);
                        ReservationSummaryModels[] charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);

                        if (changeDate) {
                            try {
                                binding.second.mileage.setText(summaryDisplay.getMileage(charges));
                                binding.second.days.setText(reservationTimeModel.TotalDays + " Days");
                                binding.second.totalamt.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100)), false));
                                binding.second.paymentmade.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 103)), false));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                changeDate = true;
                                binding.first.mileage.setText(summaryDisplay.getMileage(charges));
                                binding.first.days.setText(reservationTimeModel.TotalDays + " Days");
                                binding.first.totalamt.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100)), false));
                                binding.first.paymentmade.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 103)), false));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    OnResponseListener getVehicleTypeList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        JSONObject responseJSON = new JSONObject(response);
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray data = resultSet.getJSONArray("ListOfVehicleType");
                        ReservationVehicleType[] reservationVehicleType;
                        reservationVehicleType = loginRes.getModel(data.toString(),ReservationVehicleType[].class);
                        int len = reservationVehicleType.length;
                        for (int i = 0; i <len ; i++) {
                            vehicleTypes.add(reservationVehicleType[i]);
                        }

                        setSpinner();

                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "onError: " + error );
        }
    };

    public void setSpinner(){
        List<String> vehicleTyp = new ArrayList<>();
        for (int i = 0; i <vehicleTypes.size() ; i++) {
            vehicleTyp.add(vehicleTypes.get(i).Name);
           if (reservationSummarry.ReservationVehicleModel.VehicleTypeId == vehicleTypes.get(i).Id){
                vehicleTypeId = i;
           }
        }
        ArrayAdapter<String> vehicletypeadapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleTyp);
        binding.vehicletype.setAdapter(vehicletypeadapter);
        binding.vehicletype.setSelection(vehicleTypeId);

        binding.vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleTypeId = position;
                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    try {
                                        JSONObject responseJSON = new JSONObject(response);
                                        Boolean status = responseJSON.getBoolean("Status");

                                        if (status)
                                        {
                                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                                            JSONArray VehicleList = resultSet.getJSONArray("ListOfVehicle");
                                            VehicleModel[] vehicleModel = loginRes.getModel(VehicleList.toString(), VehicleModel[].class);
                                            List<String> vehicledata = new ArrayList<>();
                                            for (int i = 0; i <vehicleModel.length; i++) {
                                                vehicledata.add(vehicleModel[i].VehicleName);
                                                if (reservationSummarry.ReservationVehicleModel.VehicleTypeId == vehicleTypes.get(i).Id){
                                                    vehicecleId = i;
                                                }
                                            }

                                            ArrayAdapter<String> vehicleidadapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicledata);
                                            binding.vehiclelist.setAdapter(vehicleidadapter);
                                            binding.vehiclelist.setSelection(vehicecleId);
                                            changereservationSummarry.ReservationVehicleModel.VehicleTypeId = vehicleTypes.get(vehicecleId).Id;
                                            int vehid;
                                            for (int i = 0; i <vehicleModel.length ; i++) {
                                                if (vehicleModel[i].VehicleName.trim().equals(binding.vehiclelist.getSelectedItem().toString().trim())){
                                                    changereservationSummarry.ReservationVehicleModel.VehicleId = vehicleModel[i].Id;
                                                }
                                            }

                                            new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, changereservationSummarry);


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
                }, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                        params.getVechicleList(reservationSummarry.PickUpLocation, reservationSummarry.CheckOutDate,
                                reservationSummarry.CheckInDate, vehicleTypes.get(vehicleTypeId).Id, reservationSummarry.ReservationVehicleModel.VehicleId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}