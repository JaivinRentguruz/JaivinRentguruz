package com.abel.app.b2b.flexiicar.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentUpdateCustomerProfileBinding;
import com.abel.app.b2b.model.AddressesModel;
import com.abel.app.b2b.model.base.UploadImage;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.UpdateDL;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.ADDPROFILEPICTURE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.CUSTOMERUPDATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.REMOVEPROFILEPICTURE;
import static com.abel.app.b2b.apicall.ApiEndPoint.UPLOADIMAGE;

public class Fragment_Update_Customer_Profile extends BaseFragment
{
   // ImageView backimg,Img_Profile,RemoveProfilePic;
    //Bundle CustomerBundle;
   // Handler handler = new Handler();
    public String id="";
    //public static Context context;
    //EditText edtFirstName,edtLastName,edtMobileNO,edtPhoneNo,edtEmail,edtStreetNo,edtUnit,edZipcode,edtCity;
    //TextView txt_DiscardProfile;
    //Spinner Sp_Country,Sp_State;
    //LinearLayout lblSubmitprofile,ll_Profile;
    public String[] Country,State;
    public int[] CountyId,StateId;
    HashMap<String, Integer> countryhashmap=new HashMap<String, Integer>();
    HashMap<String,Integer>Statehashmap=new HashMap<>();
    private int RESULT_LOAD_IMAGE = 1;
    String imagestr;
    JSONObject profilepicObj = new JSONObject();
    int cust_Country_ID, cust_State_ID;
    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    ImageLoader imageLoader;
    String serverpath="",cust_Photo=" ";
    CustomerProfile customerProfile;
    //CustomPreference preference;
    UpdateDL updateDL;
    int statecode=0, countrycode=0;
    //private DoHeader header;
    FragmentUpdateCustomerProfileBinding binding;
    Intent intent;
    public static void initImageLoader(Context context)
    {
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

        customerProfile = new CustomerProfile();
        updateDL = new UpdateDL();
        binding = FragmentUpdateCustomerProfileBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        String bodyParam = "";
        super.onViewCreated(view, savedInstanceState);
        //((User_Profile)getActivity()).BottomnavInVisible();
        customerProfile = (CustomerProfile) getArguments().getSerializable("CustomerBundle");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (Helper.isActiveCustomer){
            ((User_Profile)getActivity()).BottomnavInVisible();
        }
        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }
        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();
        binding.header.screenHeader.setText("User Profile");

   /*     backimg=view.findViewById(R.id.backimg_editprofile);
        RemoveProfilePic=view.findViewById(R.id.RemoveProfilePic);
        lblSubmitprofile=view.findViewById(R.id.lblSubmitprofile);
        edtFirstName=view.findViewById(R.id.edt_Firstname);
        edtLastName=view.findViewById(R.id.edt_LastName);
        edtMobileNO=view.findViewById(R.id.edt_MobileNo);
        edtPhoneNo=view.findViewById(R.id.edt_PhoneNO);
        edtEmail=view.findViewById(R.id.edt_EmailBox);
        ll_Profile=view.findViewById(R.id.ll_Profile);
        edtStreetNo=view.findViewById(R.id.edt_streetNum);
        edtUnit=view.findViewById(R.id.edt_UnitNum);
        edZipcode=view.findViewById(R.id.edt_Zipcode);
        edtCity=view.findViewById(R.id.Edt_City);

        preference.getStringArray("state");
        Sp_Country=view.findViewById(R.id.sp_Country);
        Sp_State=view.findViewById(R.id.Sp_State);

        txt_DiscardProfile=view.findViewById(R.id.txt_DiscardProfile);*/

        try {
        binding.edtFirstname.setText(customerProfile.Fname);
        binding.edtLastName.setText(customerProfile.Lname);
        binding.edtPhoneNO.setText(customerProfile.MobileNo);
        binding.edtEmail.setText(customerProfile.Email);
        binding.edtCity.setText(customerProfile.AddressesModel.City);
        binding.edtZipcode.setText(customerProfile.AddressesModel.ZipCode);
        binding.edtUnitNum.setText(customerProfile.AddressesModel.UnitNo);
        binding.edtStreetNum.setText(customerProfile.AddressesModel.Street);

        preference.stateCountry(binding.spCountry,binding.SpState,
                customerProfile.AddressesModel.CountryName,
                customerProfile.AddressesModel.StateName);

        } catch (Exception e){
            preference.stateCountry(binding.spCountry,binding.SpState,
                    "",
                    "");
            e.printStackTrace();
        }
//        edtStreetNo.setText(customerProfile.AddressesModel.Street.toString());
//        edtUnit.setText(customerProfile.AddressesModel.Unitno.toString());
//        edZipcode.setText(customerProfile.AddressesModel.Zipcode.toString());
//        edtCity.setText(customerProfile.AddressesModel.City.toString());

   /*     AndroidNetworking.initialize(getActivity());
        Fragment_Update_Customer_Profile.context = getActivity();*/

        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.ImgProfilePic.setOnClickListener(this);
        binding.llProfile.setOnClickListener(this);
        binding.edtStreetNum.setOnClickListener(this);
        binding.lblSubmitprofile.setOnClickListener(this);
        binding.RemoveProfilePic.setOnClickListener(this);
        try {

            String url = String.valueOf(UserData.updateDL.AttachmentsModel.AttachmentPath);
            Glide.with(getContext()).load(url).into(binding.ImgProfilePic);



        } catch (Exception e){
            e.printStackTrace();
        }

        binding.edtEmail.setFocusable(false);
        binding.edtEmail.setEnabled(false);
        binding.edtEmail.setCursorVisible(false);
        binding.edtEmail.setKeyListener(null);
       // binding.edtEmail.setBackgroundColor(Color.TRANSPARENT);



/*
        binding.ImgProfilePic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });*/

  /*      ll_Profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });*/

       /* edtStreetNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if(!Places.isInitialized())
                    {
                        Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                    }
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                    startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });*/

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        serverpath = sp.getString("serverPath", "");
        id = sp.getString(getString(R.string.id), "");

      /*  CustomerBundle = getArguments().getBundle("CustomerBundle");

        edtFirstName.setText(CustomerBundle.getString("cust_FName"));
        edtLastName.setText(CustomerBundle.getString("cust_LName"));
        edtMobileNO.setText(CustomerBundle.getString("cust_MobileNo"));
        edtPhoneNo.setText(CustomerBundle.getString("cust_Phoneno"));
        edtEmail.setText(CustomerBundle.getString("cust_Email"));
        edtStreetNo.setText(CustomerBundle.getString("cust_Street"));
        edtUnit.setText(CustomerBundle.getString("cust_UnitNo"));
        edZipcode.setText(CustomerBundle.getString("cust_ZipCode"));
        edtCity.setText(CustomerBundle.getString("cust_City"));

        cust_Country_ID = CustomerBundle.getInt("cust_Country_ID");
        cust_State_ID = CustomerBundle.getInt("cust_State_ID");

        cust_Photo = CustomerBundle.getString("cust_Photo");*/

     //   String url1 = serverpath+cust_Photo.substring(2);
     //   imageLoader.displayImage(url1,Img_Profile);


      /*  lblSubmitprofile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (edtFirstName.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter FirstName",1);
                else if (edtLastName.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter LastName",1);
                else if (edtMobileNO.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter Mobile Number",1);
                else if (edtEmail.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter Email",1);
                else if (edtStreetNo.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter Street No",1);
                else if (edZipcode.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter Zipcode",1);
                else if (edtCity.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter City",1);
                else
                {
                    updateDL.Id = UserData.customerProfile.Id;
                    updateDL.IsActive = true;
                    updateDL.CompanyId = 1;
                    updateDL.CustomerId = UserData.customerProfile.UserModel.UserFor ;
                    updateDL.FName = edtFirstName.getText().toString();
                    updateDL.LName = edtLastName.getText().toString();
                    updateDL.PhoneNo =edtPhoneNo.getText().toString();
                    updateDL.MobileNo =edtMobileNO.getText().toString();
                    updateDL.Email = edtEmail.getText().toString();
                    updateDL.AddressesModel = customerProfile.AddressesModel;
                    updateDL.AddressesModel.APIRequestType = 2;
                    updateDL.AddressesModel.Street = edtStreetNo.getText().toString();
                    updateDL.AddressesModel.City = edtCity.getText().toString();
                    updateDL.AddressesModel.ZipCode = edZipcode.getText().toString();
                    updateDL.AddressesModel.CountryId = preference.countryToCountryCode.get(Sp_Country.getSelectedItem());
                    updateDL.AddressesModel.CountryName = Sp_Country.getSelectedItem().toString();
                    updateDL.AddressesModel.StateId = preference.stateToSateCode.get(Sp_State.getSelectedItem());
                    updateDL.AddressesModel.StateName = Sp_State.getSelectedItem().toString();

                    JSONObject bodyParam = new JSONObject();
                *//*    try {
                        bodyParam.accumulate("CustomerId", Integer.parseInt(id));
                        bodyParam.accumulate("cust_FName", edtFirstName.getText().toString());
                        bodyParam.accumulate("cust_LName", edtLastName.getText().toString());
                        bodyParam.accumulate("cust_Email", edtEmail.getText().toString());
                        bodyParam.accumulate("cust_MobileNo", edtMobileNO.getText().toString());
                        bodyParam.accumulate("cust_Phoneno", edtPhoneNo.getText().toString());
                        bodyParam.accumulate("cust_Street", edtStreetNo.getText().toString());
                        bodyParam.accumulate("cust_UnitNo", edtUnit.getText().toString());
                        bodyParam.accumulate("cust_City", edtCity.getText().toString());
                        bodyParam.accumulate("cust_ZipCode",edZipcode.getText().toString());
                        int s = countryhashmap.get(Sp_Country.getSelectedItem());
                        int s1=Statehashmap.get(Sp_State.getSelectedItem());
                        bodyParam.accumulate("cust_Country_ID",s);
                        bodyParam.accumulate("cust_State_ID", s1);
                       // bodyParam.accumulate("cust_Gender", CustomerBundle.getString("cust_Gender"));
                      //  bodyParam.accumulate("cust_DOB", CustomerBundle.getString("cust_DOB"));
                        System.out.println(bodyParam);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }*//*
                   *//* ApiService ApiService = new ApiService(UpdateCustomerProfile, RequestType.POST,
                            UPDATECUSTOMERPROFILE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*//*
                    ApiService2<UpdateDL> apiService2 = new ApiService2<UpdateDL>(UpdateCustomerProfile, RequestType.PUT,
                    CUSTOMERUPDATE, BASE_URL_CUSTOMER, header, updateDL);
                }
            }
        });*/

        /*RemoveProfilePic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject bodyParam = new JSONObject();
                try {
                    bodyParam.accumulate("Trans_ID", Integer.parseInt(id));
                   // bodyParam.accumulate("Doc_Details",CustomerBundle.getString("cust_Photo"));

                     ApiService ApiService = new ApiService(RemoveCustomerProfile, RequestType.POST,
                             REMOVEPROFILEPICTURE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });*/



      /*  ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1, preference.getStringArray("country"));
        Sp_Country.setAdapter(adapterCategories);
       // Sp_Country.setSelection(countrycode);
        Sp_Country.setSelection(customerProfile.AddressesModel.CountryId);
        String country = String.valueOf(Sp_Country.getSelectedItem());

        ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, preference.getstatename(country));
        Sp_State.setAdapter(adapterCategories2);
        Sp_State.setSelection(statecode);

        Sp_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, preference.getstatename(String.valueOf(Sp_Country.getSelectedItem())));
                statecode = preference.getDefaultstate(customerProfile.AddressesModel.StateName);
                Sp_State.setAdapter(adapterCategories2);
                Sp_State.setSelection(statecode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

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
            String[] imageProjection = {MediaStore.Images.Media.DATA};
            Cursor cursor =context.getContentResolver().query(selectedImage, imageProjection, null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();
                int indexImage = cursor.getColumnIndex(imageProjection[0]);
                String  part_image = cursor.getString(indexImage);
                File file = new File(part_image);
                UploadImage uploadImages = new UploadImage();
                uploadImages.CompanyId = 1;
                uploadImages.fileUploadType = 1;
                uploadImages.Id =UserData.loginResponse.User.UserFor ;
                uploadImages.file = file;
                uploadImages.test = imagestr;

                AndroidNetworking.initialize(getActivity());
                ApiService apiService = new ApiService();

                Double file_size = Double.valueOf(String.valueOf(file.length()/1024));
                Double file_sizeMB = file_size/1024;

                Log.d("TAG", "onActivityResult: " + file_sizeMB);

                apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, params.getUserImage(UserData.loginResponse.User.UserFor), file);

                Bitmap bitmap = null;
            }


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
                bitmap = getScaledBitmap(selectedImage,150,150);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.DEFAULT);

                JSONObject profilepicObj = new JSONObject();
//                profilepicObj.put("Trans_ID",Integer.parseInt(id));
                profilepicObj.put("Doc_For", 20);
                profilepicObj.put("fileBase64", img_str_base64);
             //   System.out.println(profilepicObj);
                try
                {
                  /*  ApiService ApiService = new ApiService(UpdateCustomerProfile, RequestType.POST,
                            ADDPROFILEPICTURE, BASE_URL_CUSTOMER, new HashMap<String, String>(), profilepicObj);*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                binding.ImgProfilePic.setImageBitmap(bitmap);
        }


        if (requestCode == REQUEST_PLACE_ADDRESS && resultCode == Activity.RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            // Log.i(TAG, "Place city and postal code: " + place.getAddress().subSequence(place.getName().length(),place.getAddress().length()));
            try {
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String UnitNo=addresses.get(0).getFeatureName();
                    String Street=addresses.get(0).getThoroughfare();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();

                    Log.e("Address: ", "" + address);
                    Log.e("City: ", "" + city);
                    Log.e("Street: ", "" + Street);
                    Log.e("State: ", "" + state);
                    Log.e("Country: ", "" + country);
                    Log.e("APostalCode: ", "" + postalCode);
                    Log.e("UnitNO: ", "" + UnitNo);

                    binding.edtStreetNum.setText(Street);
                    binding.edtCity.setText(city);
                    binding.edtZipcode.setText(postalCode);
                    binding.edtUnitNum.setText(UnitNo);
                  /*  for (int i = 0; i < State.length; i++) {

                        if (State[i].equals(state)) {
                            Sp_State.setSelection(i);
                            break;
                        }
                    }

                    for (int j = 0; j < Country.length; j++) {

                        if (Country[j].equals(country)) {
                            Sp_Country.setSelection(j);
                            break;
                        }
                    }*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //setMarker(latLng);
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
    //UpdateCustomerProfile
    OnResponseListener UpdateCustomerProfile = new OnResponseListener()
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
                            NavHostFragment.findNavController(Fragment_Update_Customer_Profile.this)
                                    .navigate(R.id.action_Edit_Personal_info_to_User_Details);
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

    //RemoveCustomerProfile
    OnResponseListener RemoveCustomerProfile = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,0);
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
            System.out.println("Error-" + error);
        }
    };

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
        try {
            switch (v.getId()){
                case R.id.back:

                case R.id.discard:
                    NavHostFragment.findNavController(Fragment_Update_Customer_Profile.this)
                            .navigate(R.id.action_Edit_Personal_info_to_User_Details);
                    break;

                case R.id.Img_ProfilePic:
                case R.id.ll_Profile:
                    intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    break;

                case R.id.edt_streetNum:
                    try
                    {
                        if(!Places.isInitialized())
                        {
                            Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                        }
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                        startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case R.id.RemoveProfilePic:

                    break;
                case R.id.lblSubmitprofile:

                    if (binding.edtFirstname.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter FirstName",1);
                    else if (binding.edtLastName.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter LastName",1);
                    else if (binding.edtPhoneNO.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Mobile Number",1);
                    else if (binding.edtEmail.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Email",1);
                    else if (binding.edtStreetNum.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Street No",1);
                    else if (binding.edtZipcode.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Zipcode",1);
                    else if (binding.edtCity.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter City",1);
                    else
                    {
                        updateDL.Id = UserData.customerProfile.Id;
                        updateDL.IsActive = true;
                        updateDL.CustomerId = UserData.customerProfile.UserModel.UserFor ;
                        updateDL.FName =binding.edtFirstname.getText().toString();
                        updateDL.LName = binding.edtLastName.getText().toString();
                        updateDL.PhoneNo =binding.edtPhoneNO.getText().toString();
                        updateDL.MobileNo = binding.edtPhoneNO.getText().toString();
                        updateDL.Email = binding.edtEmail.getText().toString();
                        try {
                            updateDL.AddressesModel = customerProfile.AddressesModel;
                            updateDL.AddressesModel.APIRequestType = 2;
                            updateDL.AddressesModel.Street = binding.edtStreetNum.getText().toString();
                            updateDL.AddressesModel.City = binding.edtCity.getText().toString();
                            updateDL.AddressesModel.ZipCode = binding.edtZipcode.getText().toString();
                            updateDL.AddressesModel.CountryId = preference.countryToCountryCode.get(binding.spCountry.getSelectedItem());
                            updateDL.AddressesModel.CountryName = binding.spCountry.getSelectedItem().toString();
                            updateDL.AddressesModel.StateId = preference.stateToSateCode.get(binding.SpState.getSelectedItem());
                            updateDL.AddressesModel.StateName = binding.SpState.getSelectedItem().toString();
                        } catch (Exception e){
                            updateDL.AddressesModel = new AddressesModel();
                            updateDL.AddressesModel.APIRequestType = 2;
                            updateDL.AddressesModel.Street = binding.edtStreetNum.getText().toString();
                            updateDL.AddressesModel.City = binding.edtCity.getText().toString();
                            updateDL.AddressesModel.ZipCode = binding.edtZipcode.getText().toString();
                            updateDL.AddressesModel.CountryId = preference.countryToCountryCode.get(binding.spCountry.getSelectedItem());
                            updateDL.AddressesModel.CountryName = binding.spCountry.getSelectedItem().toString();
                            updateDL.AddressesModel.StateId = preference.stateToSateCode.get(binding.SpState.getSelectedItem());
                            updateDL.AddressesModel.StateName = binding.SpState.getSelectedItem().toString();
                            e.printStackTrace();
                        }


                        JSONObject bodyParam = new JSONObject();
                /*    try {
                        bodyParam.accumulate("CustomerId", Integer.parseInt(id));
                        bodyParam.accumulate("cust_FName", edtFirstName.getText().toString());
                        bodyParam.accumulate("cust_LName", edtLastName.getText().toString());
                        bodyParam.accumulate("cust_Email", edtEmail.getText().toString());
                        bodyParam.accumulate("cust_MobileNo", edtMobileNO.getText().toString());
                        bodyParam.accumulate("cust_Phoneno", edtPhoneNo.getText().toString());
                        bodyParam.accumulate("cust_Street", edtStreetNo.getText().toString());
                        bodyParam.accumulate("cust_UnitNo", edtUnit.getText().toString());
                        bodyParam.accumulate("cust_City", edtCity.getText().toString());
                        bodyParam.accumulate("cust_ZipCode",edZipcode.getText().toString());
                        int s = countryhashmap.get(Sp_Country.getSelectedItem());
                        int s1=Statehashmap.get(Sp_State.getSelectedItem());
                        bodyParam.accumulate("cust_Country_ID",s);
                        bodyParam.accumulate("cust_State_ID", s1);
                       // bodyParam.accumulate("cust_Gender", CustomerBundle.getString("cust_Gender"));
                      //  bodyParam.accumulate("cust_DOB", CustomerBundle.getString("cust_DOB"));
                        System.out.println(bodyParam);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }*/
                   /* ApiService ApiService = new ApiService(UpdateCustomerProfile, RequestType.POST,
                            UPDATECUSTOMERPROFILE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*/
                        ApiService2<UpdateDL> apiService2 = new ApiService2<UpdateDL>(UpdateCustomerProfile, RequestType.PUT,
                                CUSTOMERUPDATE, BASE_URL_CUSTOMER, header, updateDL);
                    }

                    break;
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }

 /*   //Countrylist
    OnResponseListener CountryList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {

                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray Countrylist = resultSet.getJSONArray("t0040_Country_Master");

                            int len;
                            len = Countrylist.length();

                            Country = new String[len];
                            CountyId = new int[len];
                            int position = 0;

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) Countrylist.get(j);
                                final int country_ID = test.getInt("country_ID");
                                final String country_Name = test.getString("country_Name");
                                Country[j] = country_Name;
                                CountyId[j] = country_ID;

                                countryhashmap.put(country_Name,country_ID);

                                if(cust_Country_ID==(country_ID))
                                {
                                    position = j;
                                }
                            }

                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_layout, R.id.text1, Country);
                            Sp_Country.setAdapter(adapterCategories);
                            Sp_Country.setSelection(position);

                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
    //StateList
    OnResponseListener StateList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {

                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray StateList = resultSet.getJSONArray("t0040_State_Master");

                            int len;
                            len = StateList.length();
                            StateId = new int[len];
                            State = new String[len];
                            int position = 0;
                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) StateList.get(j);
                                final int state_ID = test.getInt("state_ID");
                                final String state_Name = test.getString("state_Name");

                                State[j] = state_Name;
                                StateId[j] = state_ID;
                                Statehashmap.put(state_Name,state_ID);

                                if(cust_State_ID == (state_ID))
                                {
                                    position = j;
                                }
                            }

                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, State);
                            Sp_State.setAdapter(adapterCategories);
                            Sp_State.setSelection(position);
                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
    };*/
}
