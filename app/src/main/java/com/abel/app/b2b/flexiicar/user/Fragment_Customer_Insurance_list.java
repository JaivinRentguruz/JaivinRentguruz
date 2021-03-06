package com.abel.app.b2b.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.DateConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentCustomerInsuranceListBinding;
import com.abel.app.b2b.databinding.ListCustomerInsuranceBinding;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.InsuranceCompanyDetailsModel;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.booking.Fragment_Payment_checkout;
import com.abel.app.b2b.model.parameter.DateType;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETINSURANCE;

public class Fragment_Customer_Insurance_list extends BaseFragment
{
   // Handler handler = new Handler();
   // public static Context context;
    public String id = "";
    ImageLoader imageLoader;
    String serverpath = "";
   // ImageView BackArrow;
   // TextView addInsurancePolicy;
   // private DoHeader header;
   // LoginRes loginRes;
    InsuranceModel insuranceModel;
    InsuranceCompanyDetailsModel insuranceCompanyDetailsModel;

    FragmentCustomerInsuranceListBinding binding;
   // ListCustomerInsuranceBinding listCustomerInsuranceBinding;
    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        insuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();
        insuranceModel = new InsuranceModel();
        binding = FragmentCustomerInsuranceListBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getInt("key") != 1) {
           // ((User_Profile) getActivity()).BottomnavVisible();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bundle.putInt("key", getArguments().getInt("key"));
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

       binding.backimgIP.setOnClickListener(this);
       binding.editInsurancePolicy.setOnClickListener(this);
       String  path = "?tableType=3&insuranceFor=" + UserData.loginResponse.User.UserFor;
       //String  path = "?tableType=3&insuranceFor=" + UserData.customer.Id;
       apiService = new ApiService(GetCustomerInsurance, RequestType.GET,
                GETINSURANCE+path, BASE_URL_CUSTOMER, header, new JSONObject());

        try {
            bundle.putSerializable("timemodel",getArguments().getSerializable("timemodel"));
            bundle.putSerializable("pickuploc", getArguments().getSerializable("pickuploc"));
            bundle.putSerializable("droploc", getArguments().getSerializable("droploc"));
            bundle.putSerializable("Model",getArguments().getSerializable("Model"));
            bundle.putSerializable("ratemaster", getArguments().getSerializable("ratemaster"));
            bundle.putSerializable("vechicle", getArguments().getSerializable("vechicle"));
            bundle.putString("netrate",getArguments().getString("netrate"));
            //bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("defaultcard",getArguments().getSerializable("defaultcard"));
            bundle.putInt("frag",2);
            bundle.putSerializable("summarry",getArguments().getSerializable("summarry") );
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("charges",getArguments().getSerializable("charges"));
            bundle.putSerializable("model",getArguments().getSerializable("model"));
            bundle.putSerializable("models", getArguments().getSerializable("models"));
            bundle.putString("DeliveryAndPickupModel", getArguments().getString("DeliveryAndPickupModel"));
            bundle.putSerializable("reservationSum",getArguments().getSerializable("reservationSum"));
            bundle.putString("insuranceOption",getArguments().getString("insuranceOption"));
            Log.d(TAG, "onViewCreated: " + TAG);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    //GetCustomerInsurance
    OnResponseListener GetCustomerInsurance = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        binding.editInsurancePolicy.setVisibility(View.GONE);
                   //     final JSONArray getcustomerInsurance = resultSet.getJSONArray("Data");
                        final RelativeLayout rlInsurancePolicy = getActivity().findViewById(R.id.rl_InsurancePolicyList);
                        loginRes.storedata("insurance", resultSet.toString() );
                        insuranceModel = loginRes.callFriend("insurance",InsuranceModel.class);
                        JSONObject jsonObject = resultSet.getJSONObject("InsuranceCompanyDetailsModel");
                        loginRes.storedata("insurance1", jsonObject.toString());
                        insuranceCompanyDetailsModel = loginRes.callFriend("insurance1", InsuranceCompanyDetailsModel.class);
                        UserData.insuranceModel = insuranceModel;
                        UserData.insuranceCompanyDetailsModel = insuranceCompanyDetailsModel;
                        getSubview(1);
                        ListCustomerInsuranceBinding listCustomerInsuranceBinding = ListCustomerInsuranceBinding.inflate(subinflater,
                                getActivity().findViewById(android.R.id.content), false);
                        listCustomerInsuranceBinding.getRoot().setId(200+1);
                        listCustomerInsuranceBinding.getRoot().setLayoutParams(subparams);
                       /* listCustomerInsuranceBinding.txtprimaryName.setText(UserData.customerProfile.FullName);
                        listCustomerInsuranceBinding.txtTelephoneNo.setText(UserData.customerProfile.MobileNo);
                        listCustomerInsuranceBinding.txtUserEmail.setText(UserData.customerProfile.Email);*/

                        CustomBindingAdapter.capss(listCustomerInsuranceBinding.namee, UserData.customer.FullName);
                        /*CustomBindingAdapter.caps(listCustomerInsuranceBinding.second, UserData.customer.Lname);*/

                        listCustomerInsuranceBinding.txtprimaryName.setText(UserData.customer.FullName);
                        listCustomerInsuranceBinding.txtTelephoneNo.setText(UserData.customer.MobileNo);
                        listCustomerInsuranceBinding.txtUserEmail.setText(UserData.customer.Email);

                        listCustomerInsuranceBinding.txtInsuranceCmpName.setText(insuranceCompanyDetailsModel.Name);
                        listCustomerInsuranceBinding.txtPolicyNoINs.setText(insuranceModel.PolicyNo);
                        //listCustomerInsuranceBinding.txtExpiryDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.ExpiryDate, DateType.ddMMyyyyS));
                        listCustomerInsuranceBinding.txtExpiryDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.ExpiryDate));
                        //listCustomerInsuranceBinding.txtIssueDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.IssueDate, DateType.ddMMyyyyS));
                        listCustomerInsuranceBinding.txtIssueDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.IssueDate));
                        listCustomerInsuranceBinding.llInsurancePolicy.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (getArguments().getInt("key") == 1){
                                    Helper.VISIBLE = true;
                                    NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).popBackStack();
                                } else {
                                Bundle InsurancePolicyBundle=new Bundle();
                                Bundle InsurancePolicy=new Bundle();
                                InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                        .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy,InsurancePolicy);
                                }
                            }
                        });
                        listCustomerInsuranceBinding.ViewInsDetails.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                    Bundle InsurancePolicyBundle = new Bundle();
                                    Bundle InsurancePolicy = new Bundle();
                                    InsurancePolicy.putInt("key",getArguments().getInt("key"));
                                    InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                    NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                            .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy, InsurancePolicy);
                            }
                        });
                        binding.rlInsurancePolicyList.addView(listCustomerInsuranceBinding.getRoot());

                       // final JSONArray getInsuranceDoc = resultSet.getJSONArray("t0050_Documents");

                        int len,len1;
                      //  len = getcustomerInsurance.length();
                      //  len1 = getInsuranceDoc.length();

                      /*  for (int j = 0; j < len; j++)
                        {
                            final JSONObject test = (JSONObject) getcustomerInsurance.get(j);

                            final int insurance_Cmp_ID = test.getInt("insurance_Cmp_ID");
                            final String insurance_Cmp_Name = test.getString("insurance_Cmp_Name");
                            final String policy_No = test.getString("policy_No");
                            final String expiryDate = test.getString("expiryDate");
                            final String insIssueDate = test.getString("insIssueDate");
                            final String imageFile = test.getString("imageList");

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            lp.setMargins(0, 10, 0, 0);

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_customer_insurance, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                            linearLayout.setId(200 + j);
                            linearLayout.setLayoutParams(lp);

                            final JSONArray docArray = new JSONArray();

                        *//*    for(int i = 0; i < len1; i++)
                            {
                                final JSONObject test1 = (JSONObject) getInsuranceDoc.get(i);

                                final String doc_Details = test1.getString("doc_Details");
                                final String doc_Name = test1.getString("doc_Name");

                                JSONObject docObj = new JSONObject();
                                docObj.put("doc_Name", doc_Name);
                                docObj.put("doc_Details", doc_Details);
                                docArray.put(docObj);
                            }*//*

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            Date date = dateFormat.parse(expiryDate);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String strexpiryDate = sdf.format(date);

                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            Date date1 = dateFormat1.parse(insIssueDate);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                            String strinsIssueDate = sdf1.format(date1);

                            TextView txtInsPolicyNo=linearLayout.findViewById(R.id.txt_policy_NoINs);
                            TextView txtinsurance_Cmp_Name=linearLayout.findViewById(R.id.txt_insurance_Cmp_Name);
                            TextView ExpiryDate=linearLayout.findViewById(R.id.txt_ExpiryDateIns);
                            TextView IssueDate=linearLayout.findViewById(R.id.txt_IssueDateIns);

                            TextView txtName=linearLayout.findViewById(R.id.txtprimaryName);
                            TextView txtTelephoneNo=linearLayout.findViewById(R.id.txtTelephoneNo);
                            TextView txtUserEmail=linearLayout.findViewById(R.id.txtUserEmail);

                            TextView View_InsDetails=linearLayout.findViewById(R.id.View_InsDetails);

                            SharedPreferences sp1 = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
                            String cust_FName = sp1.getString("cust_FName", "");
                            String cust_LName = sp1.getString("cust_LName", "");
                            String cust_Email = sp1.getString("cust_Email", "");
                            String cust_MobileNo = sp1.getString("cust_MobileNo", "");

                            txtName.setText(cust_FName+" "+cust_LName);
                            txtTelephoneNo.setText(cust_MobileNo);
                            txtUserEmail.setText(cust_Email);

                            LinearLayout View_InsurancePolicy=linearLayout.findViewById(R.id.llInsurancePolicy);

                            View_InsurancePolicy.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Bundle InsurancePolicyBundle=new Bundle();
                                    InsurancePolicyBundle.putString("insurance_Cmp_Name",insurance_Cmp_Name);
                                    InsurancePolicyBundle.putString("policy_No",policy_No);
                                    InsurancePolicyBundle.putString("expiryDate",expiryDate);
                                    InsurancePolicyBundle.putString("insIssueDate",insIssueDate);
                                    InsurancePolicyBundle.putString("docArray",docArray.toString());
                                    Bundle InsurancePolicy=new Bundle();
                                    InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                    NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                            .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy,InsurancePolicy);
                                }
                            });
                            View_InsDetails.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    Bundle InsurancePolicyBundle=new Bundle();
                                    InsurancePolicyBundle.putString("insurance_Cmp_Name",insurance_Cmp_Name);
                                    InsurancePolicyBundle.putString("policy_No",policy_No);
                                    InsurancePolicyBundle.putString("expiryDate",expiryDate);
                                    InsurancePolicyBundle.putString("insIssueDate",insIssueDate);
                                    InsurancePolicyBundle.putString("docArray",docArray.toString());
                                    Bundle InsurancePolicy=new Bundle();
                                    InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                    NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                            .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy,InsurancePolicy);
                                }
                            });
                            txtinsurance_Cmp_Name.setText(insurance_Cmp_Name);
                            txtInsPolicyNo.setText(policy_No);
                            ExpiryDate.setText(strexpiryDate);
                            IssueDate.setText(strinsIssueDate);

                            rlInsurancePolicy.addView(linearLayout);
                        }*/

                    } else {
                        String errorString = responseJSON.getString("Message");
                       // CustomToast.showToast(getActivity(),errorString,1);
                        NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).
                                navigate(R.id.action_InsurancePolicyList_to_AddInsurancePolicy,bundle);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
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
        switch (v.getId()){
            case R.id.backimg_IP:
               /* NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).
                        navigate(R.id.action_InsurancePolicyList_to_User_Details);*/
                NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).popBackStack();
                break;

            case R.id.edit_InsurancePolicy:
                NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).
                        navigate(R.id.action_InsurancePolicyList_to_AddInsurancePolicy,bundle);
                break;
        }
    }
}