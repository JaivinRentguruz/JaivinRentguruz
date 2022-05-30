package com.rentguruz.app.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.bumptech.glide.Glide;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentPaymentOfflineBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.parameter.PaymentMode;
import com.rentguruz.app.model.parameter.PaymentProcess;
import com.rentguruz.app.model.parameter.PaymentProcessMode;
import com.rentguruz.app.model.parameter.PaymentTransactionType;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.ReservationPMT;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONPMT;

public class Fragment_Payment_Offline extends BaseFragment {

    FragmentPaymentOfflineBinding binding;
    ReservationPMT reservationPMT;
    ReservationSummarry reserversationSummary;
    Customer customer;
    List<ReservationPMT> pmtList = new ArrayList<>();
    String transactionId;
    CustomerProfile customerProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentOfflineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.PaymentProcess.setOnClickListener(this);
        binding.OnlinePayment.setOnClickListener(this);
        binding.header.screenHeader.setText("Offline Payment");
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
        bundle.putString("netrate", getArguments().getString("netrate"));
        bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));

        customerProfile = new CustomerProfile();
        reservationPMT = new ReservationPMT();
        reserversationSummary = new ReservationSummarry();
        customer = new Customer();
        reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        customer = (Customer) getArguments().getSerializable("customer");
        customerProfile = (CustomerProfile) getArguments().getSerializable("customerDetail");
        reservationPMT.SplitAmount =0;
        //reservationPMT.SplitAmountType = 0;
        reservationPMT.TransactionType = 1;
        reservationPMT.IsSplit = false;
        reservationPMT.PaymentForId = 13;
        reservationPMT.PaymentProcess = 1;
//        int i =  PaymentProcessMode.Offline.inte;
        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Offline.inte;
        reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
        reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
        //reservationPMT.SplitAmountType =
        reservationPMT.PaymentMode = PaymentMode.Cash.inte;

        VehicleModel  vehicleModel = new VehicleModel();
        vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
        LocationList pickuplocation = new LocationList();
        pickuplocation = (LocationList) getArguments().getSerializable("model");

        LocationList returnlocation = new LocationList();
        returnlocation = (LocationList) getArguments().getSerializable("models");

        binding.textViewPickupLocation.setText(pickuplocation.Name);
        binding.textViewReturnLocationName.setText(returnlocation.Name);
        binding.textViewPickupLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
        binding.textViewReturnLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
        Glide.with(context).load(vehicleModel.DefaultImagePath).into(binding.carimage);
        binding.textVVehicleModelName.setText(vehicleModel.VehicleShortName);

        Double amountPayable = Double.valueOf(getArguments().getString("netrate"));
        //txtAmtPayable.setText("$ "+((String.format(Locale.US,"%.2f",amountPayable))));
        binding.txtAmtPayable.setText(Helper.getAmtount(amountPayable,false));
        binding.customerName.setText(customer.FullName);
        binding.netAmount.setText(Helper.currencyName + Helper.getAmtount(amountPayable,false));
        reservationPMT.AgreementNumber = reserversationSummary.ReservationNo;

        reservationPMT.Amount =Double.valueOf(getArguments().getString("netrate"));
        reservationPMT.BillTo = 1;

        binding.process.setOnClickListener(this);
        binding.PaymentProcess.setOnClickListener(this);
        binding.OnlinePayment.setOnClickListener(this);

        if (Helper.pmt){
            reservationPMT = (ReservationPMT) getArguments().getSerializable("pmtmodel");
        }

        loginRes.testingLog(TAG, reservationPMT);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PaymentProcess:
               // NavHostFragment.findNavController(Fragment_Payment_Offline.this).navigate(R.id.paymentoffline_to_paymentchangeoption);


                reservationPMT.CustomerId = customer.Id;
                reservationPMT.InvoiceAmount = reservationPMT.Amount;
                reservationPMT.ReservationId = reserversationSummary.Id;
                pmtList.add(reservationPMT);

                new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                        RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);
                break;

            case R.id.OnlinePayment:
                NavHostFragment.findNavController(Fragment_Payment_Offline.this).navigate(R.id.paymentoffline_to_payment,bundle);
                break;

            case R.id.process:
                reservationPMT.CreditCardId = UserData.UpdateCreditCard.Id;
                reservationPMT.CustomerId = customer.Id;
                reservationPMT.InvoiceAmount = reservationPMT.Amount;
                reservationPMT.ReservationId = reserversationSummary.Id;
                pmtList.add(reservationPMT);
                bundle.putBoolean("pmt", false);
                NavHostFragment.findNavController(Fragment_Payment_Offline.this).navigate(R.id.paymentoffline_to_paymentchangeoption,bundle);
              /*  new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                        RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);*/
                break;
        }

    }

    OnResponseListener processPayment = new OnResponseListener()
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
                        try
                        {
                            transactionId = responseJSON.getString("Data");
                            bundle.putString("transactionId",transactionId);
                            bundle.putString("netrate", getArguments().getString("netrate"));
                            String message = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),message,0);
                            NavHostFragment.findNavController(Fragment_Payment_Offline.this).navigate(R.id.paymentoffline_to_paymentsucess,bundle);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }
                }
                catch (Exception e)
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
}
