package com.rentguruz.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.model.display.Appcolor;
import com.rentguruz.app.model.display.HomeScreen;
import com.rentguruz.app.model.display.MobileBasicDetails;
import com.rentguruz.app.model.parameter.CommonParams;
import com.rentguruz.app.model.parameter.VehiclePurchasedBy;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.flexiicar.booking2.Customer_Booking_Activity;
import com.rentguruz.app.flexiicar.login.Login;
import com.rentguruz.app.home.Activity_Home;
import com.rentguruz.app.model.base.ResponseBase;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.companyModel;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.parameter.CheckList;
import com.rentguruz.app.model.parameter.UserTypes;
import com.rentguruz.app.model.response.CompanyModelTest;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeData;
import com.rentguruz.app.adapters.LoginRes;
import com.rentguruz.app.flexiicar.login.OnBoarding;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.model.response.LoginResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.INITIATION;
import static com.rentguruz.app.apicall.ApiEndPoint.LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SPLASH;

/*
public class SplashScreen extends AppCompatActivity
{
    */
/**
 * Whether or not the system UI should be auto-hidden after
 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
 *//*

    private static final boolean AUTO_HIDE = true;

    */
/**
 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
 * user interaction before hiding the system UI.
 *//*

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    public static Context context;
    public String id="";
    */

/**
 * Some older devices needs a small delay between UI widget updates
 * and a change of the status and navigation bar.
 *//*

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
    ImageView icon,image;
    public static com.rentguruz.app.model.display.SplashScreen splashScreen;


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

        //Log.e("TAG", "onCreate: " + ReservationStatus.valueOf("1"));
        //Log.e("TAG", "onCreate: " + ReservationStatus.fromString("1"));

*/
/*
        SharedPreferences preferences = getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        preferences.edit().clear().apply();


        SharedPreferences preferences2 = getSharedPreferences("NexPort", MODE_PRIVATE);
        preferences2.edit().clear().apply();
*//*

        params = new CommonParams();
        Log.d("TAG", "onCreate: " + CheckList.Agreement.inte + " " + CheckList.Agreement.toString());
        Log.d("TAG", "onCreate: " + VehiclePurchasedBy.Cash.inte);
        splashScreen = new com.rentguruz.app.model.display.SplashScreen();
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
        image = findViewById(R.id.splash);
        CustomBindingAdapter.loadImage(image,loginRes.getData(getResources().getString(R.string.cmshome)));
  */
/*      Log.d("Mungara", "onCreate: " + com.rentguruz.app.model.parameter.TableType.Addresses.anInt);
        Log.d("Mungara", "onCreate: " + com.rentguruz.app.model.parameter.TableType.TimelineDescriptionTypes.Created.inte);*//*


        //wihout authication work
       */
/* Intent i = new Intent(SplashScreen.this, OnBoarding.class);
        startActivity(i);*//*

       // ApiService ApiService = new ApiService(appInitialize, RequestType.GET, APP_INTIALIZATION, BASE_URL_LOGIN,  new HashMap<String, String>(), bodyParam);
        //Loading = ProgressDialog.show(getApplicationContext(), "Welcome to FlexiiCar", "Please Wait...", true, true);






        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        header.mut = "aE/De0gJDss=";

        path = null;
        try {
            //String ecars ="RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
           //regular  bodyParam.accumulate("companyKey", "RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=");
           //ecars
            String ecars = "b3NPYzlBZVVxSGs1VWtrV3RMbmFUVGFCaTdDWVYraUIyVFgzWG13dG1oL3FBeWZtZkc1WHdnPT0=";
        // abel
          //  String ecars = "V0dZR3JzMVlVSkx4dlJ1bU5Ld3BFYms2dE1IZk1lSzEzWXl2WjZKODVyWHY4UW10SnBmdk1BPT0=";
            bodyParam.accumulate("companyKey", "b3NPYzlBZVVxSGs1VWtrV3RMbmFUVGFCaTdDWVYraUIyVFgzWG13dG1oL3FBeWZtZkc1WHdnPT0=");
            bodyParam.accumulate("DataVersion", "1");
           // path = "?companyKey=" +"RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
            path = "?companyKey=" + ecars;
        } catch (JSONException e) {
             e.printStackTrace();
        }
      //  UpdateApp();

      */
/*  handler.postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        },1000 );*//*





       // ApiService apiService = new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);

        Helper.di = 2;

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //   Log.e("TAG", "onSuccess: "+  response);
                            MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");

                            if (status)
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                // Log.e("TAG", "run: " + resultSet.toString());
                                mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);
                                ImageView icon = findViewById(R.id.logo);
                                CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                                JSONObject object = new JSONObject(mobileBasicDetails.DetailJson);
                                Appcolor appcolor = new Appcolor();
                                appcolor = loginRes.getModel(object.toString(),Appcolor.class);
                                UserData.UiColor.primary = appcolor.PrimaryColor;
                                UserData.UiColor.secondary = appcolor.SecondColor;
                                UserData.UiColor.primaryfont = appcolor.ThirdColor;
                                UserData.UiColor.secondaryfont = appcolor.SecondColor;
                                UserData.UiColor.additionalcolor = appcolor.AdditionalColor;
                                Log.e("TAG", "run: " + appcolor.AdditionalColor );
                                String colordata =  loginRes.modeltostring(appcolor);
                                loginRes.storedata(getResources().getString(R.string.Appcolor), colordata);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            loginRes.storedata(getResources().getString(R.string.logo), "0");
                            loginRes.storedata(getResources().getString(R.string.icon), "0");
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.MobileBasicDetails),2));

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //  Log.e("TAG", "onSuccess: "+  response);
                            //  Log.e("TAG", "onSuccess: "+  response);
                            HomeScreen homeScreen = new HomeScreen();
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            //ImageView image = findViewById(R.id.splash);
                            if (status){
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                homeScreen =    loginRes.getModel(resultSet.toString(), HomeScreen.class);
                                //ImageView image =
                                //loginRes.storedata(getResources().getString(R.string.splashimage), homeScreen.AttachmentsModels.get(0).AttachmentPath);
                                //CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(0).AttachmentPath);

                                for (int i = 0; i <homeScreen.AttachmentsModels.size() ; i++) {

                                    if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmshome))){
                                        loginRes.storedata(getResources().getString(R.string.cmshome), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                        CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                        // Log.e("TAG", "run: " + i + "  " +  homeScreen.AttachmentsModels.get(i).AttachmentPath );
                                    }

                                    if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmsdash))){
                                        loginRes.storedata(getResources().getString(R.string.cmsdash), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                    }
                                }

                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            //  loginRes.storedata(getResources().getString(R.string.splashimage), "0");
                            loginRes.storedata(getResources().getString(R.string.cmshome),"0");
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.HomePageScreen), 2));

        new ApiService(getSplashScreen, RequestType.POST,SPLASH,BASE_URL_LOGIN,header,params.getBasicDetail(getResources().getString(R.string.SplashScreens),2));

       */
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
        }*//*


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    //Permission granted

                } else {
                    //permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        //show permission snackbar
                    }
                }

            });

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
                            //JSONObject object = resultSet.getJSONObject("companyModel");
                            loginRes.storedata("MUT",resultSet.getString("MUT"));
                            header.mut = resultSet.getString("MUT");



                          //  cm = loginRes.getModel(object.toString(),companyModel.class);
                            cm = loginRes.getModel(resultSet.toString(),companyModel.class);
                            UserData.companyModel = cm;
                           // Log.d("TAG", "run: "+String.valueOf(cm.id)  +  "  " + cm.Id  );
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
                                             //   Log.e("TAG", "onSuccess: "+  response);
                                                MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");

                                                if (status)
                                                {
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                   // Log.e("TAG", "run: " + resultSet.toString());
                                                    mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);
                                                    ImageView icon = findViewById(R.id.logo);
                                                    CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                                                    JSONObject object = new JSONObject(mobileBasicDetails.DetailJson);
                                                    Appcolor appcolor = new Appcolor();
                                                    appcolor = loginRes.getModel(object.toString(),Appcolor.class);
                                                    UserData.UiColor.primary = appcolor.PrimaryColor;
                                                    UserData.UiColor.secondary = appcolor.SecondColor;
                                                    UserData.UiColor.primaryfont = appcolor.ThirdColor;
                                                    UserData.UiColor.secondaryfont = appcolor.SecondColor;
                                                    UserData.UiColor.additionalcolor = appcolor.AdditionalColor;
                                                    Log.e("TAG", "run: " + appcolor.AdditionalColor );
                                                    String colordata =  loginRes.modeltostring(appcolor);
                                                    loginRes.storedata(getResources().getString(R.string.Appcolor), colordata);
                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                                loginRes.storedata(getResources().getString(R.string.logo), "0");
                                                loginRes.storedata(getResources().getString(R.string.icon), "0");
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


                                */
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



                                       *//*
 */
/* Intent i = new Intent(SplashScreen.this, Login.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);*//*
 */
/*
                                    }
                                },AUTO_HIDE_DELAY_MILLIS );*//*


 */
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
                            },AUTO_HIDE_DELAY_MILLIS );*//*



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

                          */
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
                            },AUTO_HIDE_DELAY_MILLIS );*//*



 */
/*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*//*


 */
/* Intent i = new Intent(SplashScreen.this, Booking_Activity.class);
                            //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);*//*



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
                      //  Log.e("TAG", "onSuccess: "+  response);
                        MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {

                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);

                                CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                            } catch (Exception e){
                                e.printStackTrace();
                                CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                            }



                       */
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
                            },AUTO_HIDE_DELAY_MILLIS);*//*




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
                      //  Log.e("TAG", "onSuccess: "+  response);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                splashScreen =    loginRes.getModel(resultSet.toString(), com.rentguruz.app.model.display.SplashScreen.class);

                            } catch (Exception e){
                                e.printStackTrace();
                            }
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
              //  new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);
            }
        });
    }

    private void changeicon() {

        // enable old icon
        PackageManager manager=getPackageManager();
        manager.setComponentEnabledSetting(new ComponentName(SplashScreen.this,"com.rentguruz.app.SplashScreen")
                ,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

        // disable new icon
        manager.setComponentEnabledSetting(new ComponentName(SplashScreen.this,"com.rentguruz.app.SplashScreen")
                ,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        //Toast.makeText(MainActivity.this,"Enable Old Icon" ,Toast.LENGTH_LONG).show();

    }
}



*/

public class SplashScreen extends AppCompatActivity {
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
    public String id = "";
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    Handler handler = new Handler(Looper.getMainLooper());
    public DoHeader header;
    LoginRes loginRes;
    ResponseBase<CompanyModelTest> data;
    CustomeData customeData;
    //  CompanyModelTest companyModel;
    companyModel cm;
    String username, password;
    private CustomPreference preference;
    public CommonParams params;
    String path;
    ImageView icon, image;
    public static com.rentguruz.app.model.display.SplashScreen splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_splash_screen);

        //Log.e("TAG", "onCreate: " + ReservationStatus.valueOf("1"));
        //Log.e("TAG", "onCreate: " + ReservationStatus.fromString("1"));

/*
        SharedPreferences preferences = getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        preferences.edit().clear().apply();


        SharedPreferences preferences2 = getSharedPreferences("NexPort", MODE_PRIVATE);
        preferences2.edit().clear().apply();
*/
        Log.d("TAG", "onCreate: " + CheckList.Agreement.inte + " " + CheckList.Agreement.toString());
        Log.d("TAG", "onCreate: " + VehiclePurchasedBy.Cash.inte);
        splashScreen = new com.rentguruz.app.model.display.SplashScreen();
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
        CustomBindingAdapter.loadImage(icon,loginRes.getData(getResources().getString(R.string.icon)));
        image = findViewById(R.id.splash);
        CustomBindingAdapter.loadImage(image,loginRes.getData(getResources().getString(R.string.cmshome)));
  /*      Log.d("Mungara", "onCreate: " + com.rentguruz.app.model.parameter.TableType.Addresses.anInt);
        Log.d("Mungara", "onCreate: " + com.rentguruz.app.model.parameter.TableType.TimelineDescriptionTypes.Created.inte);*/

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
            // abel
            //  String ecars = "V0dZR3JzMVlVSkx4dlJ1bU5Ld3BFYms2dE1IZk1lSzEzWXl2WjZKODVyWHY4UW10SnBmdk1BPT0=";
            //masterlogin
            //String ecars = "RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
            //super login
            String ecars = "Z3dBbmpLTlp1TEkvVTJuTGVUbVV6dUd5SWZRakNzd015WTI2UmJoUUdka1d1eVNYd1JreHFnPT0=";
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    //Permission granted

                } else {
                    //permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        //show permission snackbar
                    }
                }

            });

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
                            //JSONObject object = resultSet.getJSONObject("companyModel");
                            loginRes.storedata("MUT",resultSet.getString("MUT"));
                            header.mut = resultSet.getString("MUT");



                            //  cm = loginRes.getModel(object.toString(),companyModel.class);
                            cm = loginRes.getModel(resultSet.toString(),companyModel.class);
                            UserData.companyModel = cm;
                            // Log.d("TAG", "run: "+String.valueOf(cm.id)  +  "  " + cm.Id  );
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
                           // header.ut =  "PYOtYmuTsLQ=";
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
                                                //  Log.e("TAG", "onSuccess: "+  response);
                                                //  Log.e("TAG", "onSuccess: "+  response);
                                                HomeScreen homeScreen = new HomeScreen();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");
                                                //ImageView image = findViewById(R.id.splash);
                                                if (status){
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                    homeScreen =    loginRes.getModel(resultSet.toString(), HomeScreen.class);
                                                    //ImageView image =
                                                    //loginRes.storedata(getResources().getString(R.string.splashimage), homeScreen.AttachmentsModels.get(0).AttachmentPath);
                                                    //CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(0).AttachmentPath);

                                                    for (int i = 0; i <homeScreen.AttachmentsModels.size() ; i++) {

                                                        if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmshome))){
                                                            Log.e("TAG", "run:1 " +  homeScreen.AttachmentsModels.get(i).AttachmentPath );
                                                            loginRes.storedata(getResources().getString(R.string.cmshome), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                            CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                            // Log.e("TAG", "run: " + i + "  " +  homeScreen.AttachmentsModels.get(i).AttachmentPath );
                                                        }

                                                        if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmsdash))){
                                                            Log.e("TAG", "run:2 " +  homeScreen.AttachmentsModels.get(i).AttachmentPath );
                                                            loginRes.storedata(getResources().getString(R.string.cmsdash), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                        }
                                                    }

                                                }

                                            } catch (Exception e){
                                                e.printStackTrace();
                                                //  loginRes.storedata(getResources().getString(R.string.splashimage), "0");
                                                //loginRes.storedata(getResources().getString(R.string.cmshome),"0");
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {

                                }
                            }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.HomePageScreen)));

                            new ApiService(new OnResponseListener() {
                                @Override
                                public void onSuccess(String response, HashMap<String, String> headers) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //   Log.e("TAG", "onSuccess: "+  response);
                                                MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");

                                                if (status)
                                                {
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                    // Log.e("TAG", "run: " + resultSet.toString());
                                                    mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);
                                                    ImageView icon = findViewById(R.id.logo);
                                                    CustomBindingAdapter.loadImage(icon,mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                                                    Log.e("TAG", "run:logo " + mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath  );
                                                    Log.e("TAG", "run:icon " + mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath  );
                                                    JSONObject object = new JSONObject(mobileBasicDetails.DetailJson);
                                                    Appcolor appcolor = new Appcolor();
                                                    appcolor = loginRes.getModel(object.toString(),Appcolor.class);
                                                    UserData.UiColor.primary = appcolor.PrimaryColor;
                                                    UserData.UiColor.secondary = appcolor.SecondColor;
                                                    UserData.UiColor.primaryfont = appcolor.ThirdColor;
                                                    UserData.UiColor.secondaryfont = appcolor.SecondColor;
                                                    UserData.UiColor.additionalcolor = appcolor.AdditionalColor;
                                                    Log.e("TAG", "run: " + appcolor.AdditionalColor );
                                                    String colordata =  loginRes.modeltostring(appcolor);
                                                    loginRes.storedata(getResources().getString(R.string.Appcolor), colordata);
                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                              //  loginRes.storedata(getResources().getString(R.string.logo), "0");
                                             //   loginRes.storedata(getResources().getString(R.string.icon), "0");
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
                                   // bodyParam.accumulate("CompanyId", cm.Id);
                                   // bodyParam.accumulate("IsActive", true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // customResponse.UserLogin(getContext(),editText_Username.getText().toString(), editText_Password.getText().toString());

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        new ApiService(doLogin, RequestType.POST,
                                                LOGIN, BASE_URL_LOGIN, header, bodyParam);
                                    }
                                },AUTO_HIDE_DELAY_MILLIS);

                            }

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

                            //String  path = "?companyKey=" +"RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
                            String  path = "?companyKey=" +userDetail.CompanyKey;

                            new ApiService(appInitialize2, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);

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
                        //  Log.e("TAG", "onSuccess: "+  response);
                        MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {

                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);

                                CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                            } catch (Exception e){
                                e.printStackTrace();
                                CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);
                            }



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
                        //  Log.e("TAG", "onSuccess: "+  response);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                splashScreen = loginRes.getModel(resultSet.toString(), com.rentguruz.app.model.display.SplashScreen.class);

                            } catch (Exception e){
                                e.printStackTrace();
                            }
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


    OnResponseListener appInitialize2 = new OnResponseListener()
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
                            //JSONObject object = resultSet.getJSONObject("companyModel");
                            loginRes.storedata("MUT",resultSet.getString("MUT"));
                            header.mut = resultSet.getString("MUT");



                            //  cm = loginRes.getModel(object.toString(),companyModel.class);
                            cm = loginRes.getModel(resultSet.toString(), companyModel.class);
                            UserData.companyModel = cm;
                            // Log.d("TAG", "run: "+String.valueOf(cm.id)  +  "  " + cm.Id  );
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
                            Helper.di = 0;
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

    private void changeicon() {

        // enable old icon
        PackageManager manager=getPackageManager();
        manager.setComponentEnabledSetting(new ComponentName(SplashScreen.this,"com.rentguruz.app.SplashScreen")
                ,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

        // disable new icon
        manager.setComponentEnabledSetting(new ComponentName(SplashScreen.this,"com.rentguruz.app.SplashScreen")
                ,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        //Toast.makeText(MainActivity.this,"Enable Old Icon" ,Toast.LENGTH_LONG).show();

    }


   /* private void storeImage(Bitmap image) {
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
            //imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }*/

}



