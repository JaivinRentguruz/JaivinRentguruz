package com.rentguruz.app.home.vehicles;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.fragment.NavHostFragment;
import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehiclesBinding;
import com.rentguruz.app.databinding.VehicleAvailableList2Binding;
import com.rentguruz.app.databinding.VehicleAvailableListBinding;
import com.rentguruz.app.databinding.VehicleAvailableListtBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.base.UserReservationData;
import com.rentguruz.app.model.reservation.ReservationVehicleType;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLEVICHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFILTER;

public class Fragment_Vehicles  extends BaseFragment {

    FragmentVehiclesBinding binding;
    JSONArray VehicleList = new JSONArray();


    List<VehicleModel> vehicleModelList = new ArrayList<>();
   // RowVehicleBinding vehicleAvailableListBinding;
    VehicleAvailableListtBinding vehicleAvailableListBinding;

    int TotalRecord=0;

    int page = 1;
    int len;
    boolean scroll = true;

    int recordList;
    OptionMenu optionMenu;
    public static ReservationVehicleType[] reservationVehicleType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehiclesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        optionMenu = new OptionMenu(getActivity());
        fullProgressbar.show();
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(companyLabel.Vehicle + " List");
        binding.header.discard.setText(getResources().getString(R.string.add));
        binding.header.back.setVisibility(View.GONE);
        binding.header.optionmenu.setVisibility(View.GONE);
        binding.header.screenHeader.setPadding(40,0,0,0);

        try {
            Log.e(TAG, "onViewCreated: " + getArguments().getInt("staus") );
                new ApiService(getVehicleList, RequestType.POST,
                        AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord,
                        getArguments().getInt("staus"),
                        getArguments().getInt("vehiceltype"),
                        getArguments().getBoolean("10day"),
                        getArguments().getBoolean("checking")
                ));

        } catch (Exception e ){
            e.printStackTrace();
            new ApiService(getVehicleList, RequestType.POST,
                    AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord));
        }
        try {
            ((Activity_Vehicles)getActivity()).BottomnavVisible();
        } catch (Exception e){
            e.printStackTrace();
        }


        binding.header.discard.setOnClickListener(this);

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vehicleModelList.clear();
                binding.listItem.removeAllViews();
                ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                        AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(0, String.valueOf(s)));
            }
        });


        binding.nested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
              //  Log.e(TAG, "onScrollChange: " + scrollX  + " : " + scrollY  + " : " + oldScrollX + " : " + oldScrollY + " : " + v.getMaxScrollAmount());
              /*  if (scrollY  == 4000){
                    Log.e(TAG, "onScrollChange: " + scrollX  + " : " + scrollY  + " : " + oldScrollX + " : " + oldScrollY + " : " + v.getMaxScrollAmount());
                }*/

               // Log.e(TAG, "onScrollChange: " + scrollY + " : " + v.getChildAt(0).getMeasuredHeight() + " : " + v.getMeasuredHeight());
                if (len > 19) {
              if (scroll) {
                  if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 100) {
                      Log.e(TAG, "onScrollChange: ");
                      scroll = false;

                      ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                              AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord));
                      fullProgressbar.show();
                  }
              }
                }
              /*  if (scrollY > page * 3960 && scrollY < page * 4150){
                    Log.e(TAG, "onScrollChange: " + scrollY );

                    ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                            AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord+1));

                }*/
            }
        });

        try {

        } catch (Exception e){
            e.printStackTrace();
        }

        optionMenu.vehiclefilter(binding.sucessfullRegi,view,bundle,Fragment_Vehicles.this,header,params);

     /*   new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                try {

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                   // JSONObject resultSet = responseJSON.getJSONObject("Data");
                    JSONArray data = responseJSON.getJSONArray("Data");
                    reservationVehicleType = loginRes.getModel(data.toString(),ReservationVehicleType[].class);

                   // optionMenu.vehiclefilter(binding.sucessfullRegi,view,bundle,Fragment_Vehicles.this,header,params,reservationVehicleType);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, VEHICLEFILTER, BASE_URL_LOGIN, header, params.getEquipment());*/
        binding.view.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.filter_Vehicles:
                NavHostFragment.findNavController(Fragment_Vehicles.this)
                        .navigate(R.id.action_Vehicles_to_Vehicles_Filter);
                break;*/

            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Vehicles.this)
                        .navigate(R.id.action_Vehicles_to_VehiclesBasicDetail);
                break;
        }
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
                        //     VehicleModel[] vehicleModel = loginRes.getModel(VehicleList.toString(), VehicleModel[].class);
                        VehicleModel[] vehicleModel;
                        vehicleModel = loginRes.getModel(VehicleList.toString(), VehicleModel[].class);

                      //  Type listType = new TypeToken<ArrayList<VehicleModel>>(){}.getType();
                       // vehicleModelList = (List<VehicleModel>) loginRes.getModelList(VehicleList.toString(), VehicleModel.class);

                        len = vehicleModel.length;
                        for (int j = 0; j < len; j++)
                        {

                            vehicleModelList.add(vehicleModel[j]);

                       /*     getSubview(j);
                            vehicleAvailableListBinding = RowVehicleBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            vehicleAvailableListBinding.setVehicle(vehicleModel[j]);
                            vehicleAvailableListBinding.getRoot().setId(200 + j);
                            vehicleAvailableListBinding.getRoot().setLayoutParams(subparams);
                            int finalJ = j;

                            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    binding.listItem.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                                    bundle.putSerializable("Model", vehicleModel[finalJ]);
                                    bundle.putInt("Idd", vehicleModel[finalJ].Id );
                                    NavHostFragment.findNavController(Fragment_Vehicles.this)
                                            .navigate(R.id.action_Vehicles_to_update_vehicle, bundle);
                                }
                            });
                            binding.listItem.addView(vehicleAvailableListBinding.getRoot());*/

                        //    getViewDetail(vehicleModelList);
                        }
                        if (vehicleModelList.size()>0) {
                            int ttt = vehicleModelList.size() - 1;
                            recordList = vehicleModelList.get(ttt).TotalRecord;
                            Log.e(TAG, "onSuccess: " + recordList);
                        }
                        getViewDetail();
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
            fullProgressbar.hide();
            System.out.println("Error-" + error);
        }
    };

    public void getViewDetail(){
        Log.e(TAG, "getViewDetail: " + TotalRecord );
        if (vehicleModelList.size() < recordList){
            TotalRecord += 1;
            scroll = true;
            Log.e(TAG, "getViewDetail: " + TotalRecord );
        }
        for (int j = 0; j <vehicleModelList.size() ; j++) {
           // Log.e(TAG, "getViewDetail: " + vehicleModelList.size() );
            getSubview(j);
            vehicleAvailableListBinding = VehicleAvailableListtBinding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            vehicleAvailableListBinding.setVehicle(vehicleModelList.get(j));
            vehicleAvailableListBinding.getRoot().setId(200 + j);
            vehicleAvailableListBinding.getRoot().setLayoutParams(subparams);
            vehicleAvailableListBinding.setUiColor(UiColor);
            int finalJ = j;

            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                 //   binding.listItem.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                    bundle.putSerializable("Model", vehicleModelList.get(finalJ));
                    bundle.putInt("Idd", vehicleModelList.get(finalJ).Id);
                   /* NavHostFragment.findNavController(Fragment_Vehicles.this)
                            .navigate(R.id.action_Vehicles_to_update_vehicle, bundle);*/

                    NavHostFragment.findNavController(Fragment_Vehicles.this)
                            .navigate(R.id.action_Vehicles_to_Vehicle_Master, bundle);
                }
            });
            binding.listItem.addView(vehicleAvailableListBinding.getRoot());
        }
    }
}


