package com.rentguruz.app.flexiicar.login;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COUNTRYLIST;
import static com.rentguruz.app.apicall.ApiEndPoint.STATELIST;
import com.rentguruz.app.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Login extends AppCompatActivity
{
    CustomPreference preference;
    private DoHeader header;
    //Handler handler = new Handler(Looper.getMainLooper());
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        preference = new CustomPreference(this);
        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        JSONObject object = new JSONObject();

        if (preference.getdata("state").equals("")) {
            ApiService ApiService2 = new ApiService(StateList2, RequestType.GET,
                    STATELIST, BASE_URL_LOGIN, header, "");

            ApiService ApiService = new ApiService(CountryList, RequestType.GET,
                    COUNTRYLIST, BASE_URL_LOGIN, header, "");
        }

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }




    }
 /*   @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }*/

    OnResponseListener CountryList = new OnResponseListener()
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
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");


                        if (status)
                        {
                            JSONArray country = responseJSON.getJSONArray("Data");
                            preference.storeCountryState("country",responseJSON.toString());
                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
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
    };

    OnResponseListener StateList2 = new OnResponseListener()
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
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status)
                        {
                            JSONArray state = responseJSON.getJSONArray("Data");
                            preference.storeCountryState("state",responseJSON.toString());
                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
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
    };
}
