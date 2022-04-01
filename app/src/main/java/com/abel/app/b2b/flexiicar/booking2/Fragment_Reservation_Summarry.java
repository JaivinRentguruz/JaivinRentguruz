package com.abel.app.b2b.flexiicar.booking2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.abel.app.b2b.adapters.OptionMenu;
import com.abel.app.b2b.home.Fragment_Resevation_Summarry;
import com.abel.app.b2b.model.response.Reservation;
import com.bumptech.glide.Glide;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.SummaryDisplay;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehicleCurrentRentalBinding;
import com.abel.app.b2b.databinding.VehicleTaxDetailsBinding;
import com.abel.app.b2b.flexiicar.commonfragment.Fragment_Term_And_Condition;
import com.abel.app.b2b.flexiicar.user.User_Profile;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.display.Pickupdrop;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationSummaryModels;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.CHECKOUTTIME;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETBYID;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Reservation_Summarry  extends BaseFragment {

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

        binding.header.screenHeader.setText("Booking Confirm");
        binding.btmcharges.text.setText("Home");

        binding.header.screenHeader.setText(companyLabel.Reservation +" " + "Confirm");
        binding.btmcharges.text.setText("Home");

        binding.header.optionmenu.setVisibility(View.VISIBLE);
        binding.header.optionmenu.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.option.cancel.setOnClickListener(this);
        summaryDisplay = new SummaryDisplay(getActivity());
        pickupdrop = new Pickupdrop();
        Log.e(TAG, "onViewCreated: "+getArguments().getString("miles") );
        binding.option.AgreementMenu.setVisibility(View.GONE);
      /*  try {
            //reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");


            apiService = new ApiService(getReservation, RequestType.GET,
                    RESERVATIONGETBYID , BASE_URL_LOGIN, header,"?id="+reserversationSummary.Id);
        } catch (Exception e){
            e.printStackTrace();
        }*/

        try {

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            String id = sp.getString(getString(R.string.id), "");
            String serverpath = sp.getString("serverPath", "");

            reserversationSummary = (ReservationSummarry) getArguments().getSerializable("summarry");
            bundle.putSerializable("pickuploc", getArguments().getSerializable("pickuploc"));
            bundle.putSerializable("droploc", getArguments().getSerializable("droploc"));
            bundle.putSerializable("ratemaster", getArguments().getSerializable("ratemaster"));
            bundle.putSerializable("vechicle", getArguments().getSerializable("vechicle"));
            bundle.putString("netrate",getArguments().getString("netrate"));
            //bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("defaultcard",getArguments().getSerializable("defaultcard"));
            bundle.putInt("frag",2);
            bundle.putSerializable("summarry",getArguments().getSerializable("summarry") );
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("charges",getArguments().getSerializable("charges"));


            model = (VehicleModel) getArguments().getSerializable("vechicle");
            Glide.with(context).load(model.DefaultImagePath).into(binding.carimage);
            binding.VehicleTypeName.setText(model.VehicleName);
            binding.VehicleName.setText(model.VehicleCategory);

            Glide.with(context).load(model.DefaultImagePath).into(binding.pickup.carimage);

            binding.pickup.TxtDays.setText(String.valueOf(UserReservationData.reservationTimeModel.TotalDays));
            // binding.txtVehicleTypeName.setText(model.VehicleShortName);
            //binding.txtMileage.setText(model.NoOfSeats + "kms");
            binding.btmcharges.fueltype.setText(Helper.fueltype);
            //binding.btmcharges.txtMileage.setText("Unlimited");
            binding.btmcharges.txtMileage.setText(getArguments().getString("miles"));
            binding.btmcharges.textviewTotalAmount.setText(getArguments().getString("netrate"));
//            binding.btmcharges.currency.setText(Helper.currencySymbol);

            LocationList pickuplocation = new LocationList();
            LocationList droplocation = new LocationList();

            pickuplocation = (LocationList) getArguments().getSerializable("pickuploc");
            droplocation = (LocationList) getArguments().getSerializable("droploc");
            binding.pickup.txtPickupLocationName.setText(pickuplocation.Name);
            binding.pickup.txtReturnLocationName.setText(droplocation.Name);
           // binding.pickup.txtPickupdate1.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")));
           // binding.pickup.txtPICKUPTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
           // binding.pickup.txtREturnDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")));
            //binding.pickup.txtREturnTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));

            pickupdrop.pickuploc = pickuplocation.Name;
            pickupdrop.droploc = droplocation.Name;


            binding.scan.txtConformationNo.setText(reserversationSummary.ReservationNo);


            binding.textViewSeats1.setText(String.valueOf(model.NoOfSeats));
            binding.textViewBags1.setText(String.valueOf(model.NoOfBags));
            binding.txtDoor1.setText(String.valueOf(model.NoOfDoors));
            binding.txtAuto.setText(model.TransmissionDesc);
            binding.btmcharges.fueltype.setText(Helper.fueltype);

            binding.reservationDetail.textVDriverName.setText(UserData.loginResponse.LogedInCustomer.FullName);
            binding.reservationDetail.TextVDriverEmail.setText(UserData.loginResponse.LogedInCustomer.Email);
            binding.reservationDetail.TextVDriverPhoneno.setText(UserData.loginResponse.LogedInCustomer.MobileNo);
            CustomBindingAdapter.capss(binding.reservationDetail.first, UserData.loginResponse.LogedInCustomer.FullName);

            binding.reservationDetail.getRoot();
            binding.reservationDetail.rlSummaryofcharge.setVisibility(View.VISIBLE);
            ReservationSummaryModels[] charges;
            charges = (ReservationSummaryModels[]) getArguments().getSerializable("charges");
            //summaryDisplay.getB2BSummarry(bundle,charges,binding.reservationDetail.rlSummaryofcharge);

            new ApiService(getTaxtDetails, RequestType.GET,
                    RESERVATIONGETBYID+"?id="+reserversationSummary.Id , BASE_URL_LOGIN, header, new JSONObject());


            try {
               if (reserversationSummary.ReservationFlightAndHotelModel.DepFlightNo != null){
                   binding.reservationDetail.showFlightDetail.setVisibility(View.VISIBLE);
               } else {
                   binding.reservationDetail.showFlightDetail.setVisibility(View.GONE);
               }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        binding.btmcharges.layoutPayment.setOnClickListener(this);
        binding.reservationDetail.opensummary.setOnClickListener(this);
        binding.reservationDetail.openDriver.setOnClickListener(this);
        binding.reservationDetail.lblSelfCheckout.setOnClickListener(this);
        binding.reservationDetail.termCondition.setOnClickListener(this);

//        new ApiService(,RequestType.GET,CHECKOUTTIME,BASE_URL_LOGIN,header,)

        //binding.reservationDetail.carcheckoutstatus.setText("Self checkout is available "+ UserData.companyModel.CompanyPreference.RsvReadyForCheckoutHoursValue.Value + " Hours prior tothe checkout time. We will notify you as soon the self checkout is available.  ");
        try {
            binding.reservationDetail.carcheckoutstatus.setText("Self "+companyLabel.CheckOut +" is available "+ UserData.companyModel.CompanyPreference.RsvReadyForCheckoutHoursValue.Value + " Hours prior tothe "+companyLabel.CheckOut +" time. We will notify you as soon the self "+companyLabel.CheckOut +" is available.  ");

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
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Reservation_Summarry.this).popBackStack();
                break;

            case R.id.layout_payment:
                Intent intent = new Intent(getContext(), User_Profile.class);
                startActivity(intent);
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
                NavHostFragment.findNavController(Fragment_Reservation_Summarry.this)
                        .navigate(R.id.action_Finalize_your_rental_to_Term_and_Condtion, bundle);
                break;

            case R.id.optionmenu:
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                transition.addTarget( binding.sucessfullRegi);
                //binding.sucessfullRegi.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(binding.getRoot(),transition);
                binding.sucessfullRegi.setVisibility(View.VISIBLE);
                break;

            case R.id.cancel:
                Transition transition2 = new Slide(Gravity.BOTTOM);
                transition2.setDuration(600);
                transition2.addTarget( binding.sucessfullRegi);
                binding.sucessfullRegi.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(binding.getRoot(),transition2);
                binding.sucessfullRegi.setVisibility(View.GONE);
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
                                try {
                                    Reservation r = new Reservation();
                                    r = (Reservation)  loginRes.getModel(resultSet.toString(), Reservation.class);
                                    bundle.putSerializable("reservation",r);
                                    OptionMenu optionMenu = new OptionMenu(getActivity());
                                    optionMenu.optionmenulist(binding.sucessfullRegi,getView(),bundle, Fragment_Reservation_Summarry.this,header,params);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                reservationSummarry = (ReservationSummarry) loginRes.getModel(resultSet.toString(), ReservationSummarry.class);
                                bundle.putSerializable("reservationD",reservationSummarry);
                                pickupdrop.pickupdate = reservationSummarry.CheckOutDate;
                                pickupdrop.dropdate = reservationSummarry.CheckOutDate;
                                pickupdrop.noDays = reservationSummarry.ReservationInsuranceModel.NoOfDays;
                                binding.pickup.setPickupdrop(pickupdrop);

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
