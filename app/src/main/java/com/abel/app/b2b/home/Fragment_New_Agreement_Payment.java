package com.abel.app.b2b.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.model.response.Reservation;
import com.bumptech.glide.Glide;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.ApiService2;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementPaymentBinding;
import com.abel.app.b2b.flexiicar.booking.Fragment_Payment_checkout;
import com.abel.app.b2b.model.CreditCardModel;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.parameter.PaymentMode;
import com.abel.app.b2b.model.parameter.PaymentProcess;
import com.abel.app.b2b.model.parameter.PaymentProcessMode;
import com.abel.app.b2b.model.parameter.PaymentTransactionType;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationPMT;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETCUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.RESERVATIONPMT;

public class Fragment_New_Agreement_Payment extends BaseFragment {

    FragmentNewAgreementPaymentBinding binding;
    ReservationPMT reservationPMT;
    ReservationSummarry reserversationSummary;
    Customer customer;
    List<ReservationPMT> pmtList = new ArrayList<>();
    String transactionId;
    CustomerProfile customerProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.screenHeader.setText(companyLabel.Payment);
        customerProfile = new CustomerProfile();
        reservationPMT = new ReservationPMT();
        reserversationSummary = new ReservationSummarry();
        customer = new Customer();
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
        bundle.putString("transactionId",transactionId);
        bundle.putString("netrate", getArguments().getString("netrate"));
        bundle.putSerializable("customerDetail",(CustomerProfile) getArguments().getSerializable("customerDetail"));

        reserversationSummary = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        customer = (Customer) getArguments().getSerializable("customer");
        customerProfile = (CustomerProfile) getArguments().getSerializable("customerDetail");

            VehicleModel  vehicleModel = new VehicleModel();
            vehicleModel = (VehicleModel) getArguments().getSerializable("Model");
            LocationList pickuplocation = new LocationList();
            pickuplocation = (LocationList) getArguments().getSerializable("model");

            LocationList returnlocation = new LocationList();
            returnlocation = (LocationList) getArguments().getSerializable("models");

            binding.textViewPickupLocation.setText(pickuplocation.Name);
            binding.textViewReturnLocationName.setText(returnlocation.Name);
            binding.textViewPickupLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
            binding.textViewReturnLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
            Glide.with(context).load(vehicleModel.DefaultImagePath).into(binding.carimage);
            binding.textVVehicleModelName.setText(vehicleModel.VehicleShortName);
            binding.textVVehicleTypeVName.setText(vehicleModel.VehicleCategory);
            Double amountPayable = Double.valueOf(getArguments().getString("netrate"));
            //txtAmtPayable.setText("$ "+((String.format(Locale.US,"%.2f",amountPayable))));
            binding.txtAmtPayable.setText(Helper.getAmtount(amountPayable,false));
            reservationPMT.Amount =Double.valueOf(getArguments().getString("netrate"));

        } catch (Exception e){
            e.printStackTrace();
        }
        reservationPMT.SplitAmount =0;
        reservationPMT.SplitAmountType = 0;
        reservationPMT.TransactionType = 1;
        reservationPMT.IsSplit = false;
        reservationPMT.PaymentForId = 13;
        reservationPMT.PaymentProcess = 1;

        reservationPMT.PaymentProcessMode =  PaymentProcessMode.Online.inte;
        reservationPMT.PaymentTransactionType = PaymentTransactionType.Deposit.inte;
        reservationPMT.PaymentProcess = PaymentProcess.Charge.inte;
        //reservationPMT.SplitAmountType =
        reservationPMT.PaymentMode = PaymentMode.CreditCard.inte;


        String dddd = "{\"CompanyId\":1,\"CustomerTypeId\":50,\"CorporateCompanyId\":0,\"DefaultLocationId\":9,\"DefaultRateId\":3,\"Fname\":\"AARON\",\"Lname\":\"ANDERSON\",\"Gender\":0,\"DOB\":\"1990-05-30T00:00:00\",\"Email\":\"nirav@rentguruz.com\",\"MobileNo\":\"5735762041\",\"QuickBookId\":null,\"IsDoNotRent\":false,\"IsDNDSMS\":false,\"IsDNDEmail\":false,\"Status\":0,\"IsTaxExemption\":false,\"CorporateEmail\":null,\"CorporateName\":\"Personal\",\"CorporatePhone\":null,\"customerSaveType\":0,\"FullName\":\"AARON ANDERSON\",\"AddressesModel\":{\"AddressType\":3,\"AddressFor\":10932,\"CountryId\":231,\"StateId\":3953,\"ZoneId\":0,\"Street\":\"879 NORTH KINGSHIGHWAY\",\"UnitNo\":null,\"City\":\"CAPE GIRARDEAU\",\"ZipCode\":\"63701\",\"Latitude\":null,\"Longitude\":null,\"CountryName\":\"United States\",\"StateName\":\"New Jersey\",\"PreviewAddress\":\"879 NORTH KINGSHIGHWAY, <br/>CAPE GIRARDEAU<br/>New Jersey, 63701, United States\",\"Id\":15929,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1},\"DrivingLicenceModel\":{\"LicenceType\":3,\"LicenceFor\":10932,\"Number\":\"MO 14FA\",\"IssueDate\":\"2020-01-01T00:00:00\",\"ExpiryDate\":\"2022-01-01T00:00:00\",\"DOB\":\"1990-05-30T00:00:00\",\"IssueByCountry\":231,\"IssuedByState\":3956,\"Category\":null,\"EvidentStatus\":null,\"IssueByCountryName\":\"United States\",\"IssuedByStateName\":\"New York\",\"DisplayIssuedBy\":\"New York, United States\",\"DrivingLicenseFront\":null,\"DrivingLicenseBack\":null,\"DrivingHistoryDocument\":null,\"CriminalRecordDocument\":null,\"AddresssAttachmentsModel\":null,\"Id\":346,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1},\"InsuranceDetailsModel\":{\"InsuranceType\":3,\"InsuranceFor\":10932,\"InsuranceCompanyId\":3,\"PolicyNo\":\"234377\",\"IssueDate\":\"2021-07-29T00:00:00\",\"ExpiryDate\":\"2022-01-01T00:00:00\",\"Deductible\":500,\"CoverLimit\":2000,\"Description\":null,\"VerifiedBy\":1,\"InsuranceCompanyDetailsModel\":{\"CompanyId\":1,\"Name\":\"PROGRESSIVE\",\"ContactName\":\"Mark mathew\",\"Email\":\"mark@gmail.com\",\"PhoneNo\":\"4561237899\",\"FaxNo\":\"\",\"Id\":3,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1},\"GetCompanyDetail\":false,\"AttachmentsModel\":null,\"Id\":10067,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1},\"CustomerIdProofModel\":null,\"CustomerDetailModel\":{\"CustomerId\":10932,\"ReferralAgentId\":0,\"AccountManagerId\":0,\"IsReset\":false,\"IsBlockAccess\":false,\"IsGrantAccess\":false},\"CorporateCompanyModel\":null,\"UserModel\":null,\"Age\":31,\"ReservationNo\":null,\"CheckInDate\":null,\"VehicleName\":null,\"LicenseNumber\":null,\"fStartDate\":\"0001-01-01T00:00:00\",\"fEndDate\":\"0001-01-01T00:00:00\",\"fUnderCheckIn\":false,\"CustomerTypeName\":\"Retail\",\"CTypeColorCode\":null,\"CreditCardModel\":{\"CreditCardType\":3,\"CreditCardFor\":10932,\"CardCompany\":\"Visa\",\"Number\":\"4111 1111 1111 1111\",\"NameOn\":\"\",\"ExpiryMonth\":9,\"ExpiryYear\":21,\"CVVCode\":123,\"IsDefault\":true,\"ZipCode\":\"\",\"Last4DigitNumber\":\"**** **** **** 1111\",\"Name\":\"**** **** **** 1111\",\"fStartDate\":\"0001-01-01T00:00:00\",\"fEndDate\":\"0001-01-01T00:00:00\",\"IsFirstInsert\":false,\"AddressesModel\":null,\"Fname\":null,\"Lname\":null,\"MobileNo\":null,\"Email\":null,\"Id\":5970,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1},\"fIsDoNotRent\":false,\"CustomerDepositModel\":null,\"Search\":null,\"CustomerVerificationModel\":null,\"EvidentRequestId\":null,\"IsOnlyForUsa\":false,\"AttachmentsModel\":null,\"Id\":10932,\"DetailId\":0,\"IsActive\":true,\"auditLogModel\":null,\"dataTableRequestModel\":null,\"TotalRecord\":0,\"fIds\":null,\"APIRequestType\":1}";
        reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,customerProfile);
        String  bodyParam;
        if (getArguments().getInt("reservationpmt") == 1){
            Reservation reservation =(Reservation) getArguments().getSerializable("reservation");
            bodyParam ="?id="+ reservation.CustomerId +"&isActive=true";
            reservationPMT.CustomerId = reservation.CustomerId;
            reservationPMT.ReservationId = reservation.Id;
            reservationPMT.AgreementNumber = reservation.ReservationNo;
            String data = " Once the payment is processed successfully, you will get your payment reference number. Payment confirmation email will been sent to "+
                    reservation.Email +". Please call customer service for any errors.";
            binding.detail.setText(data);

            binding.textViewPickupLocation.setText(reservation.PickUpLocationName);
            binding.textViewReturnLocationName.setText(reservation.DropLocationName);
            //binding.textViewPickupLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));
            //binding.textViewReturnLocationDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")) + "," + Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));
            CustomBindingAdapter.dateFullConvert(binding.textViewPickupLocationDate,reservation.CheckOutDate);
            CustomBindingAdapter.dateFullConvert(binding.textViewReturnLocationDate,reservation.CheckInDate);
            Glide.with(context).load(reservation.VehicleImagePath).into(binding.carimage);
            binding.textVVehicleModelName.setText(reservation.VehicleName);
            //binding.textVVehicleTypeVName.setText(vehicleModel.VehicleCategory);
            UserData.loginResponse.User.UserFor = reservation.CustomerId;
            reservationPMT.BillToInfoJSON = loginRes.modeltostring(TAG,UserData.customerProfile);

        } else {
         bodyParam ="?id="+ customer.Id+"&isActive=true";
            reservationPMT.CustomerId = customer.Id;
            reservationPMT.ReservationId = reserversationSummary.Id;
            reservationPMT.AgreementNumber = reserversationSummary.ReservationNo;
            String data = " Once the payment is processed successfully, you will get your payment reference number. Payment confirmation email will been sent to "+
                    reserversationSummary.CustomerEmail +". Please call customer service for any errors.";
            binding.detail.setText(data);
        }


        new ApiService(GetDefaultCreditCard, RequestType.GET, GETCUSTOMER,BASE_URL_CUSTOMER, header,bodyParam);




        reservationPMT.BillTo = 1;

        binding.process.setOnClickListener(this);
        binding.offlinepayment.setOnClickListener(this);
        binding.payment.setOnClickListener(this);
        /*binding.header.discard.setText("Add");
        binding.header.discard.setOnClickListener(this);*/
        binding.editcard.setOnClickListener(this);
        binding.backimgPayment.setOnClickListener(this);


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

          /*  case R.id.save:
                new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                        RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);
                break;*/

            case R.id.process:
                reservationPMT.CreditCardId = UserData.UpdateCreditCard.Id;

                reservationPMT.InvoiceAmount = reservationPMT.Amount;

                pmtList.add(reservationPMT);
                bundle.putBoolean("pmt", true);
                NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_optionpayment,bundle);
              /*  new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                        RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);*/
                break;

            case R.id.offlinepayment:
                NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_paymentoffline,bundle);
                break;

            case R.id.payment:

                reservationPMT.CreditCardId = UserData.UpdateCreditCard.Id;
               // reservationPMT.CustomerId = customer.Id;
                reservationPMT.InvoiceAmount = reservationPMT.Amount;
                //reservationPMT.ReservationId = reserversationSummary.Id;
                pmtList.add(reservationPMT);

                new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                        RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);

                break;

            case R.id.discard:
                    UserData.loginResponse.User.UserFor = customer.Id;
                    NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_select_card, bundle);
                break;

            case R.id.editcard:

                NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_select_card,bundle);
                break;

            case R.id.backimg_payment:

                if (getArguments().getInt("reservationpmt") == 1){
                    NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).popBackStack();
                }else {
                    NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_agreement,bundle);
                }
                break;
        }
    }

    OnResponseListener GetDefaultCreditCard = new OnResponseListener()
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

                            final JSONObject creditcardDetails= resultSet.getJSONObject("CreditCardModel");
                            loginRes.storedata("CustomerProfile", creditcardDetails.toString());
                            UserData.UpdateCreditCard = loginRes.getModel(creditcardDetails.toString(), CreditCardModel.class) ;

                            binding.txtCardName.setText(UserData.UpdateCreditCard.NameOn);
                            binding.txtCardNumber.setText(UserData.UpdateCreditCard.Last4DigitNumber);
                            binding.txtExpiryDate.setText(UserData.UpdateCreditCard.ExpiryMonth + "/"+ UserData.UpdateCreditCard.ExpiryYear);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
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
            System.out.println("Error-" + error);
        }
    };

    OnResponseListener processPayment = new OnResponseListener()
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
                            transactionId = responseJSON.getString("Data");
                            bundle.putString("transactionId",transactionId);
                            bundle.putString("netrate", getArguments().getString("netrate"));
                            String message = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),message,0);
                            NavHostFragment.findNavController(Fragment_New_Agreement_Payment.this).navigate(R.id.payment_to_paymentsucess,bundle);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
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
            System.out.println("Error-" + error);
        }
    };
}
