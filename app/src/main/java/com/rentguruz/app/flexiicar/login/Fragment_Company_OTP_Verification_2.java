package com.rentguruz.app.flexiicar.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCompanyOtpVerification2Binding;

import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SMSSENT;
import static com.rentguruz.app.apicall.ApiEndPoint.SMSVERIFY;

public class Fragment_Company_OTP_Verification_2 extends BaseFragment {

    FragmentCompanyOtpVerification2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompanyOtpVerification2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.footerbtn.setOnClickListener(this);

        /*binding.first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.first.getText().toString() != null) {
                    if (s.length() == 1) {
                        binding.second.requestFocus();
                    }
                }
            }
        });*/


        binding.first.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (binding.first.getText().length() == 1){
                    binding.second.requestFocus();
                }
                return false;
            }
        });

        textchange(binding.second,binding.third, binding.first);
        textchange(binding.third,binding.fourth, binding.second);


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footerbtn:
                int otp = Integer.valueOf(binding.first.getText().toString() + binding.second.getText().toString() + binding.third.getText().toString() + binding.fourth.getText().toString());

              //  Log.e(TAG, "onClick: "+  getArguments().getString("mobilenumber") + " : " + otp);
              //  NavHostFragment.findNavController(Fragment_Company_OTP_Verification_2.this).navigate(R.id.company_register_2_to_company_register_3, bundle);

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
                                        NavHostFragment.findNavController(Fragment_Company_OTP_Verification_2.this).navigate(R.id.company_register_2_to_company_register_3, bundle);
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
                }, RequestType.POST, SMSVERIFY, BASE_URL_LOGIN, header, params.getOtpVerify(getArguments().getString("mobilenumber"),otp));

                break;
        }
    }

    public void textchange(EditText current, EditText next, EditText previous){
        current.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (current.getText().length() == 1){
                    next.requestFocus();
                }
                return false;
            }
        });

        /*current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (current.getText().toString() != null ){
                    if (s.length() == 1 ){
                        next.requestFocus();
                    }  else {
                        previous.requestFocus();
                    }
                }

            }
        });*/
    };
}
