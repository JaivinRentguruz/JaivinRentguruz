package com.abel.app.b2b.home.agreement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;

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
import com.abel.app.b2b.databinding.ReservationListBinding;
import com.abel.app.b2b.flexiicar.user.Fragment_Agreements;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.Reservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETALL;


public class Fragment_Agreement extends BaseFragment {

    public static Context context;
    public String id = "";
    Handler handler = new Handler(Looper.getMainLooper());
    FragmentAgreementBinding binding;
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
        binding = FragmentAgreementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 21)
        { // lower api
            View v = getActivity().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 21)
        {

            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        try {
            binding.screenHeader.setText("List " + companyLabel.Agreement);
            binding.subheader.setText(companyLabel.Agreement);
        } catch (Exception e){
            e.printStackTrace();
        }

        fullProgressbar.show();
        ApiService apiService = new ApiService(GetReservationlist, RequestType.POST,
                RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(true,false));

        //public JSONObject getAggrementList(Boolean IsGetAgreement, Boolean IsGetReservation,int offset,int count,String search)

      /*  binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 200){
                    new ApiService(GetReservationlist, RequestType.POST,
                            RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getAggrementList(true,false,1,60,""));
                }
            }
        });*/

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onClick(View v) {

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
                            int TotalRecord = resultSet.getInt("TotalRecord");
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
                                        //CustomerId
                                      //  UserData.loginResponse.User.UserFor = reservations[finalJ].CustomerId;

                                       // reservations[finalJ].ReservationStatus = 3;
                                        NavHostFragment.findNavController(Fragment_Agreement.this)
                                                .navigate(R.id.action_Agreement_to_Agreement_Summary,bundle);
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
            fullProgressbar.hide();
            System.out.println("Error-" + error);
        }
    };
}
