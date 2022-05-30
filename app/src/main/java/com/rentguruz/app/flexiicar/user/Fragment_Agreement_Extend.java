package com.rentguruz.app.flexiicar.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAgreementExtendBinding;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Agreement_Extend extends BaseFragment {

    FragmentAgreementExtendBinding binding;
    Reservation reservations;
    SummaryDisplay summaryDisplay;
    ReservationSummarry  reservationSummarry;
    private static Boolean changeDate = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementExtendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeDate = false;
        binding.setUiColor(UiColor);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reservationSummarry = new ReservationSummarry();
        binding.header.screenHeader.setText(getResources().getString(R.string.extendagreement));
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        summaryDisplay = new SummaryDisplay(getActivity());
        try {
            reservations = new Reservation();
            reservations = (Reservation) getArguments().getSerializable("reservation");
            binding.reservation.setReservation(reservations);
            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");
            new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_Extend.this).popBackStack();
                break;
          /*  case R.id.next:
                NavHostFragment.findNavController(Fragment_Agreement_Extend.this).navigate(R.id.test);
                break;*/
        }
    }

    OnResponseListener SummaryCharge = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray summarry = resultSet.getJSONArray("ReservationSummaryModels");
                        JSONObject time = resultSet.getJSONObject("ReservationTimeModel");
                        ReservationTimeModel reservationTimeModel = (ReservationTimeModel) loginRes.getModel(time.toString(), ReservationTimeModel.class);
                        ReservationSummaryModels[] charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);

                        if (changeDate) {
                            try {
                                binding.second.mileage.setText(summaryDisplay.getMileage(charges));
                                binding.second.days.setText(reservationTimeModel.TotalDays + " Days");
                                binding.second.totalamt.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100)), false));
                                binding.second.paymentmade.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 103)), false));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                changeDate = true;
                                binding.first.mileage.setText(summaryDisplay.getMileage(charges));
                                binding.first.days.setText(reservationTimeModel.TotalDays + " Days");
                                binding.first.totalamt.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100)), false));
                                binding.first.paymentmade.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 103)), false));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };
}