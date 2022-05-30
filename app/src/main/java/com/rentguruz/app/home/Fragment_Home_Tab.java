package com.rentguruz.app.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rentguruz.app.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentHomeBinding;
import com.rentguruz.app.model.display.HomeScreen;
import com.rentguruz.app.model.response.CompanyLabel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.DateType;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SPLASH;


public class Fragment_Home_Tab extends BaseFragment
{
    LinearLayout lblUserTimeLine,lblAgreement,lblReservation;
    LocationManager locationManager;
    boolean GpsStatus;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CAMERA = 2;
    public static String lat,log;
    TextView loc1,loc2,loc3;
    ImageView image;
    Handler handler=new Handler(Looper.getMainLooper());
    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
      //  return inflater.inflate(R.layout.fragment_home, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        ((Activity_Home)getActivity()).BottomnavVisible();
        //((Activity_Home) getActivity()).BottomnavInVisible();
        lblUserTimeLine=view.findViewById(R.id.lblUserTimeLine);
        lblAgreement=view.findViewById(R.id.lblAgreement);
        lblReservation=view.findViewById(R.id.lblReservation);
        loc1=view.findViewById(R.id.loc1);
        loc2=view.findViewById(R.id.loc2);
        loc3=view.findViewById(R.id.loc3);
        image = view.findViewById(R.id.homeImage);
        binding.setUiColor(UiColor);
        binding.details.setUiColor(UiColor);
      //  getview.getCompanyLabel(view, companyLabel);
        Log.e(TAG, "onViewCreated: " + getResources().getResourceName(R.color.black));
        try {
            Log.e("TAG", "onViewCreated: " + UserData.loginResponse.LogedInCustomer.FullName);
            TextView name =  view.findViewById(R.id.name);
            name.setText(UserData.loginResponse.User.UserName);
            try {
                //UserData.loginResponse.User.UserName = "js sps";
                String[] d1 = UserData.loginResponse.User.UserName.split(" ");
               // Log.e(TAG, "onViewCreated: " + d1[0] + "  "  +  d1[1] );
                /*if (d1[1].isEmpty()){
                    setText(binding.details.f, String.valueOf(UserData.loginResponse.User.UserName.charAt(1)));
                    setText(binding.details.l, String.valueOf(UserData.loginResponse.User.UserName.charAt(2)));
                }*/
               String datas =  UserData.loginResponse.User.UserName.split(" ")[1];
                setText(binding.details.l, UserData.loginResponse.User.UserName.split(" ")[1]);
                setText(binding.details.f, UserData.loginResponse.User.UserName.split(" ")[0]);
            } catch (Exception e){
                e.printStackTrace();
                setText(binding.details.f, String.valueOf(UserData.loginResponse.User.UserName.charAt(0)));
                setText(binding.details.l, String.valueOf(UserData.loginResponse.User.UserName.charAt(1)));
            }
            //CustomBindingAdapter.camelcase(view.findViewById(R.id.name), UserData.loginResponse.User.UserName);
        if (UserData.loginResponse.User.addressesModel!=null) {
            String data = "appid=fa5d4f1a62ad70c2521d22756bd210d9&lat=" + UserData.loginResponse.User.addressesModel.Latitude + "&lon=" + UserData.loginResponse.User.addressesModel.Longitude + "&units=Imperial";
            new ApiService(weatherReport, RequestType.GET, "data/2.5/weather", "https://api.openweathermap.org/", new HashMap<String, String>(), data);
        } else {
            if (!CheckGpsStatus()) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
            getCurrentLocation();
        }
        } catch (Exception e){
            e.printStackTrace();
        }



        ImageView logo = view.findViewById(R.id.logo);
        CustomBindingAdapter.loadImage(logo, loginRes.getData(getResources().getString(R.string.logo)));
        ImageView icon = view.findViewById(R.id.icon);
        CustomBindingAdapter.loadImage(icon,loginRes.getData(getResources().getString(R.string.icon)));
        CustomBindingAdapter.loadImage(image,loginRes.getData(getResources().getString(R.string.cmsdash)));

       // new ApiService(getHomeImage, RequestType.POST,SPLASH,BASE_URL_LOGIN,header,params.getBasicDetail(getResources().getString(R.string.HomePageScreen),2));

        //setLable(UserData.loginResponse.CompanyLabel);

       /* lblUserTimeLine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Home_Tab.this)
                        .navigate(R.id.action_Customer_Profile_to_UserTimeLine);
            }
        });

        lblAgreement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Agreements.class);
                startActivity(i);
            }
        });

        lblReservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Reservation.class);
                startActivity(i);
            }
        });*/


        /*if (!CheckGpsStatus()) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
        getCurrentLocation();*/

       /* lblAgreement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              //  NavHostFragment.findNavController(Fragment_Home_Tab.this).navigate(R.id.profile_to_newAgreemnent);
              //  new  Fragment_Selected_Location().startdatejourney = null;
              //  NavHostFragment.findNavController(Fragment_Home_Tab.this).navigate(R.id.profile_to_location);

                NavHostFragment.findNavController(Fragment_Home_Tab.this).navigate(R.id.testing);
            }
        });*/

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
             requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            //Permission already granted
        }

    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    //Permission granted

                } else {
                    //permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        //show permission snackbar
                    } else {
                        //display error dialog
                    }
                }

            });


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    public Boolean CheckGpsStatus(){
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return GpsStatus;
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLastLocation() != null){
                            Log.e("TAG", "onLocationResult: " + locationResult.getLocations().get(0).getLatitude() +  " : " + locationResult.getLocations().get(0).getLongitude() );
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getContext(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                lat = String.valueOf(locationResult.getLastLocation().getLatitude());
                                log = String.valueOf(locationResult.getLastLocation().getLongitude());
                                String data="appid=fa5d4f1a62ad70c2521d22756bd210d9&lat=" + lat +"&lon=" +log+"&units=Imperial";
                                new ApiService(weatherReport, RequestType.GET, "data/2.5/weather","https://api.openweathermap.org/", new HashMap<String, String>(),data);

                                Log.e("TAG", "onLocationResult: " + locationResult.getLastLocation().getLatitude() +  " : " + locationResult.getLastLocation().getLongitude() );
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                Log.e("TAG", "onLocationResult: " +  addresses.get(0));

                                Log.e("TAG", "onLocationResult: " + addresses.get(0).getMaxAddressLineIndex());

                                // useraddress =  addresses.get(0).getPremises() + " " +  knownName + " " + city + " " + state + " " + postalCode;
                                String[] a = address.split(",");
                                Log.e("TAG", "onLocationResult: " + a.length);
                                Log.e("TAG", "onLocationResult: " + a[0] + " " + a[1] + " " + a[2]   );
                                //useraddress = address;
                                /*loc1.setText(knownName);
                                loc2.setText(city);
                                loc3.setText(state);*/

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }, Looper.getMainLooper());

    }

    OnResponseListener weatherReport = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Log.e("TAG", "run: " + responseJSON );
                        JSONObject coord = responseJSON.getJSONObject("coord");
                        JSONArray weather = responseJSON.getJSONArray("weather");
                        for (int i = 0; i < weather.length(); i++) {
                            JSONObject weathers = weather.getJSONObject(i);
                            TextView weatherReport = getView().findViewById(R.id.weatherReport);
                            weatherReport.setText(Helper.getCapitalise(weathers.getString("description")));
                        }
                        JSONObject main = responseJSON.getJSONObject("main");
                        TextView humidity = getView().findViewById(R.id.humidity);
                        humidity.setText(main.getString("humidity") + " %");
                        TextView temp = getView().findViewById(R.id.temp);
                        Double tt = Double.parseDouble(main.getString("temp"));
                        //Helper.getAmtount(tt);
                        String amt2 = String.format(Locale.US,"%.0f",tt);
                        temp.setText(amt2+"°c");

                        JSONObject wind = responseJSON.getJSONObject("wind");
                        TextView winds = getView().findViewById(R.id.wind);
                        winds.setText(wind.getString("speed")+"km/hr");

                        TextView location = getView().findViewById(R.id.location);
                        location.setText(responseJSON.getString("name"));
                        loc1.setText(responseJSON.getString("name"));
                        Date date = new Date();
                        loc2.setText(Helper.getDateDisplay(DateType.defaultdate,date.toString()));
                        loc3.setText(DateConvert.DateConverter(DateType.defaultdate,date.toString(),DateType.day));

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
    public void onClick(View v) {

    }

    OnResponseListener getHomeImage = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("TAG", "onSuccess: "+  response);
                        Log.e("TAG", "onSuccess: "+  response);
                        HomeScreen homeScreen = new HomeScreen();
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status){
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            homeScreen =    loginRes.getModel(resultSet.toString(), HomeScreen.class);
                            //ImageView image =
                            //loginRes.storedata(getResources().getString(R.string.homescreenimage), homeScreen.AttachmentsModels.get(0).AttachmentPath);
                            //  CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(0).AttachmentPath);

                            for (int i = 0; i <homeScreen.AttachmentsModels.size() ; i++) {
                                if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmsdash))){
                                    loginRes.storedata(getResources().getString(R.string.cmsdash), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                    CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                }
                            }

                        }

                    } catch (Exception e){
                        e.printStackTrace();
                       // loginRes.storedata(getResources().getString(R.string.cmsdash), "00");
                    }
                }
            });

        }

        @Override
        public void onError(String error) {

        }
    };

    public void setLable( CompanyLabel companyLabel){
        try {
            if (companyLabel !=null){

            } else {
                companyLabel = new CompanyLabel();
            }
            String open = "Open \n ";
           binding.txtopnres.setText(open + companyLabel.Reservation);
           binding.txtopnagr.setText(open + companyLabel.Agreement);
           binding.txtopntick.setText("Traffic \nTickets");

           binding.txt1agr.setText(companyLabel.Agreement);
           binding.txt2agr.setText(companyLabel.Agreement);

            binding.txt2res.setText(companyLabel.Reservation);
            binding.txt1res.setText(companyLabel.Reservation);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setText(TextView text, String data){
        try {
            StringBuilder builder = new StringBuilder(data); // String builder to store string
            final int builderLength = builder.length();
            for (int i = 0; i < builderLength; ++i) {
                Log.e(TAG, "onViewCreated: " + builder.charAt(i) );
                char c = builder.charAt(i);
                builder.setCharAt(i, Character.toTitleCase(c));
                break;
            }
            text.setText(String.valueOf(builder.charAt(0)));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
