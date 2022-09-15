package com.rentguruz.app.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCustomerListBinding;
import com.rentguruz.app.databinding.RowCustomerlist2Binding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.UpdateDL;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CUSTOMERLIST;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONVALIDATION;

public class Fragment_Customer extends BaseFragment {


    FragmentCustomerListBinding binding;
    int CustomerTypeId=0;
    Customer[] customers;
    Handler handler = new Handler(Looper.getMainLooper());
    ReservationSummarry reservationSummarry;
    int page = 0;
    List<Customer> customerList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCustomerListBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        fullProgressbar.show();
        customerList = new ArrayList<>();



        try {
            bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
            bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
            bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
            bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
            bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            reservationSummarry = new ReservationSummarry();
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (Helper.rsvcustomerscan){
                Log.e(TAG, "onViewCreated: " + 1 );
                bundle.putSerializable("reservationSum", loginRes.getModelSystem("reservationSum",ReservationSummarry.class));
                bundle.putSerializable("Model", loginRes.getModelSystem("Model",VehicleModel.class));
                bundle.putSerializable("models",loginRes.getModelSystem("models",LocationList.class));
                bundle.putSerializable("model",loginRes.getModelSystem("model",LocationList.class));
                bundle.putSerializable("timemodel",loginRes.getModelSystem("timemodel",ReservationTimeModel.class));
                bundle.putString("pickupdate", loginRes.getData("pickupdate"));
                bundle.putString("dropdate", loginRes.getData("dropdate"));
                bundle.putString("droptime", loginRes.getData("droptime"));
                bundle.putString("pickuptime",  loginRes.getData("pickuptime"));
                reservationSummarry = new ReservationSummarry();
                reservationSummarry = loginRes.getModelSystem("reservationSum",ReservationSummarry.class);
                Log.e(TAG, "onViewCreated: " + reservationSummarry.ReservationNo );
                Helper.rsvcustomerscan = false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));
        Helper.defaultInsurance = true;
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Customer);
        binding.header.discard.setText(getResources().getString(R.string.add));
        binding.setUiColor(UiColor);
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerList.clear();
                binding.listItem.removeAllViews();
                new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(0, String.valueOf(s)));
            }
        });


        binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 100){
                    fullProgressbar.show();
                    new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));
                }
            }
        });

        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);

        try {
            UserData.updateDL = new UpdateDL();
            UserData.billingdetail = null;
            UserData.equipment = null;
            UserData.customerProfile = new CustomerProfile();
        } catch (Exception e){
            e.printStackTrace();
        }

   /*     Intent i = new Intent(getActivity(), Driver_Profile.class);
        startActivity(i);*/
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    OnResponseListener CustomerList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        binding.listItem.removeAllViews();
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        final JSONArray getReservationList = resultSet.getJSONArray("Data");
                        Customer[] customers;
                        customers = loginRes.getModel(getReservationList.toString(),Customer[].class);
                        for (int i = 0; i <customers.length ; i++) {
                            customerList.add(customers[i]);
                          /*  getSubview(i);
                            RowCustomerlist2Binding rowCustomerlist2Binding = RowCustomerlist2Binding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            rowCustomerlist2Binding.getRoot().setId(200 + i);
                            rowCustomerlist2Binding.getRoot().setLayoutParams(subparams);
                            rowCustomerlist2Binding.setCustomer(customers[i]);
                            binding.listItem.addView(rowCustomerlist2Binding.getRoot());

                            int finalI = i;
                            rowCustomerlist2Binding.getRoot().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    *//*loginRes.storedata("iddd", String.valueOf(customers[finalI].Id));
                                    Intent i = new Intent(getActivity(), User_Profile.class);
                                    startActivity(i);*//*

                                   *//* UserData.customer =customers[finalI];
                                    Helper.isCustomerVisible = true;
                                    UserData.loginResponse.User.UserFor = customers[finalI].Id;
                                    NavHostFragment.findNavController(Fragment_Customer.this).popBackStack();*//*

                                    reservationSummarry.CustomerId = customers[finalI].Id;
                                    reservationSummarry.CustomerEmail = customers[finalI].Email;
                                    reservationSummarry.CustomerPhone = customers[finalI].MobileNo;
                                    bundle.putSerializable("reservationSum", reservationSummarry);
                                    bundle.putSerializable("customer",customers[finalI]);
                                    NavHostFragment.findNavController(Fragment_Customer.this).navigate(R.id.customerlist_to_booking,bundle);
                                }
                            });*/

                        }
                        getCustomer();
                        fullProgressbar.hide();

                    } catch (Exception e){
                        e.printStackTrace();
                        fullProgressbar.hide();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            fullProgressbar.hide();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.discard:
                /*Intent i = new Intent(getActivity(), Customer_Profile.class);
                startActivity(i);*/
                Helper.rsvcustomerscan = true;
                loginRes.storedata("pickuptime",  getArguments().getString("pickuptime"));
                loginRes.storedata("droptime",  getArguments().getString("droptime"));
                loginRes.storedata("dropdate",  getArguments().getString("dropdate"));
                loginRes.storedata("pickupdate",  getArguments().getString("pickupdate"));
                loginRes.storedata("timemodel",  loginRes.modeltostring((ReservationTimeModel) getArguments().getSerializable("timemodel")));
                loginRes.storedata("model",  loginRes.modeltostring((LocationList) getArguments().getSerializable("model")));
                loginRes.storedata("models",  loginRes.modeltostring((LocationList) getArguments().getSerializable("models")));
                loginRes.storedata("Model",  loginRes.modeltostring((VehicleModel) getArguments().getSerializable("Model")));
                loginRes.storedata("reservationSum", loginRes.modeltostring((ReservationSummarry) getArguments().getSerializable("reservationSum")));
                Log.e(TAG, "onClick: " + "addcustomer" );
                NavHostFragment.findNavController(Fragment_Customer.this)
                        .navigate(R.id.action_CustomerList_to_CreateProfile);
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Customer.this)
                        .navigate(R.id.action_CustomerList_to_Agreement,bundle);

                break;
        }

    }

    public void getCustomer() {
        page += 1;
        for (int i = 0; i < customerList.size(); i++) {
            getSubview(i);
            RowCustomerlist2Binding rowCustomerlist2Binding = RowCustomerlist2Binding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            rowCustomerlist2Binding.getRoot().setId(200 + i);
            rowCustomerlist2Binding.getRoot().setLayoutParams(subparams);
            rowCustomerlist2Binding.setCustomer(customerList.get(i));
            binding.listItem.addView(rowCustomerlist2Binding.getRoot());
           rowCustomerlist2Binding.setUiColor(UiColor);
            int finalI = i;
            rowCustomerlist2Binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                    /*loginRes.storedata("iddd", String.valueOf(customers[finalI].Id));
                                    Intent i = new Intent(getActivity(), User_Profile.class);
                                    startActivity(i);*/

                                   /* UserData.customer =customers[finalI];
                                    Helper.isCustomerVisible = true;
                                    UserData.loginResponse.User.UserFor = customers[finalI].Id;
                                    NavHostFragment.findNavController(Fragment_Customer.this).popBackStack();*/

                    reservationSummarry.CustomerId = customerList.get(finalI).Id;
                    reservationSummarry.CustomerEmail = customerList.get(finalI).Email;
                    reservationSummarry.CustomerPhone =  customerList.get(finalI).MobileNo;
                    bundle.putSerializable("reservationSum", reservationSummarry);
                    bundle.putSerializable("customer", customerList.get(finalI));
                    //NavHostFragment.findNavController(Fragment_Customer.this).navigate(R.id.customerlist_to_booking,bundle);

                    new ApiService2<ReservationSummarry>(isReseravtionValid, RequestType.POST,RESERVATIONVALIDATION, BASE_URL_LOGIN,header,reservationSummarry);
                }
            });
        }
    }

    OnResponseListener isReseravtionValid = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status){
//                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            //  Log.e(TAG, "run: "+ resultSet );
                            NavHostFragment.findNavController(Fragment_Customer.this).navigate(R.id.customerlist_to_booking,bundle);
                        } else {
                            CustomToast.showToast(getActivity(),responseJSON.getString("Message"),1);
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
    };
}
