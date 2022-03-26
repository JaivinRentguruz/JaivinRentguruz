package com.abel.app.b2b.flexiicar.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.google.gson.Gson;
import com.abel.app.b2b.adapters.CustomResponse;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.parameter.UserTypes;
import com.abel.app.b2b.model.response.LoginModel;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.flexiicar.booking.Booking_Activity;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.LOGIN;

public class Fragment_login extends Fragment
{
    public static Context context;
    ProgressDialog Loading;
    public String id = "", role = "";
    TextView txtRegister,txtforgetpassword,txt_GuestUser;
    TextView lbllogin;
    TextView login;
    ProgressBar progressBar;
    EditText editText_Username, editText_Password;
    Handler handler=new Handler(Looper.getMainLooper());
    CheckBox chksavepass;
    private DoHeader header;
    private LoginModel loginModel;
    private CustomPreference preference;
    LoginRes loginRes;
    CustomResponse customResponse;
    OnResponseListener responseListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        loginRes = new LoginRes(getContext());
        loginModel = new LoginModel();
        preference = new CustomPreference(getContext());
        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        //header.ut =  "PYOtYmuTsLQ=";
        header.ut =  "PYOtYmuTsLQ=";
        header.exptime = "7/20/2021 5:47:18 PM";
        header.islogin = "1";
        //header.mut = loginRes.getData("MUT");
        header.mut = loginRes.getData("MUT");
        customResponse = new CustomResponse(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
        try {
            String data = sharedPreferences.getString("Data","");
//            JSONObject object = new JSONObject(data);

        } catch (Exception e){
            e.printStackTrace();
        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

      //  Log.e("TAG", "onViewCreated: "+ Helper.getDateDisplay(DateType.ddMMyyyyD, "20-10-2021"));

        txtRegister=view.findViewById(R.id.registration);
        editText_Username = view.findViewById(R.id.emailid);
        editText_Password = view.findViewById(R.id.password);
        txtforgetpassword=view.findViewById(R.id.txt_forgetPassword);
        lbllogin = view.findViewById(R.id.lbllogin);
        chksavepass=view.findViewById(R.id.checkboxSavePass);
        chksavepass.setChecked(true);
        //txt_GuestUser=view.findViewById(R.id.txt_GuestUser);
        //login = view.findViewById(R.id.txtlogin);
        progressBar = view.findViewById(R.id.fullProgressbar);

        txtRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.RegistrationD= true;
                Intent i = new Intent(getActivity(), Driver_Profile.class);
                startActivity(i);
            }
        });

      //  login.setVisibility(View.VISIBLE);
      //  progressBar.setVisibility(View.GONE);

/*        txt_GuestUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Booking_Activity.class);
                startActivity(i);
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), Driver_Profile.class);
                startActivity(i);
            }
        });
        txtforgetpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_login.this)
                        .navigate(R.id.action_LoginFragment_to_Forgot_Password);
            }
        });*/

        ImageView icon =view.findViewById(R.id.logo);
        CustomBindingAdapter.loadImage(icon,loginRes.getData(getResources().getString(R.string.logo)));

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        String default_Email=sp.getString("default_Email","");
        String default_Password=sp.getString("default_Password","");


       /* default_Email = "nk@siimbaz.com";
        default_Password = "RGZv22021";*/

       // default_Email = "ecars@rentguruz.online";
       // default_Password = "ecars@2022";

        /*default_Email = "sample@siimbaz.com";
        default_Password = "123456789T";*/

        editText_Username.setText(default_Email);
        editText_Password.setText(default_Password);

        System.out.println("ID : " + id);
        System.out.println("Role : " + role);


        if (!id.equals("0") && id != null && !id.equals(null) && !id.equals("null") && role.equals("1"))
        {
            Intent i = new Intent(getActivity(), Booking_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        Fragment_login.context = getActivity();

        lbllogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
             //   login.setVisibility(View.GONE);
             //   progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                lbllogin.setVisibility(View.GONE);
                if (editText_Username.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter UserName",1);
                else if (editText_Password.getText().toString().equals(""))
                CustomToast.showToast(getActivity(),"Please Enter Password",1);

                else
                {
                    JSONObject bodyParam = new JSONObject();
                    try
                    {
                        bodyParam.accumulate("Email", editText_Username.getText().toString());
                        bodyParam.accumulate("Password", editText_Password.getText().toString());
                        bodyParam.accumulate("APIRequestType",2);
                        bodyParam.accumulate("CompanyId", UserData.companyModel.CompanyId);
                        bodyParam.accumulate("IsActive", true);
                   //     bodyParam.accumulate("CompanyId", 1);
                        System.out.println(bodyParam);

                        loginModel.Email = editText_Username.getText().toString();
                        loginModel.Password = editText_Password.getText().toString();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                   // customResponse.UserLogin(getContext(),editText_Username.getText().toString(), editText_Password.getText().toString());

                    ApiService ApiService = new ApiService(doLogin, RequestType.POST,
                            LOGIN, BASE_URL_LOGIN, header, bodyParam);

                   /* ApiService2<LoginModel> apiService2 = new ApiService2<LoginModel>(myLogin, RequestType.POST,
                            LOGIN, BASE_URL_LOGIN, header, loginModel, "Testing");*/

                }
            }
        });
    }
    OnResponseListener doLogin = new OnResponseListener()
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

                        Gson gson = new Gson();

                        if (status)
                        {
                            Log.d("Mungara", "run: " + headers.toString());
                            Log.d("Mungara", "run: " + headers.get("mut"));
                            Log.d("Mungara", "run: " + headers.get("ut"));
                            Log.d("Mungara", "run: " + headers.get("token"));
                            loginRes.storedata("UT",headers.get("ut"));
                            loginRes.storedata("TOKEN",headers.get("token"));

                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            preference.storeCountryState("Login", resultSet.toString());
                            LoginResponse userDetail = gson.fromJson(String.valueOf(resultSet), LoginResponse.class);
                            Log.d("Mungara", "run: " + userDetail.apiUserLogin.UserId);
                            UserData.loginResponse = userDetail;
                            //Helper.fueel = UserData.loginResponse.CompanyLabel.Miles;
                            preference.storeCountryState("Data", resultSet.toString());
                            preference.storeCountryState("UserData", resultSet.toString());
                            preference.storeCountryState("CompanyId", String.valueOf(userDetail.User.CompanyId));
                            //Integer.parseInt(loginRes.getData("CompanyId");

                            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sp.edit();
                            editor.putString(getString(R.string.id), String.valueOf(UserData.loginResponse.User.UserFor));
                            editor.putString("Data",resultSet.toString());

                            loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(userDetail));
                        /*    String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);*/

                            if (UserData.loginResponse.User.UserType == UserTypes.Customer.inte){
                                         Helper.isActiveCustomer = true;
                                loginRes.storedata(getResources().getString(R.string.userType), "1");
                                         Intent i = new Intent(getActivity(), Customer_Booking_Activity.class);
                                   //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         startActivity(i);
                            }  else {
                                loginRes.storedata(getResources().getString(R.string.userType), "2");
                                Intent i = new Intent(getActivity(), Activity_Home.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }

                           /* Intent i = new Intent(getActivity(), Booking_Activity.class);
                            //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);*/

                            if (chksavepass.isChecked())
                            {
                                editor.putString("default_Email",editText_Username.getText().toString());
                                editor.putString("default_Password",editText_Password.getText().toString());
                            }
                            else
                            {
                                editor.putString("default_Email","");
                                editor.putString("default_Password","");
                            }
                            editor.apply();
                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                            lbllogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                        // Loading.hide();
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
            lbllogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            System.out.println("Error"+error);
        }
    };

}
