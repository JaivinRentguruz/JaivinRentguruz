package com.rentguruz.app.flexiicar.booking2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.model.display.Pickupdrop;
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
import com.rentguruz.app.databinding.FragmentSelectAdditionalOptionBinding;
import com.rentguruz.app.databinding.MiscellaneousTaxDetailsBinding;
import com.rentguruz.app.databinding.TaxDetailsListBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.base.UserReservationData;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RIchauffer;
import com.rentguruz.app.model.response.RIequipment;
import com.rentguruz.app.model.response.RInsuranceModel;
import com.rentguruz.app.model.response.RateModel;
import com.rentguruz.app.model.response.ReservationChargesModels;
import com.rentguruz.app.model.response.ReservationOriginDataModels;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.EQUIPMENT;
import static com.rentguruz.app.apicall.ApiEndPoint.MISCCHARGES;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Select_addition_Options extends BaseFragment
{
   // Bundle BookingBundle,VehicleBundle;
    JSONArray SelectedEquipmentList = new JSONArray();
    JSONArray MiscList = new JSONArray();
    JSONArray SummaryOfCharges = new JSONArray();
    int cmP_DISTANCE;
   // Bundle returnLocationBundle, locationBundle;
    //Boolean locationType, initialSelect;
    String DeliveryAndPickupModel = "";
    Double DeliveryChargeAmount=0.0, PickupChargeAmount=0.0;
    int DeliveryChargeLocID=0, PickupChargeLocID=0;
    String previousSwitchtotal="";
    ImageLoader imageLoader;
    String serverpath="";
    ToggleButton previousSwitch = null;
    FragmentSelectAdditionalOptionBinding binding;
    Pickupdrop pickupdrop;
   // Bundle Booking = new Bundle();
    VehicleModel model = new VehicleModel();
    //ReservationVehicleType reservationVehicleType = new ReservationVehicleType();
    LocationList pickuplocation = new LocationList();
    LocationList droplocation = new LocationList();
    RateModel rateModel = new RateModel();
    ReservationSummarry reserversationSummary = new ReservationSummarry();
    DateConvert dateConvert = new DateConvert();
    ReservationSummaryModels[] charges;
    ReservationTimeModel timeModel = new ReservationTimeModel();
    List<RIequipment> addinventory = new ArrayList<>();
    public static Boolean condition;
    public static HashMap<Integer, Boolean> activeid = new HashMap<>();
    public static HashMap<Boolean,Integer> activeid2 = new HashMap<>();
    public static HashMap<Integer, String> activationDetail = new HashMap<>();
    RInsuranceModel ReservationDeclineInsuranceModel;
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentSelectAdditionalOptionBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void setInitialSavedState(@Nullable @org.jetbrains.annotations.Nullable SavedState state) {
        super.setInitialSavedState(state);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        pickupdrop = new Pickupdrop();
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();
        binding.txtDiscardSAO.setOnClickListener(this);
        binding.backbtn1.setOnClickListener(this);
        try {
      //      getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            try {
              /*  Helper.AllowCustomerInsurance = true;
                if (Helper.AllowCustomerInsurance){
                    binding.difins.setVisibility(View.INVISIBLE);
                }*/
               if (UserData.companyModel.CompanyPreference.AllowCustomerInsurance){
                   binding.difins.setVisibility(View.VISIBLE);
               } else {
                   binding.difins.setVisibility(View.INVISIBLE);
               }

            } catch (Exception e){
                e.printStackTrace();
            }




           // fullProgressbar.show();

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            cmP_DISTANCE = sp.getInt("cmP_DISTANCE", 0);

            pickuplocation = (LocationList) getArguments().getSerializable("model");
            droplocation = (LocationList) getArguments().getSerializable("models");
            timeModel = (ReservationTimeModel) getArguments().getSerializable("timemodel");
            bundle.putSerializable("model",getArguments().getSerializable("model"));
            bundle.putSerializable("models", getArguments().getSerializable("models"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("timemodel",getArguments().getSerializable("timemodel"));
            bundle.putSerializable("Model",getArguments().getSerializable("Model"));
            bundle.putString("DeliveryAndPickupModel", getArguments().getString("DeliveryAndPickupModel"));
            bundle.putSerializable("reservationSum",getArguments().getSerializable("reservationSum"));
            bundle.putString("insuranceOption",getArguments().getString("insuranceOption"));
            ReservationDeclineInsuranceModel = new RInsuranceModel();
            DeliveryAndPickupModel = getArguments().getString("DeliveryAndPickupModel");
            reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");
            pickupdrop.pickupdate =  reserversationSummary.CheckOutDate;
            pickupdrop.dropdate = reserversationSummary.CheckInDate;
            try {
                model = (VehicleModel) getArguments().getSerializable("Model");
                reserversationSummary.ReservationVehicleModel.CurrentOdo = model.CurrentOdo;
                reserversationSummary.ReservationVehicleModel.VehicleId = model.Id;
                reserversationSummary.ReservationVehicleModel.VehicleTypeId = model.VehicleTypeId;
                Glide.with(context).load(model.DefaultImagePath).into(binding.VehImageBg1);
                binding.txtVehicleMOdelName.setText(model.VehicleName);
                binding.txtVehicleTypeName.setText(model.VehicleCategory);
                binding.refund.setText(Helper.getAmtount(Double.valueOf(model.SecurityDeposit),true) + " / REFUNDABLE");
                apiService = new ApiService(getMischarges, RequestType.POST, MISCCHARGES, BASE_URL_LOGIN, header, params.getMisc(pickuplocation.Id, reserversationSummary.ReservationVehicleModel.VehicleTypeId));
            } catch (Exception e){
                e.printStackTrace();
               /* reservationVehicleType = (ReservationVehicleType)getArguments().getSerializable("Model");
                //reserversationSummary.ReservationVehicleModel.CurrentOdo = reservationVehicleType.CurrentOdo;
                //reserversationSummary.ReservationVehicleModel.VehicleId = reservationVehicleType.Id;
                reserversationSummary.ReservationVehicleModel.VehicleTypeId = reservationVehicleType.Id;
                Glide.with(context).load(reservationVehicleType.VehicleClassStandaredImagePath).into(binding.VehImageBg1);
                binding.txtVehicleMOdelName.setText(reservationVehicleType.Name);
                binding.refund.setText(Helper.getAmtount(Double.valueOf(reservationVehicleType.SecurityDeposit),true) + " / REFUNDABLE");
                apiService = new ApiService(getMischarges, RequestType.POST, MISCCHARGES, BASE_URL_LOGIN, header, params.getMisc(pickuplocation.Id, reservationVehicleType.Id));*/
            }

          //  apiService = new ApiService(ReserCal, RequestType.POST, RATE, BASE_URL_LOGIN, header, params.getRate(model.RateId, model.VehicleTypeId, pickuplocation.Id));


            reserversationSummary.TotalDays = timeModel.TotalDays;
            reserversationSummary.CheckOutDate = getArguments().getString("pickupdate")+"T"+getArguments().getString("pickuptime");
            reserversationSummary.CheckInDate = getArguments().getString("dropdate")+"T"+getArguments().getString("droptime");
            reserversationSummary.PickUpLocation = pickuplocation.Id;
            reserversationSummary.DropLocation = droplocation.Id;
            reserversationSummary.IsIgnoreLocationClose = true;
            reserversationSummary.GetSummaryForView = true;
            reserversationSummary.BusinessSourceId = Fragment_Selected_Location.businessmaster;

            binding.txtDiscardSAO.setOnClickListener(this);
            binding.btm.layoutPayment.setOnClickListener(this);
            binding.backbtn1.setOnClickListener(this);
            //binding.insuranceDetail.setOnClickListener(this);
            binding.equipment.setOnClickListener(this);
            binding.miscellaneous.setOnClickListener(this);
            binding.insuranceDecline.setOnClickListener(this);
            binding.rlInsuranceCover.setVisibility(View.GONE);
            binding.rlEquipmentExtra.setVisibility(View.GONE);
            binding.rlMiscellaneousModel.setVisibility(View.GONE);
            pickupdrop.noDays = UserReservationData.reservationTimeModel.TotalDays;
            //binding.TxtDays.setText(String.valueOf(UserReservationData.reservationTimeModel.TotalDays));
           // binding.txtVehicleTypeName.setText(model.VehicleShortName);

            //binding.txtMileage.setText(model.NoOfSeats + "kms");
            binding.btm.fueltype.setText(Helper.fueltype);
            binding.btm.txtMileage.setText("Unlimited");
            //binding.btm.fcurrency.setText(Helper.displaycurrency);
            binding.btm.text.setText(getResources().getString(R.string.confirm));
            //binding.txtPickupLocationName.setText(pickuplocation.Name);
            //binding.txtReturnLocationName.setText(droplocation.Name);
            pickupdrop.pickuploc = pickuplocation.Name;
            pickupdrop.droploc = droplocation.Name;
            pickupdrop.pickupdate = reserversationSummary.CheckOutDate+":00";
            pickupdrop.dropdate = reserversationSummary.CheckInDate+":00";
            //binding.txtPickupdate1.setText(dateConvert.allDateConverter(DateType.yyyyMMddD,getArguments().getString("pickupdate"),DateType.ddMMyyyyS));
            //binding.txtPICKUPTime.setText(dateConvert.allDateConverter(DateType.time,getArguments().getString("pickuptime"),DateType.time2));
            //binding.txtPickupdate1.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")));
            //binding.txtPICKUPTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
            //binding.txtREturnDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")));
            //binding.txtREturnTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));

            binding.imgBottomArrow1.setVisibility(View.GONE);
            binding.SummaryofChargesArrowDown1.setVisibility(View.VISIBLE);
            binding.rlInsuranceCover.setVisibility(View.VISIBLE);
            binding.rlInsuranceCover.removeAllViews();
            binding.selectCustomer.setVisibility(View.GONE);
            binding.layoutDeliverypickupservice.setVisibility(View.GONE);
            binding.pickup.setPickupdrop(pickupdrop);
            //jvm
            //apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays) );

            //binding.insuranceDecline.setChecked(false);

        /*    if (Helper.VISIBLE) {
                apiService = new ApiService(getInsurance, RequestType.POST, INSURANCECOVER, BASE_URL_LOGIN, header, params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays));
            }
*/



       /*     JSONArray array = new JSONArray(getArguments().getString("insuranceOption"));

            binding.rlInsuranceCover.removeAllViews();
            for (int i = 0; i <array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                getSubview(i);
                TaxDetailsListBinding insurance = TaxDetailsListBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false);
                insurance.getRoot().setId(200 + i);
                insurance.getRoot().setLayoutParams(subparams);
                JSONObject extra = obj.getJSONObject("ReservationSummaryDetailModel");
                insurance.txtInsuranceDesc.setText(obj.getString("Description"));
                DecimalFormat df = new DecimalFormat("#.00");
                Double test = Double.valueOf(obj.getString("PerDayCharge"));
                String text = df.format(test);
                insurance.txtTotalCharge.setText(text);
                insurance.txtInsuranceName.setText(obj.getString("Name"));
                //MySpannable mySpannable = new MySpannable(true);
                //mySpannable.makeTextViewResizable1(insurance.txtInsuranceDesc, 2, "See More", true);
                MySpannable.makeTextViewResizable(insurance.txtInsuranceDesc, 2, "See More", true);
                insurance.switch2.setId(obj.getInt("Id"));
                if (condition) {
                    if (obj.getBoolean("IsSelected")) {
                        insurance.switch2.setChecked(true);
                        previousSwitch = insurance.switch2;
                        activeid.put(obj.getInt("Id"), true);
                        activeid2.put(true,obj.getInt("Id"));
                        reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                        reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                        reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                        reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                        reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                        reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                   *//*     if (reserversationSummary.ReservationOriginDataModels.size() == 0) {
                            reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(52, obj.toString()));
                        }*//*
                        activationDetail.put(52, obj.toString());
                        updateReservationSummarry(52);
                        ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                    }
                }

                if (activeid2.get(true) == obj.getInt("Id")){
                    insurance.switch2.setChecked(true);
                    reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                    reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                    reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                    reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                    reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                    reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                }

                insurance.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        fullProgressbar.show();
                        previousSwitch.setChecked(false);
                        previousSwitch = insurance.switch2;
                        activeid.put(obj.getInt("Id"), isChecked);
                        activeid2.put(isChecked,obj.getInt("Id"));
                       *//* for (int j = 0; j < reserversationSummary.ReservationOriginDataModels.size(); j++) {
                            if (reserversationSummary.ReservationOriginDataModels.get(j).TableType == 52) {
                                if (isChecked) {
                                    reserversationSummary.ReservationOriginDataModels.get(j).JsonData = obj.toString();
                                }
                            }
                        }*//*

                        if (isChecked) {
                            activationDetail.put(52, obj.toString());
                            updateReservationSummarry(52);
                        }

                        if (reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId == obj.getInt("Id")) {
                            insurance.switch2.setChecked(false);
                        } else {
                            insurance.switch2.setChecked(true);
                            reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                            reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                            reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                            reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                            reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                            reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                });
                binding.rlInsuranceCover.addView(insurance.getRoot());
            }*/

            try {
                if (Helper.BACKTO){
                    if (reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline){
                        Helper.VISIBLE = true;
                        binding.insuranceDecline.setChecked(true);
                        binding.rlInsuranceCover.setVisibility(View.GONE);
                        binding.rlInsuranceCover2.setVisibility(View.VISIBLE);
                        binding.rlInsuranceCover2line.setVisibility(View.VISIBLE);
                        declineInsurance();
                    }
                }
            } catch (Exception e ){
                e.printStackTrace();
            }

            try {
                if (Helper.isFirstB2CReservation) {
                    reserversationSummary.ReservationEquipmentInventoryModel.clear();
                    reserversationSummary.ReservationChargesModels.clear();
                    reserversationSummary.MiscellaneousChargeModels.clear();
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }

          /*  try {
               if (reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline){
                   declineInsurance();
               }
            } catch (Exception e){
                e.printStackTrace();
            }*/


            binding.insuranceDecline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        binding.rlInsuranceCover.setVisibility(View.GONE);
                        binding.rlInsuranceCover2.setVisibility(View.VISIBLE);
                        binding.rlInsuranceCover2line.setVisibility(View.VISIBLE);
                        declineInsurance();
                        ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                    } else {
                        //jvm
                        //apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays) );
                        binding.rlInsuranceCover.setVisibility(View.VISIBLE);
                        binding.rlInsuranceCover2.setVisibility(View.GONE);
                        binding.rlInsuranceCover2line.setVisibility(View.VISIBLE);
                        getInsurance();
                        Helper.VISIBLE=false;
                    }
                }
            });

            if(Helper.VISIBLE){
                declineInsurance();
            } else {
                getInsurance();
            }
          //  previousSwitch.setChecked(false);
            try {

                if (Helper.isCustomerVisible) {
                    binding.customerDetail.setVisibility(View.VISIBLE);
                    reserversationSummary.CustomerEmail = UserData.customer.Email;
                    reserversationSummary.CustomerId = UserData.customer.Id;
                    reserversationSummary.CustomerPhone = UserData.customer.MobileNo;
                    binding.customerDetails.setCustomer(UserData.customer);
                }
            } catch (Exception e){
               e.printStackTrace();
            }
            binding.selectCustomer.setOnClickListener(this);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        apiService = new ApiService(getInventory, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());



    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener getInsurance = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {

            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        Log.d(TAG, "onSuccess: " + resultSet);
                        binding.rlInsuranceCover.removeAllViews();
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            getSubview(i);
                            TaxDetailsListBinding insurance = TaxDetailsListBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false);
                            insurance.getRoot().setId(200 + i);
                            insurance.getRoot().setLayoutParams(subparams);
                            JSONObject extra =  obj.getJSONObject("ReservationSummaryDetailModel");
                            insurance.txtInsuranceDesc.setText(obj.getString("Description"));
                            DecimalFormat df = new DecimalFormat("#.00");
                            Double test = Double.valueOf(obj.getString("PerDayCharge"));
                            String text = df.format(test);
                            insurance.txtTotalCharge.setText(text);
                            insurance.txtInsuranceName.setText(obj.getString("Name"));
                          //  MySpannable.makeTextViewResizable(insurance.txtInsuranceDesc, 2, "See More", true);
                            if(obj.getBoolean("IsSelected")){
                                insurance.switch2.setChecked(true);
                                previousSwitch = insurance.switch2;
                                reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                                reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                                reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                                reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                                reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                                reserversationSummary.ReservationInsuranceModel.InsuranceDate =  (getArguments().getString("dropdate"));
                                if ( reserversationSummary.ReservationOriginDataModels.size() == 0){
                                    reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(52, obj.toString()));
                                }
                             /*   reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(52, obj.toString()));

                                for (int j = 0; j <reserversationSummary.ReservationOriginDataModels.size(); j++) {
                                    if (reserversationSummary.ReservationOriginDataModels.get(j).TableType == 52){
                                    reserversationSummary.ReservationOriginDataModels.get(j).JsonData = obj.toString();
                                    }
                                }*/

                                ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                            }
                            insurance.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                try {
                                    fullProgressbar.show();
                                    previousSwitch.setChecked(false);
                                    previousSwitch = insurance.switch2;
                                    for (int j = 0; j <reserversationSummary.ReservationOriginDataModels.size(); j++) {
                                        if (reserversationSummary.ReservationOriginDataModels.get(j).TableType == 52){
                                            if (isChecked) {
                                                reserversationSummary.ReservationOriginDataModels.get(j).JsonData = obj.toString();
                                            }
                                        }
                                    }

                                    /*reserversationSummary.ReservationOriginDataModels.get(0).TableType = 52;
                                    reserversationSummary.ReservationOriginDataModels.get(0).JsonData = obj.toString();*/
                                    if (reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId == obj.getInt("Id")){
                                        insurance.switch2.setChecked(false);
                                    } else {
                                        insurance.switch2.setChecked(true);
                                reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                                reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                                reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                                reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                                reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                                reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));

                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                            });
                            binding.rlInsuranceCover.addView(insurance.getRoot());
                        }

                    }
                    else
                    {
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

    OnResponseListener getInventory = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
          handler.post(() -> {
            try {
                JSONObject responseJSON = new JSONObject(response);
                Boolean status = responseJSON.getBoolean("Status");

                if (status) {
                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                    JSONArray array = resultSet.getJSONArray("Data");
                    Log.d(TAG, "onSuccess: " + resultSet);
                    for (int i = 0; i <array.length() ; i++) {
                        if (array.length()>0){
                            binding.rlEquipmentExtra.setVisibility(View.VISIBLE);
                            binding.rlEquipmentExtraLine.setVisibility(View.VISIBLE);
                            binding.equipment.setVisibility(View.VISIBLE);
                        }
                        JSONObject obj = array.getJSONObject(i);
                        getSubview(i);
                        TaxDetailsListBinding detailsListBinding = TaxDetailsListBinding.inflate(subinflater,
                                getActivity().findViewById(android.R.id.content), false);
                        detailsListBinding.txtInsuranceName.setText(obj.getString("Name"));
                        detailsListBinding.txtInsuranceDesc.setText(obj.getString("Description"));
                        detailsListBinding.txtTotalCharge.setText(DigitConvert.getDoubleDigit(Double.valueOf(obj.getString("Price"))));
                        detailsListBinding.currency.setText(Helper.displaycurrency);

                        try {
                            if (Helper.BACKTO) {
                                for (int j = 0; j < reserversationSummary.ReservationEquipmentInventoryModel.size(); j++) {
                                    if (reserversationSummary.ReservationEquipmentInventoryModel.get(j).EquipInventId == obj.getInt("Id")) {
                                        detailsListBinding.switch2.setChecked(true);
                                       // removeEquipment(obj.getInt("Id"));
                                    }
                                }
                                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                            }
                        } catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }

                        detailsListBinding.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            try {
                                fullProgressbar.show();
                            if (isChecked){
                                reserversationSummary.ReservationEquipmentInventoryModel.add( new RIequipment(1,obj.getString("SerialNo"), obj.getInt("Id"),
                                        1,obj.getInt("Price"),obj.getInt("MaxPrice")));
                            } else {
                                int len = reserversationSummary.ReservationEquipmentInventoryModel.size();
                                for (int j = 0; j <len ; j++) {
                                   if (reserversationSummary.ReservationEquipmentInventoryModel.get(j).EquipInventId == obj.getInt("Id"))
                                    {
                                        reserversationSummary.ReservationEquipmentInventoryModel.remove(j);
                                        break;
                                        //removeEquipment(obj.getInt("Id"));
                                    }
                                }
                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loginRes.testingLog(TAG, reserversationSummary);
                            ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                        });


                        detailsListBinding.getRoot().setId(200+i);
                        detailsListBinding.getRoot().setLayoutParams(subparams);
                        binding.rlEquipmentExtra.addView(detailsListBinding.getRoot());

                    }
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

    OnResponseListener getMischarges = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        Log.d(TAG, "onSuccess: " + resultSet);
                        for (int i = 0; i <array.length() ; i++) {
                            if (array.length()>0){
                                binding.rlMiscellaneousModel.setVisibility(View.VISIBLE);
                                binding.rlMiscellaneousModelLine.setVisibility(View.VISIBLE);
                                binding.miscellaneous.setVisibility(View.VISIBLE);
                            }
                            JSONObject obj = array.getJSONObject(i);
                            getSubview(i);
                            MiscellaneousTaxDetailsBinding miscellaneousTaxDetailsBinding = MiscellaneousTaxDetailsBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            miscellaneousTaxDetailsBinding.txtMiscName.setText(obj.getString("Name"));
                            miscellaneousTaxDetailsBinding.currency.setText(Helper.displaycurrency);
                            miscellaneousTaxDetailsBinding.txtMiscAmount.setText(DigitConvert.getDoubleDigit(Double.valueOf(obj.getString("Amount"))));
                            miscellaneousTaxDetailsBinding.txtMiscDesc.setVisibility(View.GONE);

                            //reserversationSummary.MiscellaneousChargeModels.clear();
                            if (obj.getBoolean("IsMandatory")) {
                                miscellaneousTaxDetailsBinding.switch1.setChecked(true);
                                reserversationSummary.MiscellaneousChargeModels.add(new RIchauffer(obj.getString("Name"),
                                        obj.getInt("ChargeType"),obj.getInt("DetailId"),obj.getInt("Amount"),obj.getInt("TotalUnit")));
                                miscellaneousTaxDetailsBinding.switch1.setClickable(false);
                                miscellaneousTaxDetailsBinding.switch1.setBackground(context.getResources().getDrawable(R.drawable.toggle_selector_red));
                            }
                        //    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);


                            miscellaneousTaxDetailsBinding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                try {
                                    fullProgressbar.show();
                                    if (isChecked){
                                        reserversationSummary.MiscellaneousChargeModels.add(new RIchauffer(obj.getString("Name"),
                                                obj.getInt("ChargeType"),obj.getInt("DetailId"),obj.getInt("Amount"),obj.getInt("TotalUnit")));
                                        ReservationChargesModels chargesModels = new ReservationChargesModels();
                                        chargesModels.ChargeFor = obj.getInt("DetailId");
                                        chargesModels.Amount = obj.getInt("Amount");
                                        //(MiscellaneousCharges = 1, TaxCharges = 2
                                        chargesModels.AmountType = 1;
                                        chargesModels.ChargeType = 1;
                                        chargesModels.IsTaxable = false;
                                        reserversationSummary.ReservationChargesModels.add(chargesModels);
                                    } else {
                                        int len = reserversationSummary.MiscellaneousChargeModels.size();
                                        for (int j = 0; j <len ; j++) {
                                            if (reserversationSummary.MiscellaneousChargeModels.get(j).DetailId == obj.getInt("DetailId")){
                                                Log.e(TAG, "onSuccess: " + "Mis" + reserversationSummary.MiscellaneousChargeModels.size() );
                                                removeInventory(obj.getInt("DetailId"));
                                               // reserversationSummary.MiscellaneousChargeModels.remove(j);

                                            }
                                        }
                                        for (int j = 0; j <reserversationSummary.ReservationChargesModels.size() ; j++) {
                                            if (reserversationSummary.ReservationChargesModels.get(j).ChargeFor ==obj.getInt("DetailId") ){
                                                Log.e(TAG, "onSuccess: " + "Char" + reserversationSummary.ReservationChargesModels.size() );
                                                removeInvtory(obj.getInt("DetailId"));
                                                //reserversationSummary.ReservationChargesModels.remove(j);
                                                Log.e(TAG, "onSuccess: " + "Mis" + reserversationSummary.MiscellaneousChargeModels.size() );
                                                Log.e(TAG, "onSuccess: " + "Char" + reserversationSummary.ReservationChargesModels.size() );
                                            }
                                        }
                                    }
                                }catch (JSONException | IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                                loginRes.testingLog(TAG, reserversationSummary);
                                ApiService2 apiService21 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                            });


                            try {
                                if (Helper.BACKTO) {
                                    for (int j = 0; j < reserversationSummary.ReservationChargesModels.size(); j++) {
                                        if (reserversationSummary.ReservationChargesModels.get(j).ChargeFor ==obj.getInt("DetailId")
                                           && reserversationSummary.MiscellaneousChargeModels.get(j).DetailId == obj.getInt("DetailId")) {
                                            miscellaneousTaxDetailsBinding.switch1.setChecked(true);
                                            // removeEquipment(obj.getInt("Id"));
                                        }
                                    }
                                    new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                                }
                            } catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                            miscellaneousTaxDetailsBinding.getRoot().setId(200+i);
                            miscellaneousTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            binding.rlMiscellaneousModel.addView(miscellaneousTaxDetailsBinding.getRoot());
                        }
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

    OnResponseListener getTaxtDetails = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");

                                final JSONArray getInsuranceDEtails = resultSet.getJSONArray("insuranceModel");
                                final RelativeLayout rlInsuranceDetails = getActivity().findViewById(R.id.rlInsuranceCover);

                                final JSONArray getEquipmentdetails = resultSet.getJSONArray("equipmentModel");
                                final RelativeLayout rlEquipmentDetails = getActivity().findViewById(R.id.rl_EquipmentExtra);

                                final JSONArray getmiscellaneousModel = resultSet.getJSONArray("miscellaneousModel");
                                final RelativeLayout rlmiscellaneousModel = getActivity().findViewById(R.id.rl_miscellaneousModel);

                                //Vehicle Model
                                JSONObject vehicleModel =resultSet.getJSONObject("vehicleModel");
                                final int vehiclE_ID = vehicleModel.getInt("vehiclE_ID");
                                final String vehiclE_NAME = vehicleModel.getString("vehiclE_NAME");
                                final int vehiclE_TYPE_ID = vehicleModel.getInt("vehiclE_TYPE_ID");
                                final String vehiclE_TYPE_NAME = vehicleModel.getString("vehiclE_TYPE_NAME");
                                final String vehiclE_SEAT_NO = vehicleModel.getString("vehiclE_SEAT_NO");
                                final String transmission = vehicleModel.getString("transmission");
                                final String v_CURR_ODOM = vehicleModel.getString("v_CURR_ODOM");
                                final String img_Path = vehicleModel.getString("img_Path");
                                final String vehiclE_OPTIONS_IDS = vehicleModel.getString("vehiclE_OPTIONS_IDS");
                                final String rate_ID = vehicleModel.getString("rate_ID");
                                final String rate_Name = vehicleModel.getString("rate_Name");
                                final int package_ID = vehicleModel.getInt("package_ID");
                                final double totaL_DAYS = vehicleModel.getDouble("totaL_DAYS");
                                final String veh_bags = vehicleModel.getString("veh_bags");
                                final String doors = vehicleModel.getString("doors");
                                final String fuel_Name = vehicleModel.getString("fuel_Name");
                                final String transmission_Name = vehicleModel.getString("transmission_Name");
                                final String veh_Name = vehicleModel.getString("veh_Name");
                                final String year_Name = vehicleModel.getString("year_Name");
                                final String lateR_PRICE = vehicleModel.getString("lateR_PRICE");
                                final int lateR_RATE_ID = vehicleModel.getInt("lateR_RATE_ID");
                                final String lateR_RATE_NAME = vehicleModel.getString("lateR_RATE_NAME");
                                final double daily_Price = vehicleModel.getDouble("daily_Price");
                                final String available_QTY = vehicleModel.getString("available_QTY");
                                final String vehDescription = vehicleModel.getString("vehDescription");
                                final String vehicle_Make_Model_Name = vehicleModel.getString("vehicle_Make_Model_Name");
                                final int isDepositMandatory = vehicleModel.getInt("isDepositMandatory");
                                final double securityDeposit = vehicleModel.getDouble("securityDeposit");
                                final double hourlyMilesAllowed = vehicleModel.getDouble("hourlyMilesAllowed");
                                final double halfDayMilesAllowed = vehicleModel.getDouble("halfDayMilesAllowed");
                                final double dailyMilesAllowed = vehicleModel.getDouble("dailyMilesAllowed");
                                final double weeklyMilesAllowed = vehicleModel.getDouble("weeklyMilesAllowed");
                                final double monthlyMilesAllowed = vehicleModel.getDouble("monthlyMilesAllowed");
                                final double totalMilesAllowed = vehicleModel.getDouble("totalMilesAllowed");
                                final int lockKey = vehicleModel.getInt("lockKey");

                                String url1 = serverpath + img_Path;
                                imageLoader.displayImage(url1, binding.VehImageBg1);

                                //binding.TxtDays.setText(String.valueOf(totaL_DAYS));
                                //binding.txtVehicleTypeName.setText(vehiclE_TYPE_NAME);
                                binding.txtVehicleMOdelName.setText(vehiclE_NAME);
//                                binding.txtRate1.setText(rate_ID);

                             /*   if(cmP_DISTANCE==1)
                                {
                                    String Miles=(String.valueOf(totalMilesAllowed));
                                    binding.txtMileage.setText(Miles+"kms");
                                }
                                else {
                                    String Miles=(String.valueOf(totalMilesAllowed));
                                    binding.txtMileage.setText(Miles+"kms");
                                }*/

                                //insuranceModel
                                int len;
                                len = getInsuranceDEtails.length();


                                for (int j = 0; j < len; j++)
                                {

                                    final JSONObject test = (JSONObject) getInsuranceDEtails.get(j);

                                    final  int transId = test.getInt("transId");
                                    final  int deductableId = test.getInt("deductableId");
                                    final String insuranceName = test.getString("insuranceName");
                                    final String insuranceDesc=test.getString("insuranceDesc");
                                    final int isSelected=test.getInt("isSelected");
                                    final double excessCharge=test.getDouble("excessCharge");
                                    final double perDayCharge=test.getDouble("perDayCharge");
                                    final double totalCharge=test.getDouble("totalCharge");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 10, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final LinearLayout l1 = (LinearLayout) inflater.inflate(R.layout.tax_details_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    l1.setId(200 + j);
                                    l1.setLayoutParams(lp);

                                    final TextView txt_insuranceName,txt_insuranceDesc,txt_totalCharge;
                                    txt_insuranceName= (TextView)l1.findViewById(R.id.txt_insuranceName);
                                    txt_insuranceDesc=(TextView)l1.findViewById(R.id.txt_insuranceDesc);
                                    txt_totalCharge=(TextView)l1.findViewById(R.id.txt_totalCharge);

                                    txt_insuranceName.setText(insuranceName);
                                    txt_insuranceDesc.setText(insuranceDesc);
                                    txt_totalCharge.setText(((String.format(Locale.US,"%.2f",totalCharge))));

                                    final ToggleButton s1=l1.findViewById(R.id.switch2);

                                    if(isSelected == 1)
                                    {
                                        previousSwitch = s1;
                                        s1.setChecked(true);
                                    }

                                    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                        {
                                            try {
                                                if (isChecked)
                                                {
                                                    previousSwitch.setChecked(false);
                                                    previousSwitch = s1;

                                                    previousSwitchtotal=txt_totalCharge.getText().toString();

                                                    String totalAmount = binding.btm.textviewTotalAmount.getText().toString();
                                                    double TotalValue = Double.parseDouble(totalAmount)+Double.parseDouble(previousSwitchtotal) ;

                                                    binding.btm.textviewTotalAmount.setText((String.format(Locale.US, "%.2f", TotalValue)));
                                                }
                                                else
                                                {
                                                        previousSwitchtotal=txt_totalCharge.getText().toString();
                                                        String totalAmount = binding.btm.textviewTotalAmount.getText().toString();
                                                        double TotalValue = Double.parseDouble(totalAmount)-Double.parseDouble(previousSwitchtotal);
                                                    binding.btm.textviewTotalAmount.setText((String.format(Locale.US, "%.2f", TotalValue)));
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    rlInsuranceDetails.addView(l1);
                                }

                                //equipmentModel
                                int len1;
                                len1 = getEquipmentdetails.length();

                                for (int j = 0; j < len1; j++)
                                {

                                    final JSONObject test = (JSONObject) getEquipmentdetails.get(j);
                                    final  int equipmentTypeId = test.getInt("equipmentTypeId");
                                    final String equipmentImagePath = test.getString("equipmentImagePath");
                                    final String equipmentName=test.getString("equipmentName");
                                    final String equipmentDesc=test.getString("equipmentDesc");
                                    final int equipmentQty=test.getInt("equipmentQty");
                                    final int taxValue=test.getInt("taxValue");
                                    final int taxAmount=test.getInt("taxAmount");
                                    final double equipmentAmount=test.getDouble("equipmentAmount");

                                    final JSONObject equipmentObj = new JSONObject();
                                    equipmentObj.put("equipmentTypeId", equipmentTypeId);
                                    equipmentObj.put("equipmentImagePath", equipmentImagePath);
                                    equipmentObj.put("equipmentName", equipmentName);
                                    equipmentObj.put("equipmentDesc", equipmentDesc);
                                    equipmentObj.put("equipmentQty", equipmentQty);
                                    equipmentObj.put("taxValue", taxValue);
                                    equipmentObj.put("taxAmount", taxAmount);
                                    equipmentObj.put("equipmentAmount", equipmentAmount+"");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout2 = (LinearLayout) inflater.inflate(R.layout.tax_details_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout2.setId(200 + j);
                                    linearLayout2.setLayoutParams(lp);

                                    final TextView txt_equipmentName,txt_equipmentDescc,txt_equipmentAmount;

                                    txt_equipmentName= (TextView) linearLayout2.findViewById(R.id.txt_insuranceName);
                                    txt_equipmentDescc=(TextView)linearLayout2.findViewById(R.id.txt_insuranceDesc);
                                    txt_equipmentAmount=(TextView)linearLayout2.findViewById(R.id.txt_totalCharge);
                                    txt_equipmentName.setText(equipmentName);
                                    txt_equipmentDescc.setText(equipmentDesc);
                                    txt_equipmentAmount.setText(((String.format(Locale.US,"%.2f",equipmentAmount))));

                                    final ToggleButton s1=linearLayout2.findViewById(R.id.switch2);
                                    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                        {
                                            try {

                                                if (isChecked)
                                                {
                                                    equipmentObj.put("equipmentQty", 1);

                                                    String TotalequipmentAmount= txt_equipmentAmount.getText().toString();
                                                    String totalAmount= binding.btm.textviewTotalAmount.getText().toString();
                                                    double TotalValue=Double.parseDouble(TotalequipmentAmount)+Double.parseDouble(totalAmount);

                                                    binding.btm.textviewTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));
                                                    SelectedEquipmentList.put(equipmentObj);

                                                } else
                                                    {
                                                    equipmentObj.put("equipmentQty", 0);

                                                        int count=SelectedEquipmentList.length();

                                                        for (int i=0;i<count;i++)
                                                        {
                                                            try {
                                                                JSONObject obj=SelectedEquipmentList.getJSONObject(i);

                                                                if(obj.getInt("equipmentTypeId")==equipmentTypeId)
                                                                {
                                                                    String TotalequipmentAmount= txt_equipmentAmount.getText().toString();
                                                                    String totalAmount= binding.btm.textviewTotalAmount.getText().toString();
                                                                    double TotalValue=Double.parseDouble(totalAmount)-Double.parseDouble(TotalequipmentAmount);
                                                                    binding.btm.textviewTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));
                                                                    SelectedEquipmentList.remove(i);
                                                                    i--;
                                                                    count--;
                                                                }

                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    rlEquipmentDetails.addView(linearLayout2);
                                }

                                //miscellaneousModel
                                int len2;
                                len2 = getmiscellaneousModel.length();

                                for (int j = 0; j < len2; j++)
                                {
                                    final JSONObject test = (JSONObject) getmiscellaneousModel.get(j);
                                    final  int miscId = test.getInt("miscId");
                                    final String miscName = test.getString("miscName");
                                    final String miscDesc=test.getString("miscDesc");
                                    final double basicValue=test.getDouble("basicValue");
                                    final int quantity=test.getInt("quantity");
                                    final double miscAmount=test.getDouble("miscAmount");
                                    final int taxableAmount=test.getInt("taxableAmount");
                                    final int isOptional=test.getInt("isOptional");

                                    final JSONObject miscObj = new JSONObject();
                                    miscObj.put("miscId",miscId);
                                    miscObj.put("miscName",miscName);
                                    miscObj.put("miscDesc",miscDesc);
                                    miscObj.put("basicValue",basicValue);
                                    miscObj.put("quantity",quantity);
                                    miscObj.put("miscAmount",miscAmount);
                                    miscObj.put("taxableAmount",taxableAmount);

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.miscellaneous_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout.setId(200 + j);
                                    linearLayout.setLayoutParams(lp);

                                    final TextView txt_miscName,txt_miscDesc,txt_miscAmount;
                                    final ToggleButton s1=linearLayout.findViewById(R.id.switch1);


                                    txt_miscName= (TextView)linearLayout.findViewById(R.id.txt_miscName);
                                    txt_miscDesc=(TextView)linearLayout.findViewById(R.id.txt_miscDesc);
                                    txt_miscAmount=(TextView)linearLayout.findViewById(R.id.txt_miscAmount);
                                    txt_miscName.setText(miscName);
                                    txt_miscDesc.setText(miscDesc);

                                    txt_miscAmount.setText(((String.format(Locale.US,"%.2f",miscAmount))));

                                    if (isOptional==1)
                                    {
                                        s1.setChecked(true);
                                        s1.setEnabled(false);

                                        miscObj.put("miscId",miscId);
                                        miscObj.put("miscAmount",miscAmount);
                                        MiscList.put(miscObj);
                                    }
                                    else
                                    {
                                        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                        {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                            {
                                                try {
                                                    if (isChecked)
                                                    {
                                                        miscObj.put("isOptional",1);
                                                    } else {
                                                        miscObj.put("isOptional",0);
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    rlmiscellaneousModel.addView(linearLayout);
                                }

                                //getsummaryOfCharges
                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                len = getsummaryOfCharges.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                    final  int sortId = test.getInt("sortId");
                                    final  double chargeAmount=test.getDouble("chargeAmount");
                                    String chargeCode = "";
                                    
                                    if(test.has("chargeCode"))
                                    {
                                        chargeCode = test.getString("chargeCode");
                                    }
                                    
                                    final  String chargeName = test.getString("chargeName");

                                    JSONObject summaryOfChargesObj = new JSONObject();
                                    summaryOfChargesObj.put("sortId",sortId);
                                    summaryOfChargesObj.put("chargeCode",chargeCode);
                                    summaryOfChargesObj.put("chargeName",chargeName);
                                    summaryOfChargesObj.put("chargeAmount",chargeAmount);

                                    SummaryOfCharges.put(summaryOfChargesObj);

                                    if(chargeName.equals("Estimated Total"))
                                    {
                                        binding.btm.textviewTotalAmount.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

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
                                            binding.btm.txtMileage.setText( Helper.getDistance(charges[i].ReservationSummaryDetailModels[j].Total));
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
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    OnResponseListener ReserCal = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
          handler.post(new Runnable() {
              @Override
              public void run() {
                  try
                  {
                      JSONObject responseJSON = new JSONObject(response);
                      Boolean status = responseJSON.getBoolean("Status");

                      if (status) {
                          JSONObject resultSet = responseJSON.getJSONObject("Data");
                          rateModel = loginRes.getModel(resultSet.toString(), RateModel.class);
                          Log.d(TAG, "run: " + rateModel.RateId);
                          reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(47, resultSet.toString()));
                          reserversationSummary.ReservationRatesModel = rateModel;
                          //apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays) );
                         // apiService = new ApiService(getInventory, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());
                         // apiService = new ApiService(getMischarges, RequestType.POST, MISCCHARGES, BASE_URL_LOGIN, header, params.getMisc(pickuplocation.Id, model.VehicleTypeId));
                          /*reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                          reserversationSummary.ReservationInsuranceModel.NoOfDays = 3;*/

                        /*  reserversationSummary.CheckOutDate = getArguments().getString("pickupdate")+"T"+getArguments().getString("pickuptime")+":45.495Z";
                          reserversationSummary.CheckInDate = getArguments().getString("dropdate")+"T"+getArguments().getString("droptime")+":45.495Z";
                          reserversationSummary.PickUpLocation = 10;
                          reserversationSummary.DropLocation = 8;*/

                         // ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                      } else {
                          String errorString = responseJSON.getString("Message");
                          CustomToast.showToast(getActivity(),errorString,1);
                      }
                  }  catch (Exception e)
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

    public void updateReservationSummarry(int key){
        int data = reserversationSummary.ReservationOriginDataModels.size();
        for (int i = 0; i <data; i++) {
            if (reserversationSummary.ReservationOriginDataModels.get(i).TableType == key) {
                reserversationSummary.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(key, activationDetail.get(key)));  // activationDetail.get(52).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_DiscardSAO:
                NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                        .navigate(R.id.action_Select_addtional_options_to_Search_activity);
                /*Intent intent = new Intent( getActivity(), Activity_Agreements.class);
                startActivity(intent);*/

                break;

            //case R.id.lbl_confirm_2:
            case R.id.layout_payment:
                bundle.putSerializable("pickuploc", pickuplocation);
                bundle.putSerializable("droploc", droplocation);
                bundle.putSerializable("ratemaster", rateModel);
                bundle.putSerializable("vechicle", model);
                bundle.putSerializable("summarry",reserversationSummary );
                bundle.putString("netrate",binding.btm.textviewTotalAmount.getText().toString());
                bundle.putString("miles", binding.btm.txtMileage.getText().toString());
                bundle.putString("pickupdate", getArguments().getString("pickupdate"));
                bundle.putString("dropdate", getArguments().getString("dropdate"));
                bundle.putString("droptime", getArguments().getString("droptime"));
                bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
                if (validation()) {
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_Finalize_your_rental, bundle);
                }
                break;

            case  R.id.backbtn1:
                bundle.putSerializable("model", pickuplocation);
                bundle.putSerializable("models", droplocation);
                bundle.putSerializable("ratemaster", rateModel);
                bundle.putSerializable("vechicle", model);
                bundle.putString("netrate",binding.btm.textviewTotalAmount.getText().toString());
                bundle.putString("miles",binding.btm.txtMileage.getText().toString());
                bundle.putString("pickupdate", getArguments().getString("pickupdate"));
                bundle.putString("dropdate", getArguments().getString("dropdate"));
                bundle.putString("droptime", getArguments().getString("droptime"));
                bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
                NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                        .navigate(R.id.action_Select_addtional_options_to_Vehicles_Available, bundle);
                break;

            case R.id.insuranceDetail:
                if (binding.rlInsuranceCover.getVisibility() == View.GONE)
                {
                binding.imgBottomArrow1.setVisibility(View.GONE);
                binding.SummaryofChargesArrowDown1.setVisibility(View.VISIBLE);
                binding.rlInsuranceCover.setVisibility(View.VISIBLE);
                    binding.rlMiscellaneousModel.setVisibility(View.GONE);
                    binding.rlEquipmentExtra.setVisibility(View.GONE);
                    binding.rlInsuranceCover.removeAllViews();
                   // apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays) );
                } else {
                    binding.imgBottomArrow1.setVisibility(View.VISIBLE);
                    binding.SummaryofChargesArrowDown1.setVisibility(View.GONE);
                    binding.rlInsuranceCover.removeAllViews();
                    binding.rlInsuranceCover.setVisibility(View.GONE);
                }
                break;

            case R.id.equipment:
                if (binding.rlEquipmentExtra.getVisibility() == View.GONE)
                {
                    binding.imgBottomArrow2.setVisibility(View.GONE);
                    binding.SummaryofChargesArrowDown2.setVisibility(View.VISIBLE);
                    binding.rlEquipmentExtra.setVisibility(View.VISIBLE);
                    binding.rlEquipmentExtra.removeAllViews();
                    apiService = new ApiService(getInventory, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());
                } else {
                    binding.imgBottomArrow2.setVisibility(View.VISIBLE);
                    binding.SummaryofChargesArrowDown2.setVisibility(View.GONE);
                    binding.rlEquipmentExtra.removeAllViews();
                    binding.rlEquipmentExtra.setVisibility(View.GONE);
                }
                break;
            case R.id.miscellaneous:
                if (binding.rlMiscellaneousModel.getVisibility() == View.GONE)
                {
                    binding.imgBottomArrow3.setVisibility(View.GONE);
                    binding.SummaryofChargesArrowDown3.setVisibility(View.VISIBLE);
                    binding.rlMiscellaneousModel.setVisibility(View.VISIBLE);
                    binding.rlMiscellaneousModel.removeAllViews();
                    apiService = new ApiService(getMischarges, RequestType.POST, MISCCHARGES, BASE_URL_LOGIN, header, params.getMisc(pickuplocation.Id, model.VehicleTypeId));
                } else {
                    binding.imgBottomArrow3.setVisibility(View.VISIBLE);
                    binding.SummaryofChargesArrowDown3.setVisibility(View.GONE);
                    binding.rlMiscellaneousModel.removeAllViews();
                    binding.rlMiscellaneousModel.setVisibility(View.GONE);
                }
                break;

            case R.id.insuranceDecline:
                if (binding.insuranceDecline.isChecked()) {
                    bundle.putInt("key", 1);
                    UserData.customer.FullName = UserData.loginResponse.LogedInCustomer.FullName;
                    UserData.customer.MobileNo = UserData.loginResponse.LogedInCustomer.MobileNo;
                    UserData.customer.Email = UserData.loginResponse.LogedInCustomer.Email;
                    Helper.insertinsuarancefromreservation = true;
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_insurance_policy_list, bundle);
                }
                break;

            case R.id.select_customer:
                NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                        .navigate(R.id.action_Vehicle_Finalize_Your_Rental_to_CustomerList,bundle);
        }
    }

    public void declineInsurance(){
        if (Helper.VISIBLE){
            int datalist = 3;
            reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
            reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = true;
            reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
            reserversationSummary.ReservationInsuranceModel.Remarks = "null";
            reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = UserData.insuranceModel.Id;
            reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
            reserversationSummary.ReservationInsuranceModel.InsuranceDetailsModel = UserData.insuranceModel;


            ReservationDeclineInsuranceModel.IsSureInsurance = false;
            ReservationDeclineInsuranceModel.IsInsuranceDecline = true;
            ReservationDeclineInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
            ReservationDeclineInsuranceModel.Remarks = "null";
            ReservationDeclineInsuranceModel.InsuranceCoverDetailId = UserData.insuranceModel.Id;
            ReservationDeclineInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
            ReservationDeclineInsuranceModel.InsuranceDetailsModel = UserData.insuranceModel;
            binding.rlInsuranceCover2.removeAllViews();
            for (int j = 0; j < datalist; j++){

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.setMargins(0, 10, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.decline_insurance_detail, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                linearLayout.setId(200 + j);
                linearLayout.setLayoutParams(lp);

                TextView lbl_chargeAmount, lblchargeName;
                lbl_chargeAmount = (TextView) linearLayout.findViewById(R.id.value);
                lblchargeName = (TextView) linearLayout.findViewById(R.id.name);

                if (j == 0) {
                    lblchargeName.setText("Insurance Company : " +  UserData.insuranceCompanyDetailsModel.Name);
                 //   lbl_chargeAmount.setText(Helper.insuranceModel.insurance_Cmp_Name);
                    //lbl_chargeAmount.setText( UserData.insuranceCompanyDetailsModel.Name);
                } if (j==1){
                    lblchargeName.setText("Insurance Policy No : " + UserData.insuranceModel.PolicyNo);
                   // lbl_chargeAmount.setText(Helper.insuranceModel.policy_No);
                   //lbl_chargeAmount.setText(UserData.insuranceModel.PolicyNo);
                }if (j==2){
                    lblchargeName.setText("Insurance Expiry Date : " + Helper.getDateDisplay(DateType.fulldate, UserData.insuranceModel.ExpiryDate));
                   // lbl_chargeAmount.setText(Helper.insuranceModel.expiryDate);
                    //lbl_chargeAmount.setText(Helper.getDateDisplay(DateType.fulldate, UserData.insuranceModel.ExpiryDate));
                }

                binding.rlInsuranceCover2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putInt("key", 1);
                        UserData.customer.FullName = UserData.loginResponse.LogedInCustomer.FullName;
                        UserData.customer.MobileNo = UserData.loginResponse.LogedInCustomer.MobileNo;
                        UserData.customer.Email = UserData.loginResponse.LogedInCustomer.Email;
                        Helper.insertinsuarancefromreservation = true;
                        NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                                .navigate(R.id.action_Select_addtional_options_to_insurance_policy_list, bundle);
                    }
                });

                String ins = loginRes.modeltostring(TAG,ReservationDeclineInsuranceModel);
                Log.e(TAG, "declineInsurance: " + ins );
                activationDetail.put(52, ins);
                updateReservationSummarry(52);
                ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                binding.rlInsuranceCover2.addView(linearLayout);

            }
        }
    }

    public void getInsurance(){
        try {
            JSONArray array = new JSONArray(getArguments().getString("insuranceOption"));
            Boolean dcheckon = true;
            binding.rlInsuranceCover.removeAllViews();
            for (int i = 0; i <array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                getSubview(i);
                TaxDetailsListBinding insurance = TaxDetailsListBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false);
                insurance.getRoot().setId(200 + i);
                insurance.getRoot().setLayoutParams(subparams);
                JSONObject extra = obj.getJSONObject("ReservationSummaryDetailModel");
                insurance.txtInsuranceDesc.setText(obj.getString("Description"));
                DecimalFormat df = new DecimalFormat("#.00");
                Double test = Double.valueOf(obj.getString("PerDayCharge"));
                String text = df.format(test);
                insurance.txtTotalCharge.setText(text);
                insurance.txtInsuranceName.setText(obj.getString("Name"));
                insurance.currency.setText(Helper.displaycurrency);
                //MySpannable mySpannable = new MySpannable(true);
                //mySpannable.makeTextViewResizable1(insurance.txtInsuranceDesc, 2, "See More", true);
               /* MySpannable.makeTextViewResizable(insurance.txtInsuranceDesc, 2, "See More", true);*/
               // insurance.switch2.setId(obj.getInt("Id"));
                Log.e(TAG, "getInsurance: "+ UserData.reservationBusinessSource.InsuraceCoverId);
                if (condition) {
                    if (obj.getBoolean("IsSelected")) {
                        if (dcheckon) {
                            dcheckon = false;
                            insurance.switch2.setChecked(true);
                            previousSwitch = insurance.switch2;
                            activeid.put(obj.getInt("Id"), true);
                            activeid2.put(true, obj.getInt("Id"));
                            reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                            reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                            reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                            reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                            reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("DetailId");
                            reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                   /*     if (reserversationSummary.ReservationOriginDataModels.size() == 0) {
                            reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(52, obj.toString()));
                        }*/
                            activationDetail.put(52, obj.toString());
                            updateReservationSummarry(52);
                            new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                        }
                    }
                }

                if (activeid2.get(true) != null  && activeid2.get(true) == obj.getInt("Id")){
                    insurance.switch2.setChecked(true);
                    reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                    reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                    reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                    reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                    reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("DetailId");
                    reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                }
               /*
               default insurance is not selected set defult last true use this
               if (getArguments().getInt("idd") == obj.getInt("Id")){
                    insurance.switch2.setChecked(true);
                    previousSwitch = insurance.switch2;
                    activeid.put(obj.getInt("Id"), true);
                    activeid2.put(true,obj.getInt("Id"));
                    reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                    reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                    reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                    reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                    reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("DetailId");
                    reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                   *//*     if (reserversationSummary.ReservationOriginDataModels.size() == 0) {
                            reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(52, obj.toString()));
                        }*//*
                    activationDetail.put(52, obj.toString());
                    updateReservationSummarry(52);
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                }*/

                /*else {
                    insurance.switch2.setChecked(true);
                    previousSwitch = insurance.switch2;
                    activeid.put(obj.getInt("Id"), true);
                    activeid2.put(true,obj.getInt("Id"));
                    reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                    reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                    reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                    reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                    reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("Id");
                    reserversationSummary.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                    activationDetail.put(52, obj.toString());
                    updateReservationSummarry(52);
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                }*/

                insurance.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                       // fullProgressbar.show();
                        try {
                            previousSwitch.setChecked(false);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                       /* if (Fragment_Vehicles_Available.userIns) {
                            previousSwitch = insurance.switch2;
                        }*/
                        activeid.put(obj.getInt("Id"), isChecked);
                        activeid2.put(isChecked,obj.getInt("Id"));
                       /* for (int j = 0; j < reserversationSummary.ReservationOriginDataModels.size(); j++) {
                            if (reserversationSummary.ReservationOriginDataModels.get(j).TableType == 52) {
                                if (isChecked) {
                                    reserversationSummary.ReservationOriginDataModels.get(j).JsonData = obj.toString();
                                }
                            }
                        }*/

                        if (isChecked) {
                            activationDetail.put(52, obj.toString());
                            updateReservationSummarry(52);
                        }

                        if (reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId == obj.getInt("Id")) {
                           // insurance.switch2.setChecked(false);
                        } else {
                           // insurance.switch2.setChecked(true);
                            reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                            reserversationSummary.ReservationInsuranceModel.IsInsuranceDecline = false;
                            reserversationSummary.ReservationInsuranceModel.NoOfDays = UserReservationData.reservationTimeModel.TotalDays;
                            reserversationSummary.ReservationInsuranceModel.Remarks = obj.getString("Description");
                            reserversationSummary.ReservationInsuranceModel.InsuranceCoverDetailId = obj.getInt("DetailId");
                            reserversationSummary.ReservationInsuranceModel.InsuranceDate =  (getArguments().getString("dropdate"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);
                });
                binding.rlInsuranceCover.addView(insurance.getRoot());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void removeEquipment(int id){

        Iterator itr = reserversationSummary.ReservationEquipmentInventoryModel.iterator();
        while (itr.hasNext()){
           /* ArrayList<RIequipment> EquipmentInventoryModel = new ArrayList<RIequipment>();
            EquipmentInventoryModel  =   itr.next();*/

            RIequipment rIequipment = new RIequipment();
            rIequipment = (RIequipment) itr.next();
            if (rIequipment.EquipInventId == id){
                itr.remove();
            }
        }
    }

    public void removeInvtory(int id){
        Iterator itr = reserversationSummary.ReservationChargesModels.iterator();

        while (itr.hasNext()){
            ReservationChargesModels reservationChargesModels = new ReservationChargesModels();
            reservationChargesModels = (ReservationChargesModels) itr.next();
            if (reservationChargesModels.ChargeFor == id){
                itr.remove();
            }
        }
    }

    public void removeInventory(int id){
        Iterator itr = reserversationSummary.MiscellaneousChargeModels.iterator();

        while (itr.hasNext()){
            RIchauffer rIchauffer = new RIchauffer();
            rIchauffer = (RIchauffer) itr.next();
            if (rIchauffer.DetailId == id){
                itr.remove();
            }
        }
    }

    private Boolean validation(){
        Boolean value = false;
        if ( reserversationSummary.ReservationInsuranceModel.NoOfDays == 0){
            CustomToast.showToast(getActivity(),"Please Select Insurance", 1);
        } else {
            value = true;
        }


        return value;
    }

}