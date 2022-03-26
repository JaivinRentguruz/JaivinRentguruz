package com.abel.app.b2b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.model.display.MobileBasicDetails;
import com.abel.app.b2b.model.parameter.CommonParams;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.flexiicar.login.Login;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.model.base.BaseModel;
import com.abel.app.b2b.model.base.ResponseBase;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.companyModel;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.parameter.CheckList;
import com.abel.app.b2b.model.parameter.ReservationStatus;
import com.abel.app.b2b.model.parameter.UserTypes;
import com.abel.app.b2b.model.response.CompanyModelTest;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeData;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.flexiicar.login.OnBoarding;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.booking.Booking_Activity;
import com.abel.app.b2b.model.response.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.INITIATION;
import static com.abel.app.b2b.apicall.ApiEndPoint.LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.SPLASH;

public class SplashScreen extends AppCompatActivity
{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    public static Context context;
    public String id="";
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    Handler handler=new Handler(Looper.getMainLooper());
    public DoHeader header;
    LoginRes loginRes;
    ResponseBase<CompanyModelTest> data;
    CustomeData customeData;
  //  CompanyModelTest companyModel;
    companyModel cm;
    String username, password;
    private CustomPreference preference;
    public CommonParams params;
    String  path;
    ImageView icon;
    public static com.abel.app.b2b.model.display.SplashScreen splashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_splash_screen);

       // Log.e("TAG", "onCreate: " + ReservationStatus.valueOf("1"));
      //  Log.e("TAG", "onCreate: " + ReservationStatus.fromString("1"));

/*
        SharedPreferences preferences = getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        preferences.edit().clear().apply();


        SharedPreferences preferences2 = getSharedPreferences("NexPort", MODE_PRIVATE);
        preferences2.edit().clear().apply();
*/
        Log.d("TAG", "onCreate: " + CheckList.Agreement);
        splashScreen = new com.abel.app.b2b.model.display.SplashScreen();
        preference = new CustomPreference(this);
        SharedPreferences sp = getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        password = sp.getString("default_Password","");
        username = sp.getString("default_Email","");

        JSONObject bodyParam = new JSONObject();

        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        SplashScreen.context = getApplicationContext();
        loginRes = new LoginRes(this);
       // companyModel = new CompanyModelTest();
        cm = new companyModel();
        data = new ResponseBase<>();
        icon = findViewById(R.id.logo);
        CustomBindingAdapter.loadImage(icon,loginRes.getData(getResources().getString(R.string.logo)));
  /*      Log.d("Mungara", "onCreate: " + com.abel.app.b2b.model.parameter.TableType.Addresses.anInt);
        Log.d("Mungara", "onCreate: " + com.abel.app.b2b.model.parameter.TableType.TimelineDescriptionTypes.Created.inte);*/

        //wihout authication work
       /* Intent i = new Intent(SplashScreen.this, OnBoarding.class);
        startActivity(i);*/
       // ApiService ApiService = new ApiService(appInitialize, RequestType.GET, APP_INTIALIZATION, BASE_URL_LOGIN,  new HashMap<String, String>(), bodyParam);
        //Loading = ProgressDialog.show(getApplicationContext(), "Welcome to FlexiiCar", "Please Wait...", true, true);






        header = new DoHeader();
     /*   header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";*/

        path = null;
        try {
            //String ecars ="RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
           //regular  bodyParam.accumulate("companyKey", "RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=");
           //ecars
            //String ecars = "b3NPYzlBZVVxSGs1VWtrV3RMbmFUVGFCaTdDWVYraUIyVFgzWG13dG1oL3FBeWZtZkc1WHdnPT0=";
            String ecars = "V0dZR3JzMVlVSkx4dlJ1bU5Ld3BFYms2dE1IZk1lSzEzWXl2WjZKODVyWHY4UW10SnBmdk1BPT0=";
            bodyParam.accumulate("companyKey", "b3NPYzlBZVVxSGs1VWtrV3RMbmFUVGFCaTdDWVYraUIyVFgzWG13dG1oL3FBeWZtZkc1WHdnPT0=");
            bodyParam.accumulate("DataVersion", "1");
           // path = "?companyKey=" +"RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
            path = "?companyKey=" + ecars;
        } catch (JSONException e) {
             e.printStackTrace();
        }
      //  UpdateApp();

      /*  handler.postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        },1000 );*/


        ApiService apiService = new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);

       /* if (loginRes.getData("MUT").isEmpty()) {
            ApiService apiService = new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);
        } else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(id.equals(""))
                    {
                        Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
            },AUTO_HIDE_DELAY_MILLIS );
        }*/

    }

    OnResponseListener appInitialize = new OnResponseListener()
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
                        loginRes.storedata("Basic", response);
//                        data = loginRes.callFriend("Basic", ResponseBase.class);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            Log.d("Mungara", "run: " + headers.toString());
                            Log.d("Mungara", "run: " + headers.get("mut"));
                            Log.d("Mungara", "run: " + headers.get("ut"));
                            Log.d("Mungara", "run: " + headers.get("token"));

                            loginRes.storedata("DataVersion", responseJSON.getString("DataVersion"));

                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            JSONObject object = resultSet.getJSONObject("companyModel");
                            loginRes.storedata("MUT",object.getString("MUT"));
                            header.mut = object.getString("MUT");



                            cm = loginRes.getModel(object.toString(),companyModel.class);
                            UserData.companyModel = cm;
                            Log.d("TAG", "run: "+String.valueOf(cm.id)  +  "  " + cm.Id  );
                            //loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(cm));
                            loginRes.storedata("CompanyId", String.valueOf(cm.Id));
                            //BaseModel.CompanyId = cm.Id;
                            Helper.di = cm.Id;
                            Helper.currencySymbol = cm.CurrencySymbol;
                            Helper.currencyName = cm.DisplayCurrency;
                            Helper.displaycurrency = cm.DisplayCurrency;
                            Helper.fueel = cm.DistanceDesc;
                            Helper.fuel = cm.Distance;
                            Helper.fueltype = cm.DistanceDesc;
                            Helper.dateformat = cm.DefaultDateFormat;
                            params = new CommonParams();
                            header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
                            //header.ut =  "PYOtYmuTsLQ=";
                            header.ut =  "PYOtYmuTsLQ=";
                            header.exptime = "7/20/2021 5:47:18 PM";
                            header.islogin = "1";
                            new ApiService(new OnResponseListener() {
                                @Override
                                public void onSuccess(String response, HashMap<String, String> headers) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Log.e("TAG", "onSuccess: "+  response);
                                                MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");

                                                if (status)
                                                {
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                    mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);
                                                    ImageView icon = findViewById(R.id.logo);
                                                    CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
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
                            }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.MobileBasicDetails)));


                            try {
                                UserData.companyModel = cm;
                                for (int i = 0; i < UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.size(); i++) {
                                    if (UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.get(i).Label.matches("MinDays")){
                                        UserData.companyModel.MinReservationDays = UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.get(i).Value;
                                    }
                                }

                                for (int i = 0; i <UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.size() ; i++) {
                                    if (UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.get(i).Label.matches("MinAge")){
                                        UserData.companyModel.MinDOBAge = UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.get(i).Value;
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(cm));
                            if(id.equals(""))
                            {

                                new ApiService(getSplashScreen, RequestType.POST,SPLASH,BASE_URL_LOGIN,header,params.getBasicDetail(getResources().getString(R.string.SplashScreens)));

                            }
                            else
                            {
                                JSONObject bodyParam = new JSONObject();
                                try {
                                    bodyParam.accumulate("Email", username);
                                    bodyParam.accumulate("Password", password);
                                    bodyParam.accumulate("APIRequestType",2);
                                    bodyParam.accumulate("CompanyId", cm.Id);
                                    bodyParam.accumulate("IsActive", true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // customResponse.UserLogin(getContext(),editText_Username.getText().toString(), editText_Password.getText().toString());

                                new ApiService(doLogin, RequestType.POST,
                                        LOGIN, BASE_URL_LOGIN, header, bodyParam);
                            }


                                /*handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(id.equals(""))
                                        {
                                            Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                                            startActivity(i);
                                        }
                                        else
                                        {
                                            JSONObject bodyParam = new JSONObject();
                                            try {
                                                bodyParam.accumulate("Email", username);
                                                bodyParam.accumulate("Password", password);
                                                bodyParam.accumulate("APIRequestType",2);
                                                bodyParam.accumulate("CompanyId", UserData.companyModel.CompanyId);
                                                bodyParam.accumulate("IsActive", true);


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            // customResponse.UserLogin(getContext(),editText_Username.getText().toString(), editText_Password.getText().toString());

                                           new ApiService(doLogin, RequestType.POST,
                                                    LOGIN, BASE_URL_LOGIN, header, bodyParam);
                                        }



                                       *//* Intent i = new Intent(SplashScreen.this, Login.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);*//*
                                    }
                                },AUTO_HIDE_DELAY_MILLIS );*/

                           /* handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(id.equals(""))
                                    {
                                        Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            },AUTO_HIDE_DELAY_MILLIS );*/


                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(SplashScreen.this,errorString,1);
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
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
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

                        Gson gson = new Gson();

                        if (status)
                        {

                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            preference.storeCountryState("Login", resultSet.toString());
                            LoginResponse userDetail = gson.fromJson(String.valueOf(resultSet), LoginResponse.class);
                            Log.d("Mungara", "run: " + userDetail.apiUserLogin.UserId);
                            UserData.loginResponse = userDetail;
                            //Helper.fueel = UserData.loginResponse.CompanyLabel.Miles;
                            preference.storeCountryState("Data", resultSet.toString());
                            preference.storeCountryState("UserData", resultSet.toString());

                            Log.d("Mungara", "run: " + headers.toString());
                            Log.d("Mungara", "run: " + headers.get("mut"));
                            Log.d("Mungara", "run: " + headers.get("ut"));
                            Log.d("Mungara", "run: " + headers.get("token"));
                            loginRes.storedata("UT",headers.get("ut"));
                            loginRes.storedata("TOKEN",headers.get("token"));

                            preference.storeCountryState("Login", resultSet.toString());

                            preference.storeCountryState("Data", resultSet.toString());
                            preference.storeCountryState("CompanyId", String.valueOf(userDetail.User.CompanyId));
                            //Integer.parseInt(loginRes.getData("CompanyId");

                            SharedPreferences sp = getSharedPreferences("FlexiiCar",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sp.edit();
                            editor.putString(getString(R.string.id), String.valueOf(UserData.loginResponse.User.UserFor));
                            editor.putString("Data",resultSet.toString());

                            if (UserData.loginResponse.User.UserType == UserTypes.Customer.inte){
                                Helper.isActiveCustomer = true;
                                loginRes.storedata(getResources().getString(R.string.userType), "1");
                                Intent i = new Intent(SplashScreen.this, Customer_Booking_Activity.class);
                                //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }  else {
                                loginRes.storedata(getResources().getString(R.string.userType), "2");
                                Intent i = new Intent(SplashScreen.this, Activity_Home.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }

                          /*  handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(id.equals(""))
                                    {
                                        Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            },AUTO_HIDE_DELAY_MILLIS );*/


                        /*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*/

                           /* Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                            //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);*/


                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(SplashScreen.this,msg,1);

                            Intent intent = new Intent(SplashScreen.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

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
            Intent intent = new Intent(SplashScreen.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    OnResponseListener getBasicDetail = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("TAG", "onSuccess: "+  response);
                        MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);

                            CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                            loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                            loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);

                       /*     handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (UserData.loginResponse.User.UserType == UserTypes.Customer.inte){
                                        Helper.isActiveCustomer = true;
                                        loginRes.storedata(getResources().getString(R.string.userType), "1");
                                        Intent i = new Intent(SplashScreen.this, Customer_Booking_Activity.class);
                                        //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }  else {
                                        loginRes.storedata(getResources().getString(R.string.userType), "2");
                                        Intent i = new Intent(SplashScreen.this, Activity_Home.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }
                                }
                            },AUTO_HIDE_DELAY_MILLIS);*/



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
    };

    OnResponseListener getSplashScreen = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("TAG", "onSuccess: "+  response);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            splashScreen =    loginRes.getModel(resultSet.toString(), com.abel.app.b2b.model.display.SplashScreen.class);
                            Intent i = new Intent(SplashScreen.this, OnBoarding.class);
                            startActivity(i);
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
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void UpdateApp(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                requestUpdate(result);
                android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this,R.style.AppTheme);
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
                alertDialogBuilder.setTitle("E-Cars");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setIcon(R.drawable.small_logo);
                alertDialogBuilder.setMessage("E-Cars recommends that you update to the latest version for a seamless & enhanced performance of the app.");
                alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No Thanks",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialogBuilder.show();

            } else {
                new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);
            }
        });
    }
}



