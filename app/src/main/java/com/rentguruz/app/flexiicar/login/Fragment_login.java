package com.rentguruz.app.flexiicar.login;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rentguruz.app.SplashScreen;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentLoginBinding;
import com.google.gson.Gson;
import com.rentguruz.app.adapters.CustomResponse;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.flexiicar.booking2.Customer_Booking_Activity;
import com.rentguruz.app.home.Activity_Home;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.companyModel;
import com.rentguruz.app.model.display.Appcolor;
import com.rentguruz.app.model.display.HomeScreen;
import com.rentguruz.app.model.display.MobileBasicDetails;
import com.rentguruz.app.model.parameter.CommonParams;
import com.rentguruz.app.model.parameter.UserTypes;
import com.rentguruz.app.model.response.LoginModel;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.adapters.CustomPreference;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.LoginRes;
import com.rentguruz.app.flexiicar.booking.Booking_Activity;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.INITIATION;
import static com.rentguruz.app.apicall.ApiEndPoint.LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SPLASH;

import com.rentguruz.app.R;
public class Fragment_login extends BaseFragment
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
    //private DoHeader header;
    private LoginModel loginModel;
    private CustomPreference preference;
    //LoginRes loginRes;
    CustomResponse customResponse;
    OnResponseListener responseListener;
    FragmentLoginBinding binding;
    companyModel cm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //loginRes = new LoginRes(getContext());
        loginModel = new LoginModel();
        preference = new CustomPreference(getContext());
      /*  header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        //header.ut =  "PYOtYmuTsLQ=";
        header.ut =  "PYOtYmuTsLQ=";
        header.exptime = "7/20/2021 5:47:18 PM";
        header.islogin = "1";
        //header.mut = loginRes.getData("MUT");
        header.mut = loginRes.getData("MUT");*/
        cm = new companyModel();
        customResponse = new CustomResponse(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
        try {
            String data = sharedPreferences.getString("Data","");
//            JSONObject object = new JSONObject(data);

        } catch (Exception e){
            e.printStackTrace();
        }

        binding = FragmentLoginBinding.inflate(inflater,container,false);

        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

      //  Log.e("TAG", "onViewCreated: "+ Helper.getDateDisplay(DateType.ddMMyyyyD, "20-10-2021"));
        binding.setUiColor(UiColor);
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
                        //bodyParam.accumulate("CompanyId", UserData.companyModel.CompanyId);
                        //bodyParam.accumulate("IsActive", true);
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

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
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
                            loginRes.storedata("MUT",headers.get("MUT"));
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

                           // String  path = "?companyKey=" +"RTJvUVN2L0VxS2tVUXltMUl2eWlBRWdmOWxCMExUN294dTZCOGwwSHNFc3F2OTdFejJJWFdnPT0=";
                            String  path = "?companyKey=" +userDetail.CompanyKey;;

                            new ApiService(appInitialize, RequestType.GET, INITIATION, BASE_URL_LOGIN, header, path);

                           /*
                            cm = loginRes.getModelSystem(getResources().getString(R.string.scompany),companyModel.class);
                            cm.Id = userDetail.User.CompanyId;
                            Log.e(TAG, "run: " +  cm.Id );
                            loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(cm));
                            loginRes.storedata("CompanyId", String.valueOf(cm.Id));
                            Helper.di = 0;
                            UserData.companyModel = cm;

                            try {
                                UserData.companyModel = cm;
                                for (int i = 0; i < userDetail.CompanyPreference.BookingNoOfDaysValue.size(); i++) {
                                     if (userDetail.CompanyPreference.BookingNoOfDaysValue.get(i).Label.matches("MinDays")){
                                        UserData.companyModel.MinReservationDays = userDetail.CompanyPreference.BookingNoOfDaysValue.get(i).Value;
                                    }
                                }

                                for (int i = 0; i <userDetail.CompanyPreference.CustomerAgeLimitValue.size() ; i++) {
                                    if (userDetail.CompanyPreference.CustomerAgeLimitValue.get(i).Label.matches("MinAge")){
                                        UserData.companyModel.MinDOBAge = userDetail.CompanyPreference.CustomerAgeLimitValue.get(i).Value;
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            */


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

    @Override
    public void onClick(View v) {

    }

    OnResponseListener appInitialize = new OnResponseListener()
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
                        loginRes.storedata("Basic", response);
//                        data = loginRes.callFriend("Basic", ResponseBase.class);
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            Log.d("Mungara", "run: " + headers.toString());
                            Log.d("Mungara", "run: " + headers.get("mut"));
                            Log.d("Mungara", "run: " + headers.get("ut"));
                            Log.d("Mungara", "run: " + headers.get("token"));

                            loginRes.storedata("DataVersion", responseJSON.getString("DataVersion"));

                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            //JSONObject object = resultSet.getJSONObject("companyModel");
                            loginRes.storedata("MUT",resultSet.getString("MUT"));
                            header.mut = resultSet.getString("MUT");



                            //  cm = loginRes.getModel(object.toString(),companyModel.class);
                            cm = loginRes.getModel(resultSet.toString(), companyModel.class);
                            UserData.companyModel = cm;
                            // Log.d("TAG", "run: "+String.valueOf(cm.id)  +  "  " + cm.Id  );
                            //loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(cm));
                            loginRes.storedata("CompanyId", String.valueOf(cm.Id));
                            //BaseModel.CompanyId = cm.Id;
                            Helper.di = cm.Id;
                            Helper.currencySymbol = cm.CurrencySymbol;
                            Helper.currencyName = cm.DisplayCurrency;
                            Helper.displaycurrency = cm.DisplayCurrency;
                            Helper.fueel = cm.DistanceDesc;
                            Helper.fuel = cm.Distance;
                            Helper.fueltype = cm.DistanceDesc;
                            Helper.dateformat = cm.DefaultDateFormat;
                            params = new CommonParams();
                            header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
                            //header.ut =  "PYOtYmuTsLQ=";
                            header.ut =  "PYOtYmuTsLQ=";
                            header.exptime = "7/20/2021 5:47:18 PM";
                            header.islogin = "1";
                           /* new ApiService(new OnResponseListener() {
                                @Override
                                public void onSuccess(String response, HashMap<String, String> headers) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //   Log.e("TAG", "onSuccess: "+  response);
                                                MobileBasicDetails mobileBasicDetails = new MobileBasicDetails();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");

                                                if (status)
                                                {
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                    // Log.e("TAG", "run: " + resultSet.toString());
                                                    mobileBasicDetails =    loginRes.getModel(resultSet.toString(), MobileBasicDetails.class);
                                                    *//*ImageView icon = findViewById(R.id.logo);
                                                    CustomBindingAdapter.loadImage(icon,mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.logo), mobileBasicDetails.LogoAttachmentsModels.get(0).AttachmentPath);
                                                    loginRes.storedata(getResources().getString(R.string.icon), mobileBasicDetails.IconAttachmentsModels.get(0).AttachmentPath);*//*
                                                    JSONObject object = new JSONObject(mobileBasicDetails.DetailJson);
                                                    Appcolor appcolor = new Appcolor();
                                                    appcolor = loginRes.getModel(object.toString(),Appcolor.class);
                                                    UserData.UiColor.primary = appcolor.PrimaryColor;
                                                    UserData.UiColor.secondary = appcolor.SecondColor;
                                                    UserData.UiColor.primaryfont = appcolor.ThirdColor;
                                                    UserData.UiColor.secondaryfont = appcolor.SecondColor;
                                                    UserData.UiColor.additionalcolor = appcolor.AdditionalColor;
                                                    Log.e("TAG", "run: " + appcolor.AdditionalColor );
                                                    String colordata =  loginRes.modeltostring(appcolor);
                                                    loginRes.storedata(getResources().getString(R.string.Appcolor), colordata);
                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                                loginRes.storedata(getResources().getString(R.string.logo), "0");
                                                loginRes.storedata(getResources().getString(R.string.icon), "0");
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {

                                }
                            }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.MobileBasicDetails)));

                            new ApiService(new OnResponseListener() {
                                @Override
                                public void onSuccess(String response, HashMap<String, String> headers) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //  Log.e("TAG", "onSuccess: "+  response);
                                                //  Log.e("TAG", "onSuccess: "+  response);
                                                HomeScreen homeScreen = new HomeScreen();
                                                JSONObject responseJSON = new JSONObject(response);
                                                Boolean status = responseJSON.getBoolean("Status");
                                                //ImageView image = findViewById(R.id.splash);
                                                if (status){
                                                    JSONObject resultSet = responseJSON.getJSONObject("Data");
                                                    homeScreen =    loginRes.getModel(resultSet.toString(), HomeScreen.class);
                                                    //ImageView image =
                                                    //loginRes.storedata(getResources().getString(R.string.splashimage), homeScreen.AttachmentsModels.get(0).AttachmentPath);
                                                    //CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(0).AttachmentPath);

                                                    for (int i = 0; i <homeScreen.AttachmentsModels.size() ; i++) {

                                                       *//* if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmshome))){
                                                            loginRes.storedata(getResources().getString(R.string.cmshome), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                            CustomBindingAdapter.loadImage(image,homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                            // Log.e("TAG", "run: " + i + "  " +  homeScreen.AttachmentsModels.get(i).AttachmentPath );
                                                        }*//*

                                                      *//*  if (homeScreen.AttachmentsModels.get(i).AttachmentTypeName.equals(getResources().getString(R.string.cmsdash))){
                                                            loginRes.storedata(getResources().getString(R.string.cmsdash), homeScreen.AttachmentsModels.get(i).AttachmentPath);
                                                        }*//*
                                                    }

                                                }

                                            } catch (Exception e){
                                                e.printStackTrace();
                                                //  loginRes.storedata(getResources().getString(R.string.splashimage), "0");
                                                loginRes.storedata(getResources().getString(R.string.cmshome),"0");
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {

                                }
                            }, RequestType.POST, SPLASH, BASE_URL_LOGIN, header, params.getBasicDetail(getResources().getString(R.string.HomePageScreen)));*/

                            try {
                                UserData.companyModel = cm;
                                for (int i = 0; i < UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.size(); i++) {
                                    if (UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.get(i).Label.matches("MinDays")){
                                        UserData.companyModel.MinReservationDays = UserData.companyModel.CompanyPreference.BookingNoOfDaysValue.get(i).Value;
                                    }
                                }

                                for (int i = 0; i <UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.size() ; i++) {
                                    if (UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.get(i).Label.matches("MinAge")){
                                        UserData.companyModel.MinDOBAge = UserData.companyModel.CompanyPreference.CustomerAgeLimitValue.get(i).Value;
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            loginRes.storedata(getResources().getString(R.string.scompany)    ,loginRes.modeltostring(cm));
                            Helper.di = 0;
                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
            System.out.println("Error"+error);
        }
    };
}
