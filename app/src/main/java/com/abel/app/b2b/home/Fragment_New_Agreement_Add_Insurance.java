package com.abel.app.b2b.home;

import android.os.Bundle;
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
import com.abel.app.b2b.adapters.DateConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementAddInsuranceBinding;
import com.abel.app.b2b.flexiicar.user.Fragment_Add_Insurance_For_User;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.InsuranceCompanyDetailsModel;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.ADDINSURANCEDETAIL;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSURANCECOMPANYLIST;
import static com.abel.app.b2b.apicall.ApiEndPoint.UPDATEINSURANCE;

public class Fragment_New_Agreement_Add_Insurance extends BaseFragment {

    FragmentNewAgreementAddInsuranceBinding binding;
    Customer customer;

    public String[] InsuranceCompanyName, InsuranceEmail, InsuranceContactPerson;
    public int[] InsuranceId;
    int selected = 0;
    HashMap<String, Integer> getInsurance = new HashMap<>();
    public InsuranceModel insuranceModel;
    CustomeDialog dialog;

    InsuranceCompanyDetailsModel insuranceCompanyDetailsModel;
    DateConvert dateConvert= new DateConvert();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementAddInsuranceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText(getResources().getString(R.string.add) + " " + companyLabel.Insurance);
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer",(Customer) getArguments().getSerializable("customer"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        apiService = new ApiService(InsuranceList, RequestType.POST,
                INSURANCECOMPANYLIST, BASE_URL_LOGIN, header, params.getInsuranceCompanyList());
        customer = new Customer();
        customer = (Customer) getArguments().getSerializable("customer");
        binding.customer.setCustomer(customer);
        insuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();
        insuranceModel = new InsuranceModel();
        dialog = new CustomeDialog(context);
        binding.save.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.IpIssueDate.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        try {
            insuranceCompanyDetailsModel = (InsuranceCompanyDetailsModel)getArguments().getSerializable("insuranceData2");
            insuranceModel = (InsuranceModel)getArguments().getSerializable("insuranceData1");

            binding.insurancepolicy.setText(insuranceModel.PolicyNo);
            binding.edtExpiryDate.setText(dateConvert.allDateConverter(DateType.fulldate, insuranceModel.ExpiryDate,DateType.MMddyyyyS));
            binding.IpIssueDate.setText(dateConvert.allDateConverter(DateType.fulldate, insuranceModel.IssueDate,DateType.MMddyyyyS));
            binding.coverlimit.setText(String.valueOf(insuranceModel.CoverLimit));
            binding.deductible.setText(String.valueOf(insuranceModel.Deductible));

        } catch (Exception e){
            e.printStackTrace();
            insuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();
            insuranceModel = new InsuranceModel();
        }

        if (getArguments().getBoolean("screen")) {
            binding.text.setText("Update");
        } else {
            binding.text.setText(getResources().getString(R.string.save));
        }

        insuranceModel.InsuranceType = 3;

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_New_Agreement_Add_Insurance.this).popBackStack();
                break;

            case R.id.IpIssue_Date:
                dialog.getFullDate("", dialog.getToday(), string -> binding.IpIssueDate.setText(string));
                break;

            case R.id.edt_ExpiryDate:
                dialog.getFullDate(dialog.getToday(),"",string -> binding.edtExpiryDate.setText(string));
                break;

            case R.id.save:
                if (validation()) {

                    // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
                    // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                    insuranceModel.VerifiedBy = Helper.id;
                    insuranceModel.PolicyNo = binding.insurancepolicy.getText().toString();
                    insuranceModel.ExpiryDate = CustomeDialog.dateConvert(binding.edtExpiryDate.getText().toString());
                    insuranceModel.IssueDate = CustomeDialog.dateConvert(binding.IpIssueDate.getText().toString());
                    insuranceModel.InsuranceFor = customer.Id;
                    insuranceModel.GetCompanyDetail = true;
                    insuranceModel.InsuranceCompanyId = getInsurance.get(binding.spInsuranceCompList.getSelectedItem());
                    insuranceModel.CoverLimit = Integer.parseInt(binding.coverlimit.getText().toString());
                    insuranceModel.Deductible = Integer.parseInt(binding.deductible.getText().toString());

                    if (getArguments().getBoolean("screen")) {
                        new ApiService2<InsuranceModel>(AddInsuranceDetail,RequestType.PUT, UPDATEINSURANCE,BASE_URL_LOGIN, header, insuranceModel);
                    } else {
                        new ApiService2<>(AddInsuranceDetail, RequestType.POST, ADDINSURANCEDETAIL, BASE_URL_LOGIN, header, insuranceModel);
                    }



                }
                break;


        }

    }

    OnResponseListener InsuranceList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try
                {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    if (status)
                    {
                        JSONObject data = responseJSON.getJSONObject("Data");
                        final JSONArray resultSet = data.getJSONArray("Data");
                        int len;
                        len = resultSet.length();

                        InsuranceId = new int[len];
                        InsuranceCompanyName = new String[len];
                        InsuranceContactPerson = new String[len];
                        InsuranceEmail = new String[len];
                        for (int i = 0; i <len ; i++) {
                            final JSONObject test = (JSONObject) resultSet.get(i);
                            InsuranceCompanyName[i] = test.getString("Name");
                            InsuranceId[i] = test.getInt("Id");
                            InsuranceContactPerson[i] = test.getString("ContactName");
                            InsuranceEmail[i] = test.getString("Email");
                            getInsurance.put(InsuranceCompanyName[i], InsuranceId[i]);
                            if (InsuranceId[i]==UserData.insuranceModel.InsuranceCompanyId){
                                selected = i;
                            }
                        }
                        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, InsuranceCompanyName);
                        binding.spInsuranceCompList.setAdapter(adapterCategories);
                        binding.spInsuranceCompList.setSelection(selected);
                    }
                    else {
                        CustomToast.showToast(getActivity(),responseJSON.getString("Message"),1);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };


    OnResponseListener AddInsuranceDetail = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    if (status)
                    {
                        NavHostFragment.findNavController(Fragment_New_Agreement_Add_Insurance.this).popBackStack();
                    } else {
                        CustomToast.showToast(getActivity(), responseJSON.getString("Message"),1);
                    }


                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    public Boolean validation(){
        Boolean value = false;
        if (binding.insurancepolicy.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Ploicy Number", 1);
        else if (binding.edtExpiryDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Expiry Date", 1);
        else if (binding.IpIssueDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Issue Date", 1);
        else if (getInsurance.get(binding.spInsuranceCompList.getSelectedItem()).equals(""))
            CustomToast.showToast(getActivity(), "Please Select Insurance Company", 1);
        else if(binding.coverlimit.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Coverlimit", 1);
        else if(binding.deductible.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Deductible", 1);
        else {
            value = true;
        }
        return value;
    }
}
