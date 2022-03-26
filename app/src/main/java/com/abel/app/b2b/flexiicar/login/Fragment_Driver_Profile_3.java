package com.abel.app.b2b.flexiicar.login;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.model.DoRegistration;
import com.abel.app.b2b.model.UserModel;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;

import org.jetbrains.annotations.NotNull;

import javax.xml.validation.Validator;

public class Fragment_Driver_Profile_3 extends Fragment
{
    LinearLayout lblnext;
    ImageView backArrow;
    EditText edt_CustPhoneNo,edt_CustEmailId,edt_CustPassWord,edt_CustConformPass;
    CheckBox checkBoxTC, receiveEmail;
    Bundle RegistrationBundle;
    TextView txtDiscard;
    public static UserModel userModel;
    public static DoRegistration doRegistration;
    DoRegistration registration;


/*    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }*/

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void setInitialSavedState(@Nullable @org.jetbrains.annotations.Nullable SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        userModel = new UserModel();
        doRegistration = new DoRegistration();
        registration = new DoRegistration();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile_3, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblnext = view.findViewById(R.id.lblnext1);
        backArrow =view.findViewById(R.id.back);
        edt_CustPhoneNo=view.findViewById(R.id.edt_CustPhoneNo);
        edt_CustEmailId=view.findViewById(R.id.edt_CustEmailId);
        edt_CustPassWord=view.findViewById(R.id.edt_CustPassWord);
        edt_CustConformPass=view.findViewById(R.id.edt_CustConformPass);
        checkBoxTC=view.findViewById(R.id.CheckboxtTC);
        txtDiscard=view.findViewById(R.id.discard);
        receiveEmail = view.findViewById(R.id.receiveEmail);
        receiveEmail.setChecked(true);

        try {
            registration = (DoRegistration) getArguments().getSerializable("RegistrationBundle");
           // RegistrationBundle = getArguments().getBundle("RegistrationBundle");
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        lblnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {

                    if (edt_CustPhoneNo.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Mobile No.",1);
                    else if (edt_CustEmailId.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Email",1);
                    else if (!Patterns.EMAIL_ADDRESS.matcher(edt_CustEmailId.getText().toString()).matches())
                        CustomToast.showToast(getActivity(),"Please Enter Valid Email",1);
                    else if (edt_CustPassWord.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Password",1);
                    else if (edt_CustPassWord.getText().toString().length()<8)
                        CustomToast.showToast(getActivity(),"Please Enter Your Password more than 8 characters",1);
                    else if (edt_CustConformPass.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Confirm Password",1);
                    else {
                        final String password = edt_CustPassWord.getText().toString();
                        final String confirmPassword = edt_CustConformPass.getText().toString();

                            if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword))
                            {
                                if (password.equals(confirmPassword))
                                {
                                    if(checkBoxTC.isChecked())
                                    {
                                  /*  RegistrationBundle.putString("Cust_MobileNo", edt_CustPhoneNo.getText().toString());
                                    RegistrationBundle.putString("Cust_Email", edt_CustEmailId.getText().toString());
                                    RegistrationBundle.putString("PasswordHash", edt_CustConformPass.getText().toString());
                                    Bundle Registration = new Bundle();
                                    Registration.putBundle("RegistrationBundle", RegistrationBundle);
                                    System.out.println(Registration);*/

                                        doRegistration.Fname = Fragment_Driver_Profile_1.addressesModel.Fname;
                                        doRegistration.Lname = Fragment_Driver_Profile_1.addressesModel.Lname;
                                        doRegistration.Email =edt_CustEmailId.getText().toString();
                                        doRegistration.MobileNo = edt_CustPhoneNo.getText().toString();
                                      //  doRegistration.dob = Fragment_Driver_Profile_2.drivingLicenceModel.dob;
                                        doRegistration.DOB = Fragment_Driver_Profile_2.drivingLicenceModel.DOB;
                                        doRegistration.IsActive = true;
                                        doRegistration.IsDNDSMS = false;
                                        doRegistration.IsDNDEmail = false;
                                      //  doRegistration.CreditCardModel.IsActive = true;
                                        userModel.Email =  edt_CustEmailId.getText().toString();
                                        userModel.Password = edt_CustPassWord.getText().toString();
                                        userModel.UserName = Fragment_Driver_Profile_1.addressesModel.Drivername;

                                       // doRegistration.UserModel = userModel;
                                        registration.UserModel.Email = edt_CustEmailId.getText().toString();
                                        registration.UserModel.Password = edt_CustPassWord.getText().toString();
                                        registration.UserModel.UserName = registration.AddressesModel.Drivername;
                                        registration.UserModel.UserType = 3;

                                       // registration.dob = registration.DrivingLicenceModel.dob;
                                        registration.DOB = registration.DrivingLicenceModel.DOB;
                                        registration.MobileNo = edt_CustPhoneNo.getText().toString();
                                        registration.IsActive = true;
                                        registration.IsDNDSMS = false;
                                        registration.IsDNDEmail = false;
                                        registration.Email = edt_CustEmailId.getText().toString();

                                        Bundle Registration = new Bundle();
                                        Registration.putSerializable("RegistrationBundle", registration);

                                        NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                                                .navigate(R.id.action_DriverProfile3_to_DriverProfile4,Registration);
                                    }
                                    else
                                    {
                                        String msg = "Please accept term & condition";
                                        CustomToast.showToast(getActivity(),msg,1);
                                    }

                                } else
                                    {
                                        CustomToast.showToast(getActivity(),"Your password and confirmation password do not match",1);
                                    }
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
          /*      Bundle Registration = new Bundle();
                Registration.putSerializable("RegistrationBundle", doRegistration);
                System.out.println(Registration);
                NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                        .navigate(R.id.action_DriverProfile3_to_DriverProfile2,Registration);*/
                NavHostFragment.findNavController(Fragment_Driver_Profile_3.this).popBackStack();

            }
        });
        txtDiscard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                        .navigate(R.id.action_DriverProfile3_to_CreateProfile);
            }
        });
    }
}

