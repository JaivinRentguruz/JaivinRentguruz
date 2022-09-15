package com.rentguruz.app.flexiicar.login;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCompanyInsert2Binding;
import com.rentguruz.app.model.company.CompanyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMPANYINSERT;
import static com.rentguruz.app.apicall.ApiEndPoint.LOGIN;

public class Fragment_Company_Insert_2 extends BaseFragment {

    private final int REQUEST_PLACE_ADDRESS = 40;
    FragmentCompanyInsert2Binding binding;
    CustomPreference preference;
    CompanyModel companyModel;
    private Geocoder geocoder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompanyInsert2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.footerbtn.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.streetname.setOnClickListener(this);
        preference = new CustomPreference(getContext());
        preference.stateCountry(binding.spCountrylist, binding.SpStatelist, "", "");
        companyModel = new CompanyModel();
        companyModel = (CompanyModel) getArguments().getSerializable("companyModel");
        MapsInitializer.initialize(this.getActivity());
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PLACE_ADDRESS && resultCode == Activity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            // Log.i(TAG, "Place city and postal code: " + place.getAddress().subSequence(place.getName().length(),place.getAddress().length()));
            try {
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String UnitNo = addresses.get(0).getFeatureName();
                    String Street = addresses.get(0).getThoroughfare();
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

                    binding.streetname.setText(Street);
                    binding.custCityName.setText(city);
                    binding.edtCustZipCode.setText(postalCode);
                    binding.edtCustUnitNo.setText(UnitNo);

                    preference.stateCountry(binding.spCountrylist, binding.SpStatelist, country, state);
                    companyModel.AddressesModel.Latitude = place.getLatLng().latitude;
                    companyModel.AddressesModel.Longitude = place.getLatLng().longitude;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footerbtn:
                if (validation()){
                    new ApiService2(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(() -> NavHostFragment.findNavController(Fragment_Company_Insert_2.this).navigate(R.id.company_insert_2_to_company_insert_completed, bundle));
                        }

                        @Override
                        public void onError(String error) {

                        }
                    }, RequestType.POST,
                            COMPANYINSERT, BASE_URL_LOGIN, header, getCompanyModel());



                }

                break;
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Company_Insert_2.this).popBackStack();
                break;

            case R.id.streetname:
                try {
                    if (!Places.isInitialized()) {
                        Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                    }
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                            Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
                            .build(getContext());
                    startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public Boolean validation(){
        Boolean value = false;
        if (binding.companyname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Company Name",1);
        else if (binding.streetname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Street NO & Name",1);
        else if (binding.spCountrylist.getSelectedItem().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Select Your Country",1);
        else if (binding.SpStatelist.getSelectedItem().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Select Your State",1);
        else if (binding.edtCustZipCode.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Pin/Zip Code",1);
        else if (binding.custCityName.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your City Name",1);
        else {
            value =true;
        }
        return value;
    }

    public CompanyModel getCompanyModel(){
        companyModel.Name = binding.companyname.getText().toString();
        companyModel.AddressesModel.Street = binding.streetname.getText().toString();
        companyModel.AddressesModel.UnitNo = binding.edtCustUnitNo.getText().toString();
        companyModel.AddressesModel.ZipCode = binding.edtCustZipCode.getText().toString();
        companyModel.AddressesModel.City = binding.custCityName.getText().toString();
        companyModel.AddressesModel.CountryId = preference.countryToCountryCode.get(binding.spCountrylist.getSelectedItem());
        companyModel.AddressesModel.StateId = preference.stateToSateCode.get(binding.SpStatelist.getSelectedItem());

        return companyModel;
    }
}
