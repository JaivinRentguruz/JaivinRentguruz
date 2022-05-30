package com.rentguruz.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import com.rentguruz.app.R;
import com.bumptech.glide.Glide;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleCurrentRentalBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.display.Pickupdrop;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationSummaryModels;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Resevation_Summarry  extends BaseFragment  {

    FragmentVehicleCurrentRentalBinding binding;

    ReservationSummarry reserversationSummary = new ReservationSummarry();
    VehicleModel model = new VehicleModel();
    SummaryDisplay summaryDisplay;
    ReservationSummaryModels[] charges;
    Pickupdrop pickupdrop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleCurrentRentalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(companyLabel.Reservation +" " + "Confirm");
        binding.btmcharges.text.setText("Home");
        //binding.header.optionmenu.setVisibility(View.VISIBLE);
        binding.header.optionmenu.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.option.cancel.setOnClickListener(this);
        summaryDisplay = new SummaryDisplay(getActivity());
        pickupdrop = new Pickupdrop();
        binding.option.AgreementMenu.setVisibility(View.GONE);
        //binding.btmcharges.next.setGravity(Gravity.CENTER);
        getVisibility(binding.reservationDetail.rlSummaryofcharge);
        reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");

      /*  new ApiService(getTaxtDetails, RequestType.GET,
                RESERVATIONGETBYID+"?id="+reserversationSummary.Id , BASE_URL_LOGIN, header, new JSONObject());*/
        apiService = new ApiService(getReservation, RequestType.GET,
                RESERVATIONGETBYID , BASE_URL_LOGIN, header,"?id="+reserversationSummary.Id);

        binding.btmcharges.layoutPayment.setOnClickListener(this);
        binding.btmcharges.layoutPayment.setOnClickListener(this);
        binding.reservationDetail.opensummary.setOnClickListener(this);
        binding.reservationDetail.openDriver.setOnClickListener(this);
        binding.reservationDetail.lblSelfCheckout.setOnClickListener(this);
        binding.reservationDetail.termCondition.setOnClickListener(this);

//        new ApiService(,RequestType.GET,CHECKOUTTIME,BASE_URL_LOGIN,header,)

      //  binding.reservationDetail.carcheckoutstatus.setText("Self checkout is available "+ UserData.companyModel.CompanyPreference.RsvReadyForCheckoutHoursValue.Value + " Hours prior tothe checkout time. We will notify you as soon the self checkout is available.  ");
        try {
            binding.reservationDetail.carcheckoutstatus.setText("Self "+companyLabel.CheckOut +" is available "+ UserData.companyModel.CompanyPreference.RsvReadyForCheckoutHoursValue.Value + " Hours prior tothe "+companyLabel.CheckOut +" time. We will notify you as soon the self "+companyLabel.CheckOut +" is available.  ");

        } catch (Exception e){
            e.printStackTrace();
        }


        try {
           /* bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
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
            bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));*/
            Customer customer = new Customer();
            customer = (Customer) getArguments().getSerializable("customer");
            binding.reservationDetail.textVDriverName.setText(customer.FullName);
            binding.reservationDetail.TextVDriverEmail.setText(customer.Email);
            binding.reservationDetail.TextVDriverPhoneno.setText(customer.MobileNo);
            CustomBindingAdapter.capss(binding.reservationDetail.first, customer.FullName);

            VehicleModel  vehicleModel = new VehicleModel();
            vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
            LocationList pickuplocation = new LocationList();
            pickuplocation = (LocationList) getArguments().getSerializable("model");

            LocationList returnlocation = new LocationList();
            returnlocation = (LocationList) getArguments().getSerializable("models");
            pickupdrop.pickuploc = pickuplocation.Name;
            pickupdrop.droploc = returnlocation.Name;

            binding.scan.txtConformationNo.setText(reserversationSummary.ReservationNo);
            binding.btmcharges.fueltype.setText(Helper.fueltype);
            model = (VehicleModel) getArguments().getSerializable("Model");
            Glide.with(context).load(model.DefaultImagePath).into(binding.carimage);
            binding.VehicleTypeName.setText(model.VehicleName);
            binding.VehicleName.setText(model.VehicleCategory);

            Glide.with(context).load(model.DefaultImagePath).into(binding.pickup.carimage);
            binding.textViewSeats1.setText(String.valueOf(model.NoOfSeats));
            binding.textViewBags1.setText(String.valueOf(model.NoOfBags));
            binding.txtDoor1.setText(String.valueOf(model.NoOfDoors));
            binding.txtAuto.setText(model.TransmissionDesc);

        } catch (Exception e){
            e.printStackTrace();
        }



        try {
            if (reserversationSummary.ReservationFlightAndHotelModel.DepFlightNo != null){
                binding.reservationDetail.showFlightDetail.setVisibility(View.VISIBLE);
            } else {
                binding.reservationDetail.showFlightDetail.setVisibility(View.GONE);
            }
        }catch (Exception e){
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
            case R.id.layout_payment:
                Intent intent = new Intent(getActivity(), Activity_Home.class);
                startActivity(intent);
                getActivity().finish();
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Resevation_Summarry.this).popBackStack();
                break;

            case R.id.opensummary:
                getVisibility(binding.reservationDetail.rlSummaryofcharge);
                //binding.reservationDetail.rlSummaryofcharge.setVisibility(View.VISIBLE);
                break;

            case R.id.open_driver:
                if (binding.reservationDetail.driverDetails.getVisibility() == View.GONE) {
                    binding.reservationDetail.driverDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.reservationDetail.driverDetails.setVisibility(View.GONE);
                }
                break;

            case R.id.lblSelfCheckout:
                getVisibility(binding.reservationDetail.checkouts);
                break;
            case R.id.term_condition:
                //Fragment_Term_And_Condition.popbackstack = true;
                NavHostFragment.findNavController(Fragment_Resevation_Summarry.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Term_and_Condtion, bundle);
                break;


        }

    }

    OnResponseListener getTaxtDetails = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
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
                                ReservationSummarry reservationSummarry = new ReservationSummarry();
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                               // bundle.putSerializable("reservationD",reservationSummarry);
                                pickupdrop.pickupdate = reservationSummarry.CheckOutDate;
                                pickupdrop.dropdate = reservationSummarry.CheckOutDate;
                                pickupdrop.noDays = reservationSummarry.ReservationInsuranceModel.NoOfDays;
                                binding.pickup.setPickupdrop(pickupdrop);
                               // bundle.putSerializable("reservation",reservationSummarry);
                               // OptionMenu optionMenu = new OptionMenu(getActivity());
                               // optionMenu.optionmenulist(binding.sucessfullRegi,getView(),bundle,Fragment_Resevation_Summarry.this,header,params);
                                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

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
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

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
                               // reserversationSummary = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                try {
                                    Reservation r = new Reservation();
                                   // r = (Reservation)  loginRes.getModel(resultSet.toString(), Reservation.class);
                                   // bundle.putSerializable("reservation",r);
                                   // OptionMenu optionMenu = new OptionMenu(getActivity());
                                   // optionMenu.optionmenulist(binding.sucessfullRegi,getView(),bundle,Fragment_Resevation_Summarry.this,header,params);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                              /*  Reservation reservations = new Reservation();
                                reservations = (Reservation) getArguments().getSerializable("reservation");
                                pickupdrop = new Pickupdrop();*/

                                reserversationSummary = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                               // bundle.putSerializable("reservationD",reserversationSummary);
                                pickupdrop.pickupdate = reserversationSummary.CheckOutDate;
                                pickupdrop.dropdate = reserversationSummary.CheckOutDate;
                                pickupdrop.noDays = reserversationSummary.ReservationInsuranceModel.NoOfDays;
                                binding.pickup.setPickupdrop(pickupdrop);

                                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);

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

    public void getVisibility (View Display){
        if (Display.getVisibility() == View.GONE){
            Display.setVisibility(View.VISIBLE);
        } else {
            Display.setVisibility(View.GONE);
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
                        charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);
                        summaryDisplay.getB2BSummarry(bundle,charges,binding.reservationDetail.rlSummaryofcharge);
                       // binding.btmcharges.currency.setVisibility(View.GONE);
                        binding.btmcharges.txtMileage.setText(summaryDisplay.getMileage(charges));
                        binding.btmcharges.textviewTotalAmount.setText(Helper.getAmtount(Double.valueOf(summaryDisplay.getDatafrom(charges, 100))));
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
