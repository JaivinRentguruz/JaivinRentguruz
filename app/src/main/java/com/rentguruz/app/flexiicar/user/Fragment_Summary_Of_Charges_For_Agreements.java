package com.rentguruz.app.flexiicar.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.home.reservation.Activity_Reservation;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentSummaryOfChargesForAgreementsBinding;
import com.rentguruz.app.model.CheckOutList;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.display.Pickupdrop;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rentguruz.app.model.response.UpdateDL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CHECKOUT;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKLISTFORACCESORRIES;
import static com.rentguruz.app.apicall.ApiEndPoint.GETSINGLELICENCE;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Summary_Of_Charges_For_Agreements extends BaseFragment
{
    //Handler handler = new Handler();
    //public static Context context;
    Bundle AgreementsBundle;
    ImageView SummaryOfChargeArrow,SummaryOfChargeArrowDown,QRImage,arrowSelfcheckOut,driverdetails_icon,ArrowFlight;
    RelativeLayout rlSummaryOfCharge;
    TextView CheckOutLocName,CheckOutDate,CheckInLocName,CheckInDate, txtDays,txt_vehicletype,checkstatus,
            txt_vehName,txt_rate,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txt_conformationNo,txt_CreatedDate;
    LinearLayout lblterm_condition1,Layout_SelfCheckoutGreen,Layout_SelfCheckInGreen,flightdetailsLayout,
            ArrowselfcheckOut,ArrowselfcheckIn;
    LinearLayout driverdetails1;
    int reservation_ID,agreement_ID;
    JSONArray summaryOfCharges = new JSONArray();
    ImageLoader imageLoader;
    String serverpath="",domainName="";
    ImageView BackArrow;
    String doc_Name,doc_Details;
    TextView txtDiscard;
    TextView driverName, driverPhone, driverEmail;
    String FlightTimeStr,StatusName;
    LinearLayout AddCalender;
    int iid;
    FragmentSummaryOfChargesForAgreementsBinding binding;
    public static ReservationSummarry reservationSummarry = new ReservationSummarry();
    DateConvert dateConvert = new DateConvert();
    ReservationSummaryModels[] charges;
    Reservation reservations;
    List<CheckOutList> checkOutLists;
    Pickupdrop pickupdrop;
    SummaryDisplay summaryDisplay;
    View views;
    Bundle Agreements=new Bundle();
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); //50 Mib
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentSummaryOfChargesForAgreementsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       /* int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        view.onWindowSystemUiVisibilityChanged(uiOptions);*/
        //((User_Profile) getActivity()).BottomnavInVisible();
        //vehiclecategory pending from api
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getActivity().getResources().getString(R.string.summary_of_charges));
        binding.header.optionmenu.setVisibility(View.VISIBLE);
        fullProgressbar.show();
        iid = getArguments().getInt("Id");
        bundle.putInt("Id", getArguments().getInt("Id"));
        views = view;
        iid = loginRes.getstoreId(getResources().getString(R.string.rid));
        pickupdrop = new Pickupdrop();
        summaryDisplay = new SummaryDisplay(getActivity());
        checkOutLists = new ArrayList<>();
        try {
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();
            reservations = new Reservation();

            AddCalender=view.findViewById(R.id.AddCalender);
            BackArrow=view.findViewById(R.id.back);
            txtDiscard=view.findViewById(R.id.discard);
            lblterm_condition1 = view.findViewById(R.id.termAndCondition1);
            Layout_SelfCheckoutGreen = view.findViewById(R.id.Layout_SelfCheckoutGreen);
            Layout_SelfCheckInGreen= view.findViewById(R.id.Layout_SelfCheckInGreen);
            arrowSelfcheckOut = view.findViewById(R.id.ArrowSelfcheckout);

            checkstatus = view.findViewById(R.id.checkstatus);

            ArrowselfcheckOut= view.findViewById(R.id.ArrowselfcheckOut);
            ArrowselfcheckIn= view.findViewById(R.id.ArrowselfcheckIn);

            QRImage = view.findViewById(R.id.QR_Image);
            ArrowFlight=view.findViewById(R.id.ArrowFlight);

            SummaryOfChargeArrow = view.findViewById(R.id.ArrowForSummaryOfCharges);
            SummaryOfChargeArrowDown= view.findViewById(R.id.DownArrowForSummaryOfCharges);

            rlSummaryOfCharge = view.findViewById(R.id.rlSummaryofcharge);
            flightdetailsLayout= view.findViewById(R.id.flightdetails);

            txt_conformationNo=view.findViewById(R.id.txt_conformationNo);
            CheckOutLocName = view.findViewById(R.id.CheckOutLocName);
            CheckOutDate = view.findViewById(R.id.CheckOutDate);
            CheckInLocName = view.findViewById(R.id.CheckInLocName);
            CheckInDate = view.findViewById(R.id.CheckInDate);
            txt_CreatedDate=view.findViewById(R.id.txtDateAgreements);

            txtDays = view.findViewById(R.id.txt_TotalDay2);
            txt_rate = view.findViewById(R.id.txt_Vehiclerate2);

            txt_Seats = view.findViewById(R.id.textViewSeats);
            txt_Bags = view.findViewById(R.id.textViewBags);
            txt_Automatic = view.findViewById(R.id.txtAutoMatic);
            txt_Doors = view.findViewById(R.id.txtDoor);

            txt_vehName = view.findViewById(R.id.VehicleName);
            txt_vehicletype = view.findViewById(R.id.VehicleType_name);

            //reservations = (Reservation) getArguments().getSerializable("reservation");
            //bundle.putSerializable("reservation",reservations);
            txt_vehicletype.setText(reservations.VehicleName);
            txt_vehName.setText(reservations.VehicleTypeName);
            txt_Seats.setText(String.valueOf(reservations.NoOfSeats));
            txt_Bags.setText(String.valueOf(reservations.NoOfBags));
            txt_Automatic.setText(String.valueOf(reservations.TransmissionDesc));
            txt_Doors.setText(String.valueOf(reservations.NoOfDoors));
            CustomBindingAdapter.loadImage(binding.VehicleImg,reservations.VehicleImagePath);
           /* binding.txtVehicleTypeName.setText(model.VehicleCategory);
            reservations.Vehi*/
            binding.CheckInLocName.setText(reservationSummarry.PickUpLocationName);
            binding.CheckOutLocName.setText(reservationSummarry.DropLocationName);
            UserData.loginResponse.User.UserFor = reservations.CustomerId;
            apiService = new ApiService(getTaxtDetails, RequestType.GET,
                    RESERVATIONGETBYID , BASE_URL_LOGIN, header, "?id="+iid);

            new ApiService(new OnResponseListener() {
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
                                    try
                                    {
                                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                                       // reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                        reservations = (Reservation) loginRes.getModel(resultSet.toString(),Reservation.class);
                                      //  bundle.putSerializable("reservationsum",reservationSummarry);
                                        //bundle.putSerializable("reservationD",reservationSummarry);
                                        bundle.putSerializable("reservation",reservations);
                                        Agreements.putSerializable("reservation",reservations);
                                        pickupdrop.pickupdate = reservations.CheckOutDate;
                                        pickupdrop.dropdate = reservations.CheckInDate;
                                        pickupdrop.pickuploc = reservations.PickUpLocationName;
                                        pickupdrop.droploc = reservations.DropLocationName;
                                        pickupdrop.imagepath = reservations.VehicleImagePath;
                                        txt_vehicletype.setText(reservations.VehicleName);
                                        txt_vehName.setText(reservations.VehicleTypeName);
                                        txt_Seats.setText(String.valueOf(reservations.NoOfSeats));
                                        txt_Bags.setText(String.valueOf(reservations.NoOfBags));
                                        txt_Automatic.setText(String.valueOf(reservations.TransmissionDesc));
                                        txt_Doors.setText(String.valueOf(reservations.NoOfDoors));
                                        CustomBindingAdapter.loadImage(binding.VehicleImg,reservations.VehicleImagePath);
                                        binding.CheckInLocName.setText(reservationSummarry.PickUpLocationName);
                                        binding.CheckOutLocName.setText(reservationSummarry.DropLocationName);
                                        binding.staus.setText(companyLabel.Reservation + " " + companyLabel.Status);
                                        binding.statusdetails.setText(reservations.ReservationStatusDesc);
                                        UserData.loginResponse.User.UserFor = reservations.CustomerId;
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

                }
            }, RequestType.POST, RESERVATIONGETSINGLE, BASE_URL_LOGIN, header, params.getsingle(UserData.loginResponse.User.UserFor,iid));

            AgreementsBundle = getArguments().getBundle("AgreementsBundle");

            pickupdrop.pickuploc = reservations.PickUpLocationName;
            pickupdrop.droploc = reservations.DropLocationName;
            pickupdrop.pickupdate = reservations.CheckOutDate;
            pickupdrop.dropdate = reservations.CheckInDate;
            //binding.option.bookingconfirm.setVisibility(View.GONE);


            Agreements.putBundle("AgreementsBundle",AgreementsBundle);
            Agreements.putInt("Id", iid);
            bundle.putInt("Id", iid);
            bundle.putSerializable("resrvation", reservationSummarry);
            Agreements.putSerializable("resrvation", reservationSummarry);
            Agreements.putSerializable("checklist", (Serializable) checkOutLists);

            try {
                ((Activity_Reservation)getActivity()).BottomnavInVisible();
            } catch (Exception e){
                e.printStackTrace();
            }

           /* CheckOutLocName.setText(AgreementsBundle.getString("check_Out_Location_Name"));
            CheckInLocName.setText(AgreementsBundle.getString("check_in_Location_Name"));
            final String check_IN=(AgreementsBundle.getString("check_IN"));
            // Date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
            Date date1 = dateFormat1.parse(check_IN);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
            String checkinStr = sdf1.format(date1);
            CheckOutDate.setText(checkinStr);

            final String CheckOut=(AgreementsBundle.getString("check_Out"));


            //check_Out Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date date = dateFormat.parse(CheckOut);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
            String check_OutStr = sdf.format(date);
            CheckInDate.setText(check_OutStr);

            txt_vehName.setText(AgreementsBundle.getString("vehicle_Type_Name"));
            txt_vehicletype.setText(AgreementsBundle.getString("vehicle"));
            
            String DayStr=AgreementsBundle.getString("totalDays");
            DayStr=DayStr.substring(0,DayStr.length()-2);
            txtDays.setText(DayStr);

            txt_Bags.setText(AgreementsBundle.getString("veh_Bags"));
            txt_rate.setText(AgreementsBundle.getString("rate_ID"));
            txt_Automatic.setText(AgreementsBundle.getString("transmission_Name"));

            reservation_ID=AgreementsBundle.getInt("reservation_ID");
            StatusName=AgreementsBundle.getString("status_Name");
            agreement_ID=AgreementsBundle.getInt("agreement_ID");

            txt_conformationNo.setText(String.valueOf(reservation_ID));
            txt_Seats.setText(AgreementsBundle.getString("veh_Seat"));

            String Date=(AgreementsBundle.getString("default_Created_Date"));
           *//* SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date2 = dateFormat2.parse(Date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy,HH:mm aa",Locale.US);
            String Issue_DateStr = sdf2.format(date2);*//*
            txt_CreatedDate.setText("SEE YOU ON "+(CheckInDate.getText().toString()));

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            domainName= sp.getString("domainName", "");

            String id = sp.getString(getString(R.string.id), "");

            ImageView Vehicle_Img=view.findViewById(R.id.Vehicle_Img);

            String VehicleImgstr=AgreementsBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,Vehicle_Img);*/
            CustomBindingAdapter.loadImage(binding.icon,loginRes.getData(getResources().getString(R.string.icon)));


    /*        binding.OpenbottomMenu.setOnClickListener(new View.OnClickListener() {
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



            binding.option.editAgreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                                .navigate(R.id.action_SummaryOfChargesForAgreements_to_chnageAgeement,bundle);

                    Transition transition = new Slide(Gravity.BOTTOM);
                    transition.setDuration(600);
                    transition.addTarget( binding.sucessfullRegi);
                    //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                    TransitionManager.beginDelayedTransition(binding.getRoot(),transition);
                    binding.sucessfullRegi.setVisibility(View.GONE);
                }
            });

            binding.option.printAgreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Printpreview,bundle);
                }
            });

            binding.option.deleteagreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Delete Agreement?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String msg = "You Have Been Successfully Delete Agreement!";
                                    CustomToast.showToast(getActivity(),msg,0);
                                    new ApiService(DeleteReservation, RequestType.POST,
                                            DELETE, BASE_URL_LOGIN, header, params.getDelete(36,iid));

                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    Transition transition = new Slide(Gravity.BOTTOM);
                                    transition.setDuration(600);
                                    transition.addTarget( binding.sucessfullRegi);
                                    //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                                    TransitionManager.beginDelayedTransition(binding.getRoot(),transition);
                                    binding.sucessfullRegi.setVisibility(View.GONE);
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

            binding.option.paymentprocess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

            ApiService apiService = new ApiService(GetSelfCheckOuts,RequestType.POST,CHECKLISTFORACCESORRIES, BASE_URL_CHECKOUT, header, params.getCheckListAccessories());



            AddCalender.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Calendar calendarEvent = Calendar.getInstance();
                    Intent i = new Intent(Intent.ACTION_EDIT);
                    i.setType("vnd.android.cursor.item/event");
                    i.putExtra("Date",calendarEvent.getTimeInMillis());
                    i.putExtra("allDay", true);
                    i.putExtra("rule", "FREQ=YEARLY");
                    i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                    i.putExtra("title", "Calendar Event");
                    startActivity(i);
                }
            });

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Agreements);
                }
            });

            BackArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Agreements);
                }
            });
            if (reservations.ReservationStatus != 5){
                checkstatus.setText(companyLabel.CheckOut);
            } else {
                checkstatus.setText(companyLabel.CheckIn);
            }
            arrowSelfcheckOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    arrowSelfcheckOut.setVisibility(View.INVISIBLE);
                    //if(StatusName.equals("Open"))
                    //Log.e(TAG, "onClick: " + reservations.ReservationStatus );
                    Layout_SelfCheckoutGreen.setVisibility(View.VISIBLE);
                   // Layout_SelfCheckInGreen.setVisibility(View.VISIBLE);
                /*    if (reservations.ReservationStatus != 5)
                    {
                       if (Layout_SelfCheckoutGreen.getVisibility() == View.GONE)
                        {
                            Layout_SelfCheckoutGreen.setVisibility(View.VISIBLE);

                        } else
                            Layout_SelfCheckoutGreen.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (Layout_SelfCheckInGreen.getVisibility() == View.GONE)
                        {
                            Layout_SelfCheckInGreen.setVisibility(View.VISIBLE);
                        } else
                            Layout_SelfCheckInGreen.setVisibility(View.GONE);
                    }*/
                }
            });

            lblterm_condition1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    /*Bundle Agreements = new Bundle();
                    Agreements.putInt("termcondition", 3);
                    Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                    System.out.println(Agreements);*/
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_TermAndCondition,bundle);
                }
            });

            ArrowselfcheckOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   /* Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    Agreements.putInt("Id", iid);
                    bundle.putInt("Id", iid);
                    bundle.putSerializable("resrvation", reservationSummarry);
                    Agreements.putSerializable("resrvation", reservationSummarry);
                    Agreements.putSerializable("checklist", (Serializable) checkOutLists);*/
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,Agreements);
                }
            });

            ArrowselfcheckIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
               /*     Bundle Agreements=new Bundle();
                    AgreementsBundle.putString("doc_Name",doc_Name);
                    AgreementsBundle.putString("doc_Details",doc_Details);
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(Agreements);*/
                    //bundle.putSerializable("reservation",reservationSummarry);
                    bundle.putSerializable("reservation",reservations);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_In,bundle);

                   /* NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);*/
                }
            });

            driverdetails1 = view.findViewById(R.id.Driverdetails);

            driverName = view.findViewById(R.id.textDriverName);
            driverPhone = view.findViewById(R.id.TextDriverPhoneno);
            driverEmail = view.findViewById(R.id.TextDriverEmail);

    /*        String cust_FName=AgreementsBundle.getString("cust_FName");
            String cust_LName=AgreementsBundle.getString("cust_LName");
            String cust_MobileNo=AgreementsBundle.getString("cust_MobileNo");
            String cust_Email=AgreementsBundle.getString("cust_Email");

            driverName.setText(cust_FName + " " + cust_LName);
            driverPhone.setText(cust_MobileNo);
            driverEmail.setText(cust_Email);*/

            driverdetails_icon = view.findViewById(R.id.driversIcon1);
          /*  driverdetails_icon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (driverdetails1.getVisibility() == View.GONE)
                    {
                        driverdetails1.setVisibility(View.VISIBLE);
                    }
                    else {
                        driverdetails1.setVisibility(View.GONE);
                    }
                }
            });*/

            final TextView flightName=view.findViewById(R.id.lblFlightName);
            final TextView flightNo=view.findViewById(R.id.lblflightNumber);
            final TextView flightTime=view.findViewById(R.id.lblflightTime);

            ArrowFlight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        flightName.setText(getArguments().getBundle("AgreementsBundle").getString("d_Airport"));
                        flightNo.setText(getArguments().getBundle("AgreementsBundle").getString("d_FlightNo"));

                        FlightTimeStr = (getArguments().getBundle("AgreementsBundle").getString("default_D_Time"));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date date = dateFormat.parse(FlightTimeStr);
                        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy,HH:mm aa", Locale.US);
                        String FlightTimeDateStr = sdf.format(date);
                        flightTime.setText(FlightTimeDateStr);

                        if(flightdetailsLayout.getVisibility()== View.GONE)
                        {
                            flightdetailsLayout.setVisibility(View.VISIBLE);
                        }
                        else
                            flightdetailsLayout.setVisibility(View.GONE);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            TextView txt_charges = view.findViewById(R.id.txt_charges);
            txt_charges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rlSummaryOfCharge.getVisibility() == View.GONE)
                    {
                        rlSummaryOfCharge.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrow.setVisibility(View.GONE);
                    } else {
                        rlSummaryOfCharge.setVisibility(View.GONE);
                        SummaryOfChargeArrow.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.GONE);
                    }
                }
            });

            SummaryOfChargeArrow.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if (rlSummaryOfCharge.getVisibility() == View.GONE)
                    {
                        rlSummaryOfCharge.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrow.setVisibility(View.GONE);
                    }
                }
            });

            SummaryOfChargeArrowDown.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(rlSummaryOfCharge.getVisibility() == View.VISIBLE)
                    {
                        rlSummaryOfCharge.setVisibility(View.GONE);
                        SummaryOfChargeArrow.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.GONE);
                    }
                }
            });

            JSONObject bodyParam = new JSONObject();
            try
            {
                bodyParam.accumulate("ForTransId", AgreementsBundle.getInt("reservation_ID"));
                bodyParam.accumulate("BookingStep", 6);
                System.out.println(bodyParam);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
         /*   AndroidNetworking.initialize(getActivity());
            Fragment_Summary_Of_Charges_For_Agreements.context = getActivity();

            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //fullProgressbar.hide();
        }


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener getTaxtDetails = new OnResponseListener() {
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
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");

                                reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                UserData.reserversationSummary = reservationSummarry;
                                bundle.putSerializable("reservationsum",reservationSummarry);
                                //bundle.putSerializable("reservationD",reservationSummarry);
                              //  binding.CheckInLocName.setText(reservationSummarry.PickUpLocationName);
                              //  binding.CheckOutLocName.setText(reservationSummarry.DropLocationName);

                               // binding.CheckInDate.setText(dateConvert.allDateConverter(DateType.fulldate,reservationSummarry.CheckOutDate,DateType.ddMMyyyyS1));
                                binding.CheckInDate.setText(Helper.getDateDisplay(DateType.fulldate,reservationSummarry.CheckOutDate) + " , " + Helper.getTimeDisplay(DateType.fulldate,reservationSummarry.CheckOutDate));
                                binding.CheckOutDate.setText(Helper.getDateDisplay(DateType.fulldate,reservationSummarry.CheckInDate) + " , " + Helper.getTimeDisplay(DateType.fulldate,reservationSummarry.CheckInDate));
                                binding.txtConformationNo.setText(reservationSummarry.ReservationNo);
                                binding.txtTotalDay2.setText(String.valueOf(reservationSummarry.ReservationInsuranceModel.NoOfDays));

                                binding.txtDateAgreements.setText( "See you on " +dateConvert.allDateConverter(DateType.fulldate,reservationSummarry.CheckInDate,DateType.ymdd));
                                loginRes.testingLog(TAG,reservationSummarry);
                                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
                                Log.e(TAG, "run: "+  reservationSummarry.ReservationSummaryModels.get(0).ReservationSummaryDetailModels[1].Title);

                              /*  charges =  new ReservationSummaryModels[reservationSummarry.ReservationSummaryModels.size()];
                                for (int i = 0; i <reservationSummarry.ReservationSummaryModels.size() ; i++) {
                                    charges[i] = reservationSummarry.ReservationSummaryModels.get(i);
                                }

                                summaryDisplay.getB2BSummarry(bundle,charges, binding.rlSummaryofcharge);*/

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

                                //TextView driverName, driverPhone, driverEmail;

                                try {

                                    apiService = new ApiService(new OnResponseListener() {
                                        @Override
                                        public void onSuccess(String response, HashMap<String, String> headers) {
                                            handler.post(() -> {
                                                try {
                                                    System.out.println("Success");
                                                    System.out.println(response);
                                                    JSONObject responseJSON = new JSONObject(response);
                                                    Boolean status = responseJSON.getBoolean("Status");
                                                    fullProgressbar.hide();
                                                    if (status)
                                                    {
                                                        JSONObject data = responseJSON.getJSONObject("Data");
                                                        UpdateDL updateDL = new UpdateDL();
                                                        updateDL = loginRes.getModel(data.toString(), UpdateDL.class);
                                                        driverName.setText(updateDL.FullName);
                                                        driverPhone.setText(updateDL.MobileNo);
                                                        driverEmail.setText(updateDL.Email);
                                                        /*if (!updateDL.FullName.isEmpty()){
                                                            driverdetails_icon.setOnClickListener(new View.OnClickListener()
                                                            {
                                                                @Override
                                                                public void onClick(View view)
                                                                {
                                                                    if (driverdetails1.getVisibility() == View.GONE)
                                                                    {
                                                                        driverdetails1.setVisibility(View.VISIBLE);
                                                                    }
                                                                    else {
                                                                        driverdetails1.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            getView().findViewById(R.id.additionalDrivers).setVisibility(View.GONE);
                                                        }*/
                                                    }
                                                    else
                                                    {
                                                        fullProgressbar.hide();
                                                        String msg = responseJSON.getString("Message");
                                                        CustomToast.showToast(getActivity(),msg,1);
                                                    }
                                                } catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                    fullProgressbar.hide();
                                                }

                                            });
                                        }

                                        @Override
                                        public void onError(String error) {
                                            fullProgressbar.hide();
                                        }
                                    }, RequestType.GET, GETSINGLELICENCE, BASE_URL_LOGIN, header, "?Id=" + reservationSummarry.ReservationDriversModel.get(0).DriverId);

                                } catch (Exception e){
                                    getView().findViewById(R.id.additionalDrivers).setVisibility(View.GONE);
                                    e.printStackTrace();
                                    fullProgressbar.hide();
                                }





                                // driverName.setText(reservationSummarry.ReservationDriversModel.get());

                               /* for (int i = 0; i < reservationSummarry.IsIgnoreLocationClose; i++) {

                                }*/

                               /* JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rlSummaryofcharge);

                                final JSONArray getIncludeDetails = resultSet.getJSONArray("includesItem");
                                RelativeLayout rlIncludeDetails = getActivity().findViewById(R.id.rl_includeDetailsItem);

                                if(resultSet.equals("t0050_Documents"))
                                {
                                    //QrImage
                                    JSONObject Documents=resultSet.getJSONObject("t0050_Documents");
                                    final int doc_ID = Documents.getInt("doc_ID");
                                    doc_Name = Documents.getString("doc_Name");
                                    doc_Details = Documents.getString("doc_Details");
                                    final String doc_Status = Documents.getString("doc_Status");
                                    final String created_Date = Documents.getString("created_Date");

                                    String url2 = domainName+doc_Details.substring(2);
                                    System.out.println(url2);
                                    //url2=url2.substring(0,url2.lastIndexOf("/")+1)+doc_Name;
                                    imageLoader.displayImage(url2, QRImage);
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

                                    if (sortId == 10) {
                                        arrowIcon.setVisibility(View.VISIBLE);
                                    }
                                    arrowIcon.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle Agreements = new Bundle();
                                            Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                                            Agreements.putInt("backtoforterms", 6);
                                            Agreements.putBoolean("TaxType", false);
                                            NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                                                    .navigate(R.id.action_SummaryOfChargesForAgreements_to_Total_tax_fee_details, Agreements);
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
                                    }
                                    txt_chargeName.setText(chargeName);

                                    rlSummaryOfCharge.addView(linearLayout);
                                }

                                int len1;
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
                            String msg = responseJSON.getString("message");
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
            fullProgressbar.hide();
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
                        JSONObject time = resultSet.getJSONObject("ReservationTimeModel");
                        ReservationTimeModel timeModel = new ReservationTimeModel();
                        timeModel = loginRes.getModel(time.toString(),ReservationTimeModel.class);
                        charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        Helper.isDeposit = reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit;
                        Log.e(TAG, "onSuccess: " + reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit + " " +  Helper.isDeposit);
                        List<ReservationSummaryModels> data = loginRes.arraytolist(charges);
                        Log.e(TAG, "onSuccess: " + data.size() );
                       // data =  stream(data).
                        summaryDisplay.getB2BSummarry(bundle,charges, binding.rlSummaryofcharge);
                        pickupdrop.noDays = timeModel.TotalDays;
                        binding.pickup.setPickupdrop(pickupdrop);
                        bundle.putSerializable("reservationSum", reservationSummarry);
                        OptionMenu optionMenu = new OptionMenu(getActivity());
                        optionMenu.optionmenulist(binding.sucessfullRegi,views,bundle,Fragment_Summary_Of_Charges_For_Agreements.this,header,params,binding.option);
                        /*for (int i = 0; i <charges.length ; i++) {
                            getSubview(i);
                            VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                            vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                            vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            vehicleTaxDetailsBinding.txtChargeName.setText(charges[i].SummaryName);
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

                                for (int i = 0; i <array.length() ; i++) {
                                    String  data = array.get(i).toString();
                                    CheckOutList  check = new CheckOutList();
                                    check = loginRes.getModel(data,CheckOutList.class);
                                    //  checkOutLists = (List<CheckOutList>)  loginRes.getModel(data,CheckOutList.class);
                                    checkOutLists.add(check);
                                }


                                bundle.putSerializable("checklist", (Serializable) checkOutLists);

                              /*  NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                                        .navigate(R.id.action_SummaryOfCharges_to_Self_check_Out,bundle);*/

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

    OnResponseListener DeleteReservation = new OnResponseListener() {
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
                       // CustomToast.showToast(getActivity(),msg,0);
                        NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this).popBackStack();
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

    @Override
    public void onClick(View v) {

    }
}


