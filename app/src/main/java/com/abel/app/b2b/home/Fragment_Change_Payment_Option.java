package com.abel.app.b2b.home;

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

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentChangePaymentOptionBinding;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.parameter.PaymentMode;
import com.abel.app.b2b.model.parameter.PaymentProcess;
import com.abel.app.b2b.model.parameter.PaymentProcessMode;
import com.abel.app.b2b.model.parameter.PaymentTransactionType;
import com.abel.app.b2b.model.parameter.SplitAmountType;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationPMT;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONPMT;

public class Fragment_Change_Payment_Option extends BaseFragment {

    FragmentChangePaymentOptionBinding binding;
    ReservationPMT reservationPMT;
    CustomeDialog dialog;
    ReservationSummarry reservationSummarry;
    CustomerProfile customerProfile;
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
        binding.PaymentAccept.setOnClickListener(this);
        binding.chequeDate.setOnClickListener(this);
        customerProfile = new CustomerProfile();
        dialog = new CustomeDialog(getContext());
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

        reservationPMT = new ReservationPMT();
        reservationSummarry = new ReservationSummarry();
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");

        binding.agreement.setText(reservationSummarry.ReservationNo);
        binding.invoicenumber.setText(String.valueOf(reservationSummarry.Id));

        binding.amount.setText(getArguments().getString("netrate"));

        customerProfile = (CustomerProfile) getArguments().getSerializable("customerDetail");


        reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,customerProfile);

   /*     binding.paymentOption.setVisibility(View.GONE);
        binding.cashpmtdetail.setVisibility(View.GONE);

        binding.chequeDetail.setVisibility(View.GONE);
        binding.splitheader.setVisibility(View.GONE);
        binding.splitdata.setVisibility(View.GONE);*/

        binding.Deposit.setChecked(true);
        binding.cash.setChecked(true);
        binding.splitpercentage.setChecked(true);
        binding.chequeDetail.setVisibility(View.GONE);
        binding.splitAmount.setChecked(true);


        binding.transationtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.Payment:
                        Log.e(TAG, "onCheckedChanged: " + "Payment" );
                        break;

                    case R.id.Deposit:
                        Log.e(TAG, "onCheckedChanged: " + "Deposit" );
                        break;

                    case R.id.preAuthorization:
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
                        binding.cashpmtdetail.setVisibility(View.VISIBLE);
                        binding.splitheader.setVisibility(View.VISIBLE);
                        binding.splitdata.setVisibility(View.VISIBLE);
                        binding.chequeDetail.setVisibility(View.GONE);
                        break;

                    case R.id.cheque:
                        binding.cashpmtdetail.setVisibility(View.GONE);
                        binding.chequeDetail.setVisibility(View.VISIBLE);
                        binding.splitheader.setVisibility(View.VISIBLE);
                        binding.splitdata.setVisibility(View.VISIBLE);
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
                            Double amt = Double.valueOf(binding.amount.getText().toString());
                            int valuee = Integer.valueOf(binding.lessamount.getText().toString());
                            Double rate = Double.valueOf(amt * valuee / 100);
                            binding.amountPayable.setText(Helper.getAmtount(rate));
                            reservationPMT.SplitAmountType = SplitAmountType.Percentage.inte;
                        }
                        break;

                    case R.id.splitfixamount:
                        binding.currency.setText("USD $");
                        if  (!binding.amount.getText().toString().trim().isEmpty()) {
                            Double amt = Double.valueOf(binding.amount.getText().toString());
                            int valuee = Integer.valueOf(binding.lessamount.getText().toString());
                            Double rate = Double.valueOf(amt - valuee);
                            binding.amountPayable.setText(Helper.getAmtount(rate));
                            reservationPMT.SplitAmountType = SplitAmountType.Amount.inte;
                        }
                        break;
                }
            }
        });

        binding.splitAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                    Double amt = Double.valueOf(binding.amount.getText().toString());
                    int valuee = Integer.valueOf(String.valueOf(s));
                    if (binding.splitpercentage.isChecked()) {
                        Double rate = Double.valueOf(amt * valuee / 100);
                        binding.amountPayable.setText(Helper.getAmtount(rate));
                    } else {
                        Double rate = Double.valueOf(amt - valuee);
                        binding.amountPayable.setText(Helper.getAmtount(rate));
                    }
                    /*if (binding.splitAmount.isChecked()) {
                        Double rate = Double.valueOf(amt - valuee);
                        binding.amountPayable.setText(rate.toString());
                    }*/
                }
            }
        });

        if (getArguments().getBoolean("pmt")){
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
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PaymentAccept:
                getModel();
                pmtList.add(reservationPMT);
                //new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST, RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);
                bundle.putSerializable("pmtmodel", reservationPMT);
                bundle.putString("netrate",binding.amountPayable.getText().toString() );
                Helper.pmt = true;
                if (getArguments().getBoolean("pmt")){
                    NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_Payment,bundle);
                } else {
                    NavHostFragment.findNavController(Fragment_Change_Payment_Option.this).navigate(R.id.paymentChangeOption_to_PaymentOffline,bundle);
                }

            break;

            case R.id.chequeDate:
                dialog.getFullDate(dialog.getToday(), "", string -> binding.chequeDate.setText(string));
                break;
        }

    }

    private void getModel(){
      reservationPMT.IsSplit = binding.splitAmount.isChecked();
      reservationPMT.Amount = Double.valueOf(binding.amountPayable.getText().toString());
      reservationPMT.AgreementNumber = reservationSummarry.ReservationNo;
      reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,customerProfile);
      reservationPMT.BillTo = 1;
      reservationPMT.CustomerId = customerProfile.Id;
      reservationPMT.InvoiceAmount = reservationPMT.Amount;
      reservationPMT.ReservationId = reservationSummarry.Id;
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
