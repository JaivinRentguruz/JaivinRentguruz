package com.rentguruz.app.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentChangePaymentOptionBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.parameter.PaymentMode;
import com.rentguruz.app.model.parameter.PaymentProcess;
import com.rentguruz.app.model.parameter.PaymentProcessMode;
import com.rentguruz.app.model.parameter.PaymentTransactionType;
import com.rentguruz.app.model.parameter.SplitAmountType;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.Reservation;
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

public class Fragment_Change_Payment_Option extends BaseFragment {

    FragmentChangePaymentOptionBinding binding;
    ReservationPMT reservationPMT;
    CustomeDialog dialog;
    ReservationSummarry reservationSummarry;
    CustomerProfile customerProfile;
    Customer customer;
    List<ReservationPMT> pmtList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePaymentOptionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        reservationPMT = new ReservationPMT();
        reservationSummarry = new ReservationSummarry();
        binding.PaymentAccept.setOnClickListener(this);
        binding.chequeDate.setOnClickListener(this);
        binding.header.screenHeader.setText(companyLabel.Reservation +" "+companyLabel.Payment);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        customerProfile = new CustomerProfile();
        customer = new Customer();
        dialog = new CustomeDialog(getContext());
        binding.checkbtn.setChecked(true);

        try {
            if (getArguments().getInt("reservationpmt") != 1){
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
                bundle.putString("transactionId",getArguments().getString("transactionId"));
                bundle.putString("netrate", getArguments().getString("netrate"));
                bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            Log.e(TAG, "onViewCreated: " + "0t" );
            binding.amount.setText(Helper.getAmtount(Double.valueOf(getArguments().getString("netrate"))));
            binding.amountPayable.setText(Helper.getAmtount (Double.valueOf(getArguments().getString("netrate"))));
            if (getArguments().getSerializable("reservationSum") != null){
                Log.e(TAG, "onViewCreated:11 " + getArguments().getSerializable("reservationSum"));
                reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
                bundle.putSerializable("reservationSum", reservationSummarry);
                if (getArguments().getInt("reservationpmt") == 1){
                    bundle.putInt("reservationpmt",getArguments().getInt("reservationpmt"));
                    String datasad = loginRes.modeltostring(reservationSummarry);
                    Reservation reservation =  loginRes.stringtomodel(datasad,Reservation.class);
                    reservation.Email = reservationSummarry.CustomerEmail;
                    bundle.putSerializable("reservation", getArguments().getSerializable("reservation"));
                    Log.e(TAG, "onViewCreated:12 "  + reservationSummarry.CustomerEmail);
                }
            } else {
                Reservation reservation =(Reservation) getArguments().getSerializable("reservation");
                reservationSummarry.Id = reservation.Id;
                reservationSummarry.ReservationNo = reservation.ReservationNo;
                reservationSummarry.CustomerEmail = reservation.Email;
                bundle.putSerializable("reservationSum", reservationSummarry);
            }
            Log.e(TAG, "onViewCreated:13 "  + reservationSummarry.CustomerEmail);

            binding.agreement.setText(reservationSummarry.ReservationNo);
            binding.invoicenumber.setText(String.valueOf(reservationSummarry.Id));
            customerProfile = (CustomerProfile) getArguments().getSerializable("customerDetail");
            customer =(Customer)  getArguments().getSerializable("customer");
            if (customer!= null) {

            } else {
                customer = new Customer();
                customer.Id = customerProfile.Id;
            }
            reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,customerProfile);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            bundle.putBundle(getActivity().getResources().getString(R.string.bundle),getArguments().getBundle(getActivity().getResources().getString(R.string.bundle)));
        } catch (Exception e){
            e.printStackTrace();
        }


   /*     binding.paymentOption.setVisibility(View.GONE);
        binding.cashpmtdetail.setVisibility(View.GONE);

        binding.chequeDetail.setVisibility(View.GONE);
        binding.splitheader.setVisibility(View.GONE);
        binding.splitdata.setVisibility(View.GONE);*/


        userDraw.checkbtn(binding.splitfixamount);
        userDraw.checkbtn(binding.cash);
        userDraw.checkbtn(binding.online);
        userDraw.checkbtn(binding.Deposit);
        binding.splitfixamount.setChecked(true);
        binding.cash.setChecked(true);
        binding.online.setChecked(true);
        binding.chequeDetail.setVisibility(View.GONE);
        binding.splitAmount.setChecked(false);
        binding.Deposit.setChecked(true);



        userDraw.toggle(binding.changeamt, false);
        userDraw.toggle(binding.splitAmount, false);

        binding.paymentOption.setVisibility(View.GONE);
        binding.splitheader.setVisibility(View.GONE);
        binding.splitdata.setVisibility(View.GONE);
        binding.transationDetail.setVisibility(View.GONE);
        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Online.inte;
        //reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
        reservationPMT.TransactionType = PaymentTransactionType.Deposit.inte;
        reservationPMT.PaymentProcess = PaymentProcess.Refund.inte;
        reservationPMT.PaymentMode = PaymentMode.CreditCard.inte;
       // userDraw.enablechangeEdit(binding.amount, false);
        //binding.amount.setEnabled(false);
        binding.changeamt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (isChecked){
                    binding.amount.setEnabled(true);
                }*/
                userDraw.toggle(binding.changeamt, isChecked);
              //  userDraw.enablechangeEdit(binding.amount,isChecked);
            }
        });



        binding.transationtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.Payment:
                       // reservationPMT.PaymentTransactionType = PaymentTransactionType.Payment.inte;
                        reservationPMT.TransactionType = PaymentTransactionType.Payment.inte;
                        reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
                        binding.amount.setText(Helper.getAmtount(Helper.reservationamt));
                        binding.amountPayable.setText(Helper.getAmtount(Helper.reservationamt));
                        Log.e(TAG, "onCheckedChanged: " + "Payment" );
                        break;

                    case R.id.Deposit:
                        //reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
                        reservationPMT.TransactionType = PaymentTransactionType.Deposit.inte;
                        reservationPMT.PaymentProcess = PaymentProcess.Refund.inte;
                        binding.amount.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));
                        binding.amountPayable.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));
                        Log.e(TAG, "onCheckedChanged: " + "Deposit" );
                        break;

                    case R.id.preAuthorization:
                        //reservationPMT.PaymentTransactionType = PaymentTransactionType.PreAuthorization.inte;
                        reservationPMT.TransactionType = PaymentTransactionType.PreAuthorization.inte;
                        binding.amount.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));
                        binding.amountPayable.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));
                        Log.e(TAG, "onCheckedChanged: " + "Pre Authorization" );
                        break;
                }
            }
        });

        binding.selectPaymentCashCheque.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.cash:
                        reservationPMT.PaymentMode = PaymentMode.Cash.inte;
                       // binding.cashpmtdetail.setVisibility(View.VISIBLE);
                       // binding.chequeDetail.setVisibility(View.GONE);
                        //binding.splitheader.setVisibility(View.VISIBLE);
                        //binding.splitdata.setVisibility(View.VISIBLE);
                        //binding.chequeDetail.setVisibility(View.GONE);
                        break;

                    case R.id.debitcard:
                        reservationPMT.PaymentMode = PaymentMode.DebitCard.inte;
                        break;

                    case R.id.cheque:
                        reservationPMT.PaymentMode = PaymentMode.Cheque.inte;
                        //binding.cashpmtdetail.setVisibility(View.GONE);
                        //binding.chequeDetail.setVisibility(View.VISIBLE);
                      //  binding.splitheader.setVisibility(View.VISIBLE);
                       // binding.splitdata.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        binding.selectPaymentCashCheque.setVisibility(View.GONE);
        binding.paymentOptionOnlineOffline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.online:
                        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Online.inte;
                        binding.selectPaymentCashCheque.setVisibility(View.GONE);
                        break;

                    case R.id.offline:
                        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Offline.inte;
                        binding.selectPaymentCashCheque.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });


        binding.splitPercentageAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.splitpercentage:
                        binding.currency.setText("  %  ");
                        if  (!binding.amount.getText().toString().trim().isEmpty()) {
                            try {
                                Double amt = Double.valueOf(binding.amount.getText().toString());
                                int valuee = Integer.valueOf(binding.lessamount.getText().toString());
                                Double rate = Double.valueOf(amt * valuee / 100);
                                binding.amountPayable.setText(Helper.getAmtount(rate));
                                reservationPMT.SplitAmountType = SplitAmountType.Percentage.inte;
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        break;

                    case R.id.splitfixamount:
                        binding.currency.setText(Helper.displaycurrency);
                        if  (!binding.amount.getText().toString().trim().isEmpty()) {
                            try {
                                Double amt = Double.valueOf(binding.amount.getText().toString());
                                int valuee = Integer.valueOf(binding.lessamount.getText().toString());
                                Double rate = Double.valueOf(amt - valuee);
                                binding.amountPayable.setText(Helper.getAmtount(rate));
                                reservationPMT.SplitAmountType = SplitAmountType.Amount.inte;
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        break;
                }
            }
        });



        binding.splitAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDraw.toggle(binding.splitAmount, isChecked);
                if (isChecked){
                    binding.paymentOption.setVisibility(View.VISIBLE);
                    binding.splitheader.setVisibility(View.VISIBLE);
                    binding.splitdata.setVisibility(View.VISIBLE);
                    binding.transationDetail.setVisibility(View.VISIBLE);
                } else {
                    binding.paymentOption.setVisibility(View.GONE);
                    binding.splitheader.setVisibility(View.GONE);
                    binding.splitdata.setVisibility(View.GONE);
                    binding.transationDetail.setVisibility(View.GONE);
                }
            }
        });

        //binding.splitfixamount.setChecked(true);
       // binding.splitPercentageAmount.clearCheck();
        //binding.splitPercentageAmount.check(binding.splitpercentage.getId());


       /* for (int i = 0; i < binding.splitPercentageAmount.getChildCount(); i++) {
           if (binding.splitPercentageAmount.getChildAt(i).getId() == binding.splitfixamount.getId()){
                binding.splitPercentageAmount.clearCheck();
                binding.splitPercentageAmount.check(binding.splitfixamount.getId());
                binding.splitfixamount.setButtonTintList(ColorStateList.valueOf(Color.parseColor(UiColor.primary)));
           }
        }*/

       // binding.splitpercentage.setChecked(true);
       // binding.splitfixamount.setChecked(true);

        binding.amount.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));
        binding.amountPayable.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.RateFeaturesModel.SecurityDeposit));

        binding.lessamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("")) {
                    try {
                        Double amt = Double.valueOf(binding.amount.getText().toString());
                        int valuee = Integer.valueOf(String.valueOf(s));
                        if (binding.splitpercentage.isChecked()) {
                            Double rate = Double.valueOf(amt * valuee / 100);
                            binding.amountPayable.setText(Helper.getAmtount(rate));
                        } else {
                            Double rate = Double.valueOf(amt - valuee);
                            binding.amountPayable.setText(Helper.getAmtount(rate));
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    /*if (binding.splitAmount.isChecked()) {
                        Double rate = Double.valueOf(amt - valuee);
                        binding.amountPayable.setText(rate.toString());
                    }*/
                }
            }
        });

       /* if (getArguments().getBoolean("pmt")){
            reservationPMT.PaymentProcessMode =  PaymentProcessMode.Online.inte;
            reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
            reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
            //reservationPMT.SplitAmountType =
            reservationPMT.PaymentMode = PaymentMode.CreditCard.inte;

        } else {
            reservationPMT.PaymentProcessMode =  PaymentProcessMode.Offline.inte;
            reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
            reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
            //reservationPMT.SplitAmountType =
            reservationPMT.PaymentMode = PaymentMode.Cash.inte;
        }

        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Offline.inte;
        reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
        reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
        //reservationPMT.SplitAmountType =
        reservationPMT.PaymentMode = PaymentMode.Cash.inte;*/


        try {
            binding.paymentoptions.setChecked(true);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PaymentAccept:
                try {
                getModel();
                reservationPMT.SplitAmount =0;
                //reservationPMT.SplitAmountType = 0;
                //reservationPMT.TransactionType = 1;
                reservationPMT.IsSplit = false;
                //reservationPMT.PaymentForId = 13;
                //reservationPMT.PaymentProcess = 1;
//        int i =  PaymentProcessMode.Offline.inte;



                //reservationPMT.SplitAmountType =

                reservationPMT.CustomerId = customer.Id;
                reservationPMT.InvoiceAmount = reservationPMT.Amount;
                reservationPMT.ReservationId = reservationSummarry.Id;
                pmtList.add(reservationPMT);
                //new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST, RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);
                bundle.putSerializable("pmtmodel", reservationPMT);
                bundle.putString("netrate",binding.amountPayable.getText().toString() );
                bundle.putString("netrate",binding.amount.getText().toString() );
                Helper.pmt = true;
             /*   if (getArguments().getBoolean("pmt")){
                    NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_Payment,bundle);
                } else {
                    NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_PaymentOffline,bundle);
                }*/
                if (binding.offline.isChecked()) {
                    new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                            RESERVATIONPMT, BASE_URL_LOGIN, header, pmtList);
                } else {
                    NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_Payment,bundle);
                }
                } catch (Exception e){
                    e.printStackTrace();
                }
            break;

            case R.id.chequeDate:
                dialog.getFullDate(dialog.getToday(), "", string -> binding.chequeDate.setText(string));
                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).popBackStack();
                break;
        }

    }

    private void getModel(){
        try {
            reservationPMT.IsSplit = binding.splitAmount.isChecked();
            reservationPMT.Amount = Double.valueOf(binding.amountPayable.getText().toString());
            reservationPMT.AgreementNumber = reservationSummarry.ReservationNo;
            reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,customerProfile);
            reservationPMT.BillTo = 1;
            reservationPMT.CustomerId = customerProfile.Id;
            reservationPMT.InvoiceAmount = reservationPMT.Amount;
            reservationPMT.ReservationId = reservationSummarry.Id;
        } catch (Exception e){
            e.printStackTrace();
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
                           /* transactionId = responseJSON.getString("Data");
                            bundle.putString("transactionId",transactionId);
                            bundle.putString("netrate", getArguments().getString("netrate"));*/
                            String message = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),message,0);
                           // NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_paymentsucess,bundle);
                            NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_paymentsuccess,bundle);

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
