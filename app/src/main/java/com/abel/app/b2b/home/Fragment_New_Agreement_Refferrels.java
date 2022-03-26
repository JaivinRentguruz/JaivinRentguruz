package com.abel.app.b2b.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementReferalBinding;

public class Fragment_New_Agreement_Refferrels extends BaseFragment {

    FragmentNewAgreementReferalBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementReferalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.add.setOnClickListener(this);
        binding.header.screenHeader.setText("Select Refferal");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                NavHostFragment.findNavController(Fragment_New_Agreement_Refferrels.this).navigate(R.id.refferrels_to_addrefferrels);
                break;
            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_New_Agreement_Refferrels.this).popBackStack();
                break;

        }
    }
}
