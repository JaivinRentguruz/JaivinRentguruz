package com.abel.app.b2b.flexiicar.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentCustomerListBinding;
import com.abel.app.b2b.databinding.RowCustomerlist2Binding;
import com.abel.app.b2b.flexiicar.user.User_Profile;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.base.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.CUSTOMERLIST;

public class Fragment_Customer extends BaseFragment {


    FragmentCustomerListBinding binding;
    int CustomerTypeId=0;
    Customer[] customers;
    Handler handler = new Handler(Looper.getMainLooper());

    int page = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCustomerListBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        new ApiService(CustomerList , RequestType.POST, CUSTOMERLIST,BASE_URL_CUSTOMER, header, params.getCustomerList(page));

        binding.BackToMoreInfo.setOnClickListener(this);
        binding.AddCustomer.setOnClickListener(this);

        binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });



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
                            getSubview(i);
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
                                    /*loginRes.storedata("iddd", String.valueOf(customers[finalI].Id));
                                    Intent i = new Intent(getActivity(), User_Profile.class);
                                    startActivity(i);*/

                                    UserData.customer =customers[finalI];
                                    Helper.isCustomerVisible = true;
                                    UserData.loginResponse.User.UserFor = customers[finalI].Id;
                                    NavHostFragment.findNavController(Fragment_Customer.this).popBackStack();

                                }
                            });

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

    @Override
    public void onClick(View v) {

    }
}
