package com.abel.app.b2b.home.vehicles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleBasicDetailsBinding;
import com.abel.app.b2b.databinding.ReservationListBinding;
import com.abel.app.b2b.databinding.VehicleCommonListBinding;
import com.abel.app.b2b.model.response.Reservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETALL;

public class Vehicle_Master_Reservation extends BaseFragment {

    VehicleCommonListBinding binding;
    Reservation[] reservations;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = VehicleCommonListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.header.screenHeader.setText(companyLabel.Reservation);



        ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(false,true, getArguments().getInt("Idd")));
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
                NavHostFragment.findNavController(Vehicle_Master_Reservation.this).popBackStack();
                break;
        }
    }

    OnResponseListener GetReservationlist = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        fullProgressbar.hide();
                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray getReservationList = resultSet.getJSONArray("Data");
                            reservations = loginRes.getModel(getReservationList.toString(), Reservation[].class);
                            int len;
                            len = reservations.length;
                            binding.listItem.removeAllViews();
                            for (int j = 0; j < len; j++)
                            {
                                getSubview(j);
                                ReservationListBinding reservationListBinding = ReservationListBinding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                reservationListBinding.getRoot().setId(200 + j);
                                reservationListBinding.getRoot().setLayoutParams(subparams);
                                reservationListBinding.setReservation(reservations[j]);
                                Log.d(TAG, "run: " + reservations[j]._date);
                                int finalJ = j;

                              /*  reservationListBinding.ReservationListLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putInt("Id",reservations[finalJ].Id);
                                        Log.d(TAG, "run: " + reservations[finalJ].Id);
                                        bundle.putSerializable("reservation",reservations[finalJ]);
                                  *//*      NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*//*
                                      *//*  NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*//*
                                    }
                                });*/
                                binding.listItem.addView(reservationListBinding.getRoot());
                                fullProgressbar.hide();

                            }
                        }
                        else
                        {
                            fullProgressbar.hide();
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                        // showReservation(reservationList);
                    }
                    catch (Exception e)
                    {
                        fullProgressbar.hide();
                        e.printStackTrace();
                    }
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
