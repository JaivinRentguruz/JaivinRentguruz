package com.rentguruz.app.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rentguruz.app.R;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.databinding.VehiclecashBinding;
import com.rentguruz.app.databinding.VehiclecatagorylistBinding;
import com.rentguruz.app.databinding.VehiclefiltermenuBinding;
import com.rentguruz.app.databinding.FiltermenuBinding;
import com.rentguruz.app.flexiicar.booking2.Customer_Booking_Activity;
import com.rentguruz.app.home.reservation.Activity_Reservation;
import com.rentguruz.app.home.reservation.Fragment_Reservations;
import com.rentguruz.app.home.vehicles.Activity_Vehicles;
import com.rentguruz.app.home.vehicles.Fragment_Vehicles;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.ReservationStatusDetail;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.CommonParams;
import com.rentguruz.app.model.parameter.ReservationStatuss;
import com.rentguruz.app.model.reservation.ReservationVehicleType;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.Reservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.DELETE;
import static com.rentguruz.app.apicall.ApiEndPoint.EMAIL;
import static com.rentguruz.app.apicall.ApiEndPoint.GETCUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.READYFORCHECKOUT;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONSTATUS;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONSTATUSUPDATE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFILTER;
import static com.rentguruz.app.apicall.ApiEndPoint.secondImage;


public class OptionMenu {

    public Activity activity;
    public RelativeLayout.LayoutParams subparams;
    public Fragment fragments;
    public LayoutInflater subinflater;
    public Handler handler = new Handler(Looper.getMainLooper());
    public LoginRes loginRes;
    public String TAG = "OptionMenu";
    Bundle back;
    public OptionMenu(Activity activity) {
        this.activity = activity;
        loginRes = new LoginRes(activity);
        back = new Bundle();
    }

    public void getSubview(int i){
        subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        subparams.addRule(RelativeLayout.BELOW, (200 + i - 1));
        subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        subparams.setMargins(0, 10, 0, 0);
        subinflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void optionVisible(RelativeLayout sucessfullRegi,Boolean value){
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(600);
        transition.addTarget( sucessfullRegi);
        TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
        if (value){
            sucessfullRegi.setVisibility(View.VISIBLE);
        } else {
            sucessfullRegi.setVisibility(View.GONE);
        }
    }

    public void optionmenulist(RelativeLayout sucessfullRegi, View view, Bundle bundle, Fragment fragment, DoHeader header, CommonParams params){
        fragments = fragment;
        TextView cancel = view.findViewById(R.id.cancel2);
        TextView cancel2 = view.findViewById(R.id.cancel);
        ImageView OpenbottomMenu = view.findViewById(R.id.optionmenu);
        Reservation reservation = new Reservation();
        reservation = (Reservation) bundle.getSerializable("reservation");
        bundle.putSerializable("reservation",reservation);
        back.putSerializable("reservation",reservation);
        back.putSerializable("resrvation", bundle.getSerializable("resrvation"));
        bundle.putBundle(activity.getResources().getString(R.string.bundle), back);
        cancel2.setOnClickListener(v -> optionVisible(sucessfullRegi,false));
        cancel.setOnClickListener(v -> {
            optionVisible(sucessfullRegi,false);
       /*     Transition transition2 = new Slide(Gravity.BOTTOM);
            transition2.setDuration(600);
            transition2.addTarget(sucessfullRegi);
            TransitionManager.beginDelayedTransition(sucessfullRegi,transition2);
            sucessfullRegi.setVisibility(View.GONE);*/
        });

        OpenbottomMenu.setOnClickListener(v -> {
            optionVisible(sucessfullRegi,true);
           /* Transition transition = new Slide(Gravity.BOTTOM);
            transition.setDuration(600);
            transition.addTarget( sucessfullRegi);
            TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
            sucessfullRegi.setVisibility(View.VISIBLE);*/
        });

        TextView editAgreement = view.findViewById(R.id.editAgreement);
        editAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginRes.getData(activity.getResources().getString(R.string.userType)).equals("1")){
                    Helper.isActiveCustomer = true;
                    Helper.reservationwithoutmap =true;
                    Intent i = new Intent(activity, Customer_Booking_Activity.class);
                    //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(i);
                } else {
                    Helper.B2BRESERVATION = true;
                    Intent i = new Intent(activity, Activity_Reservation.class);
                    //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(i);
                }


            }
        });
        bundle.putInt("reservationpmt", 1);
        bundle.putString("netrate","1");
        String  bodyParam = "?id=" + reservation.CustomerId + "&isActive=true"+"&"+"IsWithSummary=true";
        getText(view,R.id.changedate).setOnClickListener(v -> NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_SummaryOfChargesForAgreements_to_chnageAgeement,bundle));

        getText(view,R.id.changevehicle).setOnClickListener(v -> NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_SummaryOfChargesForAgreements_to_chnageAgeementVehicle,bundle));

        getText(view,R.id.extendagreement).setOnClickListener(v ->NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_SummaryOfChargesForAgreements_to_chnageAgeementextend,bundle) );

        getText(view,R.id.tollcharge).setOnClickListener(v -> NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_summary_to_toll_charge,bundle));

        getText(view,R.id.traffic_tic).setOnClickListener(v -> NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_summary_to_traffic_tic,bundle));

        getText(view,R.id.cancelAgreement).setOnClickListener(v -> NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_summary_to_cancel_agreement,bundle));

        Reservation finalReservation2 = reservation;

        getText(view,R.id.readycheckout).setOnClickListener(v -> {
            if  (finalReservation2.ReservationStatus == ReservationStatuss.Confirmed.inte) {

                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    String msg = responseJSON.getString("Message");
                                    CustomToast.showToast(activity,msg,1);
                                    if (status){

                                        Intent i = new Intent(activity, Activity_Reservation.class);
                                        activity.startActivity(i);
                                    }
                                    // CustomToast.showToast(activity,msg,1);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.POST, READYFORCHECKOUT, BASE_URL_LOGIN, header, params.readyforcheckout(finalReservation2.Id));

               /* AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to Allow Checkout Without Payment ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                new ApiService(new OnResponseListener() {
                                    @Override
                                    public void onSuccess(String response, HashMap<String, String> headers) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject responseJSON = new JSONObject(response);
                                                    Boolean status = responseJSON.getBoolean("Status");
                                                    String msg = responseJSON.getString("Message");
                                                    CustomToast.showToast(activity,msg,1);
                                                    if (status){

                                                        Intent i = new Intent(activity, Activity_Reservation.class);
                                                        activity.startActivity(i);
                                                    }
                                                   // CustomToast.showToast(activity,msg,1);
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {

                                    }
                                }, RequestType.POST, READYFORCHECKOUT, BASE_URL_LOGIN, header, params.readyforcheckout(finalReservation2.Id));
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
                                transition.addTarget( sucessfullRegi);
                                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                                TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
                                sucessfullRegi.setVisibility(View.GONE);
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
*/
            }
        });

        TextView printAgreement = view.findViewById(R.id.printAgreement);
        printAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_Printpreview,bundle);
            }
        });

        TextView deleteagreement = view.findViewById(R.id.deleteagreement);
        Reservation finalReservation = reservation;
        deleteagreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to Delete Agreement?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String msg = "You Have Been Successfully Delete Agreement!";
                                CustomToast.showToast(activity,msg,0);
                                new ApiService(DeleteReservation, RequestType.POST,
                                        DELETE, BASE_URL_LOGIN, header, params.getDelete(36, finalReservation.Id));
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
                                transition.addTarget( sucessfullRegi);
                                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                                TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
                                sucessfullRegi.setVisibility(View.GONE);
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView email_agreement = view.findViewById(R.id.email_agreement);
        email_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                            // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                            final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                            Log.e(TAG, "run: "+  customerProfile );

                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                            //builder.setMessage("Are you sure you want to Delete Agreement?");
                                            builder.setMessage(UserData.loginResponse.CompanyLabel.Agreement + " sent EmailId" + finalReservation.Email);
                                         /*   builder.setPositiveButton("Yes",
                                                    new DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which)
                                                        {
                                                            String msg = "You Have Been Successfully Delete Agreement!";
                                                            CustomToast.showToast(activity,msg,0);
                                                            new ApiService(DeleteReservation, RequestType.POST,
                                                                    DELETE, BASE_URL_LOGIN, header, params.getDelete(36, finalReservation.Id));
                                                        }
                                                    });*/
                                            builder.setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which)
                                                        {
                                                            dialog.dismiss();
                                                            optionVisible(sucessfullRegi,false);
                                                        }
                                                    });

                                            final AlertDialog dialog = builder.create();
                                            dialog.show();

                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }



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
                }, RequestType.POST, EMAIL, BASE_URL_LOGIN, header, params.getemail(finalReservation.Id, finalReservation.Email));
            }
        });

        TextView checkout = view.findViewById(R.id.checkout);
        Reservation finalReservation1 = reservation;
        //temparroy off for test
        if (finalReservation1.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte)
        {
           checkout.setText(UserData.loginResponse.CompanyLabel.CheckOut);
        }
        else if (finalReservation1.ReservationStatus == ReservationStatuss.CheckOut.inte)
        {
            checkout.setText(UserData.loginResponse.CompanyLabel.CheckIn);
        } else if (finalReservation1.ReservationStatus == ReservationStatuss.CheckIn.inte){
            checkout.setText(UserData.loginResponse.CompanyLabel.CheckIn);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* NavHostFragment.findNavController(fragments)
                        //    .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_In,bundle);*/

           /*     NavHostFragment.findNavController(fragments)
                        //.navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,bundle);
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_Location_And_Key,bundle);*/

                if (finalReservation1.ReservationStatus == ReservationStatuss.CheckIn.inte){
                    NavHostFragment.findNavController(fragments)
                            //    .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_In,bundle);
                }
               else if (finalReservation1.ReservationStatus != 5){
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,bundle);
                   // .navigate(R.id.action_SummaryOfChargesForAgreements_to_Location_And_Key,bundle);
                }
                else {
                    NavHostFragment.findNavController(fragments)
                        //    .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                              .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_In,bundle);
                }

               /* NavHostFragment.findNavController(fragments)
                        //    .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_In,bundle);*/

               /* if (finalReservation1.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte)
                {
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,bundle);
                } if (finalReservation1.ReservationStatus == ReservationStatuss.CheckOut.inte)
                {
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                }*/
            }
        });

        if (finalReservation1.ReservationStatus == ReservationStatuss.Draft.inte) {
            getText(view, R.id.readyconfirm).setVisibility(View.VISIBLE);
        }

        getText(view,R.id.readyconfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure ?" +
                        "\n" +
                        "Do you want to Confirmed this Booking without payment?");

                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new ApiService(new OnResponseListener() {
                                    @Override
                                    public void onSuccess(String response, HashMap<String, String> headers) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject responseJSON = new JSONObject(response);
                                                    Boolean status = responseJSON.getBoolean("Status");
                                                    String Message = responseJSON.getString("Message");
                                                    CustomToast.showToast(activity, Message,1);
                                                    if (status){
                                                        Intent i = new Intent(activity, Activity_Reservation.class);
                                                        activity.startActivity(i);

                                                    }


                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {

                                    }
                                }, RequestType.POST, RESERVATIONSTATUSUPDATE, BASE_URL_LOGIN, header, params.reservationstatusupdate(finalReservation.Id, ReservationStatuss.Confirmed.inte));
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                optionVisible(sucessfullRegi,false);
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView pmt = view.findViewById(R.id.paymentprocess);
        pmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (UserData.customerProfile != null){
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_Acceptance_signature_to_Agreements_to_payment,bundle);
                } else {*/

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
                                                // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                                final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                                loginRes.storedata("CustomerProfile", customerProfile.toString());
                                                UserData.UserDetail = customerProfile.toString();
                                                CustomerProfile customerProfile1 = new CustomerProfile();
                                                customerProfile1 =  loginRes.callFriend("CustomerProfile", CustomerProfile.class);
                                                UserData.customerProfile = customerProfile1;
                                                NavHostFragment.findNavController(fragment)
                                                        .navigate(R.id.action_Acceptance_signature_to_Agreements_to_payment,bundle);

                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }



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
                    }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, bodyParam);
                }

           /* }*/
        });

       /* setText(getText(view,R.id.paymentprocess), UserData.loginResponse.CompanyLabel.Payment + " " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.deleteagreement),  "Delete " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.editAgreement),  "Edit " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.printAgreement),  "Print " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.email_agreement),  "Email " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.changevehicle),  "Change " +UserData.loginResponse.CompanyLabel.Vehicle);
        setText(getText(view,R.id.extendagreement),  "Extend " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.tollcharge),  "Toll " +UserData.loginResponse.CompanyLabel.Charge);
        setText(getText(view,R.id.cancelAgreement),  "Cancel " +UserData.loginResponse.CompanyLabel.Agreement);*/

        setText(getText(view,R.id.paymentprocess), UserData.loginResponse.CompanyLabel.Payment );
        setText(getText(view,R.id.deleteagreement),  "Delete");
        setText(getText(view,R.id.editAgreement),  "Edit");
        setText(getText(view,R.id.printAgreement),  "Print");
        setText(getText(view,R.id.email_agreement),  "Email");
        setText(getText(view,R.id.changevehicle),   "Change " +UserData.loginResponse.CompanyLabel.Vehicle);
        setText(getText(view,R.id.extendagreement),  "Extend " + UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.tollcharge),  "Toll" +UserData.loginResponse.CompanyLabel.Charge);
        setText(getText(view,R.id.cancelAgreement),  "Cancel");

        if (reservation.ReservationStatus == ReservationStatuss.Draft.inte || reservation.ReservationStatus == ReservationStatuss.Confirmed.inte){
            setText(getText(view,R.id.readycheckout), "Ready For " + UserData.loginResponse.CompanyLabel.CheckOut);
            getText(view,R.id.readycheckout).setVisibility(View.VISIBLE);
        }
        setVisibility(getText(view,R.id.checkout), reservation);
    }

    /*public void optionmenulist(RelativeLayout sucessfullRegi, View view, Bundle bundle, Fragment fragment, DoHeader header, CommonParams params, View.OnClickListener clickListener){
        fragments = fragment;
        TextView cancel = view.findViewById(R.id.cancel2);
        TextView cancel2 = view.findViewById(R.id.cancel);
        ImageView OpenbottomMenu = view.findViewById(R.id.optionmenu);
        Reservation reservation = new Reservation();
        reservation = (Reservation) bundle.getSerializable("reservation");

        *//*cancel.setOnClickListener(clickListener);
        cancel2.setOnClickListener(clickListener);

        view.setOnClickListener(clickListener);*//*
        clickListener.onClick(view);
        switch (view.getId()){
            case R.id.cancel2:
            case R.id.cancel:
                optionVisible(sucessfullRegi,false);
                break;

            case R.id.optionmenu:
                optionVisible(sucessfullRegi,true);
                break;
        }

        *//*cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition2 = new Slide(Gravity.BOTTOM);
                transition2.setDuration(600);
                transition2.addTarget(sucessfullRegi);
                TransitionManager.beginDelayedTransition(sucessfullRegi,transition2);
                sucessfullRegi.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition2 = new Slide(Gravity.BOTTOM);
                transition2.setDuration(600);
                transition2.addTarget(sucessfullRegi);
                TransitionManager.beginDelayedTransition(sucessfullRegi,transition2);
                sucessfullRegi.setVisibility(View.GONE);
            }
        });
*//*

        OpenbottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                transition.addTarget( sucessfullRegi);
                TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
                sucessfullRegi.setVisibility(View.VISIBLE);
            }
        });

        TextView editAgreement = view.findViewById(R.id.editAgreement);
        editAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_chnageAgeement,bundle);
            }
        });

        TextView printAgreement = view.findViewById(R.id.printAgreement);
        printAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_SummaryOfChargesForAgreements_to_Printpreview,bundle);
            }
        });

        TextView deleteagreement = view.findViewById(R.id.deleteagreement);
        Reservation finalReservation = reservation;
        deleteagreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to Delete Agreement?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String msg = "You Have Been Successfully Delete Agreement!";
                                CustomToast.showToast(activity,msg,0);
                                new ApiService(DeleteReservation, RequestType.POST,
                                        DELETE, BASE_URL_LOGIN, header, params.getDelete(36, finalReservation.Id));
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
                                transition.addTarget( sucessfullRegi);
                                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                                TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
                                sucessfullRegi.setVisibility(View.GONE);
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView email_agreement = view.findViewById(R.id.email_agreement);
        email_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                            // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                            final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                            Log.e(TAG, "run: "+  customerProfile );

                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }



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
                }, RequestType.PUT, EMAIL, BASE_URL_LOGIN, header, params.getemail(finalReservation.Id, finalReservation.Email));
            }
        });

        TextView checkout = view.findViewById(R.id.checkout);
        Reservation finalReservation1 = reservation;
        if (finalReservation1.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte)
        {
           checkout.setText(UserData.loginResponse.CompanyLabel.CheckOut);
        }
        else if (finalReservation1.ReservationStatus == ReservationStatuss.CheckOut.inte)
        {
            checkout.setText(UserData.loginResponse.CompanyLabel.CheckIn);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalReservation1.ReservationStatus != 5){
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,bundle);
                } else {
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                }

               *//* if (finalReservation1.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte)
                {
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,bundle);
                } if (finalReservation1.ReservationStatus == ReservationStatuss.CheckOut.inte)
                {
                    NavHostFragment.findNavController(fragments)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,bundle);
                }*//*
            }
        });

        TextView pmt = view.findViewById(R.id.paymentprocess);
        pmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("reservationpmt", 1);
               *//* if (UserData.customerProfile != null){
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_Acceptance_signature_to_Agreements_to_payment,bundle);
                } else {*//*
                    String  bodyParam = "?id=" + finalReservation1.CustomerId + "&isActive=true"+"&"+"IsWithSummary=true";
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
                                                // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                                final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                                loginRes.storedata("CustomerProfile", customerProfile.toString());
                                                UserData.UserDetail = customerProfile.toString();
                                                CustomerProfile customerProfile1 = new CustomerProfile();
                                                customerProfile1 =  loginRes.callFriend("CustomerProfile", CustomerProfile.class);
                                                UserData.customerProfile = customerProfile1;
                                                NavHostFragment.findNavController(fragment)
                                                        .navigate(R.id.action_Acceptance_signature_to_Agreements_to_payment,bundle);

                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }



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
                    }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, bodyParam);
                }

           *//* }*//*
        });
    }*/

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
                        // CustomToast.showToast(getActivity(),msg,0);
                        NavHostFragment.findNavController(fragments).popBackStack();
                    }
                    else
                    {
                        CustomToast.showToast(activity,msg,1);
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

    public void setText(TextView view, String text){
        try {
            view.setText(text);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public TextView getText(View v, int id){
        return v.findViewById(id);
    }

    public void makePayment(Fragment fragment,Bundle bundle,DoHeader header,CommonParams params,int navid,int backid){
        Reservation reservation = new Reservation();
        reservation = (Reservation) bundle.getSerializable("reservation");
        String  bodyParam = "?id=" + reservation.CustomerId + "&isActive=true"+"&"+"IsWithSummary=true";
        back.putSerializable("reservation",reservation);
        back.putSerializable("resrvation", bundle.getSerializable("resrvation"));
        back.putSerializable("checklist", bundle.getSerializable("checklist"));

        back.putInt(activity.getResources().getString(R.string.back), backid);
        bundle.putBundle(activity.getResources().getString(R.string.bundle), back);
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
                                    // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                    final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                    loginRes.storedata("CustomerProfile", customerProfile.toString());
                                    UserData.UserDetail = customerProfile.toString();
                                    CustomerProfile customerProfile1 = new CustomerProfile();
                                    customerProfile1 =  loginRes.callFriend("CustomerProfile", CustomerProfile.class);
                                    UserData.customerProfile = customerProfile1;
                                    bundle.putSerializable("CustomerProfile",customerProfile1);
                                    bundle.putSerializable("customerDetail",customerProfile1);
                                   /* NavHostFragment.findNavController(fragment)
                                            .navigate(R.id.action_Acceptance_signature_to_Agreements_to_payment,bundle);*/

                                    NavHostFragment.findNavController(fragment)
                                            .navigate(navid,bundle);

                                } catch (Exception e){
                                    e.printStackTrace();
                                }



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
        }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, bodyParam);

    }


    public void filtermenu(RelativeLayout sucessfullRegi, View view, Bundle bundle, Fragment fragment, DoHeader header, CommonParams params){
        getText(view,R.id.cancel2).setOnClickListener(v -> optionVisible(sucessfullRegi,false));
        getText(view,R.id.cancel).setOnClickListener(v -> optionVisible(sucessfullRegi,false));

        ImageView OpenbottomMenu = view.findViewById(R.id.optionmenu);
        OpenbottomMenu.setOnClickListener(v -> optionVisible(sucessfullRegi,true));
       // Trsv

        setText(getText(view,R.id.all), "All "  + "("+ Fragment_Reservations.total +  ")");
        getText(view,R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("status",false);
                NavHostFragment.findNavController(fragment).navigate(R.id.same, bundle);
            }
        });
        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");

                            JSONArray resultSet = responseJSON.getJSONArray("Data");
                            ReservationStatusDetail[] reservationStatusDetails = loginRes.getModel(resultSet.toString(), ReservationStatusDetail[].class);

                            for (int i = 0; i <reservationStatusDetails.length ; i++) {
                               //if (reservationStatusDetails[i].ReservationStatus == )

                                  if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Draft.inte){
                                      setText(getText(view,R.id.draft), "Draft " + "("+reservationStatusDetails[i].Total  +  ")");
                                      bundle.putBoolean("status",true);
                                      //bundle.putInt("statuss", ReservationStatuss.Draft.inte);
                                      setFilterview(getText(view,R.id.draft),bundle,fragment, ReservationStatuss.Draft.inte);
                                     /* getText(view,R.id.draft).setOnClickListener(v -> {
                                          bundle.putBoolean("status",true);
                                          bundle.putInt("statuss", ReservationStatuss.Draft.inte);
                                          NavHostFragment.findNavController(fragment).navigate(R.id.same, bundle);
                                      });*/
                                  }



                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Cancelled.inte){
                                    setText(getText(view,R.id.cancelRes), "Cancelled " + "("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                   // bundle.putInt("statuss", ReservationStatuss.Cancelled.inte);
                                    setFilterview(getText(view,R.id.cancelRes),bundle,fragment, ReservationStatuss.Cancelled.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.NoShow.inte){
                                    setText(getText(view,R.id.noshow), "No Show " + "("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                   // bundle.putInt("statuss", ReservationStatuss.NoShow.inte);
                                    setFilterview(getText(view,R.id.noshow),bundle,fragment, ReservationStatuss.NoShow.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Confirmed.inte){
                                    setText(getText(view,R.id.confirmT), "Confirmed " + "("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    //bundle.putInt("statuss", ReservationStatuss.Confirmed.inte);
                                    setFilterview(getText(view,R.id.confirmT),bundle,fragment, ReservationStatuss.Confirmed.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte){
                                    setText(getText(view,R.id.rforchk), "Ready For " +UserData.loginResponse.CompanyLabel.CheckOut  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    //bundle.putInt("statuss", ReservationStatuss.ReadyForCheckOut.inte);
                                    setFilterview(getText(view,R.id.rforchk),bundle,fragment, ReservationStatuss.ReadyForCheckOut.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.CheckOut.inte){
                                    setText(getText(view,R.id.checkoutrsv), UserData.loginResponse.CompanyLabel.CheckOut  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                  //  bundle.putInt("statuss", ReservationStatuss.CheckOut.inte);
                                    setFilterview(getText(view,R.id.checkoutrsv),bundle,fragment, ReservationStatuss.CheckOut.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.CheckIn.inte){
                                    setText(getText(view,R.id.checkinrsv), UserData.loginResponse.CompanyLabel.CheckIn  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                   // bundle.putInt("statuss", ReservationStatuss.CheckIn.inte);
                                    setFilterview(getText(view,R.id.checkinrsv),bundle,fragment, ReservationStatuss.CheckIn.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.PendingPayment.inte){
                                    setText(getText(view,R.id.pendingpmt), "Pending "+ UserData.loginResponse.CompanyLabel.Payment  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                   // bundle.putInt("statuss", ReservationStatuss.PendingPayment.inte);
                                    setFilterview(getText(view,R.id.pendingpmt),bundle,fragment, ReservationStatuss.PendingPayment.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.PaymentOutStanding.inte){
                                    setText(getText(view,R.id.pmtos), UserData.loginResponse.CompanyLabel.Payment+ " Out Standing"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    //bundle.putInt("statuss", ReservationStatuss.PaymentOutStanding.inte);
                                    setFilterview(getText(view,R.id.pmtos),bundle,fragment,ReservationStatuss.PaymentOutStanding.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.DepositOutStanding.inte){
                                    setText(getText(view,R.id.depositos), UserData.loginResponse.CompanyLabel.Deposit+ " Out Standing"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                   // bundle.putInt("statuss", ReservationStatuss.DepositOutStanding.inte);
                                    setFilterview(getText(view,R.id.depositos),bundle,fragment,ReservationStatuss.DepositOutStanding.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.OverDue.inte){
                                    setText(getText(view,R.id.overdue), "Over Due"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    //bundle.putInt("statuss", ReservationStatuss.OverDue.inte);
                                    setFilterview(getText(view,R.id.overdue),bundle,fragment,ReservationStatuss.OverDue.inte);
                                }

                                if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Closed.inte){
                                    setText(getText(view,R.id.close), "Closed"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    //bundle.putInt("statuss", ReservationStatuss.Closed.inte);
                                    setFilterview(getText(view,R.id.close),bundle,fragment,ReservationStatuss.Closed.inte);
                                }

                              /*  if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Arc.inte){
                                    setText(getText(view,R.id.close), "Closed"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    bundle.putInt("statuss", ReservationStatuss.Closed.inte);
                                    setFilterview(getText(view,R.id.close),bundle,fragment);
                                }*/

                            }


                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.GET,RESERVATIONSTATUS,BASE_URL_LOGIN,header,"?companyId="+Helper.di);
    }


    public void filtermenu(RelativeLayout sucessfullRegi, View view, Bundle bundle, Fragment fragment, DoHeader header, CommonParams params,ReservationStatusDetail[] reservationStatusDetails ){

    /*    VehiclefiltermenuBinding vehiclefiltermenuBinding = VehiclefiltermenuBinding.inflate(activity.getLayoutInflater(),
                activity.findViewById(android.R.id.content), false);
        vehiclefiltermenuBinding.setUiColor(UserData.UiColor);
        sucessfullRegi.addView(vehiclefiltermenuBinding.getRoot());*/

       /* FiltermenuBinding filtermenuBinding = FiltermenuBinding.inflate(activity.getLayoutInflater(),
                activity.findViewById(android.R.id.content), false);
        filtermenuBinding.setUiColor(UserData.UiColor);
        sucessfullRegi.addView(filtermenuBinding.getRoot());*/

        getText(view,R.id.cancel2).setOnClickListener(v -> optionVisible(sucessfullRegi,false));
        getText(view,R.id.cancel).setOnClickListener(v -> optionVisible(sucessfullRegi,false));

        ImageView OpenbottomMenu = view.findViewById(R.id.optionmenu);
        OpenbottomMenu.setOnClickListener(v -> optionVisible(sucessfullRegi,true));
        // Trsv

        setText(getText(view,R.id.all), "All "  + "("+ Fragment_Reservations.total +  ")");
        getText(view,R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putBoolean("status",false);
                NavHostFragment.findNavController(fragment).navigate(R.id.same, bundle);
            }
        });

        for (int i = 0; i <reservationStatusDetails.length ; i++) {
            //if (reservationStatusDetails[i].ReservationStatus == )

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Draft.inte){
                setText(getText(view,R.id.draft), "Draft " + "("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.Draft.inte);
                setFilterview(getText(view,R.id.draft),bundle,fragment, ReservationStatuss.Draft.inte);
                                     /* getText(view,R.id.draft).setOnClickListener(v -> {
                                          bundle.putBoolean("status",true);
                                          bundle.putInt("statuss", ReservationStatuss.Draft.inte);
                                          NavHostFragment.findNavController(fragment).navigate(R.id.same, bundle);
                                      });*/
            }



            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Cancelled.inte){
                setText(getText(view,R.id.cancelRes), "Cancelled " + "("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                // bundle.putInt("statuss", ReservationStatuss.Cancelled.inte);
                setFilterview(getText(view,R.id.cancelRes),bundle,fragment, ReservationStatuss.Cancelled.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.NoShow.inte){
                setText(getText(view,R.id.noshow), "No Show " + "("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                // bundle.putInt("statuss", ReservationStatuss.NoShow.inte);
                setFilterview(getText(view,R.id.noshow),bundle,fragment, ReservationStatuss.NoShow.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Confirmed.inte){
                setText(getText(view,R.id.confirmT), "Confirmed " + "("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.Confirmed.inte);
                setFilterview(getText(view,R.id.confirmT),bundle,fragment, ReservationStatuss.Confirmed.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte){
                setText(getText(view,R.id.rforchk), "Ready For " + UserData.loginResponse.CompanyLabel.CheckOut  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.ReadyForCheckOut.inte);
                setFilterview(getText(view,R.id.rforchk),bundle,fragment, ReservationStatuss.ReadyForCheckOut.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.CheckOut.inte){
                setText(getText(view,R.id.checkoutrsv), UserData.loginResponse.CompanyLabel.CheckOut  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //  bundle.putInt("statuss", ReservationStatuss.CheckOut.inte);
                setFilterview(getText(view,R.id.checkoutrsv),bundle,fragment, ReservationStatuss.CheckOut.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.CheckIn.inte){
                setText(getText(view,R.id.checkinrsv), UserData.loginResponse.CompanyLabel.CheckIn  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                // bundle.putInt("statuss", ReservationStatuss.CheckIn.inte);
                setFilterview(getText(view,R.id.checkinrsv),bundle,fragment, ReservationStatuss.CheckIn.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.PendingPayment.inte){
                setText(getText(view,R.id.pendingpmt), "Pending "+ UserData.loginResponse.CompanyLabel.Payment  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                // bundle.putInt("statuss", ReservationStatuss.PendingPayment.inte);
                setFilterview(getText(view,R.id.pendingpmt),bundle,fragment, ReservationStatuss.PendingPayment.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.PaymentOutStanding.inte){
                setText(getText(view,R.id.pmtos), UserData.loginResponse.CompanyLabel.Payment+ " Out Standing"  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.PaymentOutStanding.inte);
                setFilterview(getText(view,R.id.pmtos),bundle,fragment,ReservationStatuss.PaymentOutStanding.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.DepositOutStanding.inte){
                setText(getText(view,R.id.depositos), UserData.loginResponse.CompanyLabel.Deposit+ " Out Standing"  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                // bundle.putInt("statuss", ReservationStatuss.DepositOutStanding.inte);
                setFilterview(getText(view,R.id.depositos),bundle,fragment,ReservationStatuss.DepositOutStanding.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.OverDue.inte){
                setText(getText(view,R.id.overdue), "Over Due"  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.OverDue.inte);
                setFilterview(getText(view,R.id.overdue),bundle,fragment,ReservationStatuss.OverDue.inte);
            }

            if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Closed.inte){
                setText(getText(view,R.id.close), "Closed"  + " ("+reservationStatusDetails[i].Total  +  ")");
                bundle.putBoolean("status",true);
                //bundle.putInt("statuss", ReservationStatuss.Closed.inte);
                setFilterview(getText(view,R.id.close),bundle,fragment,ReservationStatuss.Closed.inte);
            }

                              /*  if (reservationStatusDetails[i].ReservationStatus == ReservationStatuss.Arc.inte){
                                    setText(getText(view,R.id.close), "Closed"  + " ("+reservationStatusDetails[i].Total  +  ")");
                                    bundle.putBoolean("status",true);
                                    bundle.putInt("statuss", ReservationStatuss.Closed.inte);
                                    setFilterview(getText(view,R.id.close),bundle,fragment);
                                }*/

        }
    }


    public void setFilterview(TextView view, Bundle bundle, Fragment fragment,int id){
        view.setOnClickListener(v -> {
            bundle.putInt("statuss", id);
            NavHostFragment.findNavController(fragment).navigate(R.id.same, bundle);
        });
    }

    public void setVisibility(TextView view, Reservation reservation){

        switch (view.getId()){
            case  R.id.checkout:
                Log.e(TAG, "setVisibility: " + reservation.ReservationStatus );
              /*  if (reservation.ReservationStatus == ReservationStatuss.Cancelled.inte &&
                    reservation.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte &&
                    reservation.ReservationStatus == ReservationStatuss.DepositOutStanding.inte &&
                    reservation.ReservationStatus == ReservationStatuss.PaymentOutStanding.inte &&
                    reservation.ReservationStatus == ReservationStatuss.PendingPayment.inte &&
                    reservation.ReservationStatus == ReservationStatuss.Closed.inte
                ) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);

                     reservation.ReservationStatus == ReservationStatuss.Draft.inte &&
                        reservation.ReservationStatus == ReservationStatuss.Rejected.inte &&

                }*/

                if (reservation.ReservationStatus == ReservationStatuss.Cancelled.inte ||
                       // reservation.ReservationStatus == ReservationStatuss.ReadyForCheckOut.inte ||
                        reservation.ReservationStatus == ReservationStatuss.DepositOutStanding.inte ||
                        reservation.ReservationStatus == ReservationStatuss.PaymentOutStanding.inte ||
                        reservation.ReservationStatus == ReservationStatuss.PendingPayment.inte ||
                        reservation.ReservationStatus == ReservationStatuss.Closed.inte ||
                        reservation.ReservationStatus == ReservationStatuss.Confirmed.inte ||
                        reservation.ReservationStatus == ReservationStatuss.Draft.inte  ||
                        reservation.ReservationStatus == ReservationStatuss.NoShow.inte ||
                        reservation.ReservationStatus == ReservationStatuss.OverDue.inte
                ) {
                    view.setVisibility(View.GONE);
                }


                break;
        }

    }

    public void vehiclefilter (RelativeLayout sucessfullRegi, View view, Bundle bundle, Fragment fragment, DoHeader header, CommonParams params){
        getText(view,R.id.cancel2).setOnClickListener(v -> optionVisible(sucessfullRegi,false));
        getText(view,R.id.cancel).setOnClickListener(v -> optionVisible(sucessfullRegi,false));

        ImageView OpenbottomMenu = view.findViewById(R.id.optionmenu);
        OpenbottomMenu.setOnClickListener(v -> optionVisible(sucessfullRegi,true));
        RelativeLayout  vehicletypelist = view.findViewById(R.id.addtional);


        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                try {
                    VehiclecatagorylistBinding vehiclecatagorylistBinding;
                    ReservationVehicleType[] reservationVehicleType;
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    // JSONObject resultSet = responseJSON.getJSONObject("Data");
                    JSONArray data = responseJSON.getJSONArray("Data");
                    reservationVehicleType = loginRes.getModel(data.toString(),ReservationVehicleType[].class);
                    if (reservationVehicleType.length > 0){
                        for (int i = 0; i <reservationVehicleType.length ; i++) {
                            getSubview(i);
                            vehiclecatagorylistBinding = VehiclecatagorylistBinding.inflate(subinflater,
                                    activity.findViewById(android.R.id.content), false);


                            vehiclecatagorylistBinding.getRoot().setId(200 + i);
                            vehiclecatagorylistBinding.getRoot().setLayoutParams(subparams);
                            vehiclecatagorylistBinding.suspend.setText(reservationVehicleType[i].Name +  "(" + reservationVehicleType[i].TotalVehicle + ")");
                            vehiclecatagorylistBinding.suspend.setTextColor(Color.parseColor(UserData.UiColor.secondary));
                            int finalI = i;
                            vehiclecatagorylistBinding.suspend.setOnClickListener(v -> {
                                bundle.putInt("staus",0);
                                bundle.putInt("vehiceltype", reservationVehicleType[finalI].Id);
                                NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
                            });
                            vehicletypelist.addView(vehiclecatagorylistBinding.getRoot());

                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, VEHICLEFILTER, BASE_URL_LOGIN, header, params.getEquipment());

        getText(view,R.id.all).setOnClickListener(v -> {
            bundle.putInt("staus",0);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.available).setOnClickListener(v -> {
            bundle.putInt("staus",1);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.unavailable).setOnClickListener(v -> {
            bundle.putInt("staus",2);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.accident).setOnClickListener(v -> {
            bundle.putInt("staus",3);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.maintenance).setOnClickListener(v -> {
            bundle.putInt("staus",4);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.onrent).setOnClickListener(v -> {
            bundle.putInt("staus",5);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.licexpiry).setOnClickListener(v -> {
            bundle.putInt("staus",6);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.onhold).setOnClickListener(v -> {
            bundle.putInt("staus",7);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.sell).setOnClickListener(v -> {
            bundle.putInt("staus",8);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.suspend).setOnClickListener(v -> {
            bundle.putInt("staus",9);
            bundle.putInt("vehiceltype", 0);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.last10days).setOnClickListener(v -> {
            bundle.putInt("staus",0);
            bundle.putInt("vehiceltype", 0);
            bundle.putBoolean("10day", true);
            bundle.putBoolean("checking", false);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

        getText(view,R.id.underchecking).setOnClickListener(v -> {
            bundle.putInt("staus",0);
            bundle.putInt("vehiceltype", 0);
            bundle.putBoolean("checking", true);
            bundle.putBoolean("10day", false);
            NavHostFragment.findNavController(fragment).navigate(R.id.same,bundle);
        });

    }



    public void uploadImage(ImageView imageView, Bitmap bitmap){
       /* Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = new Canvas();
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        canvas.drawText(currentDateandTime , 10, 25, paint);*/

        Bitmap changeimg = decodeFile(BitMapToString(bitmap));

        imageView.setImageBitmap(changeimg);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap decodeFile(String part_image){
        Bitmap bitmap =null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(part_image,op);
        bitmap = bitmap.copy(op.inPreferredConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawText("17-06-2022",bitmap.getWidth()-200,30,paint);
        // canvas.drawText("Rajkot", 10, bitmap.getWidth()-100, paint);

        Canvas canvas2 = new Canvas(bitmap);
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setTextSize(10);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.RED);
        canvas2.drawText("useraddress", 10, bitmap.getHeight()-30,paint2);


        return bitmap;
    }

    public void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
          //  imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public   File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                +"/Android/data/"
                + activity.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp  +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

    public void imagedtails(ImageView imageView, Bitmap bitmap){
        storeImage(bitmap, "myfile");
        File pictureFile = getOutputMediaFile("myfile");
       // Bitmap image = null;
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmaps = BitmapFactory.decodeFile(pictureFile.getAbsolutePath(),bmOptions);
            bitmaps  = Bitmap.createScaledBitmap(bitmap,imageView.getWidth(),imageView.getHeight(),true);
            storeImage(bitmaps, "myfile");
            File pictureFile2 = getOutputMediaFile("myfile");
            imageView.setImageBitmap(bitmaps);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmaps = BitmapFactory.decodeFile(pictureFile2.getPath(),op);
            bitmaps = bitmaps.copy(op.inPreferredConfig, true);
            Canvas canvas = new Canvas(bitmaps);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawText("17-06-2022",bitmaps.getWidth()-200,50,paint);
            // canvas.drawText("Rajkot", 10, bitmap.getWidth()-100, paint);

            Canvas canvas2 = new Canvas(bitmaps);
            Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setTextSize(40);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.RED);
            canvas2.drawText("useraddress", 10, bitmaps.getHeight()-30,paint2);
            imageView.setImageBitmap(bitmaps);

            //  imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public void storeImage(Bitmap image, String filename) {
        File pictureFile = getOutputMediaFile(filename);
        if (pictureFile == null) {
            Log.e("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            //  imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public   File getOutputMediaFile(String filename){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                +"/Android/data/"
                + activity.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp  +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename);

        return mediaFile;
    }
}
