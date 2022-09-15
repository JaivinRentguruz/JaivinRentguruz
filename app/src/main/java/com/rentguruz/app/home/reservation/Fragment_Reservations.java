package com.rentguruz.app.home.reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.model.ReservationStatusDetail;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.display.ThemeColors;
import com.rentguruz.app.model.parameter.ReservationStatuss;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentReservationsBinding;
import com.rentguruz.app.databinding.ReservationListBinding;
import com.rentguruz.app.home.Fragment_Selected_Location;
import com.rentguruz.app.model.response.Reservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETALL;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONSTATUS;

public class Fragment_Reservations extends BaseFragment {

    public static int total = 0;
    public static Context context;
    public String id = "";
   // Handler handler = new Handler(Looper.getMainLooper());
    FragmentReservationsBinding binding;
    Reservation[] reservations;
    List<Reservation>  reservationList;
    ImageLoader imageLoader;
    String serverpath = "";
    ProgressDialog progressDialog;
    int page = 0;
    int count = 0;
    public static boolean scroll = false;
    OptionMenu optionMenu;
    static ReservationStatusDetail[] reservationStatusDetails;
    ActionBarDrawerToggle actionBarDrawerToggle;
    List<ThemeColors> colors = new ArrayList<>();
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationList = new ArrayList<>();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setPadding(40,0,0,0);
        binding.header.discard.setOnClickListener(this);
        binding.header.discard.setText(getResources().getString(R.string.add));
        binding.header.back.setVisibility(View.GONE);
        binding.header.optionmenu.setVisibility(View.GONE);
        fullProgressbar.show();
        binding.setUiColor(UiColor);

       /* actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), binding.slider,R.string.nav_open,R.string.nav_close);
        binding.slider.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        binding.slider.closeDrawer(GravityCompat.END);
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            binding.slider.openDrawer(GravityCompat.END);
            }
        });*/


        try {
            if (getArguments().getBoolean("status")){
                ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                        RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(false,true,getArguments().getInt("statuss") ,0));
            } else {
                ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                        RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(false,true));
            }
        } catch (Exception e){
            e.printStackTrace();
            ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                    RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(false,true));
        }


        try {
            new Fragment_Selected_Location().startdatejourney = null;
            new Fragment_Selected_Location().enddatejourney = null;
            new Fragment_Selected_Location().starttimejourney = null;
            new Fragment_Selected_Location().endtimejourney = null;
            new Fragment_Selected_Location().lease = false;
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            ((Activity_Reservation) getActivity()).BottomnavVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.header.screenHeader.setText(companyLabel.Vehicle + " " + companyLabel.Reservation);

        optionMenu = new OptionMenu(getActivity());

        binding.rlAgreement.removeAllViews();

        /*binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 200){
                    new ApiService(GetReservationlist, RequestType.POST,
                            RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(true,false,page,count,""));
                }
            }
        });*/

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (s.length() > 3) {
                    binding.rlAgreement.removeAllViews();
                    fullProgressbar.show();
                    scroll = false;
                    new ApiService(GetReservationlist, RequestType.POST,
                            RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(true, false, 0, count, String.valueOf(s)));

                }*/

                new ApiService(GetReservationlist, RequestType.POST,
                        RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(true, false, 0, count, String.valueOf(s)));
            }
        });



        binding.view.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.filter_Reservation:
                NavHostFragment.findNavController(Fragment_Reservations.this)
                        .navigate(R.id.action_Reservations_to_Reservations_Filter);
                break;*/

            case R.id.discard:
                /*Intent intent = new Intent(getContext(), Booking_Activity.class);
                startActivity(intent);*/

                new Fragment_Selected_Location().startdatejourney = null;
                NavHostFragment.findNavController(Fragment_Reservations.this).navigate(R.id.profile_to_location);

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
                            count = resultSet.getInt("TotalRecord");  //TotalRecord
                            try {
                                if (getArguments().getBoolean("stetus")){

                                }
                            } catch (Exception e ){
                               e.printStackTrace();
                               loginRes.storedata("Trsv", String.valueOf(count));
                                total = count;
                            }

                            try {
                                if (getArguments().getBoolean("status")){
                                    optionMenu.filtermenu(binding.sucessfullRegi, getView(),bundle,Fragment_Reservations.this,header,params,reservationStatusDetails);
                                }

                            } catch (Exception e){
                                e.printStackTrace();
                                new ApiService(new OnResponseListener() {
                                    @Override
                                    public void onSuccess(String response, HashMap<String, String> headers) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    JSONObject responseJSON = new JSONObject(response);
                                                    Boolean status = responseJSON.getBoolean("Status");

                                                    JSONArray resultSet = responseJSON.getJSONArray("Data");
                                                    reservationStatusDetails = loginRes.getModel(resultSet.toString(), ReservationStatusDetail[].class);
                                                    optionMenu.filtermenu(binding.sucessfullRegi, getView(),bundle,Fragment_Reservations.this,header,params,reservationStatusDetails);
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {

                                    }
                                }, RequestType.GET,RESERVATIONSTATUS,BASE_URL_LOGIN,header,"?companyId="+ Helper.di);
                            }

                            final JSONArray getReservationList = resultSet.getJSONArray("Data");
                            reservations = loginRes.getModel(getReservationList.toString(), Reservation[].class);
                            int len;
                            len = reservations.length;
                            reservationList = new ArrayList<>();
                            for (int i = 0; i <len; i++) {
                             /*   if (scroll){
                                    Log.e(TAG, "run: " +  reservationList.size() );
                                    for (Reservation value : reservationList) {
                                        Reservation reservation = new Reservation();
                                        reservation = value;
                                        if (reservation.Id != reservations[i].Id) {
                                            reservationList.add(reservations[i]);
                                        }
                                    }
                                } else {
                                    reservationList = new ArrayList<>(new LinkedHashSet<>(reservations[i].Id));
                                    reservationList.add( reservations[i]);
                                }*/
                                reservationList.add( reservations[i]);
                                colors.add(UiColor);

                            }
                            showData();

                           // optionMenu.filtermenu(binding.sucessfullRegi, getView(),bundle,Fragment_Reservations.this,header,params);
                            /*for (int j = 0; j < len; j++)
                            {
                                getSubview(j);
                                ReservationListBinding reservationListBinding = ReservationListBinding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                reservationListBinding.getRoot().setId(200 + j);
                                reservationListBinding.getRoot().setLayoutParams(subparams);
                                reservationListBinding.setReservation(reservations[j]);
                                Log.d(TAG, "run: " + reservations[j]._date);
                                int finalJ = j;

                                reservationListBinding.ReservationListLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putInt("Id",reservations[finalJ].Id);
                                        Log.d(TAG, "run: " + reservations[finalJ].Id);
                                        bundle.putSerializable("reservation",reservations[finalJ]);
                                        bundle.putInt("Id",reservations[finalJ].Id);
                                        bundle.putSerializable("reservation",reservations[finalJ]);
                                        loginRes.storedataId(getResources().getString(R.string.rid), reservations[finalJ].Id);

                                  *//*      NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*//*
                                      *//*  NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*//*
                                        NavHostFragment.findNavController(Fragment_Reservations.this)
                                                .navigate(R.id.action_Agreement_to_Agreement_Summary,bundle);
                                    }
                                });
                                binding.rlAgreement.addView(reservationListBinding.getRoot());
                                fullProgressbar.hide();

                            }*/
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

    public void showData (){
        page += 1;
        binding.rlAgreement.removeAllViews();

        for (int i = 0; i <reservationList.size(); i++) {
            scroll = true;
            getSubview(i);
            ReservationListBinding reservationListBinding = ReservationListBinding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            reservationListBinding.getRoot().setId(200 + i);
            reservationListBinding.getRoot().setLayoutParams(subparams);
            reservationListBinding.setReservation(reservationList.get(i));

           /* Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_pickup_location_circle);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(UiColor.primary));

            reservationListBinding.te.setBackground(wrappedDrawable);*/
            reservationListBinding.setUiColor(UiColor);
            Log.d(TAG, "run: " + reservationList.get(i)._date);
            int finalJ = i;

            reservationListBinding.ReservationListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putInt("Id" ,reservationList.get(finalJ).Id);
                    Log.d(TAG, "run: " + reservationList.get(finalJ).Id);
                    bundle.putSerializable("reservation",reservationList.get(finalJ));
                    loginRes.storedataId(getResources().getString(R.string.rid), reservationList.get(finalJ).Id);
                                  /*      NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*/
                                      /*  NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*/
                    NavHostFragment.findNavController(Fragment_Reservations.this)
                            .navigate(R.id.action_Agreement_to_Agreement_Summary,bundle);
                }
            });
            binding.rlAgreement.addView(reservationListBinding.getRoot());
        }
        fullProgressbar.hide();
    }
}
