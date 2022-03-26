package com.abel.app.b2b.flexiicar.booking2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.model.BusinessSource;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.common.DropDown;
import com.abel.app.b2b.model.common.OnDropDownList;
import com.abel.app.b2b.model.parameter.CommonParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.abel.app.b2b.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.BUSINESSBOOKINGTYPE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BusinessSourceMaster;
import static com.abel.app.b2b.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;

public class Customer_Booking_Activity extends AppCompatActivity implements OnMapReadyCallback
{

    final int LOCATION_REQUEST_CODE = 1;
    public LoginRes loginRes;
    public Handler handler = new Handler(Looper.getMainLooper());
    public DoHeader header;
    public static BusinessSource businessSource;
    public static  JSONArray ints;
    BusinessSource[] businessSourceList;
    CommonParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        MapsInitializer.initialize(this);
        params = new CommonParams();
        loginRes = new LoginRes(this);
        header = new DoHeader();
        header.exptime = "12/22/2021 11:47:18 PM";
        // header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.token = loginRes.getData("TOKEN");
        header.islogin = "1";
        //header.ut = "PYOtYmuTsLQ=";
        header.ut = loginRes.getData("UT");
        header.mut = loginRes.getData("MUT");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE}, LOCATION_REQUEST_CODE);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Helper.isDropdown = true;
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_booking2);
           // navGraph.setStartDestination(R.id.Search_activity);


            if (Helper.reservationwithoutmap){
                navGraph.setStartDestination(R.id.Selected_location);
            }

        navController.setGraph(navGraph);

        DropDown dropDownList;
        Log.e("Company", "onCreate: "+loginRes.getData("CompanyId") );
        dropDownList = (new DropDown(BUSINESSBOOKINGTYPE,Integer.valueOf(loginRes.getData("CompanyId")),true,true));
        businessSource = new BusinessSource();
        new ApiService2<DropDown>(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(() -> {
                    try {

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        final JSONArray businessSoures = responseJSON.getJSONArray("Data");
                        businessSourceList = loginRes.getModel(businessSoures.toString(),BusinessSource[].class);
                        for (int i = 0; i < businessSourceList.length; i++) {
                            if (businessSourceList[i].IsDefault){
                                businessSource = businessSourceList[i];


                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap gMap) {
        boolean success = gMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style));
        if (success){
            gMap.getUiSettings().setZoomControlsEnabled(true);
            gMap.getUiSettings().setZoomGesturesEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);
            gMap.getUiSettings().setRotateGesturesEnabled(true);
            gMap.getUiSettings().setTiltGesturesEnabled(true);
            gMap.getUiSettings().setCompassEnabled(true);
            gMap.getUiSettings().setScrollGesturesEnabled(true);
            gMap.getUiSettings().setAllGesturesEnabled(true);
            gMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            gMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case LOCATION_REQUEST_CODE: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // binding.mapView.getMapAsync(this);
                    } else {
                        // CustomToast.showToast(getActivity(),"Please provide the permission",1);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  OnResponseListener location = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                    String locationss = getReservationList.getString("LocationMasterIds");
                    ints = new JSONArray();
                    for (int i = 0; i < locationss.split(",").length; i++) {
                        ints.put(Integer.valueOf(locationss.split(",")[i].trim()));
                    }
                    Log.e("TAG", "onSuccess: " + ints);
                    // new ApiService(AvailableLocation2, RequestType.POST, AVAILABLELOCATION, BASE_URL_LOGIN, header, params.getAvailableLocation(ints));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {

        }
    };*/
}