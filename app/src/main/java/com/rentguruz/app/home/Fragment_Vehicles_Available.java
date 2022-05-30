package com.rentguruz.app.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehiclesAvailableBinding;
import com.rentguruz.app.databinding.VehicleAvailableListBinding;
import com.rentguruz.app.flexiicar.booking2.Fragment_Select_addition_Options;
import com.rentguruz.app.model.base.UserReservationData;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.model.response.RateModel;
import com.rentguruz.app.model.response.ReservationOriginDataModels;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLEVICHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.INSURANCECOVER;

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
    //VehicleAvailableList2Binding vehicleAvailableListBinding;
  //  Bundle Booking = new Bundle();

    VehicleModel[] vehicleModel;

    List<VehicleModel> vehicleModelList = new ArrayList<>();
    LocationList pickuploc;
    DateConvert dateConvert;
    RateModel rateModel = new RateModel();
    ReservationSummarry reserversationSummary = new ReservationSummarry();
    VehicleModel vm = new VehicleModel();
    Boolean insurance = false;
    public static HashMap<Integer, String> activationRate = new HashMap<>();

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

 /*       int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);*/
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Vehicle);
        binding.subheader.setText(getResources().getString(R.string.available) + " " + companyLabel.Vehicle);
        fullProgressbar.show();
/*


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
        }*/

        loginResponse = loginRes.callFriend("Data", LoginResponse.class);

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        serverpath = sp.getString("serverPath", "");
        pickuploc = (LocationList) getArguments().getSerializable("model");
        bundle.putSerializable("model", pickuploc);
        bundle.putSerializable("models", getArguments().getSerializable("models"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        locationType = getArguments().getBoolean("locationType");
        initialSelect = getArguments().getBoolean("initialSelect");
        Fragment_Select_addition_Options.condition = true;
        binding.header.discard.setOnClickListener(this);
        binding.filterIcon.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);

        reserversationSummary.CheckOutDate = getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime");
        reserversationSummary.CheckInDate =getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime");

     /*   apiService = new ApiService(getVehicleList, RequestType.POST,
                AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                params.getVechicleList(pickuploc.Id,
                        getArguments().getString("pickupdate")+ "T" +getArguments().getString("pickuptime"),
                        getArguments().getString("dropdate")+ "T" +getArguments().getString("droptime")));
*/
        try {
            VehicleModel vm = new VehicleModel();
            vm = (VehicleModel) getArguments().getSerializable("Model");
            new ApiService(getVehicleList, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                    params.getVechicleList(pickuploc.Id,getArguments().getString("pickupdate"),getArguments().getString("dropdate"),vm.VehicleTypeId));
        } catch (Exception e){
            e.printStackTrace();
            new ApiService(getVehicleList, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                    params.getVechicleList(pickuploc.Id,getArguments().getString("pickupdate"),getArguments().getString("dropdate")));
        }




     /*   binding.edtSearchVehicleName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try {

                    int len;
                    len = VehicleList.length();
                    if (len>0){
                    binding.relativeVehicleAvailable.removeAllViews();
                    for (int j = 0; j < len; j++)
                    {
                        if(vehicleModel[j].VehicleName.contains(charSequence)){
                            getSubview(j);
                            int finalJ = j;
                            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    binding.relativeVehicleAvailable.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                    bundle.putSerializable("Model", vehicleModel[finalJ]);
                                    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, bundle);
                                }
                            });
                            binding.relativeVehicleAvailable.addView(vehicleAvailableListBinding.getRoot());
                        }
                    }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });*/

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

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        VehicleList = resultSet.getJSONArray("ListOfVehicle");
                        ReservationTimeModel timeModel = new ReservationTimeModel();
                        timeModel = loginRes.getModel(resultSet.getString("ReservationTimeModel"), ReservationTimeModel.class);
                        bundle.putSerializable("timemodel", timeModel);
                        UserReservationData.reservationTimeModel = timeModel;
                        reserversationSummary.TotalDays = timeModel.TotalDays;
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
                            vehicleAvailableListBinding.setUiColor(UiColor);
                            vehicleAvailableListBinding.setVehicle(vehicleModel[j]);
                            vehicleAvailableListBinding.getRoot().setId(200 + j);
                            vehicleAvailableListBinding.getRoot().setLayoutParams(subparams);
                            int finalJ = j;

                       /*     vehicleAvailableListBinding.available.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    binding.relativeVehicleAvailable.removeAllViews();
                                    new ApiService(getVehicleList, RequestType.POST, AVAILABLEVICHICLE, BASE_URL_LOGIN, header,
                                         //   params.getVechicleList(pickuploc.Id,getArguments().getString("pickupdate"),getArguments().getString("dropdate"),vehicleModel[finalJ].VehicleTypeId));
                                            params.getVechicleList(pickuploc.Id,reserversationSummary.CheckInDate,reserversationSummary.CheckOutDate,vehicleModel[finalJ].VehicleTypeId));
                                }
                            });*/

                            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    binding.relativeVehicleAvailable.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                    bundle.putSerializable("Model", vehicleModel[finalJ]);
                                    reserversationSummary.ReservationVehicleModel.VehicleTypeId = vehicleModel[finalJ].VehicleTypeId;
                                    reserversationSummary.ReservationVehicleModel.VehicleId = vehicleModel[finalJ].Id;
                                    reserversationSummary.ReservationVehicleModel.CurrentOdo = vehicleModel[finalJ].CurrentOdo;
                                    bundle.putSerializable("reservationSum", reserversationSummary);
                                    vm = vehicleModel[finalJ];
                                    Helper.defaultInsurance = true;
                                  //  apiService = new ApiService(ReserCal, RequestType.POST, RATE, BASE_URL_LOGIN, header, params.getRate(vehicleModel[finalJ].RateId, vehicleModel[finalJ].VehicleTypeId, pickuploc.Id));

                                /*    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, bundle);*/
                                    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.vehicles_available_to_new_agreemnent, bundle);
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
            fullProgressbar.hide();
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
                        } else {
                            String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                            fullProgressbar.hide();
                        }
                    }  catch (Exception e)
                    {
                        e.printStackTrace();
                        fullProgressbar.hide();
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
                        Log.d(TAG, "onSuccess: " + resultSet);
                        bundle.putString("insuranceOption", array.toString());
                        int idd = 0;
                        for (int i = 0; i <array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Boolean isSelect = obj.getBoolean("IsSelected");
                            idd = obj.getInt("Id");
                            if (isSelect){
                                insurance = true;
                            }
                        }

                        if (!insurance) {
                            bundle.putInt("idd", idd);
                        }

                        NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                .navigate(R.id.vehicles_available_to_new_agreemnent, bundle);
                    }
                    else
                    {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                        fullProgressbar.hide();
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
            Log.d(TAG, "onError: " + error);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discard:
                /*NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Search_activity);*/
                Intent intent = new Intent( getActivity(), Activity_Home.class);
                startActivity(intent);
                break;

            case R.id.filter_icon:
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Vehicles_FilterList,bundle);
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this).navigate(R.id.vehicles_available_to_location,bundle);
                       // .navigate(R.id.action_Vehicles_Available_to_Selected_location, bundle);
                break;

        }
    }
}
//,"doc_Name":"14332_QRCode_20502021034350.png","doc_Details":"../Images/Company/204/Booking/QRCode/6721/14332_QRCode_20502021034350.png"

