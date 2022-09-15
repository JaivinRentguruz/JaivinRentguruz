package com.rentguruz.app.flexiicar.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCompanyOtpVerification1Binding;
import com.rentguruz.app.model.weather.CountryModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SMSSENT;

public class Fragment_Company_OTP_Verification_1 extends BaseFragment {

    FragmentCompanyOtpVerification1Binding binding;
    ArrayAdapter<String> adaptercustomer;
    CustomPreference preference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompanyOtpVerification1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.footerbtn.setOnClickListener(this);
        preference = new CustomPreference(getContext());
        Log.e(TAG, "onViewCreated: " + loginRes.getData("country"));

       /* Dexter.withContext(context).withPermissions(Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS)
                .withListener(new MultiplePermissionsListener() {


                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                            @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
                            Log.e(TAG, "onPermissionsChecked: " + mPhoneNumber );
                            if (mPhoneNumber!= null){
                                binding.tnumber.setText(mPhoneNumber);
                            }
                           *//* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                                @SuppressLint("MissingPermission") List<SubscriptionInfo>  subscription = SubscriptionManager.from(getContext()).getActiveSubscriptionInfoList();
                                for (int i = 0; i <subscription.size() ; i++) {
                                    SubscriptionInfo info = subscription.get(i);
                                    Log.e(TAG, "number " + info.getNumber());
                                    Log.e(TAG, "network name : " + info.getCarrierName());
                                    Log.e(TAG, "country iso " + info.getCountryIso());
                                }
                            }*//*

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).withErrorListener(dexterError -> {

        }).onSameThread().check();*/
        adaptercustomer = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, loginRes.getCountryName());
        binding.spCountrylist.setAdapter(adaptercustomer);
        binding.spCountrylist.setSelection(37);
        //preference.stateCountry(binding.spCountrylist, binding.state, "", "");

        binding.callingCountrycode.setText("+" + loginRes.getCountryModel(binding.spCountrylist.getSelectedItem().toString()).CallingCode);

        binding.spCountrylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.callingCountrycode.setText("+" + loginRes.getCountryModel(binding.spCountrylist.getSelectedItem().toString()).CallingCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.e(TAG, "onPermissionsChecked: " + mPhoneNumber );
            if (mPhoneNumber!= null){
                binding.tnumber.setText(mPhoneNumber);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        /*List<CountryModel> countryModelList = new ArrayList<>();
        countryModelList = loginRes.getCountryList();
        CountryModel countryModel = new CountryModel();
        List<CountryModel> contacts =
                stream(countryModelList)
                        .where(c -> Boolean.parseBoolean(c.Name.toString()))
                        .toList();

        Log.e(TAG, "onViewCreated: " +  contacts );*/



       // adaptercustomer = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, loginRes.getCountryName());
      //  binding.searchview.setThreshold(1);
      //  binding.searchview.setAdapter(adaptercustomer);
      //  binding.spCountrylist.setAdapter(adaptercustomer);
       // binding.spCountrylist.setSelection(10);



     /*   binding.searchview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.callingCountrycode.setText("+" + loginRes.getCountryModel(binding.searchview.getText().toString()).CallingCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 3) {
                    binding.callingCountrycode.setText("+" + loginRes.getCountryModel(binding.searchview.getText().toString()).CallingCode);
                }
            }
        });*/

        //binding.callingCountrycode.setText("+" + loginRes.getCountryModel(binding.searchview.getText().toString()).CallingCode);

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footerbtn:

               // bundle.putString("mobilenumber", binding.callingCountrycode.getText().toString()+binding.tnumber.getText().toString());
               // NavHostFragment.findNavController(Fragment_Company_OTP_Verification_1.this).navigate(R.id.company_register_1_to_company_register_2, bundle);


                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    if (status){
                                        bundle.putString("mobilenumber", binding.callingCountrycode.getText().toString()+binding.tnumber.getText().toString());
                                        NavHostFragment.findNavController(Fragment_Company_OTP_Verification_1.this).navigate(R.id.company_register_1_to_company_register_2, bundle);
                                    } else {
                                        CustomToast.showToast(getActivity(), responseJSON.getString("Message"),1);
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
                }, RequestType.POST, SMSSENT, BASE_URL_LOGIN, header, params.getOtp(binding.callingCountrycode.getText().toString()+binding.tnumber.getText().toString()));


        }
    }
}
