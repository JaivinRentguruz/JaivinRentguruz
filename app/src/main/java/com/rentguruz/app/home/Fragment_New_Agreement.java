package com.rentguruz.app.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.DigitConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.base.UserReservationData;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.parameter.enums.RecurringRsvBillingCycle;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RateModel;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.RANDUMNUMBERS;
import static com.rentguruz.app.apicall.ApiEndPoint.RATE;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;
import static com.rentguruz.app.model.parameter.enums.RecurringRsvBillingCycle.*;

public class Fragment_New_Agreement extends BaseFragment {

    FragmentNewAgreementBinding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    List<OnDropDownList> data;
    Handler handler=new Handler(Looper.getMainLooper());
    int idd;
    VehicleModel vehicleModel;
    LocationList pickuplocation = new LocationList();
    LocationList returnlocation = new LocationList();
    RateModel rateModel = new RateModel();
    ReservationTimeModel timeModel = new ReservationTimeModel();
    ReservationSummarry reservationSummarry;
    ReservationSummaryModels[] charges;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       /* getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
       /* if (binding.leftscreen.sw.isChecked()){
            binding.leftscreen.sw.setThumbTintList(ColorStateList.valueOf(Color.parseColor(UiColor.primary)));
        }*/

        binding.leftscreen.customeRate.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);
        binding.leftscreen.promocode.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);
        binding.leftscreen.unlimitedMiles.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);
        binding.leftscreen.fixrate.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);

        binding.setUiColor(UiColor);
        //binding.test.setUiColor(UiColor);
        //binding.leftscreen.setUiColor(UiColor);
       // binding.test.carimage.setUiColor(UiColor);
        data = new ArrayList<>();
        binding.test.header.screenHeader.setText(companyLabel.Reservation);
        binding.test.editRate.setOnClickListener(this);
        binding.test.btm.layoutPayment.setOnClickListener(this);
        binding.test.header.back.setOnClickListener(this);
        binding.test.header.discard.setOnClickListener(this);
        binding.leftscreen.apply.setOnClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), binding.slider,R.string.nav_open,R.string.nav_close);
        binding.slider.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        binding.slider.closeDrawer(GravityCompat.END);
        //binding.test.fueltype.setText(Helper.fueltype);
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        vehicleModel = new VehicleModel();
        vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
        pickuplocation = (LocationList) getArguments().getSerializable("model");
        returnlocation = (LocationList) getArguments().getSerializable("models");
        timeModel = (ReservationTimeModel) getArguments().getSerializable("timemodel");
        bundle.putSerializable("timemodel", timeModel);
        binding.test.carimage.txtCheckOutLocName.setText(pickuplocation.Name);
        binding.test.carimage.checkinLocName.setText(returnlocation.Name);
        reservationSummarry = new ReservationSummarry();
        reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationSum");
        Log.e(TAG, "onViewCreated:p " + getArguments().getString("pickupdate")  + " * " +  getArguments().getString("dropdate") );
        Log.e(TAG, "onViewCreated:g " + reservationSummarry.CheckInDate  + " * " +  reservationSummarry.CheckOutDate  );
        new ApiService(getReservationNumber, RequestType.GET,RANDUMNUMBERS,BASE_URL_LOGIN, header, "?length=8");
        binding.leftscreen.setLabel(companyLabel);

        /*binding.txtPickupdate1.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")));
        binding.txtPICKUPTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
        binding.txtREturnDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")));
        binding.txtREturnTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));*/

        binding.test.carimage.txtCheckOutDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + " , " + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
        binding.test.carimage.checkInDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + " , " + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
        Glide.with(context).load(vehicleModel.DefaultImagePath).into(binding.test.carimage.VehImage);
        binding.test.carimage.vehicleName.setText(vehicleModel.VehicleShortName);
        binding.test.carimage.carAgr.setText(companyLabel.Reservation + "\n" + "Retail");
 /*       binding.test.txtcheckOutLocationName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                view.setSystemUiVisibility(uiOptions);
            }
        });*/

        //{"TableType":8,"CompanyId":1,"IsActive":true,"IsJoinApplied":true,"VehicleTypeIds":[1],"LocationIds":[8]}

        JSONObject object = new JSONObject();
        JSONArray VehicleTypeIds = new JSONArray();
        JSONArray LocationIds = new JSONArray();
        try
        {
            object.accumulate("TableType", 8);
            object.accumulate("CompanyId", Helper.id);
            object.accumulate("IsActive", true);
            object.accumulate("IsJoinApplied", true);
            VehicleTypeIds.put(vehicleModel.VehicleTypeId);
            object.accumulate("VehicleTypeIds", VehicleTypeIds);
            LocationIds.put(pickuplocation.Id);
            object.accumulate("LocationIds",LocationIds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                            OnDropDownList[] onDropDownLists;
                            List<String> strings = new ArrayList<>();

                            onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);

                            for (int i = 0; i <onDropDownLists.length; i++) {
                                OnDropDownList onDropDownList = new OnDropDownList();
                                onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                data.add(onDropDownList);
                            }
                            listSpinner(data);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, object);

        /*binding.test.txtcheckOutLocationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

     //   new ApiService(getReservationNumber, RequestType.GET,RANDUMNUMBERS,BASE_URL_LOGIN, header, "?length=8&prefix=8");

        try {
            binding.test.currency1.setText(Helper.displaycurrency);
            binding.test.currency2.setText(Helper.displaycurrency);
            binding.test.currency3.setText(Helper.displaycurrency);
            binding.test.currency4.setText(Helper.displaycurrency);
            binding.test.currency5.setText(Helper.displaycurrency);
            binding.test.currency6.setText(Helper.displaycurrency);
            binding.test.currency7.setText(Helper.displaycurrency);
            //binding.test.currency.setText(Helper.currencySymbol);
            binding.leftscreen.currency8.setText(Helper.displaycurrency);
            binding.leftscreen.currency9.setText(Helper.displaycurrency);

            binding.leftscreen.mi.setText(Helper.fueel);
            binding.leftscreen.mileages.setText(Helper.fueel + " " + "Allowance");
            binding.leftscreen.mileagess.setText("Unlimited "+Helper.fueel);
            binding.leftscreen.dep.setText(UserData.loginResponse.CompanyLabel.Deposit);
            binding.leftscreen.dep1.setText("Waive "+UserData.loginResponse.CompanyLabel.Deposit);
            binding.test.btm.text.setText(getResources().getString(R.string.next));
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.leftscreen.fixrate.setChecked(true);
        binding.leftscreen.fixrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getChecked();
                /*if (isChecked){
                    binding.leftscreen.fixratedetail.setVisibility(View.GONE);
                } else {
                    binding.leftscreen.fixratedetail.setVisibility(View.VISIBLE);
                }*/
            }
        });
        binding.leftscreen.unlimitedMiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getChecked();
               /* if (isChecked){
                    binding.leftscreen.unlimitedMilesdetail.setVisibility(View.GONE);
                } else {
                    binding.leftscreen.unlimitedMilesdetail.setVisibility(View.VISIBLE);
                }*/
            }
        });
        binding.leftscreen.promocode.setChecked(true);
        binding.leftscreen.promocode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getChecked();
                    binding.leftscreen.promocode.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);
                }else {
                    binding.leftscreen.customeRate.setChecked(true);
                    getCustomeRate();
                    binding.leftscreen.promocode.getBackground().setColorFilter(Color.parseColor(UiColor.secondary), PorterDuff.Mode.SCREEN);
                }
               /* if (isChecked){
                    binding.leftscreen.promocodedetail.setVisibility(View.GONE);
                } else {
                    binding.leftscreen.promocodedetail.setVisibility(View.VISIBLE);
                }*/
            }
        });

        binding.leftscreen.customeRate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getCustomeRate();
                } else {
                    binding.leftscreen.promocode.setChecked(true);
                    getChecked();
                }
            }
        });

        getChecked();

        if (Fragment_Selected_Location.lease){
            binding.test.lease.setVisibility(View.VISIBLE);
            binding.test.returntype.getBackground().setColorFilter(Color.parseColor(UiColor.secondary), PorterDuff.Mode.SCREEN);
            binding.test.returntype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        binding.test.billingdate.setVisibility(View.GONE);
                        reservationSummarry.ReservationRecurringDetailModel.IsNoReturnDate = isChecked;
                        binding.test.returntype.getBackground().setColorFilter(Color.parseColor(UiColor.primary), PorterDuff.Mode.SCREEN);
                    } else {
                        binding.test.returntype.getBackground().setColorFilter(Color.parseColor(UiColor.secondary), PorterDuff.Mode.SCREEN);
                        binding.test.billingdate.setVisibility(View.VISIBLE);
                        reservationSummarry.ReservationRecurringDetailModel.IsNoReturnDate = isChecked;
                    }
                }
            });

            binding.test.duration.setText("1");
            reservationSummarry.ReservationRecurringDetailModel.IsNoReturnDate = false;
            /*binding.test.duration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                  binding.test.date.setText(reservationSummarry.CheckOutDate);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/

            binding.test.date.setText(binding.test.carimage.checkInDateTime.getText().toString());

            List<String> duration = new ArrayList<>();
            duration.add(0,"Billing Cycle");
            duration.add("Daily");
            duration.add("Weekly");
            duration.add("BiWeekly");
            duration.add("FourWeekly");
            duration.add("ThirtyDays");

            ArrayAdapter<String> test = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,duration);
            /*ArrayAdapter<RecurringRsvBillingCycle> test = new ArrayAdapter<RecurringRsvBillingCycle>( context, R.layout.spinner_layout, R.id.text1,RecurringRsvBillingCycle.values());*/
           // binding.test.billingcycle.setPrompt("Select One");
            binding.test.billingcycle.setAdapter(test);
            binding.test.billingcycle.setSelection(2);
            binding.test.billingcycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(TAG, "onItemSelected: " +  binding.test.billingcycle.getSelectedItem().toString() );
                    /*if (binding.test.billingcycle.getSelectedItem().toString() == Daily.toString()){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckInDate,1));
                    } else if (binding.test.billingcycle.getSelectedItem().toString() == Weekly.toString()){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckInDate,7));
                    } else if (binding.test.billingcycle.getSelectedItem().toString() == BiWeekly.toString()){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckInDate,14));
                    } else if (binding.test.billingcycle.getSelectedItem().toString() == FourWeekly.toString()){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckInDate,28));
                    } else if (binding.test.billingcycle.getSelectedItem().toString() == ThirtyDays.toString()){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckInDate,30));
                    }*/
                    int value = 1;
                    if (binding.test.duration.getText().toString().trim().length() > 0){
                        value = Integer.parseInt(binding.test.duration.getText().toString());
                    }


                    if (binding.test.billingcycle.getSelectedItemPosition() == 1){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,1));
                        reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 1;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 2){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,6));
                        reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 6;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 3){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,14));
                        reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 14;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 4){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,28));
                        reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 28;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 5){
                        binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,30));
                        reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 30;
                    }

                    //dayclaculate(1);



                    if (binding.test.billingcycle.getSelectedItemPosition() != 0){
                        reservationSummarry.SelectedRecurringRsvBillingCycle = String.valueOf(reservationSummarry.ReservationRecurringDetailModel.BillingCycle);

                        reservationSummarry.ReservationRecurringDetailModel.Duration = Integer.parseInt(binding.test.duration.getText().toString());

                        reservationSummarry.CheckInDate = DateConvert.DateConverter(DateType.yyyyMMddD, binding.test.date.getText().toString().split(",")[0],DateType.yyyyMMddD) + "T" +getArguments().getString("pickuptime");

                        binding.test.date.setText(DateConvert.DateConverter(DateType.yyyyMMddD, binding.test.date.getText().toString().split(",")[0],DateType.yyyyMMddD) + "," +getArguments().getString("pickuptime"));

                        binding.test.carimage.checkInDateTime.setText(Helper.getDateDisplay(DateType.yyyyMMddD,binding.test.date.getText().toString().split("T")[0]) + " , " + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        bundle.putString("dropdate",DateConvert.DateConverter(DateType.yyyyMMddD, binding.test.date.getText().toString().split(",")[0],DateType.yyyyMMddD));
                        new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

                        binding.test.date.setText(DateConvert.DateConverter(DateType.yyyyMMddD, binding.test.date.getText().toString().split(",")[0],DateType.MMddyyyyS) + "," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                    }


                    if (binding.test.billingcycle.getSelectedItemPosition() == 1){
                       String dd = DateConvert.DateConverter(DateType.yyyyMMddD, getReturndate(reservationSummarry.CheckOutDate, 1 * value).split(",")[0], DateType.MMddyyyyS);
                      //  Helper.getTimeDisplay(DateType.time, getArguments().getString("droptime"));
                        // binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,1*value) +"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        binding.test.date.setText(dd+"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 1;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 2){
                        String dd = DateConvert.DateConverter(DateType.yyyyMMddD, getReturndate(reservationSummarry.CheckOutDate, 6 * value).split(",")[0], DateType.MMddyyyyS);
                        binding.test.date.setText(dd+"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 6;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 3){
                        String dd = DateConvert.DateConverter(DateType.yyyyMMddD, getReturndate(reservationSummarry.CheckOutDate, 14 * value).split(",")[0], DateType.MMddyyyyS);
                        binding.test.date.setText(dd+"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 14;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 4){
                        String dd = DateConvert.DateConverter(DateType.yyyyMMddD, getReturndate(reservationSummarry.CheckOutDate, 28 * value).split(",")[0], DateType.MMddyyyyS);
                        binding.test.date.setText(dd+"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 28;
                    } else if (binding.test.billingcycle.getSelectedItemPosition() == 5){
                        String dd = DateConvert.DateConverter(DateType.yyyyMMddD, getReturndate(reservationSummarry.CheckOutDate, 30 * value).split(",")[0], DateType.MMddyyyyS);
                        binding.test.date.setText(dd+"," +Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 30;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.test.duration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.test.billingcycle.getSelectedItemPosition() >= 1) {
                        if (binding.test.duration.getText().toString().length() != 0) {
                            reservationSummarry.SelectedRecurringRsvBillingCycle = String.valueOf(reservationSummarry.ReservationRecurringDetailModel.BillingCycle);
                            reservationSummarry.ReservationRecurringDetailModel.Duration = Integer.parseInt(binding.test.duration.getText().toString());
                            String dataa = getReturndate(reservationSummarry.CheckOutDate, dayclaculate(Integer.valueOf(s.toString())));
                            binding.test.date.setText(DateConvert.DateConverter(DateType.yyyyMMddD, dataa, DateType.MMddyyyyS) + "," + Helper.getTimeDisplay(DateType.time, getArguments().getString("droptime")));
                        }
                        //dayclaculate(reservationSummarry.ReservationRecurringDetailModel.Duration);
                        //reservationSummarry.CheckInDate = DateConvert.DateConverter(DateType.datecalculate, binding.test.date.getText().toString().split(" ")[0],DateType.yyyyMMddD) + "T" +getArguments().getString("pickuptime");
                        //binding.test.carimage.checkInDateTime.setText(Helper.getDateDisplay(DateType.datecalculate,binding.test.date.getText().toString().split(" ")[0]) + " , " + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
                        //bundle.putString("dropdate",DateConvert.DateConverter(DateType.datecalculate, binding.test.date.getText().toString().split(" ")[0],DateType.yyyyMMddD));
                        //new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        userDraw.toggle(binding.leftscreen.customeRate, false);
        userDraw.toggle(binding.leftscreen.fixrate, true);
        userDraw.toggle(binding.leftscreen.promocode, true);
        userDraw.toggle(binding.leftscreen.unlimitedMiles,false );
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply:
               // rateModel.DailyRate = Integer.valueOf(binding.leftscreen.userrate.getText().toString());
               // rateModel.HourlyRate = Integer.valueOf(binding.leftscreen.userrate.getText().toString());

                double rate = Double.valueOf(binding.leftscreen.userrate.getText().toString());
                double miles = Double.valueOf(binding.leftscreen.mileage.getText().toString());

                Log.e(TAG, "onClick: " + rate + " : " + miles );

                if (binding.leftscreen.daily.isChecked()){
                    rateModel.HourlyRate = Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(rate))/24));
                    rateModel.HourlyMilesAllowed =Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(miles))/24));
                }

                if (binding.leftscreen.weekly.isChecked()){
                    rateModel.HourlyRate = Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(rate))/24/7));
                    rateModel.HourlyMilesAllowed = Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(miles))/24/7));
                }


                if (binding.leftscreen.monthly.isChecked()){
                    rateModel.HourlyRate = Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(rate))/24/30));
                    rateModel.HourlyMilesAllowed = Float.valueOf(String.valueOf(Double.valueOf(String.valueOf(miles))/24/30));
                }

                rateModel.DailyRate = rateModel.HourlyRate*24;
                rateModel.WeeklyRate = rateModel.DailyRate*7;
                rateModel.MonthlyRate = rateModel.DailyRate*30;
                rateModel.HalfDayRate = rateModel.DailyRate/2;

              /*  rateModel.DailyMilesAllowed = rateModel.HourlyMilesAllowed*24;
                rateModel.WeeklyMilesAllowed = rateModel.DailyMilesAllowed*7;
                rateModel.MonthlyMilesAllowed = rateModel.DailyMilesAllowed*30;
                rateModel.HalfDayMilesAllowed = rateModel.DailyMilesAllowed/2;*/


                rateModel.RateFeaturesModel.DailyMilesAllowed = Double.valueOf((int) (rateModel.HourlyMilesAllowed*24));
                rateModel.RateFeaturesModel.MonthlyMilesAllowed = rateModel.RateFeaturesModel.DailyMilesAllowed * 30;
                rateModel.RateFeaturesModel.WeeklyMilesAllowed =  rateModel.RateFeaturesModel.DailyMilesAllowed *7;

                binding.test.ratehourly.setText(Helper.getAmtount(Double.valueOf(rateModel.HourlyRate * timeModel.HR)));
                binding.test.hourlyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.HourlyRate)));

                binding.test.ratedaily.setText(Helper.getAmtount(Double.valueOf(rateModel.DailyRate * timeModel.Days)));
                binding.test.dailyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.DailyRate)));

                binding.test.rateweekly.setText(Helper.getAmtount(Double.valueOf(rateModel.WeeklyRate * timeModel.Weeks)));
                binding.test.weeklyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.WeeklyRate)));

                binding.test.ratemonthly.setText(Helper.getAmtount(Double.valueOf(rateModel.MonthlyRate * timeModel.Months)));
                binding.test.monthlyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.MonthlyRate)));

                //rateModel.RateFeaturesModel.MonthlyMilesAllowed = Integer.parseInt(binding.leftscreen.mileage.getText().toString());
                //rateModel.MonthlyMilesAllowed = Float.parseFloat(binding.leftscreen.mileage.getText().toString());

                reservationSummarry.ReservationRatesModel = rateModel;
                //IsCustomRateApply: true
                reservationSummarry.ReservationRatesModel.IsCustomRateApply = true;
                reservationSummarry.ReservationRatesModel.IsApplyCustomRate = true;

                rateModel.RateFeaturesModel.SecurityDeposit = Double.valueOf(binding.leftscreen.deposit.getText().toString());

                binding.test.graceminute.setText(String.valueOf(rateModel.RateFeaturesModel.GraceMinutes));
                binding.test.extramileagecharge.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.ExtraMilesCharges)));
                binding.test.extrafuelcharge.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.FuelCharge)));
                binding.test.deposit.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.SecurityDeposit)));

                Log.e(TAG, "onClick: " + rateModel.HourlyRate );
                Log.e(TAG, "onClick: " + rateModel.HourlyMilesAllowed );
                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

                binding.slider.closeDrawer(GravityCompat.END);
                break;

            case R.id.editRate:
              //  NavHostFragment.findNavController(Fragment_New_Agreement.this).navigate(R.id.newAgreemnent_to_slider);
                binding.slider.openDrawer(GravityCompat.END);
                break;

            case R.id.lbl_confirm_2:
            case R.id.layout_payment:
                //NavHostFragment.findNavController(Fragment_New_Agreement.this).navigate(R.id.newAgreemnent_to_booking);
                NavHostFragment.findNavController(Fragment_New_Agreement.this).navigate(R.id.newAgreemnent_to_customerlist,bundle);
                break;

            case R.id.back:
                if (UserData.companyModel.CompanyPreference.DefaultBookingOnVehicleType){
                    NavHostFragment.findNavController(Fragment_New_Agreement.this)
                            .navigate(R.id.newAgreemnent_to_vehiclestype_available, bundle);
                } else {
                    NavHostFragment.findNavController(Fragment_New_Agreement.this)
                            .navigate(R.id.newAgreemnent_to_vehicle, bundle);
                }
                break;
            case R.id.discard:
                Intent intent = new Intent( getActivity(), Activity_Home.class);
                startActivity(intent);
                break;
        }
    }


    public String getReturndate(String cdate, int day){
        String rdate;
        SimpleDateFormat sdf = new SimpleDateFormat(DateType.datecalculate.toString());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(cdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getReturndate: " + c.getTime());
        c.add(Calendar.DATE, day);
       String d =   DateConvert.DateConverter(DateType.defaultdate,c.getTime().toString(), DateType.yyyyMMddD);
        //rdate = sdf.format(c.getTime()) + " " +Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime"));
        rdate = d.toString();// + " , " +Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime"));
      //  rdate = d;
        Log.e(TAG, "getReturndate: " + rdate );
        return  rdate;
    }

    public Integer dayclaculate (int value){
        int data = 0;
        /*if (binding.test.billingcycle.getSelectedItemPosition() == 1){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,1*value));
            reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 1;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 2){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,6*value));
            reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 6;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 3){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,14*value));
            reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 14;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 4){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,28*value));
            reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 28;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 5){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,30*value));
            reservationSummarry.ReservationRecurringDetailModel.BillingCycle = 30;
        }*/


        if (binding.test.billingcycle.getSelectedItemPosition() == 1){
            binding.test.date.setText(getReturndate(reservationSummarry.CheckOutDate,1*value));
            data = 1*value;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 2){
            data = 6 * value;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 3){
            data = 14* value;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 4){
            data = 28*value;
        } else if (binding.test.billingcycle.getSelectedItemPosition() == 5){
            data = 30* value;
        }
        return data;
    }

    public void getChecked(){
        try {
            if (binding.leftscreen.promocode.isChecked()){
                binding.leftscreen.customeRate.setChecked(false);
                getCustomeRate();
                binding.leftscreen.promocodedetail.setVisibility(View.VISIBLE);
            } else {
                binding.leftscreen.promocodedetail.setVisibility(View.GONE);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


      /*  if (  binding.leftscreen.unlimitedMiles.isChecked()){
            binding.leftscreen.unlimitedMilesdetail.setVisibility(View.VISIBLE);
        } else {
            binding.leftscreen.unlimitedMilesdetail.setVisibility(View.GONE);
        }*/

        /*if ( binding.leftscreen.fixrate.isChecked()){
            binding.leftscreen.fixratedetail.setVisibility(View.VISIBLE);
        } else {
            binding.leftscreen.fixratedetail.setVisibility(View.GONE);
        }*/
    }

    public void getCustomeRate() {
        try {
            if(binding.leftscreen.customeRate.isChecked()){
                binding.leftscreen.promocode.setChecked(false);
                getChecked();
                binding.leftscreen.customeRateview.setVisibility(View.VISIBLE);
            } else {
                binding.leftscreen.customeRateview.setVisibility(View.GONE);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }



   /* private void toggle(boolean show) {
        View redLayout = findViewById(R.id.redLayout);
        ViewGroup parent = findViewById(R.id.parent);

        Transition transition = new Slide(Gravity.END);
        transition.setDuration(600);
        transition.addTarget(R.id.slide);

        TransitionManager.beginDelayedTransition(parent, transition);
        redLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }*/

    public void listSpinner(List<OnDropDownList> data){
        List<String> business = new ArrayList<>();
        for (int i = 0; i <data.size() ; i++) {
            if (data.get(i).TableType == 8){
                business.add(data.get(i).Name);
            }

        }
        ArrayAdapter<String> adapterbusiness = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,business);
        binding.test.txtcheckOutLocationName.setAdapter(adapterbusiness);
//        binding.test.txtcheckOutLocationName.setSelection(select);

        binding.test.txtcheckOutLocationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDropdownId(binding.test.txtcheckOutLocationName.getSelectedItem().toString());

                /*new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    final JSONObject data = responseJSON.getJSONObject("Data");

                                    binding.test.ratecode.setText( data.get("RateCode").toString());

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.GET, TESTING, BASE_URL_LOGIN, header,String.valueOf(idd));*/

                new ApiService(new OnResponseListener() {
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
                                        rateModel = loginRes.getModel(data.toString(), RateModel.class);
                                        //binding.test.ratecode.setText( data.get("RateCode").toString());
                                        binding.test.ratecode.setText(rateModel.RateCode);
                                        binding.test.hourlyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.HourlyRate)));
                                        binding.test.dailyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.DailyRate)));
                                        binding.test.weeklyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.WeeklyRate)));
                                        binding.test.monthlyRate.setText(Helper.getAmtount(Double.valueOf(rateModel.MonthlyRate)));

                                        binding.test.graceminute.setText(String.valueOf(rateModel.RateFeaturesModel.GraceMinutes));
                                        binding.test.extramileagecharge.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.ExtraMilesCharges)));
                                        binding.test.extrafuelcharge.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.FuelCharge)));
                                        binding.test.deposit.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.SecurityDeposit)));

                                        binding.leftscreen.deposit.setText(Helper.getAmtount(Double.valueOf(rateModel.RateFeaturesModel.SecurityDeposit)));

                                        binding.leftscreen.totalday.setText(String.valueOf(timeModel.TotalDays));
                                        //binding.leftscreen.mileage.setText(String.valueOf(rateModel.));

                                        binding.leftscreen.daily.setChecked(true);
                                        binding.leftscreen.userrate.setText(Helper.getAmtount(Double.valueOf(rateModel.DailyRate)));
                                        binding.leftscreen.mileage.setText(String.valueOf(rateModel.RateFeaturesModel.MonthlyMilesAllowed/30));

                                        binding.test.ratehourly.setText(Helper.getAmtount(Double.valueOf(rateModel.HourlyRate * timeModel.HR)));

                                        binding.test.ratedaily.setText(Helper.getAmtount(Double.valueOf(rateModel.DailyRate * timeModel.Days)));

                                        binding.test.rateweekly.setText(Helper.getAmtount(Double.valueOf(rateModel.WeeklyRate * timeModel.Weeks)));

                                        binding.test.ratemonthly.setText(Helper.getAmtount(Double.valueOf(rateModel.MonthlyRate * timeModel.Months)));


                                        binding.leftscreen.unlimitedMiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked){
                                                    rateModel.RateFeaturesModel.IsUnlimitedMiles = true;
                                                } else {
                                                    rateModel.RateFeaturesModel.IsUnlimitedMiles = false;
                                                }
                                            }
                                        });

                                        /*if (binding.leftscreen.unlimitedMiles.isChecked()){
                                            rateModel.RateFeaturesModel.IsUnlimitedMiles = true;
                                        } else {
                                            rateModel.RateFeaturesModel.IsUnlimitedMiles = false;
                                        }*/



                                        reservationSummarry.ReservationRatesModel = rateModel;
                                        new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
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
                }, RequestType.POST, RATE, BASE_URL_LOGIN, header, params.getRate(idd, vehicleModel.VehicleTypeId, pickuplocation.Id,
                        getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                        getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void getDropdownId(String name){
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).Name.equals(name)){
                idd = data.get(i).Id;
            }
        }
    }

    OnResponseListener SummaryCharge = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
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
                                           // Log.e(TAG, "run: " + charges[i].ReservationSummaryDetailModels[j].Total );
                                           // Log.e(TAG, "run: " + charges[i].ReservationSummaryDetailModels[j].Description );
                                            if (charges[i].ReservationSummaryDetailModels[j].Total == 0) {
                                                binding.test.btm.txtMileage.setText(charges[i].ReservationSummaryDetailModels[j].Description.trim());
                                                //binding.test.txtMileage.setTextSize(15);
                                            } else{
                                                binding.test.btm.txtMileage.setText(Helper.getDistance(charges[i].ReservationSummaryDetailModels[j].Total));
                                                //binding.test.txtMileage.setTextSize(30);
                                            }
                                    }
                                    // binding.txtMileage.setText(String.valueOf(charges[i].ReservationSummaryDetailModels[1].Total));
                                }
                                if (charges[i].ReservationSummaryType==100){
                                    binding.test.btm.textviewTotalAmount.setText(DigitConvert.getDoubleDigit(charges[i].ReservationSummaryDetailModels[0].Total));
                                }
                            }
                            JSONObject obj =  resultSet.getJSONObject("ReservationTimeModel");
                            ReservationTimeModel reservationTimeModel = new ReservationTimeModel();
                            reservationTimeModel = loginRes.getModel(obj.toString(), ReservationTimeModel.class);
                            UserReservationData.reservationTimeModel = reservationTimeModel;
                            bundle.putSerializable("timemodel", reservationTimeModel);
                        }
                        else {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    OnResponseListener getReservationNumber = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {

                try {


                    Log.d("TAG", "onSuccess: " + response);
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        String data = responseJSON.getString("Data");
                        reservationSummarry.ReservationNo = data;
                        binding.test.carimage.reservationNumber.setText(data);
                        bundle.putSerializable("reservationSum",reservationSummarry);

                    }  else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {

        }
    };
}
