package com.abel.app.b2b.flexiicar.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.DoRegistration;
import com.abel.app.b2b.model.DrivingLicenceModel;
import com.abel.app.b2b.R;
import com.abel.app.b2b.ScanDrivingLicense;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.databinding.FragmentDriverProfile2Binding;
import com.abel.app.b2b.model.parameter.DateType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.abel.app.b2b.apicall.ApiEndPoint.firstImage;
import static com.abel.app.b2b.apicall.ApiEndPoint.secondImage;

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


/*    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }*/


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

            //getbackscreendata
            registration = (DoRegistration) getArguments().getSerializable("RegistrationBundle");

            preference.stateCountry( binding.SpCountry, binding.SpState, registration.AddressesModel.CountryName, registration.AddressesModel.StateName);
            //2021-10-21
            binding.edtExpiryDateDL.setText(Helper.getDateDisplay(DateType.yyyyMMddD,drivingLicenceModel.ExpiryDate));
            binding.CusDateofBirth.setText(Helper.getDateDisplay(DateType.yyyyMMddD, drivingLicenceModel.DOB));
            binding.ISSueDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,drivingLicenceModel.IssueDate));



           /* preference.getStringArray("state");

            countrycode = Fragment_Driver_Profile_1.addressesModel.CountryId-1;

            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(),
                    R.layout.spinner_layout, R.id.text1, preference.getStringArray("country"));
            binding.SpCountry.setAdapter(adapterCategories);
            binding.SpCountry.setSelection(countrycode);
            String country = String.valueOf(binding.SpCountry.getSelectedItem());

            ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(),
                    R.layout.spinner_layout, R.id.text1, preference.getstatename(country));
            binding.SpState.setAdapter(adapterCategories2);
            binding.SpState.setSelection(statecode);

            binding.SpCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String countryname = String.valueOf(binding.SpCountry.getSelectedItem());
                    ArrayAdapter<String> adapterCategories21 = new ArrayAdapter<String>(getActivity(),
                            R.layout.spinner_layout, R.id.text1, preference.getstatename(countryname));
                    binding.SpState.setAdapter(adapterCategories21);
                    statecode = preference.getDefaultstate(Fragment_Driver_Profile_1.addressesModel.StateName);
                    binding.SpState.setSelection(statecode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/


          //  RegistrationBundle = getArguments().getBundle("RegistrationBundle");

         /*   lblnext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {

                        if (edt_DriverLicenseNO.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Driving License NO.",1);
                        else if (edt_ExpiryDateDL.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Driving License Expiry Date",1);
                        else if (Cus_DateofBirth.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Date Of Birth",1);
                        else if (IssueDate.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter License Issue Date",1);
                        else if (StateandProvience.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your State Name",1);
                        else {

                            RegistrationBundle.putString("Licence_No", edt_DriverLicenseNO.getText().toString());

                            //ExpiryDate
                            String Exdatestr = edt_ExpiryDateDL.getText().toString();
                            Date ExDate = new SimpleDateFormat("mm/dd/yyyy").parse(Exdatestr);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedDate = formatter.format(ExDate);
                            RegistrationBundle.putString("LExpiry_Date", parsedDate);
                            //Date Of Birth
                            String Cus_DateofBirthStr = Cus_DateofBirth.getText().toString();
                            Date DateofBirth = new SimpleDateFormat("mm/dd/yyyy").parse(Cus_DateofBirthStr);
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedDateofBirth = format1.format(DateofBirth);
                            RegistrationBundle.putString("Cust_DOB", parsedDateofBirth);
                            RegistrationBundle.putString("LIssue_By", DL_IssueBy.getText().toString());
                            //Issue Date
                            String IssueDateStr = IssueDate.getText().toString();
                            Date DateofIssue = new SimpleDateFormat("mm/dd/yyyy").parse(IssueDateStr);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedIssueDate = format2.format(DateofIssue);
                            RegistrationBundle.putString("LIssue_Date", parsedIssueDate);
                            RegistrationBundle.putString("Cust_StateProvience", StateandProvience.getText().toString());
                            RegistrationBundle.putString("ImageList", ImageList.toString());
                            Bundle Registration = new Bundle();
                            Registration.putBundle("RegistrationBundle", RegistrationBundle);
                            System.out.println(Registration);

                            Date date = formatter.parse(parsedDate);

                            drivingLicenceModel.Number = edt_DriverLicenseNO.getText().toString();
                            drivingLicenceModel.ExpiryDate =parsedDate;

                            drivingLicenceModel.DOB =parsedDateofBirth;
                            drivingLicenceModel.IssueDate = parsedIssueDate;

                            drivingLicenceModel.LicenceFor = 0;
                            drivingLicenceModel.LicenceType = 3;
                            drivingLicenceModel.Category = "";

                            drivingLicenceModel.IssuedByStateName=StateandProvience.getText().toString();

                            drivingLicenceModel.IssuedByState = Fragment_Driver_Profile_1.addressesModel.StateId;
                            drivingLicenceModel.IssueByCountry = Fragment_Driver_Profile_1.addressesModel.CountryId;
                            drivingLicenceModel.IssueByCountryName = Fragment_Driver_Profile_1.addressesModel.CountryName;

                            NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                                    .navigate(R.id.action_DriverProfile2_to_DriverProfile3, Registration);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });*/

        }catch (Exception e)
        {
            e.printStackTrace();
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
                    }
                    if (imgId.equals("3"))
                    {
                        JSONObject docObj = new JSONObject();
                        docObj.put("Doc_For", 6);
                        docObj.put("VhlPictureSide", 3);
                        docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                        ImageList.put(docObj);
                        binding.DLBacksideImg.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    break;

                case R.id.DLFronsideImg:
                    imgId = "2";
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
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


}
