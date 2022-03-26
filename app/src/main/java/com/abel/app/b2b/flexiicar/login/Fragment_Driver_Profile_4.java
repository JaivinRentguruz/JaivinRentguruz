package com.abel.app.b2b.flexiicar.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.model.parameter.UserTypes;
import com.androidnetworking.AndroidNetworking;
import com.abel.app.b2b.adapters.CreditCardWatcher;
import com.abel.app.b2b.model.CreditCardModel;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.DoRegistration;
import com.abel.app.b2b.R;
import com.abel.app.b2b.ScanDrivingLicense;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.OnStringListner;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.abel.app.b2b.model.base.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kotlin.text.Regex;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSERT;

public class Fragment_Driver_Profile_4 extends Fragment
{
    String TAG = "Fragment_Driver_Profile_4";
    LinearLayout lblnext;
    ImageView backArrow;
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    Bundle RegistrationBundle;
    EditText edt_cardNo,edt_CvvNo,edt_cardholderName,edt_streetName,edtxtUnitNumber,edtPincodeNo,Edtext_City;
    TextView edt_ExDate;
    Spinner SP_Country,SP_State;
    public String[] Country,State;
    int statecode=0, countrycode=0;
    public int[] CountryId,StateId;
    String imagestr;
    Bitmap bitmap = null;
    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    String country, state;
    JSONArray ImageList = new JSONArray();
    TextView txtDiscard;
    CreditCardModel creditCardModel;
    String card = "";
    private DoHeader header;
    CustomPreference preference;
    CustomeDialog dialog;
    DoRegistration registration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        creditCardModel = new CreditCardModel();
        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        preference = new CustomPreference(getContext());
        dialog = new CustomeDialog(getContext());
        registration = new DoRegistration();
        RegistrationBundle = new Bundle();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile_4, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            lblnext = view.findViewById(R.id.lblnextscreen);
            edt_cardNo = view.findViewById(R.id.edt_cardNo);
            edt_ExDate = view.findViewById(R.id.edt_ExDate);
            edt_CvvNo = view.findViewById(R.id.edt_CvvNo);
          //  preference.getStringArray("state");
            edt_cardholderName = view.findViewById(R.id.edt_cardholderName);

            edt_streetName = view.findViewById(R.id.edt_streetNameCC);
            edtxtUnitNumber = view.findViewById(R.id.edtxtUnitNumberCC);
            edtPincodeNo = view.findViewById(R.id.edtPincodeNoCC);
            Edtext_City = view.findViewById(R.id.Edtext_CityCC);
            txtDiscard=view.findViewById(R.id.discard);

            SP_Country=view.findViewById(R.id.spi_CountryList);
            SP_State=view.findViewById(R.id.Spi_StatelistCC);

            registration = (DoRegistration) getArguments().getSerializable("RegistrationBundle");
            char space = ' ';
            edt_cardNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d(TAG, "afterTextChanged: " + s.length() + " : " + s);

                    // Remove spacing char
                    if (s.length() > 0 && (s.length() % 5) == 0) {
                        final char c = s.charAt(s.length() - 1);
                        if (space == c) {
                            s.delete(s.length() - 1, s.length());
                        }
                    }
                    // Insert char where needed.
                    if (s.length() > 0 && (s.length() % 5) == 0) {
                        char c = s.charAt(s.length() - 1);
                        // Only if its a digit where there should be a space we insert a space
                        if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                            s.insert(s.length() - 1, String.valueOf(space));
                        }
                    }
                }
            });

           /* RegistrationBundle = getArguments().getBundle("RegistrationBundle");
            ImageList = new JSONArray(RegistrationBundle.getString("ImageList"));

            edt_streetName.setText(RegistrationBundle.getString("Cust_Street"));
            edtxtUnitNumber.setText(RegistrationBundle.getString("Cust_UnitNo"));
            edtPincodeNo.setText(RegistrationBundle.getString("Cust_ZipCode"));
            Edtext_City.setText(RegistrationBundle.getString("Cust_City"));
            edt_cardholderName.setText(RegistrationBundle.getString("Cust_FName"));

            country = RegistrationBundle.getString("Cust_Country_Name");
            state = RegistrationBundle.getString("Cust_State_Name");

            countrycode = RegistrationBundle.getInt("Cust_Country_ID")-1;
            statecode = RegistrationBundle.getInt("Cust_State_ID")-1;*/


            edt_streetName.setText(registration.AddressesModel.Street);
            edtxtUnitNumber.setText(registration.AddressesModel.UnitNo);
            edtPincodeNo.setText(registration.AddressesModel.ZipCode);
            Edtext_City.setText(registration.AddressesModel.City);
            edt_cardholderName.setText(registration.Fname + " " + registration.Lname);

            preference.stateCountry( SP_Country, SP_State, registration.AddressesModel.CountryName, registration.AddressesModel.StateName);
    /*        country =registration.AddressesModel.CountryName;
            state = registration.AddressesModel.StateName;

            countrycode =registration.AddressesModel.CountryId-1;
            statecode = registration.AddressesModel.StateId-1;

            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1, preference.getStringArray("country"));
            SP_Country.setAdapter(adapterCategories);
            SP_Country.setSelection(countrycode);

            SP_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    try {
                        String d = String.valueOf(SP_Country.getSelectedItem());
                        Log.d(TAG, "onItemSelected: " + d);
                        ArrayAdapter<String> adapterCategories2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, preference.getstatename(d));
                        SP_State.setAdapter(adapterCategories2);
                        statecode = preference.getDefaultstate(registration.AddressesModel.StateName);
                        SP_State.setSelection(statecode);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {

                }
            });
*/
            backArrow = view.findViewById(R.id.back);

            AndroidNetworking.initialize(getActivity());
            Fragment_Driver_Profile_4.context = getActivity();

            edt_ExDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.getMonthYearDialog(new OnStringListner() {
                        @Override
                        public void getString(String string) {
                            edt_ExDate.setText(string);
                        }
                    });
                }
            });
            backArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                 /*   Bundle Registration = new Bundle();
                    Registration.putBundle("RegistrationBundle", RegistrationBundle);
                    System.out.println(Registration);
                    NavHostFragment.findNavController(Fragment_Driver_Profile_4.this)
                            .navigate(R.id.action_DriverProfile4_to_DriverProfile3,Registration);*/

                    NavHostFragment.findNavController(Fragment_Driver_Profile_4.this).popBackStack();
                }
            });
            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Driver_Profile_4.this)
                            .navigate(R.id.action_DriverProfile4_to_CreateProfile);
                }
            });
            ImageView imgScanDrivingLicense = view.findViewById(R.id.ScancreditCard);

            imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                    i.putExtra("afterScanBackTo", 1);
                    startActivity(i);

                }
            });

            edt_streetName.setOnClickListener(new View.OnClickListener()
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
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                        startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            lblnext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {

                        for (int i = 0; i < ImageList.length(); i++)
                        {
                            try {
                                System.out.println(i);

                                JSONObject imgObj = ImageList.getJSONObject(i);

                                String imgPath = imgObj.getString("fileBase64");

                                File imgFile = new File(imgPath);
                                Uri selectedImage = Uri.fromFile(imgFile);

                                bitmap = getScaledBitmap(selectedImage, 400, 400);
                                Bitmap temp = bitmap;

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                temp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                byte[] image = stream.toByteArray();
                                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                                imgObj.put("fileBase64", img_str_base64);
                                ImageList.put(imgObj);

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        if (edt_cardNo.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Credit Card No.",1);
                        else if (edt_ExDate.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Credit Card Expiry Date",1);
                        else if (edt_CvvNo.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your CVV",1);
                        else if (edt_cardholderName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Credit Card Holder Name",1);
                        else if (edt_streetName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Street NO & Name",1);
                        else if (SP_Country.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your Country",1);
                        else if (SP_State.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your State",1);
                        else if (Edtext_City.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your City",1);
                        else if (edtPincodeNo.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
                        else {
                            JSONObject bodyParam = new JSONObject();
                            try {
                               /* bodyParam.accumulate("Cust_FName", RegistrationBundle.getString("Cust_FName"));
                                bodyParam.accumulate("Cust_Street", RegistrationBundle.getString("Cust_Street"));
                                bodyParam.accumulate("Cust_UnitNo", RegistrationBundle.getString("Cust_UnitNo"));
                                bodyParam.accumulate("Cust_City", RegistrationBundle.getString("Cust_City"));
                                bodyParam.accumulate("Cust_State_ID", RegistrationBundle.getInt("Cust_State_ID"));
                                bodyParam.accumulate("Cust_Country_ID", RegistrationBundle.getInt("Cust_Country_ID"));
                                bodyParam.accumulate("Cust_Country_Name", RegistrationBundle.getString("Cust_Country_Name"));
                                bodyParam.accumulate("Cust_State_Name", RegistrationBundle.getString("Cust_State_Name"));
                                bodyParam.accumulate("Cust_ZipCode", RegistrationBundle.getString("Cust_ZipCode"));
                                bodyParam.accumulate("Cust_DOB", RegistrationBundle.getString("Cust_DOB"));
                                bodyParam.accumulate("Cust_Email", RegistrationBundle.getString("Cust_Email"));
                                bodyParam.accumulate("Cust_MobileNo", RegistrationBundle.getString("Cust_MobileNo"));

                                bodyParam.accumulate("Licence_No", RegistrationBundle.getString("Licence_No"));
                                bodyParam.accumulate("LIssue_Date", RegistrationBundle.getString("LIssue_Date"));
                                bodyParam.accumulate("LExpiry_Date", RegistrationBundle.getString("LExpiry_Date"));
                                bodyParam.accumulate("LIssue_By", RegistrationBundle.getString("LIssue_By"));
                                bodyParam.accumulate("PasswordHash", RegistrationBundle.getString("PasswordHash"));*/



//                                bodyParam.accumulate("CustCard_PCountry", preference.stateToCountryCode.get(preference.getdata("Cust_Country_Name"))-1);
//                                bodyParam.accumulate("CustCard_PState", preference.stateToSateCode.get(preference.getdata("Cust_State_Name"))-1);
         /*                       bodyParam.accumulate("Card_No", edt_cardNo.getText().toString());
                                bodyParam.accumulate("Card_Name", edt_cardholderName.getText().toString());
                                bodyParam.accumulate("CVS_Code", edt_CvvNo.getText().toString());
                                bodyParam.accumulate("CustCard_ExpiryDate", edt_ExDate.getText().toString());
                                bodyParam.accumulate("CustCard_PStreet", edt_streetName.getText().toString());
                                bodyParam.accumulate("CustCard_PUnitNo", edtxtUnitNumber.getText().toString());
                                bodyParam.accumulate("CustCard_PCity", Edtext_City.getText().toString());
                                bodyParam.accumulate("CustCard_Default", 1);

                                bodyParam.accumulate("ImageList", ImageList);*/

                                System.out.println(bodyParam);

                                creditCardModel.Number =  edt_cardNo.getText().toString();
                                creditCardModel.NameOn = edt_cardholderName.getText().toString();
                                creditCardModel.CVVCode = Integer.parseInt(edt_CvvNo.getText().toString());
                                String[] card_expdate =  edt_ExDate.getText().toString().split("/");
                                creditCardModel.ExpiryMonth =  Integer.parseInt(card_expdate[0]);
                                creditCardModel.ExpiryYear = Integer.parseInt(card_expdate[1]);
                                creditCardModel.ZipCode =edtPincodeNo.getText().toString();
                                creditCardModel.CardCompany =GetCreditCardType(edt_cardNo.getText().toString());
                                creditCardModel.IsFirstInsert = true;
                                creditCardModel.IsDefault = true;
                                creditCardModel.CreditCardType = 3;
                               // Fragment_Driver_Profile_1.addressesModel;
                                //Fragment_Driver_Profile_2.drivingLicenceModel;
                                Fragment_Driver_Profile_3.doRegistration.DrivingLicenceModel =  Fragment_Driver_Profile_2.drivingLicenceModel;
                                Fragment_Driver_Profile_3.doRegistration.UserModel = Fragment_Driver_Profile_3.userModel;
                                Fragment_Driver_Profile_3.doRegistration.AddressesModel =  Fragment_Driver_Profile_1.addressesModel;
                                //Fragment_Driver_Profile_3.doRegistration.CreditCardModel = creditCardModel;

                                registration.CreditCardModel = creditCardModel;
                               // registration.CustomerType = UserTypes.Customer.inte;
                                registration.CustomerType = 3;

                             /*   try {
                                    registration.CompanyId = UserData.companyModel.CompanyId;
                                } catch (Exception e){
                                    e.printStackTrace();
                                }*/

                         /*       ApiService2<DoRegistration> apiService2 =
                                        new ApiService2<DoRegistration>(Registration,
                                                RequestType.POST, INSERT, BASE_URL_CUSTOMER,header,Fragment_Driver_Profile_3.doRegistration);*/


                                RegistrationBundle.putString("Cust_Email",registration.Email);
                                RegistrationBundle.putString("PasswordHash",registration.UserModel.Password);

                                Log.d(TAG, "onClick: " + registration.Email);
                                lblnext.setClickable(false);
                                ApiService2<DoRegistration> apiService2 = new ApiService2<DoRegistration>(Registration,
                                                RequestType.POST, INSERT, BASE_URL_CUSTOMER,header,registration);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            /*ApiService ApiService = new ApiService(Registration, RequestType.POST,
                                    REGISTRATION, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*/
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });




        }catch (Exception e)
        {
            e.printStackTrace();
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

        if (height > reqHeight || width > reqWidth)
        {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //REGISTRATION
    OnResponseListener Registration = new OnResponseListener()
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

                           String transactionId= responseJSON.getString("Data");
                            Bundle Registration = new Bundle();

                            Registration.putBundle("RegistrationBundle", RegistrationBundle);
                            Registration.putString("transactionId",transactionId);

                            System.out.println(Registration);
                            NavHostFragment.findNavController(Fragment_Driver_Profile_4.this)
                                    .navigate(R.id.action_DriverProfile4_to_Complete_Register,Registration);
                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                            lblnext.setClickable(true);
                        }
                    }
                    catch (Exception e)
                    {
                        lblnext.setClickable(true);
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
            lblnext.setClickable(true);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

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

                    edt_streetName.setText(Street);
                    Edtext_City.setText(city);
                    edtPincodeNo.setText(postalCode);
                    edtxtUnitNumber.setText(UnitNo);

                    for(int j=0;j<Country.length;j++)
                    {

                        if(Country[j].equals(country))
                        {
                            SP_Country.setSelection(j);
                            break;
                        }
                    }
                    for(int i=0;i<State.length;i++)
                    {

                        if(State[i].equals(state))
                        {
                            SP_State.setSelection(i);
                            break;
                        }
                    }



                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                //setMarker(latLng);
            }
        }
    }

     public String GetCreditCardType(String CreditCardNumber)
    {
        CreditCardNumber = CreditCardNumber.replace(" ", "");
        Regex regVisa = new Regex("^4[0-9]{12}(?:[0-9]{3})?$");
        Regex regMaster = new Regex("^5[1-5][0-9]{14}$");
        Regex regExpress = new Regex("^3[47][0-9]{13}$");
        Regex regDiners = new Regex("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
        Regex regDiscover = new Regex("^6(?:011|5[0-9]{2})[0-9]{12}$");
        Regex regJCB = new Regex("^(?:2131|1800|35\\d{3})\\d{11}$");
 /* VISA: /^4[0-9]{12}(?:[0-9]{3})?$/,
    MASTER: /^5[1-5][0-9]{14}$/,
    AMEX: /^3[47][0-9]{13}$/,
    ELO: /^((((636368)|(438935)|(504175)|(451416)|(636297))\d{0,10})|((5067)|(4576)|(4011))\d{0,12})$/,
    AURA: /^(5078\d{2})(\d{2})(\d{11})$/,
    JCB: /^(?:2131|1800|35\d{3})\d{11}$/,
    DINERS: /^3(?:0[0-5]|[68][0-9])[0-9]{11}$/,
    DISCOVERY: /^6(?:011|5[0-9]{2})[0-9]{12}$/,
    HIPERCARD: /^(606282\d{10}(\d{3})?)|(3841\d{15})$/,
    ELECTRON: /^(4026|417500|4405|4508|4844|4913|4917)\d+$/,
    MAESTRO: /^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\d+$/,
    DANKORT: /^(5019)\d+$/,
    INTERPAYMENT: /^(636)\d+$/,
    UNIONPAY: /^(62|88)\d+$/,*/

        if (regVisa.matches(CreditCardNumber))
            return "VISA";
        else if (regMaster.matches(CreditCardNumber))
            return "MASTER";
        else  if (regExpress.matches(CreditCardNumber))
            return "AEXPRESS";
        else if (regDiners.matches(CreditCardNumber))
            return "DINERS";
        else if (regDiscover.matches(CreditCardNumber))
            return "DISCOVERS";
        else if (regJCB.matches(CreditCardNumber))
            return "JCB";
        else
            return "invalid";
    }

}