package com.rentguruz.app.flexiicar.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.ImageOptionMenu;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.DoRegistration;
import com.rentguruz.app.model.DrivingLicenceModel;
import com.rentguruz.app.ScanDrivingLicense;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.databinding.FragmentDriverProfile2Binding;
import com.rentguruz.app.model.parameter.DateType;
import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;


import static com.rentguruz.app.apicall.ApiEndPoint.firstImage;
import static com.rentguruz.app.apicall.ApiEndPoint.secondImage;
import com.rentguruz.app.R;
public class Fragment_Driver_Profile_2 extends BaseFragment
{
   // ImageView backArrow;
   //ImageView DLBacksideImg,DLFronsideImg;
   // LinearLayout lblnext;
   // TextView edt_ExpiryDateDL,Cus_DateofBirth,IssueDate;
   // EditText edt_DriverLicenseNO,DL_IssueBy,StateandProvience;
   //TextView txtDiscard;

    Bundle RegistrationBundle;
    public int RESULT_LOAD_IMAGE = 1;
    JSONArray ImageList = new JSONArray();

    String imgId = "";

    ArrayList<String> scanData;
    public static DrivingLicenceModel drivingLicenceModel;
   // CustomPreference preference;
    CustomeDialog dialog;
    private FragmentDriverProfile2Binding binding;
    Intent intent;
    static int statecode=0, countrycode=0;
    DoRegistration registration;


    ImageCapture imageCapture;
    ProcessCameraProvider  cameraProvider;

    int TAKE_PHOTO_CODE = 11;
    public static int count = 0;
    public static final int RequestPermissionCode = 1;
    ImageOptionMenu optionMenu;

/*    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }*/

    ActivityResultLauncher<Intent> activityResultLauncher;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    public void setInitialSavedState(@org.jetbrains.annotations.Nullable SavedState state) {
       super.setInitialSavedState(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
      //  NavHostFragment.findNavController(Fragment_Driver_Profile_2.this).getCurrentBackStackEntry();
        binding = FragmentDriverProfile2Binding.inflate(inflater, container,false);
        drivingLicenceModel = new DrivingLicenceModel();
        //preference = new CustomPreference(getContext());
        dialog = new CustomeDialog(getContext());
        registration = new DoRegistration();
        registration = Fragment_Create_Profile.registration;
        drivingLicenceModel = Fragment_Create_Profile.registration.DrivingLicenceModel;
        Log.d("Mungara", "onCreateView: " + UserData.UserDetail + " : " + UserData.customerProfile.Age);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        scanData = getActivity().getIntent().getStringArrayListExtra("scanData");
        binding.setUiColor(UiColor);
        EnableRuntimePermission();

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
          //  NavHostFragment.findNavController(Fragment_Driver_Profile_2.this).getCurrentBackStackEntry();
            //click event
            binding.LicenceScanimg.setOnClickListener(this);
            binding.header.discard.setOnClickListener(this);
            binding.edtExpiryDateDL.setOnClickListener(this);
            binding.CusDateofBirth.setOnClickListener(this);
            binding.ISSueDate.setOnClickListener(this);
            binding.DLFronsideImg.setOnClickListener(this);
            binding.DLBacksideImg.setOnClickListener(this);
            binding.lblnextscreen.setOnClickListener(this);
            binding.header.back.setOnClickListener(this);
            binding.option.cameraopen.setOnClickListener(this);
            binding.option.gallaryopen.setOnClickListener(this);


            //getbackscreendata
            registration = (DoRegistration) getArguments().getSerializable("RegistrationBundle");

            preference.stateCountry( binding.SpCountry, binding.SpState, registration.AddressesModel.CountryName, registration.AddressesModel.StateName);
            //2021-10-21
            binding.edtExpiryDateDL.setText(Helper.getDateDisplay(DateType.yyyyMMddD,drivingLicenceModel.ExpiryDate));
            binding.CusDateofBirth.setText(Helper.getDateDisplay(DateType.yyyyMMddD, drivingLicenceModel.DOB));
            binding.ISSueDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,drivingLicenceModel.IssueDate));

            optionMenu = new ImageOptionMenu(getActivity());

            cameraProviderFuture  = ProcessCameraProvider.getInstance(getContext());

/*
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                getActivity().startActivityForResult(takePictureIntent, 123);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }*/

            activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Bundle buundal = result.getData().getExtras();
                                Bitmap bitmap = (Bitmap) buundal.get("data");
                               if (imgId.equals("2")) {
                                    binding.DLFronsideImg.setImageBitmap(bitmap);
                                }
                                if (imgId.equals("3")) {
                                    binding.DLBacksideImg.setImageBitmap(bitmap);
                                }
                            }
                        }
                    }
            );

        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage() );
        }

        if(scanData != null)
        {
            for (String data : scanData)
            {
                String[] datas = data.split(":");
                if (datas[0].equals("Document Number"))
                binding.edtDriverLicenseNO.setText(datas[1].toString());
                binding.edtExpiryDateDL.setText(preference.getdata("Expiration Date"));
                binding.CusDateofBirth.setText(preference.getdata("Birth Date"));
                binding.ISSueDate.setText(preference.getdata("Issue Date"));
             //   StateandProvience.setText(preference.getdata("Issuing State Name"));

                //if (datas[0].equals(""))
            }
            try {
                binding.DLBacksideImg.setImageBitmap(secondImage);
                binding.DLFronsideImg.setImageBitmap(firstImage);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        try {

            optionMenu.optionmenulist(binding.sucessfullRegi, view);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;

          /*      try {
                    imgId = Helper.imagee;
                } catch (Exception e){
                    e.printStackTrace();
                }*/


                try {
                    bitmap = getScaledBitmap(selectedImage,250,250);
                    if (imgId.equals("2"))
                    {
                        JSONObject docObj = new JSONObject();
                        docObj.put("Doc_For", 6);
                        docObj.put("VhlPictureSide", 2);
                        docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                        ImageList.put(docObj);
                        binding.DLFronsideImg.setImageBitmap(bitmap);
                        firstImage = bitmap;
                    }
                    if (imgId.equals("3"))
                    {
                        JSONObject docObj = new JSONObject();
                        docObj.put("Doc_For", 6);
                        docObj.put("VhlPictureSide", 3);
                        docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                        ImageList.put(docObj);
                        binding.DLBacksideImg.setImageBitmap(bitmap);
                        secondImage = bitmap;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK ){
                Log.e(TAG, "onActivityResult: " + data.getExtras().get("data"));
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                binding.DLFronsideImg.setImageBitmap(bitmap);
                firstImage = bitmap;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getImagePathFromUri(Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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

    @Override
    public void onClick(View v) {
        try {
            switch(v.getId()){
               case  R.id.LicenceScanimg:
                   intent = new Intent(getActivity(), ScanDrivingLicense.class);
                   intent.putExtra("afterScanBackTo", 4);
                   startActivity(intent);
                   break;

                case R.id.discard:
                    NavHostFragment.findNavController(Fragment_Driver_Profile_2.this).navigate(R.id.action_DriverProfile2_to_CreateProfile);
                    break;

                case R.id.Cus_DateofBirth:
                    dialog.getMaxDate(dialog.getDOB(),"" ,string -> binding.CusDateofBirth.setText(string));
                    break;

                case R.id.ISSueDate:
                    dialog.getMinDate(dialog.getIssueDate(binding.CusDateofBirth.getText().toString()),dialog.getToday(),string -> binding.ISSueDate.setText(string));
                    break;

                case R.id.edt_ExpiryDateDL:
                    dialog.getFullDate(dialog.getToday(), "", string -> binding.edtExpiryDateDL.setText(string));
                    break;

                case R.id.DLBacksideImg:
                    imgId = "3";
                  /*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getActivity().getPackageManager())!= null){
                        activityResultLauncher.launch(cameraIntent);
                    }*/


                 //   activityResultLauncher.launch(getIntent(intent,false));
                    optionMenu.optionVisible(binding.sucessfullRegi,true);

                    /*
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);*/

/*

                    File photoDir = new File(Environment.getExternalStorageDirectory()
                            + "/Android/data/"
                            + getContext().getPackageName()
                            + "/Files");

                    if (!photoDir.exists())
                        photoDir.mkdir();

                    final String dir =photoDir.getPath().toString();
                    File newdir = new File(dir);
                    newdir.mkdirs();

                    count++;
                    String file = dir+count+".jpg";
                    File newfile = new File(file);

                    try {
                        newfile.createNewFile();
                    }
                    catch (IOException e)
                    {
                    }
                    Uri outputFileUri = Uri.fromFile(newfile);
*/

       /*             File photoDir = new File(Environment.getExternalStorageDirectory()
                            + "/Android/data/"
                            + getContext().getPackageName()
                            + "/Files");

                    if (!photoDir.exists())
                        photoDir.mkdir();

                    Date date = new Date();
                    String  timetemp = String.valueOf(date.getTime());

                    String photofilepath = photoDir.getAbsolutePath() + "/" + timetemp +".jpg";


                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
              //      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
               //             Uri.fromFile(new File(photoDir.getAbsolutePath(),timetemp+".jpg")));
                           // MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
                    getActivity().startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);*/

                   // capturePhoto();
                    break;

                case R.id.DLFronsideImg:


                    imgId = "2";
                    optionMenu.optionVisible(binding.sucessfullRegi,true);
                   /* intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);*/



                   /* binding.camera.setVisibility(View.VISIBLE);

                    cameraProviderFuture.addListener(() -> {
                        try {
                            cameraProvider = cameraProviderFuture.get();
                            startcamera(cameraProvider);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, getExecutor());*/

                    break;

                case R.id.cameraopen:
                    activityResultLauncher.launch(getIntent(intent,true));
                    optionMenu.optionVisible(binding.sucessfullRegi,false);
                    break;

                case R.id.gallaryopen:
                    //activityResultLauncher.launch(getIntent(intent,false));
                    startActivityForResult(getIntent(intent,false), RESULT_LOAD_IMAGE);
                    optionMenu.optionVisible(binding.sucessfullRegi,false);
                    break;

                case R.id.back:
                 /*   Bundle Registration = new Bundle();
                    registration.DrivingLicenceModel = drivingLicenceModel;
                    Registration.putSerializable("RegistrationBundle", registration);
                    System.out.println(Registration);
                    NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                            .navigate(R.id.action_DriverProfile2_to_DriverProfile,Registration);*/
                    NavHostFragment.findNavController(Fragment_Driver_Profile_2.this).popBackStack();
                    break;

                case R.id.lblnextscreen:
                    if (validation()){
                        registration.DrivingLicenceModel = getData();
                        Bundle Registrations=new Bundle();
                        // Registration.putBundle("RegistrationBundle",RegistrationBundle);
                        Registrations.putSerializable("RegistrationBundle", registration);
                        Fragment_Create_Profile.registration = registration;
                        NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                                .navigate(R.id.action_DriverProfile2_to_DriverProfile3,Registrations);
                    }
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean validation(){
        boolean value = false;
        if (binding.edtDriverLicenseNO.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Driving License NO.",1);
        else if (binding.CusDateofBirth.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Date Of Birth",1);
        else if (binding.ISSueDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter License Issue Date",1);
        else if (binding.edtExpiryDateDL.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Your Driving License Expiry Date",1);
        else {
            value = true;
        }
        return value;
    }

    public DrivingLicenceModel getData(){
        try {
            drivingLicenceModel.LicenceFor = 0;
            drivingLicenceModel.LicenceType = 3;
            drivingLicenceModel.Category = "";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");


            drivingLicenceModel.Number = binding.edtDriverLicenseNO.getText().toString();
           // drivingLicenceModel.ExpiryDate = dialog.dateConvert(binding.edtExpiryDateDL.getText().toString());
            drivingLicenceModel.ExpiryDate = Helper.setPostDate(binding.edtExpiryDateDL.getText().toString());
            drivingLicenceModel.DOB =Helper.setPostDate(binding.CusDateofBirth.getText().toString());
            drivingLicenceModel.IssueDate =Helper.setPostDate(binding.ISSueDate.getText().toString());
            drivingLicenceModel.IssuedByStateName = binding.SpState.getSelectedItem().toString();
            drivingLicenceModel.IssuedByState = preference.stateToSateCode.get(binding.SpState.getSelectedItem());
            drivingLicenceModel.IssueByCountryName = binding.SpCountry.getSelectedItem().toString();
            drivingLicenceModel.IssueByCountry = preference.countryToCountryCode.get(binding.SpCountry.getSelectedItem());

/*            drivingLicenceModel.IssuedByState = Fragment_Driver_Profile_1.addressesModel.StateId;
            drivingLicenceModel.IssueByCountry = Fragment_Driver_Profile_1.addressesModel.CountryId;
            drivingLicenceModel.IssueByCountryName = Fragment_Driver_Profile_1.addressesModel.CountryName;
            drivingLicenceModel.IssuedByStateName = Fragment_Driver_Profile_1.addressesModel.StateName;*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drivingLicenceModel;
    }

    private void startcamera(ProcessCameraProvider cameraProvider){
            cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(binding.camera.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();


        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector,preview,imageCapture);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                capturePhoto();
            }
        },3000);
    }

    private Executor getExecutor(){
        return  ContextCompat.getMainExecutor(getContext());
    }

    private void capturePhoto(){
        File photoDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");

            if (!photoDir.exists())
                photoDir.mkdir();

        Date date = new Date();
        String  timetemp = String.valueOf(date.getTime());

        String photofilepath = photoDir.getAbsolutePath() + "/" + timetemp +".jpg";

        File photofile = new File(photofilepath);

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photofile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull @NotNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d(TAG, "onImageSaved: " + outputFileResults.getSavedUri());
                        //getImagePathFromUri(outputFileResults.getSavedUri());
                        Uri selectedImage = outputFileResults.getSavedUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = getScaledBitmap(selectedImage,250,250);
                            if (imgId.equals("2"))
                            {
                                JSONObject docObj = new JSONObject();
                                docObj.put("Doc_For", 6);
                                docObj.put("VhlPictureSide", 2);
                                docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                                ImageList.put(docObj);
                                binding.DLFronsideImg.setImageBitmap(bitmap);
                                firstImage = bitmap;
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        binding.DLFronsideImg.setImageBitmap(bitmap);
                        binding.camera.setVisibility(View.GONE);
                        cameraProvider.unbindAll();
                    }

                    @Override
                    public void onError(@NonNull @NotNull ImageCaptureException exception) {
                        Log.d(TAG, "onError: " + exception.toString());
                    }
                }
        );

    }


    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            //Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA},RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                  //  Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                   // Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    
    public Intent getIntent(Intent intents, boolean image){
        if(image){
            intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        } else {
            intents = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            //startActivityForResult(intent,RESULT_LOAD_IMAGE);
        }
        return intents;
    }
}
