package com.abel.app.b2b.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.flexiicar.selfcheckout.Fragment_Vehicle_Image_1;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.CHANGEPASSWORD;

public class Fragment_Change_Password extends BaseFragment
{
    LinearLayout Save_Password;
    ImageView backarrowimg;
    //Handler handler = new Handler();
    public static Context context;
    public String CurrentPass = "",id="";
    EditText Edt_Currentpassword,edt_NewPassword,edt_ConfirmPassword;

   // LoginRes loginRes;
    LoginResponse loginResponse;
   // private DoHeader header;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        loginResponse = new LoginResponse();
        loginResponse = loginRes.callFriend("Data", LoginResponse.class);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_changepassword, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        backarrowimg = view.findViewById(R.id.backimg_changepass);
        Edt_Currentpassword = view.findViewById(R.id.edt_CurrentPass);
        edt_NewPassword = view.findViewById(R.id.edt_NewPass1);
        edt_ConfirmPassword = view.findViewById(R.id.edt_ConfirmPass1);
        Save_Password = view.findViewById(R.id.lbl_SavePassword);
        backarrowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  NavHostFragment.findNavController(Fragment_Change_Password.this)
                        .navigate(R.id.action_ChangePass_to_User_Details);*/
                //((User_Profile)getActivity()).BottomnavVisible();
                NavHostFragment.findNavController(Fragment_Change_Password.this).popBackStack();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");


        Save_Password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String password = edt_NewPassword.getText().toString();
                final String confirmPassword = edt_ConfirmPassword.getText().toString();

                if (Edt_Currentpassword.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter Your Current Password",Toast.LENGTH_LONG).show();
                }
                else if (edt_NewPassword.getText().toString().length() < 6 || edt_NewPassword.getText().toString().length() > 15)
                {
                    edt_NewPassword.requestFocus();
                    edt_NewPassword.setError("Password should be between 6 to 15 characters long");
                }
                else if (edt_ConfirmPassword.getText().toString().length() < 6 || edt_ConfirmPassword.getText().toString().length() > 15)
                {
                    edt_ConfirmPassword.requestFocus();
                    edt_ConfirmPassword.setError("Password should be between 6 to 15 characters long");
                }
                else
                {
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword))
                {
                    if (password.equals(confirmPassword)) {
                        JSONObject bodyParam = new JSONObject();
                        try {
                          /*  bodyParam.accumulate("CustomerId", Integer.parseInt(id));
                            bodyParam.accumulate("PasswordHash", edt_ConfirmPassword.getText().toString());
                            bodyParam.accumulate("CurrentPasswordHash", Edt_Currentpassword.getText().toString());
                            bodyParam.accumulate("Type", 1);*/
                            bodyParam.accumulate("UserId",loginResponse.apiUserLogin.UserId);
                            bodyParam.accumulate("Email",loginResponse.User.Email);
                            bodyParam.accumulate("CurrentPassword",Edt_Currentpassword.getText().toString());
                            bodyParam.accumulate("Password",edt_NewPassword.getText().toString());
                            System.out.println(bodyParam);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApiService ApiService = new ApiService(ChangePassword, RequestType.POST,
                                CHANGEPASSWORD, BASE_URL_CUSTOMER, header, bodyParam);

                    } else {
                        CustomToast.showToast(getActivity(), "Your Current Password And Confirmation Password Do Not Match", 1);
                    }
                }
                }
                if (Edt_Currentpassword.getText().toString().equals(""))
                {
                    CustomToast.showToast(getActivity(),"Please Enter Your Current Password",1);
                }
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_changepassword;
    }

    //REGISTRATION
    OnResponseListener ChangePassword = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);

                           /* NavHostFragment.findNavController(Fragment_Change_Password.this)
                                    .navigate(R.id.action_ChangePass_to_User_Details);*/
                            //((User_Profile)getActivity()).BottomnavVisible();
                            NavHostFragment.findNavController(Fragment_Change_Password.this).popBackStack();
                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    @Override
    public void onClick(View v) {

    }
}