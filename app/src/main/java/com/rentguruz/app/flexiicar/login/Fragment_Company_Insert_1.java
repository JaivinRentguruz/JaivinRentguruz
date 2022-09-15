package com.rentguruz.app.flexiicar.login;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCompanyInsert1Binding;
import com.rentguruz.app.model.company.CompanyModel;

public class Fragment_Company_Insert_1 extends BaseFragment {

    FragmentCompanyInsert1Binding binding;
    CompanyModel companyModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCompanyInsert1Binding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.footerbtn.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        companyModel = new CompanyModel();
    }
    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.footerbtn:
                if (validation()) {
                    bundle.putSerializable("companyModel", getCompanyModel());
                    NavHostFragment.findNavController(Fragment_Company_Insert_1.this).navigate(R.id.company_insert_1_to_company_insert_2, bundle);
                }
                break;
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Company_Insert_1.this).popBackStack();
                break;

        }
    }

    public Boolean validation(){
        Boolean value = false;
        if (binding.firstname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter First Name",1);
        else if (binding.lastname.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Last Name",1);
        else if (binding.email.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter EMAIL",1);
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches())
            CustomToast.showToast(getActivity(),"Please Enter Valid Email",1);
        else if (binding.telephone.getText().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Telephone",1);
        else if (binding.edtNewPass1.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Password",1);
        else if (binding.edtNewPass1.getText().toString().length()<8)
            CustomToast.showToast(getActivity(),"Please Enter Your Password more than 8 characters",1);
        else if (binding.edtNewPass2.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Confirm Password",1);
        else if (!binding.edtNewPass1.getText().toString().matches(binding.edtNewPass2.getText().toString()))
            CustomToast.showToast(getActivity(),"Please Enter Password and Confirm Password Not Match",1);
        else {
            value =true;
        }
        return value;
    }

    public CompanyModel getCompanyModel(){
        companyModel.EmailId = binding.email.getText().toString();
        companyModel.OwnerFirstName = binding.firstname.getText().toString();
        companyModel.OwnerLastName = binding.lastname.getText().toString();
        companyModel.OwnerEmailId = binding.email.getText().toString();
        companyModel.OwnerTelephone = binding.telephone.getText().toString();
        companyModel.IsActive = true;
        companyModel.Name = "BusinessName";
        companyModel.Telephone =binding.telephone.getText().toString();
        companyModel.TimeZoneId = 103;
        companyModel.BaseDefaultCurrencyId = 1;
        companyModel.IsSelfManaged = false;
        companyModel.HasPrimaryContact = false;
        companyModel.Entry4Booking = true;
        companyModel.Distance = 1;
        companyModel.DefaultDateFormat = 1;
        companyModel.FuelMeasurement = 1;
        companyModel.LanguageMasterId = 1;
        companyModel.AddressesModel.IsActive = true;
        companyModel.CurrencyAlias = "US$";
        return companyModel;
    }
}
