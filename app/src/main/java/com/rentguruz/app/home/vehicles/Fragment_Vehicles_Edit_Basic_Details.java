package com.rentguruz.app.home.vehicles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.rentguruz.app.R;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.ImageOptionMenu;
import com.androidnetworking.AndroidNetworking;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeView;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleBasicDetailsBinding;
import com.rentguruz.app.databinding.RowCheckboxBinding;
import com.rentguruz.app.model.DoVehicle;
import com.rentguruz.app.model.VehicleOptionMappingModel;
import com.rentguruz.app.model.VehicleOtherDetailsModel;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.response.AttachmentsModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.graphics.BitmapFactory.decodeFile;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.IMAGEDELETE;
import static com.rentguruz.app.apicall.ApiEndPoint.MAKEVEHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADIMAGE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLECLASS;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEENGINE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFEATURE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFLEET;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEGETBYID;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEMAKE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEUPDATE;

public class Fragment_Vehicles_Edit_Basic_Details extends BaseFragment {

    FragmentVehicleBasicDetailsBinding binding;
    List<DropDown> dropDownList = new ArrayList<>();
    Handler handler=new Handler(Looper.getMainLooper());
    CustomeView customeView;
    List<OnDropDownList> data = new ArrayList<>();
    List<OnDropDownList> model = new ArrayList<>();
    int idd;
    DoVehicle doVehicle;
    VehicleModel vehicleModel;
    int vehicleyear,vehicledoor,vehicleseat,vehiclebags;
    int vehicleMakeId, vehicleModelId,vehicleClassId,engineId,parkedlocationId,fleetId,ownLocationId;

    String vMake,vModel,vClass,vEngine,vParked,vFeetId,vOwnlocation;
    int iddd;
    VehicleOtherDetailsModel vehicleOtherDetailsModel;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE2 = 2;
    public static List<Bitmap> carimage = new ArrayList<>();
    ImageOptionMenu optionMenu;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static int selectimg = 0;
    int word = 50;
    int counter = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVehicleBasicDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        optionMenu = new ImageOptionMenu(getActivity());
        binding.setUiColor(UiColor);
        optionMenu.optionmenulist(binding.sucessfullRegi, view);
        binding.header.screenHeader.setText("Edit " +companyLabel.Vehicle + " Basic Details");
        binding.header.discard.setVisibility(View.GONE);
        vehicleModel = new VehicleModel();
       // vehicleModel =(VehicleModel)  getArguments().getSerializable("Model");
        iddd= getArguments().getInt("Idd");
        String string = "?id=" + iddd + "&isActive=false";
        //binding.vehDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.vehDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);



        doVehicle = new DoVehicle();
        vehicleOtherDetailsModel= new VehicleOtherDetailsModel();
        dropDownList.add(new DropDown(MAKEVEHICLE,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        dropDownList.add(new DropDown(VEHICLECLASS,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        dropDownList.add(new DropDown(VEHICLELOCATION,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        dropDownList.add(new DropDown(VEHICLEENGINE,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        dropDownList.add(new DropDown(VEHICLEFEATURE,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        dropDownList.add(new DropDown(VEHICLEFLEET,Integer.parseInt(loginRes.getData("CompanyId")),true,false));
        customeView = new CustomeView();

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                            vehicleModel = loginRes.getModel(getReservationList.toString(),VehicleModel.class);
                            doVehicle =loginRes.getModel(getReservationList.toString(),DoVehicle.class);


                            new ApiService2<List<DropDown>>(new OnResponseListener() {
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
                            }, RequestType.POST, COMMONDROPDOWN, BASE_URL_LOGIN, header, dropDownList);


                            binding.makeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    binding.makeId.getSelectedItem();
                                    makeIdSpinner(binding.makeId.getSelectedItem().toString());
                                    Log.e(TAG, "onItemSelected: " + binding.makeId.getSelectedItem() );
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });



                            otherSpinner();
                            customeView.vehicleTypeSpinner(getContext(),binding.VehiclesStatus, doVehicle.Status);
                            customeView.vehicleTransmissionSpinner(getContext(),binding.vehicleTransmission,doVehicle.TransmissionDesc);
                            customeView.vehicleFuel(getContext(),binding.VehiclesFuel, doVehicle.FuelTypeDesc);

                            binding.year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Log.e(TAG, "onItemSelected: "+ binding.year.getSelectedItem() );
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            binding.manufacturer.setText(vehicleModel.Manufacturer);
                            binding.keycode.setText(vehicleModel.KeyCode);
                            binding.licenceNumber.setText(vehicleModel.LicenseNumber);
                            binding.vinNumber.setText(vehicleModel.VinNumber);
                            binding.vehicleNumber.setText(vehicleModel.Number);
                            binding.licplate.setText(vehicleModel.LicenseNumber);
                            binding.tanksize.setText(String.valueOf(vehicleModel.TankSize));
                            binding.vehDescription.setText(doVehicle.VehicleOtherDetailsModel.Description);

                            binding.vehDescription.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    if (binding.vehDescription.getText().length() == word){
                                        binding.vehDescription.append("\n");
                                        if (binding.vehDescription.getText().length() > word){
                                            counter += 1;
                                            word = counter*word;
                                        } else {
                                            //counter += 1;
                                            //word = counter*word;
                                        }
                                    }

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            loginRes.testingLog(TAG,vehicleModel);
                            try {
                                CustomBindingAdapter.loadImage(binding.carImage1, vehicleModel.AttachmentsModels.get(0).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage2, vehicleModel.AttachmentsModels.get(1).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage3, vehicleModel.AttachmentsModels.get(2).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage4, vehicleModel.AttachmentsModels.get(3).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage5, vehicleModel.AttachmentsModels.get(4).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage6, vehicleModel.AttachmentsModels.get(5).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage7, vehicleModel.AttachmentsModels.get(6).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage8, vehicleModel.AttachmentsModels.get(7).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage9, vehicleModel.AttachmentsModels.get(8).AttachmentPath);
                                CustomBindingAdapter.loadImage(binding.carImage10, vehicleModel.AttachmentsModels.get(9).AttachmentPath);



                            } catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                            try {
                                for (int i = 0; i <vehicleModel.AttachmentsModels.size(); i++) {
                                    if (vehicleModel.AttachmentsModels.get(i).AttachmentPath != null){
                                        Log.e(TAG, "run: "+ vehicleModel.AttachmentsModels);
                                        if (i==0) {
                                            imgDelet(binding.cardelete1, 1, vehicleModel.AttachmentsModels.get(i));
                                        } else if (i == 1){
                                            imgDelet(binding.cardelete2, 2, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 2){
                                            imgDelet(binding.cardelete3, 3, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 3){
                                            imgDelet(binding.cardelete4, 4, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 4){
                                            imgDelet(binding.cardelete5, 5, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 5){
                                            imgDelet(binding.cardelete6, 6, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 6){
                                            imgDelet(binding.cardelete7, 7, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 7){
                                            imgDelet(binding.cardelete8, 8, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 8){
                                            imgDelet(binding.cardelete9, 9, vehicleModel.AttachmentsModels.get(i));
                                        }else if (i == 9){
                                            imgDelet(binding.cardelete10, 10, vehicleModel.AttachmentsModels.get(i));
                                        }
                                    }
                                }
                            } catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.GET, VEHICLEGETBYID, BASE_URL_LOGIN, header, string);


        binding.lblNextToRentalRate.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);

       /* binding.carImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
*//*
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);*//*
            }
        });*/

        for (int i = 1; i <=10 ; i++) {
            if (i==1)
                imageClick(binding.carImage1,i);
            if (i==2)
                imageClick(binding.carImage2,i);
            if (i==3)
                imageClick(binding.carImage3,i);
            if (i==4)
                imageClick(binding.carImage4,i);
            if (i==5)
                imageClick(binding.carImage5,i);
            if (i==6)
                imageClick(binding.carImage6,i);
            if (i==7)
                imageClick(binding.carImage7,i);
            if (i==8)
                imageClick(binding.carImage8,i);
            if (i==9)
                imageClick(binding.carImage9,i);
            if (i==10)
                imageClick(binding.carImage10,i);
        }

        try {
            activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Bundle buundal = result.getData().getExtras();
                                Bitmap bitmap = (Bitmap) buundal.get("data");
                                int i = selectimg;

                                if (i==1) {
                                    binding.carImage1.setImageBitmap(bitmap);
                                } else if (i==2){
                                    binding.carImage2.setImageBitmap(bitmap);
                                } else if (i==3){
                                    binding.carImage3.setImageBitmap(bitmap);
                                }else if (i==4){
                                    binding.carImage4.setImageBitmap(bitmap);
                                }else if (i==5){
                                    binding.carImage5.setImageBitmap(bitmap);
                                }else if (i==6){
                                    binding.carImage6.setImageBitmap(bitmap);
                                }else if (i==7){
                                    binding.carImage7.setImageBitmap(bitmap);
                                }else if (i==8){
                                    binding.carImage8.setImageBitmap(bitmap);
                                }else if (i==9){
                                    binding.carImage9.setImageBitmap(bitmap);
                                }else if (i==10){
                                    binding.carImage10.setImageBitmap(bitmap);
                                }

                                carimage.add(bitmap);

                            }
                        }
                    }
            );
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            binding.imgg1.setBackground(userDraw.getImageUpload());
            binding.imgg2.setBackground(userDraw.getImageUpload());
            binding.imgg3.setBackground(userDraw.getImageUpload());
            binding.imgg4.setBackground(userDraw.getImageUpload());
            binding.imgg5.setBackground(userDraw.getImageUpload());
            binding.imgg6.setBackground(userDraw.getImageUpload());
            binding.imgg7.setBackground(userDraw.getImageUpload());
            binding.imgg8.setBackground(userDraw.getImageUpload());
            binding.imgg9.setBackground(userDraw.getImageUpload());
            binding.imgg10.setBackground(userDraw.getImageUpload());
        } catch (Exception e){
            e.printStackTrace();
        }

       // binding.carImage1.setOnClickListener(this);
    }
    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblNextToRentalRate:
                if (validation()){
                new ApiService2<DoVehicle>(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(() -> {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                Log.e(TAG, "run: " + responseJSON);
                                if (status) {
                                   /* NavHostFragment.findNavController(Fragment_Vehicles_Edit_Basic_Details.this)
                                            .popBackStack();*/
                                    NavHostFragment.findNavController(Fragment_Vehicles_Edit_Basic_Details.this)
                                            .popBackStack();
                                      storeImage(carimage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.PUT,VEHICLEUPDATE,BASE_URL_LOGIN,header,getDoVehicle());
                }
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicles_Edit_Basic_Details.this).popBackStack();
                break;

            case R.id.car_image_1:
                   Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 1; i <=10; i++) {
        if (requestCode == i && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = null;
            Uri targetUri = data.getData();
            try
            {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                // imagestr = ConvertBitmapToString(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (i==1) {
                binding.carImage1.setImageBitmap(bitmap);
            } else if (i==2){
                binding.carImage2.setImageBitmap(bitmap);
            } else if (i==3){
                binding.carImage3.setImageBitmap(bitmap);
            }else if (i==4){
                binding.carImage4.setImageBitmap(bitmap);
            }else if (i==5){
                binding.carImage5.setImageBitmap(bitmap);
            }else if (i==6){
                binding.carImage6.setImageBitmap(bitmap);
            }else if (i==7){
                binding.carImage7.setImageBitmap(bitmap);
            }else if (i==8){
                binding.carImage8.setImageBitmap(bitmap);
            }else if (i==9){
                binding.carImage9.setImageBitmap(bitmap);
            }else if (i==10){
                binding.carImage10.setImageBitmap(bitmap);
            }

            carimage.add(bitmap);
        }
        }
    }

    public void getSpinner(Spinner spinner, List<String> data){
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,data);
        spinner.setAdapter(adapterCategories);
    }

    public void getSpinner(Spinner spinner, List<String> data, int i){
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,data);
        spinner.setAdapter(adapterCategories);
        spinner.setSelection(i-1);
    }
    public void listSpinner(List<OnDropDownList> data){
        List<String> makeVehicle = new ArrayList<>();
        List<String> vehicleClass = new ArrayList<>();
        List<String> vehicleEngine = new ArrayList<>();
        List<OnDropDownList> vehicleFeature = new ArrayList<>();
        List<String> location = new ArrayList<>();
        location.add("Parked Location");

        List<String> location2 = new ArrayList<>();
        location2.add("Owning Location");
        List<String> vehicleFleet = new ArrayList<>();

        for (int i = 0; i <data.size() ; i++) {
            if (data.get(i).TableType == MAKEVEHICLE){
                makeVehicle.add(data.get(i).Name);
                if (doVehicle.VehicleMakeId == data.get(i).Id){
                    vMake = data.get(i).Name;
                }
            }
            if (data.get(i).TableType == VEHICLECLASS){
                Log.e(TAG, "listSpinner: " + i  + " : " + data.get(i).Name );
                vehicleClass.add(data.get(i).Name);
                if (doVehicle.VehicleTypeId == data.get(i).Id){
                    vClass = data.get(i).Name;
                }
               /* if (data.get(i).Name.equals(vehicleModel.VehicleClass)){
                    vehicleClassId = i;
                }*/
            }
            if (data.get(i).TableType == VEHICLEENGINE){
                vehicleEngine.add(data.get(i).Name);
               /* if (data.get(i).Name.equals(vehicleModel.EngineName)){
                    engineId = i;
                }*/
                if (doVehicle.EngineId==data.get(i).Id){
                    vEngine = data.get(i).Name;
                }
            }
            if (data.get(i).TableType == VEHICLEFEATURE){
                vehicleFeature.add(data.get(i));
            }
            if (data.get(i).TableType == VEHICLELOCATION){
                location.add(data.get(i).Name);
                location2.add(data.get(i).Name);
                if (doVehicle.CurrentLocation == data.get(i).Id){
                    vParked = data.get(i).Name;
                }

                if (doVehicle.OwningLocation == data.get(i).Id){
                    vOwnlocation = data.get(i).Name;
                }
            }
            if (data.get(i).TableType == VEHICLEFLEET){
                vehicleFleet.add(data.get(i).Name);
                if (doVehicle.VehicleCategoryId == data.get(i).Id){
                    vFeetId = data.get(i).Name;
                }
            }


        }
   /*     vehicleMakeId = getIdd(makeVehicle,vehicleModel.MakeName);
        vehicleClassId = getIdd(vehicleClass,vehicleModel.VehicleClass);
        engineId =  getIdd(vehicleEngine,vehicleModel.EngineName);
        parkedlocationId = getIdd(location,vehicleModel.ParkedLocation);
        fleetId = getIdd(vehicleFleet,vehicleModel.VehicleCategory);*/

        vehicleMakeId = getIdd(makeVehicle,vMake);
        vehicleClassId = getIdd(vehicleClass,vClass);
        engineId =  getIdd(vehicleEngine,vEngine);
        parkedlocationId = getIdd(location,vParked);
        fleetId = getIdd(vehicleFleet,vFeetId);
        ownLocationId = getIdd(location2,vOwnlocation);

        ArrayAdapter<String> adaptermakeVehicle = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,makeVehicle);
        binding.makeId.setAdapter(adaptermakeVehicle);
        binding.makeId.setSelection(vehicleMakeId);

        ArrayAdapter<String> adaptervehicleClass = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleClass);
        binding.VehiclesClass.setAdapter(adaptervehicleClass);
        binding.VehiclesClass.setSelection(vehicleClassId);

        ArrayAdapter<String> adaptervehicleEngine = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleEngine);
        binding.VehiclesEngine.setAdapter(adaptervehicleEngine);
        binding.VehiclesEngine.setSelection(engineId);

        getCheckBox2(vehicleFeature);

        ArrayAdapter<String> adaptervehicleParked = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,location);
        binding.vehicleParked.setAdapter(adaptervehicleParked);
        binding.vehicleParked.setSelection(parkedlocationId);

        ArrayAdapter<String> adaptervehicleOwning = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,location2);
        binding.vehicleOwnLoc.setAdapter(adaptervehicleOwning);
        binding.vehicleOwnLoc.setSelection(ownLocationId);

        ArrayAdapter<String> adaptervehicleFleet = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleFleet);
        binding.VehiclesFleet.setAdapter(adaptervehicleFleet);
        binding.VehiclesFleet.setSelection(fleetId);
    }

    public void getCheckBox2(List<OnDropDownList> data){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        width = width-120;
        int increment = 1;
        int tops =  -1;
        int high = height/20;
        int wid = width/2;
        for (int i = 0; i <data.size() ; i++) {
            RelativeLayout.LayoutParams subparams;
            LayoutInflater subinflater;
            subparams = new RelativeLayout.LayoutParams(wid, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (i % 2 == 0) {
                increment = 1;
                tops = tops + 1;
                subparams.addRule(RelativeLayout.BELOW, 0);
                //subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                subparams.setMargins(0, tops * high, 0, 0);
            } else {
                subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                subparams.addRule(RelativeLayout.BELOW, 0);
                subparams.setMargins(increment * wid, tops * high, 0, 0);
                increment = increment + 1;
            }
            subinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            RowCheckboxBinding rowCheckboxBinding = RowCheckboxBinding.inflate(subinflater,
                    getActivity().findViewById(android.R.id.content), false);
            rowCheckboxBinding.getRoot().setId(200 + i);
            rowCheckboxBinding.getRoot().setLayoutParams(subparams);
            rowCheckboxBinding.checkbox.setText(data.get(i).Name);

            int len = doVehicle.VehicleOptionMappingModel.size();
            for (int j = 0; j < len; j++) {
                if (doVehicle.VehicleOptionMappingModel.get(j).VehicleOptionId == data.get(i).Id){
                    rowCheckboxBinding.checkbox.setChecked(true);
                }
            }

            int finalI = i;
            rowCheckboxBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            doVehicle.VehicleOptionMappingModel.add(new VehicleOptionMappingModel(iddd,data.get(finalI).Id, data.get(finalI).Name));
                        } else {
                            removeId(data.get(finalI).Id);
                        }
                }
            });

            binding.relativelayout.addView(rowCheckboxBinding.getRoot());
        }
    }


    public void removeId(int id){
        for (int i = 0; i <doVehicle.VehicleOptionMappingModel.size(); i++) {
            if (doVehicle.VehicleOptionMappingModel.get(i).VehicleOptionId == id){
                doVehicle.VehicleOptionMappingModel.remove(i);
                break;
            }
        }
    }


    public void otherSpinner(){
        List<String> years = new ArrayList<>();
        List<String> vehicleDoors = new ArrayList<>();
        List<String> vehicleBags = new ArrayList<>();
        List<String> vehicleSeat = new ArrayList<>();
        for (int i = 1; i < 40; i++) {
            if (i<30){
                years.add(String.valueOf(i+1992));
                if (i+1992 == vehicleModel.Year){
                    vehicleyear = i;
                }
            }

            if (i<6){
                vehicleDoors.add(String.valueOf(i));
                if (i == vehicleModel.NoOfDoors){
                    vehicledoor = i;
                }
            }


            if (i<6){
                vehicleBags.add(String.valueOf(i));
                if (i == vehicleModel.NoOfBags){
                    vehiclebags = i;
                }
            }

            vehicleSeat.add(String.valueOf(i));
            if (i == vehicleModel.NoOfSeats){
                vehicleseat = i;
            }
        }
        getSpinner(binding.year, years,vehicleyear);
        getSpinner(binding.VehiclesSeat,vehicleSeat,vehicleseat);
        getSpinner(binding.VehiclesDoors,vehicleDoors,vehicledoor);
        getSpinner(binding.VehiclesBags,vehicleBags,vehiclebags);
    }

    public void makeIdSpinner(String name){
        getDropdownId(name);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.accumulate("TableType", VEHICLEMAKE);
            jsonObject.accumulate("CompanyId", Helper.id);
            jsonObject.accumulate("IsActive", true);
            JSONObject filterObj = new JSONObject();
            filterObj.accumulate("MakeId", String.valueOf(idd));
            jsonObject.accumulate("filterObj",filterObj);
        } catch (Exception e){
            e.printStackTrace();
        }

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                            model.clear();
                            List<String> strings = new ArrayList<>();
                            for (int i = 0; i <getReservationList.length() ; i++) {
                                OnDropDownList onDropDownList = new OnDropDownList();
                                onDropDownList =  loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                model.add(onDropDownList);
                                strings.add(model.get(i).Name);
                                /*if (model.get(i).Name.equals(vehicleModel.ModelName)){
                                    vehicleModelId = i;
                                }*/

                                if (model.get(i).Id == doVehicle.VehicleModelId){
                                    vehicleModelId = i;
                                }
                            }

                            getSpinner(binding.vehicleModel, strings,vehicleModelId+1);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }) ;
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, jsonObject);

    }

    public void getDropdownId(String name){
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).Name.equals(name)){
                idd = data.get(i).Id;
            }
        }
    }

    public int getId(String name){
        int id = 0;
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).Name.equals(name)){
                id = data.get(i).Id;
                return id;
            }
        }
        return id;
    }

    public int getIdd(List<String> data, String name){
        int id = 0;
        for (int i = 0; i <data.size(); i++) {
            if (data.get(i).equals(name)){
                id = i;
                return id;
            }
        }
        return id;
    }

    public int getModelId(String name){
        int id = 0;
        for (int i = 0; i <model.size(); i++) {
            if (model.get(i).Name.equals(name)){
                id = model.get(i).Id;
                return id;
            }
        }
        return id;
    }

    private Boolean validation(){
        boolean value = false;
        if (binding.vinNumber.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Number",1);
        else if (binding.manufacturer.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Manufacturer",1);
        else if (binding.keycode.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Key",1);
        else if (binding.licenceNumber.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Licence Number",1);
        else if (binding.vehicleNumber.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Number",1);
        else if (binding.tanksize.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Vehicle Fuel Tank Size",1);
        else if (binding.VehiclesFuel.getSelectedItemPosition()==0)
            CustomToast.showToast(getActivity(),"Please Select Vehicle Fuel",1);
        else if (binding.vehicleTransmission.getSelectedItemPosition()==0)
            CustomToast.showToast(getActivity(),"Please Select Vehicle Transmission",1);
        /*else if (carimage.size() == 0 )
            CustomToast.showToast(getActivity(),"Please Add Vehicle Image",1);*/
        else
            value=true;

        return value;
    }

    private DoVehicle getDoVehicle(){
        doVehicle.ColorExterior = "";
        doVehicle.ColorInterior = "";
        doVehicle.CurrentLocation = getId(binding.vehicleOwnLoc.getSelectedItem().toString());
        doVehicle.EngineId = getId(binding.VehiclesEngine.getSelectedItem().toString());
        doVehicle.FuelType = binding.VehiclesFuel.getSelectedItemPosition();
        doVehicle.KeyCode = binding.keycode.getText().toString();
        doVehicle.LicenseNumber =binding.licenceNumber.getText().toString();
        doVehicle.MakeName = binding.makeId.getSelectedItem().toString();
        doVehicle.Manufacturer = binding.manufacturer.getText().toString();
        doVehicle.ModelName = binding.vehicleModel.getSelectedItem().toString();
        doVehicle.NoOfDoors = binding.VehiclesDoors.getSelectedItemPosition()+1;
        doVehicle.NoOfSeats = binding.VehiclesSeat.getSelectedItemPosition()+1;
        doVehicle.NoOfBags = binding.VehiclesBags.getSelectedItemPosition()+1;
        doVehicle.Number = binding.vehicleNumber.getText().toString();
        doVehicle.OwningLocation = getId(binding.vehicleOwnLoc.getSelectedItem().toString());
        doVehicle.Status = binding.VehiclesStatus.getSelectedItemPosition()+1;
        Double value = Double.valueOf(binding.tanksize.getText().toString());
        String amt2 = String.format(Locale.US,"%.0f",value);
        doVehicle.TankSize = Integer.valueOf(amt2);
        doVehicle.Transmission = binding.vehicleTransmission.getSelectedItemPosition();
        doVehicle.VehicleCategoryId = getId(binding.VehiclesFleet.getSelectedItem().toString());
        doVehicle.VehicleMakeId = getId(binding.makeId.getSelectedItem().toString());
        doVehicle.VehicleModelId = getModelId(binding.vehicleModel.getSelectedItem().toString());
        doVehicle.VehicleTypeId = getId(binding.VehiclesClass.getSelectedItem().toString());
        doVehicle.VinNumber = binding.vinNumber.getText().toString();
        doVehicle.Year = Integer.valueOf(binding.year.getSelectedItem().toString());
        vehicleOtherDetailsModel.Description = binding.vehDescription.getText().toString();
        doVehicle.VehicleOtherDetailsModel=vehicleOtherDetailsModel;
        return doVehicle;
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp + 0 +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private  File getOutputMediaFile(int i){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp + i +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private void storeImage(List<Bitmap> image) {
        Log.e(TAG, "storeImage: " + image.size() );
        List<File> pictureFiles = new ArrayList<>();
        for (int i = 0; i <image.size(); i++) {
        File pictureFile = getOutputMediaFile(i);
           // pictureFile.add(getOutputMediaFile(i));
        if (pictureFile == null) {
            Log.e(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.get(i).compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                pictureFiles.add(pictureFile);
                Log.e(TAG, "storeImage: " + i );
                if ( i == image.size()-1){
                    Log.e(TAG, "storeImage: " + image.size() );
                    imgUpload(pictureFiles);
                }
               // imgUpload(pictureFile);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "Error accessing file: " + e.getMessage());
            }
        }
        //imgUpload(pictureFile);
      /*  try {
            //FileOutputStream fos = new FileOutputStream(pictureFile);
            //image.get(i).compress(Bitmap.CompressFormat.PNG, 90, fos);
           // fos.close();
            imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }*/
    }

    public void imgUpload(File file){
        HashMap<String, String> header = new HashMap<>();
        //header.put("FileUploadMasterId", String.valueOf(getArguments().getInt("iddd")));
        header.put("Id", String.valueOf(iddd));
        header.put("IsActive","true");
        header.put("CompanyId", String.valueOf(Helper.id));
        header.put("fileUploadType", "8");
        Log.e(TAG, "imgUpload: " + header);
        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, header, file);
    }

    public void imgUpload(List<File> file){
       // for (int i = 0; i <file.size() ; i++) {


        HashMap<String, String> header = new HashMap<>();
        //header.put("FileUploadMasterId", String.valueOf(getArguments().getInt("iddd")));
        header.put("Id", String.valueOf(iddd));
        header.put("IsActive","true");
        header.put("CompanyId", String.valueOf(Helper.id));
        header.put("fileUploadType", "8");
        Log.e(TAG, "imgUpload: " + header);
        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, header, file);
       // }
    }


    OnResponseListener uploadImage = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.e(TAG, "onSuccess: " + response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        Log.e(TAG, "run: " + response );
                        if (status)
                        {

                            NavHostFragment.findNavController(Fragment_Vehicles_Edit_Basic_Details.this)
                                    .popBackStack();


                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "onError: " + error);
        }
    };

    private void imageClick(ImageView imageView, int i){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, i);*/
                optionMenu.optionVisible(binding.sucessfullRegi, true);

                binding.option.gallaryopen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: " + i );
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, i);

                        optionMenu.optionVisible(binding.sucessfullRegi, false);
                    }
                });

                binding.option.cameraopen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: " + i );
                        selectimg = i;
                        Intent intent = new Intent();
                        activityResultLauncher.launch(getIntent(intent,true));

                        optionMenu.optionVisible(binding.sucessfullRegi, false);
                    }
                });
            }
        });
    }

    private void imgDelet(ImageView imageView, int i, AttachmentsModel attachmentsModel){
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + attachmentsModel.AttachmentPath );
                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    try {
                                        JSONObject responseJSON = new JSONObject(response);
                                        Boolean status = responseJSON.getBoolean("Status");
                                        if (status)
                                        {
                                            CustomToast.showToast(getActivity(),responseJSON.getString("Message"),1);
                                            //binding.carImage1.
                                            if (i==1){
                                                Glide.with(binding.carImage1).clear(binding.carImage1);
                                            } else if (i==2){
                                                Glide.with(binding.carImage2).clear(binding.carImage2);
                                            }else if (i==3){
                                                Glide.with(binding.carImage3).clear(binding.carImage3);
                                            }else if (i==4){
                                                Glide.with(binding.carImage4).clear(binding.carImage4);
                                            }else if (i==5){
                                                Glide.with(binding.carImage5).clear(binding.carImage5);
                                            }else if (i==6){
                                                Glide.with(binding.carImage6).clear(binding.carImage6);
                                            }else if (i==7){
                                                Glide.with(binding.carImage7).clear(binding.carImage7);
                                            }else if (i==8){
                                                Glide.with(binding.carImage8).clear(binding.carImage8);
                                            }else if (i==9){
                                                Glide.with(binding.carImage9).clear(binding.carImage9);
                                            }else if (i==10){
                                                Glide.with(binding.carImage10).clear(binding.carImage10);
                                            }

                                        } else {
                                            CustomToast.showToast(getActivity(),responseJSON.getString("Message"),0);
                                        }

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.DELETE, IMAGEDELETE, BASE_URL_LOGIN, header, "?Id=" + attachmentsModel.Id);
            }
        });
    }

    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public Intent getIntent(Intent intents, boolean image){
        if(image){
            intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        } else {
            //Intent i = new Intent();
            intents = new Intent();
            intents.setType("image/*");
            intents.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        }
        return intents;
    }
}
