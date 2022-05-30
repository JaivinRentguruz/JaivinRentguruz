package com.rentguruz.app.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementDriverBinding;
import com.rentguruz.app.databinding.ListAdditionalDriverDataBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RIchauffer;
import com.rentguruz.app.model.response.ReservationChargesModels;
import com.rentguruz.app.model.response.ReservationDriversModel;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.UpdateDL;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETLICENSEALL;
import static com.rentguruz.app.apicall.ApiEndPoint.MISCCHARGESSINGLE;

public class Fragment_New_Agreement_Driver extends BaseFragment {

    FragmentNewAgreementDriverBinding binding;
    ReservationSummarry reservationSummarry;
    ReservationTimeModel reservationTimeModel;
    VehicleModel vm;
    LocationList pickuploc;
    Customer customer;
    RIchauffer drivercharge;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementDriverBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.add.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.screenHeader.setText("Additional Driver");
        binding.setUiColor(UiColor);
        drivercharge = new RIchauffer();
        reservationSummarry = new ReservationSummarry();
        vm = new VehicleModel();
        reservationTimeModel = new ReservationTimeModel();
        pickuploc = new LocationList();
        customer = new Customer();
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
        bundle.putBoolean("updateLic", false);
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        vm = (VehicleModel) getArguments().getSerializable("Model");
        reservationTimeModel = (ReservationTimeModel) getArguments().getSerializable("timemodel");
        pickuploc =(LocationList) getArguments().getSerializable("model");
        customer =(Customer) getArguments().getSerializable("customer");

        apiService = new ApiService(GetDrivingLicense, RequestType.POST,
                GETLICENSEALL, BASE_URL_LOGIN, header, params.getDrivingLicenseList(customer.Id));

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                NavHostFragment.findNavController(Fragment_New_Agreement_Driver.this).navigate(R.id.driver_to_drivercreate,bundle);
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_New_Agreement_Driver.this).popBackStack();
                break;
        }
    }

    OnResponseListener GetDrivingLicense = new OnResponseListener() {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray getdrivingLicense = resultSet.getJSONArray("Data");
                        UpdateDL[] updateDLS;
                        updateDLS =  loginRes.getModel(getdrivingLicense.toString(), UpdateDL[].class);

                        for (int i = 0; i <updateDLS.length ; i++) {
                            getSubview(i);
                            ListAdditionalDriverDataBinding listAdditionalDriverDataBinding = ListAdditionalDriverDataBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            listAdditionalDriverDataBinding.getRoot().setId(200 + i);
                            listAdditionalDriverDataBinding.getRoot().setLayoutParams(subparams);
                            listAdditionalDriverDataBinding.setDriver(updateDLS[i]);
                            listAdditionalDriverDataBinding.setUiColor(UiColor);
                            binding.rlInsurancePolicyList.addView(listAdditionalDriverDataBinding.getRoot());
                            int finalI = i;
                           /* listAdditionalDriverDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reservationSummarry.ReservationDriversModel.add(new ReservationDriversModel(updateDLS[finalI].Id));
                                    bundle.putSerializable("reservationSum",reservationSummarry);
                                    UserData.updateDL = updateDLS[finalI];
                                    new ApiService(additionalDriver, RequestType.POST, MISCCHARGESSINGLE, BASE_URL_LOGIN, header, params.getAdditionalDriver(reservationSummarry.PickUpLocation, reservationSummarry.ReservationVehicleModel.VehicleTypeId));
                                }
                            });*/

                            listAdditionalDriverDataBinding.updateDriver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserData.updateDL = updateDLS[finalI];
                                    bundle.putBoolean("updateLic", true);
                                    NavHostFragment.findNavController(Fragment_New_Agreement_Driver.this).navigate(R.id.driver_to_drivercreate,bundle);
                                }
                            });

                            listAdditionalDriverDataBinding.selectDriver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reservationSummarry.ReservationDriversModel.add(new ReservationDriversModel(updateDLS[finalI].Id));
                                    bundle.putSerializable("reservationSum",reservationSummarry);
                                    UserData.updateDL = updateDLS[finalI];
                                    new ApiService(additionalDriver, RequestType.POST, MISCCHARGESSINGLE, BASE_URL_LOGIN, header, params.getAdditionalDriver(reservationSummarry.PickUpLocation, reservationSummarry.ReservationVehicleModel.VehicleTypeId));
                                }
                            });

                        }


                    } else {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(), errorString, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };


    OnResponseListener additionalDriver =new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        try {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        Log.e(TAG, "run: " + resultSet );
                        drivercharge = loginRes.getModel(resultSet.toString(), RIchauffer.class);
                        drivercharge.TotalUnit = 1;
                        reservationSummarry.MiscellaneousChargeModels.add(drivercharge);
                            ReservationChargesModels chargesModels = new ReservationChargesModels();
                            chargesModels.ChargeFor = drivercharge.DetailId;
                            chargesModels.Amount = drivercharge.Amount;
                            //(MiscellaneousCharges = 1, TaxCharges = 2

                            chargesModels.ChargeType = 1;
                            /*
                            chargesModels.AmountType = 1;
                            chargesModels.IsTaxable = false;
                            */
                        reservationSummarry.ReservationChargesModels.add(chargesModels);
                        bundle.putSerializable("reservationSum",reservationSummarry);
                        NavHostFragment.findNavController(Fragment_New_Agreement_Driver.this).navigate(R.id.driver_to_booking,bundle);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {

        }
    };
}
