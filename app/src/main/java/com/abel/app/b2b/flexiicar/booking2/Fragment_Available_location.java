package com.abel.app.b2b.flexiicar.booking2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAvailableLocationBinding;
import com.abel.app.b2b.databinding.LocationAvailableListBinding;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Location;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.LocationList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;

public class Fragment_Available_location extends BaseFragment
{
    public String id = "";
    int flag=0;
    FragmentAvailableLocationBinding binding;
    LocationList []  locationLists;
    Boolean value = false;
    Boolean screen = false;
    public static Boolean dropdown = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentAvailableLocationBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        binding.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Location);
        binding.subheader.setText(getResources().getString(R.string.available) + " " + companyLabel.Location);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fullProgressbar.show();
        binding.txtDiscardLoc.setOnClickListener(this);
        binding.backArrowImg.setOnClickListener(this);
        binding.rlToggleBtn.setOnClickListener(this);


     /*   try {
        value = getArguments().getBoolean("locationType");
        bundle.putSerializable("model", getArguments().getSerializable("model"));
        Helper.isDropdown = false;
        Log.d(TAG, "onViewCreated: " + value);


            RelativeLayout rlLocationlist = getActivity().findViewById(R.id.location_available_layout);
            rlLocationlist.removeAllViews();
            int len;
            len = Fragment_Selected_Location.locationListsData.length;

            for (int j = 0; j < len; j++)
            {
                getSubview(j);
                LocationAvailableListBinding locationAvailableListBinding = LocationAvailableListBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false );
                locationAvailableListBinding.setLocation(Fragment_Selected_Location.locationListsData[j]);
                locationAvailableListBinding.txtStreet.setText(Html.fromHtml(Fragment_Selected_Location.locationListsData[j].AddressesModel.PreviewAddress));
                locationAvailableListBinding.getRoot().setId(200+j);
                locationAvailableListBinding.getRoot().setLayoutParams(subparams);
                binding.locationAvailableLayout.addView(locationAvailableListBinding.getRoot());
                int finalJ = j;
                locationAvailableListBinding.lblselectedlocation.setOnClickListener(v -> {
                    if (value) {
                        bundle.putSerializable("model", Fragment_Selected_Location.locationListsData[finalJ]);
                    } else {
                        bundle.putSerializable("models", Fragment_Selected_Location.locationListsData[finalJ]);
                    }
                    NavHostFragment.findNavController(Fragment_Available_location.this)
                            .navigate(R.id.action_Available_location_to_Selected_location, bundle);
                });
            }


        } catch (Exception e){
            e.printStackTrace();
        }*/

        screen = getArguments().getBoolean("screen");
        value = getArguments().getBoolean("locationType");
        bundle.putSerializable("model", getArguments().getSerializable("model"));
        Log.d(TAG, "onViewCreated: " + value);
        JSONArray locationtype = new JSONArray();
        try {
            String location =    loginRes.getData("LocationMasterIds");

            for (int i = 0; i < location.split(",").length; i++) {
                locationtype.put(Integer.valueOf(location.split(",")[i].trim()));
            }
        } catch (Exception e){
            e.printStackTrace();
            locationtype = Customer_Booking_Activity.ints;
        }

        apiService = new ApiService(LocationDataList, RequestType.POST,
                AVAILABLELOCATION, BASE_URL_LOGIN, header, params.getAvailableLocation(locationtype));


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener LocationDataList = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response1, HashMap<String, String> headers)
        {

            final String response = response1;
            handler.post(() -> {

                try
                {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    fullProgressbar.hide();

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        final JSONArray locationlist = resultSet.getJSONArray("Data");
                        locationLists = loginRes.getModel(locationlist.toString(), LocationList[].class);
                        RelativeLayout rlLocationlist = getActivity().findViewById(R.id.location_available_layout);
                        rlLocationlist.removeAllViews();
                        int len;
                        len = locationLists.length;

                        if (len>0){
                            binding.searchbox.setVisibility(View.VISIBLE);
                        }

                        for (int j = 0; j < len; j++)
                        {
                            getSubview(j);
                            LocationAvailableListBinding locationAvailableListBinding = LocationAvailableListBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false );
                            locationAvailableListBinding.setLocation(locationLists[j]);
                            locationAvailableListBinding.txtStreet.setText(Html.fromHtml(locationLists[j].AddressesModel.PreviewAddress));
                            locationAvailableListBinding.getRoot().setId(200+j);
                            locationAvailableListBinding.getRoot().setLayoutParams(subparams);
                            binding.locationAvailableLayout.addView(locationAvailableListBinding.getRoot());
                            int finalJ = j;
                            locationAvailableListBinding.lblselectedlocation.setOnClickListener(v -> {
                                if(screen){
                                    UserData.companyModel.DefaultReservationPickUpLocId = locationLists[finalJ].Id;
                                    NavHostFragment.findNavController(Fragment_Available_location.this)
                                            .navigate(R.id.action_Available_location_to_Search_activity,bundle);
                                } else {
                                    if (value) {
                                        bundle.putSerializable("model", locationLists[finalJ]);
                                    } else {
                                        bundle.putSerializable("models", locationLists[finalJ]);
                                    }
                                    NavHostFragment.findNavController(Fragment_Available_location.this)
                                            .navigate(R.id.action_Available_location_to_Selected_location, bundle);
                                }
                            });
                        }

                    }
                    else
                    {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_discardLoc:

            case R.id.back_arrow_img:

            case R.id.rlToggleBtn:
                NavHostFragment.findNavController(Fragment_Available_location.this)
                        .navigate(R.id.action_Available_location_to_Search_activity);
                /*Intent intent = new Intent( getActivity(), Activity_Agreements.class);
                startActivity(intent);*/
                break;

            case R.id.edttxt_searchLoc:
              /*  apiService = new ApiService(LocationDataList, RequestType.POST,
                        AVAILABLELOCATION, BASE_URL_LOGIN, header, new JSONObject());*/
                break;
        }
    }
}