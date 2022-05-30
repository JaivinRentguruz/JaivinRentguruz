package com.rentguruz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.flexiicar.booking.Booking_Activity;
import com.rentguruz.app.flexiicar.user.Fragment_Customer_Profile;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.LoginModel;
import com.rentguruz.app.model.response.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.LOGIN;

public class CustomResponse {

    public Fragment fragment;
    Handler handler=new Handler(Looper.getMainLooper());
    OnResponseListener doLogin;
    public static Context context;
    ApiService ApiService;
    public CustomResponse(Fragment fragment) {
        this.fragment = fragment;
    }

    public void UserLogin(Context contexts, String username, String password){
        LoginRes loginRes = new LoginRes(contexts);
        DoHeader header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut =  "PYOtYmuTsLQ=";
        header.exptime = "7/20/2021 5:47:18 PM";
        header.islogin = "1";
        header.mut = loginRes.getData("MUT");
        context = contexts;
        JSONObject bodyParam = new JSONObject();
        try
        {
            bodyParam.accumulate("Email", username);
            bodyParam.accumulate("Password", password);
            bodyParam.accumulate("APIRequestType",2);
            //     bodyParam.accumulate("CompanyId", 1);
            System.out.println(bodyParam);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(contexts);

        ApiService = new ApiService(new OnResponseListener() {
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

                                JSONObject resultSet = responseJSON.getJSONObject("Data");



                        /*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*/

                                Intent i = new Intent(context, Booking_Activity.class);
                                //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);

                            }
                            else
                            {
                                String msg = responseJSON.getString("Message");
                                CustomToast.showToast((Activity) context ,msg,1);

                            }
                            // Loading.hide();
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
                System.out.println("Error"+error);
            }
        }, RequestType.POST,
                LOGIN, BASE_URL_LOGIN, header, bodyParam);

      /*  doLogin = new OnResponseListener()
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

                                JSONObject resultSet = responseJSON.getJSONObject("Data");



                        *//*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*//*

                                Intent i = new Intent(context, Booking_Activity.class);
                                //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);

                            }
                            else
                            {
                                String msg = responseJSON.getString("Message");
                                CustomToast.showToast((Activity) context ,msg,1);

                            }
                            // Loading.hide();
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
                System.out.println("Error"+error);
            }
        };*/



    }

    public void getLogin(){
        doLogin = new OnResponseListener()
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

                                JSONObject resultSet = responseJSON.getJSONObject("Data");



                        /*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*/

                                Intent i = new Intent(context, Booking_Activity.class);
                                //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);

                            }
                            else
                            {
                                String msg = responseJSON.getString("Message");
                                CustomToast.showToast((Activity) context ,msg,1);

                            }
                            // Loading.hide();
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
                System.out.println("Error"+error);
            }
        };
    }

    public OnResponseListener UserLogin(Context contexts,String username, String password, OnResponseListener responseListener ){
        LoginRes loginRes = new LoginRes(contexts);
        DoHeader header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut =  "PYOtYmuTsLQ=";
        header.exptime = "7/20/2021 5:47:18 PM";
        header.islogin = "1";
        header.mut = loginRes.getData("MUT");
        context = contexts;
        JSONObject bodyParam = new JSONObject();
        try
        {
            bodyParam.accumulate("Email", username);
            bodyParam.accumulate("Password", password);
            bodyParam.accumulate("APIRequestType",2);
            //     bodyParam.accumulate("CompanyId", 1);
            System.out.println(bodyParam);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(contexts);

        ApiService = new ApiService(responseListener, RequestType.POST,
                LOGIN, BASE_URL_LOGIN, header, bodyParam);

        return responseListener;
    }
}
