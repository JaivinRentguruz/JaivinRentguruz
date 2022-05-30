package com.rentguruz.app.home.vehicles;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleMasterUploadimageBinding;
import com.rentguruz.app.model.display.ThemeColors;
import com.rentguruz.app.model.response.VehicleModel;
import com.sdsmdg.harjot.vectormaster.enums.TintMode;

import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETIMAGE;

public class Fragment_Vehicle_Master_Images extends BaseFragment {

    FragmentVehicleMasterUploadimageBinding binding;
    VehicleModel vehicleModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleMasterUploadimageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getResources().getString(R.string.specification));

        JSONObject object = new JSONObject();
        try {
            object.accumulate("fileUploadType","8");
            object.accumulate("CompanyId", Helper.id);
        } catch (Exception e){
            e.printStackTrace();
        }
//        binding.test.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));

        //binding.test.getBackground().getState();

        LayerDrawable layerDrawable = (LayerDrawable) getResources()
                .getDrawable(R.drawable.custome_cam_select);
        Drawable drawable =   layerDrawable.findDrawableByLayerId(R.id.sample);
        drawable.setTint(Color.parseColor(UiColor.primary));
        binding.test.setBackground(layerDrawable);

        binding.images.imgg1.setBackground(userDraw.getImageUpload());
        binding.images.imgg2.setBackground(userDraw.getImageUpload());
        binding.images.imgg3.setBackground(userDraw.getImageUpload());
        binding.images.imgg4.setBackground(userDraw.getImageUpload());
        binding.images.imgg5.setBackground(userDraw.getImageUpload());
        binding.images.imgg6.setBackground(userDraw.getImageUpload());
        binding.images.imgg7.setBackground(userDraw.getImageUpload());
        binding.images.imgg8.setBackground(userDraw.getImageUpload());
        binding.images.imgg9.setBackground(userDraw.getImageUpload());
        binding.images.imgg10.setBackground(userDraw.getImageUpload());

        //DrawableCompat.setTint(binding.test.getBackground(),Color.parseColor(appcolor.PrimaryColor));

        /*Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.custome_cam_select);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(appcolor.PrimaryColor));

        binding.test.setBackground(wrappedDrawable);*/

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {

            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST,GETIMAGE,BASE_URL_LOGIN,header,object);

        binding.header.screenHeader.setText(companyLabel.Vehicle + " " + getResources().getString(R.string.detailed));
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);

        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");
        bundle.putSerializable("Model", vehicleModel);

        CustomBindingAdapter.loadImage(binding.carimage, vehicleModel.DefaultImagePath);
        binding.txtOdoMeter.setText(String.valueOf(vehicleModel.CurrentOdo));
        binding.isActive.setChecked(vehicleModel.IsActive);
        binding.isOnline.setChecked(vehicleModel.IsOnline);
        binding.isTemSuspend.setChecked(vehicleModel.IsTemSuspend);
        getToggle(binding.isActive, UiColor, vehicleModel.IsActive);
        getToggle(binding.isOnline, UiColor, vehicleModel.IsOnline);
        getToggle(binding.isTemSuspend, UiColor, vehicleModel.IsTemSuspend);

        //CustomBindingAdapter.loadImage(binding.images.carImage1,vehicleModel.AttachmentsModels.get(0).AttachmentPath);
       // binding.images.setVehicle(vehicleModel);

        try {
            CustomBindingAdapter.loadImage(binding.images.carImage1, vehicleModel.AttachmentsModels.get(1).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage2, vehicleModel.AttachmentsModels.get(2).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage3, vehicleModel.AttachmentsModels.get(3).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage4, vehicleModel.AttachmentsModels.get(4).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage5, vehicleModel.AttachmentsModels.get(5).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage6, vehicleModel.AttachmentsModels.get(6).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage7, vehicleModel.AttachmentsModels.get(7).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage8, vehicleModel.AttachmentsModels.get(8).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage9, vehicleModel.AttachmentsModels.get(9).AttachmentPath);
            CustomBindingAdapter.loadImage(binding.images.carImage10, vehicleModel.AttachmentsModels.get(10).AttachmentPath);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicle_Master_Images.this).popBackStack();
                break;
        }
    }

    public void getToggle(ToggleButton toggleButton, ThemeColors themeColors, Boolean value){
        if (value) {
            toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.primary), PorterDuff.Mode.SCREEN);
        } else {
            toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.secondary), PorterDuff.Mode.SCREEN);
        }

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.primary), PorterDuff.Mode.SCREEN);
            } else {
                toggleButton.getBackground().setColorFilter(Color.parseColor(themeColors.secondary), PorterDuff.Mode.SCREEN);
            }
        });
    }
}
