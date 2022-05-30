package com.rentguruz.app.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentDrivingLicenseListBinding;
import com.rentguruz.app.databinding.ListDrivingLicenseBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.model.response.UpdateDL;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETLICENSEALL;

public class Fragment_Driving_License_List extends BaseFragment {
    public String id = "", cust_FName = "";
    ImageLoader imageLoader;
    LoginResponse loginResponse;
    CustomerProfile customerProfile;
    UpdateDL updateDL;
    FragmentDrivingLicenseListBinding binding;

    public static void initImageLoader(Context context) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  container.getContext().getDisplay().getName().toString();
        loginResponse = new LoginResponse();
        customerProfile = new CustomerProfile();
        updateDL = new UpdateDL();
        binding = FragmentDrivingLicenseListBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.fragment_driving_license_list, container, false);
        return binding.getRoot();
    }

    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
     //   ((User_Profile)getActivity()).BottomnavVisible();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getArguments().getInt("frag")!=2){
           // ((User_Profile)getActivity()).BottomnavVisible();
        }else {
            binding.primarydriverDetail.setVisibility(View.GONE);
        }

        try {
            ((User_Profile)getActivity()).BottomnavVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.header.screenHeader.setText(getResources().getString(R.string.driver) + " " + companyLabel.License);
        binding.header.discard.setText(getResources().getString(R.string.add));
        try {
            //  customerProfile = (CustomerProfile) getArguments().getSerializable("Driving");
            customerProfile = UserData.customerProfile;
            UserData.updateDL.MobileNo = UserData.customerProfile.MobileNo;
            UserData.updateDL.PhoneNo = UserData.customerProfile.MobileNo;
            updateDL = UserData.updateDL;
            binding.defaultDl.setDriver(updateDL);
            binding.progressCircular.setVisibility(View.VISIBLE);
            Log.d(TAG, "onViewCreated: " + updateDL.DrivingLicenceModel.ExpiryDate);
            loginResponse = loginRes.callFriend("Data", LoginResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initImageLoader(getActivity());

        imageLoader = ImageLoader.getInstance();
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.defaultDl.ViewDLDetails.setOnClickListener(this);
        bundle.putInt("frag",getArguments().getInt("frag"));
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
       /* AndroidNetworking.initialize(getActivity());
        Fragment_Driving_License_List.this.context = getActivity();*/
        loginResponse.User.UserFor =  UserData.loginResponse.User.UserFor;
       apiService = new ApiService(GetDrivingLicense, RequestType.POST,
                GETLICENSEALL, BASE_URL_LOGIN, header, params.getDrivingLicenseList(loginResponse.User.UserFor));


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

    public void showLicence(@NotNull List<UpdateDL> modelList) {
        for (int i = 0; i < modelList.size(); i++) {
            try {
               /* RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.setMargins(0, 10, 0, 0);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
                getSubview(i);
                ListDrivingLicenseBinding listDrivingLicenseBinding = ListDrivingLicenseBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false);
                modelList.get(i).Fname = modelList.get(i).FName;
                modelList.get(i).Lname = modelList.get(i).LName;
                listDrivingLicenseBinding.setDriver(modelList.get(i));
                int finalI = i;
                listDrivingLicenseBinding.ViewDLDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putSerializable("LicenseBundle", modelList.get(finalI));
                        bundle.putInt("frag",getArguments().getInt("frag"));
                        bundle.putString("netrate",getArguments().getString("netrate"));
                        bundle.putDouble("miles", getArguments().getDouble("miles"));
                        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
                        bundle.putString("dropdate", getArguments().getString("dropdate"));
                        bundle.putString("droptime", getArguments().getString("droptime"));
                        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
                        bundle.putSerializable("summarry",getArguments().getSerializable("summarry"));
                        bundle.putString("testSummerry",getArguments().getString("testSummerry"));
                        bundle.putSerializable("vechicle", getArguments().getSerializable("vechicle"));
                        bundle.putSerializable("pickuploc",getArguments().getSerializable("pickuploc"));
                        bundle.putSerializable("droploc",getArguments().getSerializable("droploc"));
                        bundle.putSerializable("ratemaster",getArguments().getSerializable("ratemaster"));
                        bundle.putSerializable("charges", getArguments().getSerializable("charges"));
                        bundle.putString("miles",getArguments().getString("miles"));
                        bundle.putBoolean("defaultDriver", false);
                        if (getArguments().getInt("frag")!=2){
                            NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                                    .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Update, bundle);
                        } else {
                            NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                                    .navigate(R.id.action_DrivingLicense_Details_to_Finalize_Rental, bundle);
                        }
                    }
                });
                listDrivingLicenseBinding.getRoot().setId(200 + i);
                listDrivingLicenseBinding.getRoot().setLayoutParams(subparams);
                binding.rlDrivingLicenceList.addView(listDrivingLicenseBinding.getRoot());

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                 if (Fragment_Driving_License_Add.screen) {
                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_User_Details);
                 } else {
                     NavHostFragment.findNavController(Fragment_Driving_License_List.this).popBackStack();
                 }
                break;

            case R.id.discard:
                bundle.putSerializable("Driving", customerProfile);
                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Add,bundle);
                break;

            case R.id.View_DL_Details:
                /*bundle.putSerializable("LicenseBundle", updateDL);
                bundle.putBoolean("defaultDriver", true);
                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Update,bundle);*/
                updateDL.FName = updateDL.Fname;
                updateDL.LName = updateDL.Lname;
                bundle.putSerializable("LicenseBundle", updateDL);
                bundle.putBoolean("defaultLic", true);
                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Update,bundle);
                break;
        }
    }

    OnResponseListener GetDrivingLicense = new OnResponseListener() {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");

                        final JSONArray getdrivingLicense = resultSet.getJSONArray("Data");

                        List<UpdateDL> drivingLicenceModels = new ArrayList<>();
                        int len;
                        len = getdrivingLicense.length();

                        for (int j = 0; j < len; j++) {
                            final JSONObject test = (JSONObject) getdrivingLicense.get(j);
                            loginRes.storedata("dirivingT", test.toString());
                            updateDL = loginRes.callFriend("dirivingT", UpdateDL.class);
                            drivingLicenceModels.add(updateDL);
                        }
                        showLicence(drivingLicenceModels);
                        binding.progressCircular.setVisibility(View.GONE);
                    } else {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(), errorString, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };
}
