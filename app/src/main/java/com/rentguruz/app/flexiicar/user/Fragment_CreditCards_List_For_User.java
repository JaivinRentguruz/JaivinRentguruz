package com.rentguruz.app.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CreditCard;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCreditcardsListBinding;
import com.rentguruz.app.databinding.ListCreditCardBinding;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.CreditCardModel;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.ReservationTimeModel;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETALLCARD;

public class Fragment_CreditCards_List_For_User extends BaseFragment
{
    public String id = "";
    int backTo;
    LoginResponse loginResponse;
    CreditCardModel creditCardModel;
    FragmentCreditcardsListBinding binding;
    Bundle cardBundle;
    CreditCard card;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        loginResponse = new LoginResponse();
        cardBundle = new Bundle();
        binding = FragmentCreditcardsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        card = new CreditCard(getContext());

        loginResponse = loginRes.callFriend("Data", LoginResponse.class);
        backTo = getArguments().getInt("backTo");
        if (backTo==0)
            backTo = 1;
        Log.d(TAG, "onViewCreated: " + backTo);

        if (getArguments().getInt("frag")!=2){
            //((User_Profile)getActivity()).BottomnavVisible();
        }

        try {
            ((User_Profile)getActivity()).BottomnavVisible();
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText(getActivity().getResources().getString(R.string.cards_on_account));
        binding.header.discard.setText(getActivity().getResources().getString(R.string.add));
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

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

        loginResponse.User.UserFor =  UserData.loginResponse.User.UserFor;

        try {
            bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
            bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
            bundle.putSerializable("models", (LocationList) getArguments().getSerializable("models"));
            bundle.putSerializable("model", (LocationList) getArguments().getSerializable("model"));
            bundle.putSerializable("timemodel", (ReservationTimeModel) getArguments().getSerializable("timemodel"));
            bundle.putSerializable("customer", (Customer) getArguments().getSerializable("customer"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime", getArguments().getString("pickuptime"));
            bundle.putString("transactionId",getArguments().getString("transactionId"));
            bundle.putString("netrate", getArguments().getString("netrate"));
            bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));
            Customer customer = new Customer();
            customer = (Customer) getArguments().getSerializable("customer");
            loginResponse.User.UserFor = customer.Id;
            bundle.putInt("frag",2);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            bundle.putBoolean("popback", getArguments().getBoolean("popback"));
        } catch (Exception e){
            e.printStackTrace();
        }
        apiService = new ApiService(GetCreditcardList, RequestType.POST,
                GETALLCARD, BASE_URL_LOGIN, header, params.getCreditCardList(loginResponse.User.UserFor));


}

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener GetCreditcardList = new OnResponseListener()
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
                        try
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray getCardDEtails = resultSet.getJSONArray("Data");

                            List<CreditCardModel> creditCardModels = new ArrayList<>();

                            final RelativeLayout rlCreditcard = getActivity().findViewById(R.id.rl_cards);

                            int len;
                            len = getCardDEtails.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getCardDEtails.get(j);
                                creditCardModel = new CreditCardModel();
                                loginRes.storedata("UpdateCreditCard", test.toString());
                                creditCardModel = loginRes.callFriend("UpdateCreditCard", CreditCardModel.class);
                                creditCardModels.add(creditCardModel);
                            }
                            showCards(creditCardModels);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };


    public void showCards( List<CreditCardModel> creditCardModels){
        //final RelativeLayout rlCreditcard = getActivity().findViewById(R.id.rl_cards);

        for (int i = 0; i <creditCardModels.size() ; i++) {

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.setMargins(0, 0, 0, 0);

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

           ListCreditCardBinding listCreditCardBinding = ListCreditCardBinding.inflate(inflater,
                   getActivity().findViewById(android.R.id.content), false);
            listCreditCardBinding.notifyChange();
            listCreditCardBinding.setCreditCard(creditCardModels.get(i));
          //  listCreditCardBinding.cardImage.setImageDrawable(getResources().getDrawable(R.drawable.visa));
            card.GetCreditCardType(listCreditCardBinding.cardImage,creditCardModels.get(i).Number);
            int finalI = i;
            if (getArguments().getInt("frag")==2){
                listCreditCardBinding.creditcardLayout.setOnClickListener(v -> {
                    try {

                    UserData.activepmt = creditCardModels.get(finalI);
                    bundle.putSerializable("defaultcard",creditCardModels.get(finalI));

                    if (getArguments().getBoolean("popback")){
                        Helper.selectCardforpmt = true;
                        NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this).popBackStack();
                    } else {
                        NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                                .navigate(R.id.action_CardsOnAccount_to_Payment_checkout, bundle);
                    }

                    } catch (IllegalStateException e){
                        e.printStackTrace();
                    } catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                });
            }

            listCreditCardBinding.editcard1.setOnClickListener(view -> {

                bundle.putSerializable("CreditCardUpdate",creditCardModel);
                bundle.putInt("Id", creditCardModels.get(finalI).Id);
                UserData.UpdateCreditCard = creditCardModel;

                NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                        .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
             /*   if(backTo == 1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("backTo",1);
                    bundle.putInt("Id", creditCardModels.get(finalI).Id);
                    bundle.putInt("FOR", creditCardModel.CreditCardFor);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }
                //TollCharge_Image
                else if(backTo == 2)
                {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putInt("backTo",2);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }
                //Payment_Reciept_2
                else if(backTo == 3)
                {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putInt("backTo",3);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);

                }
                //Invoice_Image
                else if(backTo == 4)
                {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putInt("backTo",4);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }
                //Traffic_Ticket_Image
                else if(backTo == 5)
                {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putInt("backTo",5);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }
                else if(backTo == 6)
                {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putBundle("ReservationBundle",ReservationBundle);
                    bundle.putInt("backTo",6);
                    bundle.putBundle("CardBundle", CardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }
                else if(backTo ==7)
                {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putBundle("AgreementsBundle",AgreementsBundle);
                    bundle.putInt("backTo",7);
                    bundle.putBundle("CardBundle", CardBundle);
                    bundle.putSerializable("Card", creditCardModel);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_UpdateCreditCard,bundle);
                }*/
            });

           /* binding.getRoot().setOnClickListener(view -> {
                if(backTo == 2)
                {

                    Bundle bundle = new Bundle();
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putString("creditcard", creditCardModels.get(finalI).NameOn);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_TollCharge_Image,bundle);
                }
                if(backTo == 3)
                {
                    Bundle bundle = new Bundle();
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putString("creditcard", creditCardModels.get(finalI).NameOn);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Payment_Reciept_2,bundle);
                }
                //Invoice_Image
                if(backTo == 4)
                {
                    Bundle bundle = new Bundle();
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putString("creditcard", creditCardModels.get(finalI).NameOn);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Invoice_Image,bundle);
                }
                //Traffic_Ticket_Image
                if(backTo == 5)
                {
                    Bundle bundle = new Bundle();
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    bundle.putBundle("PaymentBundle",cardBundle);
                    bundle.putString("creditcard", creditCardModels.get(finalI).NameOn);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Traffic_Ticket_Image,bundle);
                }

                if(backTo == 6)
                {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");
                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putString("creditcard",creditCardModels.get(finalI).NameOn);
                    bundle.putBundle("ReservationBundle",ReservationBundle);

                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Payment_checkout,bundle);
                }
                else if(backTo ==7)
                {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                    bundle.putString("total",total);
                    bundle.putString("creditcard", creditCardModels.get(finalI).NameOn);
                    bundle.putBundle("AgreementsBundle",AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_PaymentCheckOutSelfCheckIn,bundle);
                }
            });*/

            listCreditCardBinding.getRoot().setId(200 + i);
            listCreditCardBinding.getRoot().setLayoutParams(lp);
            binding.rlCards.addView(listCreditCardBinding.getRoot());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
            /*    if (backTo == 1) {
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_User_Details);
                }
                //TollCharge_Image
                else if (backTo == 2) {
                    cardBundle = getArguments().getBundle("PaymentBundle");

                    bundle.putBundle("PaymentBundle", cardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_TollCharge_Image, bundle);
                }
                //Payment_Reciept_2
                else if (backTo == 3) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Payment_Reciept_2, bundle);

                }
                //Invoice_Image
                else if (backTo == 4) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Invoice_Image, bundle);
                }
                //Traffic_Ticket_Image
                else if (backTo == 5) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Traffic_Ticket_Image, bundle);
                }

                else if (backTo == 6) {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId", transactionId);
                    bundle.putString("total", total);
                    bundle.putBundle("ReservationBundle", ReservationBundle);

                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_Payment_checkout, bundle);
                }

                else if (backTo == 7) {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId", transactionId);
                    bundle.putString("total", total);
                    bundle.putBundle("AgreementsBundle", AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_PaymentCheckOutSelfCheckIn, bundle);
                }*/

                NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this).popBackStack();
                break;

            case R.id.discard:
                bundle.putInt("backTo", 1);
                NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                        .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);

            /*    if (backTo == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("backTo", 1);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                }
                //TollCharge_Image
                else if (backTo == 2) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    bundle.putInt("backTo", 2);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                }
                //Payment_Reciept_2
                else if (backTo == 3) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    bundle.putInt("backTo", 3);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);

                }
                //Invoice_Image
                else if (backTo == 4) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    bundle.putInt("backTo", 4);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                }
                //Traffic_Ticket_Image
                else if (backTo == 5) {
                    cardBundle = getArguments().getBundle("PaymentBundle");
                    Bundle bundle = new Bundle();
                    bundle.putBundle("PaymentBundle", cardBundle);
                    bundle.putInt("backTo", 5);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                } else if (backTo == 6) {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId", transactionId);
                    bundle.putString("total", total);
                    bundle.putBundle("ReservationBundle", ReservationBundle);
                    bundle.putInt("backTo", 6);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                } else if (backTo == 7) {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");

                    String transactionId = getArguments().getString("transactionId");
                    String total = getArguments().getString("total");

                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId", transactionId);
                    bundle.putString("total", total);
                    bundle.putBundle("AgreementsBundle", AgreementsBundle);
                    bundle.putInt("backTo", 7);
                    NavHostFragment.findNavController(Fragment_CreditCards_List_For_User.this)
                            .navigate(R.id.action_CardsOnAccount_to_AddCreditCard, bundle);
                }*/
                break;

        }
    }
}