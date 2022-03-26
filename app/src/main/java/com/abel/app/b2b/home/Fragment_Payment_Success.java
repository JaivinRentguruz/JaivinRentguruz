package com.abel.app.b2b.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentPaymentSuccessBinding;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

public class Fragment_Payment_Success extends BaseFragment {

    FragmentPaymentSuccessBinding binding;
    ReservationSummarry reserversationSummary = new ReservationSummarry();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.header.screenHeader.setText("Payment Success");
        binding.header.back.setVisibility(View.GONE);
        binding.header.discard.setVisibility(View.GONE);
        binding.header.screenHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);

        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));

        reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");

        binding.txtMessage.setText(" Your payment is processed successfully. Your payment reference number is " + reserversationSummary.ReservationNo + ". Confirmation email is been sent to " + reserversationSummary.CustomerEmail + ". Please call customer service for any changes or cancellations.  ");

        binding.txtAmtPayable.setText(Helper.getAmtount(Double.valueOf(getArguments().getString("netrate")),false));

        binding.next.setVisibility(View.VISIBLE);
        binding.next.setOnClickListener(this);

        try {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        CustomBindingAdapter.loadImage(binding.icon,loginRes.getData(getResources().getString(R.string.icon)));
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.next:
                NavHostFragment.findNavController(Fragment_Payment_Success.this).navigate(R.id.payment_sucess_to_reservation_summarry,bundle);
                break;

        }
    }
}
