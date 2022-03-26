package com.abel.app.b2b.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.PREF;

public class LoginRes {

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public LoginRes(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void storedata(String key, String data){
        editor.putString(key, data);
        editor.commit();
    }

    public Testing dataGet(String key){
        String data = preferences.getString(key,"");
        Gson gson = new Gson();
        Testing testing = gson.fromJson(data, Testing.class);
        return testing;
    }

    public <T> T callFriend(String key, Class<T>tClass ){
        String data = preferences.getString(key,"");
        Gson gson = new Gson();
        T t = gson.fromJson(data,tClass);
        return t;
    }

    public String getData(String key){
        String data = preferences.getString(key,"");
        return data;
    }

    public <T> T getModel(String data, Class<T>tClass ){
       // Log.d("Mungara", "getModel: " + data);
        T t = null;
        try {
            Gson gson = new Gson();
           t = gson.fromJson(data,tClass);
        } catch (IllegalFormatException e){
            e.printStackTrace();
        }
   
        return t;
    }

    public <T> T getModelSystem(String key, Class<T>tClass ){
        // Log.d("Mungara", "getModel: " + data);
        T t = null;
        try {
            String data = preferences.getString(key,"");
            Gson gson = new Gson();
            t = gson.fromJson(data,tClass);
        } catch (IllegalFormatException e){
            e.printStackTrace();
        }

        return t;
    }

    public <T>String log(Class<T>tClass){
        Gson gson = new Gson();
        String personString = gson.toJson(tClass);
        return personString;
    }

    public <T> T getModelList(String data, Class<T>tClass){
        T t = null;
        try {
            Type listType = new TypeToken<ArrayList<T>>(){}.getType();
            Gson gson = new Gson();
            t = gson.fromJson(data,listType);
        } catch (IllegalFormatException e){
            e.printStackTrace();
        }

        return t;

    }

    public <T> void  test(Class<T>tClass){
        Gson gson = new Gson();
        JSONObject object = new JSONObject();
    }

    public <T> void  testingLog(String tag, T t){
        Gson gson = new Gson();
        String personString = gson.toJson(t);
        Log.e(tag, "testingLog: " +personString );
    }

    public <T> String  modeltostring(String tag, T t){
        Gson gson = new Gson();
        String personString = gson.toJson(t);
        Log.e(tag, "testingLog: " +personString );
        return personString;
    }
    public <T> String  modeltostring( T t){
        Gson gson = new Gson();
        String personString = gson.toJson(t);
        return personString;
    }
}


