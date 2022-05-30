package com.rentguruz.app.home.vehicles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.rentguruz.app.R;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

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
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

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

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWN;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.INSERTVEHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.MAKEVEHICLE;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADIMAGE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLECLASS;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEDETAIL;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEENGINE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFEATURE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEFLEET;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEMAKE;

public class Fragment_Vehicles_Basic_Details extends BaseFragment {

    FragmentVehicleBasicDetailsBinding binding;
    List<DropDown> dropDownList = new ArrayList<>();
    Handler handler=new Handler(Looper.getMainLooper());
    CustomeView customeView;
    List<OnDropDownList> data = new ArrayList<>();
    List<OnDropDownList> model = new ArrayList<>();
    int idd;
    DoVehicle doVehicle;
    List<VehicleOptionMappingModel> optionMappingModels = new ArrayList<>();
    VehicleOtherDetailsModel vehicleOtherDetailsModel;
    private static int RESULT_LOAD_IMAGE = 1;
    public static List<Bitmap> carimage = new ArrayList<>();
    public static int iddd = 0;
    private static final Boolean isDefault = false;
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
        optionMenu = new ImageOptionMenu(getActivity());
        binding.setUiColor(UiColor);
        optionMenu.optionmenulist(binding.sucessfullRegi, view);
        binding.header.screenHeader.setText(companyLabel.Vehicle + " Basic Details");
        binding.header.discard.setVisibility(View.GONE);
        doVehicle = new DoVehicle();
        binding.vehDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //binding.vehDescription.setInputType(EditorInfo.IME_ACTION_DONE);
        vehicleOtherDetailsModel= new VehicleOtherDetailsModel();
        dropDownList.add(new DropDown(MAKEVEHICLE,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        dropDownList.add(new DropDown(VEHICLECLASS,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        dropDownList.add(new DropDown(VEHICLELOCATION,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        dropDownList.add(new DropDown(VEHICLEENGINE,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        dropDownList.add(new DropDown(VEHICLEFEATURE,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        dropDownList.add(new DropDown(VEHICLEFLEET,Integer.parseInt(loginRes.getData("CompanyId")),true,isDefault));
        customeView = new CustomeView();

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
        binding.searchVehicle.setOnClickListener(this);
        binding.lblNextToRentalRate.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        otherSpinner();
        customeView.vehicleTypeSpinner(getContext(),binding.VehiclesStatus);
        customeView.vehicleTransmissionSpinner(getContext(),binding.vehicleTransmission);
        customeView.vehicleFuel(getContext(),binding.VehiclesFuel);

        binding.year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: "+ binding.year.getSelectedItem() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* binding.carImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });*/

       /* Drawable[] layers = new Drawable[2];

        ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
        sd1.getPaint().setColor(Color.parseColor(UiColor.secondary));
        sd1.getPaint().setStyle(Paint.Style.STROKE);
        sd1.getPaint().setStrokeWidth(1);

        ShapeDrawable sd2 = new ShapeDrawable(new RectShape());
        sd2.getPaint().setColor(0xFF000000);
        sd2.getPaint().setStyle(Paint.Style.STROKE);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_main_camera);
        drawable.setTint(Color.parseColor(UiColor.primary));
       // Log.e(TAG, "onViewCreated:1 " + drawable.getBounds() );
        //drawable.setBounds(0,0, 0,0);
       // Log.e(TAG, "onViewCreated:2 " + drawable.getBounds() );
        drawable.setBounds(0,0,40,40);

        Drawable drawable1 = getResources().getDrawable(R.drawable.testing);
        //drawable1.setTint(Color.parseColor(UiColor.secondary));
        //drawable1.setBounds(40,40,40,40);

        //layers[0] =  new ScaleDrawable(drawable.getCurrent(), Gravity.CENTER,1,1).getDrawable();
        layers[0] = drawable;
        layers[0].setBounds(0,200,0,0);
        layers[1] = drawable1;

        LayerDrawable composite = new LayerDrawable(layers);*/


/*        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,7)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);


        //Stroke color and width
        //shapeDrawable = (MaterialShapeDrawable) getResources().getDrawable(R.drawable.ic_main_camera);
        //shapeDrawable.setFillColor(ContextCompat.getColorStateList(this,R.color.yourColor));
        //shapeDrawable.setTint(Color.parseColor(UiColor.primary));*/



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
      /*  binding.view1.getBackground().setColor(Color.parseColor(UiColor.primary));
        binding.view1.getBackground().setC*/

        for (int i = 1; i <10 ; i++) {
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


    }


    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblNextToRentalRate:
                if (validation()) {
                    new ApiService2<DoVehicle>(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(() -> {
                                try {
                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    Log.e(TAG, "run: " + responseJSON);
                                    if (status) {
                                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                                        Log.e(TAG, "onSuccess: "+  resultSet );
                                       iddd =  resultSet.getInt("Id");
                                        Log.e(TAG, "onSuccess: " + iddd );
                                        NavHostFragment.findNavController(Fragment_Vehicles_Basic_Details.this)
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
                    }, RequestType.POST, INSERTVEHICLE, BASE_URL_LOGIN, header, getDoVehicle());
                }
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_Vehicles_Basic_Details.this).popBackStack();
                break;

            case R.id.searchVehicle:
                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            if (status){

                            } else {
                                CustomToast.showToast(getActivity(),responseJSON.getString("Message"),1);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                }, RequestType.GET, VEHICLEDETAIL, BASE_URL_LOGIN, header, "?vinNumber=" + binding.vinNumber.getText().toString());
                break;
        }
    }

    public void getSpinner(Spinner spinner, List<String> data){
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,data);
        spinner.setAdapter(adapterCategories);
    }

    public void listSpinner(List<OnDropDownList> data){
        List<String> makeVehicle = new ArrayList<>();
        List<String> vehicleClass = new ArrayList<>();
        List<String> vehicleEngine = new ArrayList<>();
        List<OnDropDownList> vehicleFeature = new ArrayList<>();
        List<String> location = new ArrayList<>();
        location.add(0,"Parked Location");

        List<String> location2 = new ArrayList<>();
        location2.add(0,"Owning Location");
        List<String> vehicleFleet = new ArrayList<>();

        for (int i = 0; i <data.size() ; i++) {
            if (data.get(i).TableType == MAKEVEHICLE){
                makeVehicle.add(data.get(i).Name);
            }
            if (data.get(i).TableType == VEHICLECLASS){
                vehicleClass.add(data.get(i).Name);
            }
            if (data.get(i).TableType == VEHICLEENGINE){
                vehicleEngine.add(data.get(i).Name);
            }
            if (data.get(i).TableType == VEHICLEFEATURE){
                vehicleFeature.add(data.get(i));
            }
            if (data.get(i).TableType == VEHICLELOCATION){
                location.add(data.get(i).Name);
                location2.add(data.get(i).Name);
            }
            if (data.get(i).TableType == VEHICLEFLEET){
                vehicleFleet.add(data.get(i).Name);
            }


        }
        makeVehicle.add(0, "Select Company");
        ArrayAdapter<String> adaptermakeVehicle = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,makeVehicle);
        binding.makeId.setAdapter(adaptermakeVehicle);

        vehicleClass.add(0,"Select " + companyLabel.Vehicle + " Class" );
        ArrayAdapter<String> adaptervehicleClass = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleClass);
        binding.VehiclesClass.setAdapter(adaptervehicleClass);

        vehicleEngine.add(0,"Select " + companyLabel.Vehicle + " Engine" );
        ArrayAdapter<String> adaptervehicleEngine = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleEngine);
        binding.VehiclesEngine.setAdapter(adaptervehicleEngine);

        getCheckBox2(vehicleFeature);

        ArrayAdapter<String> adaptervehicleParked = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,location);
        binding.vehicleParked.setAdapter(adaptervehicleParked);

        ArrayAdapter<String> adaptervehicleOwning = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,location2);
        binding.vehicleOwnLoc.setAdapter(adaptervehicleOwning);

        ArrayAdapter<String> adaptervehicleFleet = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleFleet);
        binding.VehiclesFleet.setAdapter(adaptervehicleFleet);
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

            int finalI = i;
            rowCheckboxBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        doVehicle.VehicleOptionMappingModel.add(new VehicleOptionMappingModel(data.get(finalI).Id, data.get(finalI).Name));
                    } else {
                        removeId(data.get(finalI).Id);
                    }
                }
            });

            binding.relativelayout.addView(rowCheckboxBinding.getRoot());
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
            }

            if (i<6){
                vehicleDoors.add(String.valueOf(i));
                vehicleBags.add(String.valueOf(i));
            }

            vehicleSeat.add(String.valueOf(i));
        }
        years.add(0,"Select Year");
        getSpinner(binding.year, years);
        vehicleSeat.add(0,"Select " + companyLabel.Vehicle  + " Seat");
        getSpinner(binding.VehiclesSeat,vehicleSeat);
        vehicleDoors.add(0,"Select " + companyLabel.Vehicle  + " Doors");
        getSpinner(binding.VehiclesDoors,vehicleDoors);
        vehicleBags.add(0,"Select " + companyLabel.Vehicle  + " Bags");
        getSpinner(binding.VehiclesBags,vehicleBags);
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
                            }
                            strings.add(0,"Select " + companyLabel.Vehicle + " Model");
                            getSpinner(binding.vehicleModel, strings);
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
       /* else if (carimage.size() == 0 )
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
        doVehicle.TankSize = Integer.valueOf(binding.tanksize.getText().toString());
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

    public void removeId(int id){
        for (int i = 0; i <doVehicle.VehicleOptionMappingModel.size(); i++) {
            if (doVehicle.VehicleOptionMappingModel.get(i).VehicleOptionId == id){
                doVehicle.VehicleOptionMappingModel.remove(i);
                break;
            }
        }
    }

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

    private File getOutputMediaFile(){
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
            Log.e("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    private void storeImage(List<Bitmap> image) {
        List<File> pictureFiles = new ArrayList<>();
        for (int i = 0; i <image.size(); i++) {
            File pictureFile = getOutputMediaFile(i);
            // pictureFile.add(getOutputMediaFile(i));
            if (pictureFile == null) {
                Log.e("TAG",
                        "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.get(i).compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
               // imgUpload(pictureFile);
                pictureFiles.add(pictureFile);
                if ( i == image.size()-1){
                    Log.e(TAG, "storeImage: " + image.size() );
                    imgUpload(pictureFiles);
                }
            } catch (FileNotFoundException e) {
                Log.e("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.e("TAG", "Error accessing file: " + e.getMessage());
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

                            NavHostFragment.findNavController(Fragment_Vehicles_Basic_Details.this)
                                    .navigate(R.id.action_insert_vehicles_to_Vehicles);
                                    //.popBackStack();


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
