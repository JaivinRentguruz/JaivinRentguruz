package com.rentguruz.app.flexiicar.user;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.fragment.NavHostFragment;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAgreementDatechangeBinding;
import com.rentguruz.app.databinding.TimepickupListBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.reservation.ReservationDataChanges;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationOriginDataModels;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONDATECHANGE;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Agreement_DateChange extends BaseFragment {

    FragmentAgreementDatechangeBinding binding;
    private DateConvert dateConvert = new DateConvert();
    String StarDate, EndDate;
    int cmP_DATE_FORMAT;
    public static String startdatejourney,enddatejourney,starttimejourney,endtimejourney;
    Reservation reservations;
    SummaryDisplay summaryDisplay;
    ReservationSummarry  reservationSummarry;
    private static Boolean changeDate = false;
    ReservationDataChanges reservationDataChanges;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentAgreementDatechangeBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        changeDate = false;
        Log.e(TAG, "onViewCreated: " + changeDate );
        reservationDataChanges = new ReservationDataChanges();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reservationSummarry = new ReservationSummarry();
        binding.header.screenHeader.setText(getResources().getString(R.string.datechange));
        binding.header.back.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.lblstartdate.setOnClickListener(this);
        binding.txtDone.setOnClickListener(this);
        binding.txtCancelCal.setOnClickListener(this);
        binding.pickupTime.setOnClickListener(this);
        binding.returnTime.setOnClickListener(this);
        binding.pickupCancel.setOnClickListener(this);
        binding.droptimeCancel.setOnClickListener(this);

        summaryDisplay = new SummaryDisplay(getActivity());
        try {
            for (int i = 0; i <47 ; i++) {
                getSubview(i);
                TimepickupListBinding timepickupListBinding = TimepickupListBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false);
                timepickupListBinding.getRoot().setId(200 + i);
                timepickupListBinding.getRoot().setLayoutParams(subparams);

                final int tempPosition = i;
                timepickupListBinding.linearlayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.pickupTime.setText(timepickupListBinding.txtTime.getText());
                        binding.returnTime.setText(timepickupListBinding.txtTime.getText());
                        int len = binding.relativeLayoutTime.getChildCount();
                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = binding.relativeLayoutTime.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = binding.relativeLayoutTime.getChildAt(m).findViewById(R.id.txt_time);
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

                timepickupListBinding.txtTime.setText(hour + ":" + mins);
                binding.relativeLayoutTime.addView(timepickupListBinding.getRoot());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            for (int i = 0; i <47 ; i++) {
                getSubview(i);
                TimepickupListBinding timepickupListBinding = TimepickupListBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false);
                timepickupListBinding.getRoot().setId(200 + i);
                timepickupListBinding.getRoot().setLayoutParams(subparams);

                final int tempPosition = i;
                timepickupListBinding.linearlayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.pickupTime.setText(timepickupListBinding.txtTime.getText());
                        binding.returnTime.setText(timepickupListBinding.txtTime.getText());
                        int len = binding.droptimeLayout.getChildCount();
                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = binding.droptimeLayout.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = binding.droptimeLayout.getChildAt(m).findViewById(R.id.txt_time);
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

                timepickupListBinding.txtTime.setText(hour + ":" + mins);
                binding.droptimeLayout.addView(timepickupListBinding.getRoot());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getBookingCalander();

        try {
            reservations = new Reservation();
            reservations = (Reservation) getArguments().getSerializable("reservation");
            binding.reservation.setReservation(reservations);

            reservationDataChanges.ReservationId = reservations.Id;
            binding.pickupTime.setText(dateConvert.allDateConverter(DateType.fulldate, reservations.CheckOutDate,DateType.time));
            binding.returnTime.setText(dateConvert.allDateConverter(DateType.fulldate, reservations.CheckInDate,DateType.time));
            binding.lblstartdate.setText(dateConvert.allDateConverter(DateType.fulldate, reservations.CheckOutDate,DateType.monthwithdate));
            binding.lblenddate.setText(dateConvert.allDateConverter(DateType.fulldate, reservations.CheckInDate,DateType.monthwithdate));

            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");

            reservationDataChanges.SendNotificationToCustomer = false;
            new ApiService(getTaxtDetails, RequestType.GET,
                    RESERVATIONGETBYID , BASE_URL_LOGIN, header, "?id="+reservations.Id);

        } catch (Exception e){
            e.printStackTrace();
        }

        try {

          /*  binding.cdrvCalendar.setCalendarListener(calendarListener);


            Calendar current = Calendar.getInstance();
            Calendar yearAfter = Calendar.getInstance();
            yearAfter.add(Calendar.DATE, 365);
            binding.cdrvCalendar.setSelectableDateRange(current, yearAfter);
            binding.pickupTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));
            binding.returnTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));
            binding.lblstartdate.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.monthwithdate));

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            cmP_DATE_FORMAT= UserData.companyModel.MinReservationDays;
            current.add(Calendar.DATE,(cmP_DATE_FORMAT));
            String enddate = sdf.format(current.getTime());
            binding.lblenddate.setText(enddate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(new Date());
            EndDate = sdfDate.format(current.getTime());

            binding.cdrvCalendar.setSelected(true);*/

/*
            Calendar current = Calendar.getInstance();
            final Calendar startSelectedDate = (Calendar) current.clone();
            int y = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckOutDate,DateType.y));
            int d = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckOutDate,DateType.d));
            int m = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckOutDate,DateType.m));
            startSelectedDate.set(y,m-1,d);
//            startSelectedDate.set(2022,3,7);
            //startSelectedDate.add(Calendar.DATE, validation(EndDate,StarDate));
            Log.e(TAG, "onViewCreated: " + startSelectedDate.getTime() );
            //final Calendar endSelectedDate = (Calendar) yearAfter.clone();
            final Calendar endSelectedDate = (Calendar) current.clone();

            int yy = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckInDate,DateType.y));
            int dd = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckInDate,DateType.d));
            int mm = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,reservations.CheckInDate,DateType.m));

            endSelectedDate.set(yy,mm-1,dd);
            //endSelectedDate.add(Calendar.DATE, validation(StarDate,EndDate));
            //endSelectedDate.set(2022,3,10);
            Log.e(TAG, "onViewCreated: " + endSelectedDate.getTime() );
            binding.cdrvCalendar.setSelectedDateRange(startSelectedDate,endSelectedDate);*/

            setBookingDate(reservations.CheckOutDate,reservations.CheckInDate);

            if (startdatejourney != null) {
                //StarDate = "2021-10-18";
                binding.lblstartdate.setText(startdatejourney);
                binding.lblenddate.setText(enddatejourney);
                binding.returnTime.setText(endtimejourney);
                binding.pickupTime.setText(starttimejourney);

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private final CalendarListener calendarListener = new CalendarListener()
    {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate)
        {

        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate)
        {

            binding.cdrvCalendar.setSelectedDateRange(startDate,endDate);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
            String formattedDate = sdf.format(startDate.getTime());
            binding.lblstartdate.setText(formattedDate);
            String formattedDate1 = sdf.format(endDate.getTime());
            binding.lblenddate.setText(formattedDate1);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(startDate.getTime());

            if (endDate != null)

                EndDate = sdfDate.format(endDate.getTime());

            String startdate = StarDate+"T"+binding.pickupTime.getText().toString()+":00";
            String enddate = EndDate+"T"+binding.returnTime.getText().toString()+":00";
            reservationSummarry.CheckOutDate = dateConvert.allDateConverter(DateType.fulldate, startdate,DateType.fulldate);
            reservationSummarry.CheckInDate = dateConvert.allDateConverter(DateType.fulldate, enddate,DateType.fulldate);
            reservationDataChanges.CheckOutDate =reservationSummarry.CheckOutDate;
            reservationDataChanges.CheckInDate =reservationSummarry.CheckInDate;
            reservationSummarry.ReservationInsuranceModel.InsuranceDate = reservationSummarry.CheckInDate;
            if (changeDate) {
                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_DateChange.this).popBackStack();
                break;
       /*     case R.id.next:
              //  NavHostFragment.findNavController(Fragment_Agreement_DateChange.this).navigate(R.id.test);
                break;*/
            case R.id.next:
                if (binding.customerinform.isChecked()){
                    reservationDataChanges.SendNotificationToCustomer = true;
                } else {
                    reservationDataChanges.SendNotificationToCustomer = false;
                }
                reservationDataChanges.ReservationOriginDataModels.add(new ReservationOriginDataModels(36,loginRes.modeltostring(TAG,reservationSummarry)));
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
                                        NavHostFragment.findNavController(Fragment_Agreement_DateChange.this).popBackStack();
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
                }, RequestType.POST, RESERVATIONDATECHANGE, BASE_URL_LOGIN, header, reservationDataChanges);
                break;
        }
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
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                //ReservationTimeModel reservationTimeModel = (ReservationTimeModel) loginRes.getModel(resultSet.toString(), ReservationTimeModel.class);
                                reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    reservationSummarry.ReservationOriginDataModels.stream().findAny().equals(104);
                                    reservationSummarry.ReservationOriginDataModels.removeIf(ReservationOriginDataModels -> (ReservationOriginDataModels.TableType == 104));
                                }
                                //binding.first.days.setText(summaryDisplay.getDatafrom(reservationSummarry.ReservationSummaryModels,100));

                               // binding.first.days.setText(reservationTimeModel.TotalDays + "Days");
                               // binding.first.totalamt.setText(summaryDisplay.getDatafrom(reservationSummarry.ReservationSummaryModels,100));
                               // binding.first.paymentmade.setText(summaryDisplay.getDatafrom(reservationSummarry.ReservationSummaryModels,103));
                                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

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
                        addTableType(104,summarry.toString());
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer validation (String pickupdate, String dropdate){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
       /* final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final String firstInput = reader.readLine();
        final String secondInput = reader.readLine();*/
        final LocalDate firstDate = LocalDate.parse(pickupdate, formatter);
        final LocalDate secondDate = LocalDate.parse(dropdate, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
        System.out.println("Days between: " + days);
        int value = 0;
        value = Math.toIntExact(days);
        return  value;
    }

    public void addTableType(int id, String Json){
        for (ReservationOriginDataModels m : reservationDataChanges.ReservationOriginDataModels ) {
            if (m.TableType==id){
                reservationDataChanges.ReservationOriginDataModels.remove(m);
            }
        }
        reservationDataChanges.ReservationOriginDataModels.add(new ReservationOriginDataModels(id,Json));
    }

    public void getBookingCalander(){
        try {
            binding.cdrvCalendar.setCalendarListener(calendarListener);

            Calendar current = Calendar.getInstance();
            Calendar yearAfter = Calendar.getInstance();
            yearAfter.add(Calendar.DATE, 365);

            Calendar yearBefore = Calendar.getInstance();
            yearBefore.add(Calendar.DATE, -365);

            binding.cdrvCalendar.setSelectableDateRange(yearBefore, yearAfter);
            binding.pickupTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));
            binding.returnTime.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.time));
            binding.lblstartdate.setText(dateConvert.allDateConverter(DateType.defaultdate, current.getTime().toString(),DateType.monthwithdate));

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            cmP_DATE_FORMAT= UserData.companyModel.MinReservationDays;
            current.add(Calendar.DATE,(cmP_DATE_FORMAT));
            String enddate = sdf.format(current.getTime());
            binding.lblenddate.setText(enddate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(new Date());
            EndDate = sdfDate.format(current.getTime());

            binding.cdrvCalendar.setSelected(true);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setBookingDate (String checkoutdata, String checkindate){

        try {
            Calendar current = Calendar.getInstance();
            final Calendar startSelectedDate = (Calendar) current.clone();
            int y = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkoutdata,DateType.y));
            int d = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkoutdata,DateType.d));
            int m = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkoutdata,DateType.m));
            startSelectedDate.set(y,m-1,d);
//            startSelectedDate.set(2022,3,7);
            //startSelectedDate.add(Calendar.DATE, validation(EndDate,StarDate));
            Log.e(TAG, "onViewCreated: " + startSelectedDate.getTime() );
            //final Calendar endSelectedDate = (Calendar) yearAfter.clone();
            final Calendar endSelectedDate = (Calendar) current.clone();

            int yy = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkindate,DateType.y));
            int dd = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkindate,DateType.d));
            int mm = Integer.parseInt(DateConvert.DateConverter(DateType.fulldate,checkindate,DateType.m));

            endSelectedDate.set(yy,mm-1,dd);
            //endSelectedDate.add(Calendar.DATE, validation(StarDate,EndDate));
            //endSelectedDate.set(2022,3,10);
            Log.e(TAG, "onViewCreated: " + endSelectedDate.getTime() );
            binding.cdrvCalendar.setSelectedDateRange(startSelectedDate,endSelectedDate);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}