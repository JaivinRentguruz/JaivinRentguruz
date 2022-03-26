package com.abel.app.b2b.home;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAdditionalDriverProfile2Binding;
import com.abel.app.b2b.flexiicar.user.Fragment_Driving_License_Add;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.UpdateDL;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSERTLICENCE;
import static com.abel.app.b2b.apicall.ApiEndPoint.UPDATELICENCE;

public class Fragment_Additional_Driver_Profile2 extends BaseFragment {

    FragmentAdditionalDriverProfile2Binding binding;
    List<String> relation;
    ArrayList<String> scanData;
    UpdateDL updateDL;
    CustomeDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        relation = new ArrayList<>();
        binding = FragmentAdditionalDriverProfile2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.lblnextscreen.setOnClickListener(this);
        binding.edtExpiryDateDL.setOnClickListener(this);
        binding.CusDateofBirth.setOnClickListener(this);
        binding.ISSueDate.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        dialog = new CustomeDialog(getContext());
        updateDL = new UpdateDL();
        updateDL = (UpdateDL) getArguments().getSerializable("drivinglist");

        relation.add("Parent");
        relation.add("Child");
        relation.add("Spouse");
        relation.add("Sibling");
        relation.add("GrandParents");
        relation.add("GrandChild");
        relation.add("Friend");
        relation.add("Other");



        try {
            scanData = getActivity().getIntent().getStringArrayListExtra("scanData");
            if (scanData != null) {
                for (String data : scanData) {
                    String[] datas = data.split(":");
                    if (datas[0].equals("DD Number"))
                        binding.edtDriverLicenseNO.setText(datas[1]);
                    else if (datas[0].equals("Issue Date"))
                        binding.ISSueDate.setText(userDate(datas[1], "yyyy-MM-dd"));
                    else if (datas[0].equals("Expiration Date"))
                        binding.edtExpiryDateDL.setText(userDate(datas[1], "yyyy-MM-dd"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        preference.stateCountry(binding.SpCountry,binding.SpState, updateDL.DrivingLicenceModel.IssueByCountryName,
                updateDL.DrivingLicenceModel.IssuedByStateName);

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,relation);
        binding.edtRelationship.setAdapter(adapterCategories);
      //  binding.edtRelationship.setSelection(3);
        if (getArguments().getBoolean("updateLic"))
            binding.edtRelationship.setSelection(updateDL.RelationId, true);

        try {
            binding.edtExpiryDateDL.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.ExpiryDate));
            binding.ISSueDate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.IssueDate));
            binding.CusDateofBirth.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.DOB));
            binding.edtDriverLicenseNO.setText(updateDL.DrivingLicenceModel.Number);
            binding.edtEmail.setText(updateDL.Email);
            binding.edtTelephone.setText(updateDL.PhoneNo);
        } catch (Exception e){
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
            case R.id.lblnextscreen:
                if (validation()){
                    if (getArguments().getBoolean("updateLic")) {
                        new ApiService2<UpdateDL>(addLicence, RequestType.PUT,
                                UPDATELICENCE, BASE_URL_CUSTOMER,header,getModel());
                    }else {
                        new ApiService2<>(addLicence,
                                RequestType.POST, INSERTLICENCE, BASE_URL_CUSTOMER, header, getModel());
                    }
                }
                break;

            case R.id.Cus_DateofBirth:
                dialog.getMaxDate(dialog.getDOB(), "", string -> binding.CusDateofBirth.setText(string));
                break;

            case R.id.ISSueDate:
                dialog.getMinDate(dialog.getIssueDate(binding.CusDateofBirth.getText().toString()),dialog.getToday(),string -> binding.ISSueDate.setText(string));
                break;

            case R.id.edt_ExpiryDateDL:
                dialog.getFullDate(dialog.getToday(), "", string -> binding.edtExpiryDateDL.setText(string));
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Additional_Driver_Profile2.this).popBackStack();
                break;
        }
    }

    public String userDate(String timemills, String dateFormat) {
        timemills = timemills.replace("/Date(", "");
        timemills = timemills.replace(")/", "");
        Log.d("Mungara", "userDate: " + timemills);

        return DateFormat.format(dateFormat, Long.parseLong(timemills)).toString();
    }


    private boolean validation(){
        boolean value = false;
        if (binding.edtDriverLicenseNO.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Driving License NO.",1);
        else if (binding.CusDateofBirth.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Date Of Birth",1);
        else if (binding.ISSueDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter License Issue Date",1);
        else if (binding.edtExpiryDateDL.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Driving License Expiry Date",1);
        else if (binding.edtEmail.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter EmailId",1);
        else if (binding.edtTelephone.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Contact Number",1);
        else {
            value = true;
        }
        return value;
    }

    private UpdateDL getModel(){
        updateDL.RelationId = binding.edtRelationship.getSelectedItemPosition();
        updateDL.RelationDesc = binding.edtRelationship.getSelectedItem().toString();
        updateDL.DrivingLicenceModel.DOB = Helper.setPostDate(binding.CusDateofBirth.getText().toString());
        updateDL.DrivingLicenceModel.ExpiryDate = Helper.setPostDate(binding.edtExpiryDateDL.getText().toString());
        updateDL.DrivingLicenceModel.IssueDate = Helper.setPostDate(binding.ISSueDate.getText().toString());
        updateDL.DrivingLicenceModel.Number = binding.edtDriverLicenseNO.getText().toString();
        updateDL.DOB = Helper.setPostDate(binding.CusDateofBirth.getText().toString());
        updateDL.Email = binding.edtEmail.getText().toString();
        updateDL.PhoneNo = binding.edtTelephone.getText().toString();
        updateDL.MobileNo = binding.edtTelephone.getText().toString();

        return updateDL;
    }

    OnResponseListener addLicence = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        fullProgressbar.hide();
                        bundle.putSerializable("drivinglist", getModel());
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(), msg, 0);
                        NavHostFragment.findNavController(Fragment_Additional_Driver_Profile2.this).navigate(R.id.adddriver2_to_driverdetails,bundle);


                    } else {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(), msg, 1);
                        fullProgressbar.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };
}
