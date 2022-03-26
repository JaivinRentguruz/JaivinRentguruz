package com.abel.app.b2b.flexiicar.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentUpdateCreditcardBinding;
import com.abel.app.b2b.model.CreditCardModel;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.model.response.UpdateCard;
import com.abel.app.b2b.R;
import com.abel.app.b2b.ScanDrivingLicense;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.adapters.OnStringListner;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
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

import static android.content.Context.MODE_PRIVATE;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.DELETE;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETBYIDCARD;
import static com.abel.app.b2b.apicall.ApiEndPoint.TableType;
import static com.abel.app.b2b.apicall.ApiEndPoint.UPDATECARD;

public class Fragment_Update_CreditCard_For_user extends BaseFragment
{
   // String TAG = "Fragment_Update_CreditCard_For_user";
   // ImageView imgback,ScanCreditCard;
   // EditText edt_Card_No, edt_CVV, edt_NameofCard, edtStreetNo,edtUnitNo,edtCityName,edt_Pincode;
   // Spinner Sp_CountryList,SP_StateList;
   // TextView txt_CardNumber, txt_CardholderName, txt_cardExpiryDate,txtDiscard, edt_ExpiryDate;
   // CheckBox chk_DeleteCard, chk_DefaultCard;
    Bundle CreditCardBundle;
   // LinearLayout Save;
   // Handler handler = new Handler();
   // public static Context context;
    public String id = "";
    public String[] Country,State;
    public int[] CountyId,StateId;
    int backTo;
    int card_PCountry,card_PState;
    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    int statecode=0, countrycode=0;
    CreditCardModel creditCardModel;
    private UpdateCard updateCard;
    int ids,fors;
    CustomeDialog dialog;
    FragmentUpdateCreditcardBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        creditCardModel = new CreditCardModel();
        updateCard = new UpdateCard();
        dialog = new CustomeDialog(getContext());
        binding = FragmentUpdateCreditcardBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getInt("frag")!=2){
            //((User_Profile)getActivity()).BottomnavInVisible();
        }
        if (Helper.isActiveCustomer){
            ((User_Profile)getActivity()).BottomnavInVisible();
        }
        try {
            ((User_Profile)getActivity()).BottomnavInVisible();
        } catch (Exception e){
            e.printStackTrace();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        backTo = getArguments().getInt("backTo");
        ids  = getArguments().getInt("Id");
        fors  = getArguments().getInt("FOR");
        Log.d(TAG, "onViewCreated: " + backTo);
        CreditCardBundle = getArguments().getBundle("CardBundle");
        //creditCardModel = (CreditCardModel) getArguments().getSerializable("CardBundle");


        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.header.screenHeader.setText(getResources().getString(R.string.creditcardupdate));
        binding.edtExpiryDate.setOnClickListener(this);
        binding.edtStreetNumandName.setOnClickListener(this);
        binding.ScanCard.setOnClickListener(this);
        binding.lblSaveCard.setOnClickListener(this);
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");



       /* Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject bodyParam = new JSONObject();

                if(chk_DeleteCard.isChecked())
                {
                    if (edt_Card_No.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter CardNumber",1);
                    else if (edt_ExpiryDate.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter ExpiryDate",1);
                    else if (edt_CVV.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter CVV",1);
                    else if (edt_NameofCard.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Card Holder Name",1);
                    else if (edt_Pincode.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
                    else
                        {
                        try
                        {
                            bodyParam.accumulate("TableType", TableType);
                            bodyParam.accumulate("Id", creditCardModel.Id);
                            bodyParam.accumulate("IsActive", false);

                           // bodyParam.accumulate("Card_ID", CreditCardBundle.getInt("card_ID"));
                            ApiService ApiService1 = new ApiService(DeleteCreditCard, RequestType.POST,
                                    DELETECARD, BASE_URL_CUSTOMER, header, bodyParam);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    {
                    if (edt_Card_No.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter CardNumber",1);
                    else if (edt_ExpiryDate.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter ExpiryDate",1);
                    else if (edt_CVV.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter CVV",1);
                    else if (edt_NameofCard.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Card Holder Name",1);
                    else if (edt_Pincode.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
                    else
                    {
                        *//*     bodyParam.accumulate("Customer_ID",Integer.parseInt(id));
                             bodyParam.accumulate("Card_ID",CreditCardBundle.getInt("card_ID"));
                             bodyParam.accumulate("Card_Type_ID",CreditCardBundle.getString("card_Type_ID"));
                             bodyParam.accumulate("Card_No", edt_Card_No.getText().toString());
                             bodyParam.accumulate("Expiry_Date", edt_ExpiryDate.getText().toString());
                             bodyParam.accumulate("CVS_Code", edt_CVV.getText().toString());
                             bodyParam.accumulate("Card_Name", edt_NameofCard.getText().toString());
                             bodyParam.accumulate("Card_PStreet",edtStreetNo.getText().toString());
                             bodyParam.accumulate("Card_PCity",edtCityName.getText().toString());
                             bodyParam.accumulate("Card_PUnitNo",edtUnitNo.getText().toString());
                             bodyParam.accumulate("ZipCode", edt_Pincode.getText().toString());
                             int s = countryhashmap.get(Sp_CountryList.getSelectedItem());
                             int s1=Statehashmap.get(SP_StateList.getSelectedItem());
                             bodyParam.accumulate("Card_PCountry",s);
                             bodyParam.accumulate("Card_PState", s1);*//*

                        creditCardModel.Number = edt_Card_No.getText().toString();
                        creditCardModel.CVVCode  = Integer.valueOf(edt_CVV.getText().toString());
                      *//*  creditCardModel.Id = ids;
                        creditCardModel.AddressesModel.Id = ids;
                        creditCardModel.isActive = true;
                        //creditCardModel.AddressesModel.Id = fors;
                        creditCardModel.CreditCardFor = fors;

                        creditCardModel.CreditCardType = 3;*//*

                        creditCardModel.IsFirstInsert = false;
                        creditCardModel.Number = edt_Card_No.getText().toString();
                        String[] card_expdate =  edt_ExpiryDate.getText().toString().split("/");

                        creditCardModel.ExpiryMonth = Integer.valueOf(card_expdate[0]);
                        creditCardModel.ExpiryYear = Integer.valueOf(card_expdate[1]);
                        creditCardModel.CVVCode =Integer.valueOf(edt_CVV.getText().toString());
                        creditCardModel.NameOn = edt_NameofCard.getText().toString();
                        creditCardModel.ZipCode = edt_Pincode.getText().toString();

                        creditCardModel.AddressesModel.Street = edtStreetNo.getText().toString();
                        creditCardModel.AddressesModel.UnitNo = edtUnitNo.getText().toString();
                        creditCardModel.AddressesModel.City = edtCityName.getText().toString();
                        creditCardModel.AddressesModel.ZipCode = edt_Pincode.getText().toString();
                        String country = String.valueOf(Sp_CountryList.getSelectedItem());
                        String state = String.valueOf(SP_StateList.getSelectedItem());
                        creditCardModel.AddressesModel.StateId = preference.stateToSateCode.get(state);
                        creditCardModel.AddressesModel.CountryId=preference.countryToCountryCode.get(country);

                        creditCardModel.Fname = edt_NameofCard.getText().toString();
                        creditCardModel.Lname = edt_NameofCard.getText().toString();

                       *//* updateCard.Id = loginResponse.User.UserFor;
                        updateCard.IsActive = true;
                        updateCard.creditCardModel=creditCardModel;
                        updateCard.creditCardModel.CreditCardFor = 0;
                        updateCard.creditCardModel.CreditCardType = 3;*//*

                        System.out.println(bodyParam);

                        if(chk_DefaultCard.isChecked())
                            //bodyParam.accumulate("IsDefault",1);
                            creditCardModel.IsDefault = true;
                        else
                            creditCardModel.IsDefault = false;
                        //    bodyParam.accumulate("IsDefault", 0);

                        System.out.println(bodyParam);

                     *//*   ApiService ApiService = new ApiService(UpdateCreditCard, RequestType.PUT,
                                UPDATECARD, BASE_URL_LOGIN,header, bodyParam);*//*
                        ApiService2<CreditCardModel> apiService2 = new ApiService2<CreditCardModel>(UpdateCreditCard, RequestType.PUT,
                                UPDATECARD, BASE_URL_LOGIN,header,creditCardModel);
                    }


                }
            }
        });*/




        apiService = new ApiService(getUserCredtiCard, RequestType.GET, GETBYIDCARD+"?Id="+ids, BASE_URL_LOGIN, header, new JSONObject());

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

    OnResponseListener getUserCredtiCard = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    JSONObject data = responseJSON.getJSONObject("Data");
                    if (status){
                       creditCardModel  = loginRes.getModel(data.toString(), CreditCardModel.class);
                     //  binding.setCreditCardModel(creditCardModel);

                        try {

                       if(creditCardModel.AddressesModel.AddressType != 67) {
                           try {
                               creditCardModel.AddressesModel = UserData.customerProfile.AddressesModel;
                               creditCardModel.AddressesModel.AddressType = 67;
                           } catch (Exception e){
                               e.printStackTrace();
                           }
                       }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                       binding.edtCardNo.setText(creditCardModel.Number);
                       binding.edtExpiryDate.setText(creditCardModel.ExpiryMonth +"/"+ creditCardModel.ExpiryYear);
                       binding.edtCVV.setText(String.valueOf( creditCardModel.CVVCode));
                       binding.edtNameofCard.setText(creditCardModel.NameOn);
                       binding.edtPincode.setText(creditCardModel.ZipCode);
                       binding.txtCardholderName.setText(creditCardModel.NameOn);
                       binding.txtCardExpiryDate.setText(creditCardModel.ExpiryMonth +"/"+ creditCardModel.ExpiryYear);
                       binding.txtCardNumber.setText(creditCardModel.Number);
                       binding.edtStreetNumandName.setText(creditCardModel.AddressesModel.Street);
                       binding.edtxtUnitNum.setText(creditCardModel.AddressesModel.UnitNo);
                       binding.EdtextCity.setText(creditCardModel.AddressesModel.City);
                       preference.stateCountry(binding.spCountryListForCreditcard, binding.SpinnerState,
                               creditCardModel.AddressesModel.CountryName, creditCardModel.AddressesModel.StateName);
                    } else
                    {
                        String msg = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),msg,1);
                    }


                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError:  " + error);
        }
    };

    OnResponseListener UpdateCreditCard = new OnResponseListener()
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
                        NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this).popBackStack();
                       /* if (backTo == 1)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putInt("backTo", 1);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //TollCharge_Image
                        else if (backTo == 2)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 2);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //Payment_Reciept_2
                        else if (backTo == 3)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 3);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);

                        }
                        //Invoice_Image
                        else if (backTo == 4)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 4);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //Traffic_Ticket_Image
                        else if (backTo == 5)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 5);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        } else if (backTo == 6)
                        {
                            Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");

                            String transactionId = getArguments().getString("transactionId");
                            String total = getArguments().getString("total");

                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId", transactionId);
                            bundle.putString("total", total);
                            bundle.putBundle("ReservationBundle", ReservationBundle);
                            bundle.putInt("backTo", 6);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        else if (backTo == 7)
                        {
                            Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                            String transactionId = getArguments().getString("transactionId");
                            String total = getArguments().getString("total");

                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId", transactionId);
                            bundle.putString("total", total);
                            bundle.putBundle("AgreementsBundle", AgreementsBundle);
                            bundle.putInt("backTo", 7);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }*/
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
            System.out.println("Error" + error);
        }
    };

    OnResponseListener DeleteCreditCard = new OnResponseListener()
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
                        NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this).popBackStack();
                        /*if (backTo == 1)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putInt("backTo", 1);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //TollCharge_Image
                        else if (backTo == 2)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 2);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //Payment_Reciept_2
                        else if (backTo == 3)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 3);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);

                        }
                        //Invoice_Image
                        else if (backTo == 4)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 4);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        //Traffic_Ticket_Image
                        else if (backTo == 5)
                        {
                            Bundle PaymentBundle = getArguments().getBundle("PaymentBundle");
                            Bundle bundle = new Bundle();
                            bundle.putBundle("PaymentBundle", PaymentBundle);
                            bundle.putInt("backTo", 5);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        } else if (backTo == 6)
                        {
                            Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");

                            String transactionId = getArguments().getString("transactionId");
                            String total = getArguments().getString("total");

                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId", transactionId);
                            bundle.putString("total", total);
                            bundle.putBundle("ReservationBundle", ReservationBundle);
                            bundle.putInt("backTo", 6);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }
                        else if (backTo == 7)
                        {
                            Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                            String transactionId = getArguments().getString("transactionId");
                            String total = getArguments().getString("total");

                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId", transactionId);
                            bundle.putString("total", total);
                            bundle.putBundle("AgreementsBundle", AgreementsBundle);
                            bundle.putInt("backTo", 7);
                            NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this)
                                    .navigate(R.id.action_UpdateCreditCard_to_CardsOnAccount, bundle);
                        }*/
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
            System.out.println("Error" + error);
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

                    binding.edtStreetNumandName.setText(Street);
                    binding.EdtextCity.setText(city);
                    binding.edtPincode.setText(postalCode);
                    binding.edtxtUnitNum.setText(UnitNo);
                    preference.stateCountry(binding.spCountryListForCreditcard, binding.SpinnerState,country, state);
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
            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_Update_CreditCard_For_user.this).popBackStack();
                break;
            case R.id.edt_ExpiryDate:
                dialog.getMonthYearDialog(string -> binding.edtExpiryDate.setText(string));
                break;
            case R.id.edt_streetNumandName:
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
            case R.id.ScanCard:
                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                i.putExtra("afterScanBackTo", 2);
                startActivity(i);
                break;
            case R.id.lblSaveCard:
                if (validation()){
                    if (binding.chkDeleteCard.isChecked()){
                       // apiService = new ApiService(DeleteCreditCard, RequestType.GET, GETBYIDCARD+"?Id="+ids, BASE_URL_LOGIN, header, new JSONObject());
                        apiService = new ApiService(DeleteCreditCard, RequestType.POST,
                                DELETE, BASE_URL_LOGIN, header, params.getDelete(TableType,ids));
                    } else {
                        creditCardModel.ExpiryMonth = Integer.parseInt(binding.edtExpiryDate.getText().toString().split("/")[0]);
                        creditCardModel.ExpiryYear = Integer.parseInt(binding.edtExpiryDate.getText().toString().split("/")[1]);
                        if(binding.chkDefaultCard.isChecked())
                            creditCardModel.IsDefault = true;
                        else
                            creditCardModel.IsDefault = false;
                        loginRes.testingLog(TAG,creditCardModel);
                        ApiService2<CreditCardModel> apiService2 = new ApiService2<CreditCardModel>(UpdateCreditCard, RequestType.PUT,
                                UPDATECARD, BASE_URL_LOGIN,header,creditCardModel);
                    }
                }
                break;
        }
    }

    public Boolean validation(){
    Boolean value = false;
        if (binding.edtCardNo.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter CardNumber",1);
        else if (binding.edtExpiryDate.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter ExpiryDate",1);
        else if (binding.edtCVV.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter CVV",1);
        else if (binding.edtNameofCard.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Card Holder Name",1);
        else if (binding.edtPincode.getText().toString().equals(""))
            CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
        else
            value = true;
        return value;
    }
}

