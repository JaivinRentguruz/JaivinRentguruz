package com.abel.app.b2b.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentCustomerListBinding;
import com.abel.app.b2b.databinding.RowCustomerlist2Binding;
import com.abel.app.b2b.home.more.Fragment_CustomerList;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.CUSTOMERLIST;

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
        fullProgressbar.show();
        customerList = new ArrayList<>();
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
        new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));
        Helper.defaultInsurance = true;
        binding.BackToMoreInfo.setOnClickListener(this);
        binding.AddCustomer.setOnClickListener(this);
        binding.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Customer);
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
                fullProgressbar.show();
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

        binding.AddCustomer.setOnClickListener(this);
        binding.BackToMoreInfo.setOnClickListener(this);



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

            case R.id.Add_Customer:
                /*Intent i = new Intent(getActivity(), Customer_Profile.class);
                startActivity(i);*/

                NavHostFragment.findNavController(Fragment_Customer.this)
                        .navigate(R.id.action_CustomerList_to_CreateProfile);
                break;

            case R.id.BackToMoreInfo:
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
                    NavHostFragment.findNavController(Fragment_Customer.this).navigate(R.id.customerlist_to_booking,bundle);
                }
            });
        }
    }
}
