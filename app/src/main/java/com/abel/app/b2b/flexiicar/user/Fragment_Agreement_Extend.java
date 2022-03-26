package com.abel.app.b2b.flexiicar.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.SummaryDisplay;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAgreementExtendBinding;
import com.abel.app.b2b.model.response.Reservation;

public class Fragment_Agreement_Extend extends BaseFragment {

    FragmentAgreementExtendBinding binding;
    Reservation reservations;
    SummaryDisplay summaryDisplay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementExtendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.header.screenHeader.setText(getResources().getString(R.string.extendagreement));
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        summaryDisplay = new SummaryDisplay(getActivity());
        try {
            reservations = new Reservation();
            reservations = (Reservation) getArguments().getSerializable("reservation");
            binding.reservation.setReservation(reservations);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Agreement_Extend.this).popBackStack();
                break;
          /*  case R.id.next:
                NavHostFragment.findNavController(Fragment_Agreement_Extend.this).navigate(R.id.test);
                break;*/
        }
    }
}