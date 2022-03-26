package com.abel.app.b2b.flexiicar.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.model.parameter.AttachmentType;
import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.abel.app.b2b.ProcessedData;
import com.abel.app.b2b.R;
import com.abel.app.b2b.ScanDrivingLicense;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentDrivingLicenseAddUpdateBinding;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.model.response.UpdateDL;

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

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSERTLICENCE;
import static com.abel.app.b2b.apicall.ApiEndPoint.UPLOADIMAGE;
import static com.abel.app.b2b.apicall.ApiEndPoint.firstImage;
import static com.abel.app.b2b.apicall.ApiEndPoint.secondImage;

public class Fragment_Driving_License_Add extends BaseFragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    public String id = "";
    public String[] Country, State;
    public int[] CountryId, StateId;
    String imgId = "";
    UpdateDL updateDL;
    LoginResponse loginResponse;
    CustomerProfile customerProfile;
    ArrayList<String> scanData;
    FragmentDrivingLicenseAddUpdateBinding binding;
    JSONObject frontImgObj = new JSONObject();
    JSONObject backImgObj = new JSONObject();
    Intent intent;
    String imagestr;
    CustomeDialog dialog;
    //String[] relation = { "Parent", "Child", "Spouse", "Sibling", "GrandParents","GrandChild","Friend","Other"};
    List<String> relation;
    Bitmap Image1, Image2;
    public static Boolean screen = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        relation = new ArrayList<>();
        updateDL = new UpdateDL();
        loginResponse = new LoginResponse();
        customerProfile = new CustomerProfile();
        dialog = new CustomeDialog(getContext());
        binding = FragmentDrivingLicenseAddUpdateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (getArguments().getInt("frag")!=2){
            //((User_Profile)getActivity()).BottomnavInVisible();
        }
        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
           if (Helper.isActiveCustomer){
                if (getArguments().getInt("frag")!=2){
                    ((User_Profile)getActivity()).BottomnavInVisible();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        customerProfile = UserData.customerProfile;
        binding.screenHeader.setText(getResources().getString(R.string.driver) + " " + companyLabel.License);
        binding.subheader.setText(getResources().getString(R.string.driver) + " " + companyLabel.License);
        //  loginResponse = loginRes.callFriend("Data", LoginResponse.class);
        // UserData.loginResponse = loginResponse;
//        Log.d(TAG, "onViewCreated: " + customerProfile.DOB + " : " + customerProfile.DrivingLicenceModel.DOB);
       /* updateDL.AddressesModel = UserData.loginResponse.LogedInCustomer.AddressesModel;
        updateDL.Email = UserData.loginResponse.LogedInCustomer.Email;
        updateDL.FullName = UserData.loginResponse.LogedInCustomer.FullName;*/

        // updateDL = UserData.updateDL;
        updateDL.AddressesModel = UserData.loginResponse.LogedInCustomer.AddressesModel;
        updateDL.FullName = UserData.loginResponse.LogedInCustomer.FullName;
        updateDL.Email = UserData.loginResponse.LogedInCustomer.Email;
        updateDL.MobileNo = UserData.loginResponse.LogedInCustomer.MobileNo;
        binding.imgDLBackside.setOnClickListener(this);
        binding.imgDLFronside.setOnClickListener(this);
        binding.ImageBackDL.setOnClickListener(this);
        binding.imgScanDrivingLicense.setOnClickListener(this);
        binding.edtIssuedate.setOnClickListener(this);
        binding.edtExDate.setOnClickListener(this);
        binding.txtEditDL.setOnClickListener(this);
        binding.CusDateofBirth.setOnClickListener(this);
        updateDL.AddressesModel = customerProfile.AddressesModel;
        updateDL.MobileNo = UserData.customerProfile.MobileNo;
        updateDL.PhoneNo = UserData.customerProfile.MobileNo;
        /*updateDL.FName = UserData.customerProfile.Fname;
        updateDL.Fname = UserData.customerProfile.Fname;
        updateDL.Lname = UserData.customerProfile.Lname;
        updateDL.LName = UserData.customerProfile.Lname;*/
        binding.setDriver(updateDL);
        //preference.stateCountry(binding.SpCountry, binding.SpState, updateDL.AddressesModel.CountryName, updateDL.AddressesModel.StateName);

        try {
            preference.stateCountry(binding.SpCountry, binding.SpState, updateDL.AddressesModel.CountryName, updateDL.AddressesModel.StateName);
        } catch (Exception e){
            e.printStackTrace();
            preference.stateCountry(binding.SpCountry, binding.SpState, "", "");
        }
        binding.CusDateofBirth.setText(Helper.getDateDisplay(DateType.yyyyMMddD, updateDL.DOB));

        relation.add("Parent");
        relation.add("Child");
        relation.add("Spouse");
        relation.add("Sibling");
        relation.add("GrandParents");
        relation.add("GrandChild");
        relation.add("Friend");
        relation.add("Other");

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,relation);
        binding.edtRelationship.setAdapter(adapterCategories);


        try {
            scanData = getActivity().getIntent().getStringArrayListExtra("scanData");
            if (scanData != null) {
                for (String data : scanData) {

                    String[] datas = data.split(":");

                    if (datas[0].equals("Given Name"))
                        binding.edtDriverName.setText(datas[1]);
                    else if (datas[0].equals("DD Number"))
                        binding.edtLicenseNo.setText(datas[1]);
                    else if (datas[0].equals("Issue Date"))
                        binding.edtIssuedate.setText(userDate(datas[1], "yyyy-MM-dd"));
                    else if (datas[0].equals("Expiration Date"))
                        binding.edtExDate.setText(userDate(datas[1], "yyyy-MM-dd"));
                }
                Log.e(TAG, "onViewCreated: " + binding.edtDriverName.getText() + " : "  +  binding.edtLicenseNo.getText());
                updateDL.FName = binding.edtDriverName.getText().toString();
                updateDL.LName = binding.edtDriverLastname.getText().toString();
                updateDL.DrivingLicenceModel.Number = binding.edtLicenseNo.getText().toString();
                binding.setDriver(updateDL);
                try {
                    int statecode=0, countrycode=0;
                    countrycode = preference.stateToCountryCode.get(preference.getdata("Issuing State Name"));
                    Log.d(TAG, "onViewCreated: " +"State Id : " + statecode + " : " +" Country Id : " + countrycode);
                    preference.stateCountry(binding.SpCountry, binding.SpState, preference.codetoCountry.get(countrycode),preference.getdata("Issuing State Name"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    binding.imgDLBackside.setImageBitmap(secondImage);
                    binding.imgDLFronside.setImageBitmap(firstImage);
                } catch (Exception e){
                    e.printStackTrace();
                }

                screen = true;
                bundle.putInt("frag", Integer.parseInt(preference.getdata("frag")));
            }
        } catch (Exception e) {
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

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public String userDate(String timemills, String dateFormat) {
        timemills = timemills.replace("/Date(", "");
        timemills = timemills.replace(")/", "");
        Log.d("Mungara", "userDate: " + timemills);

        return DateFormat.format(dateFormat, Long.parseLong(timemills)).toString();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.img_DLFronside:
                    imgId = "2";
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    break;
                case R.id.img_DLBackside:
                    imgId = "3";
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    break;
                case R.id.Image_BackDL:
                    // NavHostFragment.findNavController(Fragment_Driving_License_Add.this).navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);
                    //NavHostFragment.findNavController(Fragment_Driving_License_Add.this).popBackStack();
                    if (screen) {
                        NavHostFragment.findNavController(Fragment_Driving_License_Add.this).navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details,bundle);
                    } else {
                        NavHostFragment.findNavController(Fragment_Driving_License_Add.this).popBackStack();
                    }
                    break;
                case R.id.imgScanDrivingLicense:
                    intent = new Intent(getActivity(), ScanDrivingLicense.class);
                    intent.putExtra("afterScanBackTo", 2);
                    startActivity(intent);
                    break;
                case R.id.Cus_DateofBirth:
                    dialog.getMaxDate(dialog.getDOB(), "", string -> binding.CusDateofBirth.setText(string));
                    break;
                case R.id.edt_issuedate:
                    dialog.getMinDate(dialog.getIssueDate(binding.CusDateofBirth.getText().toString()), dialog.getToday(), string -> binding.edtIssuedate.setText(string));
                    break;
                case R.id.edt_exDate:
                    dialog.getFullDate(dialog.getToday(), "", string -> binding.edtExDate.setText(string));
                    break;
                case R.id.txt_EditDL:
                    if (getUserValidation()) {
                        fullProgressbar.show();
                        Gson gson = new Gson();
                        String personString = gson.toJson(updateDL);

                        Log.d(TAG, "onClick: " + personString);
                        ApiService2<UpdateDL> apiService2 = new ApiService2<>(addLicence,
                                RequestType.POST, INSERTLICENCE, BASE_URL_CUSTOMER, header, updateDL);

                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean getUserValidation() {
        Boolean value = false;
        if (binding.edtDriverName.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Your Name", 1);
        else if (binding.edtLicenseNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter License No", 1);
        else if (binding.edtExDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Expiry Date", 1);
        else if (binding.edtIssuedate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Issue Date", 1);
       /* else if (binding.edtRelationship.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Relationship", 1);*/
        else if (binding.edtDriverEmail.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Email", 1);
        else if (binding.edtDriverTelephone.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Your Telephone No.", 1);
        else {
            value = true;
            binding.notifyChange();
            updateDL = binding.getDriver();
            updateDL.IsActive = true;
            updateDL.CustomerId = UserData.loginResponse.User.UserFor;
            updateDL.GetWithDrivingLicence = true;
            updateDL.DrivingLicenceModel.LicenceType = 33;
            updateDL.DrivingLicenceModel.LicenceFor = UserData.loginResponse.User.UserFor;
            updateDL.DrivingLicenceModel.IssuedByStateName = binding.SpState.getSelectedItem().toString();
            updateDL.DrivingLicenceModel.IssuedByState = preference.stateToSateCode.get(binding.SpState.getSelectedItem());
            updateDL.DrivingLicenceModel.IssueByCountryName = binding.SpCountry.getSelectedItem().toString();
            updateDL.DrivingLicenceModel.IssueByCountry = preference.countryToCountryCode.get(binding.SpCountry.getSelectedItem());
            updateDL.RelationId = binding.edtRelationship.getSelectedItemPosition();
            //  updateDL.DrivingLicenceModel.DOB = CustomeDialog.dateConvert(binding.CusDateofBirth.getText().toString());
            updateDL.DrivingLicenceModel.DOB = Helper.setPostDate(binding.CusDateofBirth.getText().toString());
            updateDL.DrivingLicenceModel.ExpiryDate = CustomeDialog.dateConvert(binding.edtExDate.getText().toString());
            updateDL.DrivingLicenceModel.IssueDate = CustomeDialog.dateConvert(binding.edtIssuedate.getText().toString());
            updateDL.DrivingLicenceModel.Number = binding.edtLicenseNo.getText().toString();
            //updateDL.DOB = CustomeDialog.dateConvert(binding.CusDateofBirth.getText().toString());
            updateDL.DOB = Helper.setPostDate(binding.CusDateofBirth.getText().toString());
        }
        return value;
    }

    OnResponseListener addLicence = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status) {
                        fullProgressbar.hide();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Driving", customerProfile);
                        String transactionId= responseJSON.getString("Data");
                        updateDL =    loginRes.getModel(transactionId, UpdateDL.class);
                       // NavHostFragment.findNavController(Fragment_Driving_License_Add.this).popBackStack();
                       /* NavHostFragment.findNavController(Fragment_Driving_License_Add.this)
                                .navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details, bundle);*/
                        if (screen) {
                            NavHostFragment.findNavController(Fragment_Driving_License_Add.this).navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details,bundle);
                        } else {
                            NavHostFragment.findNavController(Fragment_Driving_License_Add.this).popBackStack();
                        }

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

                    } else {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(), msg, 1);
                        fullProgressbar.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
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
                binding.imgDLFronside.setImageBitmap(bitmap);
            }
            else if (imgId.equals("3"))
            {
                binding.imgDLBackside.setImageBitmap(bitmap);
            }
        }
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

    public void imgUpload(File file, Boolean value){
        HashMap<String, String> header = new HashMap<>();
        header.put("FileUploadMasterId", String.valueOf(updateDL.DrivingLicenceModel.Id));
        header.put("Id", String.valueOf(updateDL.DrivingLicenceModel.Id));
        header.put("IsActive","true");
        header.put("CompanyId", String.valueOf(Helper.id));
        if (value) {
            header.put("fileUploadType", String.valueOf(AttachmentType.DrivingLicenseFront));
        } else {
            header.put("fileUploadType", String.valueOf(AttachmentType.DrivingLicenseBack));
        }
        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, header, file);
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
}
