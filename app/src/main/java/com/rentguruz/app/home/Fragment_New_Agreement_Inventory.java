package com.rentguruz.app.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementInventoryBinding;
import com.rentguruz.app.databinding.RowInventoryBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.reservation.ReservationInventory;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RIequipment;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.INVENTORY;

public class Fragment_New_Agreement_Inventory extends BaseFragment {

    FragmentNewAgreementInventoryBinding binding;
    LocationList pickuplocation;
    VehicleModel model;
    ReservationInventory[] inventory;
    ReservationSummarry reservationSummarry;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementInventoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Inventory);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.add.setOnClickListener(this);
        binding.setUiColor(UiColor);
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer",(Customer) getArguments().getSerializable("customer"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        pickuplocation = new LocationList();
        model = new VehicleModel();
        pickuplocation = (LocationList) getArguments().getSerializable("model");
        model = (VehicleModel) getArguments().getSerializable("Model");
        reservationSummarry = new ReservationSummarry();
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        apiService = new ApiService(getInventory, RequestType.POST, INVENTORY, BASE_URL_LOGIN, header, params.getInventory());
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                NavHostFragment.findNavController(Fragment_New_Agreement_Inventory.this).navigate(R.id.inventory_to_addinventory);
                break;
            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_New_Agreement_Inventory.this).popBackStack();
                break;
        }
    }

    OnResponseListener getInventory = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        inventory = loginRes.getModel(array.toString(), ReservationInventory[].class);
                        Log.d(TAG, "onSuccess: " + resultSet);
                        for (int i = 0; i <inventory.length ; i++) {
                            //JSONObject obj = array.getJSONObject(i);
                            getSubview(i);
                            RowInventoryBinding rowInventoryBinding = RowInventoryBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            rowInventoryBinding.setInventory(inventory[i]);
                            rowInventoryBinding.getRoot().setId(200+i);
                            rowInventoryBinding.getRoot().setLayoutParams(subparams);
                            binding.adddata.addView(rowInventoryBinding.getRoot());

                           /* rowInventoryBinding.getRoot().setOnClickListener(v ->
                                    NavHostFragment.findNavController(Fragment_New_Agreement_Inventory.this).popBackStack()
                            );*/

                            int finalI = i;
                            rowInventoryBinding.getRoot().setOnClickListener(v -> {
                                reservationSummarry.ReservationEquipmentInventoryModel.add(new RIequipment(2, inventory[finalI].Id,Integer.valueOf(inventory[finalI].Price.intValue())));

                                bundle.putSerializable("reservationSum", reservationSummarry);
                                NavHostFragment.findNavController(Fragment_New_Agreement_Inventory.this).navigate(R.id.inventory_to_booking,bundle);
                            });

                        }
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
}
