package com.rentguruz.app.flexiicar.selfcheckout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.model.response.Reservation;
import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufsm.keyholder.mobile.KeyholderInterface;
import com.hufsm.secureaccess.ble.log.LoggingInterface;
import com.hufsm.tacs.mobile.TacsInterface;
import com.hufsm.tacs.mobile.TacsKeyring;
import com.hufsm.telematics.mobile.TelematicsInterface;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentLocationAndKey2Binding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_HUFKEY;
import static com.rentguruz.app.apicall.ApiEndPoint.GET_MOBILEKEY_DETAILS;

public class Fragment_Location_And_Key extends BaseFragment implements TacsInterface.Callback, LoggingInterface.EventLogCallback , OnMapReadyCallback
{
    TextView lblback;
    ImageView imgback;
    TextView Done;

    FragmentLocationAndKey2Binding binding;
    final int LOCATION_REQUEST_CODE = 1;
    TacsInterface tacsInterface;
    private boolean useMockMode = true;
    // TacsInterface tacsInterface;
    TacsInterface.VehicleStatus currentStatus;
    public static final Collection<TelematicsInterface.TelematicsDataType> DEFAULT_TELEMATICS_REQUEST = TelematicsInterface.TelematicsDataType.buildRequest();
    TacsKeyring keyring = null;
    String MY_MOCK_ACCESS_GRANT_ID;
    String tacsKeyring;
    final Object mutex = new Object();
    BluetoothAdapter mBluetoothAdapter;
    private GoogleMap googleMap;
    Reservation reservation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        binding = FragmentLocationAndKey2Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        bundle.putSerializable( "reservation",getArguments().getSerializable("reservation"));
        bundle.putInt("Id", getArguments().getInt("Id"));
        bundle.putSerializable("resrvation",getArguments().getSerializable("resrvation"));
        bundle.putSerializable("checklist",getArguments().getSerializable("checklist"));
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.discard.setText(getResources().getString(R.string.done));
        binding.header.screenHeader.setText(getResources().getString(R.string.locationkey));

        try{
            CustomBindingAdapter.loadImage(binding.logo,loginRes.getData(getResources().getString(R.string.logo)));
            reservation =(Reservation) getArguments().getSerializable("reservation");
            binding.txtVehiclaName.setText(reservation.VehicleName);
            binding.txtAddress.setText(reservation.VehicleName);
            CustomBindingAdapter.loadImage(binding.carimage,reservation.VehicleImagePath);
        }catch(Exception e){
            e.printStackTrace();
        }
        binding.lock.setVisibility(View.VISIBLE);
        binding.unlock.setVisibility(View.GONE);
      /*  MapsInitializer.initialize(this.getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_REQUEST_CODE);
        }
        else
        {
            binding.mapView.getMapAsync(this);
        }*/



        MapsInitializer.initialize(this.getActivity());
        binding.mapView.getMapAsync(this);
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

        try {
            binding.mapView.onCreate(savedInstanceState);
            MapsInitializer.initialize(getActivity());
            binding.mapView.getMapAsync(this);
        } catch (Exception e){
            e.printStackTrace();
        }

        turnOnBluetoothDevice();

        AndroidNetworking.initialize(getActivity());

        String bodyParam = "";
        try {
            //bodyParam += "vehicleId=" + vehiclE_ID;
            bodyParam += "vehicleId=" + 1;
            System.out.println(bodyParam);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        apiService = new ApiService(getMobileKeyDetails, RequestType.GET,
                GET_MOBILEKEY_DETAILS, BASE_URL_HUFKEY, new HashMap<String, String>(), bodyParam);

        CustomBindingAdapter.loadImage(binding.logo,loginRes.getData(getResources().getString(R.string.logo)));

        /*imgback=view.findViewById(R.id.imgback_stopengine);

        imgback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                *//*NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Location_And_Key_to_Waiver_Signature);*//*
            }
        });

        Done=view.findViewById(R.id.Done);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Waiver_Signature_to_Agreements);
            }
        });*/
    }

    OnResponseListener getMobileKeyDetails = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                JSONObject hufKeyDetailModelList = resultSet.getJSONObject("hufKeyDetailModel");

                                JSONObject keyringJson = hufKeyDetailModelList.getJSONObject("keyring");
                                {
                                    String userLeaseTokenTableVersion = keyringJson.getString("userLeaseTokenTableVersion");
                                    JSONArray userLeaseTokenTable = keyringJson.getJSONArray("userLeaseTokenTable");
                                    JSONArray userSorcBlobTable = keyringJson.getJSONArray("userSorcBlobTable");


                                    int len;
                                    len = userLeaseTokenTable.length();
                                    for (int j = 0; j < len; j++) {
                                        final JSONObject test = (JSONObject) userLeaseTokenTable.get(j);
                                        final String leaseId = test.getString("leaseId");
                                        final String userId = test.getString("userId");
                                        final String sorcId = test.getString("sorcId");

                                        final String leaseTokenDocumentVersion = test.getString("leaseTokenDocumentVersion");
                                        final String leaseTokenId = test.getString("leaseTokenId");
                                        final String sorcAccessKey = test.getString("sorcAccessKey");
                                        final String startTime = test.getString("startTime");
                                        final String endTime = test.getString("endTime");

                                        JSONArray serviceGrantList = test.getJSONArray("serviceGrantList");

                                        int len1;
                                        len1 = serviceGrantList.length();

                                        for (int k = 0; k < len1; k++) {
                                            final JSONObject test1 = (JSONObject) serviceGrantList.get(k);
                                            final String serviceGrantId = test1.getString("serviceGrantId");
                                            final JSONObject validators = test1.getJSONObject("validators");
                                            final String ValistartTime = validators.getString("startTime");
                                            final String ValiendTime = validators.getString("endTime");
                                        }
                                    }

                                    int len3;
                                    len3 = userSorcBlobTable.length();
                                    for (int i = 0; i < len3; i++) {
                                        final JSONObject test = (JSONObject) userSorcBlobTable.get(i);
                                        //final String sorcId = test.getString("sorcId");
                                        final String blob = test.getString("blob");
                                        final String blobMessageCounter = test.getString("blobMessageCounter");

                                    }

                                }

                                JSONObject tacsKeyringJson = hufKeyDetailModelList.getJSONObject("tacsKeyring");
                                tacsKeyring=tacsKeyringJson.toString();
                                {
                                    String tacsLeaseTokenTableVersion = tacsKeyringJson.getString("tacsLeaseTokenTableVersion");
                                    String tacsSorcBlobTableVersion = tacsKeyringJson.getString("tacsSorcBlobTableVersion");
                                    JSONArray tacsLeaseTokenTable = tacsKeyringJson.getJSONArray("tacsLeaseTokenTable");
                                    JSONArray tacsSorcBlobTable = tacsKeyringJson.getJSONArray("tacsSorcBlobTable");

                                    int len;
                                    len = tacsLeaseTokenTable.length();
                                    for (int j = 0; j < len; j++)
                                    {
                                        final JSONObject test = (JSONObject) tacsLeaseTokenTable.get(j);
                                        final String vehicleAccessGrantId = test.getString("vehicleAccessGrantId");
                                        JSONObject leaseToken = test.getJSONObject("leaseToken");
                                        final String leaseId = leaseToken.getString("leaseId");
                                        final String userId = leaseToken.getString("userId");
                                        final String sorcId = leaseToken.getString("sorcId");

                                        final String leaseTokenDocumentVersion = leaseToken.getString("leaseTokenDocumentVersion");
                                        final String leaseTokenId = leaseToken.getString("leaseTokenId");
                                        final String sorcAccessKey = leaseToken.getString("sorcAccessKey");
                                        final String startTime = leaseToken.getString("startTime");
                                        final String endTime = leaseToken.getString("endTime");

                                        MY_MOCK_ACCESS_GRANT_ID=vehicleAccessGrantId;

                                        final JSONArray serviceGrantList = leaseToken.getJSONArray("serviceGrantList");

                                        int len1;
                                        len1 = serviceGrantList.length();

                                        for (int k = 0; k < len1; k++) {
                                            final JSONObject test1 = (JSONObject) serviceGrantList.get(k);
                                            final String serviceGrantId = test1.getString("serviceGrantId");
                                            final JSONObject validators = test1.getJSONObject("validators");
                                            final String ValistartTime = validators.getString("startTime");
                                            final String ValiendTime = validators.getString("endTime");
                                        }
                                    }
                                    int len3;
                                    len3 = tacsSorcBlobTable.length();
                                    for (int i = 0; i < len3; i++)
                                    {
                                        final JSONObject test = (JSONObject) tacsSorcBlobTable.get(i);
                                        //final String sorcId = test.getString("sorcId");
                                        //final String blob = test.getString("blob");
                                        //final String blobMessageCounter = test.getString("blobMessageCounter");

                                    }
                                }
                                keyring = getMockedKeyring();
                                initializeBLESetUp();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,1);
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

    private void initializeBLESetUp()
    {

        if (tacsInterface != null)
        {
            tacsInterface.closeInterface();
        }
        tacsInterface = new TacsInterface.Builder(this, getActivity())
                .enableMockMode(useMockMode)
                .build();

        tacsInterface.logging().registerEventCallback(this);
        tacsInterface.logging().setLogLevel(LoggingInterface.LogLevel.DEBUG);

        if (tacsInterface.useAccessGrant(MY_MOCK_ACCESS_GRANT_ID, keyring))
        {
            //The key ring is loaded successfully
            Log.i(TAG, "Keyring loaded");

            if(mBluetoothAdapter.isEnabled())
            {
                //  btnBtDisConnect.setVisibility(View.GONE);
                //  btnBtConnect.setVisibility(View.VISIBLE);
                tacsInterface.searchAndConnect();
            }
        } else {
            //Invalid keyring
            Log.e(TAG, "Keyring invalid");
        }

        System.out.println("TacsInterface.ConnectionStatus:-" + TacsInterface.ConnectionStatus.UNAVAILABLE.name());
        System.out.println("KeyholderInterface.StatusCode:-" + KeyholderInterface.StatusCode.KEYHOLDER_NOT_DETECTED.name());
        System.out.println("No data");
    }

    private TacsKeyring getMockedKeyring()
    {
        Gson gson = new Gson();
        Type configType = new TypeToken<TacsKeyring>(){}.getType();
        try{
            System.out.println(tacsKeyring.toString());
            return gson.fromJson(tacsKeyring.toString(), configType);
        }catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return new TacsKeyring();
        }
    }

    void turnOnBluetoothDevice()
    {
        try {
            BluetoothAdapter BluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter();
            if (!BluetoothAdapter1.isEnabled())
            {
                BluetoothAdapter1.enable();
                // btnBtDisConnect.setVisibility(View.VISIBLE);
                // btnBtConnect.setVisibility(View.GONE);
                tacsInterface.searchAndConnect();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void turnOffBluetoothDevice()
    {
        BluetoothAdapter BluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        if (BluetoothAdapter2.isEnabled())
        {
            BluetoothAdapter2.disable();
            //  btnBtConnect.setVisibility(View.VISIBLE);
            //  btnBtDisConnect.setVisibility(View.GONE);
            tacsInterface.cancelConnection();
        }
    }


    @Override
    public void updateDeviceStatus(final TacsInterface.BluetoothDeviceStatus deviceStatus, final String errorMessage)
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    synchronized (mutex)
                    {
                        System.out.println("1"+deviceStatus.name());
                        switch (deviceStatus)
                        {
                            case DEVICE_AVAILABLE:
                                //
                                break;
                            case DEVICE_QUEUE_LIMIT_REACHED:
                                CustomToast.showToast(getActivity(), "Slow down, please!", 0);
                                //fallthrough
                                break;
                            case DEVICE_NOT_SUPPORTED:
                                CustomToast.showToast(getActivity(), "Device Not Supported!",0);
                                break;
                            case DEVICE_DEACTIVATED:
                                //
                                break;
                            case DEVICE_PERMISSION_ERROR:
                                // fallthrough
                                break;
                            case DEVICE_ERROR:
                                // fallthrough
                                break;
                            default:
                                //  System.out.println("3"+VehicleAccessInterface.DoorStatus.DOORS_UNKNOWN.name());
                                //  CustomToast.showToast(getActivity(), VehicleAccessInterface.DoorStatus.DOORS_UNKNOWN.name(), 0);
                                break;
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateVehicleStatus(final TacsInterface.VehicleStatus newStatus)
    {
        try {
            if (currentStatus != null && newStatus.getConnectionStatus().isConnected() && !currentStatus.getConnectionStatus().isConnected())
            {
                tacsInterface.vehicleAccess().requestCurrentVehicleStatus(); // Initial door and immobilizer status are highly useful to have
                tacsInterface.telematics().queryTelematicsData(DEFAULT_TELEMATICS_REQUEST); // Telematics may also be useful, depending on use case
            }
            currentStatus = newStatus;

            final Collection<TelematicsInterface.TelematicsData> td = newStatus.getTelematicsData();
            //   final String odometerStatus = formatTelematicsData(td, TelematicsInterface.TelematicsDataType.ODOMETER);

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        if (newStatus.getConnectionStatus().isError())
                        {
                            String message = newStatus.getErrorMessage();
                            message = message == null || message.isEmpty() ? "No details" : message;
                            CustomToast.showToast(getActivity(),newStatus.getConnectionStatus().name() + " : " + message, 0);
                            System.out.println(newStatus.getConnectionStatus().name() + " : " + message);
                        }
                        else {
                            switch (newStatus.getConnectionStatus())
                            {
                                case UNAVAILABLE:
                                    break;
                                case CONNECTED:
                                    CustomToast.showToast(getActivity(),newStatus.getConnectionStatus().name(), 0);
                                    System.out.println("4"+newStatus.getConnectionStatus().name());
                                    break;
                                case REMOVED:
                                    CustomToast.showToast(getActivity(),"DISCONNECTED", 0);
                                    System.out.println("5"+newStatus.getConnectionStatus().name());
                                    break;
                                default:
                                    break;
                            }
                            if(newStatus.getConnectionStatus().isConnected())
                            {
                                switch (newStatus.getDoorStatus())
                                {
                                    case DOORS_LOCKED:
                                        CustomToast.showToast(getActivity(), newStatus.getDoorStatus().name(), 0);
                                        System.out.println("6" + newStatus.getDoorStatus().name());
                                        break;
                                    case DOORS_UNLOCKED:
                                        CustomToast.showToast(getActivity(), newStatus.getDoorStatus().name(), 0);
                                        System.out.println("7" + newStatus.getDoorStatus().name());
                                        break;
                                    case DOORS_UNKNOWN:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void logEvent(@NonNull LoggingInterface.LogEvent logEvent)
    {

    }
    @Override
    public void onDestroy()
    {
        try {
            binding.mapView.onDestroy();
        } catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
        try {
            tacsInterface.closeInterface();

        } catch (Exception e){
            e.printStackTrace();
        }

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
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                /*NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Location_And_Key_to_Waiver_Signature);*/
                NavHostFragment.findNavController(Fragment_Location_And_Key.this).popBackStack();
                break;

            case R.id.discard:
                /*NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Waiver_Signature_to_Agreements);*/
                NavHostFragment.findNavController(Fragment_Location_And_Key.this)
                        .navigate(R.id.action_Location_And_Key_to_Self_check_Out,bundle);
                break;

        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap gMap) {
           try {
            /*   boolean success = gMap.setMapStyle(
                       MapStyleOptions.loadRawResourceStyle(
                               getActivity(), R.raw.style));

               if (!success) {
                   Log.e(TAG, "Style parsing failed.");
               }*/
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

             /*  googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                       new LatLng(Double.valueOf(locationListsData[i].AddressesModel.Latitude),Double.valueOf(locationListsData[i].AddressesModel.Longitude))
               ));*/

               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                    /*LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    getMapLocation();*/
                       getMapLocation();
                   }
               },1000);


             /*  googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                       new LatLng(Double.valueOf(locationListsData[i].AddressesModel.Latitude),Double.valueOf(locationListsData[i].AddressesModel.Longitude))
               ));*/

           } catch (Exception e){
               e.printStackTrace();
           }
    }

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

    public void getMapLocation(){
        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_markerpin2);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(-34, 151)
        ));

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position( new LatLng(-34, 151))
                //.title("Title")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_placeholder_2))
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_marker))
                .icon(markerIcon));
        // .snippet("City"));
      /*  CustomInfoWindowAdapter adapters = new CustomInfoWindowAdapter(getActivity());
        googleMap.setInfoWindowAdapter(adapters);*/
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        marker.showInfoWindow();

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

