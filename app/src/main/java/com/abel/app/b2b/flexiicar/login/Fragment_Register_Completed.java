package com.abel.app.b2b.flexiicar.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.home.more.Activity_MoreTab;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.booking.Booking_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.LOGIN;

public class Fragment_Register_Completed extends Fragment
{
    LinearLayout LayoutNotNow,AllowAccess,lblexplore;
    RelativeLayout relativeLayout;
    int count = 0;
    Handler handler=new Handler();
    public String id = "";
    Bundle RegistrationBundle;
    String CustEmail,Password;
    private DoHeader header;
    LoginRes loginRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/20/2021 5:47:18 PM";
        header.islogin = "1";
        loginRes = new LoginRes(getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_completed, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblexplore = view.findViewById(R.id.lblexplore);
        relativeLayout=view.findViewById(R.id.sucessfull_regi);
        AllowAccess=view.findViewById(R.id.AllowAccess);
        LayoutNotNow=view.findViewById(R.id.layoutNotNow);

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        try {
            ImageView logo = view.findViewById(R.id.logo);
            CustomBindingAdapter.loadImage(logo,loginRes.getData(getResources().getString(R.string.logo)));
            TextView textView = view.findViewById(R.id.text);
            textView.setText("Allow "+ getResources().getString(R.string.app_name) +" to Access Your Location to be\\n able to find best car deals in your area.");

        } catch (Exception e){
            e.printStackTrace();
        }

        RegistrationBundle = getArguments().getBundle("RegistrationBundle");

        CustEmail=RegistrationBundle.getString("Cust_Email");
        Password=RegistrationBundle.getString("PasswordHash");

        lblexplore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lblexplore.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });
        LayoutNotNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                relativeLayout.setVisibility(View.GONE);
                lblexplore.setVisibility(View.VISIBLE);
            }
        });
        AllowAccess.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else
                    {
                        JSONObject bodyParam = new JSONObject();
                        try
                        {
                            bodyParam.accumulate("Email",CustEmail);
                            bodyParam.accumulate("Password",Password);
                            bodyParam.accumulate("APIRequestType",2);
                            bodyParam.accumulate("CompanyId", UserData.companyModel.CompanyId);
                            bodyParam.accumulate("IsActive", true);
                            System.out.println(bodyParam);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        loginRes.storedata(getResources().getString(R.string.userType), "1");
                        if (Helper.RegistrationD){
                            Helper.RegistrationD = false;
                            new ApiService(doLogin, RequestType.POST,
                                    LOGIN, BASE_URL_LOGIN, header, bodyParam);
                        } else {
                            Helper.isRegistrationDone = true;
                            Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                            startActivity(i);
                        }
                }
            }
        });
    }
    OnResponseListener doLogin = new OnResponseListener()
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
                            loginRes.storedata("Data", resultSet.toString());
                            loginRes.storedata("Login", resultSet.toString());
                            loginRes.storedata("UserData", resultSet.toString());
                            LoginResponse loginResponse = new LoginResponse();
                            loginResponse = loginRes.callFriend("Data",LoginResponse.class);
                            UserData.loginResponse = loginResponse;
                            //Helper.fueel = UserData.loginResponse.CompanyLabel.Miles;
                            loginRes.storedata(getResources().getString(R.string.userType), "1");
                            loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(loginResponse));
                            /*JSONObject obj = resultSet.getJSONObject("t0040_Customer_Master");
                            System.out.println(obj);

                            id = obj.getString("customer_ID");
                            String cust_FName=obj.getString("cust_FName");
                            String cust_LName=obj.getString("cust_LName");
                            String cust_Street=obj.getString("cust_Street");
                            String cust_UnitNo=obj.getString("cust_UnitNo");
                            String cust_City=obj.getString("cust_City");
                            String cust_State_ID=obj.getString("cust_State_ID");
                            String cust_Country_ID=obj.getString("cust_Country_ID");
                            String cust_ZipCode=obj.getString("cust_ZipCode");
                            String cust_DOB=obj.getString("cust_DOB");
                            String cust_Email=obj.getString("cust_Email");
                            String cust_Phoneno=obj.getString("cust_Phoneno");
                            String cust_MobileNo=obj.getString("cust_MobileNo");
                            String cmp_Name=obj.getString("cmp_Name");
                            String cmp_Street=obj.getString("cmp_Street");
                            String cmp_UnitNo=obj.getString("cmp_UnitNo");
                            String cmp_City=obj.getString("cmp_City");
                            int cmp_State_ID=obj.getInt("cmp_State_ID");
                            String cmp_Country_ID=obj.getString("cmp_Country_ID");
                            String cmp_ZipCode=obj.getString("cmp_ZipCode");
                            String cmp_Phoneno=obj.getString("cmp_Phoneno");
                            String licence_No=obj.getString("licence_No");
                            String lIssue_Date=obj.getString("lIssue_Date");
                            String lExpiry_Date=obj.getString("lExpiry_Date");
                            String lIssue_By=obj.getString("lIssue_By");
                            String passport_No=obj.getString("passport_No");
                            String pIssue_Date=obj.getString("pIssue_Date");
                            String pExpiry_Date=obj.getString("pExpiry_Date");
                            String pIssue_By=obj.getString("pIssue_By");
                            String cmp_ID=obj.getString("cmp_ID");
                            String created_By=obj.getString("created_By");
                            String created_Date=obj.getString("created_Date");
                            String cust_Gender=obj.getString("cust_Gender");
                            String cust_Country_Name=obj.getString("cust_Country_Name");
                            String cust_State_Name=obj.getString("cust_State_Name");
                            String cmp_Country_Name=obj.getString("cmp_Country_Name");
                            String cmp_State_Name=obj.getString("cmp_State_Name");*/

                            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sp.edit();

                            editor.putString("default_Email",CustEmail);
                            editor.putString("default_Password",Password);
/*
                            editor.putString(getString(R.string.id), id);
                            editor.putString("cust_FName" ,cust_FName);
                            editor.putString("cust_LName" ,cust_LName);
                            editor.putString("cust_Street" ,cust_Street);
                            editor.putString("cust_UnitNo" ,cust_UnitNo);
                            editor.putString("cust_City" ,cust_City);
                            editor.putString("cust_State_ID" ,cust_State_ID);
                            editor.putString("cust_Country_ID" ,cust_Country_ID);
                            editor.putString("cust_ZipCode" ,cust_ZipCode);
                            editor.putString("cust_DOB" ,cust_DOB);
                            editor.putString("cust_Email" ,cust_Email);
                            editor.putString("cust_Phoneno" ,cust_Phoneno);
                            editor.putString("cust_MobileNo" ,cust_MobileNo);
                            editor.putString("cmp_Name" ,cmp_Name);
                            editor.putString("cmp_Street" ,cmp_Street);
                            editor.putString("cmp_UnitNo" ,cmp_UnitNo);
                            editor.putString("cmp_City" ,cmp_City);
                            editor.putInt("cmp_State_ID" ,cmp_State_ID);
                            editor.putString("cmp_Country_ID" ,cmp_Country_ID);
                            editor.putString("cmp_ZipCode" ,cmp_ZipCode);
                            editor.putString("cmp_Phoneno" ,cmp_Phoneno);
                            editor.putString("licence_No" ,licence_No);
                            editor.putString("lIssue_Date" ,lIssue_Date);
                            editor.putString("lIssue_By" ,lIssue_By);
                            editor.putString("passport_No" ,passport_No);
                            editor.putString("pIssue_Date" ,pIssue_Date);
                            editor.putString("pExpiry_Date" ,pExpiry_Date);
                            editor.putString("pIssue_By" ,pIssue_By);
                            editor.putString("cmp_ID" ,cmp_ID);
                            editor.putString("lExpiry_Date" ,lExpiry_Date);
                            editor.putString("created_By" ,created_By);
                            editor.putString("created_Date" ,created_Date);
                            editor.putString("cust_Gender" ,cust_Gender);
                            editor.putString("cust_Country_Name" ,cust_Country_Name);
                            editor.putString("cust_State_Name" ,cust_State_Name);
                            editor.putString("cmp_Country_Name" ,cmp_Country_Name);
                            editor.putString("cmp_State_Name" ,cmp_State_Name);*/

                            editor.apply();

                            String msg = "Permission Granted.";
                            CustomToast.showToast(getActivity(),msg,0);

                            //Intent i = new Intent(getActivity(), Booking_Activity.class);
                            Intent i = new Intent(getActivity(), Customer_Booking_Activity.class);
                           // i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
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
        public void onError(String error)
        {
            System.out.println("Error"+error);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    CustomToast.showToast(getActivity(),"Permission Granted",0);
                }
                else
                {
                    CustomToast.showToast(getActivity(),"Permission Denied",1);
                }
            }
            break;
        }
    }
}

 /*if (count == 0)
          {
          lblexplore.setVisibility(View.INVISIBLE);
          relativeLayout.setVisibility(View.VISIBLE);
          count++;
          }
          else
          {
          Intent i = new Intent(getActivity(), Booking_Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        count = 0;
        }*/