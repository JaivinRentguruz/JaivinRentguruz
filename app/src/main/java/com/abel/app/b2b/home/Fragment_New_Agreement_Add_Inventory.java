package com.abel.app.b2b.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementAddInventoryBinding;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.EQUIPMENTINSERT;

public class Fragment_New_Agreement_Add_Inventory extends BaseFragment {

    FragmentNewAgreementAddInventoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementAddInventoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText(getResources().getString(R.string.add) + " " + companyLabel.Inventory);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
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
                NavHostFragment.findNavController(Fragment_New_Agreement_Add_Inventory.this).popBackStack();
                break;
        }
    }
}
