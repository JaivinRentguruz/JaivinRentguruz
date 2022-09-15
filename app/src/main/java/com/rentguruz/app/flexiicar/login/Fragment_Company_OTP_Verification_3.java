package com.rentguruz.app.flexiicar.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCompanyOtpVerification3Binding;
import com.rentguruz.app.home.Activity_Home;

public class Fragment_Company_OTP_Verification_3 extends BaseFragment {

    FragmentCompanyOtpVerification3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompanyOtpVerification3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.footerbtn.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.footerbtn:
                Intent i = new Intent(getActivity(), Activity_Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                //NavHostFragment.findNavController(Fragment_Company_OTP_Verification_3.this).navigate(R.id.company_register_3_to_company_insert_1,bundle);
                break;
        }
    }
}
