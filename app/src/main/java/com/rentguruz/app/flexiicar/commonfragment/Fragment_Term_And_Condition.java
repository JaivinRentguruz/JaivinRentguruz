package com.rentguruz.app.flexiicar.commonfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentTermsAndConditionsBinding;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.TERMSCONDITION;

public class Fragment_Term_And_Condition extends BaseFragment
{
   // Handler handler = new Handler();
    public static Context context;
    public String id = "";
    TextView txt_TCName,txt_TCDesc,txt_Cancel;
    int termcondition;
    ImageView Back_arrow;
    Bundle BookingBundle,VehicleBundle,returnLocationBundle,locationBundle;
    Boolean locationType,initialSelect;

    FragmentTermsAndConditionsBinding binding;
    public static Boolean popbackstack = false;
    private int documnetType = 4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentTermsAndConditionsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.terms_amp_conditions));
       /* txt_TCName=view.findViewById(R.id.txt_terms_Cond_Name);
        txt_TCDesc=view.findViewById(R.id.txt_term_Cond_Desc);
        Back_arrow=view.findViewById(R.id.Back_arrowTC);
        txt_Cancel=view.findViewById(R.id.txt_Cancel);

        termcondition=getArguments().getInt("termcondition");

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        txt_Cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //For Booking
                if(termcondition==1)
                {
                    BookingBundle = getArguments().getBundle("BookingBundle");
                    VehicleBundle = getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle",VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    BookingBundle.putInt("BookingStep", 4);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_Term_and_Condtion_to_Finalize_your_rental,Booking);
                }

                //For User Reservation
                if(termcondition==2)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");

                    Bundle ReservationBundle = new Bundle();
                    ReservationBundle.putBundle("ReservationBundle", Reservation);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfCharges,ReservationBundle);
                }
                //For User Agreements
                if(termcondition==3)
                {
                    Bundle Agreements=getArguments().getBundle("AgreementsBundle");

                    Bundle AgreementsBundle = new Bundle();
                    AgreementsBundle.putBundle("AgreementsBundle", Agreements);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfChargesForAgreements,AgreementsBundle);
                }
                //For Booking
                if(termcondition==4)
                {
                    Bundle BookingBundle = getArguments().getBundle("BookingBundle");
                    Bundle VehicleBundle = getArguments().getBundle("VehicleBundle");
                    Bundle Booking = new Bundle();
                    Booking.putInt("BookingStep", 6);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_Term_and_Condtion_to_Summary_Of_Charges,Booking);
                }
                //For User Reservation Finalize Your Rental
                if(termcondition==5)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");

                    Bundle ReservationBundle = new Bundle();
                    ReservationBundle.putBundle("ReservationBundle", Reservation);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_Finalize_your_rental,ReservationBundle);
                }
                //For Summary Of Charges SelfCheckIn
                if(termcondition==6)
                {

                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfChargeForSelfCheckIn,Agreements);
                }
            }
        });

        Back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //For Booking
                if(termcondition==1)
                {
                    BookingBundle = getArguments().getBundle("BookingBundle");
                    VehicleBundle = getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle",VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    BookingBundle.putInt("BookingStep", 4);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_Term_and_Condtion_to_Finalize_your_rental,Booking);
                }

                //For User Reservation
                if(termcondition==2)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");

                    Bundle ReservationBundle = new Bundle();
                    ReservationBundle.putBundle("ReservationBundle", Reservation);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfCharges,ReservationBundle);
                }
                //For User Agreements
                if(termcondition==3)
                {
                    Bundle Agreements=getArguments().getBundle("AgreementsBundle");

                    Bundle AgreementsBundle = new Bundle();
                    AgreementsBundle.putBundle("AgreementsBundle", Agreements);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfChargesForAgreements,AgreementsBundle);
                }
                //For Booking
                if(termcondition==4)
                {
                    Bundle BookingBundle = getArguments().getBundle("BookingBundle");
                    Bundle VehicleBundle = getArguments().getBundle("VehicleBundle");
                    Bundle Booking = new Bundle();
                    Booking.putInt("BookingStep", 6);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_Term_and_Condtion_to_Summary_Of_Charges,Booking);
                }
                //For User Reservation Finalize Your Rental
                if(termcondition==5)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");

                    Bundle ReservationBundle = new Bundle();
                    ReservationBundle.putBundle("ReservationBundle", Reservation);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_Finalize_your_rental,ReservationBundle);
                }
                //For Summary Of Charges SelfCheckIn
                if(termcondition==6)
                {

                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_TermAndCondition_to_SummaryOfChargeForSelfCheckIn,Agreements);
                }

            }
        });

        JSONObject bodyParam = new JSONObject();
        try
        {
            bodyParam.accumulate("customerId",id);
            System.out.println(bodyParam);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getActivity());
        Fragment_Term_And_Condition.context = getActivity();

        ApiService ApiService = new ApiService(GetTermsCondition, RequestType.GET,
                GETTERMSCONDITION, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);*/
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.discard.setVisibility(View.GONE);
        binding.next.setOnClickListener(this);

        try {
            bundle.putSerializable("timemodel",getArguments().getSerializable("timemodel"));
            bundle.putSerializable("pickuploc", getArguments().getSerializable("pickuploc"));
            bundle.putSerializable("droploc", getArguments().getSerializable("droploc"));
            bundle.putSerializable("Model",getArguments().getSerializable("Model"));
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
            bundle.putSerializable("model",getArguments().getSerializable("model"));
            bundle.putSerializable("models", getArguments().getSerializable("models"));
            bundle.putString("DeliveryAndPickupModel", getArguments().getString("DeliveryAndPickupModel"));
            bundle.putSerializable("reservationSum",getArguments().getSerializable("reservationSum"));
            bundle.putString("insuranceOption",getArguments().getString("insuranceOption"));
            bundle.putString("testSummerry",getArguments().getString("testSummerry"));
            Log.d(TAG, "onViewCreated: " + TAG);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }

        try {
            LocationList pickuplocation = new LocationList();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            String id = sp.getString(getString(R.string.id), "");
            String serverpath = sp.getString("serverPath", "");
            pickuplocation = (LocationList) getArguments().getSerializable("pickuploc");
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
            bundle.putSerializable("LicenseBundle",getArguments().getSerializable("LicenseBundle"));


            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                    Log.d("TAG", "onSuccess: " + response);
                                    JSONObject responseJSON = new JSONObject(response);
                                    JSONObject data = responseJSON.getJSONObject("Data");
                                    String  Description = data.getString("Description");
                                    binding.txtTermCondDesc.setText(Html.fromHtml(Description));

                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, TERMSCONDITION, BASE_URL_LOGIN, header, params.gettermscondition(pickuplocation.Id, 2, documnetType));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            ReservationSummarry reservationSummarry = new ReservationSummarry();
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationD");
            popbackstack = true;
            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("TAG", "onSuccess: " + response);
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                if (status)
                                {
                                    try
                                    {
                                        JSONObject data = responseJSON.getJSONObject("Data");
                                        String  Description = data.getString("Description");
                                        binding.txtTermCondDesc.setText(Html.fromHtml(Description));
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
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
            }, RequestType.POST, TERMSCONDITION, BASE_URL_LOGIN, header, params.gettermscondition(reservationSummarry.PickUpLocation, 2, documnetType));

        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            Reservation reservation = new Reservation();
            reservation = (Reservation) getArguments().getSerializable("reservation");
            documnetType = 3;
            popbackstack = true;
            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("TAG", "onSuccess: " + response);
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                if (status)
                                {
                                    try
                                    {
                                        JSONObject data = responseJSON.getJSONObject("Data");
                                        String  Description = data.getString("Description");
                                        binding.txtTermCondDesc.setText(Html.fromHtml(Description));
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
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
            }, RequestType.POST, TERMSCONDITION, BASE_URL_LOGIN, header, params.gettermscondition(reservation.PickUpLocation, 2, documnetType));

        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            ReservationSummarry reservation = new ReservationSummarry();
            reservation = (ReservationSummarry) getArguments().getSerializable("reservationSum");
            documnetType = 3;
            popbackstack = true;
            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("TAG", "onSuccess: " + response);
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                if (status)
                                {
                                    try
                                    {
                                        JSONObject data = responseJSON.getJSONObject("Data");
                                        String  Description = data.getString("Description");
                                        binding.txtTermCondDesc.setText(Html.fromHtml(Description));
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
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
            }, RequestType.POST, TERMSCONDITION, BASE_URL_LOGIN, header, params.gettermscondition(reservation.PickUpLocation, 2, documnetType));

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    OnResponseListener GetTermsCondition = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONArray getInsuranceDEtails = resultSet.getJSONArray("t0040_Terms_Condition_Master");

                                int len;
                                len = getInsuranceDEtails.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getInsuranceDEtails.get(j);

                                    int terms_Cond_ID=test.getInt("terms_Cond_ID");
                                    String terms_Cond_Name=test.getString("terms_Cond_Name");
                                    String term_Cond_Desc=test.getString("term_Cond_Desc");

                                    txt_TCName.setText(terms_Cond_Name);
                                    txt_TCDesc.setText(term_Cond_Desc);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discard:
            case R.id.back:

                if (popbackstack){
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this).popBackStack();
                } else {
                    NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                            .navigate(R.id.action_Term_and_Condtion_to_Finalize_your_rental, bundle);
                }
                break;
            case R.id.next:
                if (binding.btnAccept.isChecked()) {
                    if (popbackstack) {
                        NavHostFragment.findNavController(Fragment_Term_And_Condition.this).popBackStack();
                    } else {
                        NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                                .navigate(R.id.action_Term_and_Condtion_to_Finalize_your_rental, bundle);
                    }
                } else {
                    CustomToast.showToast(getActivity(),"Please Accept Terms & Conditions", 1);
                }

                break;
          /*
                NavHostFragment.findNavController(Fragment_Term_And_Condition.this)
                        .navigate(R.id.action_Term_and_Condtion_to_Search_activity);
                break;*/
        }
    }
    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

}


