package com.rentguruz.app.flexiicar.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import com.androidnetworking.AndroidNetworking;
import com.rentguruz.app.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.ImageOptionMenu;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAddInsuranceForUserBinding;
import com.rentguruz.app.model.InsuranceModel;
import com.rentguruz.app.model.UpdateInsurance;
import com.rentguruz.app.model.base.UserData;

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
import java.util.Date;
import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.ADDINSURANCEDETAIL;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.INSURANCECOMPANYLIST;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADIMAGE;

public class Fragment_Add_Insurance_For_User extends BaseFragment {

    private static int RESULT_LOAD_IMAGE = 1;
    public String[] InsuranceCompanyName, InsuranceEmail, InsuranceContactPerson;
    public int[] InsuranceId;
    //String TAG = "Fragment_Add_Insurance_For_User";
    //Handler handler = new Handler();
    //EditText InsPolicyNo;
    //TextView txtInsurancePolicy,ExpiryDate,IssueDate;
    //ImageView Img_UploadPolicy,BackArrow;
    ImageLoader imageLoader;
    DatePickerDialog datePickerDialog;
    //Spinner SP_InsuranceCName;
    int selected = 0;
    JSONObject ImgObj = new JSONObject();
    String imagestr;
    int key = 0;
    HashMap<String, Integer> getInsurance = new HashMap<>();
    CustomeDialog dialog;
    FragmentAddInsuranceForUserBinding binding;
    Bitmap image;
    //private DoHeader header;
    private InsuranceModel insuranceModel;
    ImageOptionMenu optionMenu;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;
    public static void initImageLoader(Context context) {
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

    //method to convert the selected image to base64 encoded string
    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dialog = new CustomeDialog(getContext());
        insuranceModel = new InsuranceModel();
        binding = FragmentAddInsuranceForUserBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        if (getArguments().getInt("key") != 1) {
            //  ((User_Profile)getActivity()).BottomnavInVisible();
        }
        try {
            if (Helper.isActiveCustomer) {
                ((User_Profile) getActivity()).BottomnavInVisible();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();
        optionMenu = new ImageOptionMenu(getActivity());
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


       /* InsPolicyNo=view.findViewById(R.id.edt_PolicyNo);
        ExpiryDate=view.findViewById(R.id.edt_ExpiryDate);
        IssueDate=view.findViewById(R.id.IpIssue_Date);
        SP_InsuranceCName=view.findViewById(R.id.sp_InsuranceCompList);
        txtInsurancePolicy=view.findViewById(R.id.edit_InsurancePolicy);
        Img_UploadPolicy=view.findViewById(R.id.Img_UploadPolicy);
        BackArrow=view.findViewById(R.id.backimg_IP);*/

/*     //   String bodyParam = "";
        JSONObject object = new JSONObject();
        List<Integer> ints = new ArrayList<>();
        JSONObject filter = new JSONObject();

        try
        {
        //    bodyParam+="customerId=";
            object.accumulate("limit", 10);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 10);
            ints.add(10);
            ints.add(20);
            ints.add(30);
            ints.add(40);
            ints.add(50);
            object.accumulate("pageLimits", ints);

            filter.accumulate("CompanyId", 1);
            filter.accumulate("IsActive", true);

            object.accumulate("filterObj", filter);
       //     System.out.println(bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        apiService = new ApiService(InsuranceList, RequestType.POST,
                INSURANCECOMPANYLIST, BASE_URL_LOGIN, header, params.getInsuranceCompanyList());

        binding.image.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.IpIssueDate.setOnClickListener(this);

        binding.header.screenHeader.setText(companyLabel.Insurance + " Policy");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.discard.setText("Save");
        binding.test.setBackground(userDraw.getImageUpload());

        /*txtInsurancePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insuranceModel.InsuranceType = 3;
               // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
               // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                insuranceModel.VerifiedBy = UserData.loginResponse.User.Id;
                insuranceModel.PolicyNo = InsPolicyNo.getText().toString();
                insuranceModel.ExpiryDate = ExpiryDate.getText().toString();
                insuranceModel.IssueDate = IssueDate.getText().toString();
                insuranceModel.InsuranceFor = UserData.loginResponse.User.UserFor;
                insuranceModel.GetCompanyDetail = true;
                insuranceModel.InsuranceCompanyId = getInsurance.get(SP_InsuranceCName.getSelectedItem());

                Log.d(TAG, "onClick: " + insuranceModel);

               *//* insuranceModel.InsuranceCompanyDetailsModel.Name = SP_InsuranceCName.getSelectedItem().toString();
                insuranceModel.InsuranceCompanyDetailsModel.CompanyId = 1;

                key = Integer.valueOf(String.valueOf(SP_InsuranceCName.getSelectedItemId()));
                insuranceModel.InsuranceCompanyId = InsuranceId[key];
                insuranceModel.InsuranceCompanyDetailsModel.ContactName = InsuranceContactPerson[key];
                insuranceModel.InsuranceCompanyDetailsModel.Email = InsuranceEmail[key];*//*

                ApiService2<InsuranceModel> apiService2 = new ApiService2<InsuranceModel>(AddInsuranceDetail,RequestType.POST, ADDINSURANCEDETAIL,BASE_URL_LOGIN, header, insuranceModel);
            }
        });*/

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

                                binding.image.setImageBitmap(bitmap);
                                // Image2 = bitmap;
                                image = bitmap;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = null;
            Uri targetUri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                imagestr = ConvertBitmapToString(resizedBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bitmap = getScaledBitmap(selectedImage, 400, 250);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] images = stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(images, Base64.NO_WRAP);

                ImgObj.put("fileBase64", img_str_base64);
                ImgObj.put("Doc_For", 8);
                binding.image.setImageBitmap(bitmap);
                image = bitmap;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    /** Create a File for saving an image or video */
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
        String mImageName="MI_"+ timeStamp + "count" +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void imgUpload(File file){
       /* HashMap<String, String> header = new HashMap<>();
        header.put("FileUploadMasterId", String.valueOf(registration.DrivingLicenceModel.Id));
        header.put("Id", String.valueOf(registration.DrivingLicenceModel.Id));
        header.put("IsActive","true");
        header.put("CompanyId", String.valueOf(Helper.id));*/

        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        //apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, header, file);
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, getHeaderModel(insuranceModel.Id,2), file);
    }

    OnResponseListener uploadImage = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d("TAG", "onSuccess: " + response);
        }

        @Override
        public void onError(String error) {
            Log.d("TAG", "onError: " + error);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                optionMenu.optionVisible(binding.sucessfullRegi,true);

               /* Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);*/
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
               /* NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this)
                        .navigate(R.id.action_AddInsurancePolicy_to_InsurancePolicyList);*/
                NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this).popBackStack();
                break;

            case R.id.IpIssue_Date:
                dialog.getFullDate("", dialog.getToday(), string -> binding.IpIssueDate.setText(string));
                break;

            case R.id.edt_ExpiryDate:
                dialog.getFullDate(dialog.getToday(), "", string -> binding.edtExpiryDate.setText(string));
                break;


            case R.id.discard:
                if (validation()) {
                    insuranceModel.InsuranceType = 3;
                    // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
                    // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                    insuranceModel.VerifiedBy = UserData.loginResponse.User.Id;
                    insuranceModel.PolicyNo = binding.edtPolicyNo.getText().toString();
                    insuranceModel.ExpiryDate = CustomeDialog.dateConvert(binding.edtExpiryDate.getText().toString());
                    insuranceModel.IssueDate = CustomeDialog.dateConvert(binding.IpIssueDate.getText().toString());
                    insuranceModel.InsuranceFor = UserData.loginResponse.User.UserFor;
                    insuranceModel.GetCompanyDetail = true;
                    insuranceModel.InsuranceCompanyId = getInsurance.get(binding.spInsuranceCompList.getSelectedItem());
                    insuranceModel.CoverLimit = Integer.parseInt(binding.edtCoverlimit.getText().toString());
                    insuranceModel.Deductible = Integer.parseInt(binding.edtDeduct.getText().toString());
                    Log.d(TAG, "onClick: " + insuranceModel);

               /* insuranceModel.InsuranceCompanyDetailsModel.Name = SP_InsuranceCName.getSelectedItem().toString();
                insuranceModel.InsuranceCompanyDetailsModel.CompanyId = 1;

                key = Integer.valueOf(String.valueOf(SP_InsuranceCName.getSelectedItemId()));
                insuranceModel.InsuranceCompanyId = InsuranceId[key];
                insuranceModel.InsuranceCompanyDetailsModel.ContactName = InsuranceContactPerson[key];
                insuranceModel.InsuranceCompanyDetailsModel.Email = InsuranceEmail[key];*/

                    ApiService2<InsuranceModel> apiService2 = new ApiService2<InsuranceModel>(AddInsuranceDetail, RequestType.POST, ADDINSURANCEDETAIL, BASE_URL_LOGIN, header, insuranceModel);
                }
                break;
        }
    }

    public Boolean validation() {
        Boolean value = false;
        if (binding.edtPolicyNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Ploicy Number", 1);
        else if (binding.edtExpiryDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Expiry Date", 1);
        else if (binding.IpIssueDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Issue Date", 1);
        else if (getInsurance.get(binding.spInsuranceCompList.getSelectedItem()).equals(""))
            CustomToast.showToast(getActivity(), "Please Select Insurance Company", 1);
        else if(binding.edtCoverlimit.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Coverlimit", 1);
        else if(binding.edtDeduct.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Deductible", 1);
        else if(Integer.valueOf(binding.edtCoverlimit.getText().toString()) == 0)
            CustomToast.showToast(getActivity(), "Please Enter Insurance Coverlimit Greater Than 0", 1);
        else if(Integer.valueOf(binding.edtDeduct.getText().toString()) == 0)
            CustomToast.showToast(getActivity(), "Please Enter Insurance Deductible Greater Than 0", 1);
        else {
            value = true;
        }
        return value;
    }

    OnResponseListener InsuranceList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status) {
                            JSONObject data = responseJSON.getJSONObject("Data");
                            final JSONArray resultSet = data.getJSONArray("Data");
                            int len;
                            len = resultSet.length();

                            InsuranceId = new int[len];
                            InsuranceCompanyName = new String[len];
                            InsuranceContactPerson = new String[len];
                            InsuranceEmail = new String[len];
                            for (int i = 0; i < len; i++) {
                                final JSONObject test = (JSONObject) resultSet.get(i);
                                InsuranceCompanyName[i] = test.getString("Name");
                                InsuranceId[i] = test.getInt("Id");
                                InsuranceContactPerson[i] = test.getString("ContactName");
                                InsuranceEmail[i] = test.getString("Email");
                                getInsurance.put(InsuranceCompanyName[i], InsuranceId[i]);

                            }
                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, InsuranceCompanyName);
                            binding.spInsuranceCompList.setAdapter(adapterCategories);
                            binding.spInsuranceCompList.setSelection(selected);
                        } else {
                            CustomToast.showToast(getActivity(), responseJSON.getString("Message"), 1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };
    OnResponseListener AddInsuranceDetail = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status) {
                         /*   NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this)
                                    .navigate(R.id.action_AddInsurancePolicy_to_InsurancePolicyList);*/
                            NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this).popBackStack();
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            insuranceModel = loginRes.getModel(resultSet.toString(), InsuranceModel.class);
                            storeImage(image);
                        } else {
                            CustomToast.showToast(getActivity(), responseJSON.getString("Message"), 1);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
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