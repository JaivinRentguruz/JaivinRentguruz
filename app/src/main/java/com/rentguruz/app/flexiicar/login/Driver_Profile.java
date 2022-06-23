package com.rentguruz.app.flexiicar.login;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.response.StateCountry;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.apicall.OnResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.rentguruz.app.R;

public class Driver_Profile extends AppCompatActivity
{
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    ArrayList<String> scanData = new ArrayList();
    CustomPreference preference;
    private DoHeader header;

    Handler handler = new Handler(Looper.getMainLooper());
    StateCountry stateCountry;
    public static List<StateCountry> stateCountryList = new ArrayList<>();

    public static HashMap<String, Integer> country_Code = new HashMap<>();
    public static HashMap<String, Integer> state_stateCode = new HashMap<>();
    public static HashMap<String, Integer> state_countryCode = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);
        scanData = getIntent().getStringArrayListExtra("scanData");
        preference = new CustomPreference(this);

        stateCountry = new StateCountry();

     /*   NavHostFragment hostFragment = NavHostFragment.create(R.navigation.nav_graph_driver_registration);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, hostFragment)
                .setPrimaryNavigationFragment(hostFragment)
                .commit();*/

        if (Helper.RegistrationD){

            NavHostFragment hostFragment2 = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController2 = hostFragment2.getNavController();
            NavGraph navGraph2 = navController2.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
            navGraph2.setStartDestination(R.id.CreateProfile);
            navController2.setGraph(navGraph2);

            if (scanData != null) {
                preference.stroeScandata(scanData);
                Log.d("Driver_Profile", "onCreate: " + scanData);

                NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = hostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
                navGraph.setStartDestination(R.id.DriverProfile);
                navController.setGraph(navGraph);
            }
        } /*else {

            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
            navGraph.setStartDestination(R.id.DriverProfile);
            navController.setGraph(navGraph);
        }
*/
        else {
            if (scanData != null) {
                preference.stroeScandata(scanData);
                Log.d("Driver_Profile", "onCreate: " + scanData);

                NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = hostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
                navGraph.setStartDestination(R.id.DriverProfile);
                navController.setGraph(navGraph);
            }
        }

        try{
            if (Helper.skipScan){
                NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = hostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
                navGraph.setStartDestination(R.id.DriverProfile);
                navController.setGraph(navGraph);
                Helper.skipScan= false;
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";


   /*     ApiService ApiService2 = new ApiService(StateList2, RequestType.GET,
                STATELIST, BASE_URL_LOGIN, header, null);

        ApiService ApiService = new ApiService(CountryList, RequestType.GET,
                COUNTRYLIST, BASE_URL_LOGIN, header, null);*/

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }




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
                        //        System.out.println("Success");
                        //        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");


                        if (status)
                        {

                            JSONArray country = responseJSON.getJSONArray("Data");
                            //    final JSONArray Countrylist = resultSet.getJSONArray("t0040_Country_Master");
                            preference.storeCountryState("country",country.toString());
                            System.out.println(country);

                            int len;
                            len = country.length();

                           String[] Country = new String[len];
                           int[] CountyId = new int[len];

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) country.get(j);
                                final int country_ID = test.getInt("Id");
                                final String country_Name = test.getString("Name");
                                Country[j] = country_Name;
                                CountyId[j] = country_ID;
                                stateCountry.CountryId = country_ID;
                                stateCountry.Countryname = country_Name;

                               /* countryhashmap.put(country_Name,country_ID);
                                countryhashmap2.put(country_ID, country_Name);*/
                                country_Code.put( country_Name, country_ID);

                            }
                           stateCountryList.add(stateCountry);
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
                        //    System.out.println("Success");
                        //    System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {

                            JSONArray state = responseJSON.getJSONArray("Data");
                            preference.storeCountryState("state",state.toString());

                            int len;
                            len = state.length();

                           int[] StateId = new int[len];
                           String[] State = new String[len];

                            for (int j = 0; j < state.length(); j++)
                            {
                                final JSONObject test = (JSONObject) state.get(j);
                                final int state_ID = test.getInt("CountryId");
                                final String state_Name = test.getString("Name");
                                final int state_ID2 = test.getInt("Id");
                                State[j] = state_Name;
                                StateId[j] = state_ID;
                                //State2[j] = state_ID2;
                              /*  Statehashmap2.put(state_Name,state_ID);
                                Statehashmap3.put(state_Name,state_ID2 );*/

                                stateCountry.StateId = state_ID2;
                                stateCountry.Statename = state_Name;

                                state_stateCode.put(state_Name, state_ID2);
                                state_countryCode.put(state_Name, state_ID);
                            }
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
