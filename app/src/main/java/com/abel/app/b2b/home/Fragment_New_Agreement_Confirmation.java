package com.abel.app.b2b.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.utils.L;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.DigitConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.SummaryDisplay;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementBinding;
import com.abel.app.b2b.databinding.FragmentNewAgreementConfirmationBinding;
import com.abel.app.b2b.databinding.ListChargesBinding;
import com.abel.app.b2b.databinding.RowSummarryChargeHeadBinding;
import com.abel.app.b2b.databinding.VehicleTaxDetailsBinding;
import com.abel.app.b2b.flexiicar.booking.Fragment_Finalize_Your_Rental;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationOriginDataModels;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationSummaryModels;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONINSERT;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONUPDATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_New_Agreement_Confirmation extends BaseFragment {

    FragmentNewAgreementConfirmationBinding binding;
    ReservationSummaryModels[] charges;
    ReservationSummarry reservationSummarry;
    VehicleModel vehicleModel;
    Customer customer;
    LocationList pickuplocation,returnlocation;
    public static HashMap<Integer, String> activationDetail = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementConfirmationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.btmcharges.layoutPayment.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.screenHeader.setText(companyLabel.Reservation + " " + getResources().getString(R.string.confirmation));
        binding.customer.selectcustomer.setOnClickListener(this);
        reservationSummarry = new ReservationSummarry();
        //binding.btmcharges.currency.setText(Helper.currencySymbol);
/*        apiService = new ApiService(getTaxtDetails, RequestType.GET,
                RESERVATIONGETBYID+"?id="+iid , BASE_URL_LOGIN, header, new JSONObject());*/
        binding.btmcharges.fueltype.setText(Helper.fueltype);
        customer = new Customer();
        vehicleModel = new VehicleModel();
        pickuplocation = new LocationList();
        returnlocation = new LocationList();
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer",(Customer) getArguments().getSerializable("customer"));
        bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
        pickuplocation = (LocationList) getArguments().getSerializable("model");
        returnlocation = (LocationList) getArguments().getSerializable("models");
        customer = (Customer) getArguments().getSerializable("customer");
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        customer = (Customer) getArguments().getSerializable("customer");
        binding.carimage.txtCheckOutLocName.setText(pickuplocation.Name);
        binding.carimage.checkinLocName.setText(returnlocation.Name);
        binding.carimage.txtCheckOutDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
        binding.carimage.checkInDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
        Glide.with(context).load(vehicleModel.DefaultImagePath).into(binding.carimage.VehImage);
        binding.carimage.vehicleName.setText(vehicleModel.VehicleShortName);
        binding.carimage.reservationNumber.setText(reservationSummarry.ReservationNo);
        binding.carimage.carAgr.setText(companyLabel.Reservation + "\n" + customer.CustomerTypeName);
       /* binding.customer.txtFName.setText(customer.FullName);
        binding.customer.txtEMailAdd.setText(customer.Email);
        binding.customer.txtMobileNO.setText(customer.MobileNo);*/
        binding.customer.setCustomer(customer);
        new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

        String rate = loginRes.modeltostring(TAG,reservationSummarry.ReservationRatesModel);
        activationDetail.put(47,rate);
        updateReservationSummarry(47);

        String bussiness = loginRes.modeltostring(TAG,reservationSummarry);
        activationDetail.put(71,bussiness);
        updateReservationSummarry(71);
        Helper.pmt = false;
        /*String insurance = loginRes.modeltostring(TAG,reservationSummarry.ReservationInsuranceModel);
        activationDetail.put(52, insurance);
        updateReservationSummarry(52);*/

        /*String ddd = "{\"CompanyId\":1,\"Name\":\"Business Source One\",\"Code\":\"123\",\"ReservationTypeId\":16,\"AgreementTypeId\":2,\"IsRateSelect\":true,\"RateId\":3,\"IsVehicleCategorySelect\":true,\"VehicleCategoryId\":20,\"IsInsuranceSelect\":true,\"InsuraceCoverId\":1,\"IsInvoiceToSelect\":true,\"InvoiceTo\":2,\"IsReferralSelect\":true,\"ReferralId\":4,\"PromotionalCodeId\":null,\"LocationMasterIds\":\"10, 12, 8, 9\",\"VehicleTypeMasterIds\":\"1, 2, 3\",\"lstBusinessSourceLocationMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"LocationId\":9,\"BusinessSourceName\":null,\"LocationName\":null}],\"lstBusinessSourceVehicleTypeMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"VehicleTypeId\":3,\"BusinessSourceName\":null,\"VehicleTypeName\":null}],\"ReservationTypeName\":\"Online\",\"AgreementTypeName\":\"Insurance\",\"RateName\":\"Base Rate\",\"VehicleCategoryName\":\"In House Fleet\",\"InsuranceCoverName\":\"Basic\",\"InvoiceToName\":null,\"ReferralName\":\"John & Company\",\"fLocationId\":0,\"Id\":4,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":{\"pageSize\":0,\"limit\":0,\"offset\":0,\"orderDir\":null,\"orderBy\":null,\"filter\":null,\"filterObj\":null},\"TotalRecord\":1,\"fIds\":null,\"APIRequestType\":1}";
        // reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(71, ddd));
        // updateReservationSummarry(71,ddd);
        activationDetail.put(71,ddd);
        updateReservationSummarry(71);*/

        /*
                ins decline
                String ins = loginRes.modeltostring(TAG,ReservationDeclineInsuranceModel);
                Log.e(TAG, "declineInsurance: " + ins );
                activationDetail.put(52, ins);
                updateReservationSummarry(52);
                */
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.layout_payment:
                //NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).navigate(R.id.Agreement_Confirmation_to_PaymentOffline);
               // NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).navigate(R.id.Agreement_Confirmation_to_Payment,bundle);
                binding.btmcharges.layoutPayment.setClickable(false);
                if (reservationSummarry.Id != 0){
                    new ApiService2<ReservationSummarry>(doInsertReservation, RequestType.PUT, RESERVATIONUPDATE, BASE_URL_LOGIN, header, reservationSummarry);
                } else {
                    new ApiService2<ReservationSummarry>(doInsertReservation, RequestType.POST, RESERVATIONINSERT, BASE_URL_LOGIN, header, reservationSummarry);
                }
                //NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).navigate(R.id.Agreement_Confirmation_to_Payment,bundle);
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).popBackStack();
                break;

            case R.id.discard:
                Intent intent = new Intent( getActivity(), Activity_Home.class);
                startActivity(intent);
                break;

            case R.id.selectcustomer:
                NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).navigate(R.id.Agreement_to_customer_list,bundle);
                break;
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
                        activationDetail.put(104,getArguments().getString("testSummerry"));
                        updateReservationSummarry(104);
                        charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        bundle.putSerializable("charges", charges);
                        SummaryDisplay summaryDisplay = new SummaryDisplay(getActivity());
                        //summaryDisplay.getDatafrom(charges,)
                        binding.btmcharges.fcurrency.setVisibility(View.GONE);
                        binding.btmcharges.txtMileage.setText(summaryDisplay.getMileage(charges));
                        binding.btmcharges.textviewTotalAmount.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100)), false));
                        for (int i = 0; i <charges.length ; i++) {
                            getSubview(i);
                            RowSummarryChargeHeadBinding rowSummarryChargeHeadBinding = RowSummarryChargeHeadBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                            rowSummarryChargeHeadBinding.getRoot().setId(200 + i);
                            rowSummarryChargeHeadBinding.getRoot().setLayoutParams(subparams);
                            rowSummarryChargeHeadBinding.chargename.setText(charges[i].SummaryName);
                            rowSummarryChargeHeadBinding.charge.setText(Helper.getAmtount( charges[i].TotalAmount,true));

                            if (charges[i].ReservationSummaryType==100){
                                bundle.putString("netrate",DigitConvert.getDoubleDigit(charges[i].ReservationSummaryDetailModels[0].Total));
                            }

                        /*    if (i==1)
                                rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11blue));

                            if (i==3)
                                rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11lightyellow));*/

                            for (int j = 0; j <charges[i].ReservationSummaryDetailModels.length ; j++) {
                                getSubview(j);
                                ListChargesBinding listChargesBinding = ListChargesBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                                listChargesBinding.getRoot().setId(200 + j);
                                listChargesBinding.getRoot().setLayoutParams(subparams);

                                listChargesBinding.textHeader.setText(charges[i].ReservationSummaryDetailModels[j].Title);
                                listChargesBinding.textdetail.setText(charges[i].ReservationSummaryDetailModels[j].Description);
/*
                                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_ellipse_default);
                                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                                if (i == 1) {
                                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.txt11blue));
                                } else {
                                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.txt11lightyellow));
                                }*/

                               // listChargesBinding.icon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.AppBlack)));
                                if (i==0){
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.green));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.green));
                                } else if (i==1) {
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11blue));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.txt11blue));
                                } else if (i==2) {
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11lightyellow));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.txt11lightyellow));
                                } else if (i==3){
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11navyblue));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.txt11navyblue));
                                } else if (i==4) {
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11blue));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.txt11blue));
                                }else if (i==5){
                                    rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11navyblue));
                                    listChargesBinding.icon.setColorFilter(getResources().getColor(R.color.txt11navyblue));
                                }


                                try {
                                if (charges[i].ReservationSummaryDetailModels[j].Title.length() != 0) {
                                    rowSummarryChargeHeadBinding.listsummarry.addView(listChargesBinding.getRoot());
                                    rowSummarryChargeHeadBinding.sumarrydetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (rowSummarryChargeHeadBinding.listsummarry.getVisibility() == View.VISIBLE){

                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.GONE);
                                            } else {
                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            binding.summarry.addView(rowSummarryChargeHeadBinding.getRoot());
                        }
                        JSONObject obj =  resultSet.getJSONObject("ReservationTimeModel");
                        ReservationTimeModel reservationTimeModel = new ReservationTimeModel();
                        reservationTimeModel = loginRes.getModel(obj.toString(), ReservationTimeModel.class);
                        UserReservationData.reservationTimeModel = reservationTimeModel;

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

    OnResponseListener doInsertReservation = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {

                try {


                    Log.d("TAG", "onSuccess: " + response);
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,0);

                        JSONObject data = responseJSON.getJSONObject("Data");
                        reservationSummarry.Id =data.getInt("Id");
                        bundle.putSerializable("reservationSum",reservationSummarry );
                        loginRes.testingLog(TAG,reservationSummarry);
                        NavHostFragment.findNavController(Fragment_New_Agreement_Confirmation.this).navigate(R.id.Agreement_Confirmation_to_Payment,bundle);
                    }  else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                        binding.btmcharges.layoutPayment.setClickable(true);
                    }

                } catch (Exception e)
                {
                    binding.btmcharges.layoutPayment.setClickable(true);
                    e.printStackTrace();
                }
            });

        }

        @Override
        public void onError(String error) {
            Log.d("TAG", "onError: " + error);
            binding.btmcharges.layoutPayment.setClickable(true);
        }
    };


    public void updateReservationSummarry(int key){
        int data = reservationSummarry.ReservationOriginDataModels.size();
        for (int i = 0; i <data; i++) {
            if (reservationSummarry.ReservationOriginDataModels.get(i).TableType == key) {
                reservationSummarry.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reservationSummarry.ReservationOriginDataModels.add(new ReservationOriginDataModels(key, activationDetail.get(key))); // activationDetail.get(52).toString();
    }
}
