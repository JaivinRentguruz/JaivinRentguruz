package com.rentguruz.app.flexiicar.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAgreementCancelBinding;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CANCELAUTORISEDBY;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;

public class Fragment_Agreement_Cancel extends BaseFragment {

    FragmentAgreementCancelBinding binding;
    Reservation reservations;
    ReservationSummarry reservationSummarry;
    CustomeDialog dialog;
    private DateConvert dateConvert;
    List<OnDropDownList> data = new ArrayList<>();
    DropDown dropDownList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementCancelBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setLabel(companyLabel);
        binding.setUiColor(UiColor);
        reservations = new Reservation();
        reservationSummarry = new ReservationSummarry();
        dialog = new CustomeDialog(getActivity());
        dateConvert = new DateConvert();
        binding.header.screenHeader.setText(companyLabel.Reservation + " Cancel");
        binding.screenName.setText(companyLabel.Reservation + " Cancel");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.bottom.setOnClickListener(this);
        binding.canceldate.setOnClickListener(this);
        binding.text.setText("Confirm Cancel");
        try {
            reservations = (Reservation) getArguments().getSerializable("reservation");
            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");
            binding.checkoutdate.setText(dateConvert.allDateConverter(DateType.fulldate,reservations.CheckOutDate,DateType.datetime));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(DateType.datetime.anInt);
            binding.canceldate.setText(format.format(c.getTime()));

        }catch (Exception e){
            e.printStackTrace();
        }
            try {
                List<String> charges = new ArrayList<>();
                charges.add(0, "Reason for No Charge");
               // charges.add(RsvCancelDeductionOn.values().toString());
                charges.add("ChargeDeposit");
                charges.add("ChargePayment");
                charges.add("ChargeCreditCard");
                charges.add("Cash");
                charges.add("ManualRefund");
                charges.add("NoCancellationFees");
                ArrayAdapter<String> adaptercancelby = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,charges);
                binding.nochargespinner.setAdapter(adaptercancelby);
            } catch (Exception e){
                e.printStackTrace();
            }

        try {
            dropDownList = (new DropDown(CANCELAUTORISEDBY,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
            new ApiService2<DropDown>(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                OnDropDownList[] onDropDownLists;
                                List<String> strings = new ArrayList<>();
                                onDropDownLists = loginRes.getModel(getReservationList.toString(),OnDropDownList[].class);
                                for (int i = 0; i < onDropDownLists.length; i++) {
                                    // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                    OnDropDownList onDropDownList = new OnDropDownList();
                                    onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                    data.add(onDropDownList);

                                    strings.add(onDropDownLists[i].Name);
                                }

                                //   getSpinner(binding.makeId,strings);
                                listSpinner(data);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, dropDownList);

            reservations = (Reservation) getArguments().getSerializable("reservation");
            reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationsum");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_Cancel.this).popBackStack();
                break;

            case R.id.bottom:
                NavHostFragment.findNavController(Fragment_Agreement_Cancel.this).popBackStack();
                break;

            case R.id.canceldate:
                dialog.getFullDate(string -> binding.canceldate.setText(string));
                break;

        }
    }

    public void listSpinner(List<OnDropDownList> data){
        List<String> cancelby = new ArrayList<>();
        cancelby.add(0,"Authorized By");
        int select = 0;
        for (int i = 0; i <data.size() ; i++) {
            cancelby.add(data.get(i).Name);
            if (data.get(i).Id == reservationSummarry.ReservationVehicleModel.VehicleId){
                select = i;
            }
        }
        ArrayAdapter<String> adaptercancelby = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,cancelby);
        binding.authorised.setAdapter(adaptercancelby);
        binding.authorised.setSelection(select);

    }

}
