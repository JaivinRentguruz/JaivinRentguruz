package com.abel.app.b2b.home.reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAgreementBinding;
import com.abel.app.b2b.databinding.FragmentReservationsBinding;
import com.abel.app.b2b.databinding.ReservationListBinding;
import com.abel.app.b2b.flexiicar.booking.Booking_Activity;
import com.abel.app.b2b.home.Fragment_Home_Tab;
import com.abel.app.b2b.home.Fragment_Selected_Location;
import com.abel.app.b2b.model.response.Reservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETALL;

public class Fragment_Reservations extends BaseFragment {


    public static Context context;
    public String id = "";
    Handler handler = new Handler(Looper.getMainLooper());
    FragmentReservationsBinding binding;
    Reservation[] reservations;
    ImageLoader imageLoader;
    String serverpath = "";
    ProgressDialog progressDialog;

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.add.setOnClickListener(this);
        fullProgressbar.show();
        ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(false,true));

        binding.screenHeader.setText(companyLabel.Vehicle + " " + companyLabel.Reservation);

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

            case R.id.add:
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
                            final JSONArray getReservationList = resultSet.getJSONArray("Data");
                            reservations = loginRes.getModel(getReservationList.toString(), Reservation[].class);
                            int len;
                            len = reservations.length;
                            binding.rlAgreement.removeAllViews();
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

                                reservationListBinding.ReservationListLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putInt("Id",reservations[finalJ].Id);
                                        Log.d(TAG, "run: " + reservations[finalJ].Id);
                                        bundle.putSerializable("reservation",reservations[finalJ]);
                                  /*      NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*/
                                      /*  NavHostFragment.findNavController(Fragment_Agreements.this)
                                                .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,bundle);*/
                                    }
                                });
                                binding.rlAgreement.addView(reservationListBinding.getRoot());
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
