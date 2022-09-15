package com.rentguruz.app.home.more;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCustomerListBinding;
import com.rentguruz.app.databinding.RowCustomerlist2Binding;
import com.rentguruz.app.flexiicar.user.User_Profile;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.CUSTOMERLIST;

public class Fragment_CustomerList extends BaseFragment {

    FragmentCustomerListBinding binding;
    int CustomerTypeId=0;
    Customer[] customers;
    //Handler handler = new Handler(Looper.getMainLooper());
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
        customerList = new ArrayList<>();
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.screenHeader.setText(companyLabel.Customer + " List" );
        binding.header.discard.setText(getResources().getString(R.string.add));
        binding.setUiColor(UiColor);
       // ((Activity_MoreTab) getActivity()).BottomnavInVisible();
        fullProgressbar.show();
        new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));



   /*     Intent i = new Intent(getActivity(), Driver_Profile.class);
        startActivity(i);*/

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
               // fullProgressbar.show();
                new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(0, String.valueOf(s)));
            }
        });



        binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 100){
                  // Log.e(TAG, "onScrollChange: " +  scrollY  + " "  + v.getChildAt(0).getMeasuredHeight() + " " + v.getMeasuredHeight() + " " + (v.getMeasuredHeight()-1000));
                   // fullProgressbar.show();
                    new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));
                }
            }
        });

        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
               /* NavHostFragment.findNavController(Fragment_CustomerList.this)
                        .navigate(R.id.action_CustomerList_to_MoreInfo);*/

               /* Intent i = new Intent(getActivity(),Activity_MoreTab.class);
                startActivity(i);*/

               // NavHostFragment.findNavController(Fragment_CustomerList.this).popBackStack();
                NavHostFragment.findNavController(Fragment_CustomerList.this)
                        .navigate(R.id.action_CustomerList_to_MoreInfo);

                break;


            case R.id.discard:
                /*Intent i = new Intent(getActivity(), Customer_Profile.class);
                startActivity(i);*/

                NavHostFragment.findNavController(Fragment_CustomerList.this)
                        .navigate(R.id.action_CustomerList_to_CreateProfile);
                break;
        }
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
                            String TotalRecord = resultSet.getString("TotalRecord");

                            final JSONArray getReservationList = resultSet.getJSONArray("Data");
                            Customer[] customers;
                            customers = loginRes.getModel(getReservationList.toString(),Customer[].class);
                            for (int i = 0; i <customers.length ; i++) {

                                customerList.add(customers[i]);
                                 /*   getSubview(i);
                                RowCustomerlist2Binding rowCustomerlist2Binding = RowCustomerlist2Binding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                rowCustomerlist2Binding.getRoot().setId(200 + i);
                                rowCustomerlist2Binding.getRoot().setLayoutParams(subparams);
                                rowCustomerlist2Binding.setCustomer(customers[i]);
                                binding.listItem.addView(rowCustomerlist2Binding.getRoot());
                                int finalI = i;

                                rowCustomerlist2Binding.call.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+ customers[finalI].MobileNo));
                                        startActivity(intent);
                                    }
                                });


                                rowCustomerlist2Binding.getRoot().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loginRes.storedata("iddd", String.valueOf(customers[finalI].Id));
                                        Intent i = new Intent(getActivity(), User_Profile.class);
                                        startActivity(i);
                                    }
                                });*/

                            }
                            getCustomer();
                            //getCustomer(page*1);
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


    public void getCustomer(){
        page += 1;

        for (int i = 0; i <customerList.size() ; i++) {
            getSubview(i);
            RowCustomerlist2Binding rowCustomerlist2Binding = RowCustomerlist2Binding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            rowCustomerlist2Binding.getRoot().setId(200 + i);
            rowCustomerlist2Binding.getRoot().setLayoutParams(subparams);
            rowCustomerlist2Binding.setUiColor(UiColor);
            rowCustomerlist2Binding.setCustomer(customerList.get(i));
            binding.listItem.addView(rowCustomerlist2Binding.getRoot());
            int finalI = i;

        /*    rowCustomerlist2Binding.call.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ customerList.get(finalI).MobileNo));
                startActivity(intent);
            });*/


            rowCustomerlist2Binding.getRoot().setOnClickListener(v -> {
                loginRes.storedata("iddd", String.valueOf(customerList.get(finalI).Id));
                UserData.loginResponse.User.UserFor = customerList.get(finalI).Id;
                Intent i1 = new Intent(getActivity(), User_Profile.class);
                startActivity(i1);
            });
        }

        fullProgressbar.hide();

    }

}
