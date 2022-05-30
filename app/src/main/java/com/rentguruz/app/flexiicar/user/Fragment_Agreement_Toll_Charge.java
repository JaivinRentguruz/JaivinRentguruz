package com.rentguruz.app.flexiicar.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentTollChargeBinding;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.insert.TollCharges;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.TOLLCHARGE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLELIST;

public class Fragment_Agreement_Toll_Charge extends BaseFragment implements  GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    FragmentTollChargeBinding binding;
    private final int REQUEST_PLACE_ADDRESS = 40;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Geocoder geocoder;
    DropDown dropDownList;
    List<OnDropDownList> data = new ArrayList<>();
    Reservation reservations;
    ReservationSummarry reservationSummarry;
    TollCharges tollCharges;
    CustomeDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentTollChargeBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reservations = new Reservation();
        binding.setUiColor(UiColor);
        reservationSummarry = new ReservationSummarry();
        tollCharges = new TollCharges();
        dialog = new CustomeDialog(getActivity());
        binding.setLabel(companyLabel);
        binding.charge.setLabel(companyLabel);
        binding.header.screenHeader.setText("Toll Charge");
        binding.screenName.setText("Toll Charge");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.bottom.setOnClickListener(this);
        binding.transationdate.setOnClickListener(this);
        binding.entryplazadate.setOnClickListener(this);
        binding.exitplazadate.setOnClickListener(this);
        binding.image.setOnClickListener(this);
        try {
            dropDownList = (new DropDown(VEHICLELIST,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
            new ApiService2<DropDown>(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                OnDropDownList[] onDropDownLists;
                                List<String> strings = new ArrayList<>();
                                onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);
                                for (int i = 0; i < onDropDownLists.length; i++) {
                                    // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                    OnDropDownList onDropDownList = new OnDropDownList();
                                    onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                    data.add(onDropDownList);

                                    strings.add(onDropDownLists[i].Name);
                                }

                                //   getSpinner(binding.makeId,strings);
                                listSpinner(data);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList);

            reservations = (Reservation) getArguments().getSerializable("reservation");
            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");
            tollCharges.ReservationId = reservations.Id;
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.charge.surcharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0 & binding.charge.amount.getText().length()>0){

                }
               try {
                   int ss = Integer.valueOf(String.valueOf(s));
                   int amt = Integer.valueOf(binding.charge.amount.getText().toString());
                   int value = amt*ss/100;
                   binding.charge.netvalue.setText(String.valueOf(amt+ value));
                   tollCharges.TicketCharges = Double.valueOf(binding.charge.amount.getText().toString());
                   tollCharges.Surcharge = Double.valueOf(value);
                   tollCharges.TotalPayable = Double.valueOf(binding.charge.netvalue.getText().toString());
               } catch (Exception e){
                   e.printStackTrace();
               }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        //MapsInitializer.initialize(this.getActivity());
        binding.edtCustStreet.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_CustStreet:
                try
                {
                  /*  if(!Places.isInitialized())
                    {
                        Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                    }
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                            Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
                            .build(getContext());
                    startActivityForResult(intent, REQUEST_PLACE_ADDRESS);*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_Toll_Charge.this).popBackStack();
                break;

            case R.id.bottom:
                if (Validation()){
                    new ApiService2<TollCharges>(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responseJSON = new JSONObject(response);
                                        Boolean status = responseJSON.getBoolean("Status");
                                        final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                                        if (status) {
                                            NavHostFragment.findNavController(Fragment_Agreement_Toll_Charge.this).popBackStack();
                                        } else {
                                            CustomToast.showToast(getActivity(),responseJSON.getString("Message"),1);
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }

                        @Override
                        public void onError(String error) {

                        }
                    }, RequestType.POST, TOLLCHARGE, BASE_URL_LOGIN, header, getTollCharges());
                }
                break;

            case R.id.transationdate:
                dialog.getFullDate(string -> binding.transationdate.setText(string));
                break;

            case R.id.entryplazadate:
                dialog.getFullDate(string -> binding.entryplazadate.setText(string));
                break;

            case R.id.exitplazadate:
                dialog.getFullDate(string -> binding.exitplazadate.setText(string));
                break;

            case R.id.image:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
       // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_ADDRESS && resultCode == Activity.RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            // Log.i(TAG, "Place city and postal code: " + place.getAddress().subSequence(place.getName().length(),place.getAddress().length()));
            try {
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String UnitNo=addresses.get(0).getFeatureName();
                    String Street=addresses.get(0).getThoroughfare();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();

                    Log.e("Address: ", "" + address);
                    Log.e("City: ", "" + city);
                    Log.e("Street: ", "" + Street);
                    Log.e("State: ", "" + state);
                    Log.e("Country: ", "" + country);
                    Log.e("PostalCode: ", "" + postalCode);
                    Log.e("UnitNO: ", "" + UnitNo);

                    binding.edtCustStreet.setText(Street);
                    binding.custCityName.setText(city);
                    binding.edtCustZipCode.setText(postalCode);
                    binding.edtCustUnitNo.setText(UnitNo);

                    preference.stateCountry(binding.SpCountry,binding.SpState, country,state);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = getScaledBitmap(selectedImage,250,250);
            } catch (Exception e){
                e.printStackTrace();
            }
            binding.image.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    public void listSpinner(List<OnDropDownList> data){
        List<String> business = new ArrayList<>();
        int select = 0;
        for (int i = 0; i <data.size() ; i++) {
            business.add(data.get(i).Name);
            if (data.get(i).Id == reservationSummarry.ReservationVehicleModel.VehicleId){
                select = i;
            }
        }
        ArrayAdapter<String> adapterbusiness = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,business);
        binding.vehiclelist.setAdapter(adapterbusiness);
        binding.vehiclelist.setSelection(select);

    }

    public Boolean Validation(){
        Boolean value = false;
        if (binding.plate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter License Plate", 1);
        else if (binding.transationdate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Transation Date", 1);
        else if (binding.agency.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Agency Name", 1);
        else if (binding.entryplaza.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Entry Plaza", 1);
        else if (binding.exitlaza.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Exit Plaza", 1);
        else {
            value = true;
        }
        return value;
    }

    public TollCharges getTollCharges(){
        tollCharges.LicencePlate = binding.plate.getText().toString();
        tollCharges.Agency = binding.agency.getText().toString();
        tollCharges.EntryPlaza = binding.entryplaza.getText().toString();
        tollCharges.ExitPlaza = binding.exitlaza.getText().toString();
        //DateConvert.DateConverter(DateType.datetime, binding.transationdate.getText().toString(),DateType.fulldate);
        tollCharges.TransactionDate = DateConvert.DateConverter(DateType.datetime, binding.transationdate.getText().toString(),DateType.fulldate);
        tollCharges.EntryDateTime = DateConvert.DateConverter(DateType.datetime, binding.entryplazadate.getText().toString(),DateType.fulldate);
        tollCharges.ExitDateTime = DateConvert.DateConverter(DateType.datetime, binding.exitplazadate.getText().toString(),DateType.fulldate);
        tollCharges.VehicleId = reservationSummarry.ReservationVehicleModel.VehicleId;

        /*tollCharges.TicketCharges = Double.valueOf(binding.charge.amount.getText().toString());
        tollCharges.Surcharge = Double.valueOf(binding.charge.surcharge.getText().toString());
        tollCharges.TotalPayable = Double.valueOf(binding.charge.netvalue.getText().toString());*/
        tollCharges.Notes = binding.charge.note.getText().toString();
        return tollCharges;
    }


    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
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
}
