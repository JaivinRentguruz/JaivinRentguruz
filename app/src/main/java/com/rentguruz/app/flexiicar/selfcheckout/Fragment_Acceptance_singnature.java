package com.rentguruz.app.flexiicar.selfcheckout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.model.response.ReservationSummarry;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAcceptanceSignatureBinding;
import com.rentguruz.app.model.ReservationSignatureModel;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.CommonParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADSIGNATURE;

public class Fragment_Acceptance_singnature extends BaseFragment {
    // LinearLayout lblNext;
    // ImageView imgback;
    // SignaturePad signaturePad;
    // TextView txtclear, txt_date;
    // CheckBox CheckBoxTC;
    //  Handler handler = new Handler();
    //  JSONArray ImageList = new JSONArray();
    JSONArray finaleImageList = new JSONArray();

    //  Bundle AgreementsBundle;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    // TextView txt_Savesign;
    public int RESULT_LOAD_IMAGE = 1;
    ProgressDialog progressDialog;

    //  CommonParams params;
    ReservationSignatureModel signatureModel;
    //  DoHeader header;
    Bundle bundle = new Bundle();
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    boolean GpsStatus;
    FragmentAcceptanceSignatureBinding binding;
    ReservationSummarry reservationSummarry = new ReservationSummarry();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        params = new CommonParams();
        signatureModel = new ReservationSignatureModel();
        binding = FragmentAcceptanceSignatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding.setUiColor(UiColor);
            bundle.putInt("Id", getArguments().getInt("Id"));
            Log.e("TAG", "RID " + getArguments().getInt("Id"));
            bundle.putSerializable("resrvation",getArguments().getSerializable("resrvation"));
            //bundle.putSerializable( "reservation",getArguments().getSerializable("resrvation"));
            bundle.putSerializable("reservation",getArguments().getSerializable("reservation"));
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("resrvation");
            bundle.putSerializable("signature",1);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            String datetime = dateformat.format(c.getTime());
            binding.txtDate.setText(datetime);

            binding.txtClearsign.setEnabled(false);
            binding.txtSavesign.setVisibility(View.INVISIBLE);
            binding.backimgAcceptanceSign.setOnClickListener(this);
            binding.lblbackAcceptanceSign.setOnClickListener(this);
            binding.txtClearsign.setOnClickListener(this);
            binding.txtSavesign.setOnClickListener(this);
            binding.termsconditions.setOnClickListener(this);

         /*   CheckGpsStatus();

            if (ContextCompat.checkSelfPermission(
                    getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION
                );
            } else {
                getCurrentLocation();
            }*/

         /*   if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }*/

            binding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {
                }

                @Override
                public void onSigned() {
                    binding.txtClearsign.setEnabled(true);
                    binding.signaturePad.setEnabled(true);
                }

                @Override
                public void onClear() {
                    binding.txtClearsign.setEnabled(false);
                    //signaturePad.setEnabled(false);
                }
            });

          /*  txt_Savesign.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(CheckBoxTC.isChecked()){
                    Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String img_strbase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    try {
                        JSONObject signObj = new JSONObject();
                        signObj.put("Doc_For", 22);
                        signObj.put("fileBase64",img_strbase64);

                        System.out.println(signObj);
                        ImageList.put(signObj);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    signatureModel.img64 = img_strbase64;

                    signatureModel.ReservationId = getArguments().getInt("Id");
                    signatureModel.SignBy = UserData.loginResponse.User.UserFor;
                    signatureModel.Action = 1;
                    signatureModel.SignType = 2;
                    signatureModel.Latitude = "22.3039";
                    signatureModel.Longitude = "70.8022";
                     ApiService2 apiService = new ApiService2(uploadSign, RequestType.POST, UPLOADSIGNATURE, BASE_URL_LOGIN,header,signatureModel);
                    } else {
                        CustomToast.showToast(getActivity(), getResources().getString(R.string.acceptterms), 1);
                    }
                    //apiService.UPLOAD_REQUEST(uploadSign,UPLOADSIGNATURE, params.getCheckoutSign(getArguments().getInt("Id"),img_strbase64));
                    //Toast.makeText(getActivity(),"Signature Saved",Toast.LENGTH_LONG).show();
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                signatureModel.Latitude = String.valueOf(lat);
                signatureModel.Longitude = String.valueOf(longi);
            } else {
                CustomToast.showToast(getActivity(), "Unable to find location.", 1);
            }
        }
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
                            signatureModel.Latitude = String.valueOf(locationResult.getLastLocation().getLatitude());
                            signatureModel.Longitude = String.valueOf(locationResult.getLastLocation().getLongitude());
                        }
                    }
                }, Looper.getMainLooper());

    }


    public void CheckGpsStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
            if (ContextCompat.checkSelfPermission(
                    getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION
                );
            } else {
                getCurrentLocation();
            }
        } else {
           //CheckGpsStatus();
        }
    }

    private void locationEnabled () {
        LocationManager lm = (LocationManager)
               getActivity().getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(getContext() )
                    .setMessage( "GPS Enable" )
                    .setPositiveButton( "Settings" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
        }
    }

    OnResponseListener uploadSign = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d("TAG", "onSuccess: " + response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status) {
                            JSONObject data = responseJSON.getJSONObject("Data");

                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(), msg, 0);
                            NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                   // .navigate(R.id.action_Acceptance_signature_to_Location_And_Key, bundle);
                                    .navigate(R.id.action_Acceptance_signature_to_Agreements, bundle);

                        } else {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(), msg, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d("TAG", "onError: " + error);
        }
    };

    //Updatecheckout
    OnResponseListener Updatecheckout = new OnResponseListener() {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status) {
                            if (binding.CheckBoxTC.isChecked()) {
                                String msg = responseJSON.getString("message");
                                CustomToast.showToast(getActivity(), msg, 0);

                             /*   AgreementsBundle.putString("ImageList", ImageList.toString());
                                Bundle Agreements = new Bundle();
                                Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                                System.out.println(Agreements);
                                NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                        .navigate(R.id.action_Acceptance_signature_to_Waiver_Signature, Agreements);*/
                            } else {
                                String msg = "Please accept term & condition";
                                CustomToast.showToast(getActivity(), msg, 1);
                            }
                        } else {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(), msg, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CustomToast.showToast(requireActivity(), "Permission Granted", 0);
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (locationGPS != null) {
                        double lat = locationGPS.getLatitude();
                        double longi = locationGPS.getLongitude();
                        signatureModel.Latitude = String.valueOf(lat);
                        signatureModel.Longitude = String.valueOf(longi);
                    } else {
                        CustomToast.showToast(getActivity(), "Unable to find location." , 1);
                    }
                }
                else
                {
                    CustomToast.showToast(getActivity(),"Permission Denied",1);
                }
            }
            break;
        }
    }*/

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.backimg_acceptance_sign:
                    NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                            .navigate(R.id.action_Acceptance_signature_to_Vehicle_Additional_image, bundle);
                    break;
                case R.id.lblback_acceptance_sign:

                case R.id.txt_Savesign:
                    if(binding.CheckBoxTC.isChecked()){
                        Bitmap signatureBitmap = binding.signaturePad.getSignatureBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String img_strbase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                   /*     try {
                            JSONObject signObj = new JSONObject();
                            signObj.put("Doc_For", 22);
                            signObj.put("fileBase64",img_strbase64);

                            System.out.println(signObj);
                            //ImageList.put(signObj);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                        signatureModel.img64 = img_strbase64;

                        signatureModel.ReservationId = getArguments().getInt("Id");
                        signatureModel.SignBy = reservationSummarry.CustomerId;
                        signatureModel.Action = 1;
                        signatureModel.SignType = 2;
                        signatureModel.Latitude = Fragment_Vehicle_Image_1.lat;
                        signatureModel.Longitude = Fragment_Vehicle_Image_1.log;
                        ApiService2 apiService = new ApiService2(uploadSign, RequestType.POST, UPLOADSIGNATURE, BASE_URL_LOGIN,header,signatureModel);
                    } else {
                        CustomToast.showToast(getActivity(), getResources().getString(R.string.acceptterms), 1);
                    }
                    break;

                case R.id.txt_clearsign:
                    binding.signaturePad.clear();
                    break;
                case R.id.termsconditions://     binding.termsconditions.setOnClickListener(this);
                    NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                            .navigate(R.id.action_Signature_to_Termscondition,bundle);
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
