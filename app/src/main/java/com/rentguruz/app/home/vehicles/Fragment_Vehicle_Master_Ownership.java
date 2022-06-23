package com.rentguruz.app.home.vehicles;

import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.OnStringListner;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponse;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleMasterOwnershipBinding;
import com.rentguruz.app.model.DoVehicle;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.display.ThemeColors;
import com.rentguruz.app.model.parameter.VehiclePurchasedBy;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.VehicleModel;
import com.rentguruz.app.model.vehicle.VehiclePurchaseDetailsModel;
import com.rentguruz.app.model.vehicle.VehicleVendor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CANCELAUTORISEDBY;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEPURCHASEUPDATE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEUPDATE;
import static com.rentguruz.app.apicall.ApiEndPoint.VENDOR;
import static com.rentguruz.app.apicall.ApiEndPoint.VENDORGET;

public class Fragment_Vehicle_Master_Ownership  extends BaseFragment {

    FragmentVehicleMasterOwnershipBinding binding;
    DoVehicle vehicleModel;
    List<OnDropDownList> data = new ArrayList<>();
    DropDown dropDownList;
    CustomeDialog dialog;
    VehiclePurchaseDetailsModel VehiclePurchaseDetailsModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterOwnershipBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        vehicleModel = new DoVehicle();
        vehicleModel  = (DoVehicle) getArguments().getSerializable("Models");
        bundle.putSerializable("Models", vehicleModel);
        VehiclePurchaseDetailsModel = new VehiclePurchaseDetailsModel();
        dialog = new CustomeDialog(getActivity());
        binding.header.screenHeader.setText(companyLabel.Vehicle + " Ownership" );
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        userDraw.checkbtn(binding.cashpurchase);
        userDraw.checkbtn(binding.financepurchase);
        userDraw.checkbtn(binding.leasepurchase);
        try {
           binding.companyname.setText(UserData.companyModel.Name);
           binding.emailid.setText(UserData.companyModel.EmailId);
           binding.comnumber.setText(UserData.companyModel.Telephone);
           setAdpter();
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            DropDown dropDownList2;
            dropDownList2 = (new DropDown(VENDOR, Integer.parseInt(loginRes.getData("CompanyId")), true, false));
            new ApiService2<DropDown>(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                OnDropDownList[] onDropDownLists;
                                List<String> strings = new ArrayList<>();
                                strings.add(0, "Select Vendor");
                                int selectedvendor = 0;
                                onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);
                                for (int i = 0; i < onDropDownLists.length; i++) {
                                    // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                    OnDropDownList onDropDownList = new OnDropDownList();
                                    onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                   // data.add(onDropDownList);

                                   strings.add(onDropDownLists[i].Name);
                                   try {
                                       if (onDropDownList.Id == vehicleModel.VehiclePurchaseDetailsModel.VendorMasterId){
                                           selectedvendor = i+1;
                                       }
                                   } catch (Exception e){
                                       e.printStackTrace();
                                   }
                                }
                                ArrayAdapter<String> selectvendor = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,strings);
                                binding.selectvendor.setAdapter(selectvendor);
                                binding.selectvendor.setSelection(selectedvendor);
                                //   getSpinner(binding.makeId,strings);
                                //listSpinner(data);

                                try {
                                    binding.selectvendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            for (int i = 0; i <onDropDownLists.length ; i++) {
                                                if (onDropDownLists[i].Name.matches(binding.selectvendor.getSelectedItem().toString())){
                                                       new ApiService(new OnResponseListener() {
                                                           @Override
                                                           public void onSuccess(String response, HashMap<String, String> headers) {
                                                              try {
                                                                  handler.post(new Runnable() {
                                                                      @Override
                                                                      public void run() {
                                                                          try {
                                                                              JSONObject responseJSON = new JSONObject(response);
                                                                              Boolean status = responseJSON.getBoolean("Status");
                                                                              final JSONObject getvehicleowner = responseJSON.getJSONObject("Data");
                                                                              VehicleVendor vehicleVendor = new VehicleVendor();
                                                                              vehicleVendor = loginRes.getModel(getvehicleowner.toString(), VehicleVendor.class);
                                                                              binding.name.setText(vehicleVendor.ContactPersonName);
                                                                              binding.number.setText(vehicleVendor.ContactPersonTelephone);
                                                                              binding.email.setText(vehicleVendor.ContactPersonEmail);
                                                                              VehiclePurchaseDetailsModel.VendorMasterId = vehicleVendor.Id;
                                                                              CustomBindingAdapter.html(binding.address,vehicleVendor.AddressesModel.PreviewAddress);
                                                                              CustomBindingAdapter.loadImage(binding.image,vehicleVendor.AttachmentsModel.AttachmentPath);
                                                                              Log.e(TAG, "run: " + response );
                                                                          } catch (Exception e){
                                                                              e.printStackTrace();
                                                                          }

                                                                      }
                                                                  });
                                                              } catch (Exception e){
                                                                  e.printStackTrace();
                                                              }

                                                           }

                                                           @Override
                                                           public void onError(String error) {

                                                           }
                                                       }, RequestType.GET, VENDORGET, BASE_URL_LOGIN, header, "?Id=" + onDropDownLists[i].Id);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }catch (Exception e){
                                    e.printStackTrace();
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
            }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList2);
        } catch (Exception e){
            e.printStackTrace();
        }




        try {
            dropDownList = (new DropDown(CANCELAUTORISEDBY,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
            new ApiService2<DropDown>(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                OnDropDownList[] onDropDownLists;
                                List<String> strings = new ArrayList<>();
                                onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);
                                for (int i = 0; i < onDropDownLists.length; i++) {
                                    // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                    OnDropDownList onDropDownList = new OnDropDownList();
                                    onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                    data.add(onDropDownList);

                                    strings.add(onDropDownLists[i].Name);
                                }

                                //   getSpinner(binding.makeId,strings);
                                listSpinner(data);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList);



        } catch (Exception e){
            e.printStackTrace();
        }
        binding.cash.pmtdate.setOnClickListener(this);
        binding.finance.pmtdate.setOnClickListener(this);
        getToggle(binding.isActive,UiColor,true);
        getToggle(binding.finance.reminder,UiColor,false);



        binding.cashpurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listSpinner(data);
                    binding.cash.getRoot().setVisibility(View.VISIBLE);
                    binding.finance.getRoot().setVisibility(View.GONE);
                    setviewDetail(true);
                }
            }
        });

        binding.financepurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listSpinner(data);
                    binding.finance.getRoot().setVisibility(View.VISIBLE);
                    binding.cash.getRoot().setVisibility(View.GONE);
                    setviewDetail(false);
                }
            }
        });

        binding.leasepurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listSpinner(data);
                    binding.finance.getRoot().setVisibility(View.VISIBLE);
                    binding.cash.getRoot().setVisibility(View.GONE);
                    setviewDetail(false);
                }
            }
        });


        try {
            Log.e(TAG, "onViewCreated: " + VehiclePurchasedBy.Cash.inte  + "   " + vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy);
            if (VehiclePurchasedBy.Cash.inte == vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy ) {
                Log.e(TAG, "onViewCreated: " + VehiclePurchasedBy.Cash.inte  + "   " + vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy);
                binding.cashpurchase.setChecked(true);
                binding.cash.getRoot().setVisibility(View.VISIBLE);
                binding.finance.getRoot().setVisibility(View.GONE);
                binding.cash.pretaxamt.setText(String.valueOf(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.PreTaxAmount)));
                binding.cash.paidby.setText(vehicleModel.VehiclePurchaseDetailsModel.PaidBy);
                binding.cash.pmtdate.setText(Helper.getDateDisplay(vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment));
                binding.cash.monthlypaid.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly));
                binding.cash.kmsallowed.setText(String.valueOf(vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed));
                binding.cash.downpayment.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.DownPayment));
                binding.cash.taxamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TaxAmount));
                binding.cash.chequenumber.setText(vehicleModel.VehiclePurchaseDetailsModel.ChequeNumber);
                binding.cash.bankname.setText(vehicleModel.VehiclePurchaseDetailsModel.BankName);
                binding.cash.totalamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TotalAmount));
            }

            if (VehiclePurchasedBy.Finance.inte == vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy) {
                Log.e(TAG, "onViewCreated: " + VehiclePurchasedBy.Cash.inte  + "   " + vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy);
                binding.financepurchase.setChecked(true);
                binding.finance.getRoot().setVisibility(View.VISIBLE);
                binding.cash.getRoot().setVisibility(View.GONE);
                binding.finance.beforetax.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.CarPriceBeforeTax));
                binding.finance.reminder.setChecked(vehicleModel.VehiclePurchaseDetailsModel.Reminder);
                binding.finance.leaseterms.setText(vehicleModel.VehiclePurchaseDetailsModel.LeaseTermsMonth);
                binding.finance.pmtdate.setText(Helper.getDateDisplay(vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment));
                binding.finance.monthlypaid.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly));
                binding.finance.kmsallowed.setText(String.valueOf(vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed));
                binding.finance.downpayment.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.DownPayment));
                binding.finance.taxamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TaxAmount));
                binding.finance.bankname.setText(vehicleModel.VehiclePurchaseDetailsModel.BankName);

            }

            if (VehiclePurchasedBy.Lease.inte == vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy) {
                Log.e(TAG, "onViewCreated: " + VehiclePurchasedBy.Cash.inte  + "   " + vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy);
                binding.leasepurchase.setChecked(true);
                binding.finance.getRoot().setVisibility(View.VISIBLE);
                binding.cash.getRoot().setVisibility(View.GONE);
                binding.finance.beforetax.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.CarPriceBeforeTax));
                binding.finance.reminder.setChecked(vehicleModel.VehiclePurchaseDetailsModel.Reminder);
                binding.finance.leaseterms.setText(vehicleModel.VehiclePurchaseDetailsModel.LeaseTermsMonth);
                binding.finance.pmtdate.setText(Helper.getDateDisplay(vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment));
                binding.finance.monthlypaid.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly));
                binding.finance.kmsallowed.setText(String.valueOf(vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed));
                binding.finance.downpayment.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.DownPayment));
                binding.finance.taxamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TaxAmount));
                binding.finance.bankname.setText(vehicleModel.VehiclePurchaseDetailsModel.BankName);
            }
        } catch (Exception e){
            e.printStackTrace();
            binding.cashpurchase.setChecked(true);
            listSpinner(data);
            binding.cash.getRoot().setVisibility(View.VISIBLE);
            binding.finance.getRoot().setVisibility(View.GONE);
        }

        userDraw.toggle(binding.isActive, false);
    }

        @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Ownership.this).popBackStack();
                break;

            case R.id.pmtdate:
              /*  dialog.getFullDate(string -> {
                    binding.cash.pmtdate.setText(string);
                    binding.finance.pmtdate.setText(string);
                });*/

                dialog.getMaxDate( dialog.getToday(),"", string -> {
                    binding.cash.pmtdate.setText(string);
                    binding.finance.pmtdate.setText(string);
                });
                break;

            case R.id.next:
             //   vehicleModel = getVehicleModel();
                //vehicleModel.VehiclePurchaseDetailsModel = new VehiclePurchaseDetailsModel();
                if (validation()){
                    loginRes.testingLog(TAG,getVehicleModel());
                    new ApiService2(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(() -> {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    Log.e(TAG, "run: " + responseJSON);
                                    if (status) {
                                        NavHostFragment.findNavController(Fragment_Vehicle_Master_Ownership.this).popBackStack();
                                    } else {
                                        CustomToast.showToast(getActivity(), responseJSON.getString("Message"),1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            });
                        }

                        @Override
                        public void onError(String error) {

                        }
                    }, RequestType.PUT,VEHICLEPURCHASEUPDATE,BASE_URL_LOGIN,header,getVehicleModel());
                }
                break;
        }

    }

    public void listSpinner(List<OnDropDownList> data){
        List<String> cancelby = new ArrayList<>();
        cancelby.add(0,"Approved By");
        int select = 0;
        for (int i = 0; i <data.size() ; i++) {
            cancelby.add(data.get(i).Name);
            try {
                if (data.get(i).Id == vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy){
                    select = i;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        ArrayAdapter<String> adaptercancelby = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,cancelby);
        if (binding.cashpurchase.isChecked()){
            binding.cash.authorised.setAdapter(adaptercancelby);
            binding.cash.authorised.setSelection(select);
        }/* else {
            binding.finance.authorised.setAdapter(adaptercancelby);
            binding.finance.authorised.setSelection(select);
        }*/
        if (binding.financepurchase.isChecked()){
            binding.finance.authorised.setAdapter(adaptercancelby);
            binding.finance.authorised.setSelection(select);
        }

        if (binding.leasepurchase.isChecked()){
            binding.finance.authorised.setAdapter(adaptercancelby);
            binding.finance.authorised.setSelection(select);
        }


    }

    public void setAdpter(){
        List<String> pmtlist = new ArrayList<>();
        pmtlist.add(0,"Payment Frequency");
        pmtlist.add("Weekly");
        pmtlist.add("BiWeekly");
        pmtlist.add("Monthly");

        ArrayAdapter<String> adaptercancelby = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,pmtlist);
        binding.finance.frequencypmt.setAdapter(adaptercancelby);
    }
    public void setviewDetail(Boolean value){
        if (value){
            try {
                binding.cash.pretaxamt.setText(String.valueOf(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.PreTaxAmount)));
                binding.cash.paidby.setText(vehicleModel.VehiclePurchaseDetailsModel.PaidBy);
                binding.cash.pmtdate.setText(Helper.getDateDisplay(vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment));
                binding.cash.monthlypaid.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly));
                binding.cash.kmsallowed.setText(String.valueOf(vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed));
                binding.cash.downpayment.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.DownPayment));
                binding.cash.taxamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TaxAmount));
                binding.cash.chequenumber.setText(vehicleModel.VehiclePurchaseDetailsModel.ChequeNumber);
                binding.cash.bankname.setText(vehicleModel.VehiclePurchaseDetailsModel.BankName);
                binding.cash.totalamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TotalAmount));
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                binding.finance.beforetax.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.CarPriceBeforeTax));
                binding.finance.reminder.setChecked(vehicleModel.VehiclePurchaseDetailsModel.Reminder);
                binding.finance.leaseterms.setText(vehicleModel.VehiclePurchaseDetailsModel.LeaseTermsMonth);
                binding.finance.pmtdate.setText(Helper.getDateDisplay(vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment));
                binding.finance.monthlypaid.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly));
                binding.finance.kmsallowed.setText(String.valueOf(vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed));
                binding.finance.downpayment.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.DownPayment));
                binding.finance.taxamt.setText(Helper.getAmtount(vehicleModel.VehiclePurchaseDetailsModel.TaxAmount));
                binding.finance.bankname.setText(vehicleModel.VehiclePurchaseDetailsModel.BankName);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public DoVehicle getVehicleModel(){
        vehicleModel.VehiclePurchaseDetailsModel =  VehiclePurchaseDetailsModel;
        vehicleModel.VehiclePurchaseDetailsModel.VehicleId = vehicleModel.Id;
        vehicleModel.VehiclePurchaseDetailsModel.CompanyName = binding.companyname.getText().toString();
        vehicleModel.VehiclePurchaseDetailsModel.Email = binding.emailid.getText().toString();
        vehicleModel.VehiclePurchaseDetailsModel.MobileNo = binding.comnumber.getText().toString();
        if (binding.cashpurchase.isChecked()) {
            try{
                vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy = VehiclePurchasedBy.Cash.inte;
                vehicleModel.VehiclePurchaseDetailsModel.PreTaxAmount = Double.valueOf(binding.cash.pretaxamt.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.PaidBy = binding.cash.paidby.getText().toString();
                vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment =CustomeDialog.dateConvert(binding.cash.pmtdate.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly = Double.valueOf(binding.cash.monthlypaid.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed = Integer.parseInt(binding.cash.kmsallowed.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.DownPayment = Double.valueOf(binding.cash.downpayment.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.TaxAmount = Double.valueOf(binding.cash.taxamt.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.ChequeNumber = binding.cash.chequenumber.getText().toString();
                vehicleModel.VehiclePurchaseDetailsModel.BankName =        binding.cash.bankname.getText().toString();
                vehicleModel.VehiclePurchaseDetailsModel.TotalAmount = Double.valueOf(binding.cash.totalamt.getText().toString());
                // vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = binding.cash.authorised.getSelectedItemPosition();
                vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = getAuthoriseby(binding.cash.authorised);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        if (binding.financepurchase.isChecked()) {
            try {
                vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy = VehiclePurchasedBy.Finance.inte;
                vehicleModel.VehiclePurchaseDetailsModel.CarPriceBeforeTax = Double.valueOf(binding.finance.beforetax.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.Reminder =      binding.finance.reminder.isChecked();
                vehicleModel.VehiclePurchaseDetailsModel.LeaseTermsMonth =    binding.finance.leaseterms.getText().toString();
                vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment =CustomeDialog.dateConvert(binding.finance.pmtdate.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly =     Double.valueOf(binding.finance.monthlypaid.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed = Integer.parseInt(binding.finance.kmsallowed.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.DownPayment = Double.valueOf(binding.finance.downpayment.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.TaxAmount = Double.valueOf(binding.finance.taxamt.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.BankName =        binding.finance.bankname.getText().toString();
                // vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = binding.finance.authorised.getSelectedItemPosition();
                vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = getAuthoriseby(binding.finance.authorised);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        if (binding.leasepurchase.isChecked()) {
            try {
                vehicleModel.VehiclePurchaseDetailsModel.PurchasedBy = VehiclePurchasedBy.Lease.inte;
                vehicleModel.VehiclePurchaseDetailsModel.CarPriceBeforeTax = Double.valueOf(binding.finance.beforetax.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.Reminder =      binding.finance.reminder.isChecked();
                vehicleModel.VehiclePurchaseDetailsModel.LeaseTermsMonth =    binding.finance.leaseterms.getText().toString();
                vehicleModel.VehiclePurchaseDetailsModel.DateOfPayment =CustomeDialog.dateConvert(binding.finance.pmtdate.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AmountMonthly =     Double.valueOf(binding.finance.monthlypaid.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.AnnualMilesAllowed = Integer.parseInt(binding.finance.kmsallowed.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.DownPayment = Double.valueOf(binding.finance.downpayment.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.TaxAmount = Double.valueOf(binding.finance.taxamt.getText().toString());
                vehicleModel.VehiclePurchaseDetailsModel.BankName =        binding.finance.bankname.getText().toString();
                // vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = binding.finance.authorised.getSelectedItemPosition();
                vehicleModel.VehiclePurchaseDetailsModel.ApprovedBy = getAuthoriseby(binding.finance.authorised);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return vehicleModel;
    }

    public void getToggle(ToggleButton toggleButton, ThemeColors themeColors, Boolean value){
        if (value) {
            toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.primary), PorterDuff.Mode.SCREEN);
        } else {
            toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.secondary), PorterDuff.Mode.SCREEN);
        }

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.primary), PorterDuff.Mode.SCREEN);
            } else {
                toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.secondary), PorterDuff.Mode.SCREEN);
            }
        });
    }

    public Integer getAuthoriseby(Spinner sp){
        int value =0;
        if (sp.getSelectedItemPosition() != 0){
            for (int i = 0; i < data.size(); i++) {
                if (i == binding.finance.authorised.getSelectedItemPosition()){
                    return data.get(i).Id;
                }
            }
        }
        return value;
    }

    public Boolean validation(){
        Boolean value = true;
        if (binding.cashpurchase.isChecked()) {
            if (binding.cash.pretaxamt.getText().toString().equals("")) {

                binding.cash.pretaxamt.setError("Please Fill PreTax Amount");
                CustomToast.showToast(getActivity(),"Please Fill PreTax Amount",1);
                value = false;

              /*  if (Integer.valueOf(binding.cash.pretaxamt.getText().toString()) == 0) {
                    binding.cash.pretaxamt.setError("Please Fill PreTax Amount");
                    CustomToast.showToast(getActivity(),"Please Fill PreTax Amount",1);
                    value = false;
                }*/
            }

            if (binding.cash.paidby.getText().toString().equals("")){
                binding.cash.paidby.setError("Please Fill Paidby");
                CustomToast.showToast(getActivity(),"Please Fill Paidby",1);
                value = false;
            }

            if (binding.cash.pmtdate.getText().toString().equals("")){
                binding.cash.pmtdate.setError("Please Select Payment Date");
                CustomToast.showToast(getActivity(),"Please Select Payment Date",1);
                value = false;
            }

            if (binding.cash.monthlypaid.getText().toString().equals("")){
                binding.cash.monthlypaid.setError("Please Fill Monthly Paid Amount");
                CustomToast.showToast(getActivity(),"Please Fill Monthly Paid Amount",1);
                value = false;
                /*if (Integer.valueOf(binding.cash.monthlypaid.getText().toString()) == 0) {
                    binding.cash.monthlypaid.setError("Please Fill Monthly Paid Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Monthly Paid Amount",1);
                    value = false;
                }*/
            }

            if (binding.cash.kmsallowed.getText().toString().equals("")){
                binding.cash.kmsallowed.setError("Please Fill Annual KMS Allowed");
                CustomToast.showToast(getActivity(),"Please Fill Annual KMS Allowed",1);
                value = false;
               /* if (Integer.valueOf(binding.cash.kmsallowed.getText().toString()) == 0) {
                    binding.cash.kmsallowed.setError("Please Fill Annual KMS Allowed");
                    CustomToast.showToast(getActivity(),"Please Fill Annual KMS Allowed",1);
                    value = false;
                }*/
            }

            if (binding.cash.downpayment.getText().toString().equals("")){
                binding.cash.downpayment.setError("Please Fill Downpayment Amount");
                CustomToast.showToast(getActivity(),"Please Fill Downpayment Amount",1);
                value = false;
               /* if (Integer.valueOf(binding.cash.downpayment.getText().toString()) == 0) {
                    binding.cash.downpayment.setError("Please Fill Downpayment Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Downpayment Amount",1);
                    value = false;
                }*/
            }

            if (binding.cash.taxamt.getText().toString().equals("")){
                binding.cash.taxamt.setError("Please Fill Tax Amount");
                CustomToast.showToast(getActivity(),"Please Fill Tax Amount",1);
                value = false;
                /*if (Integer.valueOf(binding.cash.taxamt.getText().toString()) == 0) {
                    binding.cash.taxamt.setError("Please Fill Tax Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Tax Amount",1);
                    value = false;
                }*/
            }

            if (binding.cash.chequenumber.getText().toString().equals("")){
                binding.cash.chequenumber.setError("Please Fill Cheque Number");
                CustomToast.showToast(getActivity(),"Please Fill Cheque Number",1);
                value = false;
                /*if (Integer.valueOf(binding.cash.chequenumber.getText().toString()) == 0) {
                    binding.cash.chequenumber.setError("Please Fill Cheque Number");
                    CustomToast.showToast(getActivity(),"Please Fill Cheque Number",1);
                    value = false;
                }*/
            }

            if (binding.cash.bankname.getText().toString().equals("")){
                    binding.cash.bankname.setError("Please Fill Bank Name");
                    CustomToast.showToast(getActivity(),"Please Fill Bank Name",1);
                    value = false;
            }

            if (binding.cash.totalamt.getText().toString().equals("")){
                binding.cash.totalamt.setError("Please Fill Total Amount");
                CustomToast.showToast(getActivity(),"Please Fill Total Amount",1);
                value = false;
                /*if (Integer.valueOf(binding.cash.totalamt.getText().toString()) == 0) {
                    binding.cash.totalamt.setError("Please Fill Total Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Total Amount",1);
                    value = false;
                }*/
            }
        }

        if (binding.financepurchase.isChecked()){
            if (binding.finance.monthlypaid.getText().toString().equals("")) {
                binding.finance.monthlypaid.setError("Please Fill Monthly Amount");
                CustomToast.showToast(getActivity(),"Please Fill Monthly Amount",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.monthlypaid.getText().toString()) == 0) {
                    binding.finance.monthlypaid.setError("Please Fill Monthly Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Monthly Amount",1);
                    value = false;
                }*/
            }

            if (binding.finance.kmsallowed.getText().toString().equals("")) {
                binding.finance.kmsallowed.setError("Please Fill Kms Allow");
                CustomToast.showToast(getActivity(),"Please Fill Kms Allow",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.kmsallowed.getText().toString()) == 0) {
                    binding.finance.kmsallowed.setError("Please Fill Kms Allow");
                    CustomToast.showToast(getActivity(),"Please Fill Kms Allow",1);
                    value = false;
                }*/
            }

            if (binding.finance.downpayment.getText().toString().equals("")) {
                binding.finance.downpayment.setError("Please Fill Downpayment");
                CustomToast.showToast(getActivity(),"Please Fill Downpayment",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.downpayment.getText().toString()) == 0) {
                    binding.finance.downpayment.setError("Please Fill Downpayment");
                    CustomToast.showToast(getActivity(),"Please Fill Downpayment",1);
                    value = false;
                }*/
            }

            if (binding.finance.taxamt.getText().toString().equals("")) {
                binding.finance.taxamt.setError("Please Fill Taxamount");
                CustomToast.showToast(getActivity(),"Please Fill Taxamount",1);
                value = false;
                /*if (Integer.valueOf(binding.finance.taxamt.getText().toString()) == 0) {
                    binding.finance.taxamt.setError("Please Fill Taxamount");
                    CustomToast.showToast(getActivity(),"Please Fill Taxamount",1);
                    value = false;
                }*/
            }

            if (binding.finance.bankname.getText().toString().equals("")) {
                    binding.finance.bankname.setError("Please Fill Bank Name");
                    CustomToast.showToast(getActivity(),"Please Fill Bank Name",1);
                    value = false;

            }

            if (binding.finance.beforetax.getText().toString().equals("")) {
                binding.finance.beforetax.setError("Please Fill Before Tax Amount");
                CustomToast.showToast(getActivity(),"Please Fill Before Tax Amount",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.beforetax.getText().toString()) == 0) {
                    binding.finance.beforetax.setError("Please Fill Before Tax Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Before Tax Amount",1);
                    value = false;
                }*/
            }

            if (binding.finance.pmtdate.getText().toString().equals("")){
                binding.finance.pmtdate.setError("Please Select Payment Date");
                CustomToast.showToast(getActivity(),"Please Select Payment Date",1);
                value = false;
            }

            if (binding.finance.leaseterms.getText().toString().equals("")) {
                binding.finance.leaseterms.setError("Please Fill Lease Terms (Month)");
                CustomToast.showToast(getActivity(),"Please Fill Lease Terms (Month)",1);
                value = false;
                /*if (Integer.valueOf(binding.finance.leaseterms.getText().toString()) == 0) {
                    binding.finance.leaseterms.setError("Please Fill Lease Terms (Month)");
                    CustomToast.showToast(getActivity(),"Please Fill Lease Terms (Month)",1);
                    value = false;
                }*/
            }
        }

        if (binding.leasepurchase.isChecked()){
            if (binding.finance.monthlypaid.getText().toString().equals("")) {
                binding.finance.monthlypaid.setError("Please Fill Monthly Amount");
                CustomToast.showToast(getActivity(),"Please Fill Monthly Amount",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.monthlypaid.getText().toString()) == 0) {
                    binding.finance.monthlypaid.setError("Please Fill Monthly Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Monthly Amount",1);
                    value = false;
                }*/
            }

            if (binding.finance.kmsallowed.getText().toString().equals("")) {
                binding.finance.kmsallowed.setError("Please Fill Kms Allow");
                CustomToast.showToast(getActivity(),"Please Fill Kms Allow",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.kmsallowed.getText().toString()) == 0) {
                    binding.finance.kmsallowed.setError("Please Fill Kms Allow");
                    CustomToast.showToast(getActivity(),"Please Fill Kms Allow",1);
                    value = false;
                }*/
            }

            if (binding.finance.downpayment.getText().toString().equals("")) {
                binding.finance.downpayment.setError("Please Fill Downpayment");
                CustomToast.showToast(getActivity(),"Please Fill Downpayment",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.downpayment.getText().toString()) == 0) {
                    binding.finance.downpayment.setError("Please Fill Downpayment");
                    CustomToast.showToast(getActivity(),"Please Fill Downpayment",1);
                    value = false;
                }*/
            }

            if (binding.finance.taxamt.getText().toString().equals("")) {
                binding.finance.taxamt.setError("Please Fill Taxamount");
                CustomToast.showToast(getActivity(),"Please Fill Taxamount",1);
                value = false;
                /*if (Integer.valueOf(binding.finance.taxamt.getText().toString()) == 0) {
                    binding.finance.taxamt.setError("Please Fill Taxamount");
                    CustomToast.showToast(getActivity(),"Please Fill Taxamount",1);
                    value = false;
                }*/
            }

            if (binding.finance.bankname.getText().toString().equals("")) {
                binding.finance.bankname.setError("Please Fill Bank Name");
                CustomToast.showToast(getActivity(),"Please Fill Bank Name",1);
                value = false;

            }

            if (binding.finance.beforetax.getText().toString().equals("")) {
                binding.finance.beforetax.setError("Please Fill Before Tax Amount");
                CustomToast.showToast(getActivity(),"Please Fill Before Tax Amount",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.beforetax.getText().toString()) == 0) {
                    binding.finance.beforetax.setError("Please Fill Before Tax Amount");
                    CustomToast.showToast(getActivity(),"Please Fill Before Tax Amount",1);
                    value = false;
                }*/
            }

            if (binding.finance.pmtdate.getText().toString().equals("")){
                binding.finance.pmtdate.setError("Please Select Payment Date");
                CustomToast.showToast(getActivity(),"Please Select Payment Date",1);
                value = false;
            }

            if (binding.finance.leaseterms.getText().toString().equals("")) {
                binding.finance.leaseterms.setError("Please Fill Lease Terms (Month)");
                CustomToast.showToast(getActivity(),"Please Fill Lease Terms (Month)",1);
                value = false;
               /* if (Integer.valueOf(binding.finance.leaseterms.getText().toString()) == 0) {
                    binding.finance.leaseterms.setError("Please Fill Lease Terms (Month)");
                    CustomToast.showToast(getActivity(),"Please Fill Lease Terms (Month)",1);
                    value = false;
                }*/
            }
        }
        return value;
    }
}
