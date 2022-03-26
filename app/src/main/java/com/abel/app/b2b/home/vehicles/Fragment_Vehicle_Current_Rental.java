package com.abel.app.b2b.home.vehicles;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.DigitConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleCurrentRentalBinding;
import com.abel.app.b2b.databinding.VehicleTaxDetailsBinding;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.UpdateDL;

import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETSINGLELICENCE;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETBYID;

public class Fragment_Vehicle_Current_Rental extends BaseFragment {

    FragmentVehicleCurrentRentalBinding binding;
    ReservationSummarry reserversationSummary = new ReservationSummarry();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleCurrentRentalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.header.screenHeader.setText("Current Rantal");
       /* binding.btmcharges.next.setText(getResources().getString(R.string.ok));
        binding.btmcharges.next.setGravity(Gravity.CENTER);*/

        binding.header.back.setOnClickListener(this);

        try {
            reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");


            apiService = new ApiService(getReservation, RequestType.GET,
                    RESERVATIONGETBYID , BASE_URL_LOGIN, header,"?id="+reserversationSummary.Id);
        } catch (Exception e){
            e.printStackTrace();
        }



      //  binding.btmcharges.next.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Current_Rental.this).popBackStack();
                break;
        }

    }

    OnResponseListener getReservation = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                reserversationSummary = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
/*
                                binding.pickup.textViewPickupLocation.setText(reserversationSummary.PickUpLocationName);
                                binding.pickup.textViewPickupLocationDate.setText(Helper.getDateDisplay(DateType.fulldate,reserversationSummary.CheckOutDate));
                                binding.pickup.textViewPickupLocationTime.setText(Helper.getTimeDisplay(DateType.fulldate,reserversationSummary.CheckOutDate));

                                binding.pickup.textViewReturnLocationName.setText(reserversationSummary.DropLocationName);
                                binding.pickup.textViewPickupLocationDate.setText(Helper.getDateDisplay(DateType.fulldate,reserversationSummary.CheckInDate));
                                binding.pickup.textViewReturnLocationTime.setText(Helper.getTimeDisplay(DateType.fulldate,reserversationSummary.CheckInDate));*/

                                binding.reservationDetail.rlSummaryofcharge.setVisibility(View.VISIBLE);
                                binding.reservationDetail.rlSummaryofcharge.removeAllViews();

                             //   binding.pickup.txtTotalDay1.setText(String.valueOf(reserversationSummary.ReservationInsuranceModel.NoOfDays));

                                binding.scan.txtConformationNo.setText(reserversationSummary.ReservationNo);

                                //  charges = reserversationSummary.ReservationSummaryModels;
                                for (int i = 0; i <reserversationSummary.ReservationSummaryModels.size() ; i++) {

                                    getSubview(i);
                                    VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                                    vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                                    vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                                    vehicleTaxDetailsBinding.txtChargeName.setText(reserversationSummary.ReservationSummaryModels.get(i).SummaryName);
                                    //vehicleTaxDetailsBinding.txtCharge.setText(charges[i].TotalAmount.toString());
                                    vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount(reserversationSummary.ReservationSummaryModels.get(i).TotalAmount, false));
                                    int finalI = i;
                                    vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            } else {
                                                vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                                String join ="";
                                                for (int j = 0; j <reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels.length ; j++) {
                                                    // vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    //         charges[finalI].ReservationSummaryDetailModels[j].Description);
                                                    join +=  reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Title + " "+
                                                            reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Description+"\n";
                                                    if (join.trim().matches("null null"))
                                                        vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                                    vehicleTaxDetailsBinding.txtDisc.setText(join.trim());

                                                }

                                                for (int j = 0; j < reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels.length; j++) {
                                                    if (reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].ReservationSummaryDetailType == 101)
                                                        binding.btmcharges.txtMileage.setText( Helper.getDistance(reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[j].Total));

                                                    if (reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryType==100){
                                                        binding.btmcharges.textviewTotalAmount.setText(DigitConvert.getDoubleDigit(reserversationSummary.ReservationSummaryModels.get(finalI).ReservationSummaryDetailModels[0].Total));
                                                    }
                                                }


                                                //vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                            }
                                        }
                                    });
                                    binding.reservationDetail.rlSummaryofcharge.addView(vehicleTaxDetailsBinding.getRoot());
                                }

                                try {

                                    apiService = new ApiService(new OnResponseListener() {
                                        @Override
                                        public void onSuccess(String response, HashMap<String, String> headers) {
                                            handler.post(() -> {
                                                try {
                                                    System.out.println("Success");
                                                    System.out.println(response);
                                                    JSONObject responseJSON = new JSONObject(response);
                                                    Boolean status = responseJSON.getBoolean("Status");
                                                    fullProgressbar.hide();
                                                    if (status)
                                                    {
                                                        JSONObject data = responseJSON.getJSONObject("Data");
                                                        UpdateDL updateDL = new UpdateDL();
                                                        updateDL = loginRes.getModel(data.toString(), UpdateDL.class);
                                                        binding.reservationDetail.textVDriverName.setText(updateDL.FullName);
                                                        //binding.reservationDetail
                                                        CustomBindingAdapter.caps(binding.reservationDetail.first,updateDL.FName);
                                                        CustomBindingAdapter.caps(binding.reservationDetail.second,updateDL.LName);
                                                        binding.reservationDetail.TextVDriverPhoneno.setText(updateDL.MobileNo);
                                                        binding.reservationDetail.TextVDriverEmail.setText(updateDL.Email);

                                                        /*driverName.setText(updateDL.FullName);
                                                        driverPhone.setText(updateDL.MobileNo);
                                                        driverEmail.setText(updateDL.Email);*/
                                                       /* if (!updateDL.FullName.isEmpty()){
                                                            driverdetails_icon.setOnClickListener(new View.OnClickListener()
                                                            {
                                                                @Override
                                                                public void onClick(View view)
                                                                {
                                                                    if (driverdetails1.getVisibility() == View.GONE)
                                                                    {
                                                                        driverdetails1.setVisibility(View.VISIBLE);
                                                                    }
                                                                    else {
                                                                        driverdetails1.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            getView().findViewById(R.id.additionalDrivers).setVisibility(View.GONE);
                                                        }*/
                                                    }
                                                    else
                                                    {
                                                        fullProgressbar.hide();
                                                        String msg = responseJSON.getString("Message");
                                                        CustomToast.showToast(getActivity(),msg,1);
                                                    }
                                                } catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                    fullProgressbar.hide();
                                                }

                                            });
                                        }

                                        @Override
                                        public void onError(String error) {
                                            fullProgressbar.hide();
                                        }
                                    }, RequestType.GET, GETSINGLELICENCE, BASE_URL_LOGIN, header, "?Id=" + reserversationSummary.ReservationDriversModel.get(0).DriverId);

                                } catch (Exception e){
                                    // getView().findViewById(R.id.additionalDrivers).setVisibility(View.GONE);
                                    e.printStackTrace();
                                    fullProgressbar.hide();
                                }

                                fullProgressbar.hide();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                fullProgressbar.hide();
                            }
                        }

                        else
                        {
                            fullProgressbar.hide();
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        fullProgressbar.hide();
                    }
                }
            });
        }
        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
            fullProgressbar.hide();
        }
    };
}
