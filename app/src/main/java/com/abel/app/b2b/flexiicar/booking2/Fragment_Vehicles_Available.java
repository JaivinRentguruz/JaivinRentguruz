package com.abel.app.b2b.flexiicar.booking2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.abel.app.b2b.adapters.DateConvert;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.MySpannable;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehiclesAvailableBinding;
import com.abel.app.b2b.databinding.TaxDetailsListBinding;
import com.abel.app.b2b.databinding.VehicleAvailableListBinding;
import com.abel.app.b2b.databinding.VehicletypeAvailableListBinding;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.reservation.ReservationVehicleType;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.model.response.RateModel;
import com.abel.app.b2b.model.response.ReservationOriginDataModels;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.AVAILABLEVEHICLETYPE;
import static com.abel.app.b2b.apicall.ApiEndPoint.AVAILABLEVICHICLE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSURANCECOVER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.SUMMARYCHARGE;

public class Fragment_Vehicles_Available extends BaseFragment
{
    //LinearLayout lblselectedlocation, filter_icon;
   // ImageView backarrowimage;
  //  public static Context context;
    public String id = "";
 //   Bundle BookingBundle;
  //  Handler handler = new Handler();
    ImageLoader imageLoader;
    String serverpath="";
  //  Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;
   // EditText edt_searchVehicleName;
   // TextView txt_Discard;
    Double Totalprice;
    JSONArray VehicleList = new JSONArray();
   // private DoHeader header;
   // LoginRes loginRes;
    LoginResponse loginResponse;
    FragmentVehiclesAvailableBinding binding;
    VehicleAvailableListBinding vehicleAvailableListBinding;
    VehicletypeAvailableListBinding vehicletypeAvailableListBinding;
  //  Bundle Booking = new Bundle();
    public static Boolean userIns = false;
    VehicleModel[] vehicleModel;

    List<VehicleModel> vehicleModelList = new ArrayList<>();
    LocationList pickuploc;
    DateConvert dateConvert;
    RateModel rateModel = new RateModel();
    ReservationSummarry reserversationSummary = new ReservationSummarry();
    VehicleModel vm = new VehicleModel();
    Boolean insurance = false;
    public static HashMap<Integer, String> activationRate = new HashMap<>();
    private static Boolean VehicelAvai =false;
    JSONArray vehicletype;
    public static Boolean isDefaultins = false;
    public static void initImageLoader(Context context)
    {
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
        loginResponse = new LoginResponse();
        pickuploc = new LocationList();
        dateConvert = new DateConvert();
        binding = FragmentVehiclesAvailableBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();
      //  fullProgressbar.show();

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = getActivity().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView =getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        loginResponse = loginRes.callFriend("Data", LoginResponse.class);
        Helper.isFirstB2CReservation = true;
        Helper.BACKTO = true;
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        serverpath = sp.getString("serverPath", "");
        pickuploc = (LocationList) getArguments().getSerializable("model");
        bundle.putSerializable("model", pickuploc);
        bundle.putSerializable("models", getArguments().getSerializable("models"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        locationType = getArguments().getBoolean("locationType");
        initialSelect = getArguments().getBoolean("initialSelect");
        Fragment_Select_addition_Options.condition = true;
        Helper.VISIBLE = false;
        binding.txtDiscardVehAvlbl.setOnClickListener(this);
        binding.filterIcon.setOnClickListener(this);
        binding.backarrowimg.setOnClickListener(this);
        String vehicle = loginRes.getData("VehicleTypeMasterIds");
        vehicletype = new JSONArray();
        for (int i = 0; i < vehicle.split(",").length; i++) {
            vehicletype.put(Integer.valueOf(vehicle.split(",")[i].trim()));
        }
        //vehicletype.put()
        Log.e(TAG, "onViewCreated: " + loginRes.getData("VehicleTypeMasterIds") );
        apiService = new ApiService(getVehicleTypeList,RequestType.POST,AVAILABLEVEHICLETYPE, BASE_URL_LOGIN, header,
                params.getVehicleType(pickuploc.Id,
                        getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                        getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")
                , vehicletype));

        try {
            if (UserData.companyModel.CompanyPreference.DefaultBookingOnVehicleType){
                binding.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.VehicleType);
                binding.subheader.setText(getResources().getString(R.string.available) + " " + companyLabel.VehicleType);
            } else {
                binding.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Vehicle);
                binding.subheader.setText(getResources().getString(R.string.available) + " " + companyLabel.Vehicle);
            }
        } catch (Exception e){
            e.printStackTrace();
        }



      /*  if (UserData.companyModel.CompanyPreference.DefaultBookingOnVehicleType){
            apiService = new ApiService(getVehicleList,RequestType.POST,AVAILABLEVEHICLETYPE, BASE_URL_LOGIN, header,
                    params.getVehicleType(pickuploc.Id, getArguments().getString("pickupdate"), getArguments().getString("dropdate")));
        } else {
            apiService = new ApiService(getVehicleList, RequestType.POST,
                    AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                    params.getVechicleList(pickuploc.Id,
                            getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                            getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));
        }*/


        binding.edtSearchVehicleName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                try {
                    binding.relativeVehicleAvailable.removeAllViews();
                    apiService = new ApiService(getVehicleTypeList,RequestType.POST,AVAILABLEVEHICLETYPE, BASE_URL_LOGIN, header,
                            params.getVehicleType(pickuploc.Id,
                                    getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                                    getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")
                                    , vehicletype,String.valueOf(editable)));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener getVehicleList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
                try
                {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    VehicelAvai = true;
                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        VehicleList = resultSet.getJSONArray("ListOfVehicle");
                        ReservationTimeModel timeModel = new ReservationTimeModel();
                        timeModel = loginRes.getModel(resultSet.getString("ReservationTimeModel"), ReservationTimeModel.class);
                        bundle.putSerializable("timemodel", timeModel);
                        UserReservationData.reservationTimeModel = timeModel;
                   //     VehicleModel[] vehicleModel = loginRes.getModel(VehicleList.toString(), VehicleModel[].class);
                   vehicleModel = loginRes.getModel(VehicleList.toString(), VehicleModel[].class);

                        Type listType = new TypeToken<ArrayList<VehicleModel>>(){}.getType();
                   vehicleModelList = (List<VehicleModel>) loginRes.getModelList(VehicleList.toString(), VehicleModel.class);
                        int len;
                        len = vehicleModelList.size();
                        for (int j = 0; j < len; j++)
                        {
                            getSubview(j);
                            vehicleAvailableListBinding = VehicleAvailableListBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            vehicleAvailableListBinding.setVehicle(vehicleModel[j]);
                            vehicleAvailableListBinding.getRoot().setId(200 + j);
                            vehicleAvailableListBinding.getRoot().setLayoutParams(subparams);
                            int finalJ = j;

                            vehicleAvailableListBinding.available.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    binding.relativeVehicleAvailable.removeAllViews();
                                    new ApiService(getVehicleList, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                                            params.getVechicleList(pickuploc.Id,getArguments().getString("pickupdate"),getArguments().getString("dropdate"),vehicleModel[finalJ].VehicleTypeId));
                                }
                            });

                            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    binding.relativeVehicleAvailable.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                    bundle.putSerializable("Model", vehicleModel[finalJ]);
                                    vm = vehicleModel[finalJ];
                                    apiService = new ApiService(ReserCal, RequestType.POST, RATE, BASE_URL_LOGIN, header,// params.getRate(vehicleModel[finalJ].RateId, vehicleModel[finalJ].VehicleTypeId, pickuploc.Id));
                                            params.getRate(vehicleModel[finalJ].RateId, vehicleModel[finalJ].VehicleTypeId,  pickuploc.Id,
                                                    getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                                                    getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));

                                /*    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, bundle);*/
                                }
                            });
                            binding.relativeVehicleAvailable.addView(vehicleAvailableListBinding.getRoot());
                        }
                        fullProgressbar.hide();
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                        fullProgressbar.hide();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    fullProgressbar.hide();
                }
            });
        }
        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    public void updateReservationSummarry(int key){
        int data = reserversationSummary.ReservationOriginDataModels.size();
        for (int i = 0; i <data; i++) {
            if (reserversationSummary.ReservationOriginDataModels.get(i).TableType == key) {
                reserversationSummary.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(key, activationRate.get(key))); // activationDetail.get(52).toString();
    }

    OnResponseListener getVehicleTypeList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        JSONObject responseJSON = new JSONObject(response);
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray data = resultSet.getJSONArray("ListOfVehicleType");
                        ReservationVehicleType[] reservationVehicleType;
                        reservationVehicleType = loginRes.getModel(data.toString(),ReservationVehicleType[].class);
                        int len = reservationVehicleType.length;

                        ReservationTimeModel timeModel = new ReservationTimeModel();
                        timeModel = loginRes.getModel(resultSet.getString("ReservationTimeModel"), ReservationTimeModel.class);
                        bundle.putSerializable("timemodel", timeModel);
                        UserReservationData.reservationTimeModel = timeModel;

                        for (int j = 0; j < len; j++) {
                            getSubview(j);
                            vehicletypeAvailableListBinding = VehicletypeAvailableListBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            vehicletypeAvailableListBinding.getRoot().setId(200 + j);
                            vehicletypeAvailableListBinding.getRoot().setLayoutParams(subparams);
                            vehicletypeAvailableListBinding.setVehicle(reservationVehicleType[j]);

                            int finalJ = j;
                            vehicletypeAvailableListBinding.available.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    binding.relativeVehicleAvailable.removeAllViews();
                                    new ApiService(getVehicleList, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                                            params.getVechicleList(pickuploc.Id,getArguments().getString("pickupdate"),getArguments().getString("dropdate"),reservationVehicleType[finalJ].Id));
                                }
                            });


                            vehicletypeAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    binding.relativeVehicleAvailable.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                    vm.VehicleTypeId =  reservationVehicleType[finalJ].Id;
                                    vm.DefaultImagePath = reservationVehicleType[finalJ].VehicleClassStandaredImagePath;
                                    vm.NoOfBags = reservationVehicleType[finalJ].NoOfBags;
                                    vm.NoOfDoors = reservationVehicleType[finalJ].NoOfDoors;
                                    vm.NoOfSeats = reservationVehicleType[finalJ].NoOfSeats;
                                   // vm.SecurityDeposit =Integer.parseInt(String.valueOf(reservationVehicleType[finalJ].SecurityDeposit));
                                    vm.SecurityDeposit = (int) Math.round(reservationVehicleType[finalJ].SecurityDeposit);
                                    vm.VehicleShortName = reservationVehicleType[finalJ].Name;
                                    vm.VehicleClass = reservationVehicleType[finalJ].VehicleCategoryName;
                                    vm.VehicleName = reservationVehicleType[finalJ].Name;
                                    vm.TransmissionDesc = reservationVehicleType[finalJ].TransmissionDesc;
                                    vm.VehicleCategory =  reservationVehicleType[finalJ].VehicleCategoryName;
                                    //vm.Id =    reservationVehicleType[finalJ].Id;

                                  //  bundle.putSerializable("Model", reservationVehicleType[finalJ]);
                                    bundle.putSerializable("Model", vm);


//                                    vm = vehicleModel[finalJ];
                                    vm.VehicleTypeId = reservationVehicleType[finalJ].Id;
                                   /* apiService = new ApiService(ReserCal, RequestType.POST, RATE, BASE_URL_LOGIN, header,// params.getRate(vehicleModel[finalJ].RateId, vehicleModel[finalJ].VehicleTypeId, pickuploc.Id));
                                            params.getRate(reservationVehicleType[finalJ].RateId, reservationVehicleType[finalJ].Id,  pickuploc.Id,
                                                    getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                                                    getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));*/

                                    apiService = new ApiService(ReserCal, RequestType.POST, RATE, BASE_URL_LOGIN, header,// params.getRate(vehicleModel[finalJ].RateId, vehicleModel[finalJ].VehicleTypeId, pickuploc.Id));
                                            params.getRate(UserData.reservationBusinessSource.RateId, reservationVehicleType[finalJ].Id,  pickuploc.Id,
                                                    getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                                                    getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));

                                /*    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, bundle);*/
                                }
                            });
                            binding.relativeVehicleAvailable.addView(vehicletypeAvailableListBinding.getRoot());
                        }

                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "onError: " + error );
        }
    };

    OnResponseListener ReserCal = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status) {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            rateModel = loginRes.getModel(resultSet.toString(), RateModel.class);
                            Log.d(TAG, "run: " + rateModel.RateId);
                            activationRate.put(47, resultSet.toString());
                            updateReservationSummarry(47);
                            //reserversationSummary.ReservationOriginDataModels.add(new ReservationOriginDataModels(47, resultSet.toString()));
                            reserversationSummary.ReservationRatesModel = rateModel;
                            bundle.putSerializable("reservationSum", reserversationSummary);
                            //apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(model.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays) );
                            // apiService = new ApiService(getInventory, RequestType.POST, EQUIPMENT, BASE_URL_LOGIN, header, params.getEquipment());
                            // apiService = new ApiService(getMischarges, RequestType.POST, MISCCHARGES, BASE_URL_LOGIN, header, params.getMisc(pickuplocation.Id, model.VehicleTypeId));
                          /*reserversationSummary.ReservationInsuranceModel.IsSureInsurance = false;
                          reserversationSummary.ReservationInsuranceModel.NoOfDays = 3;*/

                        /*  reserversationSummary.CheckOutDate = getArguments().getString("pickupdate")+"T"+getArguments().getString("pickuptime")+":45.495Z";
                          reserversationSummary.CheckInDate = getArguments().getString("dropdate")+"T"+getArguments().getString("droptime")+":45.495Z";
                          reserversationSummary.PickUpLocation = 10;
                          reserversationSummary.DropLocation = 8;*/

                            // ApiService2 apiService2 = new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reserversationSummary);

                            apiService = new ApiService(getInsurance,RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,params.getInsuranceCover(vm.VehicleTypeId, UserReservationData.reservationTimeModel.TotalDays,pickuploc.Id));
                           // apiService = new ApiService(getInsurance, RequestType.POST, INSURANCECOVER, BASE_URL_LOGIN, header, params.getInsuranceCover(vehicleModel.VehicleTypeId, reservationTimeModel.TotalDays, pickuplocation.Id));
                        } else {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                    }  catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    OnResponseListener getInsurance = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {

            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        if (array.length()>1){
                            userIns = true;
                        }
                        Log.d(TAG, "onSuccess: " + resultSet);
                        bundle.putString("insuranceOption", array.toString());
                        int idd = 0;
                        for (int i = 0; i <array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Boolean isSelect = obj.getBoolean("IsSelected");
                            idd = obj.getInt("Id");
                            //idd = obj.getInt("DetailId");
                            if (isSelect){
                                insurance = true;
                                isDefaultins = true;
                            }
                        }

                        if (!insurance) {
                            bundle.putInt("idd", idd);
                        }

                        NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, bundle);
                    }
                    else
                    {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_discardVehAvlbl:
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Search_activity);
                /*Intent intent = new Intent( getActivity(), Activity_Agreements.class);
                startActivity(intent);*/
                break;

            case R.id.filter_icon:
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Vehicles_FilterList,bundle);
                break;

            case R.id.backarrowimg:
                if (VehicelAvai){
                    VehicelAvai = false;
                    binding.relativeVehicleAvailable.removeAllViews();
                    apiService = new ApiService(getVehicleTypeList,RequestType.POST,AVAILABLEVEHICLETYPE, BASE_URL_LOGIN, header,
                            params.getVehicleType(pickuploc.Id,
                                    getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                                    getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")
                                    , vehicletype));

                } else {
                    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                            .navigate(R.id.action_Vehicles_Available_to_Selected_location, bundle);
                }
                break;
        }
    }
}
//,"doc_Name":"14332_QRCode_20502021034350.png","doc_Details":"../Images/Company/204/Booking/QRCode/6721/14332_QRCode_20502021034350.png"

