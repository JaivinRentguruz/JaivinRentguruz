package com.rentguruz.app.flexiicar.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.rentguruz.app.R;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.ImageOptionMenu;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rentguruz.app.ScanDrivingLicense;
import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentDrivingLicenseAddUpdateBinding;
import com.rentguruz.app.model.DrivingLicenceModel;
import com.rentguruz.app.model.parameter.AttachmentType;
import com.rentguruz.app.model.response.UpdateDL;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETSINGLELICENCE;
import static com.rentguruz.app.apicall.ApiEndPoint.UPDATEDEFAULTDRIVER;
import static com.rentguruz.app.apicall.ApiEndPoint.UPDATELICENCE;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADIMAGE;

public class Fragment_Driving_License_Update extends BaseFragment
{
    public String id="";
    private static final int RESULT_LOAD_IMAGE = 1;
    String imagestr;
    String imgId = "";
    ImageLoader imageLoader;
    String serverpath="";
    JSONArray ImageList = new JSONArray();
    JSONObject frontImgObj = new JSONObject();
    JSONObject backImgObj = new JSONObject();
    int ids, fors;
    UpdateDL updateDL;
    public String[] Country,State;
    public int[] CountryId,StateId;
    FragmentDrivingLicenseAddUpdateBinding binding;
    CustomeDialog dialog;
    Intent intent;
    List<String> relation;
    Bitmap Image1, Image2;
    ImageOptionMenu optionMenu;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView img1, img2,img3,img4;
    String storeimagepath;
    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        relation = new ArrayList<>();
        updateDL = new UpdateDL();
        dialog = new CustomeDialog(getContext());
        binding = FragmentDrivingLicenseAddUpdateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        img1 = view.findViewById(R.id.img_DLFronside);
        img2 = view.findViewById(R.id.img_DLBackside);

        img3 = view.findViewById(R.id.img_DLFronside);
        img4 = view.findViewById(R.id.img_DLBackside);

        optionMenu = new ImageOptionMenu(getActivity());
        if (getArguments().getInt("frag")!=2){
            //((User_Profile)getActivity()).BottomnavInVisible();
        }
        try {
           // if (Helper.isActiveCustomer){
                ((User_Profile)getActivity()).BottomnavInVisible();
            //}
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (getArguments().getBoolean("defaultLic")){
                binding.edtRelationship.setVisibility(View.GONE);
                binding.relaion.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        binding.test.setBackground(userDraw.getImageUpload());
        binding.test2.setBackground(userDraw.getImageUpload());
        binding.header.screenHeader.setText(getResources().getString(R.string.driver) + " " + companyLabel.License);
        binding.subheader.setText(getResources().getString(R.string.driver) + " " + companyLabel.License);
        binding.header.discard.setText(getResources().getString(R.string.save));

        try {

            relation.add("Parent");
            relation.add("Child");
            relation.add("Spouse");
            relation.add("Sibling");
            relation.add("GrandParents");
            relation.add("GrandChild");
            relation.add("Friend");
            relation.add("Other");

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();
            fullProgressbar.show();
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            id = sp.getString(getString(R.string.id), "");
            try {
                bundle = getArguments().getBundle("LicenseBundle");
            } catch (Exception e){
                e.printStackTrace();
            }

            updateDL = (UpdateDL) getArguments().getSerializable("LicenseBundle");
            ids = updateDL.Id;
            fors = updateDL.DrivingLicenceModel.LicenceFor;
            Log.d(TAG, "onViewCreated: " + ids + ":" + fors);



            if (getArguments().getBoolean("defaultLic")) {
                //updateDL = loginRes.getModel(data.toString(), UpdateDL.class);
                binding.setDriver(updateDL);
                preference.stateCountry(binding.SpCountry, binding.SpState, updateDL.DrivingLicenceModel.IssueByCountryName, updateDL.DrivingLicenceModel.IssuedByStateName);
                binding.edtExDate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.ExpiryDate));
                binding.edtIssuedate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.IssueDate));
                binding.CusDateofBirth.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.DOB));
                fullProgressbar.hide();
                if (updateDL.DrivingLicenceModel.DrivingLicenseFront.AttachmentPath.length() != 0) {
                    CustomBindingAdapter.loadImage(binding.imgDLFronside, updateDL.DrivingLicenceModel.DrivingLicenseFront.AttachmentPath);
                }
                if (updateDL.DrivingLicenceModel.DrivingLicenseBack.AttachmentPath.length() != 0) {
                    CustomBindingAdapter.loadImage(binding.imgDLBackside, updateDL.DrivingLicenceModel.DrivingLicenseBack.AttachmentPath);
                }

            } else {

                JSONObject obj = new JSONObject();
                apiService = new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        Log.d(TAG, "onSuccess: " + response);
                        handler.post(() -> {
                            try {
                                System.out.println("Success");
                                System.out.println(response);
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                fullProgressbar.hide();
                                if (status)
                                {
                                    JSONObject data = responseJSON.getJSONObject("Data");
                                    updateDL = loginRes.getModel(data.toString(), UpdateDL.class);
                                    binding.setDriver(updateDL);
                                    preference.stateCountry(binding.SpCountry,binding.SpState, updateDL.DrivingLicenceModel.IssueByCountryName,updateDL.DrivingLicenceModel.IssuedByStateName);
                                    binding.edtExDate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.ExpiryDate));
                                    binding.edtIssuedate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.IssueDate));
                                    binding.CusDateofBirth.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.DOB));
                                    int list = 0;
                                    for (int i = 0; i <relation.size() ; i++) {
                                        if (relation.get(i).matches(updateDL.RelationDesc)){
                                            list = i;
                                        }
                                    }

                                    ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,relation);
                                    binding.edtRelationship.setAdapter(adapterCategories);
                                    binding.edtRelationship.setSelection(list);
                                    //Glide.with(getContext()).load(updateDL.DrivingLicenceModel.DrivingLicenseFront.AttachmentPath).into(binding.imgDLFronside);
                                    //Glide.with(getContext()).load(updateDL.DrivingLicenceModel.DrivingLicenseBack.AttachmentPath).into(binding.imgDLBackside);

                                    CustomBindingAdapter.loadImage(binding.imgDLFronside,updateDL.DrivingLicenceModel.DrivingLicenseFront.AttachmentPath);
                                    CustomBindingAdapter.loadImage(binding.imgDLBackside,updateDL.DrivingLicenceModel.DrivingLicenseBack.AttachmentPath);
                                }
                                else
                                {
                                    String msg = responseJSON.getString("Message");
                                    CustomToast.showToast(getActivity(),msg,1);
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        });

                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, "onError: " + error);
                    }
                }, RequestType.GET, GETSINGLELICENCE + "?Id=" + ids, BASE_URL_LOGIN, header, obj);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            bundle.putSerializable("timemodel",getArguments().getSerializable("timemodel"));
            bundle.putSerializable("pickuploc", getArguments().getSerializable("pickuploc"));
            bundle.putSerializable("droploc", getArguments().getSerializable("droploc"));
            bundle.putSerializable("Model",getArguments().getSerializable("Model"));
            bundle.putSerializable("ratemaster", getArguments().getSerializable("ratemaster"));
            bundle.putSerializable("vechicle", getArguments().getSerializable("vechicle"));
            bundle.putString("netrate",getArguments().getString("netrate"));
            //bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("defaultcard",getArguments().getSerializable("defaultcard"));
            bundle.putInt("frag",2);
            bundle.putSerializable("summarry",getArguments().getSerializable("summarry") );
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("charges",getArguments().getSerializable("charges"));
            bundle.putSerializable("model",getArguments().getSerializable("model"));
            bundle.putSerializable("models", getArguments().getSerializable("models"));
            bundle.putString("DeliveryAndPickupModel", getArguments().getString("DeliveryAndPickupModel"));
            bundle.putSerializable("reservationSum",getArguments().getSerializable("reservationSum"));
            bundle.putString("insuranceOption",getArguments().getString("insuranceOption"));
            Log.d(TAG, "onViewCreated: " + TAG);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }

        try {
            binding.option.cameraopen.setOnClickListener(this);
            binding.option.gallaryopen.setOnClickListener(this);
            optionMenu.optionmenulist(binding.sucessfullRegi, view);

            activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Bundle buundal = result.getData().getExtras();
                                Bitmap bitmap = (Bitmap) buundal.get("data");
                                if (imgId.equals("2")) {
                                    Glide.with(binding.imgDLFronside).clear(binding.imgDLFronside);
                                    binding.imgDLFronside.setImageBitmap(bitmap);
                                    Image1 = bitmap;
                                   // String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
                                    OptionMenu menu = new OptionMenu(getActivity());
                                    menu.imagedtails(binding.imgDLFronside,bitmap);

                                   // menu.uploadImage(binding.imgDLFronside, bitmap);
                                }
                                if (imgId.equals("3")) {
                                    Glide.with(binding.imgDLBackside).clear(binding.imgDLBackside);
                                    binding.imgDLBackside.setImageBitmap(bitmap);
                                    Image2 = bitmap;
                                }
                            }
                        }
                    }
            );
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.imgDLBackside.setOnClickListener(this);
        binding.imgDLFronside.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.imgScanDrivingLicense.setOnClickListener(this);
        binding.edtIssuedate.setOnClickListener(this);
        binding.edtExDate.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.CusDateofBirth.setOnClickListener(this);

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }
    OnResponseListener UpdateDrivingLicese = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,0);
                        /*NavHostFragment.findNavController(Fragment_Driving_License_Update.this)
                                .navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);*/
                        NavHostFragment.findNavController(Fragment_Driving_License_Update.this).popBackStack();

                        try {
                            storeImage(Image1, true);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            storeImage(Image2, false);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
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
            });
        }

        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    OnResponseListener getLicence = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d(TAG, "onSuccess: " + response);
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    fullProgressbar.hide();
                    if (status)
                    {
                        JSONObject data = responseJSON.getJSONObject("Data");
                        updateDL = loginRes.getModel(data.toString(), UpdateDL.class);
                        binding.setDriver(updateDL);
                        preference.stateCountry(binding.SpCountry,binding.SpState, updateDL.DrivingLicenceModel.IssueByCountryName,updateDL.DrivingLicenceModel.IssuedByStateName);
                        binding.edtExDate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.ExpiryDate));
                        binding.edtIssuedate.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.IssueDate));
                        binding.CusDateofBirth.setText(dialog.dateFullFormattt(updateDL.DrivingLicenceModel.DOB));
                        int list = 0;
                        for (int i = 0; i <relation.size() ; i++) {
                            if (relation.get(i).matches(updateDL.RelationDesc)){
                                list = i;
                            }
                        }

                        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,relation);
                        binding.edtRelationship.setAdapter(adapterCategories);
                        binding.edtRelationship.setSelection(list);

                        CustomBindingAdapter.loadImage(binding.imgDLFronside,updateDL.DrivingLicenceModel.DrivingLicenseFront.AttachmentPath);
                        CustomBindingAdapter.loadImage(binding.imgDLBackside,updateDL.DrivingLicenceModel.DrivingLicenseBack.AttachmentPath);
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            });

        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };


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

    //method to convert the selected image to base64 encoded string
    public static String ConvertBitmapToString(Bitmap bitmap)
    {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return encodedImage;
    }





    public Boolean getUserValidation()
    {
        Boolean value = false;
        if (binding.edtDriverName.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Name",1);
        else if (binding.edtLicenseNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter License No",1);
        else if (binding.edtExDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Expiry Date",1);
        else if (binding.edtIssuedate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Issue Date",1);
        else if (binding.edtDriverEmail.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Email",1);
        else if (binding.edtDriverTelephone.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Telephone No.",1);
        else {
            value = true;
            updateDL = binding.getDriver();
            updateDL.DrivingLicenceModel.IssueByCountryName = binding.SpCountry.getSelectedItem().toString();
            updateDL.DrivingLicenceModel.IssueByCountry = preference.countryToCountryCode.get(binding.SpCountry.getSelectedItem().toString());
            updateDL.DrivingLicenceModel.IssuedByStateName = binding.SpState.getSelectedItem().toString();
            updateDL.DrivingLicenceModel.IssuedByState = preference.stateToSateCode.get(binding.SpState.getSelectedItem().toString());
            updateDL.DrivingLicenceModel.DOB = dialog.dateConvert(binding.CusDateofBirth.getText().toString());
            updateDL.DrivingLicenceModel.ExpiryDate = dialog.dateConvert(binding.edtExDate.getText().toString());
            updateDL.DrivingLicenceModel.IssueDate = dialog.dateConvert(binding.edtIssuedate.getText().toString());
            updateDL.DrivingLicenceModel.Number = binding.edtLicenseNo.getText().toString();
            updateDL.DOB = dialog.dateConvert(binding.CusDateofBirth.getText().toString());
            updateDL.RelationId = binding.edtRelationship.getSelectedItemPosition()+1;
        }
        return value;
    }

    public Intent getIntent(Intent intents, boolean image){
        if(image){
            intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        } else {
            intents = new Intent();
            intents.setType("image/*");
            intents.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        }
        return intents;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.img_DLFronside:
                    imgId = "2";
                   /* intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);*/
                    optionMenu.optionVisible(binding.sucessfullRegi,true);
                    break;
                case R.id.img_DLBackside:
                    imgId = "3";
                   /* intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);*/
                    optionMenu.optionVisible(binding.sucessfullRegi,true);
                    break;
                case R.id.back:
                    // NavHostFragment.findNavController(Fragment_Driving_License_Update.this).navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);
                    NavHostFragment.findNavController(Fragment_Driving_License_Update.this).popBackStack();
                    break;
                case R.id.imgScanDrivingLicense:
                    intent = new Intent(getActivity(), ScanDrivingLicense.class);
                    intent.putExtra("afterScanBackTo", 2);
                    startActivity(intent);
                    break;
                case R.id.Cus_DateofBirth:
                    dialog.getMaxDate(dialog.getDOB(),"" ,string -> binding.CusDateofBirth.setText(string));
                    break;
                case R.id.edt_issuedate:
                    dialog.getMinDate(dialog.getIssueDate(binding.CusDateofBirth.getText().toString()),dialog.getToday(),string -> binding.edtIssuedate.setText(string));
                    break;
                case R.id.edt_exDate:
                    dialog.getFullDate(dialog.getToday(), "", string -> binding.edtExDate.setText(string));
                    break;
                case R.id.discard:
                    if (getUserValidation()){
                        Gson gson = new Gson();
                        String personString = gson.toJson(updateDL);
                        Log.d(TAG, "onClick: " + personString);
                        if (getArguments().getBoolean("defaultLic")){
                            new ApiService2<DrivingLicenceModel>(UpdateDrivingLicese, RequestType.PUT,
                                    UPDATEDEFAULTDRIVER, BASE_URL_CUSTOMER, header, updateDL.DrivingLicenceModel);
                            updateDL.DrivingLicenceModel.ExpiryDate = updateDL.DrivingLicenceModel.ExpiryDate+"T00:00:00";
                        } else {
                            ApiService2<UpdateDL> apiService2 = new ApiService2<UpdateDL>(UpdateDrivingLicese, RequestType.PUT,
                                    UPDATELICENCE, BASE_URL_CUSTOMER,header,updateDL);
                        }
                    }
                    break;

                case R.id.cameraopen:
                    activityResultLauncher.launch(getIntent(intent,true));
                    optionMenu.optionVisible(binding.sucessfullRegi,false);
                    break;

                case R.id.gallaryopen:
                    //activityResultLauncher.launch(getIntent(intent,false));
                    //startActivityForResult(getIntent(intent,false), RESULT_LOAD_IMAGE);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    optionMenu.optionVisible(binding.sucessfullRegi,false);
                    break;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  File getOutputMediaFile(Boolean value){
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
        String mImageName;
        if (value) {
            mImageName  = "MI_" + timeStamp + 1 + ".jpg";
        } else {
            mImageName  = "MI_" + timeStamp + 2 + ".jpg";
        }
        storeimagepath = mImageName;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(Bitmap image, Boolean value) {
        File pictureFile = getOutputMediaFile(value);
        if (pictureFile == null) {
            Log.e("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            imgUpload(pictureFile,value);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public void imgUpload(File file, Boolean value){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("FileUploadMasterId", String.valueOf(updateDL.DrivingLicenceModel.Id));
        Log.e(TAG, "imgUpload: " + updateDL.DrivingLicenceModel.Id );
        headers.put("Id", String.valueOf(updateDL.DrivingLicenceModel.Id));
        // header.put("IsActive","true");
        headers.put("CompanyId", String.valueOf(Helper.id));
        headers.put("exptime",header.exptime);
        headers.put("islogin",header.islogin);
        headers.put("token",header.token);
        headers.put("mut",header.mut);
        headers.put("ut",header.ut);
        if (value) {
            headers.put("fileUploadType", String.valueOf(AttachmentType.DrivingLicenseFront));
        } else {
            headers.put("fileUploadType", String.valueOf(AttachmentType.DrivingLicenseBack));
        }
        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE,headers, file);
    }

    OnResponseListener uploadImage = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d("TAG", "onSuccess: " + response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONArray data = responseJSON.getJSONArray("Data");

                          /*  NavHostFragment.findNavController(Fragment_Driver_Profile_4.this)
                                    .navigate(R.id.action_DriverProfile4_to_Complete_Register,Registration);*/


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
            Log.d("TAG", "onError: " + error);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = null;
            Uri targetUri = data.getData();
            try
            {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                imagestr = ConvertBitmapToString(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            try {
                bitmap = getScaledBitmap(selectedImage,400,400);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                if(imgId.equals("2"))
                {
                    Image1 = bitmap;
                    frontImgObj.put("VhlPictureSide", 2);
                    frontImgObj.put("Doc_For", 6);
                    frontImgObj.put("fileBase64",img_str_base64);
                }
                else if (imgId.equals("3"))
                {
                    Image2 = bitmap;
                    backImgObj.put("VhlPictureSide", 3);
                    backImgObj.put("Doc_For", 6);
                    backImgObj.put("fileBase64", img_str_base64);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(imgId.equals("2"))
            {
               // binding.imgDLFronside.setImageBitmap(bitmap);
                Glide.with(binding.imgDLFronside).clear(binding.imgDLFronside);
                img3.setImageBitmap(bitmap);
            }
            else if (imgId.equals("3"))
            {
                //binding.imgDLBackside.setImageBitmap(bitmap);
                Glide.with(binding.imgDLBackside).clear(binding.imgDLBackside);
                img4.setImageBitmap(bitmap);
            }
        }
    }
}