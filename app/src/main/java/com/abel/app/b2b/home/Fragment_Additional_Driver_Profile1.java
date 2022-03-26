package com.abel.app.b2b.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAdditionalDriverProfile1Binding;
import com.abel.app.b2b.databinding.FragmentCreateAdditionalDriverBinding;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.UpdateDL;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETSINGLELICENCE;

public class Fragment_Additional_Driver_Profile1 extends BaseFragment {

    FragmentAdditionalDriverProfile1Binding binding;
    ArrayList<String> scanData;
    int statecode=0, countrycode=0;
    UpdateDL updateDL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdditionalDriverProfile1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanData = getActivity().getIntent().getStringArrayListExtra("scanData");
        try {
            ((Activity_Home) getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.lblnext11.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        updateDL = new UpdateDL();
        bundle.putBoolean ("updateLic" ,getArguments().getBoolean("updateLic"));
        if (getArguments().getBoolean("updateLic")){
            new ApiService(getLicence, RequestType.GET, GETSINGLELICENCE, BASE_URL_LOGIN, header, "?Id="+UserData.updateDL.Id);
        } else {
            try {
                binding.edtcustFullname.setText(UserData.customerProfile.Fname);
                binding.edtcustLname.setText(UserData.customerProfile.Lname);
                binding.edtCustStreet.setText(UserData.customerProfile.AddressesModel.Street);
                binding.edtCustUnitNo.setText(UserData.customerProfile.AddressesModel.UnitNo);
                binding.edtCustZipCode.setText(UserData.customerProfile.AddressesModel.ZipCode);
                binding.custCityName.setText(UserData.customerProfile.AddressesModel.City);
                preference.stateCountry(binding.spCountrylist, binding.SpStatelist, UserData.customerProfile.AddressesModel.CountryName,
                        UserData.customerProfile.AddressesModel.StateName);

            } catch (Exception e) {
                e.printStackTrace();
                preference.stateCountry(binding.spCountrylist, binding.SpStatelist, "", "");
            }
        }

        if(scanData != null)
        {
            for (String data : scanData)
            {

                String[] datas = data.split(":");

                if (datas[0].equals("Given Name"))
                    binding.edtcustFullname.setText(datas[1]);
                else if (datas[0].equals("Surname"))
                    binding.edtcustLname.setText(datas[1]);
                else if (datas[0].equals("Address Line 1"))
                    binding.edtCustStreet.setText(datas[1]);
                else if (datas[0].equals("Address City"))
                    binding.custCityName.setText(datas[1]);
                else if (datas[0].equals("Address Postal Code"))
                    binding.edtCustZipCode.setText(datas[1]);

            }
            try {

                countrycode = preference.stateToCountryCode.get(preference.getdata("Issuing State Name"));
                Log.d(TAG, "onViewCreated: " +"State Id : " + statecode + " : " +" Country Id : " + countrycode);
                preference.stateCountry(binding.spCountrylist,binding.SpStatelist, preference.codetoCountry.get(countrycode),preference.getdata("Issuing State Name"));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lblnext11:
                if (validation()) {
                    bundle.putSerializable("drivinglist", getModel());
                    NavHostFragment.findNavController(Fragment_Additional_Driver_Profile1.this).navigate(R.id.adddriver1_to_adddriver2, bundle);
                }
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Additional_Driver_Profile1.this).popBackStack();
                break;
        }
    }

    OnResponseListener getLicence = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d(TAG, "onSuccess: " + response);
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    fullProgressbar.hide();
                    if (status)
                    {
                        JSONObject data = responseJSON.getJSONObject("Data");
                        updateDL = loginRes.getModel(data.toString(), UpdateDL.class);


                       /* binding.edtExDate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.ExpiryDate));
                        binding.edtIssuedate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.IssueDate));
                        binding.CusDateofBirth.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.DOB));*/
                        binding.edtcustFullname.setText(updateDL.FName);
                        binding.edtcustLname.setText(updateDL.LName);
                        preference.stateCountry(binding.spCountrylist,binding.SpStatelist, updateDL.DrivingLicenceModel.IssueByCountryName,updateDL.DrivingLicenceModel.IssuedByStateName);
                        binding.edtCustStreet.setText(UserData.customerProfile.AddressesModel.Street);
                        binding.edtCustUnitNo.setText(UserData.customerProfile.AddressesModel.UnitNo);
                        binding.edtCustZipCode.setText(UserData.customerProfile.AddressesModel.ZipCode);
                        binding.custCityName.setText(UserData.customerProfile.AddressesModel.City);
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            });

        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    private Boolean validation(){
        Boolean value = false;
        if (binding.edtcustFullname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Driver's FirstName.",1);
        else if (binding.edtcustLname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Driver's LastName.",1);
        else if (binding.edtCustStreet.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Street NO & Name",1);
        else if (binding.spCountrylist.getSelectedItem().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Select Your Country",1);
        else if (binding.SpStatelist.getSelectedItem().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Select Your State",1);
        else if (binding.edtCustZipCode.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Pin/Zip Code",1);
        else if (binding.custCityName.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your City Name",1);
        else
        {
            value = true;
        }
        return value;
    }

    private UpdateDL getModel(){
        updateDL.IsActive = true;
        //updateDL.CustomerId = UserData.customerProfile.UserModel.UserFor;
        updateDL.CustomerId = UserData.customerProfile.Id;
        updateDL.GetWithDrivingLicence = true;
        updateDL.DrivingLicenceModel.LicenceType = 33;
        //updateDL.DrivingLicenceModel.LicenceFor = UserData.customerProfile.UserModel.UserFor;
        updateDL.DrivingLicenceModel.LicenceFor = UserData.customerProfile.Id;
        updateDL.DrivingLicenceModel.IssuedByStateName = binding.SpStatelist.getSelectedItem().toString();
        updateDL.DrivingLicenceModel.IssuedByState = preference.stateToSateCode.get(binding.SpStatelist.getSelectedItem());
        updateDL.DrivingLicenceModel.IssueByCountryName = binding.spCountrylist.getSelectedItem().toString();
        updateDL.DrivingLicenceModel.IssueByCountry = preference.countryToCountryCode.get(binding.spCountrylist.getSelectedItem());
        if (UserData.customerProfile.AddressesModel!= null)
        updateDL.AddressesModel = UserData.customerProfile.AddressesModel;
        updateDL.AddressesModel.StateName = binding.SpStatelist.getSelectedItem().toString();
        updateDL.AddressesModel.StateId = preference.stateToSateCode.get(binding.SpStatelist.getSelectedItem());
        updateDL.AddressesModel.CountryName = binding.spCountrylist.getSelectedItem().toString();
        updateDL.AddressesModel.CountryId = preference.countryToCountryCode.get(binding.spCountrylist.getSelectedItem());
        updateDL.AddressesModel.Street = binding.edtCustStreet.getText().toString();
        updateDL.AddressesModel.City = binding.custCityName.getText().toString();
        updateDL.AddressesModel.UnitNo = binding.edtCustUnitNo.getText().toString();
        updateDL.AddressesModel.ZipCode = binding.edtCustZipCode.getText().toString();
        updateDL.FName = binding.edtcustFullname.getText().toString();
        updateDL.LName = binding.edtcustLname.getText().toString();
        return updateDL;
    }
}
