package com.abel.app.b2b.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.model.insert.EquipmentDetailModels;
import com.abel.app.b2b.model.reservation.ReservationEquipment;
import com.abel.app.b2b.model.response.RIequipment;
import com.bumptech.glide.Glide;
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
import com.abel.app.b2b.databinding.DetailInsuranceBinding;
import com.abel.app.b2b.databinding.FragmentNewAgreementBookingBinding;
import com.abel.app.b2b.flexiicar.user.Fragment_Customer_Profile;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.InsuranceCompanyDetailsModel;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.reservation.ReservationInsurance;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationOriginDataModels;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationSummaryModels;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.UpdateDL;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETCUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSURANCECOVER;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_New_Agreement_booking extends BaseFragment {

    FragmentNewAgreementBookingBinding binding;
    VehicleModel vehicleModel;
    Customer customer;
    CustomerProfile customerProfile;
    ReservationSummarry reservationSummarry;
    ReservationSummaryModels[] charges;
    ReservationTimeModel reservationTimeModel;
    public static HashMap<Integer, String> activationDetail = new HashMap<>();
    public static Boolean isDefaultins;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.insurance.setOnClickListener(this);
        binding.addtionalDriver.setOnClickListener(this);
        binding.refferrels.setOnClickListener(this);
        binding.equipment.setOnClickListener(this);
        binding.notes.setOnClickListener(this);
        binding.inventory.setOnClickListener(this);
        binding.billing.setOnClickListener(this);
        binding.flight.setOnClickListener(this);
        binding.btm.layoutPayment.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.screenHeader.setText(companyLabel.Reservation);
        binding.header.discard.setOnClickListener(this);
        binding.customer.selectcustomer.setOnClickListener(this);
        customerProfile = new CustomerProfile();
        reservationSummarry = new ReservationSummarry();
        reservationTimeModel = new ReservationTimeModel();
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
        bundle.putString("reservationInsurances",getArguments().getString("reservationInsurances"));
       // binding.fueltype.setText(Helper.fueltype);
        vehicleModel = new VehicleModel();
        vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
        LocationList pickuplocation = new LocationList();
        pickuplocation = (LocationList) getArguments().getSerializable("model");

        LocationList returnlocation = new LocationList();
        returnlocation = (LocationList) getArguments().getSerializable("models");
        binding.setLabel(companyLabel);
        customer = new Customer();
        customer = (Customer) getArguments().getSerializable("customer");
        reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationSum");
        String string = "?id=" + customer.Id;
       /* binding.customer.txtFName.setText(customer.FullName);
        binding.customer.txtEMailAdd.setText(customer.Email);
        binding.customer.txtMobileNO.setText(customer.MobileNo);*/
        binding.customer.setCustomer(customer);
        reservationTimeModel = (ReservationTimeModel) getArguments().getSerializable("timemodel");

        binding.carimage.txtCheckOutLocName.setText(pickuplocation.Name);
        binding.carimage.checkinLocName.setText(returnlocation.Name);
        binding.carimage.txtCheckOutDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + ","  + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
        binding.carimage.checkInDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
        Glide.with(context).load(vehicleModel.DefaultImagePath).into(binding.carimage.VehImage);
        binding.carimage.vehicleName.setText(vehicleModel.VehicleShortName);
        binding.carimage.reservationNumber.setText(reservationSummarry.ReservationNo);
        //binding.currency.setText(Helper.currencySymbol);
        binding.carimage.carAgr.setText(companyLabel.Reservation + "\n" + customer.CustomerTypeName);

        new ApiService(GetCustomerProfile, RequestType.GET, GETCUSTOMER,BASE_URL_CUSTOMER, header,string);
        binding.btm.text.setText(getResources().getString(R.string.next));




        if (Helper.defaultInsurance) {
            apiService = new ApiService(getInsurance, RequestType.POST, INSURANCECOVER, BASE_URL_LOGIN, header, params.getInsuranceCover(vehicleModel.VehicleTypeId, reservationTimeModel.TotalDays, pickuplocation.Id));
        } else {
            if (reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline){
                //binding.customerInsurance.setText(reservationSummarry.ReservationInsuranceModel.InsuranceDetailsModel.PolicyNo);
                binding.customerInsurance.setText(UserData.insuranceCompanyDetailsModel.Name + " | " + UserData.insuranceModel.Deductible);
            } else {
                binding.customerInsurance.setText(reservationSummarry.ReservationInsuranceModel.Name + " | " +reservationSummarry.ReservationInsuranceModel.InsuranceDetailsModel.Deductible );
            }
            new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
        }
        if (Helper.flightDetail)
            binding.flightDetails.setText(reservationSummarry.ReservationFlightAndHotelModel.ArrFlightNo + " | " + reservationSummarry.ReservationFlightAndHotelModel.ArrFlightDate);

        try {
           if (Fragment_Selected_Location.reservationBusinessSource.ReferralId > 0){
               binding.referal.setVisibility(View.VISIBLE);
               binding.referalLine.setVisibility(View.VISIBLE);

           }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
           // binding.additionalDriver.setText(reservationSummarry.ReservationDriversModel.get(0).)
           if (UserData.updateDL.FullName!= null){
               binding.additionalDriver.setText(UserData.updateDL.FullName);
           } else {
              // binding.additionalDriver.setText("Additional Driver is Not Selected");
               binding.additionalDriver.setText("Not Selected");
           }
           if (UserData.billingdetail!= null) {
               binding.bilinginfo.setText(UserData.billingdetail);
           } else {
               binding.bilinginfo.setText(customer.FullName);
           }
            //binding.bilinginfo.setText(UserData.billingdetail);
            //binding.equipmentname.setText();
            if (reservationSummarry.ReservationNoteModel.ExternalNote != null) {
                binding.notedata.setText(reservationSummarry.ReservationNoteModel.ExternalNote);
            } else {
               // binding.notedata.setText(companyLabel.SpecialNote +" is not Inserted");
                binding.notedata.setText("Not Inserted");
            }
            if (reservationSummarry.ReservationFlightAndHotelModel.FlightBookingReference!= null) {
                binding.flightDetails.setText(reservationSummarry.ReservationFlightAndHotelModel.FlightBookingReference + " " + reservationSummarry.ReservationFlightAndHotelModel.HotelName);
            } else {
               // binding.flightDetails.setText(companyLabel.FlightDetails    +" is not Inserted");
                binding.flightDetails.setText("Not Inserted");
            }

            if (reservationSummarry.ReservationEquipmentInventoryModel.size() == 0){
                //binding.equipmentname.setText(companyLabel.Equipment + " is not Selected");
                binding.equipmentname.setText("Not Selected");
            } else {
                String equip = "";
                //for (int i = 0; i <reservationSummarry.ReservationEquipmentInventoryModel.size(); i++) {
                    Iterator iterator = reservationSummarry.ReservationEquipmentInventoryModel.iterator();
                    while (iterator.hasNext()){
                        RIequipment rIequipment = new RIequipment();
                        rIequipment = (RIequipment) iterator.next();
                        for (int j = 0; j < UserData.equipment.length; j++) {
                            if (rIequipment.EquipInventId == UserData.equipment[j].Id){
                                equip += UserData.equipment[j].Name + " ";
                            }
                        }
                    }
                //}
                binding.equipmentname.setText(equip);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insurance:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_insurance,bundle);
                break;

            case R.id.addtionalDriver:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_driver,bundle);
                break;

            case R.id.refferrels:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_refferrels,bundle);
                break;

            case R.id.equipment:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_equipment,bundle);
                break;

            case R.id.inventory:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_inventory,bundle);
                break;

            case R.id.notes:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_notes,bundle);
                break;

            case R.id.flight:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_flight,bundle);
                break;

            case R.id.billing:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_billing_info,bundle);
                break;

            //case R.id.confirm:
            case R.id.layout_payment:
                if (validation()){
                    NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_Agreement_Confirmation,bundle);
                }
                break;
            case R.id.selectcustomer:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_New_Agreement_booking.this).navigate(R.id.booking_to_CustomerList,bundle);
                break;

            case R.id.discard:
                Intent intent = new Intent( getActivity(), Activity_Home.class);
                startActivity(intent);
                break;

        }
    }

    OnResponseListener GetCustomerProfile = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        try
                        {
                            // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONObject custommer= responseJSON.getJSONObject("Data");
                            Log.e(TAG, "run: " + custommer );
                            customerProfile = loginRes.getModel(custommer.toString(), CustomerProfile.class);
                            bundle.putSerializable("customerDetail", customerProfile);
                            UserData.customerProfile = customerProfile;
                            reservationSummarry.IsTaxExemption = customerProfile.IsTaxExemption;
                            bundle.putSerializable("reservationSum", reservationSummarry);
                          /*  String insDetail = customerProfile.InsuranceDetailsModel.InsuranceCompanyDetailsModel.Name + " | " ;
                            binding.customerInsurance.setText(insDetail);
                            String insDetail1 = customerProfile.InsuranceDetailsModel.PolicyNo + " | " ;
                            binding.customerInsurance.setText(insDetail+ insDetail1);
                            String insDetail2 = Helper.getDateDisplay(DateType.fulldate,customerProfile.InsuranceDetailsModel.ExpiryDate);
                            binding.customerInsurance.setText(insDetail+ insDetail1+insDetail2);*/
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

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
                        bundle.putString("testSummerry", summarry.toString());
                        charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        bundle.putSerializable("charges", charges);
                        fullProgressbar.hide();
                        for (int i = 0; i <charges.length ; i++) {
                            if (charges[i].ReservationSummaryType==1){
                                for (int j = 0; j <charges[i].ReservationSummaryDetailModels.length ; j++) {
                                    if (charges[i].ReservationSummaryDetailModels[j].ReservationSummaryDetailType == 101)
                                        if (charges[i].ReservationSummaryDetailModels[j].Total == 0) {
                                            binding.btm.txtMileage.setText(charges[i].ReservationSummaryDetailModels[j].Description.trim());
                                           // binding.txtMileage.setTextSize(22);
                                        } else{
                                            binding.btm.txtMileage.setText(Helper.getDistance(charges[i].ReservationSummaryDetailModels[j].Total));
                                           // binding.txtMileage.setTextSize(22);
                                        }
                                }
                                // binding.txtMileage.setText(String.valueOf(charges[i].ReservationSummaryDetailModels[1].Total));
                            }
                            if (charges[i].ReservationSummaryType==100){
                                binding.btm.textviewTotalAmount.setText(DigitConvert.getDoubleDigit(charges[i].ReservationSummaryDetailModels[0].Total));
                            }
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
            CustomToast.showToast(getActivity(),error,1);
        }
    };

    OnResponseListener getInsurance = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {

            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    ReservationInsurance[] reservationInsurances;
                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        reservationInsurances = loginRes.getModel(array.toString(), ReservationInsurance[].class);
                        //bundle.putSerializable("reservationInsurances",reservationInsurances);
                        loginRes.storedata("reservationInsurances", resultSet.toString());
                        bundle.putString("reservationInsurances",resultSet.toString());
                        isDefaultins =  getDatafrom(reservationInsurances);
                   /*     InsuranceModel insuranceModel = new InsuranceModel();
                        InsuranceCompanyDetailsModel insuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();
                        loginRes.storedata("insurance", resultSet.toString() );
                        insuranceModel = loginRes.callFriend("insurance", InsuranceModel.class);
                        JSONObject jsonObject = resultSet.getJSONObject("InsuranceCompanyDetailsModel");
                        loginRes.storedata("insurance1", jsonObject.toString());
                        insuranceCompanyDetailsModel = loginRes.callFriend("insurance1", InsuranceCompanyDetailsModel.class);*/

                        for (int i = 0; i <reservationInsurances.length ; i++) {
                            if (reservationInsurances[i].IsSelected){
                                isDefaultins = true;
                                binding.customerInsurance.setText(reservationInsurances[i].Name);
                                JSONObject obj = array.getJSONObject(i);
                                reservationSummarry.ReservationInsuranceModel.IsSureInsurance = false;
                                reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = false;
                                reservationSummarry.ReservationInsuranceModel.NoOfDays = reservationTimeModel.TotalDays;
                                reservationSummarry.ReservationInsuranceModel.Remarks = reservationInsurances[i].Description;
                                reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId = reservationInsurances[i].DetailId;
                                reservationSummarry.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));

                                reservationSummarry.ReservationInsuranceModel.Name = reservationInsurances[i].Name;

                                activationDetail.put(52, obj.toString());
                                updateReservationSummarry(52);
                            }
                        }

                        new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
                    }
                    else
                    {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                        fullProgressbar.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    fullProgressbar.hide();
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
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

    private Boolean validation(){
        Boolean value = false;
        if (isDefaultins){
            value = true;
        } else {
            CustomToast.showToast(getActivity(),"Please Select Insurance", 1);
        }


        return value;
    }

    public Boolean getDatafrom(ReservationInsurance[] reservationInsurances){
        Boolean value = false;

        for (ReservationInsurance d:reservationInsurances ) {
            if (d.IsSelected == true){
                value = true;
            }
        }

        return value;
    }
}
