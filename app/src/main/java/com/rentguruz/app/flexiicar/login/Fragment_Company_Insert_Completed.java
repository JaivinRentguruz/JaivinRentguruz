package com.rentguruz.app.flexiicar.login;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentRegistrationCompletedBinding;

public class Fragment_Company_Insert_Completed extends BaseFragment {

    FragmentRegistrationCompletedBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationCompletedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        binding.shuttle.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.shuttle.getPathModelByName("two").setFillColor(Color.parseColor(UiColor.primary));
        binding.shuttle.getPathModelByName("three").setFillColor(Color.parseColor(UiColor.primary));
        binding.shuttle.getPathModelByName("four").setFillColor(Color.parseColor(UiColor.secondary));
        binding.shuttle.getPathModelByName("five").setFillColor(Color.parseColor(UiColor.secondary));
        binding.shuttle.getPathModelByName("six").setFillColor(Color.parseColor(UiColor.secondary));
        binding.shuttle.getPathModelByName("seven").setFillColor(Color.parseColor(UiColor.secondary));
        binding.shuttle.getPathModelByName("eight").setFillColor(Color.parseColor(UiColor.secondary));
        binding.shuttle.getPathModelByName("nine").setFillColor(Color.parseColor(UiColor.primary));

        try {
            binding.text.setVisibility(View.INVISIBLE);
            binding.textline1.setVisibility(View.INVISIBLE);
            binding.textline2.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CustomToast.showToast(getActivity(), "Permission Granted", 0);
                } else {
                    CustomToast.showToast(getActivity(), "Permission Denied", 1);
                }
            }
            break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}

