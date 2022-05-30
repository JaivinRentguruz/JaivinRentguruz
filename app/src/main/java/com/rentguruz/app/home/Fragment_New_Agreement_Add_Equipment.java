package com.rentguruz.app.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementAddEquipmentBinding;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.insert.EquipmentTaxMappingModel;
import com.rentguruz.app.model.insert.InsertEquipment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.EQUIPMENTINSERT;
import static com.rentguruz.app.apicall.ApiEndPoint.TAXLIST;

public class Fragment_New_Agreement_Add_Equipment extends BaseFragment {

    FragmentNewAgreementAddEquipmentBinding binding;
    InsertEquipment insertEquipment;
    List<OnDropDownList> data;
    int idd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementAddEquipmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.add) + " " + companyLabel.Equipment);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        data = new ArrayList<>();
        insertEquipment = new InsertEquipment();
        DropDown dropDownList;
        dropDownList = (new DropDown(TAXLIST, Integer.parseInt(loginRes.getData("CompanyId")), true, false));

        new ApiService2<>(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(() -> {
                    try {

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                        OnDropDownList[] onDropDownLists;
                        List<String> strings = new ArrayList<>();
                        onDropDownLists = loginRes.getModel(getReservationList.toString(), OnDropDownList[].class);
                        for (int i = 0; i < onDropDownLists.length; i++) {
                            // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                            OnDropDownList onDropDownList = new OnDropDownList();
                            onDropDownList = loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                            data.add(onDropDownList);

                            strings.add(onDropDownLists[i].Name);
                        }

                        //   getSpinner(binding.makeId,strings);
                        listSpinner(data);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList);

        binding.maximum.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                insertEquipment.IsChargePerDay = true;
            } else {
                insertEquipment.IsChargePerDay = false;
            }
        });

        binding.perdaycherges.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                insertEquipment.UseMaxPrice = true;
            } else {
                insertEquipment.UseMaxPrice = false;
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
            case R.id.save:
                if (validation()) {
                    loginRes.testingLog(TAG,  getModel());
                    new ApiService2<>(responseListener, RequestType.POST, EQUIPMENTINSERT, BASE_URL_LOGIN, header, getModel());
                }
                break;
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_New_Agreement_Add_Equipment.this).popBackStack();
                break;
        }
    }

    public void listSpinner(List<OnDropDownList> data) {
        List<String> business = new ArrayList<>();
        int select = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).TableType == TAXLIST) {
                business.add(data.get(i).Name);
            }
        }
        ArrayAdapter<String> adapterbusiness = new ArrayAdapter<String>(context, R.layout.spinner_layout, R.id.text1, business);
        binding.tax.setAdapter(adapterbusiness);
        binding.tax.setSelection(select);
    }

    public void getDropdownId(String name) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).Name.equals(name)) {
                idd = data.get(i).Id;
            }
        }
    }

    public Boolean validation() {
        Boolean value = false;
        if (binding.name.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Equipment Number", 1);
        else if (binding.discription.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Discription", 1);
        else if (binding.priceperday.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Price Per day", 1);
        else if (binding.maxamount.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Maximum Price", 1);
        else if (binding.replacementcost.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Serial Number", 1);
        else {
            value = true;
        }
        return value;
    }

    public InsertEquipment getModel() {
        insertEquipment.Name = binding.name.getText().toString();
        insertEquipment.Description = binding.discription.getText().toString();
        getDropdownId(binding.tax.getSelectedItem().toString());
        insertEquipment.EquipmentTaxMappingModel.add(new EquipmentTaxMappingModel(idd));
        insertEquipment.Make = binding.make.getText().toString();
        insertEquipment.Model = binding.model.getText().toString();
        insertEquipment.MaxPrice = Double.valueOf(binding.maxamount.getText().toString());
        insertEquipment.Price = Double.valueOf(binding.priceperday.getText().toString());
        insertEquipment.ReplacementCost = Double.valueOf(binding.replacementcost.getText().toString());
        return insertEquipment;
    }

    OnResponseListener responseListener = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    if (status) {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        Log.e(TAG, "run: " + resultSet);
                        NavHostFragment.findNavController(Fragment_New_Agreement_Add_Equipment.this).popBackStack();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

        @Override
        public void onError(String error) {

        }
    };
}
