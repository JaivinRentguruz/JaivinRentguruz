package com.rentguruz.app.flexiicar.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentDriverProfile1Binding;
import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapsInitializer;
import com.rentguruz.app.model.AddressesModel;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.DoRegistration;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.adapters.CustomToast;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import com.rentguruz.app.R;
public class Fragment_Driver_Profile_1 extends BaseFragment
        implements  GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
{
    private static final int RESULT_CANCELED = 0;
    //String TAG = "Fragment_Driver_Profile_1";
    LinearLayout lblnext;
    ImageView backarrow;
    //Handler handler = new Handler();
    public static Context context;
    public String id = "";
    Spinner spCountryList,spStateList;
    public String[] Country,State;
    public int[] CountyId,StateId;
    int statecode=0, countrycode=0;
    TextView txtDiscard,screenHeader;
    EditText edtcust_Fullname,edt_CustStreet,edt_CustUnitNo,edt_CustZipCode,edtcust_cityName,edtcust_Lname;
    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    ArrayList<String> scanData;
    //private DoHeader header;

    public static AddressesModel addressesModel;

    CustomPreference preference;
    DoRegistration registration;
    FragmentDriverProfile1Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       /* header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        Log.d("TAG", "onCreateView: " + header);*/
        addressesModel = new AddressesModel();
        addressesModel = Fragment_Create_Profile.registration.AddressesModel;
        preference = new CustomPreference(getContext());
        registration = new DoRegistration();

        UserData.customerProfile.Age = 15;
        UserData.UserDetail = "Main";

        Log.d(TAG, "onCreateView: " + preference.getdata("Issuing State Name")  + " : " + preference.getdata("Given Name")
                + " : " + preference.getdata("Birth Date")
                + " : " + preference.getdata("Issue Date")
                + " : " + preference.getdata("Expiration Date"));
        //return inflater.inflate(R.layout.fragment_driver_profile_1, container, false);
        binding = FragmentDriverProfile1Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        scanData = getActivity().getIntent().getStringArrayListExtra("scanData");
        try
        {
            super.onViewCreated(view, savedInstanceState);
            binding.setUiColor(UiColor);
            edtcust_Fullname = view.findViewById(R.id.edtcust_Fullname);
            edtcust_Lname = view.findViewById(R.id.edtcust_Lname);
            edt_CustStreet = view.findViewById(R.id.edt_CustStreet);
            edt_CustUnitNo = view.findViewById(R.id.edt_CustUnitNo);
            edt_CustZipCode = view.findViewById(R.id.edt_CustZipCode);
            edtcust_cityName = view.findViewById(R.id.cust_cityName);
          //  preference.getStringArray("state");
            lblnext = view.findViewById(R.id.lblnext11);
            backarrow= view.findViewById(R.id.back);
            screenHeader = view.findViewById(R.id.screenHeader);
            screenHeader.setText("");
            spCountryList = view.findViewById(R.id.sp_Countrylist);
            spStateList = view.findViewById(R.id.Sp_Statelist);

            txtDiscard=view.findViewById(R.id.discard);
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            if (addressesModel.CountryName == null) {
                preference.stateCountry(spCountryList, spStateList, "", "");
            } else {
                preference.stateCountry(spCountryList, spStateList, addressesModel.CountryName, addressesModel.StateName);
            }
         /*   ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1, preference.getStringArray("country"));
            spCountryList.setAdapter(adapterCategories);
            spCountryList.setSelection(0);


            spCountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                    Log.d(TAG, "onItemSelected: " + spCountryList.getSelectedItem());
                    String d = String.valueOf(spCountryList.getSelectedItem());
                    ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, preference.getstatename(d));
                    spStateList.setAdapter(adapterCategories2);
                    spStateList.setSelection(statecode);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spStateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                    Log.d(TAG, "onItemSelected: " + spStateList.getSelectedItem());
                    preference.countrycode(String.valueOf(spStateList.getSelectedItem()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
*/
            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Driver_Profile_1.this)
                            .navigate(R.id.action_DriverProfile_to_CreateProfile);
                }
            });

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Driver_Profile_1.this)
                            .navigate(R.id.action_DriverProfile_to_CreateProfile);
                }
            });
            MapsInitializer.initialize(this.getActivity());
            edt_CustStreet.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        if(!Places.isInitialized())
                        {
                            Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                        }
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                                Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
                                .build(getContext());
                        startActivityForResult(intent, REQUEST_PLACE_ADDRESS);

                  /*      Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(getActivity());
                        startActivityForResult(intent, REQUEST_PLACE_ADDRESS);*/
                    }
                /*    catch (GooglePlayServicesRepairableException e) {
                        Log.d("Place Autocomplete",e.getLocalizedMessage());
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Log.d("Place Autocomplete",e.getLocalizedMessage());
                    }*/
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            lblnext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        if (edtcust_Fullname.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Driver's FirstName.",1);
                        else if (edtcust_Lname.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Driver's LastName.",1);
                        else if (edt_CustStreet.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Street NO & Name",1);
                        else if (spCountryList.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your Country",1);
                        else if (spStateList.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your State",1);
                        else if (edt_CustZipCode.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Pin/Zip Code",1);
                        else if (edtcust_cityName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your City Name",1);
                        else
                        {
                       /*     Bundle RegistrationBundle=new Bundle();
                            RegistrationBundle.putString("Cust_FName",edtcust_Fullname.getText().toString());
                            RegistrationBundle.putInt("Cust_Country_ID",preference.countryToCountryCode.get(spCountryList.getSelectedItem()));
                            RegistrationBundle.putString("Cust_Country_Name",spCountryList.getSelectedItem().toString());
                            RegistrationBundle.putInt("Cust_State_ID",preference.stateToSateCode.get(spStateList.getSelectedItem()));
                            RegistrationBundle.putString("Cust_State_Name",spStateList.getSelectedItem().toString());
                            RegistrationBundle.putString("Cust_Street",edt_CustStreet.getText().toString());
                            RegistrationBundle.putString("Cust_UnitNo",edt_CustUnitNo.getText().toString());
                            RegistrationBundle.putString("Cust_ZipCode",edt_CustZipCode.getText().toString());
                            RegistrationBundle.putString("Cust_City",edtcust_cityName.getText().toString());*/



                            addressesModel.Fname = edtcust_Fullname.getText().toString();
                            addressesModel.Lname = edtcust_Lname.getText().toString();
                            addressesModel.Drivername = addressesModel.Fname + " " +addressesModel.Lname;
                            addressesModel.CountryId = preference.countryToCountryCode.get(spCountryList.getSelectedItem());
                            addressesModel.CountryName = spCountryList.getSelectedItem().toString();
                            addressesModel.StateId = preference.stateToSateCode.get(spStateList.getSelectedItem());
                            addressesModel.StateName = spStateList.getSelectedItem().toString();
                            addressesModel.Street = edt_CustStreet.getText().toString();
                            addressesModel.UnitNo = edt_CustUnitNo.getText().toString();
                            addressesModel.ZipCode = edt_CustZipCode.getText().toString();
                            addressesModel.City = edtcust_cityName.getText().toString();

                            registration.AddressesModel = addressesModel;
                            registration.Fname = edtcust_Fullname.getText().toString();
                            registration.Lname = edtcust_Lname.getText().toString();;

                            Bundle Registration=new Bundle();
                           // Registration.putBundle("RegistrationBundle",RegistrationBundle);
                            Registration.putSerializable("RegistrationBundle", registration);

                            System.out.println(Registration);
                            Fragment_Create_Profile.registration = registration;
                            NavHostFragment.findNavController(Fragment_Driver_Profile_1.this)
                                    .navigate(R.id.action_DriverProfile_to_DriverProfile2,Registration);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            AndroidNetworking.initialize(getActivity());
            Fragment_Driver_Profile_1.context = getActivity();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(scanData != null)
        {
            for (String data : scanData)
            {

                String[] datas = data.split(":");

                if (datas[0].equals("Given Name"))
                    edtcust_Fullname.setText(datas[1]);
                else if (datas[0].equals("Surname"))
                    edtcust_Lname.setText(datas[1]);
                else if (datas[0].equals("Address Line 1"))
                    edt_CustStreet.setText(datas[1]);
                else if (datas[0].equals("Address City"))
                    edtcust_cityName.setText(datas[1]);
                else if (datas[0].equals("Address Postal Code"))
                    edt_CustZipCode.setText(datas[1]);

            }
            try {

                countrycode = preference.stateToCountryCode.get(preference.getdata("Issuing State Name"));
                Log.d(TAG, "onViewCreated: " +"State Id : " + statecode + " : " +" Country Id : " + countrycode);
                preference.stateCountry(spCountryList,spStateList, preference.codetoCountry.get(countrycode),preference.getdata("Issuing State Name"));
              //  statecode = preference.stateToSateCode.get(preference.getdata("Issuing State Name"))-1;
           /*
                ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1, preference.getStringArray("country"));
                spCountryList.setAdapter(adapterCategories);
                spCountryList.setSelection(countrycode);
                Log.d(TAG, "onViewCreated: " + spCountryList.getSelectedItem());

                ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, preference.getstatename(spCountryList.getSelectedItem().toString()));
                statecode = preference.getDefaultstate(preference.getdata("Issuing State Name"));
                spStateList.setAdapter(adapterCategories2);
                spStateList.setSelection(statecode);
                Log.d(TAG, "onViewCreated: " + spStateList.getSelectedItem());*/
            }
                catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
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

                    edt_CustStreet.setText(Street);
                    edtcust_cityName.setText(city);
                    edt_CustZipCode.setText(postalCode);
                    edt_CustUnitNo.setText(UnitNo);

                    preference.stateCountry(spCountryList,spStateList, country,state);
                    addressesModel.Latitude =String.valueOf(place.getLatLng().latitude);
                    addressesModel.Longitude =String.valueOf(place.getLatLng().longitude);

                 /*   for(int k=0;k<State.length;k++)
                    {

                        if(State[k].equals(state))
                        {
                            spStateList.setSelection(k);
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
                    }*/

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                //setMarker(latLng);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onClick(View v) {

    }
}