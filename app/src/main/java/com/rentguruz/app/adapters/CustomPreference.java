package com.rentguruz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.rentguruz.app.R;
import com.google.gson.Gson;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.model.response.Mystate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.PREF;

public class CustomPreference {

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public Context context;

   public HashMap<String, Integer> countryToCountryCode = new HashMap<>();
   public HashMap<Integer,String> codetoCountry = new HashMap<>();
   public HashMap<String, Integer> stateToSateCode = new HashMap<>();
   public HashMap<String, Integer> stateToCountryCode = new HashMap<>();
   private List<Mystate> mystates = new ArrayList<>();
   public static int defaultstate = 0;
    Activity activity;
    List<String> data = new ArrayList<>();
   private LinkedHashSet<String> stateData = new LinkedHashSet<>();
    Gson gson;
    int countrycode =0, statecode = 0;
    public CustomPreference(Context context) {
        this.context = context;
        gson = new Gson();
        preferences = context.getSharedPreferences(PREF,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public void stroeScandata (ArrayList<String> scanData)
    {
        for (String data : scanData)
        {
            String[] datas = data.split(":");
            if (datas[0].equals("Given Name"))
                editor.putString("Given Name", datas[1]);
            else if (datas[0].equals("Address Line 1"))
                editor.putString("Address Line 1", datas[1]);
            else if (datas[0].equals("Address City"))
                editor.putString("Address City", datas[1]);
            else if (datas[0].equals("Address Postal Code"))
                editor.putString("Address Postal Code", datas[1]);
            else if (datas[0].equals("Issuing State Name"))
                editor.putString("Issuing State Name", datas[1]);
            else if (datas[0].equals("DD Number"))
                editor.putString("DD Number", datas[1]);
            else if (datas[0].equals("Birth Date"))
                editor.putString("Birth Date", userDate(datas[1]));
            else if (datas[0].equals("Issue Date"))
                editor.putString("Issue Date", userDate(datas[1]));
            else if (datas[0].equals("Expiration Date"))
                editor.putString("Expiration Date", userDate(datas[1]));

            editor.commit();
        }
    }

    public String getdata (String key)
    {
        return  preferences.getString(key,"");
    }

    public void storeData (String key, String data)
    {
        editor.putString(key, data);
        editor.commit();
    }

    public String userDate(String timemills)
    {
        timemills = timemills.replace("/Date(","");
        timemills = timemills.replace(")/","");
      //  Log.d("Mungara", "userDate: " + timemills);

    return convertDate(timemills,"MM/dd/yyyy");
    }

    public String convertDate(String dateInMilliseconds,String dateFormat)
    {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public void storeCountryState (String key, String data)
    {
        editor.putString(key, data);
     //   Log.d("Mungara", "storeCountryState: " + key + " : " + data);
        editor.commit();
    }

    public List<String> getStringArray (String key){
       List<String> data = new ArrayList<>();
       if (key.equals("country")) {
           String response = preferences.getString(key, "");
           try {
         //      Log.d("Mungara", "getStringArray: " + response);
               JSONObject responseJSON = new JSONObject(response);
               JSONArray country = responseJSON.getJSONArray("Data");
               data.add("Select country");
               int len = country.length();
               for (int i = 0; i < len; i++) {
                   JSONObject test = (JSONObject) country.get(i);
                   int country_ID = test.getInt("Id");
                   String country_Name = test.getString("Name");
                   data.add(country_Name);
                   countryToCountryCode.put(country_Name, country_ID);
                   codetoCountry.put(country_ID,country_Name);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       } else {
           String response = preferences.getString(key, "");
           try {
          //     Log.d("Mungara", "getStringArray: " + response);
               JSONObject responseJSON = new JSONObject(response);
               JSONArray country = responseJSON.getJSONArray("Data");
               data.add("Select State");
               int len = country.length();
               for (int i = 0; i < len; i++) {
                   JSONObject test = (JSONObject) country.get(i);
                   int country_ID = test.getInt("CountryId");
                   int state_ID = test.getInt("Id");
                   String state_Name = test.getString("Name");
                   data.add(state_Name);
                   stateToSateCode.put(state_Name, state_ID);
                   stateToCountryCode.put(state_Name, country_ID);
                   mystates.add(new Mystate(country_ID,state_Name,state_ID));
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

        return data;
    }

    public List<String> getstatename(String country){
        data.clear();
        data.add("Select State");
       // Log.d("Mungara", "getstatename: " + country + " : " + countryToCountryCode.get(country));
       // Log.d("Mungara", "getstatename: " + country + ":" + data.size() );
        try {
        int countrycode2 = countryToCountryCode.get(country);
     //   Log.d("Mungara", "getstatename: " + mystates.size());

        for (int i = 0; i <mystates.size() ; i++) {
            if (mystates.get(i).countrycode==countrycode2){
            //    Log.d("Mungara", "getstatename: " + mystates.get(i).statename);
                data.add(mystates.get(i).statename);
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getDefaultstate (String statename){
        int id = 0;
        for (int i = 0; i <data.size() ; i++) {
            if (data.get(i).equals(statename)){
                id=i;
                break;
            }
        }
        return id;
    }

    public int countrycode(String state){
        int code = 0;
      //  Log.d("Mungara", "countrycode: "+ state);
        for (int i = 0; i <mystates.size() ; i++){
            if (mystates.get(i).statename==state){
              //  Log.d("Mungara", "countrycode: " + mystates.get(i).countrycode);
                code = mystates.get(i).countrycode;
            //    Log.d("Mungara", "countrycode: "+  codetoCountry.get(code));
                break;
            }
        }
      return code;
    }

    public LoginResponse getLogin(){
        LoginResponse loginResponse = new LoginResponse();
        try {
        String data = preferences.getString("Data","");
     //   Log.d("Mungara", "getLogin: " + data);

        loginResponse = gson.fromJson(data, LoginResponse.class);
    //    Log.d("Mungara", "getLogin: " + loginResponse.apiUserLogin.UserId);

        } catch (Exception e){
            e.printStackTrace();
        }
        return loginResponse;
    }

    public void stateCountry(Spinner country, Spinner state, String countryname,String statename )
    {
        try {
        List<String> test = new ArrayList<>();
        test = getStringArray("state");

      //  Log.d("Mungara", "stateCountry: " +  countryname  +  " : " + statename + " : " + test.size());
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,getStringArray("country"));
        country.setAdapter(adapterCategories);
        if (countryname!="") {
            countrycode = countryToCountryCode.get(countryname);
        } else {
            countrycode = 0;
        }
        country.setSelection(countrycode);

        /*ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, getstatename(country.getSelectedItem().toString()));
        state.setAdapter(adapterCategories2);
        state.setSelection(statecode);*/

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, getstatename(country.getSelectedItem().toString()));
                if (statename!="") {
                    statecode = getDefaultstate(statename);
                } else {
                    statecode = 0;
                }
                state.setAdapter(adapterCategories2);
                state.setSelection(statecode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

