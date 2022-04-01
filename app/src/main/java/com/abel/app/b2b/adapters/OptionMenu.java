package com.abel.app.b2b.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.abel.app.b2b.R;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.flexiicar.user.Fragment_Customer_Profile;
import com.abel.app.b2b.flexiicar.user.Fragment_Summary_Of_Charges_For_Agreements;
import com.abel.app.b2b.home.reservation.Activity_Reservation;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.CommonParams;
import com.abel.app.b2b.model.parameter.ReservationStatus;
import com.abel.app.b2b.model.parameter.ReservationStatuss;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.Reservation;
import com.abel.app.b2b.model.response.UpdateDL;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.DELETE;
import static com.abel.app.b2b.apicall.ApiEndPoint.EMAIL;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETCUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.secondImage;


public class OptionMenu {

    public Activity activity;
    public RelativeLayout.LayoutParams subparams;
    public Fragment fragments;
    public LayoutInflater subinflater;
    public Handler handler = new Handler(Looper.getMainLooper());
    public LoginRes loginRes;
    public String TAG = "OptionMenu";
    public OptionMenu(Activity activity) {
        this.activity = activity;
        loginRes = new LoginRes(activity);
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

        setText(getText(view,R.id.paymentprocess), UserData.loginResponse.CompanyLabel.Payment + " " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.deleteagreement),  "Delete " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.editAgreement),  "Edit " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.printAgreement),  "Print " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.email_agreement),  "Email " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.changevehicle),  "Change " +UserData.loginResponse.CompanyLabel.Vehicle);
        setText(getText(view,R.id.extendagreement),  "Extend " +UserData.loginResponse.CompanyLabel.Agreement);
        setText(getText(view,R.id.tollcharge),  "Toll " +UserData.loginResponse.CompanyLabel.Charge);
        setText(getText(view,R.id.cancelAgreement),  "Cancel " +UserData.loginResponse.CompanyLabel.Agreement);
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
}
