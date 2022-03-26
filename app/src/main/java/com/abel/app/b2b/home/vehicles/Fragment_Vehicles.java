package com.abel.app.b2b.home.vehicles;

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

import com.google.gson.reflect.TypeToken;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentVehiclesBinding;
import com.abel.app.b2b.databinding.RowVehicleBinding;
import com.abel.app.b2b.databinding.VehicleAvailableList2Binding;
import com.abel.app.b2b.databinding.VehicleAvailableListBinding;
import com.abel.app.b2b.flexiicar.booking.Fragment_Vehicles_Available;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.AVAILABLEVICHICLE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.RATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONGETALL;

public class Fragment_Vehicles  extends BaseFragment {

    FragmentVehiclesBinding binding;
    JSONArray VehicleList = new JSONArray();


    List<VehicleModel> vehicleModelList = new ArrayList<>();
   // RowVehicleBinding vehicleAvailableListBinding;
    VehicleAvailableList2Binding vehicleAvailableListBinding;

    int TotalRecord=0;

    int page = 1;

    boolean scroll = true;

    int recordList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehiclesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fullProgressbar.show();
        ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord));

        binding.txtAddVehicle.setOnClickListener(this);

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
              if (scroll) {
                  if (scrollY > v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - 100) {
                      Log.e(TAG, "onScrollChange: ");
                      scroll = false;

                      ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                              AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord));
                      fullProgressbar.show();
                  }
              }
              /*  if (scrollY > page * 3960 && scrollY < page * 4150){
                    Log.e(TAG, "onScrollChange: " + scrollY );

                    ApiService apiService = new ApiService(getVehicleList, RequestType.POST,
                            AVAILABLEVICHICLE, BASE_URL_CUSTOMER, header, params.getVehicleList(TotalRecord+1));

                }*/
            }
        });


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

            case R.id.txtAddVehicle:
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
                        int len;
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
            vehicleAvailableListBinding = VehicleAvailableList2Binding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            vehicleAvailableListBinding.setVehicle(vehicleModelList.get(j));
            vehicleAvailableListBinding.getRoot().setId(200 + j);
            vehicleAvailableListBinding.getRoot().setLayoutParams(subparams);
            int finalJ = j;

            vehicleAvailableListBinding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    binding.listItem.getChildAt(finalJ).setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
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


