package com.rentguruz.app.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementBillingInfoBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWN;
import static com.rentguruz.app.apicall.ApiEndPoint.CUSTOMERDDL;
import static com.rentguruz.app.apicall.ApiEndPoint.GETCUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.INSURANCECOMPANY;
import static com.rentguruz.app.apicall.ApiEndPoint.VENDOR;

public class Fragment_New_Agreement_Billing extends BaseFragment {

    FragmentNewAgreementBillingInfoBinding binding;
  //  List<DropDown> dropDownList = new ArrayList<>();
    List<OnDropDownList> data = new ArrayList<>();
    List<String> customerlist = new ArrayList<>();
    Customer customer;
    int idd,companyid;
    CustomerProfile customerProfile;
    ArrayAdapter<String> adaptercustomer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementBillingInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText("Add Billing Detail");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.setUiColor(UiColor);
        customer = new Customer();
        companyid = Integer.parseInt(loginRes.getData("CompanyId"));
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models", (LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model", (LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel", (ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer", (Customer) getArguments().getSerializable("customer"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime", getArguments().getString("pickuptime"));

        customer = (Customer) getArguments().getSerializable("customer");
        customerProfile = new CustomerProfile();

        binding.name.setText( customer.FullName);
        binding.number.setText(customer.MobileNo);
        binding.email.setText(customerProfile.Email);
        //binding.address.setText(Html.fromHtml(customer.));

        //binding.edit.setOnClickListener(this);


        List<String> entitylist = new ArrayList<>();
        entitylist.add("Customer");
        entitylist.add("Corporate");
        entitylist.add("Insurance");
        entitylist.add("Vendor");

        ArrayAdapter<String> adapterentity = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, entitylist);
        binding.entity.setAdapter(adapterentity);

        binding.entity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.clear();
                customerlist.clear();
                List<DropDown> dropDownList = new ArrayList<>();
                if (binding.entity.getSelectedItem().toString().equals("Customer")) {
                    dropDownList.add(new DropDown(CUSTOMERDDL, companyid, true, false));
                } else if (binding.entity.getSelectedItem().toString().equals("Vendor")) {
                    dropDownList.add(new DropDown(VENDOR, companyid, true, false));
                } else if (binding.entity.getSelectedItem().toString().equals("Insurance")) {
                    dropDownList.add(new DropDown(INSURANCECOMPANY, companyid, true, false));
                }

                new ApiService2<>(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(() -> {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                OnDropDownList[] onDropDownLists;
                                List<String> strings = new ArrayList<>();
                                onDropDownLists = loginRes.getModel(getReservationList.toString(), OnDropDownList[].class);
                                for (int i = 0; i < onDropDownLists.length; i++) {
                                    OnDropDownList onDropDownList = new OnDropDownList();
                                    onDropDownList = loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                    data.add(onDropDownList);
                                    strings.add(onDropDownLists[i].Name);
                                }
                                //   getSpinner(binding.makeId,strings);
                                listSpinner(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.POST, COMMONDROPDOWN, BASE_URL_LOGIN, header, dropDownList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                NavHostFragment.findNavController(Fragment_New_Agreement_Billing.this).navigate(R.id.billinginfo_to_billing_details);
                break;

            case R.id.save:
                UserData.billingdetail = binding.name.getText().toString();
                NavHostFragment.findNavController(Fragment_New_Agreement_Billing.this).navigate(R.id.billinginfo_to_booking,bundle);
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_New_Agreement_Billing.this).popBackStack();
                break;
        }
    }

    public void listSpinner(List<OnDropDownList> data) {

        int select = 0;
        for (int i = 0; i < data.size(); i++) {
            customerlist.add(data.get(i).Name);
            if (customer.Id == data.get(i).Id) {
                select = i;
            }
        }
       if (binding.entity.getSelectedItem().toString().equals("Customer")){
           binding.searchview.setVisibility(View.VISIBLE);
           binding.userlist.setVisibility(View.GONE);
           adaptercustomer = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, customerlist);
           binding.searchview.setThreshold(1);
           binding.searchview.setAdapter(adaptercustomer);
           binding.searchview.setText(customer.FullName);


       } else {
           binding.searchview.setVisibility(View.GONE);
           binding.userlist.setVisibility(View.VISIBLE);
           adaptercustomer = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, customerlist);
           binding.userlist.setAdapter(adaptercustomer);
           binding.userlist.setSelection(select);
       }

        binding.userlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getDropdownId(binding.userlist.getSelectedItem().toString());
                String string = "?id=" + idd;
                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(() -> {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");

                                if (status)
                                {
                                    final JSONObject custommer= responseJSON.getJSONObject("Data");
                                    Log.e(TAG, "run: " + custommer );
                                    customerProfile = loginRes.getModel(custommer.toString(), CustomerProfile.class);
                                    binding.name.setText( customerProfile.FullName);
                                    binding.number.setText(customerProfile.MobileNo);
                                    binding.email.setText(customerProfile.Email);
                                    binding.address.setText(Html.fromHtml(customerProfile.AddressesModel.PreviewAddress));
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, string);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       binding.searchview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               getDropdownId(binding.searchview.getText().toString());
               String string = "?id=" + idd;
               new ApiService(new OnResponseListener() {
                   @Override
                   public void onSuccess(String response, HashMap<String, String> headers) {
                       handler.post(() -> {
                           try {
                               JSONObject responseJSON = new JSONObject(response);
                               Boolean status = responseJSON.getBoolean("Status");

                               if (status)
                               {
                                   final JSONObject custommer= responseJSON.getJSONObject("Data");
                                   Log.e(TAG, "run: " + custommer );
                                   customerProfile = loginRes.getModel(custommer.toString(), CustomerProfile.class);
                                   binding.name.setText( customerProfile.FullName);
                                   binding.number.setText(customerProfile.MobileNo);
                                   binding.email.setText(customerProfile.Email);
                                   binding.address.setText(Html.fromHtml(customerProfile.AddressesModel.PreviewAddress));
                               }
                           } catch (Exception e){
                               e.printStackTrace();
                           }
                       });
                   }

                   @Override
                   public void onError(String error) {

                   }
               }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, string);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       binding.searchview.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               if (s.length() > 4){
                   getDropdownId(binding.searchview.getText().toString());
                   String string = "?id=" + idd;
                   new ApiService(new OnResponseListener() {
                       @Override
                       public void onSuccess(String response, HashMap<String, String> headers) {
                           handler.post(() -> {
                               try {
                                   JSONObject responseJSON = new JSONObject(response);
                                   Boolean status = responseJSON.getBoolean("Status");

                                   if (status)
                                   {
                                       final JSONObject custommer= responseJSON.getJSONObject("Data");
                                       Log.e(TAG, "run: " + custommer );
                                       customerProfile = loginRes.getModel(custommer.toString(), CustomerProfile.class);
                                       binding.name.setText( customerProfile.FullName);
                                       binding.number.setText(customerProfile.MobileNo);
                                       binding.email.setText(customerProfile.Email);
                                       binding.address.setText(Html.fromHtml(customerProfile.AddressesModel.PreviewAddress));
                                   } else {
                                       binding.name.setText(responseJSON.getString("Message"));
                                   }
                               } catch (Exception e){
                                   e.printStackTrace();
                               }
                           });
                       }

                       @Override
                       public void onError(String error) {

                       }
                   }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, string);
               }
           }
       });

    }


    public void getDropdownId(String name){
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).Name.equals(name)){
                idd = data.get(i).Id;
            }
        }
    }

}
