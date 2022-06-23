package com.rentguruz.app.flexiicar.user;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.androidnetworking.AndroidNetworking;
import com.rentguruz.app.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.ImageOptionMenu;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCustomerInsuranceUpdateBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.InsuranceModel;
import com.rentguruz.app.model.UpdateInsurance;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.model.parameter.DateType;
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
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETINSURANCE;
import static com.rentguruz.app.apicall.ApiEndPoint.INSURANCECOMPANYLIST;
import static com.rentguruz.app.apicall.ApiEndPoint.UPDATEINSURANCE;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADIMAGE;

public class Fragment_Update_Insurance_Policy extends BaseFragment
{
  //  Handler handler = new Handler();
  //  public static Context context;
    public String id = "";
   // EditText InsPolicyNo;
   // TextView txtInsurancePolicy,ExpiryDate,IssueDate;
   // ImageView Img_UploadPolicy,BackArrow;
    ImageLoader imageLoader;
    String serverpath="";
    Bundle InsuranceBundle;
    DatePickerDialog datePickerDialog;
    //Spinner SP_InsuranceCName;
    //public String[] InsuranceCompanyName;
    //public int[] InsuranceId;
    HashMap<String, Integer> InsCompanyhashmap=new HashMap<String, Integer>();
    String InsuranceCompany;
    private static int RESULT_LOAD_IMAGE = 1;
    JSONArray ImageList = new JSONArray();
    JSONObject ImgObj = new JSONObject();
    String imagestr;
   // private DoHeader header;
    int selected = 0;
    public String[] InsuranceCompanyName, InsuranceEmail, InsuranceContactPerson;
    public int[] InsuranceId;
    UpdateInsurance updateInsurance;
    InsuranceModel insuranceModel;
    int key = 0;
    FragmentCustomerInsuranceUpdateBinding binding;
    CustomeDialog dialog;
    DateConvert dateConvert= new DateConvert();
    ImageOptionMenu optionMenu;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Intent intent;
    Bitmap insimage;
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
        dialog = new CustomeDialog(getContext());
        updateInsurance = new UpdateInsurance();
        insuranceModel = new InsuranceModel();
        binding = FragmentCustomerInsuranceUpdateBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        if (getArguments().getInt("key") != 1) {
            //((User_Profile)getActivity()).BottomnavInVisible();
        }
        if (Helper.isActiveCustomer){
            ((User_Profile)getActivity()).BottomnavInVisible();
        }
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        serverpath = sp.getString("serverPath", "");
        binding.test.setBackground(userDraw.getImageUpload());
        optionMenu = new ImageOptionMenu(getActivity());
/*        InsPolicyNo=view.findViewById(R.id.edt_PolicyNo);
        ExpiryDate=view.findViewById(R.id.edt_ExpiryDate);
        IssueDate=view.findViewById(R.id.IpIssue_Date);
        SP_InsuranceCName=view.findViewById(R.id.sp_InsuranceCompList);
        txtInsurancePolicy=view.findViewById(R.id.edit_InsurancePolicy);
        Img_UploadPolicy=view.findViewById(R.id.Img_UploadPolicy);
        BackArrow=view.findViewById(R.id.backimg_IP);

        InsPolicyNo.setText(UserData.insuranceModel.PolicyNo);*/

     /*   InsuranceCompany =UserData.insuranceCompanyDetailsModel.Name;
        String[] exp = UserData.insuranceModel.ExpiryDate.split("T");
        ExpiryDate.setText(exp[0]);
        String[] issue = UserData.insuranceModel.IssueDate.split("T");
        IssueDate.setText(issue[0]);*/

        InsuranceBundle = getArguments().getBundle("InsurancePolicyBundle");


        apiService = new ApiService(InsuranceList, RequestType.POST,
                INSURANCECOMPANYLIST, BASE_URL_LOGIN, header, params.getInsuranceCompanyList());

        String  path = "?tableType=3&insuranceFor=" + UserData.loginResponse.User.UserFor;
        apiService = new ApiService(GetCustomerInsurance, RequestType.GET,
                GETINSURANCE+path, BASE_URL_CUSTOMER, header, new JSONObject());

/*     try {
            String Expiry_Date = InsuranceBundle.getString("expiryDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(Expiry_Date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String Expiry_DateStr = sdf.format(date);

            String Issue_Date = InsuranceBundle.getString("insIssueDate");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date1 = dateFormat1.parse(Issue_Date);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String Issue_DateStr = sdf1.format(date1);

            binding.edtPolicyNo.setText(InsuranceBundle.getString("policy_No"));

            InsuranceCompany =InsuranceBundle.getString("insurance_Cmp_Name");
            binding.edtExpiryDate.setText(Expiry_DateStr);
            binding.IpIssueDate.setText(Issue_DateStr);



   *//*         JSONArray docArray = new JSONArray(InsuranceBundle.getString("docArray"));

                final String doc_Details = ((JSONObject) (docArray.get(0))).getString("doc_Details");
                final String doc_Name = ((JSONObject) (docArray.get(0))).getString("doc_Name");

                String url1 = serverpath + doc_Details.substring(2);
                url1=url1.substring(0,url1.lastIndexOf("/")+1)+doc_Name;
                imageLoader.displayImage(url1, Img_UploadPolicy);*//*
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/


        binding.image.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.IpIssueDate.setOnClickListener(this);

        binding.header.screenHeader.setText(companyLabel.Insurance + " Policy");
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.discard.setText("Update");

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

                                    binding.image.setImageBitmap(bitmap);
                                insimage = bitmap;
                                   // Image2 = bitmap;
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
            try
            {
                bitmap = getScaledBitmap(selectedImage,400,250);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                ImgObj.put("fileBase64", img_str_base64);
                ImgObj.put("Doc_For", 8);
                binding.image.setImageBitmap(bitmap);
                insimage = bitmap;
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

    //UpdateCustomerInsurance
    OnResponseListener UpdateCustomerInsurance = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);
                           /* NavHostFragment.findNavController(Fragment_Update_Insurance_Policy.this)
                                    .navigate(R.id.action_InsurancePolicy_to_InsurancePolicyList);*/

                            NavHostFragment.findNavController(Fragment_Update_Insurance_Policy.this).popBackStack();
                            storeImage(insimage);
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    //InsuranceCompanyList
    OnResponseListener InsuranceList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status)
                        {
                             JSONObject data = responseJSON.getJSONObject("Data");
                            final JSONArray resultSet = data.getJSONArray("Data");
                            int len;
                            len = resultSet.length();

                            InsuranceId = new int[len];
                            InsuranceCompanyName = new String[len];
                            InsuranceContactPerson = new String[len];
                            InsuranceEmail = new String[len];
                            for (int i = 0; i <len ; i++) {
                                final JSONObject test = (JSONObject) resultSet.get(i);
                                InsuranceCompanyName[i] = test.getString("Name");
                                InsuranceId[i] = test.getInt("Id");
                                InsuranceContactPerson[i] = test.getString("ContactName");
                                InsuranceEmail[i] = test.getString("Email");
                                if (InsuranceId[i]==UserData.insuranceModel.InsuranceCompanyId){
                                    selected = i;
                                }

                            }
                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, InsuranceCompanyName);
                            binding.spInsuranceCompList.setAdapter(adapterCategories);
                            binding.spInsuranceCompList.setSelection(selected);
                        }

                    } catch (Exception e)
                    {
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

    OnResponseListener GetCustomerInsurance = new OnResponseListener()
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
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        updateInsurance = loginRes.getModel(resultSet.toString(), UpdateInsurance.class);
                        binding.edtPolicyNo.setText(updateInsurance.PolicyNo);
                        binding.edtExpiryDate.setText(dateConvert.allDateConverter(DateType.fulldate, updateInsurance.ExpiryDate,DateType.MMddyyyyS));
                        binding.IpIssueDate.setText(dateConvert.allDateConverter(DateType.fulldate, updateInsurance.IssueDate,DateType.MMddyyyyS));
                        binding.edtDeduct.setText(String.valueOf(updateInsurance.Deductible));
                        binding.edtCoverlimit.setText(String.valueOf(updateInsurance.CoverLimit));

                        if (insuranceModel.AttachmentsModel.AttachmentPath.length()!=0){
                            CustomBindingAdapter.loadImage(binding.image,insuranceModel.AttachmentsModel.AttachmentPath);
                        }

                    } else {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }
                } catch (Exception e)
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                optionMenu.optionVisible(binding.sucessfullRegi,true);
                /*Intent intent = new Intent(Intent.ACTION_PICK,
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
               /* NavHostFragment.findNavController(Fragment_Update_Insurance_Policy.this)
                        .navigate(R.id.action_InsurancePolicy_to_InsurancePolicyList);*/
                NavHostFragment.findNavController(Fragment_Update_Insurance_Policy.this).popBackStack();
                break;

            case R.id.IpIssue_Date:
                dialog.getFullDate("", dialog.getToday(), string -> binding.IpIssueDate.setText(string));
                break;

            case R.id.edt_ExpiryDate:
                dialog.getFullDate(dialog.getToday(),"",string -> binding.edtExpiryDate.setText(string));
                break;

            case  R.id.discard:
                    if (validation()){

                        updateInsurance.InsuranceType = 3;
                        // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
                        // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                        updateInsurance.VerifiedBy = UserData.loginResponse.User.Id;
                        updateInsurance.PolicyNo = binding.edtPolicyNo.getText().toString();
                        updateInsurance.ExpiryDate = binding.edtExpiryDate.getText().toString();
                        updateInsurance.IssueDate = binding.IpIssueDate.getText().toString();
                        updateInsurance.InsuranceFor = UserData.loginResponse.User.UserFor;
                        updateInsurance.GetCompanyDetail = true;
                        updateInsurance.InsuranceCompanyDetailsModel = UserData.insuranceCompanyDetailsModel;

                        insuranceModel.CoverLimit = Integer.parseInt(binding.edtCoverlimit.getText().toString());
                        insuranceModel.Deductible = Integer.parseInt(binding.edtDeduct.getText().toString());
                        insuranceModel.InsuranceType = 3;
                        // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
                        // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                        insuranceModel.VerifiedBy = UserData.loginResponse.User.Id;
                        insuranceModel.PolicyNo = binding.edtPolicyNo.getText().toString();
                        insuranceModel.ExpiryDate = CustomeDialog.dateConvert(binding.edtExpiryDate.getText().toString());
                        insuranceModel.IssueDate = CustomeDialog.dateConvert(binding.IpIssueDate.getText().toString());
                        insuranceModel.InsuranceFor = UserData.loginResponse.User.UserFor;
                        insuranceModel.GetCompanyDetail = true;
                        insuranceModel.Id = UserData.insuranceModel.Id;
                        //insuranceModel.InsuranceCompanyId =UserData.insuranceModel.InsuranceCompanyId;
                        key = Integer.valueOf(String.valueOf(binding.spInsuranceCompList.getSelectedItemId()));
                        insuranceModel.InsuranceCompanyId = InsuranceId[key];
                        ApiService2<InsuranceModel> apiService2 = new ApiService2<InsuranceModel>(UpdateCustomerInsurance,RequestType.PUT, UPDATEINSURANCE,BASE_URL_LOGIN, header, insuranceModel);
                    }
                    break;
        }
    }

    public Boolean validation(){
        Boolean value = false;
        if (binding.edtPolicyNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Insurance Policy No",1);
        else if (binding.edtExpiryDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Expiry Date",1);
        else if (binding.IpIssueDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Issue Date",1);
        else if (binding.spInsuranceCompList.getSelectedItem().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Select Insurance Company Name",1);
        else if(binding.edtCoverlimit.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Coverlimit", 1);
        else if(binding.edtDeduct.getText().toString().equals(""))
            CustomToast.showToast(getActivity(), "Please Enter Insurance Deductible", 1);
        else if(Integer.valueOf(binding.edtCoverlimit.getText().toString()) == 0)
            CustomToast.showToast(getActivity(), "Please Enter Insurance Coverlimit Greater Than 0", 1);
        else if(Integer.valueOf(binding.edtDeduct.getText().toString()) == 0)
            CustomToast.showToast(getActivity(), "Please Enter Insurance Deductible Greater Than 0", 1);
        else
            value = true;

        return value;
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
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, getHeaderModel(updateInsurance.Id,2), file);
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

}
