package com.abel.app.b2b.flexiicar.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomResponse;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentCustomerProfileBinding;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.home.more.Activity_MoreTab;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.flexiicar.booking.Booking_Activity;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.login.Login;
import com.abel.app.b2b.model.response.UpdateDL;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.abel.app.b2b.model.response.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETCUSTOMER;

public class Fragment_Customer_Profile extends BaseFragment
{
    TextView txtLogout,txtFName,txtLName,txtMobileNo,txtEmail,txt_OpenReservation,txt_OpenAgreement,txt_OpenTrafficticket,txt_AccountBalance;
    LinearLayout LayoutAgreements,LayoutReservation,Layoutusertimeline,Layoutbillsandaccount,
                 LayoutInsurancePolicy,LayoutDrivingLicsence,LayoutProfileDetails,LayoutCreditcard,LinearL_ChangePass,VehicleOnRent;
    ImageView backarrow,Profilepic;
    public String id = "";
    Handler handler = new Handler(Looper.getMainLooper());
    public static Context context;
    ImageLoader imageLoader;
    String serverpath="";
    SharedPreferences sp;
    String default_Email="",default_Password="";
    LinearLayout llCall, llEmail, llText;

    LoginResponse loginResponse;

    CustomerProfile customerProfile1;
    UpdateDL updateDL;
    CustomResponse customResponse;
   // private Dialog fullProgressbar;
    FragmentCustomerProfileBinding binding;
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        binding = FragmentCustomerProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, final Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //((User_Profile) getActivity()).BottomnavVisible();
        binding.setLabel(companyLabel);
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();
        loginResponse = new LoginResponse();
        loginResponse = loginRes.callFriend("Data", LoginResponse.class);
        loginResponse = loginRes.callFriend("UserData", LoginResponse.class);
        customerProfile1 = new CustomerProfile();
        updateDL = new UpdateDL();
        sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        serverpath = sp.getString("serverPath","");
        id = sp.getString(getString(R.string.id), "");
        default_Email=sp.getString("default_Email","");
        default_Password=sp.getString("default_Password","");
        fullProgressbar.show();
        /*try {
            fullProgressbar = new Dialog(getContext());
            fullProgressbar.setCancelable(false);
            fullProgressbar.requestWindowFeature(Window.FEATURE_NO_TITLE);
            fullProgressbar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            fullProgressbar.getWindow().setGravity(Gravity.CENTER);
            fullProgressbar.setContentView(R.layout.custom_progress);
            fullProgressbar.show();
        } catch (Exception e){
            e.printStackTrace();
        }*/

       /* customResponse = new CustomResponse(this);
        customResponse.UserLogin(context,default_Email,default_Password);*/

        try {
            Helper.AllowCustomerInsurance = true;
            if (Helper.AllowCustomerInsurance){
                binding.lblInsurancePolicy.setVisibility(View.GONE);
                binding.insuranceline.setVisibility(View.GONE);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        txtLogout=view.findViewById(R.id.txt_Logout);
        txtFName=view.findViewById(R.id.txt_FName);
        txtLName=view.findViewById(R.id.txt_LName);
        txtMobileNo=view.findViewById(R.id.txt_MobileNO);
        txtEmail=view.findViewById(R.id.txt_EMailAdd);
        Profilepic=view.findViewById(R.id.img_Profile);
        txt_AccountBalance=view.findViewById(R.id.txt_AccountBalance);
        txt_OpenReservation=view.findViewById(R.id.txt_openReservation);
        txt_OpenAgreement=view.findViewById(R.id.txt_openAgreement);
        txt_OpenTrafficticket=view.findViewById(R.id.txt_TrafficTicket);

        LayoutAgreements=view.findViewById(R.id.lbl_Agreements);
        LayoutReservation=view.findViewById(R.id.lbl_Reservation);
        VehicleOnRent=view.findViewById(R.id.vehicle_OnRent);
        Layoutusertimeline=view.findViewById(R.id.lblActivity_timeline);
        Layoutbillsandaccount=view.findViewById(R.id.lblaccount_statement);
        LayoutInsurancePolicy=view.findViewById(R.id.lbl_insurancePolicy);
        LayoutDrivingLicsence=view.findViewById(R.id.lbl_drivinglicense);
        LinearL_ChangePass=view.findViewById(R.id.LinearL_ChangePass);
        backarrow=view.findViewById(R.id.img_backarrow);

        llCall=view.findViewById(R.id.llCall);
        llEmail=view.findViewById(R.id.llEmail);
        llText=view.findViewById(R.id.llText);

        LayoutProfileDetails=view.findViewById(R.id.lbl_profileDetails);

        LayoutCreditcard=view.findViewById(R.id.lbl_creditcardAc);

     /*   if(Helper.isActiveCustomer){
            view.findViewById(R.id.bottom_line).setVisibility(View.GONE);
        }*/
      /*  if (Helper.isActiveCustomer){
            txtLogout.setText("Logout");
        }*/
        if (loginRes.getData(getResources().getString(R.string.userType)).equals("1")){
            txtLogout.setText("Logout");
        }

        txtLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (loginRes.getData(getResources().getString(R.string.userType)).equals("1")){
                     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Logout?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String msg = "You Have Been Successfully Logged Out!";
                                    CustomToast.showToast(getActivity(),msg,0);

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.apply();

                                    editor.putString("default_Email",default_Email);
                                    editor.putString("default_Password",default_Password);
                                    editor.commit();

                                    Intent i=new Intent (getActivity(), Login.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
                            builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Helper.isRegistrationDone = true;
                    Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Logout?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String msg = "You Have Been Successfully Logged Out!";
                                    CustomToast.showToast(getActivity(),msg,0);

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.apply();

                                    editor.putString("default_Email",default_Email);
                                    editor.putString("default_Password",default_Password);
                                    editor.commit();

                                    Intent i=new Intent (getActivity(), Login.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });
                            builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();*/
            }
        });

        LayoutAgreements.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_Agreements);
            }
        });

        LayoutReservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.onRent = false;
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_Reservation);
            }
        });
        VehicleOnRent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.onRent = true;
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_Reservation);
            }
        });
        Layoutusertimeline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_User_timeline);
            }
        });
        Layoutbillsandaccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putBoolean("StatmentType",true);
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_Bills_and_Payment, bundle);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                startActivity(i);*/
                Intent i;

             /*   if (Helper.isActiveCustomer) {
                    i = new Intent(getActivity(), Customer_Booking_Activity.class);
                } else {
                    Helper.isRegistrationDone = true;
                    i = new Intent(getActivity(), Activity_MoreTab.class);
                }*/

                if (loginRes.getData(getResources().getString(R.string.userType)).equals("1")){
                    i = new Intent(getActivity(), Customer_Booking_Activity.class);
                } else {
                    Helper.isRegistrationDone = true;
                    i = new Intent(getActivity(), Activity_MoreTab.class);
                }


                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        LayoutInsurancePolicy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("key",0);
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_InsurancePolicyList,bundle);
            }
        });
        LayoutDrivingLicsence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Driving", customerProfile1);
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_DrivingLicense_Details, bundle);
            }
        });
        LayoutCreditcard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("backTo",1);
                bundle.putSerializable("CreditCard", customerProfile1.CreditCardModel);
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_CardsOnAccount, bundle);
            }
        });
        LinearL_ChangePass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                        .navigate(R.id.action_User_Details_to_ChangePass);
            }
        });
        JSONObject object = new JSONObject();
        String bodyParam = "";
        List<Integer> ints = new ArrayList<>();
        JSONObject filter = new JSONObject();
        try {
            object.accumulate("limit", 10);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 10);
            ints.add(10);
            ints.add(20);
            ints.add(30);
            ints.add(40);
            ints.add(50);
            object.accumulate("pageLimits", ints);
            // filter.accumulate("CreditCardType", 3);
            filter.accumulate("CompanyId", loginResponse.User.CompanyId);
            filter.accumulate("UserFor", loginResponse.User.UserFor);
            filter.accumulate("CustomerTypeId", loginResponse.apiUserLogin.UserId);
            filter.accumulate("IsActive", true);
            object.accumulate("filterObj", filter);
            //  bodyParam+="?id="+loginResponse.User.UserFor+"&isActive=true";
            // bodyParam+="?id="+loginResponse.User.UserFor+"&isActive=true";
           /* if (Helper.isActiveCustomer) {
                bodyParam += "?id=" + UserData.loginResponse.User.UserFor + "&isActive=true"+"&"+"IsWithSummary=true";
                txtLogout.setText("Logout");
                ((User_Profile) getActivity()).BottomnavVisible();

               *//* RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  view.findViewById(R.id.lblcontinue1).getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.findViewById(R.id.lblcontinue1).setLayoutParams(params);*//*
            } else {
                loginResponse.User.UserFor = Integer.parseInt(loginRes.getData("iddd"));
            UserData.loginResponse.User.UserFor = Integer.parseInt(loginRes.getData("iddd"));
            bodyParam += "?id=" + loginResponse.User.UserFor + "&isActive=true"+"&"+"IsWithSummary=true";
            ((User_Profile) getActivity()).BottomnavInVisible();
            System.out.println(bodyParam);
            }*/

            Log.e(TAG, "onViewCreated: " +  loginRes.getData(getResources().getString(R.string.userType)));

            if (loginRes.getData(getResources().getString(R.string.userType)).equals("1")){
                UserData.loginResponse = loginResponse;
                bodyParam += "?id=" + UserData.loginResponse.User.UserFor + "&isActive=true"+"&"+"IsWithSummary=true";
                txtLogout.setText("Logout");
                Log.e(TAG, "onViewCreated: " +  loginRes.getData(getResources().getString(R.string.userType)));
                ((User_Profile) getActivity()).BottomnavVisible();
            } else {
                Log.e(TAG, "onViewCreated: " +  loginRes.getData(getResources().getString(R.string.userType)));
                loginResponse.User.UserFor = Integer.parseInt(loginRes.getData("iddd"));
                UserData.loginResponse.User.UserFor = Integer.parseInt(loginRes.getData("iddd"));
                bodyParam += "?id=" + loginResponse.User.UserFor + "&isActive=true"+"&"+"IsWithSummary=true";
              //  ((User_Profile) getActivity()).BottomnavInVisible();
                ((User_Profile) getActivity()).BottomnavVisible();
                System.out.println(bodyParam);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
           // fullProgressbar.hide();
        }

        AndroidNetworking.initialize(getActivity());
        Fragment_Customer_Profile.context = getActivity();

        ApiService apiService = new ApiService(GetCustomerProfile, RequestType.GET, GETCUSTOMER,BASE_URL_CUSTOMER, header,bodyParam);
       // ApiService apiService2 = new ApiService(GetCustomerProfile, RequestType.POST, GETCUSTOMERDETAIL,BASE_URL_CUSTOMER, header,object);

       /* Log.e(TAG, "onViewCreated: " + Build.VERSION_CODES.Q  + " : " + Build.VERSION.SDK_INT );
        if (Build.VERSION.SDK_INT <= 30){
            LinearLayout MainFragment = view.findViewById(R.id.layout_1);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
            params.setMargins(40, 80, 40, 0);
            MainFragment.setLayoutParams(params);
        }*/

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    //GetCustomerProfile
    OnResponseListener GetCustomerProfile = new OnResponseListener()
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
                            try
                            {
                               // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                loginRes.storedata("CustomerProfile", customerProfile.toString());
                                UserData.UserDetail = customerProfile.toString();
                                customerProfile1 =  loginRes.callFriend("CustomerProfile", CustomerProfile.class);
                                updateDL = loginRes.callFriend("CustomerProfile", UpdateDL.class);
                                UserData.customerProfile = customerProfile1;
                                UserData.updateDL=updateDL;
                                UserData.loginResponse.LogedInCustomer.AddressesModel = UserData.customerProfile.AddressesModel;

                                UserData.customer.FullName  = customerProfile1.FullName;
                                UserData.customer.MobileNo = customerProfile1.MobileNo;
                                UserData.customer.Email = customerProfile1.Email;

                                try {
                                    txt_AccountBalance.setText(Helper.getAmtount(0.0,true));
                                    txt_AccountBalance.setText(Helper.getAmtount(customerProfile1.CustomerSummary.Balance,true));
                                    txt_OpenReservation.setText(String.valueOf(customerProfile1.CustomerSummary.OpenReservation));
                                    txt_OpenAgreement.setText(String.valueOf(customerProfile1.CustomerSummary.OpenAgreement));
                                    txt_OpenTrafficticket.setText(String.valueOf(customerProfile1.CustomerSummary.TotalTrafficTickets));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }



                                try {
                                    if (UserData.updateDL.AttachmentsModel.AttachmentPath.trim().length() > 2) {
                                        binding.nameImage.setVisibility(View.VISIBLE);
                                        binding.nameText.setVisibility(View.GONE);
                                        String url = String.valueOf(UserData.updateDL.AttachmentsModel.AttachmentPath);
                                        Glide.with(getContext()).load(url).into(binding.userIcon);
                                    } else {
                                        binding.nameImage.setVisibility(View.GONE);
                                    }


                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                               /* final int customerId=customerProfile.getInt("customerId");
                                final String cust_FName=customerProfile.getString("cust_FName");
                                final String cust_LName=customerProfile.getString("cust_LName");
                                final String cust_Email=customerProfile.getString("cust_Email");
                                final String cust_MobileNo=customerProfile.getString("cust_MobileNo");
                                final String cust_Phoneno=customerProfile.getString("cust_Phoneno");
                                final String cust_Street=customerProfile.getString("cust_Street");
                                final String cust_UnitNo=customerProfile.getString("cust_UnitNo");
                                final String cust_City=customerProfile.getString("cust_City");
                                final int cust_State_ID=customerProfile.getInt("cust_State_ID");
                                final int cust_Country_ID=customerProfile.getInt("cust_Country_ID");
                                final String cust_Gender=customerProfile.getString("cust_Gender");
                                final String cust_ZipCode=customerProfile.getString("cust_ZipCode");
                                final String cust_DOB=customerProfile.getString("cust_DOB");
                                final String cust_Photo=customerProfile.getString("cust_Photo");*/

                           //     String mHtmlString = "&lt;p class=&quot;MsoNormal&quot; style=&quot;margin-bottom:10.5pt;text-align:justify;line-height: 10.5pt&quot;&gt;&lt;b&gt;&lt;span style=&quot;font-size: 8.5pt; font-family: Arial, sans-serif;&quot;&gt;Lorem Ipsum&lt;/span&gt;&lt;/b&gt;&lt;span style=&quot;font-size: 8.5pt; font-family: Arial, sans-serif;&quot;&gt;&amp;nbsp;is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.&lt;o:p&gt;&lt;/o:p&gt;&lt;/span&gt;&lt;/p&gt; &lt;p class=&quot;MsoNormal&quot; style=&quot;margin-bottom: 0.0001pt;&quot;&gt;&lt;span style=&quot;font-size: 8.5pt; font-family: Arial, sans-serif;&quot;&gt;&amp;nbsp;&lt;/span&gt;&lt;span style=&quot;font-family: Arial, sans-serif; font-size: 8.5pt; line-height: 10.5pt; text-align: justify;&quot;&gt;Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of &quot;de Finibus Bonorum et Malorum&quot; (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, &quot;Lorem ipsum dolor sit amet..&quot;, comes from a line in section 1.10.32.&lt;/span&gt;&lt;/p&gt;";
//                                txtFName.setText(Html.fromHtml(customerProfile1.AddressesModel.PreviewAddress));
                                txtFName.setText(customerProfile1.Fname);
                                txtLName.setText(customerProfile1.Lname);
                                txtMobileNo.setText(customerProfile1.MobileNo);
                                txtEmail.setText(customerProfile1.Email);
                                CustomBindingAdapter.camelcase(getView().findViewById(R.id.txt_FName), customerProfile1.FullName);
                                CustomBindingAdapter.caps(getView().findViewById(R.id.first),customerProfile1.Fname );
                                CustomBindingAdapter.caps(getView().findViewById(R.id.second),customerProfile1.Lname );

                                llCall.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+UserData.companyModel.ContactTelephone));
                                        startActivity(intent);
                                    }
                                });

                                llEmail.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                "mailto",UserData.companyModel.ContactEmailId, null));
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                    }
                                });

                                llText.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                            Uri  urimsg=Uri.parse("smsto:" + UserData.companyModel.ContactTelephone);
                                            Intent intent = new Intent(Intent.ACTION_SENDTO,urimsg);
                                            intent.putExtra("sms_body", "");
                                            startActivity(intent);
                                    }
                                });

                                //String url1 = serverpath+cust_Photo.substring(2);
                                //imageLoader.getInstance().displayImage(url1,Profilepic);

                                LayoutProfileDetails.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        /*Bundle CustomerBundle=new Bundle();
                                        CustomerBundle.putInt("customerId",customerId);
                                        CustomerBundle.putString("cust_FName",cust_FName);
                                        CustomerBundle.putString("cust_LName",cust_LName);
                                        CustomerBundle.putString("cust_Email",cust_Email);
                                        CustomerBundle.putString("cust_MobileNo",cust_MobileNo);
                                        CustomerBundle.putString("cust_Phoneno",cust_Phoneno);
                                        CustomerBundle.putString("cust_Street",cust_Street);
                                        CustomerBundle.putString("cust_UnitNo",cust_UnitNo);
                                        CustomerBundle.putString("cust_City",cust_City);
                                        CustomerBundle.putInt("cust_State_ID",cust_State_ID);
                                        CustomerBundle.putInt("cust_Country_ID",cust_Country_ID);
                                        CustomerBundle.putString("cust_Gender",cust_Gender);
                                        CustomerBundle.putString("cust_ZipCode",cust_ZipCode);
                                        CustomerBundle.putString("cust_DOB",cust_DOB);
                                        CustomerBundle.putString("cust_Photo",cust_Photo);
                                        Bundle Customer=new Bundle();
                                        Customer.putBundle("CustomerBundle", CustomerBundle);
                                        System.out.println(Customer);
                                        NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                                                .navigate(R.id.action_User_Details_to_Edit_Personal_info,Customer);*/
                                        Bundle Customer=new Bundle();
                                        Customer.putSerializable("CustomerBundle", customerProfile1);

                                        NavHostFragment.findNavController(Fragment_Customer_Profile.this)
                                                .navigate(R.id.action_User_Details_to_Edit_Personal_info,Customer);
                                    }
                                });

                                fullProgressbar.hide();
                            }
                            catch (Exception e)
                            {
                                fullProgressbar.hide();
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            fullProgressbar.hide();
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        fullProgressbar.hide();
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
            fullProgressbar.hide();
            System.out.println("Error-" + error);
        }
    };

    //GetCustomerSummary
    OnResponseListener GetCustomerSummary = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONObject customerProfile= resultSet.getJSONObject("customerSummary");

                                final String opened_Reservation=customerProfile.getString("opened_Reservation");
                                final String confirm_Reservation=customerProfile.getString("confirm_Reservation");
                                final String not_Show_Reservation=customerProfile.getString("not_Show_Reservation");
                                final String cancelled_Reservation=customerProfile.getString("cancelled_Reservation");
                                final String opened_Agreement=customerProfile.getString("opened_Agreement");
                                final String closed_Agreement=customerProfile.getString("closed_Agreement");
                                final String pending_Payment=customerProfile.getString("pending_Payment");
                                final String pending_Deposit=customerProfile.getString("pending_Deposit");
                                final String traffic_Ticket=customerProfile.getString("traffic_Ticket");
                                final String total_Revenue=customerProfile.getString("total_Revenue");

                                txt_OpenReservation.setText(opened_Reservation);
                                txt_OpenAgreement.setText(opened_Agreement);
                                txt_OpenTrafficticket.setText(traffic_Ticket);
                                txt_AccountBalance.setText(pending_Deposit);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String errorString = responseJSON.getString("message");
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
            System.out.println("Error-" + error);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
