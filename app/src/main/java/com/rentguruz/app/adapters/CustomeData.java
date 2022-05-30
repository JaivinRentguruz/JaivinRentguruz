package com.rentguruz.app.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.STATELIST;

public class CustomeData {
    public DoHeader header;
    public Context context;
    public LoginRes loginRes;
    OnResponseListener responseListener;
    ApiService ApiService;
    String data;
    Handler handler=new Handler(Looper.getMainLooper());
    public CustomeData(Context context) {
        this.context = context;
        loginRes = new LoginRes(context);
        header = new DoHeader();
    }

    public boolean check(){
      Boolean value = false;
      if (!loginRes.getData("DataVersion").equals("1")){
        value = true;
      }
      return value;
    }

    public DoHeader getHeader(){
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        return header;
    }

    public String  countryList(JSONObject body){
        ApiService = new ApiService(responseListener, RequestType.GET,
                STATELIST, BASE_URL_LOGIN, getHeader(), body);
        responseListener = new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            if (status)
                            {
                                JSONArray country = responseJSON.getJSONArray("Data");
                                loginRes.storedata("Data", country.toString());
                                data = country.toString();
                            }
                            else
                            {
                                String errorString = responseJSON.getString("Message");
                            }
                        }   catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

            }
            @Override
            public void onError(String error) {

            }
        };
    return data;
    }

}
