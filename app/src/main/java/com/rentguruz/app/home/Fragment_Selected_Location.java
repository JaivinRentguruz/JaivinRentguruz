package com.rentguruz.app.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.fragment.NavHostFragment;
import com.rentguruz.app.R;
import com.rentguruz.app.model.parameter.enums.ReservationMainType;
import com.rentguruz.app.model.response.User;
import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.archit.calendardaterangepicker.customviews.*;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentSelectedLocationBinding;
import com.rentguruz.app.flexiicar.login.Login;
import com.rentguruz.app.model.BusinessSource;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.reservation.ReservationBusinessSource;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.savvi.rangedatepicker.SubTitle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.nio.file.attribute.AttributeView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.BUSINESSBOOKINGTYPE;
import static com.rentguruz.app.apicall.ApiEndPoint.BusinessSourceMaster;
import static com.rentguruz.app.apicall.ApiEndPoint.BusinessSourceMasterALL;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;

public class Fragment_Selected_Location extends BaseFragment
{
    public DateRangeCalendarView calendar;
  //  TextView txtstartdate, txtenddate, txtdone, txtpickuptime, txtreturntime, txtpickup_cancel, txt_droptime_cancel, txt_location_name, txtcity, txtzipcode, txtstreet,
  //          txtreturn_location, txtreturn_city, txtreturn_street, txtreturn_zipcode,txt_cancelCal;
  //  ImageView backarrowimageView ;
  //  RelativeLayout img_plus;
  //  LinearLayout linearlayout_different_location, lbl_change_returnLoc, lbl_change_location,llLogin,LoginLayout,RegisterLayout;
  //  RelativeLayout calanderlayout, time_relative_layout, relative_layout_droptime;
   // Bundle returnLocationBundle, locationBundle;
   // Boolean locationType, initialSelect;
    int cmP_DATE_FORMAT;
   // TextView txt_Discard;
    String StarDate, EndDate;
   // public String id = "";
    //Bundle Booking;
    //boolean isPickTimeSelectedcolor = true;
   // private static final String TAG = Fragment_Selected_Location.class.getSimpleName();

    public static String startdatejourney,enddatejourney,starttimejourney,endtimejourney;
    public static  int businessmaster;
    public static int type;

    List<ReservationBusinessSource> data;
    Handler handler=new Handler(Looper.getMainLooper());
    public DateConvert dateConvert = new DateConvert();
    FragmentSelectedLocationBinding binding;
    LocationList location;
    LocationList returnlocation;
    //LocationList[]  locationListsData;
    ReservationSummarry reserversationSummary;
    BusinessSource[] businessSourceList;
    int idd;
    private static BusinessSource businessSource;
    public static ReservationBusinessSource reservationBusinessSource;
    public static Boolean lease =false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        location = new LocationList();
        returnlocation = new LocationList();
        reserversationSummary = new ReservationSummarry();
        binding = FragmentSelectedLocationBinding.inflate(inflater, container,false);
        data = new ArrayList<>();
        binding.LoginLayout.setOnClickListener(this);
        binding.RegisterLayout.setOnClickListener(this);
        binding.imgPlus.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);

        binding.lblstartdate.setOnClickListener(this);
        binding.txtDone.setOnClickListener(this);
        binding.txtCancelCal.setOnClickListener(this);
        binding.pickupTime.setOnClickListener(this);
        binding.returnTime.setOnClickListener(this);
        binding.pickupCancel.setOnClickListener(this);
        binding.droptimeCancel.setOnClickListener(this);

        return binding.getRoot();
    }

   // @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reservationBusinessSource = new ReservationBusinessSource();
        binding.setUiColor(UiColor);
        try {
            binding.setLabel(companyLabel);
        } catch (Exception e){
            e.printStackTrace();
        }
          binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.select_location));
//        ((Activity_Home) getActivity()).BottomnavInVisible();

/*
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, 0);


        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);

        binding.calendarView.deactivateDates(list);

        ArrayList<Date> arrayList = new ArrayList<>();
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

            String strdate = "15-12-2021";
            String strdate2 = "01-01-2022";

            Date newdate = dateformat.parse(strdate);
            Date newdate2 = dateformat.parse(strdate2);
            arrayList.add(newdate);
            arrayList.add(newdate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.calendarView.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM, YYYY", Locale.getDefault())) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
                    .withDeactivateDates(list)
                    .withSubTitles(getSubTitles())
                    .withHighlightedDates(arrayList);
        }

        binding.calendarView.scrollToDate(new Date());*/

        fullProgressbar.show();
        try {
            DropDown dropDownList;
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            dropDownList = (new DropDown(BUSINESSBOOKINGTYPE,Integer.parseInt(loginRes.getData("CompanyId")),true,true));

            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(() -> {
                        try {

                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            JSONObject datta = responseJSON.getJSONObject("Data");
                            final JSONArray getReservationList = datta.getJSONArray("Data");
                            ReservationBusinessSource[] onDropDownLists;
                            //OnDropDownList[] onDropDownLists;
                            List<String> strings = new ArrayList<>();
                            //onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);
                            onDropDownLists = loginRes.getModel(getReservationList.toString(),ReservationBusinessSource[].class);
                            for (int i = 0; i < onDropDownLists.length; i++) {
                                // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                /*OnDropDownList onDropDownList = new OnDropDownList();
                                onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                data.add(onDropDownList);*/


                                ReservationBusinessSource onDropDownList = new ReservationBusinessSource();
                                onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), ReservationBusinessSource.class);
                               // if (onDropDownList.IsRateSelect) {
                                    data.add(onDropDownList);
                                    strings.add(onDropDownLists[i].Name);
                                //}
                            }
                            final JSONArray businessSoures = datta.getJSONArray("Data");
                            businessSourceList = loginRes.getModel(businessSoures.toString(),BusinessSource[].class);
                            for (int i = 0; i < businessSourceList.length; i++) {
                                if (businessSourceList[i].IsDefault){
                                    businessSource = businessSourceList[i];


                                }
                            }
                            //   getSpinner(binding.makeId,strings);
                            listSpinner(data);


                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, BusinessSourceMasterALL, BASE_URL_LOGIN, header, params.getBusinessSource());




            if (location!=null) {
                binding.pickuploc.setLocation(location);
              /*  binding.txtLocationName.setText(location.Name);
                binding.txtStreetLoc.setText(Html.fromHtml(location.AddressesModel.PreviewAddress));*/

            }
            binding.differentLocationLayout.setVisibility(View.GONE);
            if (returnlocation!= null) {
                binding.differentLocationLayout.setVisibility(View.VISIBLE);
                binding.droploc.setLocation(returnlocation);
                /*binding.returnLocationName.setText(returnlocation.Name);
                binding.txtReturnStreet.setText(Html.fromHtml(returnlocation.AddressesModel.PreviewAddress));*/
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
           /* int[] attrs = {android.R.attr.textColor, android.R.attr.background, android.R.attr.text};
            TypedArray a = getActivity().getTheme().obtainStyledAttributes(attrs,R.styleable.DateRangeMonthView,0,0);*/
            LinearLayout lblselectedlocation = view.findViewById(R.id.lblcontinue1);
            calendar = view.findViewById(R.id.cdrvCalendar);
           /* //R.styleable.DateRangeMonthView;
            @SuppressLint("ResourceType") TypedArray a = getActivity().obtainStyledAttributes(R.attr.range_date_color,R.styleable.DateRangeMonthView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //String col = a.getColor(R.styleable.DateRangeMonthView,1);
                a.recycle();
                a.getColorStateList(R.styleable.DateRangeMonthView[0]).withAlpha(Color.parseColor(UserData.UiColor.secondary));
                a.recycle();
            }*/
            binding.pickuploc.lblselectedlocation1.setVisibility(View.VISIBLE);
            binding.droploc.lblselectedlocation1.setVisibility(View.VISIBLE);
            binding.differentLocationLayout.setVisibility(View.GONE);
            //Pick Up Time Selection
            final RelativeLayout timelayout = (RelativeLayout) view.findViewById(R.id.relative_layout_time);

            for (int i = 0; i <= 47; i++)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.setMargins(0, 0, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout Timelist = (LinearLayout) inflater.inflate(R.layout.timepickup_list, (ViewGroup) view.findViewById(android.R.id.content), false);
                Timelist.setId(200 + i);
                Timelist.setLayoutParams(lp);

                final TextView txttime = (TextView) Timelist.findViewById(R.id.txt_time);
                final Button btnselect = (Button) Timelist.findViewById(R.id.btn_selecttime);
                final LinearLayout linear = (LinearLayout) Timelist.findViewById(R.id.linearlayout2);

                final int tempPosition = i;

                linear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        binding.pickupTime.setText(txttime.getText());
                        binding.returnTime.setText(txttime.getText());
                        linear.setBackgroundColor(Color.BLACK);
                        txttime.setTextColor(Color.WHITE);

                        int len = timelayout.getChildCount();

                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = timelayout.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = timelayout.getChildAt(m).findViewById(R.id.txt_time);
                                llTimeLayout.setBackgroundColor(Color.WHITE);
                                txtTime.setTextColor(Color.BLACK);
                                binding.timeRelativeLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                String hour = (i / 2) + "";
                String mins = ((i % 2) * 30) + "";

                if (hour.length() == 1)
                    hour = "0" + hour;

                if (mins.length() == 1)
                    mins += "0";

                txttime.setText(hour + ":" + mins);
                timelayout.addView(Timelist);
            }

            //Drop Time Selection
            final RelativeLayout timelayout2 = (RelativeLayout) view.findViewById(R.id.droptime_layout);

            for (int i = 0; i <= 47; i++)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.setMargins(0, 0, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout Timelist = (LinearLayout) inflater.inflate(R.layout.timepickup_list, (ViewGroup) view.findViewById(android.R.id.content), false);
                Timelist.setId(200 + i);
                Timelist.setLayoutParams(lp);

                final TextView txttime = (TextView) Timelist.findViewById(R.id.txt_time);
                Button btnselect = (Button) Timelist.findViewById(R.id.btn_selecttime);
                final LinearLayout linear = (LinearLayout) Timelist.findViewById(R.id.linearlayout2);

                final int tempPosition = i;

                linear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        binding.returnTime.setText(txttime.getText());
                        linear.setBackgroundColor(Color.BLACK);
                        txttime.setTextColor(Color.WHITE);
                        int len = timelayout2.getChildCount();

                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = timelayout2.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = timelayout2.getChildAt(m).findViewById(R.id.txt_time);
                                llTimeLayout.setBackgroundColor(Color.WHITE);
                                txtTime.setTextColor(Color.BLACK);
                                binding.timeRelativeLayout2.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                String hour = (i / 2) + "";
                String mins = ((i % 2) * 30) + "";

                if (hour.length() == 1)
                    hour = "0" + hour;

                if (mins.length() == 1)
                    mins += "0";

                txttime.setText(hour + ":" + mins);
                timelayout2.addView(Timelist);
            }


            lblselectedlocation.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v)
                {
                    //  if(!id.equals(""))
                    // {
                   /* if (validation( binding.lblstartdate.getText().toString(),binding.lblenddate.getText().toString())){
                        Log.e(TAG, "onClick: " + "true" );
                    }*/
                    startdatejourney = binding.lblstartdate.getText().toString();
                    enddatejourney = binding.lblenddate.getText().toString();
                    starttimejourney = binding.pickupTime.getText().toString();
                    endtimejourney = binding.returnTime.getText().toString();
                    bundle.putSerializable("model", location);
                    bundle.putSerializable("models", location);
//                    getDropdownId(binding.businessMaster.getSelectedItem().toString());
                    businessmaster = idd;
                    if (returnlocation!= null)
                        bundle.putSerializable("models", returnlocation);
                    bundle.putString("pickupdate", DateConvert.DateConverter(DateType.monthwithdate, binding.lblstartdate.getText().toString(), DateType.yyyyMMddD));
                    bundle.putString("dropdate", DateConvert.DateConverter(DateType.monthwithdate, binding.lblenddate.getText().toString(), DateType.yyyyMMddD));
                    bundle.putString("droptime", binding.pickupTime.getText().toString());
                    bundle.putString("pickuptime",  binding.returnTime.getText().toString());


                    /*bundle.putString("dropdate", DateConvert.DateConverter(DateType.monthwithdate, binding.lblstartdate.getText().toString(), DateType.yyyyMMddD));
                    bundle.putString("pickupdate", DateConvert.DateConverter(DateType.monthwithdate, binding.lblenddate.getText().toString(), DateType.yyyyMMddD));
                    bundle.putString("pickuptime", binding.pickupTime.getText().toString());
                    bundle.putString("droptime",  binding.returnTime.getText().toString());*/

                    String date1 = DateConvert.DateConverter(DateType.monthwithdate, binding.lblstartdate.getText().toString(), DateType.yyyyMMddD);
                    String date2 = DateConvert.DateConverter(DateType.monthwithdate, binding.lblenddate.getText().toString(), DateType.yyyyMMddD);

                    try {
                        reservationBusinessSource = loginRes.getModel(loginRes.getData("reservationBusinessSource"),ReservationBusinessSource.class);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                /*    String d = Helper.setPostDate(date1);
                    String d2 =Helper.setPostDate(date2);*/

                    reserversationSummary.CheckOutDate =date1+"T"+binding.returnTime.getText().toString();
                    reserversationSummary.CheckInDate =date2 +"T"+binding.pickupTime.getText().toString();

                    getDropdownId(binding.businessMaster.getSelectedItem().toString());
                    businessmaster = idd;

                    reserversationSummary.BusinessSourceId = businessmaster;
                    reserversationSummary.ReservationStatus = 1;
                    reserversationSummary.ReservationType = reservationBusinessSource.ReservationTypeId;
                    reserversationSummary.TypeOf = 1;
                    reserversationSummary.PickUpLocation = location.Id;
                    reserversationSummary.DropLocation = location.Id;
                    if (returnlocation!= null)
                        reserversationSummary.DropLocation = returnlocation.Id;

                    bundle.putSerializable("reservationSum",reserversationSummary);
                    Helper.isCustomerVisible = false;
                    if (UserData.companyModel.CompanyPreference.DefaultBookingOnVehicleType){
                        NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                .navigate(R.id.action_Selected_location_to_vehiclestype_available, bundle);
                    } else {
                        NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                .navigate(R.id.action_Selected_location_to_Vehicles_Available, bundle);
                    }

                }
            });

            getCalabderData();

           /* //calander
            calendar.setCalendarListener(calendarListener);
            Calendar current = Calendar.getInstance();
            Calendar yearAfter = Calendar.getInstance();
            yearAfter.add(Calendar.DATE, 365);
            calendar.setCurrentMonth(current);
            calendar.setSelectableDateRange(current, yearAfter);

            SimpleDateFormat df=new SimpleDateFormat("HH:mm");
            current.add(Calendar.MINUTE,60);
            String formatedtime=df.format(current.getTime());
            binding.pickupTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));

            SimpleDateFormat df1=new SimpleDateFormat("HH:mm");
            String formatedtime1=df1.format(current.getTime());
            binding.returnTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));


            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            String currentDateandTime = sdf.format(new Date());
            binding.lblstartdate.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.monthwithdate));

            cmP_DATE_FORMAT= UserData.companyModel.MinReservationDays;
           // cmP_DATE_FORMAT= 4;
            current.add(Calendar.DATE,(cmP_DATE_FORMAT));
            String enddate = sdf.format(current.getTime());
            binding.lblenddate.setText(enddate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(new Date());
            EndDate = sdfDate.format(current.getTime());

            Log.e(TAG, "onViewCreated: " + binding.pickupTime.getText().toString() + " / " +
                    binding.returnTime.getText().toString()  + " / " +  binding.lblstartdate.getText().toString()  + " / " +
                    binding.lblenddate.getText().toString() );

            if (startdatejourney != null) {
                //StarDate = "2021-10-18";
                binding.lblstartdate.setText(startdatejourney);
                binding.lblenddate.setText(enddatejourney);
                binding.returnTime.setText(endtimejourney);
                binding.pickupTime.setText(starttimejourney);

            } else {
                startdatejourney = binding.lblstartdate.getText().toString();
            }

            *//*String pickupdatetime = StarDate + ":"+ txtpickuptime;
            String dropdatetime = EndDate + ":" + txtreturntime;
            SimpleDateFormat datewithtime = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
            Date date = datewithtime.parse(pickupdatetime);*//*
*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            location = (LocationList) getArguments().getSerializable("model");
            returnlocation = (LocationList) getArguments().getSerializable("models");
            bundle.putSerializable("locModel",getArguments().getSerializable("locModel"));
            LocationList[]  locationListsData;
            locationListsData = (LocationList[]) getArguments().getSerializable("locModel");
            setLocation(locationListsData,false);
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.pickuploc.lblselectedlocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putSerializable("model", location);
                bundle.putBoolean("locationType", true);
                startdatejourney = binding.lblstartdate.getText().toString();
                enddatejourney = binding.lblenddate.getText().toString();
                starttimejourney = binding.pickupTime.getText().toString();
                endtimejourney = binding.returnTime.getText().toString();
                getDropdownId(binding.businessMaster.getSelectedItem().toString());
                businessmaster = idd;
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_Available_location, bundle);
            }
        });
        binding.droploc.lblselectedlocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putSerializable("model", location);
                bundle.putBoolean("locationType", false);
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_Available_location, bundle);
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    private final CalendarListener calendarListener = new CalendarListener()
    {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate)
        {
                if (lease){
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
                    String formattedDate = sdf.format(startDate.getTime());
                    binding.lblstartdate.setText(formattedDate);

                    String formattedDate1 = sdf.format(startDate.getTime());
                    binding.lblenddate.setText(getRerundate(formattedDate));
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    StarDate = sdfDate.format(startDate.getTime());

                    EndDate = DateConvert.DateConverter(DateType.monthwithdate,binding.lblenddate.getText().toString(), DateType.yyyyMMddD);
                    //EndDate = sdfDate.format(binding.lblenddate.getText().toString());
                    binding.calenderLayout.setVisibility(View.GONE);
                }
        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate)
        {
            if (!lease){
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
                String formattedDate = sdf.format(startDate.getTime());
                binding.lblstartdate.setText(formattedDate);
                String formattedDate1 = sdf.format(endDate.getTime());
                binding.lblenddate.setText(formattedDate1);

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                StarDate = sdfDate.format(startDate.getTime());

                if (endDate != null)

                    EndDate = sdfDate.format(endDate.getTime());

                if (endDate.getTime() != null){
                    binding.calenderLayout.setVisibility(View.GONE);
                }
            }
        }
    };


    public String getRerundate(String cdate){
        String rdate;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
        Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(cdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        c.add(Calendar.DATE, 6);
            rdate = sdf.format(c.getTime());
        return  rdate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login_Layout:
                /*Bundle Booking=new Bundle();
                Booking.putBundle("returnLocation",returnLocationBundle);
                Booking.putBundle("location",locationBundle);
                Booking.putBoolean("locationType",locationType);
                Booking.putBoolean("initialSelect",initialSelect);*/
                bundle.putBoolean("LoginForUser",true);
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_LoginFragment,bundle);
                break;

            case R.id.Register_Layout:
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;

            case R.id.img_plus:
                binding.differentLocationLayout.setVisibility(View.VISIBLE);
              //  binding.droploc.setLocation(location);
                /*binding.returnLocationName.setText(location.Name);
                binding.txtReturnStreet.setText(Html.fromHtml(location.AddressesModel.PreviewAddress));*/
                break;

            case  R.id.discard:
                /*NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_Search_activity);*/
                Intent i = new Intent(getActivity(), Activity_Home.class);
                startActivity(i);
                break;

            case R.id.back:
       /*         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are You Sure You Want To Cancel?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                        .navigate(R.id.action_Selected_location_to_Search_activity);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();*/
              /*  Intent ii = new Intent(getActivity(), Activity_Home.class);
                startActivity(ii);*/
                NavHostFragment.findNavController(Fragment_Selected_Location.this).popBackStack();
                break;
            case R.id.lblselectedlocation1:
                bundle.putSerializable("model", location);
                bundle.putBoolean("locationType", true);
                startdatejourney = binding.lblstartdate.getText().toString();
                enddatejourney = binding.lblenddate.getText().toString();
                starttimejourney = binding.pickupTime.getText().toString();
                endtimejourney = binding.returnTime.getText().toString();
                getDropdownId(binding.businessMaster.getSelectedItem().toString());
                businessmaster = idd;
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_Available_location, bundle);
                break;

            case R.id.lbl_change:
                bundle.putSerializable("model", location);
                bundle.putBoolean("locationType", false);
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_Available_location, bundle);
                break;
            case R.id.lblstartdate:
                binding.calenderLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_done:
                binding.calenderLayout.setVisibility(View.GONE);
                break;
            case R.id.txt_cancelCal:
                binding.calenderLayout.setVisibility(View.GONE);
                break;
            case R.id.pickup_time:
                binding.timeRelativeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.return_time:
                binding.timeRelativeLayout2.setVisibility(View.VISIBLE);
                break;
            case R.id.pickup_cancel:
                binding.timeRelativeLayout.setVisibility(View.GONE);
                break;
            case R.id.droptime_cancel:
                binding.timeRelativeLayout2.setVisibility(View.GONE);
                break;
        }
    }

/*    OnResponseListener AvailableLocation = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray locationlist = resultSet.getJSONArray("Data");
                            locationListsData = loginRes.getModel(locationlist.toString(), LocationList[].class);
                            for (int i = 0; i <locationListsData.length ; i++) {
                                location = locationListsData[i];
                                returnlocation = locationListsData[i];
                                break;
                            }


                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
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
    };*/


    OnResponseListener AvailableLocation2 =   new OnResponseListener() {
    @Override
    public void onSuccess(String response, HashMap<String, String> headers) {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        final JSONArray locationlist = resultSet.getJSONArray("Data");
                        LocationList[]  locationListsData;
                        locationListsData = loginRes.getModel(locationlist.toString(), LocationList[].class);
                        bundle.putSerializable("locModel",locationListsData);
                        setLocation(locationListsData,true);
                   /*     if (locationListsData.length == 1){
                            binding.differentLocationLayout.setVisibility(View.GONE);
                            binding.addreturnloc.setVisibility(View.GONE);
                            binding.addreturnlocline.setVisibility(View.GONE);
                            binding.pickuploc.lblselectedlocation1.setVisibility(View.GONE);
                        } else {
                            binding.differentLocationLayout.setVisibility(View.GONE);
                            binding.addreturnloc.setVisibility(View.VISIBLE);
                           // binding.addreturnlocline.setVisibility(View.VISIBLE);
                            binding.pickuploc.lblselectedlocation1.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i <locationListsData.length ; i++) {
                            location = locationListsData[i];
                            returnlocation = locationListsData[i];

                          //  if (location.Id == reservationBusinessSource.DefaultLocationId) {
                                binding.pickuploc.setLocation(location);
                                binding.droploc.setLocation(returnlocation);
                           // }
                           // if(locationListsData[i].Name.matches("Head Office")) {
                               *//* binding.txtLocationName.setText(location.Name);
                                binding.txtStreetLoc.setText(Html.fromHtml(location.AddressesModel.PreviewAddress));
                                binding.returnLocationName.setText(returnlocation.Name);
                                binding.txtReturnStreet.setText(Html.fromHtml(returnlocation.AddressesModel.PreviewAddress));*//*

                           // }
                           // break;
                        }*/
                        fullProgressbar.hide();


                    }
                    else
                    {
                        fullProgressbar.hide();
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }
                }
                catch (Exception e)
                {
                    fullProgressbar.hide();
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onError(String error) {
        fullProgressbar.hide();
    }
};

    public void listSpinner(List<ReservationBusinessSource> data){
        List<String> business = new ArrayList<>();
        int select = 0;
        for (int i = 0; i <data.size() ; i++) {
          //  if (data.get(i).TableType == BUSINESSBOOKINGTYPE){
                business.add(data.get(i).Name);
               if (data.get(i).Id == businessSource.Id){
                   select =i;
               }
          //  }
           /* if (data.get(i).Id == 4){
                select = i;
            }*/
        }
        ArrayAdapter<String> adapterbusiness = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,business);
        binding.businessMaster.setAdapter(adapterbusiness);
        binding.businessMaster.setSelection(select);
        fullProgressbar.hide();
        if (Helper.isDropdown) {
            binding.businessMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(TAG, "onItemSelected: " + idd );
                    getDropdownId(binding.businessMaster.getSelectedItem().toString());
                    new ApiService(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(() -> {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                                    loginRes.storedata("reservationBusinessSource",getReservationList.toString());
                                    reservationBusinessSource = loginRes.getModel(getReservationList.toString(),ReservationBusinessSource.class);
                                    String locationss = getReservationList.getString("LocationMasterIds");
                                    loginRes.storedata("VehicleTypeMasterIds", getReservationList.getString("VehicleTypeMasterIds"));

                                    Log.e(TAG, "onSuccess: " + locationss );
                                    Log.e(TAG, "onSuccess: " + reservationBusinessSource.LocationMasterIds );
                                    JSONArray ints = new JSONArray();
                                    for (int i = 0; i < locationss.split(",").length; i++) {
                                        ints.put(Integer.valueOf(locationss.split(",")[i].trim()));
                                    }
                                    new ApiService(AvailableLocation2, RequestType.POST, AVAILABLELOCATION, BASE_URL_LOGIN, header, params.getAvailableLocation(ints));
                                   //
                                    if (reservationBusinessSource.ReservationMainType == ReservationMainType.Recurring.inte){
                                        startdatejourney = null;
                                        lease = true;
                                        getCalabderData();
                                        loginRes.storedata("VehicleTypeMasterIds", "0");
                                    } else {
                                        startdatejourney = null;
                                        lease = false;
                                        getCalabderData();
                                    }
                                    /*if (idd == 41){
                                        startdatejourney = null;
                                        lease = true;
                                        getCalabderData();
                                        loginRes.storedata("VehicleTypeMasterIds", "0");
                                    }*/

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {

                        }
                    }, RequestType.POST, BusinessSourceMaster, BASE_URL_LOGIN, header, params.getBusinessSource(idd));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void getDropdownId(String name){
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).Name.equals(name)){
                idd = data.get(i).Id;
            }
        }
    }


    private ArrayList<SubTitle> getSubTitles() {
        final ArrayList<SubTitle> subTitles = new ArrayList<>();
        final Calendar tmrw = Calendar.getInstance();
        tmrw.add(Calendar.DAY_OF_MONTH, 1);
        subTitles.add(new SubTitle(tmrw.getTime(), "₹1000"));
        return subTitles;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validation (String pickupdate, String dropdate){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
       /* final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final String firstInput = reader.readLine();
        final String secondInput = reader.readLine();*/

        final LocalDate firstDate = LocalDate.parse(pickupdate, formatter);
        final LocalDate secondDate = LocalDate.parse(dropdate, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
        System.out.println("Days between: " + days);
        boolean value = false;
        if (days==UserData.companyModel.MinReservationDays){
            value = true;
        }
        return  value;
    }

    public void setLocation( LocationList[]  locationListsData,Boolean value){
        if (locationListsData.length == 1){
            binding.differentLocationLayout.setVisibility(View.GONE);
            binding.addreturnloc.setVisibility(View.GONE);
            binding.addreturnlocline.setVisibility(View.GONE);
            binding.pickuploc.lblselectedlocation1.setVisibility(View.GONE);
        } else {
            binding.differentLocationLayout.setVisibility(View.GONE);
            binding.addreturnloc.setVisibility(View.VISIBLE);
            // binding.addreturnlocline.setVisibility(View.VISIBLE);
            binding.pickuploc.lblselectedlocation1.setVisibility(View.VISIBLE);
        }
        if (value){
        for (int i = 0; i <locationListsData.length; i++) {
            //LocationList location = new LocationList();
            //LocationList returnlocation = new LocationList();
            location = locationListsData[i];
            returnlocation = locationListsData[i];
            //UserData.loginResponse.User.addressesModel.Latitude ==

            //Double   Helper.getAmtount()
            if (UserData.loginResponse.User.addressesModel!=null){
                if (UserData.loginResponse.User.addressesModel.AddressFor == location.Id){
                    binding.pickuploc.setLocation(location);
                    binding.droploc.setLocation(returnlocation);
                    break;
                }
            } else if(location.Id == reservationBusinessSource.DefaultLocationId)   {
                binding.pickuploc.setLocation(location);
                binding.droploc.setLocation(returnlocation);
                break;
            }

         /*   if (location.Id == reservationBusinessSource.DefaultLocationId) {
                binding.pickuploc.setLocation(location);
                binding.droploc.setLocation(returnlocation);
                break;
                 }*/
            }
        } else {
            binding.pickuploc.setLocation(location);
            binding.differentLocationLayout.setVisibility(View.VISIBLE);
            /*if (returnlocation.Id == 0){
                binding.droploc.setLocation(location);
            } else {
                binding.droploc.setLocation(returnlocation);
            }*/

            if(returnlocation != null){
                binding.droploc.setLocation(returnlocation);
            } else {
                binding.droploc.setLocation(location);
            }
        }

     /*   if (!value){
            try {
                location = (LocationList) getArguments().getSerializable("model");
                returnlocation = (LocationList) getArguments().getSerializable("models");
            } catch (Exception e){
                e.printStackTrace();
            }
            binding.pickuploc.setLocation(location);
            binding.droploc.setLocation(returnlocation);
            if (getArguments().getBoolean("locationType")){
                binding.differentLocationLayout.setVisibility(View.VISIBLE);
                binding.addreturnloc.setVisibility(View.VISIBLE);
                //binding.addreturnlocline.setVisibility(View.VISIBLE);
                binding.pickuploc.lblselectedlocation1.setVisibility(View.VISIBLE);
            }
        }*/

    }

    public void getCalabderData(){
        //calander
        calendar.setCalendarListener(calendarListener);
        Calendar current = Calendar.getInstance();
        Calendar yearAfter = Calendar.getInstance();
        yearAfter.add(Calendar.DATE, 365);
        calendar.setCurrentMonth(current);
        calendar.setSelectableDateRange(current, yearAfter);

        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
        current.add(Calendar.MINUTE,60);
        String formatedtime=df.format(current.getTime());
        binding.pickupTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));

        SimpleDateFormat df1=new SimpleDateFormat("HH:mm");
        String formatedtime1=df1.format(current.getTime());
        binding.returnTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));


        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        String currentDateandTime = sdf.format(new Date());
        binding.lblstartdate.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.monthwithdate));

       // cmP_DATE_FORMAT = UserData.companyModel.MinReservationDays;
        if (lease){
            cmP_DATE_FORMAT = 6;
        } else {
            cmP_DATE_FORMAT = UserData.companyModel.MinReservationDays;
        }
        // cmP_DATE_FORMAT= 4;
        current.add(Calendar.DATE,(cmP_DATE_FORMAT));
        String enddate = sdf.format(current.getTime());
        binding.lblenddate.setText(enddate);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        StarDate = sdfDate.format(new Date());
        EndDate = sdfDate.format(current.getTime());

        Log.e(TAG, "onViewCreated: " + binding.pickupTime.getText().toString() + " / " +
                binding.returnTime.getText().toString()  + " / " +  binding.lblstartdate.getText().toString()  + " / " +
                binding.lblenddate.getText().toString() );

      //check startdate logic
      if (startdatejourney != null) {
            //StarDate = "2021-10-18";
            binding.lblstartdate.setText(startdatejourney);
            binding.lblenddate.setText(enddatejourney);
            binding.returnTime.setText(endtimejourney);
            binding.pickupTime.setText(starttimejourney);

        } else {
            startdatejourney = binding.lblstartdate.getText().toString();
        }
            /*String pickupdatetime = StarDate + ":"+ txtpickuptime;
            String dropdatetime = EndDate + ":" + txtreturntime;
            SimpleDateFormat datewithtime = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
            Date date = datewithtime.parse(pickupdatetime);*/
    }
}