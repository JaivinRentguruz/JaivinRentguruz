package com.rentguruz.app.flexiicar.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;

import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.FORGETPASSWORD;
import com.rentguruz.app.R;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentForgotPasswordBinding;

public class Fragment_Forgot_Password extends BaseFragment
{
    LinearLayout verify_layout;
    ImageView backarrowimg;
    EditText edt_Email;
    public String Cust_Email = "";
    //Handler handler = new Handler();
   // public static Context context;
    String bodyParam = "";
    FragmentForgotPasswordBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_forgot_password, container, false);
        binding = FragmentForgotPasswordBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        try {
            verify_layout = view.findViewById(R.id.lblverify);
            backarrowimg = view.findViewById(R.id.backimg_toLogin);
            edt_Email = view.findViewById(R.id.edt_forgetemail);
            backarrowimg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NavHostFragment.findNavController(Fragment_Forgot_Password.this)
                            .navigate(R.id.action_Forgot_Password_to_LoginFragment);
                }
            });

           /* AndroidNetworking.initialize(getActivity());
            Fragment_Forgot_Password.context = getActivity();*/

            verify_layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        if (edt_Email.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please enter a email",1);
                        else
                            {
                            bodyParam="Email="+edt_Email.getText().toString();
                                JSONObject object = new JSONObject();
                                object.accumulate("Email",edt_Email.getText().toString() );

                            new ApiService(ForgetPassword, RequestType.POST,
                                    FORGETPASSWORD, BASE_URL_CUSTOMER, header, object);


                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener ForgetPassword = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);

                            NavHostFragment.findNavController(Fragment_Forgot_Password.this)
                                    .navigate(R.id.action_Forgot_Password_to_LoginFragment);

                            /*NavHostFragment.findNavController(Fragment_Forgot_Password.this)
                                    .navigate(R.id.action_Forgot_Password_to_Reset_Password);*/
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
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
