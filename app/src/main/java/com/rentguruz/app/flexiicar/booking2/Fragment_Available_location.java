package com.rentguruz.app.flexiicar.booking2;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAvailableLocationBinding;
import com.rentguruz.app.databinding.LocationAvailableListBinding;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.LocationList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;

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

        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Location);
        binding.subheader.setText(getResources().getString(R.string.available) + " " + companyLabel.Location);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fullProgressbar.show();
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.rlToggleBtn.setOnClickListener(this);
        binding.setUiColor(UiColor);

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
            case R.id.discard:

            case R.id.back:

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