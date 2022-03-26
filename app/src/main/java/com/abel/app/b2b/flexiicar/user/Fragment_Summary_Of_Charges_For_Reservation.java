package com.abel.app.b2b.flexiicar.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.DateConvert;
import com.abel.app.b2b.adapters.DigitConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.SummaryDisplay;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentSummaryOfChargesBinding;
import com.abel.app.b2b.databinding.VehicleTaxDetailsBinding;
import com.abel.app.b2b.model.CheckOutList;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.CheckList;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.Reservation;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationSummaryModels;
import com.abel.app.b2b.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CHECKOUT;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.CHECKLISTFORACCESORRIES;
import static com.abel.app.b2b.apicall.ApiEndPoint.DELETE;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETLICENSEALL;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Summary_Of_Charges_For_Reservation extends BaseFragment
{
    Bundle ReservationBundle;
    ImageLoader imageLoader;
    int iid;
    ReservationSummaryModels[] charges;
    FragmentSummaryOfChargesBinding binding;
    ReservationSummarry reservationSummarry = new ReservationSummarry();
    DateConvert dateConvert = new DateConvert();
    Reservation reservation = new Reservation();
    SummaryDisplay summaryDisplay;
    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentSummaryOfChargesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ReservationBundle = getArguments();
        //((User_Profile) getActivity()).BottomnavInVisible();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        summaryDisplay = new SummaryDisplay(getActivity());
        iid = getArguments().getInt("Id");
        Log.d(TAG, "onViewCreated: " + iid);
        fullProgressbar.show();
        try {

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            CustomBindingAdapter.loadImage(binding.icon,loginRes.getData(getResources().getString(R.string.icon)));

         /*   txt_PickLocName.setText(ReservationBundle.getString("chk_Out_Location_Name"));
            txt_ReturnLocName.setText(ReservationBundle.getString("chk_In_Loc_Name"));

            txt_vehName.setText(ReservationBundle.getString("vehicle_Name"));
            txt_vehicletype.setText(ReservationBundle.getString("vehicle_Type_Name"));

            String CheckOut=(ReservationBundle.getString("default_Check_Out"));
            // Date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date1 = dateFormat1.parse(CheckOut);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
            String CheckOutStr = sdf1.format(date1);
            txt_PickupDate.setText(CheckOutStr);

            String CheckIn=(ReservationBundle.getString("default_Check_In"));
            //check_Out Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = dateFormat.parse(CheckIn);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
            String CheckInStr = sdf.format(date);
            txt_ReturnDate.setText(CheckInStr);

            String Date=(ReservationBundle.getString("default_Created_Date"));

            *//*SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date date2 = dateFormat2.parse(Date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy,HH:mm aa",Locale.US);
            String Issue_DateStr = sdf2.format(date2);*//*

            txt_createdDate.setText("SEE YOU ON "+(txt_PickupDate.getText().toString()));

            txt_rate.setText(ReservationBundle.getString("rate_ID"));
            reservation_ID=ReservationBundle.getInt("reservation_ID");

            txt_conformationNo.setText(String.valueOf(reservation_ID));

           // txt_Seats.setText(getArguments().getBundle("AgreementsBundle").getString("veh_Seat"));

*/


           // binding.arrowSelfcheckout.setOnClickListener(this);
          //  binding.termCondition.setOnClickListener(this);
            binding.lblAddCalender.setOnClickListener(this);
            binding.llModify.setOnClickListener(this);
            binding.Discardlbl.setOnClickListener(this);
            binding.backToHome.setOnClickListener(this);
            binding.driverdetailsIcon.setOnClickListener(this);
            binding.ArrowForSummary.setOnClickListener(this);
            binding.ArrowDownForSummary.setOnClickListener(this);
            binding.llCancelBooking.setOnClickListener(this);
            binding.CrossSymbolGreen.setOnClickListener(this);
            binding.termCondition.setOnClickListener(this);
            //binding.selfCheckOut.setOnClickListener(this);
            apiService = new ApiService(getTaxtDetails, RequestType.GET,
                    RESERVATIONGETBYID , BASE_URL_LOGIN, header,"?id="+iid);

            binding.option.bookingconfirm.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        binding.optionmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                transition.addTarget( binding.sucessfullRegi);
                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(binding.getRoot(),transition);
                binding.sucessfullRegi.setVisibility(View.VISIBLE);
            }
        });

        binding.option.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                transition.addTarget( binding.sucessfullRegi);
                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(binding.getRoot(),transition);
                binding.sucessfullRegi.setVisibility(View.GONE);
            }
        });



    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

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
                            Boolean status = responseJSON.getBoolean("Status");

                            if (status)
                            {
                                try
                                {
                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                    reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                    bundle.putSerializable("reservationsum",reservationSummarry);
                                    bundle.putSerializable("reservationD",reservationSummarry);
                                    new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
                                    binding.txtPicLocName.setText(reservationSummarry.PickUpLocationName);
                                    binding.txtPicLocDate.setText(Helper.getDateDisplay(DateType.fulldate,reservationSummarry.CheckOutDate));
                                    binding.txtPicLocTime.setText(Helper.getTimeDisplay(DateType.fulldate,reservationSummarry.CheckOutDate));

                                    binding.txtReturnLocName.setText(reservationSummarry.DropLocationName);
                                    binding.txtReturnDate.setText(Helper.getDateDisplay(DateType.fulldate,reservationSummarry.CheckInDate));
                                    binding.txtReturnTime.setText(Helper.getTimeDisplay(DateType.fulldate,reservationSummarry.CheckInDate));
                                  //  binding.txtvehiclETYPENAME.setText(reservationSummarry.VehicleName);
                                   // binding.txtTotalDay1.setText(reservationSummarry.TotalDays);
                                    //binding.txtTotalDay1.setText(reservationSummarry.ReservationInsuranceModel.NoOfDays);
                                    binding.txtTotalDay1.setText(String.valueOf(reservationSummarry.ReservationInsuranceModel.NoOfDays));
                                    binding.txtConformationNo.setText(reservationSummarry.ReservationNo);


                                    binding.txtCreatedDate.setText( "See you on " +dateConvert.allDateConverter(DateType.fulldate,reservationSummarry.CheckOutDate,DateType.ymdd));

                                        binding.arrowSelfcheckout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                binding.arrowSelfcheckout.setVisibility(View.GONE);
                                                binding.arrowSelfcheckout2.setVisibility(View.VISIBLE);
                                                if(binding.LayoutSelfCheckout.getVisibility()== View.GONE)
                                                {
                                                    binding.LayoutSelfCheckoutGreen.setVisibility(View.VISIBLE);
                                                    binding.LayoutSelfCheckout.setVisibility(View.GONE);

                                                    /*if (reservationSummarry.TypeOf == 2) {
                                                        binding.LayoutSelfCheckoutGreen.setVisibility(View.VISIBLE);
                                                    } else {
                                                        binding.LayoutSelfCheckout.setVisibility(View.VISIBLE);
                                                    }*/

                                                  /*  if (reservationSummarry.ReservationStatus == 1) {
                                                        binding.LayoutSelfCheckout.setVisibility(View.VISIBLE);
                                                    } else {
                                                        binding.LayoutSelfCheckoutGreen.setVisibility(View.VISIBLE);
                                                    }*/
                                                }
                                                else {
                                                    binding.LayoutSelfCheckout.setVisibility(View.GONE);
                                                    binding.LayoutSelfCheckoutGreen.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                        binding.arrowSelfcheckout2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                binding.arrowSelfcheckout.setVisibility(View.VISIBLE);
                                                binding.arrowSelfcheckout2.setVisibility(View.GONE);
                                                binding.LayoutSelfCheckout.setVisibility(View.GONE);
                                                binding.LayoutSelfCheckoutGreen.setVisibility(View.GONE);
                                            }
                                        });



                                      //  charges = reservationSummarry.ReservationSummaryModels;
                        /*for (int i = 0; i <reservationSummarry.ReservationSummaryModels.size() ; i++) {

                            getSubview(i);
                            VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                            vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                            vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            vehicleTaxDetailsBinding.txtChargeName.setText(reservationSummarry.ReservationSummaryModels.get(i).SummaryName);
                            //vehicleTaxDetailsBinding.txtCharge.setText(charges[i].TotalAmount.toString());
                            vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount(reservationSummarry.ReservationSummaryModels.get(i).TotalAmount, false));
                            int finalI = i;
                            vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                                        vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                    } else {
                                        vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                        String join ="";
                                        for (int j = 0; j <reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels.length ; j++) {
                                           // vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                           //         charges[finalI].ReservationSummaryDetailModels[j].Description);
                                           join +=  reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Title + " "+
                                                   reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Description+"\n";
                                            if (join.trim().matches("null null"))
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            vehicleTaxDetailsBinding.txtDisc.setText(join.trim());

                                        }

                                        //vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                    }
                                }
                            });
                            binding.rlSummaryofcharge.addView(vehicleTaxDetailsBinding.getRoot());
                        }*/


                                       /* binding.arrowSelfcheckout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                binding.arrowSelfcheckout.setVisibility(View.INVISIBLE);
                                                if(binding.LayoutSelfCheckout.getVisibility()== View.GONE)
                                                {
                                                    if (reservationSummarry.ReservationStatus == 1) {
                                                        binding.LayoutSelfCheckout.setVisibility(View.VISIBLE);
                                                    } else {
                                                        binding.LayoutSelfCheckoutGreen.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                                else {
                                                    binding.LayoutSelfCheckout.setVisibility(View.GONE);
                                                    binding.LayoutSelfCheckoutGreen.setVisibility(View.GONE);
                                                }
                                            }
                                        });*/


                                    //reservationSummarry.ReservationChargesModels;

                                   /* for (int i = 0; i <reservationSummarry.ReservationSummaryModels.size(); i++) {
                                        getSubview(i);
                                        VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                                        vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                                        vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                                        vehicleTaxDetailsBinding.txtChargeName.setText(reservationSummarry.ReservationSummaryModels.get(i).SummaryName);
                                       // vehicleTaxDetailsBinding.txtCharge.setText(reservationSummarry.ReservationSummaryModels.get(i).TotalAmount.toString());
                                        vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount( charges[i].TotalAmount,true));
                                        int finalI = i;
                                        vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                                                    vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                                } else {
                                                    vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                                    String join ="";
                                                    for (int j = 0; j <reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels.length ; j++) {
                                                        // vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                        //         charges[finalI].ReservationSummaryDetailModels[j].Description);
                                                        join +=  reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Title + " "+
                                                                reservationSummarry.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Description+"\n";
                                                    }
                                                    if (join.trim().matches("null null"))
                                                    vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                                    vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                                }
                                            }
                                        });
                                        binding.rlSummaryofcharge.addView(vehicleTaxDetailsBinding.getRoot());
                                    }*/

                                    //ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

              /*                      final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                    final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rl_Summaryofcharge);


                                    final JSONArray getIncludeDetails = resultSet.getJSONArray("includesItem");
                                    RelativeLayout rlIncludeDetails = getActivity().findViewById(R.id.rl_includeDetails1);*/

                            /*        if(!resultSet.isNull("t0050_Documents"))
                                    {
                                        //QrImage
                                        JSONObject Documents=resultSet.getJSONObject("t0050_Documents");
                                        final int doc_ID = Documents.getInt("doc_ID");
                                        final String doc_Name = Documents.getString("doc_Name");
                                        final String doc_Details = Documents.getString("doc_Details");
                                        final String doc_Status = Documents.getString("doc_Status");
                                        final String created_Date = Documents.getString("created_Date");

                                        String url2 = domainName+doc_Details.substring(2);
                                        System.out.println(url2);
                                        // url2=url2.substring(0,url2.lastIndexOf("/")+1)+doc_Name;
                                        imageLoader.displayImage(url2, QR_Image);
                                    }
                                    if(!resultSet.isNull("vehicleModel"))
                                    {
                                        //VehicleDetails
                                        JSONObject vehicleModel = resultSet.getJSONObject("vehicleModel");
                                        final String vehiclE_NAME = vehicleModel.getString("vehiclE_NAME");
                                        final String vehiclE_TYPE_NAME = vehicleModel.getString("vehiclE_TYPE_NAME");
                                        final String vehiclE_SEAT_NO = vehicleModel.getString("vehiclE_SEAT_NO");
                                        final String veh_bags = vehicleModel.getString("veh_bags");
                                        final String transmission_Name = vehicleModel.getString("transmission_Name");
                                        final String doors = vehicleModel.getString("doors");
                                        final double totaL_DAYS = vehicleModel.getDouble("totaL_DAYS");
                                        String totaLDAYSStr = (String.valueOf(totaL_DAYS));
                                        totaLDAYSStr = totaLDAYSStr.substring(0, totaLDAYSStr.length() - 2);
                                        txtDays.setText(totaLDAYSStr);

                                        txt_Seats.setText(vehiclE_SEAT_NO);
                                        txt_Bags.setText(veh_bags);
                                        txt_Automatic.setText(transmission_Name);
                                        txt_Doors.setText(doors);
                                    }

                                        //Summary Of Charges
                                        int len;
                                        len = getsummaryOfCharges.length();

                                        for (int j = 0; j < len; j++)
                                        {
                                            final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                            final int sortId = test.getInt("sortId");
                                            final String chargeCode = test.getString("chargeCode");
                                            final String chargeName = test.getString("chargeName");
                                            final Double chargeAmount = test.getDouble("chargeAmount");

                                            JSONObject summaryOfChargesObj = new JSONObject();

                                            summaryOfChargesObj.put("sortId", sortId);
                                            summaryOfChargesObj.put("chargeCode", chargeCode);
                                            summaryOfChargesObj.put("chargeName", chargeName);
                                            summaryOfChargesObj.put("chargeAmount", chargeAmount);

                                            summaryOfCharges.put(summaryOfChargesObj);

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 0, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                            linearLayout.setId(200 + j);
                                            linearLayout.setLayoutParams(lp);

                                            TextView txt_charge, txt_chargeName;
                                            LinearLayout arrowIcon = (LinearLayout) linearLayout.findViewById(R.id.arrow_icon);

                                            if (sortId == 10)
                                            {
                                                arrowIcon.setVisibility(View.VISIBLE);
                                            }
                                            if (sortId == 12)
                                            {
                                                TotalAmount = chargeAmount;
                                                System.out.println(TotalAmount);
                                            }
                                            arrowIcon.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    Bundle Agreements = new Bundle();
                                                    Agreements.putBundle("ReservationBundle", ReservationBundle);
                                                    Agreements.putInt("backtoforterms", 3);
                                                    Agreements.putBoolean("TaxType", false);
                                                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                                                            .navigate(R.id.action_SummaryOfCharges_to_Total_tax_fee_details, Agreements);
                                                }
                                            });

                                            txt_charge = (TextView) linearLayout.findViewById(R.id.txt_charge);
                                            txt_chargeName = (TextView) linearLayout.findViewById(R.id.txt_chargeName);
                                            String str=String.format(Locale.US,"%.2f",chargeAmount);
                                            txt_charge.setText("USD$ "+str);
                                            txt_chargeName.setText(chargeName);

                                            if(chargeName.equals("Discount Applied"))
                                            {
                                                txt_charge.setTextColor(getResources().getColor(R.color.btn_bg_color_2));
                                            }                                            txt_chargeName.setText(chargeName);

                                            rlSummaryOfCharge.addView(linearLayout);
                                        }*/

                                       /* int len1;
                                        len1 = getIncludeDetails.length();
                                        for (int j = 0; j < len1; )
                                        {

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + (j / 3) - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 0, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout linearLayout1 = (LinearLayout) inflater.inflate(R.layout.include_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                            linearLayout1.setId(200 + (j / 3));
                                            linearLayout1.setLayoutParams(lp);

                                            TextView txt_InsurancePro, txt_carMaintanance, txt_roadsideAssistance;
                                            txt_InsurancePro = (TextView) linearLayout1.findViewById(R.id.txt_InsurancePro);
                                            txt_carMaintanance = (TextView) linearLayout1.findViewById(R.id.txt_carMaintanance);
                                            txt_roadsideAssistance = (TextView) linearLayout1.findViewById(R.id.txt_roadsideAssistance);


                                            if (j < len1 && (j % 3) == 0) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_InsurancePro.setText(includesName);
                                                j++;
                                            }
                                            if (j < len1 && (j % 3) == 1) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_carMaintanance.setText(includesName);
                                                j++;
                                            }
                                            if (j < len1 && (j % 3) == 2) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_roadsideAssistance.setText(includesName);
                                                j++;
                                            }
                                            rlIncludeDetails.addView(linearLayout1);
                                        }*/
                                    fullProgressbar.hide();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    fullProgressbar.hide();
                                }
                            }

                            else
                            {
                                fullProgressbar.hide();
                                String msg = responseJSON.getString("Message");
                                CustomToast.showToast(getActivity(),msg,1);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            fullProgressbar.hide();
                        }
                    }
                });
            }
            @Override
            public void onError(String error) {
                System.out.println("Error-" + error);
                fullProgressbar.hide();
            }
        };

    OnResponseListener CancelBooking = new OnResponseListener()
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

                        String msg = responseJSON.getString("message");
                        if (status)
                        {
                            CustomToast.showToast(getActivity(),msg,0);
                        }

                        else
                        {
                            CustomToast.showToast(getActivity(),msg,1);
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

    OnResponseListener DeleteReservation = new OnResponseListener()
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

                    String msg = responseJSON.getString("Message");
                    if (status)
                    {
                        CustomToast.showToast(getActivity(),msg,0);
                        NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this).popBackStack();
                    }
                    else
                    {
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
            System.out.println("Error" + error);
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
                        charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        summaryDisplay.getB2BSummarry(bundle,charges,binding.rlSummaryofcharge);
                        /*charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        for (int i = 0; i <charges.length ; i++) {
                            getSubview(i);
                            VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                            vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                            vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            vehicleTaxDetailsBinding.txtChargeName.setText(charges[i].SummaryName);
                            //vehicleTaxDetailsBinding.txtCharge.setText(charges[i].TotalAmount.toString());
                            vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount(charges[i].TotalAmount, false));
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
                                           // vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                           //         charges[finalI].ReservationSummaryDetailModels[j].Description);
                                           join +=  charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description+"\n";
                                            if (join.trim().matches("null null"))
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            vehicleTaxDetailsBinding.txtDisc.setText(join.trim());

                                        }

                                        //vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                    }
                                }
                            });
                            binding.rlSummaryofcharge.addView(vehicleTaxDetailsBinding.getRoot());
                        }*/

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

                        for (int j = 0; j < len; j++) {
                            final JSONObject test = (JSONObject) getdrivingLicense.get(j);
                           // loginRes.storedata("dirivingT", test.toString());
                            binding.textVDriverName.setText(test.getString("FullName"));
                            binding.TextVDriverPhoneno.setText(test.getString("PhoneNo"));
                            binding.TextVDriverEmail.setText(test.getString("Email"));

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

    OnResponseListener GetSelfCheckOuts = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.e("TAG", "onSuccess: " + response );
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                //selfCheckOutObject
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                JSONArray array = resultSet.getJSONArray("Data");
                               // CheckOutList[] checkLists = loginRes.getModel(array.toString(), CheckOutList[].class);
                                List<CheckOutList> checkOutLists = new ArrayList<>();
                                for (int i = 0; i <array.length() ; i++) {
                                    String  data = array.get(i).toString();
                                    CheckOutList  check = new CheckOutList();
                                    check = loginRes.getModel(data,CheckOutList.class);
                                  //  checkOutLists = (List<CheckOutList>)  loginRes.getModel(data,CheckOutList.class);
                                    checkOutLists.add(check);
                                }


                                bundle.putSerializable("checklist", (Serializable) checkOutLists);
                                NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                                .navigate(R.id.action_SummaryOfCharges_to_Self_check_Out,bundle);

                              /*  List<CheckList> checkLists = new ArrayList<CheckList>();
                                checkLists = (List<CheckList>) loginRes.getModelList(array.toString(), CheckList.class);*/



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
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.e("TAG", "onError: " + error );
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_Selfcheckout:
                binding.arrowSelfcheckout.setVisibility(View.INVISIBLE);
                if(binding.LayoutSelfCheckout.getVisibility()== View.GONE)
                {
                    binding.LayoutSelfCheckout.setVisibility(View.VISIBLE);
                }
                else
                    binding.LayoutSelfCheckout.setVisibility(View.GONE);
                break;
            case R.id.txt_charges:
                if(binding.ArrowForSummary.getVisibility() == View.VISIBLE)
                {
                    binding.rlSummaryofcharge.setVisibility(View.VISIBLE);
                } else {
                    binding.ArrowDownForSummary.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.term_condition:
             /*   Bundle Reservation = new Bundle();
                Reservation.putInt("termcondition", 2);
                Reservation.putBundle("ReservationBundle", ReservationBundle);
                System.out.println(Reservation);*/
                NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                        .navigate(R.id.action_SummaryOfCharges_to_TermAndCondition,bundle);
                break;
            case R.id.lblAddCalender:
                Calendar calendarEvent = Calendar.getInstance();
                Intent i = new Intent(Intent.ACTION_EDIT);
                i.setType("vnd.android.cursor.item/event");
                i.putExtra("Date",calendarEvent.getTimeInMillis());
                i.putExtra("allDay", true);
                i.putExtra("rule", "FREQ=YEARLY");
                i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                i.putExtra("title", "Calendar Event");
                startActivity(i);
                break;

            case R.id.llModify:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Please call our office for modifiction.");
                 /* builder.setPositiveButton("Yes",
                          new DialogInterface.OnClickListener()
                          {
                              @Override
                              public void onClick(DialogInterface dialog, int which)
                              {

                              }
                          });*/
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public   void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.Discardlbl:

            case R.id.backToHome:
                NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                        .navigate(R.id.action_SummaryOfCharges_to_Reservation);
                break;

            case R.id.driverdetails_icon:

                if (binding.driverDetails.getVisibility() == View.GONE)
                {
                    binding.driverDetails.setVisibility(View.VISIBLE);
                    apiService = new ApiService(GetDrivingLicense, RequestType.POST,
                            GETLICENSEALL, BASE_URL_LOGIN, header, params.getDrivingLicenseList(UserData.loginResponse.User.UserFor));

                } else
                    binding.driverDetails.setVisibility(View.GONE);

                break;

            case R.id.ArrowForSummary:
                if (binding.rlSummaryofcharge.getVisibility() == View.GONE)
                {
                    binding.rlSummaryofcharge.setVisibility(View.VISIBLE);
                    binding.ArrowDownForSummary.setVisibility(View.VISIBLE);
                    binding.ArrowForSummary.setVisibility(View.GONE);
                }
                break;

            case R.id.ArrowDownForSummary:
                if(binding.rlSummaryofcharge.getVisibility() == View.VISIBLE)
                {
                    binding.rlSummaryofcharge.setVisibility(View.GONE);
                    binding.ArrowForSummary.setVisibility(View.VISIBLE);
                    binding.ArrowDownForSummary.setVisibility(View.GONE);
                }
                break;

            case R.id.llCancelBooking:
                //booking cancel
                apiService = new ApiService(DeleteReservation, RequestType.POST,
                        DELETE, BASE_URL_LOGIN, header, params.getDelete(36,iid));
                break;
            case R.id.Cross_SymbolGreen:
                //action_SummaryOfCharges_to_Self_check_Out
                bundle.putInt( "Id", getArguments().getInt("Id"));
                bundle.putSerializable("resrvation", reservationSummarry);
                Log.e(TAG, "RID " + getArguments().getInt("Id"));
                ApiService apiService = new ApiService(GetSelfCheckOuts,RequestType.POST,CHECKLISTFORACCESORRIES, BASE_URL_CHECKOUT, header, params.getCheckListAccessories());
                /*NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                        .navigate(R.id.action_SummaryOfCharges_to_Self_check_Out,bundle);*/
                break;
        }
    }
}


