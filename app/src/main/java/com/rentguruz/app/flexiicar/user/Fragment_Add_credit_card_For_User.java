package com.rentguruz.app.flexiicar.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAddCreditcardBinding;
import com.rentguruz.app.databinding.FragmentUpdateCreditcardBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.CreditCardModel;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.ScanDrivingLicense;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kotlin.text.Regex;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.ADDCARD;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;

public class Fragment_Add_credit_card_For_User extends BaseFragment
{
   // String TAG = "Fragment_Add_credit_card_For_User";
   // ImageView imgback,ScanCreditcard, cardimage;
   // EditText edt_Card_No, edt_CVV, edt_NameofCard,edtStreetNo,edtUnitNo,edtCityName,edt_Zipcode;
   // Spinner Sp_CountryList,SP_StateList;
   // LinearLayout AddCreditCardLayout;
   // TextView txtDiscard, edt_ExpiryDate;
   // Handler handler = new Handler();
   // public static Context context;
    public String id = "";
    public String[] Country,State;
    public int[] CountyId,StateId;
    HashMap<String, Integer> countryhashmap=new HashMap<String, Integer>();
    HashMap<String,Integer>Statehashmap=new HashMap<>();
    private Geocoder geocoder;
    int backTo;
    //CheckBox chk_DeleteCard, chk_DefaultCard;
    private final int REQUEST_PLACE_ADDRESS = 40;
    int cust_State_ID,cust_Country_ID;
    int statecode=0, countrycode=0;
    //private DoHeader header;
    //CustomPreference preference;
    //LoginRes loginRes;
    //CustomerProfile loginResponse;
    //CustomerProfile customerProfile;
    static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;

    CustomeDialog dialog;
    FragmentAddCreditcardBinding binding;

    CreditCardModel creditCardModel = new CreditCardModel();
    CustomerProfile customerProfile = new CustomerProfile();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialog = new CustomeDialog(getContext());

        binding = FragmentAddCreditcardBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        if (Helper.isActiveCustomer){
            //((User_Profile)getActivity()).BottomnavInVisible();
            if (getArguments().getInt("frag")!=2){
               //   ((User_Profile)getActivity()).BottomnavInVisible();
            } else {
                ((User_Profile)getActivity()).BottomnavInVisible();
            }
        }

        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        backTo = getArguments().getInt("backTo");
        Log.d(TAG, "onViewCreated: " + backTo);
        customerProfile = UserData.customerProfile;
        creditCardModel.IsFirstInsert = false;
        creditCardModel.CreditCardFor=UserData.loginResponse.User.UserFor;
        creditCardModel.CreditCardType = 3;
        creditCardModel.IsDefault = true;

        creditCardModel.AddressesModel = UserData.loginResponse.LogedInCustomer.AddressesModel;
        creditCardModel.NameOn = UserData.loginResponse.LogedInCustomer.FullName;
        userDraw.checkbtn(binding.DefaultCard);
        binding.DefaultCard.setChecked(true);
      /*  try {
            if (UserData.loginResponse.User.UserFor == 0){

            }
        } catch (Exception e){
            e.printStackTrace();
        }*/


        try {
            preference.stateCountry( binding.spinnerCountryList, binding.SpinnerStatelist, creditCardModel.AddressesModel.CountryName, creditCardModel.AddressesModel.StateName);
        } catch (Exception e){
            e.printStackTrace();
            preference.stateCountry( binding.spinnerCountryList, binding.SpinnerStatelist, "", "");
        }
        binding.header.screenHeader.setText("Add Credit Card");
        binding.scanCreditcard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.edtStreetNoName.setOnClickListener(this);
        binding.edtExpiryDateAdd.setOnClickListener(this);
        binding.lblSaveCardAdd.setOnClickListener(this);
        binding.setCreditCard(creditCardModel);
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        cust_State_ID = sp.getInt("cmp_State",0);
        cust_Country_ID = sp.getInt("cmp_Country",0);
        System.out.println(cust_State_ID);
        char space = ' ';
        binding.edtCardNoAdd.addTextChangedListener(new TextWatcher() {
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
                binding.txtCardNumberAdd.setText(s);
                if (s.length()==19){
                  GetCreditCardType(binding.edtCardNoAdd.getText().toString().trim());
                }
            }
        });
        binding.edtExpiryDateAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.txtCardExpiryDateAdd.setText(s);
            }
        });
        binding.edtNameofCardAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.txtCardholderNameAdd.setText(s);
            }
        });

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

    OnResponseListener AddCreditCard = new OnResponseListener()
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
                        fullProgressbar.hide();
                        NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this).popBackStack();
                    }
                    else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                        fullProgressbar.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

        @Override
        public void onError(String error)
        {
            System.out.println("Error" + error);
        }
    };


    public void GetCreditCardType(String CreditCardNumber)
    {
        CreditCardNumber = CreditCardNumber.replace(" ", "");
        Regex regVisa = new Regex("^4[0-9]{12}(?:[0-9]{3})?$");
        Regex regMaster = new Regex("^5[1-5][0-9]{14}$");
        Regex regExpress = new Regex("^3[47][0-9]{13}$");
        Regex regDiners = new Regex("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
        Regex regDiscover = new Regex("^6(?:011|5[0-9]{2})[0-9]{12}$");
        Regex regJCB = new Regex("^(?:2131|1800|35\\d{3})\\d{11}$");
/*  VISA: /^4[0-9]{12}(?:[0-9]{3})?$/,
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
            binding.cardImage.setImageDrawable(getResources().getDrawable(R.drawable.visa));
        else if (regMaster.matches(CreditCardNumber))
            binding.cardImage.setImageDrawable(getResources().getDrawable(R.drawable.mastercard));
        else  if (regExpress.matches(CreditCardNumber))
            binding.cardImage.setImageDrawable(getResources().getDrawable(R.drawable.americanexpress));
 /*       else if (regDiners.matches(CreditCardNumber))
            return "DINERS";
        else if (regDiscover.matches(CreditCardNumber))
            return "DISCOVERS";
        else if (regJCB.matches(CreditCardNumber))
            return "JCB";*/
        else
            binding.cardImage.setImageDrawable(getResources().getDrawable(R.drawable.visa));
    }

   /* public String GetCreditCardType(String CreditCardNumber)
    {
        CreditCardNumber = CreditCardNumber.replace(" ", "");
        Regex regVisa = new Regex("^4[0-9]{12}(?:[0-9]{3})?$");
        Regex regMaster = new Regex("^5[1-5][0-9]{14}$");
        Regex regExpress = new Regex("^3[47][0-9]{13}$");
        Regex regDiners = new Regex("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
        Regex regDiscover = new Regex("^6(?:011|5[0-9]{2})[0-9]{12}$");
        Regex regJCB = new Regex("^(?:2131|1800|35\\d{3})\\d{11}$");
*//*  VISA: /^4[0-9]{12}(?:[0-9]{3})?$/,
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
    UNIONPAY: /^(62|88)\d+$/,*//*

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
    }*/



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

                    binding.edtStreetNoName.setText(Street);
                    binding.EdtextCity.setText(city);
                    binding.edtPincodeNo.setText(postalCode);
                    binding.edtxtUnitNumber.setText(UnitNo);

                /*    for(int i=0;i<State.length;i++)
                    {

                        if(State[i].equals(state))
                        {
                            SP_StateList.setSelection(i);
                            break;
                        }
                    }

                    for(int j=0;j<Country.length;j++)
                    {

                        if(Country[j].equals(country))
                        {
                            Sp_CountryList.setSelection(j);
                            break;
                        }
                    }*/

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //setMarker(latLng);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanCreditcard:
                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                i.putExtra("afterScanBackTo", 2);
                startActivity(i);
                break;
            case R.id.back:

            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this).popBackStack();
             /*   if(backTo == 1)
                {
                    bundle.putInt("backTo",1);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }
                //TollCharge_Image
                else if(backTo == 2)
                {
                    Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",PaymentBundle);
                    bundle.putInt("backTo",2);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }
                //Payment_Reciept_2
                else if(backTo == 3)
                {
                    Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",PaymentBundle);
                    bundle.putInt("backTo",3);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);

                }
                //Invoice_Image
                else if(backTo == 4)
                {
                    Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",PaymentBundle);
                    bundle.putInt("backTo",4);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }
                //Traffic_Ticket_Image
                else if(backTo == 5)
                {
                    Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",PaymentBundle);
                    bundle.putInt("backTo",5);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }
                else if(backTo == 6)
                {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");
                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putBundle("ReservationBundle",ReservationBundle);
                    bundle.putInt("backTo",6);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }
                else if(backTo ==7)
                {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putBundle("AgreementsBundle",AgreementsBundle);
                    bundle.putInt("backTo",7);
                    NavHostFragment.findNavController(Fragment_Add_credit_card_For_User.this)
                            .navigate(R.id.action_AddCreditCard_to_CardsOnAccount,bundle);
                }*/
                break;
            case R.id.edt_streetNoName:
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

            case R.id.edt_ExpiryDateAdd:
                dialog.getMonthYearDialog(string -> binding.edtExpiryDateAdd.setText(string));
                break;

            case R.id.lblSaveCardAdd:
                if (validation()){
                    fullProgressbar.show();
                    creditCardModel = binding.getCreditCard();
                    creditCardModel.AddressesModel = customerProfile.AddressesModel;
                   // creditCardModel.AddressesModel.Fname = creditCardModel.NameOn.split(" ")[0];
                   // creditCardModel.AddressesModel.Lname = creditCardModel.NameOn.split(" ")[1];
                    creditCardModel.CardCompany = GetCreditCard(binding.edtCardNoAdd.getText().toString());
                    creditCardModel.Number = binding.edtCardNoAdd.getText().toString();
                    creditCardModel.NameOn = binding.edtNameofCardAdd.getText().toString();
                    creditCardModel.ExpiryMonth = Integer.valueOf(binding.edtExpiryDateAdd.getText().toString().split("/")[0]);
                    creditCardModel.ExpiryYear = Integer.valueOf(binding.edtExpiryDateAdd.getText().toString().split("/")[1]);
                    creditCardModel.CVVCode = Integer.valueOf(binding.edtCVVAdd.getText().toString());
                    creditCardModel.ZipCode = binding.edtPincodeNo.getText().toString();
                    if(binding.DefaultCard.isChecked())
                        creditCardModel.IsDefault = true;
                    else
                        creditCardModel.IsDefault = false;
                    Log.d(TAG, "onClick: ");
                    ApiService2<CreditCardModel> apiService2 = new ApiService2<CreditCardModel>(AddCreditCard, RequestType.POST,
                            ADDCARD, BASE_URL_CUSTOMER, header, creditCardModel);
                }
                break;
        }
    }

    public Boolean validation(){
        Boolean value = false;
        if (binding.edtCardNoAdd.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter CardNumber",1);
        else if (binding.edtExpiryDateAdd.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter ExpiryDate",1);
        else if (binding.edtCVVAdd.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter CVV",1);
        else if (binding.edtNameofCardAdd.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Card Holder Name",1);
        else if (binding.edtPincodeNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
        else {
            value =true;
        }
        return value;
    }

    public String GetCreditCard(String CreditCardNumber)
    {
        CreditCardNumber = CreditCardNumber.replace(" ", "");
        Regex regVisa = new Regex("^4[0-9]{12}(?:[0-9]{3})?$");
        Regex regMaster = new Regex("^5[1-5][0-9]{14}$");
        Regex regExpress = new Regex("^3[47][0-9]{13}$");
        Regex regDiners = new Regex("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
        Regex regDiscover = new Regex("^6(?:011|5[0-9]{2})[0-9]{12}$");
        Regex regJCB = new Regex("^(?:2131|1800|35\\d{3})\\d{11}$");
/*  VISA: /^4[0-9]{12}(?:[0-9]{3})?$/,
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




