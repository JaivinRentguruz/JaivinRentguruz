package com.rentguruz.app.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentNewAgreementAddInventoryBinding;

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
        binding.setUiColor(UiColor);
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
