/*
package com.rentguruz.app.flexiicar.booking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rentguruz.app.R;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentMapScreenBinding;
import com.rentguruz.app.databinding.LocationListLayoutBinding;
import com.rentguruz.app.flexiicar.commonfragment.CustomInfoWindowAdapter;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.flexiicar.user.User_Profile;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;

public class Map_Screen_Fragment extends BaseFragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    public String id = "";
    private Marker previousMarker;
    private final int REQUEST_PLACE_ADDRESS = 40;
    private Geocoder geocoder;
    final int LOCATION_REQUEST_CODE = 1;
    FragmentMapScreenBinding binding;
    LocationList[]  locationListsData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentMapScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onResume()
    {
        try
        {
            super.onResume();
            binding.mapView.onResume();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy()
    {
        try
        {
            if( binding.mapView != null)
            {
                binding.mapView.onDestroy();
            }
            super.onDestroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //mapView.onDestroy();
    }
    @Override
    public void onLowMemory()
    {
        try {
            super.onLowMemory();
            binding.mapView.onLowMemory();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);

            binding.mapView.onCreate(savedInstanceState);

            MapsInitializer.initialize(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
               ActivityCompat.requestPermissions(getActivity(),new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE}, LOCATION_REQUEST_CODE);
            }
            else
            {
                binding.mapView.getMapAsync(this);
            }
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");

            System.out.println("ID :- " + id);
            context = getActivity();

            //lblmenuicon= view.findViewById(R.id.media_mixer_icon);

            binding.profileIcon.setOnClickListener(this);
            binding.edtSearchLocation.setOnClickListener(this);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onMapReady(GoogleMap gMap)
    {
        try {
           //++ gMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            boolean success = gMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style));

          */
/*  boolean success = gMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));*//*


            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }

            Log.d(TAG, "onMapReady: " + 1);
            googleMap = gMap;
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiService = new ApiService(AvailableLocation, RequestType.POST,
                            AVAILABLELOCATION, BASE_URL_LOGIN,header ,  params.getAvailableLocation());
                }
            },500);


        //    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    OnResponseListener AvailableLocation = new OnResponseListener()
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
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        Log.d(TAG, "run: " + status);
                        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_markerpin2);
                        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

                        Drawable circleDrawable2 = getResources().getDrawable(R.drawable.ic_markerpin);
                        BitmapDescriptor markerIcon2 = getMarkerIconFromDrawable(circleDrawable2);

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray locationlist = resultSet.getJSONArray("Data");
                            locationListsData = loginRes.getModel(locationlist.toString(), LocationList[].class);
                            int key=0;
                            for (int i = 0; i <locationListsData.length ; i++) {
                               // getSubview(i);
                               */
/* LatLng latLng = new LatLng(locationListsData[i].addressesModel.Latitude,
                                        locationListsData[i].addressesModel.Longitude);*//*

                             //   double i1 = i/100;
                                if (locationListsData[i].AddressesModel != null){


                                Log.d(TAG, "run: " + locationListsData[i].Name + " : "+ locationListsData[i].AddressesModel.Latitude + " : " + locationListsData[i].AddressesModel.Longitude + " : " + locationListsData[i].Id + " : " + locationListsData[i].AddressesModel.Id   );
                                LatLng latLng = new LatLng(Double.valueOf(locationListsData[i].AddressesModel.Latitude), Double.valueOf(locationListsData[i].AddressesModel.Longitude));

                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                                        new LatLng(Double.valueOf(locationListsData[i].AddressesModel.Latitude),Double.valueOf(locationListsData[i].AddressesModel.Longitude))
                                ));
                                    if (locationListsData[i].Id == UserData.companyModel.DefaultReservationPickUpLocId)
                                       key = i;
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(locationListsData[i].Name)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder_2))
                                        .snippet(locationListsData[i].AddressesModel.ZipCode));
                              //  if(i==0) {
                                    if (locationListsData[i].Id == UserData.companyModel.DefaultReservationPickUpLocId){
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder));
                                    previousMarker = marker;
                                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
                                    googleMap.setInfoWindowAdapter(adapter);

                                    marker.showInfoWindow();
                                    }
                                }


                             */
/*   Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(locationListsData[i].Name)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder))
                                        .snippet(locationListsData[i].AddressesModel.ZipCode));
                                if(i==0)
                                {
                                    marker.setIcon(markerIcon2);
                                    previousMarker=marker;
                                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
                                    googleMap.setInfoWindowAdapter(adapter);

                                    marker.showInfoWindow();


                                }*//*



                            }

                            if (locationListsData.length>key){
                                LatLng latLng2 = new LatLng(Double.valueOf(locationListsData[key].AddressesModel.Latitude), Double.valueOf(locationListsData[key].AddressesModel.Longitude));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(locationListsData[key].AddressesModel.Latitude), Double.valueOf( locationListsData[key].AddressesModel.Longitude))));
                                getSubview(0);
                                LocationListLayoutBinding locationListLayoutBinding = LocationListLayoutBinding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                locationListLayoutBinding.setLocation(locationListsData[key]);
                                locationListLayoutBinding.getRoot().setId(200+0);
                                locationListLayoutBinding.getRoot().setLayoutParams(subparams);
                                binding.locationLayout.addView(locationListLayoutBinding.getRoot());
                                int finalI = key;
                                binding.edtSearchLocation.setHint(locationListsData[key].Name);
                                locationListLayoutBinding.txtSelectLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putSerializable("model", locationListsData[finalI]);

                                        NavHostFragment.findNavController(Map_Screen_Fragment.this)
                                                .navigate(R.id.action_Search_activity_to_Selected_location, bundle);
                                    }
                                });
                                binding.mediaMixerIcon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putSerializable("model", locationListsData[finalI]);

                                        NavHostFragment.findNavController(Map_Screen_Fragment.this)
                                                .navigate(R.id.action_Search_activity_to_Selected_location, bundle);
                                    }
                                });
                            }

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NotNull Marker marker) {
                                    if (previousMarker!=null){
                                        previousMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder_2));
                                    }
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder));

                                  */
/*  if (previousMarker!=null){
                                        previousMarker.setIcon(markerIcon);
                                    }
                                    marker.setIcon(markerIcon2);*//*

                                    previousMarker = marker;
                                    binding.locationLayout.removeAllViews();
                                    for (int j = 0; j <locationListsData.length ; j++) {

                                        if (Double.valueOf(locationListsData[j].AddressesModel.Latitude) == marker.getPosition().latitude &&
                                              Double.valueOf(locationListsData[j].AddressesModel.Longitude) == marker.getPosition().longitude) {

                                            //getSubview(j);
                                            LatLng latLng2 = new LatLng(Double.valueOf(locationListsData[j].AddressesModel.Latitude), Double.valueOf(locationListsData[j].AddressesModel.Longitude));
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(locationListsData[j].AddressesModel.Latitude), Double.valueOf(locationListsData[j].AddressesModel.Longitude))));
                                            getSubview(j);
                                            LocationListLayoutBinding locationListLayoutBinding = LocationListLayoutBinding.inflate(subinflater,
                                                    getActivity().findViewById(android.R.id.content), false);
                                            locationListLayoutBinding.setLocation(locationListsData[j]);
                                            locationListLayoutBinding.getRoot().setId(200 + j);
                                            locationListLayoutBinding.getRoot().setLayoutParams(subparams);
                                            binding.edtSearchLocation.setHint(locationListsData[j].Name);
                                            int finalJ = j;
                                            locationListLayoutBinding.txtSelectLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    bundle.putSerializable("model", locationListsData[finalJ]);

                                                    NavHostFragment.findNavController(Map_Screen_Fragment.this)
                                                            .navigate(R.id.action_Search_activity_to_Selected_location, bundle);
                                                }
                                            });

                                            binding.mediaMixerIcon.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    bundle.putSerializable("model", locationListsData[finalJ]);

                                                    NavHostFragment.findNavController(Map_Screen_Fragment.this)
                                                            .navigate(R.id.action_Search_activity_to_Selected_location, bundle);
                                                }
                                            });

                                            binding.locationLayout.addView(locationListLayoutBinding.getRoot());
                                        }
                                    }
                                    return false;
                                }
                            });

                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        try {
            switch (requestCode)
            {
                case LOCATION_REQUEST_CODE:
                    {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        binding.mapView.getMapAsync(this);
                    } else
                        {
                            CustomToast.showToast(getActivity(),"Please provide the permission",1);
                        }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_ADDRESS && resultCode == Activity.RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            // Log.i(TAG, "Place city and postal code: " + place.getAddress().subSequence(place.getName().length(),place.getAddress().length()));
            try {
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    JSONObject bodyParam = new JSONObject();
                    try
                    {
                        bodyParam.put("startlat", place.getLatLng().latitude);
                        bodyParam.put("startlng", place.getLatLng().longitude);
                        Log.d(TAG, "onActivityResult: " +place.getLatLng().latitude + " : " + place.getLatLng().longitude );
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                 //   AndroidNetworking.initialize(getActivity());

                 */
/*   ApiService ApiService = new ApiService(AvailableLocation, RequestType.POST,
                            AVAILABLELOCATION, BASE_URL_LOGIN, header, bodyParam);*//*


                    */
/*addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String Street=addresses.get(0).getFeatureName();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();

                    Log.e("Address: ", "" + address);
                    Log.e("City: ", "" + city);
                    Log.e("Street: ", "" + Street);
                    Log.e("State: ", "" + state);
                    Log.e("Country: ", "" + country);
                    Log.e("APostalCode: ", "" + postalCode);

                    edt_CustStreet.setText(Street);
                    edtcust_cityName.setText(city);
                    edt_CustZipCode.setText(postalCode);

                    for(int i=0;i<State.length;i++)
                    {

                        if(State[i].equals(state))
                        {
                            spStateList.setSelection(i);
                            break;
                        }
                    }

                    for(int j=0;j<Country.length;j++)
                    {

                        if(Country[j].equals(country))
                        {
                            spCountryList.setSelection(j);
                            break;
                        }
                    }*//*


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            }
    );

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_icon:
                Intent i = new Intent(getActivity(), User_Profile.class);
                startActivity(i);
                break;

            case R.id.edtSearchLocation:
                try
                {
                    if(!Places.isInitialized())
                    {
                        Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                    }
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                    startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.media_mixer_icon:
                NavHostFragment.findNavController(Map_Screen_Fragment.this)
                        .navigate(R.id.action_Search_activity_to_Selected_location, bundle);
                break;
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

*/
/*    public String getMapData(){
        return "[{\"featureType\":\"all\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#242f3e\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"lightness\":-80}]},{\"featureType\":\"administrative\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#746855\"}]},{\"featureType\":\"administrative.locality\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#d59563\"}]},{\"featureType\":\"poi\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#d59563\"}]},{\"featureType\":\"poi.park\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#263c3f\"}]},{\"featureType\":\"poi.park\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#6b9a76\"}]},{\"featureType\":\"road\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#2b3544\"}]},{\"featureType\":\"road\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#9ca5b3\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#38414e\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#212a37\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#746855\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#1f2835\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#f3d19c\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#38414e\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#212a37\"}]},{\"featureType\":\"transit\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#2f3948\"}]},{\"featureType\":\"transit.station\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#d59563\"}]},{\"featureType\":\"water\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#17263c\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#515c6d\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"lightness\":-20}]}]";
    }*//*

}*/
