package com.abel.app.b2b.flexiicar.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentReservationBinding;
import com.abel.app.b2b.databinding.ReservationListBinding;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.response.Reservation;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.flexiicar.booking.Fragment_Payment_checkout;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETALL;

public class Fragment_Reservation extends BaseFragment
{
    // ImageView backarrow;
    // Handler handler = new Handler();
    // public static Context context;
    public String id = "";
    ImageLoader imageLoader;
    String serverpath="";
    // ProgressDialog progressDialog;
    //private DoHeader header;
    //LoginRes loginRes;
    //List<Reservation> reservationList;
    //Reservation reservation;
    Reservation[] reservations;
    FragmentReservationBinding binding;

    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentReservationBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //((User_Profile) getActivity()).BottomnavVisible();
        // reservationList = new ArrayList<>();
        // reservation = new Reservation();

        binding.backToUsardetails1.setOnClickListener(this);
        fullProgressbar.show();


        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        serverpath = sp.getString("serverPath", "");

        if (Helper.onRent){
            binding.discard.setVisibility(View.GONE);
            binding.screenHeader.setText(companyLabel.Vehicle + " On Rent");
        } else {
            binding.screenHeader.setText("List " +companyLabel.Reservation);
        }

        apiService = new ApiService(GetReservationlist, RequestType.POST,
                RESERVATIONGETALL, BASE_URL_CUSTOMER, header, params.getReservationList(UserData.loginResponse.User.UserFor, Helper.onRent));

        try {
            if (Helper.isActiveCustomer){
                ((User_Profile)getActivity()).BottomnavInVisible();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.discard.setOnClickListener(this);

        try {
            if (!loginRes.getData(getResources().getString(R.string.userType)).equals("1")){
                binding.discard.setVisibility(View.GONE);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
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
                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray getReservationList = resultSet.getJSONArray("Data");
                            reservations = loginRes.getModel(getReservationList.toString(), Reservation[].class);
                            // final RelativeLayout rlReservation = getActivity().findViewById(R.id.rl_reservationlist);
                            int len;
                            len = reservations.length;
                            binding.rlReservationlist.removeAllViews();
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
                                if (!Helper.onRent) {
                                    reservationListBinding.ReservationListLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bundle.putInt("Id", reservations[finalJ].Id);
                                            Log.d(TAG, "run: " + reservations[finalJ].Id);
                                            bundle.putSerializable("Reservation", reservations[finalJ]);
                                            NavHostFragment.findNavController(Fragment_Reservation.this)
                                                    .navigate(R.id.action_Reservation_to_SummaryOfCharges, bundle);
                                        }
                                    });
                                }
                                binding.rlReservationlist.addView(reservationListBinding.getRoot());
                                fullProgressbar.hide();
                                //final JSONObject test = (JSONObject) getReservationList.get(j);

                                //reservation = loginRes.getModel(test.toString(), Reservation.class);
                                // reservationList.add(reservation);

                          /*      final int reservation_ID = test.getInt("reservation_ID");
                                final int reservation_Type = test.getInt("reservation_Type");
                                final String reservation_Type_Name = test.getString("reservation_Type_Name");
                                final String reservation_No = test.getString("reservation_No");
                                final String check_Out = test.getString("check_Out");
                                final String default_Check_Out = test.getString("default_Check_Out");
                                final int check_Out_Location = test.getInt("check_Out_Location");
                                final String chk_Out_Location_Name = test.getString("chk_Out_Location_Name");
                                final String check_IN = test.getString("check_IN");
                                final String default_Check_In = test.getString("default_Check_In");
                                final int check_IN_Location = test.getInt("check_IN_Location");
                                final String chk_In_Loc_Name = test.getString("chk_In_Loc_Name");
                                final int reservation_By = test.getInt("reservation_By");
                                final int vehicle_Type_ID = test.getInt("vehicle_Type_ID");
                                final String vehicle_Type_Name = test.getString("vehicle_Type_Name");
                                final int vehicle_ID = test.getInt("vehicle_ID");
                                final String vehicle_Name = test.getString("vehicle_Name");
                                final String veh_Full_Name = test.getString("veh_Full_Name");
                                final String vehicle_No = test.getString("vehicle_No");
                                final String vin_Num = test.getString("vin_Num");
                                final String lic_Num = test.getString("lic_Num");
                                final int customer_ID = test.getInt("customer_ID");
                                final String cust_MobileNo = test.getString("cust_MobileNo");
                                final int cust_Country_ID = test.getInt("cust_Country_ID");
                                final String cust_FName = test.getString("cust_FName");
                                final String cust_LName = test.getString("cust_LName");
                                final String cust_Phoneno = test.getString("cust_Phoneno");
                                final String cust_Full_Name = test.getString("cust_Full_Name");
                                final String cust_Email = test.getString("cust_Email");
                                final String veh_Img_Path = test.getString("veh_Img_Path");
                                final String cust_Img_Path = test.getString("cust_Img_Path");
                                final String rate_ID = test.getString("rate_ID");
                                final String transmission_Name = test.getString("transmission_Name");
                                final Double estimated_Total=test.getDouble("estimated_Total");
                                final String res_Status = test.getString("res_Status");
                                final String res_Status_BGColor = test.getString("res_Status_BGColor");
                                final String default_Created_Date=test.getString("default_Created_Date");

                                final String d_FlightNo = test.getString("d_FlightNo");
                                final String d_Airport = test.getString("d_Airport");
                                final String d_City = test.getString("d_City");
                                final String d_Zipcode = test.getString("d_Zipcode");
                                final String d_Time = test.getString("d_Time");
                                final String default_D_Time = test.getString("default_D_Time");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 18, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout reservationlayout = (LinearLayout) inflater.inflate(R.layout.reservation_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                reservationlayout.setId(200 + j);
                                reservationlayout.setLayoutParams(lp);

                                final ImageView car_image = (ImageView) reservationlayout.findViewById(R.id.vehicle_Image);
                                String url1 = serverpath+veh_Img_Path.substring(2);
                                imageLoader.displayImage(url1, car_image);

                                final ImageView Cust_Image= (ImageView) reservationlayout.findViewById(R.id.cust_Image);
                                String url2 = serverpath+cust_Img_Path.substring(2);
                                imageLoader.displayImage(url2, Cust_Image);

                                LinearLayout linearLayout=(LinearLayout) reservationlayout.findViewById(R.id.ReservationListLayout);
                                LinearLayout llRes_Status=(LinearLayout) reservationlayout.findViewById(R.id.ll_resStatus);

                                if (res_Status.equals("Close"))
                                {
                                    llRes_Status.setBackgroundTintList(ColorStateList.valueOf(parseColor(res_Status_BGColor)));
                                }
                                else
                                    {
                                    llRes_Status.setBackgroundTintList(ColorStateList.valueOf(parseColor(res_Status_BGColor)));
                                }

                                TextView txtfullName = reservationlayout.findViewById(R.id.txtFullname);
                                TextView txtPhoneNo = reservationlayout.findViewById(R.id.txt_PhoneNo);
                                TextView txtEmailId = reservationlayout.findViewById(R.id.txt_CustEmail);
                                TextView txt_CheckInLoc = reservationlayout.findViewById(R.id.checkinLocName);
                                TextView txtCheckInDate = reservationlayout.findViewById(R.id.checkIn_DateTime);
                                TextView txtCheckOutLoc = reservationlayout.findViewById(R.id.txt_CheckOutLocName);
                                TextView txtChekOutDate = reservationlayout.findViewById(R.id.txt_CheckOutDateTime);
                                TextView txtVehicleName = reservationlayout.findViewById(R.id.txt_VehicleFullname);
                                TextView txtVehicleNo = reservationlayout.findViewById(R.id.txtVehicleNo);
                                TextView txt_VinNo = reservationlayout.findViewById(R.id.txt_VinNo);
                                TextView txt_LicNo = reservationlayout.findViewById(R.id.txt_LicNo);
                                TextView txt_res_status= reservationlayout.findViewById(R.id.txt_res_status);

                                txtfullName.setText(cust_Full_Name);
                                txtPhoneNo.setText(cust_MobileNo);
                                txtEmailId.setText(cust_Email);

                                txtCheckOutLoc.setText(chk_Out_Location_Name);

                                //check_Out Date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                Date date = dateFormat.parse(default_Check_Out);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
                                String check_OutStr = sdf.format(date);
                                txtChekOutDate.setText(check_OutStr);

                                txt_CheckInLoc.setText(chk_In_Loc_Name);

                                //check_IN Date
                                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                Date date1 = dateFormat1.parse(default_Check_In);
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
                                String check_INStr = sdf1.format(date1);
                                txtCheckInDate.setText(check_INStr);

                                txtVehicleName.setText(vehicle_Name);
                                txtVehicleNo.setText(vehicle_No);
                                txt_VinNo.setText(vin_Num);
                                txt_LicNo.setText(lic_Num);
                                txt_res_status.setText(res_Status);

                                rlReservation.addView(reservationlayout);*/

                              /*  linearLayout.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        try
                                        {
                                            Bundle ReservationBundle=new Bundle();
                                            ReservationBundle.putInt("reservation_ID",reservation_ID);
                                            ReservationBundle.putInt("reservation_Type",reservation_Type);
                                            ReservationBundle.putString("reservation_Type_Name",reservation_Type_Name);
                                            ReservationBundle.putString("reservation_No",reservation_No);
                                            ReservationBundle.putString("check_Out",check_Out);
                                            ReservationBundle.putString("default_Check_Out",default_Check_Out);
                                            ReservationBundle.putInt("check_Out_Location",check_Out_Location);
                                            ReservationBundle.putString("chk_Out_Location_Name",chk_Out_Location_Name);
                                            ReservationBundle.putString("check_IN",check_IN);
                                            ReservationBundle.putString("default_Check_In",default_Check_In);
                                            ReservationBundle.putInt("check_IN_Location",check_IN_Location);
                                            ReservationBundle.putString("chk_In_Loc_Name",chk_In_Loc_Name);
                                            ReservationBundle.putInt("vehicle_Type_ID",vehicle_Type_ID);
                                            ReservationBundle.putInt("reservation_By",reservation_By);
                                            ReservationBundle.putInt("vehicle_Type_ID",vehicle_Type_ID);
                                            ReservationBundle.putString("vehicle_Type_Name",vehicle_Type_Name);
                                            ReservationBundle.putInt("vehicle_ID",vehicle_ID);
                                            ReservationBundle.putString("vehicle_Name",vehicle_Name);
                                            ReservationBundle.putString("veh_Full_Name",veh_Full_Name);
                                            ReservationBundle.putString("vehicle_No",vehicle_No);
                                            ReservationBundle.putString("vin_Num",vin_Num);
                                            ReservationBundle.putInt("customer_ID",customer_ID);
                                            ReservationBundle.putString("lic_Num",lic_Num);
                                            ReservationBundle.putString("cust_MobileNo",cust_MobileNo);
                                            ReservationBundle.putInt("cust_Country_ID",cust_Country_ID);
                                            ReservationBundle.putString("cust_FName",cust_FName);
                                            ReservationBundle.putString("cust_LName",cust_LName);
                                            ReservationBundle.putString("cust_Phoneno",cust_Phoneno);
                                            ReservationBundle.putString("cust_Full_Name",cust_Full_Name);
                                            ReservationBundle.putString("cust_Email",cust_Email);
                                            ReservationBundle.putString("veh_Img_Path",veh_Img_Path);
                                            ReservationBundle.putString("cust_Img_Path",cust_Img_Path);
                                            ReservationBundle.putString("rate_ID",rate_ID);
                                            ReservationBundle.putString("transmission_Name",transmission_Name);
                                            ReservationBundle.putString("default_Created_Date",default_Created_Date);
                                            ReservationBundle.putDouble("estimated_Total",estimated_Total);

                                            ReservationBundle.putString("d_FlightNo",d_FlightNo);
                                            ReservationBundle.putString("d_Airport",d_Airport);
                                            ReservationBundle.putString("d_City",d_City);
                                            ReservationBundle.putString("d_Zipcode",d_Zipcode);
                                            ReservationBundle.putString("d_Time",transmission_Name);
                                            ReservationBundle.putString("default_D_Time",default_D_Time);
                                            Bundle Reservation=new Bundle();
                                            Reservation.putBundle("ReservationBundle",ReservationBundle);
                                            System.out.println(Reservation);
                                            NavHostFragment.findNavController(Fragment_Reservation.this)
                                                    .navigate(R.id.action_Reservation_to_SummaryOfCharges,Reservation);
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });*/
                            }
                        }
                        else
                        {
                            fullProgressbar.hide();
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                        // showReservation(reservationList);
                        fullProgressbar.hide();
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

/*    public void showReservation(List<Reservation> data){

        final RelativeLayout rlReservation = getActivity().findViewById(R.id.rl_reservationlist);
        int len;
        len = data.size();
        for (int j = 0; j <len ; j++) {

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.setMargins(0, 18, 0, 0);

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout reservationlayout = (LinearLayout) inflater.inflate(R.layout.reservation_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
            reservationlayout.setId(200 + j);
            reservationlayout.setLayoutParams(lp);


            TextView txtfullName = reservationlayout.findViewById(R.id.txtFullname);
            TextView txtPhoneNo = reservationlayout.findViewById(R.id.txt_PhoneNo);
            TextView txtEmailId = reservationlayout.findViewById(R.id.txt_CustEmail);
            TextView txt_CheckInLoc = reservationlayout.findViewById(R.id.checkinLocName);
            TextView txtCheckInDate = reservationlayout.findViewById(R.id.checkIn_DateTime);
            TextView txtCheckOutLoc = reservationlayout.findViewById(R.id.txt_CheckOutLocName);
            TextView txtChekOutDate = reservationlayout.findViewById(R.id.txt_CheckOutDateTime);
            TextView txtVehicleName = reservationlayout.findViewById(R.id.txt_VehicleFullname);
            TextView txtVehicleNo = reservationlayout.findViewById(R.id.txtVehicleNo);
            TextView txt_VinNo = reservationlayout.findViewById(R.id.txt_VinNo);
            TextView txt_LicNo = reservationlayout.findViewById(R.id.txt_LicNo);
            TextView txt_res_status= reservationlayout.findViewById(R.id.txt_res_status);

            txtfullName.setText(data.get(j).CustomerName);
            txtEmailId.setText(data.get(j).Email);
            txtPhoneNo.setText(data.get(j).MobileNo);

            txt_CheckInLoc.setText(data.get(j).PickUpLocationName);
            txtCheckInDate.setText(data.get(j).CheckInDate);
            txtCheckOutLoc.setText(data.get(j).DropLocationName);
            txtChekOutDate.setText(data.get(j).CheckOutDate);
            txtVehicleName.setText(data.get(j).VehicleName);

            //txt_res_status.setText(data.get(j).ReservationStatus.toString());

             int t = j;

            //vechicle number , vechile licence number , vechicle number
            txtVehicleNo.setText(data.get(j).VehicleId);
            txt_VinNo.setText(data.get(j).VehicleYear);
            txt_LicNo.setText(UserData.customerProfile.DrivingLicenceModel.Number);

            LinearLayout linearLayout=(LinearLayout) reservationlayout.findViewById(R.id.ReservationListLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        Bundle ReservationBundle=new Bundle();
                        ReservationBundle.putInt("Id",data.get(t).Id);
                       *//* Bundle Reservation=new Bundle();
                        Reservation.putBundle("ReservationBundle",ReservationBundle);*//*
                        NavHostFragment.findNavController(Fragment_Reservation.this)
                                .navigate(R.id.action_Reservation_to_SummaryOfCharges,ReservationBundle);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            rlReservation.addView(reservationlayout);

        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_usardetails1:
               /* NavHostFragment.findNavController(Fragment_Reservation.this)
                        .navigate(R.id.action_Reservation_to_User_Details);*/
                Intent intent = new Intent(getActivity(), User_Profile.class);
                startActivity(intent);
                break;

            case R.id.discard:
                Helper.isActiveCustomer = true;
                Helper.reservationwithoutmap =true;
                Intent i = new Intent(getActivity(), Customer_Booking_Activity.class);
                //   i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }
}
