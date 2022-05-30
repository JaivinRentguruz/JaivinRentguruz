package com.rentguruz.app.flexiicar.booking2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.model.display.Pickupdrop;
import com.bumptech.glide.Glide;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentFinalizeYourRentalBinding;
import com.rentguruz.app.databinding.ListAdditionalDriverBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.base.UserReservationData;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RateModel;
import com.rentguruz.app.model.response.ReservationOriginDataModels;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.UpdateDL;
import com.rentguruz.app.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.RANDUMNUMBER;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONINSERT;

public class Fragment_Finalize_Your_Rental extends BaseFragment
{
   // ImageView backarrow,SummaryOfChargeArrowdown,SummaryOfChargeArrow,CarImage;
   // Handler handler = new Handler();
   // public static Context context;
   // Bundle BookingBundle,VehicleBundle;
   // LinearLayout TermCondition_layout,lblpay,SummaryOfChargeLayout,greenlayout,blacklayout;
   // TextView txt_PickLocName,txt_ReturnLocName,txt_PickupDate,txt_ReturnDate,txt_PickupTime,txt_ReturnTIme,txtDays,txt_vehicletype,
   //         txt_vehName,txtMileage,txt_rate,txtTotalAmount,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txtpayNow,txtPayLater,txt_Discard,txtvehDesc;

   // ImageView driverdetails_icon1,arrowflight_details;

  //  Bundle returnLocationBundle, locationBundle;
  //  Boolean locationType, initialSelect;

    ImageLoader imageLoader;
    String serverpath="",VehImage="";
    String id="";
   // TextView driverName,driverPhone,driverEmail;
   // TextView txt_driverdetails,txt_AsGuestdriver;
    double totalMilesAllowed;
    int cmP_DISTANCE;

    ReservationSummarry reserversationSummary = new ReservationSummarry();
    Pickupdrop pickupdrop;
    VehicleModel model = new VehicleModel();
    LocationList pickuplocation = new LocationList();
    LocationList droplocation = new LocationList();
    RateModel rateModel = new RateModel();
 //   Bundle bundle = new Bundle();
 //   public DoHeader header;
 DateConvert dateConvert = new DateConvert();
    FragmentFinalizeYourRentalBinding binding;

    final Boolean[] isOnePressed = {true};
    final Boolean[] isSecondPlace = {true};

    ReservationSummaryModels[] charges;
    UpdateDL updateDL;
    public static HashMap<Integer, String> activationDetail = new HashMap<>();
    public static HashMap<Integer,Integer> additionalDriver = new HashMap<>();
    SummaryDisplay summaryDisplay;
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Helper.BACKTO = true;
        binding = FragmentFinalizeYourRentalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void setInitialSavedState(@Nullable @org.jetbrains.annotations.Nullable SavedState state) {
        super.setInitialSavedState(state);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        summaryDisplay = new SummaryDisplay(getActivity());
        pickupdrop = new Pickupdrop();
        Log.e(TAG, "onViewCreated: "+getArguments().getString("miles") );
        try {
            bundle.putSerializable("timemodel",getArguments().getSerializable("timemodel"));
            bundle.putSerializable("model",getArguments().getSerializable("model"));
            bundle.putSerializable("models", getArguments().getSerializable("models"));
            bundle.putSerializable("Model",getArguments().getSerializable("Model"));
            bundle.putSerializable("pickuploc", getArguments().getSerializable("pickuploc"));
            bundle.putSerializable("droploc", getArguments().getSerializable("droploc"));
            bundle.putSerializable("ratemaster", getArguments().getSerializable("ratemaster"));
            bundle.putSerializable("vechicle", getArguments().getSerializable("vechicle"));
            bundle.putString("netrate",getArguments().getString("netrate"));
            //bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("defaultcard",getArguments().getSerializable("defaultcard"));
            bundle.putInt("frag",2);
            bundle.putSerializable("summarry",getArguments().getSerializable("summarry") );
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("charges",getArguments().getSerializable("charges"));
            bundle.putString("DeliveryAndPickupModel", getArguments().getString("DeliveryAndPickupModel"));
            bundle.putString("insuranceOption",getArguments().getString("insuranceOption"));
            bundle.putSerializable("reservationSum",getArguments().getSerializable("reservationSum"));
            Log.d(TAG, "onViewCreated: " + TAG);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


            model = (VehicleModel) getArguments().getSerializable("vechicle");
            pickuplocation = (LocationList) getArguments().getSerializable("pickuploc");
            droplocation = (LocationList) getArguments().getSerializable("droploc");
            rateModel = (RateModel) getArguments().getSerializable("ratemaster");
            reserversationSummary = (ReservationSummarry) getArguments().getSerializable("summarry");
            updateDL = new UpdateDL();
            loginRes.testingLog(TAG,reserversationSummary);
            charges = (ReservationSummaryModels[]) getArguments().getSerializable("charges");
            bundle.putSerializable("charges",charges);
            bundle.putSerializable("pickuploc", pickuplocation);
            bundle.putSerializable("model", pickuplocation);
            bundle.putSerializable("droploc", droplocation);
            bundle.putSerializable("models", droplocation);
            bundle.putSerializable("ratemaster", rateModel);
            bundle.putSerializable("ratemaster", rateModel);
            bundle.putSerializable("vechicle", model);
            //bundle.putSerializable("Model", model);
            bundle.putSerializable("defaultcard","");
            bundle.putString("netrate",getArguments().getString("netrate"));
           // bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("summarry",reserversationSummary );
            bundle.putString("testSummerry",getArguments().getString("testSummerry"));
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("LicenseBundle",getArguments().getSerializable("LicenseBundle"));
            //reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(104,getArguments().getString("testSummerry")));
            //updateReservationSummarry(104,getArguments().getString("testSummerry"));
            activationDetail.put(104,getArguments().getString("testSummerry"));
            updateReservationSummarry(104);
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();
            Fragment_Select_addition_Options.condition = false;


     /*       binding.txtPickupLoationDate.setText(dateConvert.allDateConverter(DateType.yyyyMMddD,getArguments().getString("pickupdate"),DateType.ddMMyyyyS));
            binding.txtReturnLocationDate.setText(dateConvert.allDateConverter(DateType.yyyyMMddD,getArguments().getString("dropdate"),DateType.ddMMyyyyS));
            binding.txtPickupLocationTime.setText(dateConvert.allDateConverter(DateType.time,getArguments().getString("pickuptime"),DateType.time2));
            binding.txtReturnLocationTime.setText(dateConvert.allDateConverter(DateType.time,getArguments().getString("droptime"),DateType.time2));*/

            /*binding.txtPickupLoationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")));
            binding.txtPickupLocationTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
            binding.txtReturnLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")));
            binding.txtReturnLocationTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
            binding.txtReturnLocation.setText(droplocation.Name);
            binding.txtPickupLocation.setText(pickuplocation.Name);*/

            pickupdrop.pickupdate = reserversationSummary.CheckOutDate+":00";
            pickupdrop.dropdate = reserversationSummary.CheckInDate+":00";
            pickupdrop.pickuploc = pickuplocation.Name;
            pickupdrop.droploc = droplocation.Name;

            binding.btm.fueltype.setText(Helper.fueltype);
            binding.btm.fcurrency.setText(Helper.displaycurrency);
            //reserversationSummary.MiscellaneousChargeModels.get()

            for (int i = 0; i <reserversationSummary.MiscellaneousChargeModels.size() ; i++) {
               if (reserversationSummary.MiscellaneousChargeModels.get(i).ChargeType == 1) {
                   //binding.txtDriverdetails.setOnClickListener(this);
                   binding.defaultDriver.driverDetails.setOnClickListener(this);
               }
            }



           try {
               updateDL = (UpdateDL) getArguments().getSerializable("LicenseBundle");
           } catch (Exception e){
               e.printStackTrace();
           }

           //binding.textViewTotalDays.setText(String.valueOf(UserReservationData.reservationTimeModel.TotalDays));
            pickupdrop.noDays = UserReservationData.reservationTimeModel.TotalDays;
            binding.pickups.setPickupdrop(pickupdrop);
            binding.textVVehicleModelName.setText(model.VehicleName);
         // binding.txtVTypeName.setText(model.VehicleShortName);
            binding.txtVTypeName.setText(model.VehicleCategory);
            binding.textViewSeats.setText(String.valueOf(model.NoOfSeats));
            binding.textViewBags.setText(String.valueOf(model.NoOfBags));

            //txt_Automatic.setText(model.);
            binding.textViewDoors.setText(String.valueOf(model.NoOfDoors));
        //    txt_rate.setText(getArguments().getString("netrate"));
           // binding.currency.setText(Helper.currencySymbol);
            binding.btm.textviewTotalAmount.setText(getArguments().getString("netrate"));
            binding.btm.txtMileage.setText(getArguments().getString("miles"));
            binding.txtvehDesciption.setText(model.VehDescription);

            Glide.with(getContext()).load(model.DefaultImagePath).into(binding.VehImageBg3);
            binding.DiscardFinalizeRen.setOnClickListener(this);
            binding.backbtn2.setOnClickListener(this);
            binding.txtCharges.setOnClickListener(this);
         /*   binding.imgBottomArrow.setOnClickListener(this);
            binding.SummaryofChargesArrowDown.setOnClickListener(this);*/
            binding.greenLayout1.setOnClickListener(this);
            binding.blackLayout1.setOnClickListener(this);
            binding.flightDetailsLayout.setOnClickListener(this);
            binding.lbltermCondition.setOnClickListener(this);
            binding.btm.layoutPayment.setOnClickListener(this);
            binding.txtDriverdetails.setVisibility(View.VISIBLE);
            binding.txtAsGuestdriver.setVisibility(View.GONE);
            binding.txtDriverdetails.setOnClickListener(this);
            String ddd = "{\"CompanyId\":1,\"Name\":\"Business Source One\",\"Code\":\"123\",\"ReservationTypeId\":16,\"AgreementTypeId\":2,\"IsRateSelect\":true,\"RateId\":3,\"IsVehicleCategorySelect\":true,\"VehicleCategoryId\":20,\"IsInsuranceSelect\":true,\"InsuraceCoverId\":1,\"IsInvoiceToSelect\":true,\"InvoiceTo\":2,\"IsReferralSelect\":true,\"ReferralId\":4,\"PromotionalCodeId\":null,\"LocationMasterIds\":\"10, 12, 8, 9\",\"VehicleTypeMasterIds\":\"1, 2, 3\",\"lstBusinessSourceLocationMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"LocationId\":9,\"BusinessSourceName\":null,\"LocationName\":null}],\"lstBusinessSourceVehicleTypeMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"VehicleTypeId\":3,\"BusinessSourceName\":null,\"VehicleTypeName\":null}],\"ReservationTypeName\":\"Online\",\"AgreementTypeName\":\"Insurance\",\"RateName\":\"Base Rate\",\"VehicleCategoryName\":\"In House Fleet\",\"InsuranceCoverName\":\"Basic\",\"InvoiceToName\":null,\"ReferralName\":\"John & Company\",\"fLocationId\":0,\"Id\":4,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":{\"pageSize\":0,\"limit\":0,\"offset\":0,\"orderDir\":null,\"orderBy\":null,\"filter\":null,\"filterObj\":null},\"TotalRecord\":1,\"fIds\":null,\"APIRequestType\":1}";
           // reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(71, ddd));
           // updateReservationSummarry(71,ddd);
            activationDetail.put(71,ddd);
            updateReservationSummarry(71);
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            String default_Email=sp.getString("default_Email","");


          /*    try {

                reserversationSummary.CustomerEmail = UserData.customer.Email;
                reserversationSummary.CustomerId = UserData.customer.Id;
                reserversationSummary.CustomerPhone = UserData.customer.MobileNo;
                binding.customerDetails.setCustomer(UserData.customer);

              } catch (Exception e){
                  e.printStackTrace();
              }*/

            reserversationSummary.CustomerEmail = default_Email;
            reserversationSummary.CustomerId = UserData.loginResponse.User.UserFor;
            reserversationSummary.CustomerPhone = UserData.loginResponse.LogedInCustomer.MobileNo;

           // reserversationSummary.BusinessSourceId = 4;
            reserversationSummary.BusinessSourceId = UserData.reservationBusinessSource.Id;
            reserversationSummary.ReservationStatus = 1;
            //reserversationSummary.ReservationType = 16;
            reserversationSummary.ReservationType = UserData.reservationBusinessSource.ReservationTypeId;
            reserversationSummary.TypeOf = 1;

            //String d =  CustomeDialog.dateConvert2(binding.txtReturnLocationDate.getText().toString());
           // String d = Helper.setPostDate(binding.txtReturnLocationDate.getText().toString());
            //String d2 =CustomeDialog.dateConvert2(binding.txtPickupLoationDate.getText().toString());
            //String d2 =Helper.setPostDate(binding.txtPickupLoationDate.getText().toString());
            //reserversationSummary.CheckOutDate =d2+"T"+getArguments().getString("droptime");
            //reserversationSummary.CheckInDate =d +"T"+getArguments().getString("pickuptime");

            //SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            serverpath = sp.getString("serverPath", "");
            cmP_DISTANCE=sp.getInt("cmP_DISTANCE",0);


            binding.defaultDriver.textVDriverName.setText(UserData.loginResponse.LogedInCustomer.FullName);
            CustomBindingAdapter.camelcase(binding.defaultDriver.textVDriverName,UserData.loginResponse.LogedInCustomer.FullName);
            binding.defaultDriver.TextVDriverEmail.setText(UserData.loginResponse.LogedInCustomer.Email);
            binding.defaultDriver.TextVDriverPhoneno.setText(UserData.loginResponse.LogedInCustomer.MobileNo);
            CustomBindingAdapter.capss(binding.defaultDriver.first, UserData.loginResponse.LogedInCustomer.FullName);

           /* binding.defaultDriver.textVDriverName.setText(UserData.customer.FullName);
            binding.defaultDriver.TextVDriverEmail.setText(UserData.customer.Email);
            binding.defaultDriver.TextVDriverPhoneno.setText(UserData.customer.MobileNo);*/
            binding.defaultDriver.driverDetails.setOnClickListener(this);
            //binding.customer.setOnClickListener(this);
          //  apiService = new ApiService(RateMaster, RequestType.GET,TESTING+model.RateId,BASE_URL_LOGIN, header, new JSONObject());

            binding.refund.setText(Helper.getAmtount(Double.valueOf(model.SecurityDeposit),true) + " / REFUNDABLE");


            binding.LinearLSummaryOfCharges.setVisibility(View.VISIBLE);
            /*for (int i = 0; i <charges.length ; i++) {
                getSubview(i);
                VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                vehicleTaxDetailsBinding.txtChargeName.setText(charges[i].SummaryName);
                vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount( charges[i].TotalAmount,false));
                int finalI = i;
                vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                            vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                        } else {
                            vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                            String join ="";
                            for (int j = 0; j <charges[finalI].ReservationSummaryDetailModels.length ; j++) {
                                         *//*   vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description);
                                            if (charges[finalI].ReservationSummaryDetailModels[j].Title == charges[finalI].ReservationSummaryDetailModels[j].Description){
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            }*//*

                                join +=  charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                        charges[finalI].ReservationSummaryDetailModels[j].Description+"\n";
                                if (join.trim().matches("null null")) {
                                    vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                } else {
                                    vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                }
                                vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                            }

                        }
                    }
                });

                vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                String join ="";
                for (int j = 0; j <charges[finalI].ReservationSummaryDetailModels.length ; j++) {
                                         *//*   vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description);
                                            if (charges[finalI].ReservationSummaryDetailModels[j].Title == charges[finalI].ReservationSummaryDetailModels[j].Description){
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            }*//*

                    join +=  charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                            charges[finalI].ReservationSummaryDetailModels[j].Description+"\n";
                    if (join.trim().matches("null null")) {
                        vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                    } else {
                        vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                    }
                    vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                }


                binding.rlSummaryOfCharges.addView(vehicleTaxDetailsBinding.getRoot());
            }*/

            summaryDisplay.getB2BSummarry(bundle,charges,binding.rlSummaryOfCharges);
            binding.driverdetails1.setVisibility(View.VISIBLE);

            try {
                getSubview(0);
                ListAdditionalDriverBinding additionalDriverBinding = ListAdditionalDriverBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false);
                additionalDriverBinding.getRoot().setId(200 + 0);
                additionalDriverBinding.getRoot().setLayoutParams(subparams);
                additionalDriverBinding.TextVDriverEmail.setText(updateDL.Email);
                additionalDriverBinding.textVDriverName.setText(updateDL.FullName);
                additionalDriverBinding.TextVDriverPhoneno.setText(updateDL.PhoneNo);
                additionalDriver.put(0,updateDL.Id);
                binding.showGuestDriver.addView(additionalDriverBinding.getRoot());
            } catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener RateMaster = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            System.out.println("Success");
            System.out.println(response);


            try {
                JSONObject responseJSON = new JSONObject(response);
                JSONObject data = responseJSON.getJSONObject("Data");
                reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(8, data.toString()));
               // updateReservationSummarry(8,data.toString());


    //            String testin =  "{"CompanyId":1,"Name":"Business Source One","Code":"123","ReservationTypeId":16,"AgreementTypeId":2,"IsRateSelect":true,"RateId":3,"IsVehicleCategorySelect":true,"VehicleCategoryId":20,"IsInsuranceSelect":true,"InsuraceCoverId":1,"IsInvoiceToSelect":true,"InvoiceTo":2,"IsReferralSelect":true,"ReferralId":4,"PromotionalCodeId":null,"LocationMasterIds":"10, 12, 8, 9","VehicleTypeMasterIds":"1, 2, 3","lstBusinessSourceLocationMappings":[{"Id":0,"BusinessSourceId":4,"LocationId":9,"BusinessSourceName":null,"LocationName":null}],"lstBusinessSourceVehicleTypeMappings":[{"Id":0,"BusinessSourceId":4,"VehicleTypeId":3,"BusinessSourceName":null,"VehicleTypeName":null}],"ReservationTypeName":"Online","AgreementTypeName":"Insurance","RateName":"Base Rate","VehicleCategoryName":"In House Fleet","InsuranceCoverName":"Basic","InvoiceToName":null,"ReferralName":"John & Company","fLocationId":0,"Id":4,"DetailId":0,"IsActive":true,"auditLogModel":null,"dataTableRequestModel":{"pageSize":0,"limit":0,"offset":0,"orderDir":null,"orderBy":null,"filter":null,"filterObj":null},"TotalRecord":1,"fIds":null,"APIRequestType":1}"
      //         JSONObject datas =  "{\"CompanyId\":1,\"Name\":\"Business Source One\",\"Code\":\"123\",\"ReservationTypeId\":16,\"AgreementTypeId\":2,\"IsRateSelect\":true,\"RateId\":3,\"IsVehicleCategorySelect\":true,\"VehicleCategoryId\":20,\"IsInsuranceSelect\":true,\"InsuraceCoverId\":1,\"IsInvoiceToSelect\":true,\"InvoiceTo\":2,\"IsReferralSelect\":true,\"ReferralId\":4,\"PromotionalCodeId\":null,\"LocationMasterIds\":\"10, 12, 8, 9\",\"VehicleTypeMasterIds\":\"1, 2, 3\",\"lstBusinessSourceLocationMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"LocationId\":9,\"BusinessSourceName\":null,\"LocationName\":null}],\"lstBusinessSourceVehicleTypeMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"VehicleTypeId\":3,\"BusinessSourceName\":null,\"VehicleTypeName\":null}],\"ReservationTypeName\":\"Online\",\"AgreementTypeName\":\"Insurance\",\"RateName\":\"Base Rate\",\"VehicleCategoryName\":\"In House Fleet\",\"InsuranceCoverName\":\"Basic\",\"InvoiceToName\":null,\"ReferralName\":\"John & Company\",\"fLocationId\":0,\"Id\":4,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":{\"pageSize\":0,\"limit\":0,\"offset\":0,\"orderDir\":null,\"orderBy\":null,\"filter\":null,\"filterObj\":null},\"TotalRecord\":1,\"fIds\":null,\"APIRequestType\":1}"}"";

               //String datas =   "\\\"CompanyId\\\":1,\\\"Name\\\":\\\"Business Source One\\\",\\\"Code\\\":\\\"123\\\",\\\"ReservationTypeId\\\":16,\\\"AgreementTypeId\\\":2,\\\"IsRateSelect\\\":true,\\\"RateId\\\":3,\\\"IsVehicleCategorySelect\\\":true,\\\"VehicleCategoryId\\\":20,\\\"IsInsuranceSelect\\\":true,\\\"InsuraceCoverId\\\":1,\\\"IsInvoiceToSelect\\\":true,\\\"InvoiceTo\\\":2,\\\"IsReferralSelect\\\":true,\\\"ReferralId\\\":4,\\\"PromotionalCodeId\\\":null,\\\"LocationMasterIds\\\":\\\"10, 12, 8, 9\\\",\\\"VehicleTypeMasterIds\\\":\\\"1, 2, 3\\\",\\\"lstBusinessSourceLocationMappings\\\":[{\\\"Id\\\":0,\\\"BusinessSourceId\\\":4,\\\"LocationId\\\":9,\\\"BusinessSourceName\\\":null,\\\"LocationName\\\":null}],\\\"lstBusinessSourceVehicleTypeMappings\\\":[{\\\"Id\\\":0,\\\"BusinessSourceId\\\":4,\\\"VehicleTypeId\\\":3,\\\"BusinessSourceName\\\":null,\\\"VehicleTypeName\\\":null}],\\\"ReservationTypeName\\\":\\\"Online\\\",\\\"AgreementTypeName\\\":\\\"Insurance\\\",\\\"RateName\\\":\\\"Base Rate\\\",\\\"VehicleCategoryName\\\":\\\"In House Fleet\\\",\\\"InsuranceCoverName\\\":\\\"Basic\\\",\\\"InvoiceToName\\\":null,\\\"ReferralName\\\":\\\"John & Company\\\",\\\"fLocationId\\\":0,\\\"Id\\\":4,\\\"DetailId\\\":0,\\\"IsActive\\\":true,\\\"auditLogModel\\\":null,\\\"dataTableRequestModel\\\":{\\\"pageSize\\\":0,\\\"limit\\\":0,\\\"offset\\\":0,\\\"orderDir\\\":null,\\\"orderBy\\\":null,\\\"filter\\\":null,\\\"filterObj\\\":null},\\\"TotalRecord\\\":1,\\\"fIds\\\":null,\\\"APIRequestType\\\":1";
               // String datas =   "\"CompanyId\":1,\"Name\":\"Business Source One\",\"Code\":\"123\",\"ReservationTypeId\":16,\"AgreementTypeId\":2,\"IsRateSelect\":true,\"RateId\":3,\"IsVehicleCategorySelect\":true,\"VehicleCategoryId\":20,\"IsInsuranceSelect\":true,\"InsuraceCoverId\":1,\"IsInvoiceToSelect\":true,\"InvoiceTo\":2,\"IsReferralSelect\":true,\"ReferralId\":4,\"PromotionalCodeId\":null,\"LocationMasterIds\":\"10, 12, 8, 9\",\"VehicleTypeMasterIds\":\"1, 2, 3\",\"lstBusinessSourceLocationMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"LocationId\":9,\"BusinessSourceName\":null,\"LocationName\":null}],\"lstBusinessSourceVehicleTypeMappings\":[{\"Id\":0,\"BusinessSourceId\":4,\"VehicleTypeId\":3,\"BusinessSourceName\":null,\"VehicleTypeName\":null}],\"ReservationTypeName\":\"Online\",\"AgreementTypeName\":\"Insurance\",\"RateName\":\"Base Rate\",\"VehicleCategoryName\":\"In House Fleet\",\"InsuranceCoverName\":\"Basic\",\"InvoiceToName\":null,\"ReferralName\":\"John & Company\",\"fLocationId\":0,\"Id\":4,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":{\"pageSize\":0,\"limit\":0,\"offset\":0,\"orderDir\":null,\"orderBy\":null,\"filter\":null,\"filterObj\":null},\"TotalRecord\":1,\"fIds\":null,\"APIRequestType\":1";
                //String datas2 =   "CompanyId":1,"Name":"Business Source One","Code":"123","ReservationTypeId":16,"AgreementTypeId":2,"IsRateSelect":true,"RateId":3,"IsVehicleCategorySelect":true,"VehicleCategoryId":20,"IsInsuranceSelect":true,"InsuraceCoverId":1,"IsInvoiceToSelect":true,"InvoiceTo":2,"IsReferralSelect":true,"ReferralId":4,"PromotionalCodeId":null,"LocationMasterIds":"10, 12, 8, 9","VehicleTypeMasterIds":"1, 2, 3","lstBusinessSourceLocationMappings":[{"Id":0,"BusinessSourceId":4,"LocationId":9,"BusinessSourceName":null,"LocationName":null}],"lstBusinessSourceVehicleTypeMappings":[{"Id":0,"BusinessSourceId":4,"VehicleTypeId":3,"BusinessSourceName":null,"VehicleTypeName":null}],"ReservationTypeName":"Online","AgreementTypeName":"Insurance","RateName":"Base Rate","VehicleCategoryName":"In House Fleet","InsuranceCoverName":"Basic","InvoiceToName":null,"ReferralName":"John & Company","fLocationId":0,"Id":4,"DetailId":0,"IsActive":true,"auditLogModel":null,"dataTableRequestModel":{"pageSize":0,"limit":0,"offset":0,"orderDir":null,"orderBy":null,"filter":null,"filterObj":null},"TotalRecord":1,"fIds":null,"APIRequestType":1";

                loginRes.testingLog(TAG,reserversationSummary);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {

        }
    };

    OnResponseListener getTaxtDetails = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
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
                            final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");

                            final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rl_SummaryOfCharges);

                            int len;
                            len = getsummaryOfCharges.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                final int sortId = test.getInt("sortId");

                                if(test.has("chargeCode"))
                                {
                                    final String chargeCode = test.getString("chargeCode");
                                }

                                final String chargeName = test.getString("chargeName");
                                final Double chargeAmount = test.getDouble("chargeAmount");

                                if (chargeName.equals("Estimated Total"))
                                {
                                    binding.btm.textviewTotalAmount.setText(((String.format(Locale.US,"%.2f",chargeAmount))));
                                    //binding.textviewTotalAmount.setText(Helper.getAmtount(chargeAmount, true));
                                  //  BookingBundle.putDouble("total",chargeAmount);
                                }
                                if (sortId==1)
                                {
                                    binding.txtPaymentN.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                    binding.txtPaymentL.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                }

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 0, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                linearLayout.setId(200 + j);
                                linearLayout.setLayoutParams(lp);

                                TextView txt_charge, txt_chargeName;
                           /*    // LinearLayout arrowIcon = (LinearLayout) linearLayout.findViewById(R.id.arrow_icon);

                                if (sortId == 10)
                                {
                                    arrowIcon.setVisibility(View.VISIBLE);
                                }
                                arrowIcon.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Bundle Booking = new Bundle();
                                        *//*Booking.putBundle("VehicleBundle", VehicleBundle);
                                        Booking.putBundle("BookingBundle", BookingBundle);
                                        Booking.putBundle("returnLocation", returnLocationBundle);
                                        Booking.putBundle("location", locationBundle);
                                        Booking.putBoolean("locationType", locationType);
                                        Booking.putBoolean("initialSelect", initialSelect);
                                        BookingBundle.putInt("BookingStep", 4);
                                        Booking.putInt("backtoforterms",2);
                                        Booking.putBoolean("TaxType",true);*//*
                                        NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                                .navigate(R.id.action_Finalize_your_rental_to_Total_tax_fee_details, Booking);
                                    }
                                });*/

                                txt_charge = (TextView) linearLayout.findViewById(R.id.txt_charge);
                                txt_chargeName = (TextView) linearLayout.findViewById(R.id.txt_chargeName);

                                String str=String.format(Locale.US,"%.2f",chargeAmount);
                                txt_charge.setText("USD$ "+str);
                                txt_chargeName.setText(chargeName);

                                if(chargeName.equals("Discount Applied"))
                                {
                                    txt_charge.setTextColor(getResources().getColor(R.color.btn_bg_color_2));
                                }
                                txt_chargeName.setText(chargeName);

                                rlSummaryOfCharge.addView(linearLayout);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        String msg = responseJSON.getString("message");
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

    OnResponseListener GetDrivingLicense = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");

                        final JSONArray getdrivingLicense = resultSet.getJSONArray("Data");

                        // List<UpdateDL> drivingLicenceModels = new ArrayList<>();
                        int len;
                        len = getdrivingLicense.length();
                        binding.showGuestDriver.removeAllViews();
                        for (int j = 0; j < len; j++) {
                            final JSONObject test = (JSONObject) getdrivingLicense.get(j);
                            // loginRes.storedata("dirivingT", test.toString());
                            /*binding.driverName.setText(test.getString("FullName"));
                            binding.driverPhone.setText(test.getString("PhoneNo"));
                            binding.driverEmail.setText(test.getString("Email"));*/

                            getSubview(j);
                            ListAdditionalDriverBinding additionalDriverBinding = ListAdditionalDriverBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            additionalDriverBinding.getRoot().setId(200 + j);
                            additionalDriverBinding.getRoot().setLayoutParams(subparams);
                            additionalDriverBinding.TextVDriverEmail.setText(test.getString("Email"));
                            additionalDriverBinding.textVDriverName.setText(test.getString("FullName"));
                            additionalDriverBinding.TextVDriverPhoneno.setText(test.getString("PhoneNo"));
                            int finalJ = j;
                            additionalDriverBinding.driverDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        binding.showGuestDriver.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                        bundle.putInt("GuestDriverId", test.getInt("Id"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            binding.showGuestDriver.addView(additionalDriverBinding.getRoot());

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
                        binding.btm.layoutPayment.setClickable(true);
                        JSONObject data = responseJSON.getJSONObject("Data");
                        reserversationSummary.Id =data.getInt("Id");
                        bundle.putSerializable("summarry",reserversationSummary );
                        loginRes.testingLog(TAG,reserversationSummary);
                                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, bundle);
                    }  else
                    {
                        binding.btm.layoutPayment.setClickable(true);
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                    binding.btm.layoutPayment.setClickable(true);
                }
            });

        }

        @Override
        public void onError(String error) {
            Log.d("TAG", "onError: " + error);
            binding.btm.layoutPayment.setClickable(true);
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
                        reserversationSummary.ReservationNo = data;
//                        reserversationSummary.ReservationDriversModel.add(new ReservationDriversModel(additionalDriver.get(0)));
                        ApiService2<ReservationSummarry> apiService2 = new ApiService2<ReservationSummarry>(doInsertReservation, RequestType.POST,RESERVATIONINSERT, BASE_URL_LOGIN, header, reserversationSummary);
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

    public void updateReservationSummarry(int key){
        int data = reserversationSummary.ReservationOriginDataModels.size();
        for (int i = 0; i <data; i++) {
            if (reserversationSummary.ReservationOriginDataModels.get(i).TableType == key) {
                reserversationSummary.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(key, activationDetail.get(key))); // activationDetail.get(52).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DiscardFinalizeRen:
                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Search_activity);

             /*   Intent intent = new Intent( getActivity(), Activity_Agreements.class);
                startActivity(intent);*/
                break;
            case R.id.backbtn2:
               // getActivity().getSupportFragmentManager().popBackStack();
                //NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this).popBackStack();

                Helper.isFirstB2CReservation = false;
                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Select_addtional_options, bundle);
                break;
            case R.id.txt_charges:
                    if(binding.LinearLSummaryOfCharges.getVisibility() == View.VISIBLE)
                    {
                        binding.LinearLSummaryOfCharges.setVisibility(View.GONE);
                        binding.imgBottomArrow.setVisibility(View.VISIBLE);
                        binding.SummaryofChargesArrowDown.setVisibility(View.GONE);
                    } else {
                        binding.LinearLSummaryOfCharges.setVisibility(View.VISIBLE);
                        binding.imgBottomArrow.setVisibility(View.GONE);
                        binding.SummaryofChargesArrowDown.setVisibility(View.VISIBLE);
                        summaryDisplay.getB2BSummarry(bundle,charges,binding.rlSummaryOfCharges);
                        /*for (int i = 0; i <charges.length ; i++) {
                            getSubview(i);
                            VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                            vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                            vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            vehicleTaxDetailsBinding.txtChargeName.setText(charges[i].SummaryName);
                            vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount( charges[i].TotalAmount,false));
                            int finalI = i;
                            vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                                        vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                    } else {
                                        vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                        String join ="";
                                        for (int j = 0; j <charges[finalI].ReservationSummaryDetailModels.length ; j++) {
                                         *//*   vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description);
                                            if (charges[finalI].ReservationSummaryDetailModels[j].Title == charges[finalI].ReservationSummaryDetailModels[j].Description){
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            }*//*

                                            join +=  charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description+"\n";
                                            if (join.trim().matches("null null"))
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                        }

                                    }
                                }
                            });
                            binding.rlSummaryOfCharges.addView(vehicleTaxDetailsBinding.getRoot());
                        }*/
                    }
                    break;
            case R.id.green_layout1:
                isOnePressed[0] = true;
                binding.greenLayout1.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_yellow_green));
                if (isSecondPlace[0]) {
                    binding.blackLayout1.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_blackbox));
                    isSecondPlace[0] = false;
                }
                break;
            case R.id.black_layout1:
                binding.blackLayout1.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_yellow_green));
                isSecondPlace[0] = true;
                if (isOnePressed[0]) {
                binding.greenLayout1.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_blackbox));
                isOnePressed[0] = false;
                }
                break;
            case R.id.flight_details_layout:


                break;

            case R.id.lblterm_condition:
               // Fragment_Term_And_Condition.popbackstack = false;
                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Term_and_Condtion, bundle);
                break;
            case R.id.layout_payment:
                if (reserversationSummary.Id == 0) {
                    Log.e(TAG, "onClick: " + "click");
                    binding.btm.layoutPayment.setClickable(false);
                    apiService = new ApiService(getReservationNumber, RequestType.GET, RANDUMNUMBER, BASE_URL_LOGIN, header, new JSONObject());
                } else {
                    binding.btm.layoutPayment.setClickable(false);
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, bundle);
                }
               /* NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, bundle);*/
                break;

            case R.id.txt_driverdetails:
                if (binding.driverdetails1.getVisibility() == View.GONE)
                {
                    binding.driverdetails1.setVisibility(View.VISIBLE);
                    binding.driverdetailsIcon1.setVisibility(View.GONE);
                    binding.driverdetailsIcon2.setVisibility(View.VISIBLE);
                   /* apiService = new ApiService(GetDrivingLicense, RequestType.POST,
                            GETLICENSEALL, BASE_URL_LOGIN, header, params.getDrivingLicenseList(UserData.loginResponse.User.UserFor));*/

                    try {
                        getSubview(0);
                        ListAdditionalDriverBinding additionalDriverBinding = ListAdditionalDriverBinding.inflate(subinflater,
                                getActivity().findViewById(android.R.id.content), false);
                        additionalDriverBinding.getRoot().setId(200 + 0);
                        additionalDriverBinding.getRoot().setLayoutParams(subparams);
                        additionalDriverBinding.TextVDriverEmail.setText(updateDL.Email);
                        additionalDriverBinding.textVDriverName.setText(updateDL.FullName);
                        additionalDriverBinding.TextVDriverPhoneno.setText(updateDL.PhoneNo);
                        additionalDriver.put(0,updateDL.Id);
                        binding.showGuestDriver.addView(additionalDriverBinding.getRoot());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    binding.driverdetails1.setVisibility(View.GONE);
                    binding.driverdetailsIcon1.setVisibility(View.VISIBLE);
                    binding.driverdetailsIcon2.setVisibility(View.GONE);
                }
                break;

            case R.id.driver_details:
               /* bundle.putInt("frag",2);
                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Additional_Drever,bundle);*/
                break;

           /* case  R.id.customer:
                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                        .navigate(R.id.action_Vehicle_Finalize_Your_Rental_to_CustomerList);
                break;*/
        }
    }
}

