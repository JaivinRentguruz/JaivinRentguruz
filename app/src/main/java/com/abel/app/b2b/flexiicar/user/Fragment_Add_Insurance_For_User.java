package com.abel.app.b2b.flexiicar.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentAddInsuranceForUserBinding;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.model.base.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.ADDINSURANCEDETAIL;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSURANCECOMPANYLIST;

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

    //private DoHeader header;
    private InsuranceModel insuranceModel;

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

        binding.ImgUploadPolicy.setOnClickListener(this);
        binding.backimgIP.setOnClickListener(this);
        binding.edtExpiryDate.setOnClickListener(this);
        binding.IpIssueDate.setOnClickListener(this);
        binding.editInsurancePolicy.setOnClickListener(this);



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
                byte[] image = stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                ImgObj.put("fileBase64", img_str_base64);
                ImgObj.put("Doc_For", 8);
                binding.ImgUploadPolicy.setImageBitmap(bitmap);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Img_UploadPolicy:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;

            case R.id.backimg_IP:
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


            case R.id.edit_InsurancePolicy:
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
        if (binding.editInsurancePolicy.getText().toString().equals(""))
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
}