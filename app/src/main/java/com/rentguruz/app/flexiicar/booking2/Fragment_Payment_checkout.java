package com.rentguruz.app.flexiicar.booking2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.databinding.FragmentPaymentCheckoutProcessBinding;
import com.rentguruz.app.model.display.Pickupdrop;
import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.model.CreditCardModel;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.parameter.PaymentMode;
import com.rentguruz.app.model.parameter.PaymentProcess;
import com.rentguruz.app.model.parameter.PaymentProcessMode;
import com.rentguruz.app.model.parameter.PaymentTransactionType;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.RateModel;
import com.rentguruz.app.model.response.ReservationPMT;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.GETCUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.GETDEFAULTCREDITCARD;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONPMT;

public class Fragment_Payment_checkout extends BaseFragment
{
    LinearLayout lblprocess;
    TextView lbleditcard,txtcardname,txtcardNumber,txtExpiryDate;
    ImageView imgback,CarImage;
   // Handler handler = new Handler();
    public static Context context;
    public String id = "";
    Bundle BookingBundle,VehicleBundle;;
    TextView txt_PickLocName,txt_ReturnLocName,txt_PickupDate,txt_ReturnDate,txt_PickupTime,txt_ReturnTIme,txt_vehicletype,
            txt_vehName, txtAmtPayable;
    Double amountPayable;
    String transactionId;
    JSONObject creditCardJSON;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect,isDefaultCard;
    ImageLoader imageLoader;
    String serverpath="",VehImage="";

    VehicleModel model = new VehicleModel();
    LocationList pickuplocation = new LocationList();
    LocationList droplocation = new LocationList();
    RateModel rateModel = new RateModel();
   // Bundle bundle = new Bundle();
   // LoginRes loginRes;
   // public DoHeader header;
    List<ReservationPMT> pmtList = new ArrayList<>();
    ReservationPMT reservationPMT;
    ReservationSummarry reserversationSummary;
    CreditCardModel creditCardModel;
    FragmentPaymentCheckoutProcessBinding binding;
    Pickupdrop pickupdrop;
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*header = new DoHeader();
        header.exptime = "7/24/2021 11:47:18 PM";
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.islogin = "1";
        header.ut = "PYOtYmuTsLQ=";
        loginRes = new LoginRes(getContext());*/

        reservationPMT = new ReservationPMT();
        pickupdrop = new Pickupdrop();
        binding = FragmentPaymentCheckoutProcessBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_payment_checkout_process, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try{
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            reserversationSummary = new ReservationSummarry();
            creditCardModel = new CreditCardModel();
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            serverpath = sp.getString("serverPath", "");
            Log.e(TAG, "onViewCreated: "+getArguments().getString("miles") );
            model = (VehicleModel) getArguments().getSerializable("vechicle");
            pickuplocation = (LocationList) getArguments().getSerializable("pickuploc");
            droplocation = (LocationList) getArguments().getSerializable("droploc");
            rateModel = (RateModel) getArguments().getSerializable("ratemaster");
            reserversationSummary = (ReservationSummarry) getArguments().getSerializable("summarry");
            pickupdrop.dropdate = reserversationSummary.CheckOutDate+":00";
            pickupdrop.pickupdate = reserversationSummary.CheckInDate+":00";
            bundle.putSerializable("pickuploc", pickuplocation);
            bundle.putSerializable("droploc", droplocation);
            bundle.putSerializable("ratemaster", rateModel);
            bundle.putSerializable("vechicle", model);
            bundle.putString("netrate",getArguments().getString("netrate"));
            //bundle.putDouble("miles", getArguments().getDouble("miles"));
            bundle.putString("pickupdate", getArguments().getString("pickupdate"));
            bundle.putString("dropdate", getArguments().getString("dropdate"));
            bundle.putString("droptime", getArguments().getString("droptime"));
            bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
            bundle.putSerializable("defaultcard",getArguments().getSerializable("defaultcard"));
            bundle.putInt("frag",2);
            bundle.putSerializable("summarry",reserversationSummary );
            bundle.putString("miles",getArguments().getString("miles"));
            bundle.putSerializable("charges",getArguments().getSerializable("charges"));

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
            reservationPMT.BillToInfoJSON = dddd;

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            lblprocess=view.findViewById(R.id.lblprocess);
            lbleditcard=view.findViewById(R.id.editcard);
            imgback=view.findViewById(R.id.backimg_payment);

            txtcardname=view.findViewById(R.id.txt_CardName);
            txtcardNumber=view.findViewById(R.id.txtCardNumber);
            txtExpiryDate=view.findViewById(R.id.txt_ExpiryDate);
            CarImage=view.findViewById(R.id.carimage);

            txt_PickLocName = view.findViewById(R.id.textView_PickupLocation);
            txt_PickupDate = view.findViewById(R.id.textView_PickupLocationDate);
            txt_PickupTime = view.findViewById(R.id.textView_PickupLocationTime);
            txt_ReturnLocName = view.findViewById(R.id.textView_ReturnLocationName);
            txt_ReturnDate = view.findViewById(R.id.textView_ReturnLocationDate);
            txt_ReturnTIme = view.findViewById(R.id.textView_ReturnLocationTime);
            txt_vehName=view.findViewById(R.id.textV_VehicleModelName);
            txt_vehicletype=view.findViewById(R.id.textV_VehicleTypeVName);
            txtAmtPayable = view.findViewById(R.id.txtAmtPayable);

            //String data = " Once the payment is processed successfully,\n you will get your payment reference \n number. Payment confirmation email will \n been sent to "+ reserversationSummary.CustomerEmail +".\n Please call customer service for any errors.";
            String data = " Once the payment is processed successfully, you will get your payment reference number. Payment confirmation email will been sent to "+
                    reserversationSummary.CustomerEmail +". Please call customer service for any errors.";
            TextView datail = view.findViewById(R.id.datail);
            datail.setText(data);
            if (getArguments().getSerializable("defaultcard")==""){

                JSONObject object = new JSONObject();
                String bodyParam = "";
                List<Integer> ints = new ArrayList<>();
                JSONObject filter = new JSONObject();
                try
                {   object.accumulate("limit",10);
                    object.accumulate("orderDir","desc");
                    object.accumulate("pageSize", 10);
                    ints.add(10);
                    ints.add(20);
                    ints.add(30);
                    ints.add(40);
                    ints.add(50);
                    object.accumulate("pageLimits", ints);
                    // filter.accumulate("CreditCardType", 3);
                    filter.accumulate("CompanyId", UserData.loginResponse.User.CompanyId);
                    filter.accumulate("UserFor", UserData.loginResponse.User.UserFor);
                    filter.accumulate("CustomerTypeId", UserData.loginResponse.apiUserLogin.UserId);
                    filter.accumulate("IsActive", true);
                    object.accumulate("filterObj", filter);
                    bodyParam+="?id="+UserData.loginResponse.User.UserFor+"&isActive=true";
                   // bodyParam+="?id="+UserData.customer.Id+"&isActive=true";
                    // bodyParam+="?id="+loginResponse.User.UserFor+"&isActive=true";

                    System.out.println(bodyParam);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                AndroidNetworking.initialize(getActivity());
                Fragment_Payment_checkout.context = getActivity();
                ApiService apiService = new ApiService(GetDefaultCreditCard, RequestType.GET, GETCUSTOMER+bodyParam,BASE_URL_CUSTOMER, header,object);
            }
            else {
                creditCardModel = (CreditCardModel) getArguments().getSerializable("defaultcard");
                txtcardname.setText(creditCardModel.NameOn);
                txtcardNumber.setText(creditCardModel.Last4DigitNumber);
                txtExpiryDate.setText(creditCardModel.ExpiryMonth + "/"+ creditCardModel.ExpiryYear);

                lblprocess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservationPMT.CreditCardId = creditCardModel.Id;
                        reservationPMT.CustomerId =creditCardModel.CreditCardFor;
                        reservationPMT.InvoiceAmount = reservationPMT.Amount;
                        reservationPMT.ReservationId = reserversationSummary.Id;
                        pmtList.add(reservationPMT);
                        if (Helper.reservationpmt) {
                            ApiService2<List<ReservationPMT>> apiService2 = new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                                    RESERVATIONPMT, BASE_URL_LOGIN, header, pmtList);
                        } else {
                            bundle.putInt("test",2);
                            NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                                    .navigate(R.id.action_Payment_checkout_to_Payment_Success, bundle);
                        }
                    }
                });
            }

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");
            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");
            isDefaultCard = getArguments().getBoolean("isDefaultCard");




          /*  VehImage = VehicleBundle.getString("img_Path");

            String url1 = serverpath + VehImage;
            imageLoader.displayImage(url1, CarImage);*/
            pickupdrop.imagepath = model.DefaultImagePath;
            Glide.with(getContext()).load(model.DefaultImagePath).into(CarImage);

            txt_vehName.setText(model.VehicleName);
            txt_vehicletype.setText(model.VehicleCategory);
            txt_PickLocName.setText(pickuplocation.Name);
            txt_ReturnLocName.setText(droplocation.Name);

            pickupdrop.pickuploc = pickuplocation.Name;
            pickupdrop.droploc = droplocation.Name;

            String StrPickupDate = (getArguments().getString("pickupdate"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(StrPickupDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String PickUpDateStr = sdf.format(date);
            txt_PickupDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("pickupdate")));

            String StrReturnDate = (getArguments().getString("dropdate"));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat1.parse(StrReturnDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            String ReturnDateStr = sdf1.format(date1);
            txt_ReturnDate.setText(Helper.getDateDisplay(DateType.yyyyMMddD,getArguments().getString("dropdate")));


            String strPickUpTime = (getArguments().getString("pickuptime"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            Date date2 = dateFormat2.parse(strPickUpTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa", Locale.US);
            String PickUpTimeStr = sdf2.format(date2);
            txt_PickupTime.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("pickuptime")));

            String strReturntime = (getArguments().getString("droptime"));
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm");
            Date date3 = dateFormat3.parse(strReturntime);
            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
            String ReturntimeStr = sdf3.format(date3);
            txt_ReturnTIme.setText(Helper.getTimeDisplay(DateType.time,getArguments().getString("droptime")));

            amountPayable = Double.valueOf(getArguments().getString("netrate"));
            //txtAmtPayable.setText("$ "+((String.format(Locale.US,"%.2f",amountPayable))));
            txtAmtPayable.setText(Helper.getAmtount(amountPayable,false));

            CarImage = view.findViewById(R.id.carimage);
            reservationPMT.AgreementNumber = reserversationSummary.ReservationNo;

            reservationPMT.Amount =Double.valueOf(getArguments().getString("netrate"));
            reservationPMT.BillTo = 1;
            binding.pickups.nodays.setVisibility(View.GONE);
            binding.pickups.carimages.setVisibility(View.VISIBLE);
            binding.pickups.setPickupdrop(pickupdrop);


            lbleditcard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
       /*             Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                    bundle.putBundle("VehicleBundle",VehicleBundle);
                    bundle.putBundle("BookingBundle",BookingBundle);
                    bundle.putBundle("returnLocation", returnLocationBundle);
                    bundle.putBundle("location", locationBundle);
                    bundle.putBoolean("locationType", locationType);
                    bundle.putBoolean("initialSelect", initialSelect);*/
                    NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                            .navigate(R.id.action_Payment_checkout_to_CardsOnAccount, bundle);
                }
            });

            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                 /*   Bundle bundle = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    bundle.putBundle("BookingBundle", BookingBundle);
                    bundle.putBundle("VehicleBundle", VehicleBundle);
                    bundle.putBundle("returnLocation", returnLocationBundle);
                    bundle.putBundle("location", locationBundle);
                    bundle.putBoolean("locationType", locationType);
                    bundle.putBoolean("initialSelect", initialSelect);*/
                    NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                            .navigate(R.id.action_Payment_checkout_to_Finalize_your_rental,bundle);
                }
            });

       /*     if(isDefaultCard)
            {
                JSONObject bodyParam = new JSONObject();
                try {
                    bodyParam.accumulate("ForTransId", BookingBundle.getInt("ForTransId"));
                    bodyParam.accumulate("PickupLocId", BookingBundle.getInt("PickupLocId"));
                    bodyParam.accumulate("ReturnLocId", BookingBundle.getInt("ReturnLocId"));
                    bodyParam.accumulate("CustomerId", BookingBundle.getInt("CustomerId"));
                    bodyParam.accumulate("VehicleTypeId", BookingBundle.getInt("vehiclE_TYPE_ID"));
                    bodyParam.accumulate("VehicleID", BookingBundle.getInt("VehicleID"));
                    bodyParam.accumulate("StrFilterVehicleTypeIds", BookingBundle.getString("StrFilterVehicleTypeIds"));
                    bodyParam.accumulate("StrFilterVehicleOptionIds", BookingBundle.getString("StrFilterVehicleOptionIds"));

                    String dateFormatPickupDate = (BookingBundle.getString("PickupDate"));
                    String strPickUpTime1 = (BookingBundle.getString("PickupTime"));
                    String PickupDateTime = dateFormatPickupDate + "T" + strPickUpTime1;

                    bodyParam.accumulate("PickupDate", PickupDateTime);

                    String dateFormatReturnDate = (BookingBundle.getString("ReturnDate"));
                    String strReturnTime1 = (BookingBundle.getString("ReturnTime"));
                    String ReturnDateTime = dateFormatReturnDate + "T" + strReturnTime1;

                    bodyParam.accumulate("ReturnDate", ReturnDateTime);

                    bodyParam.accumulate("FilterTransmission", BookingBundle.getInt("FilterTransmission"));
                    bodyParam.accumulate("FilterPassengers", BookingBundle.getInt("FilterPassengers"));
                    bodyParam.accumulate("BookingStep", BookingBundle.getInt("BookingStep"));
                    bodyParam.accumulate("BookingType", BookingBundle.getInt("BookingType"));
                    bodyParam.accumulate("DeliveryChargeLocID", BookingBundle.getInt("DeliveryChargeLocID"));
                    bodyParam.accumulate("DeliveryChargeAmount", BookingBundle.getDouble("DeliveryChargeAmount"));
                    bodyParam.accumulate("PickupChargeLocID", BookingBundle.getInt("PickupChargeLocID"));
                    bodyParam.accumulate("PickupChargeAmount", BookingBundle.getDouble("PickupChargeAmount"));
                    bodyParam.accumulate("EquipmentList", new JSONArray(BookingBundle.getString("EquipmentList")));
                    bodyParam.accumulate("MiscList", new JSONArray(BookingBundle.getString("MiscList")));
                    bodyParam.accumulate("SummaryOfCharges", new JSONArray(BookingBundle.getString("SummaryOfCharges")));
                    // bodyParam.accumulate("DeliveryAndPickupModel", new JSONArray(BookingBundle.getString("DeliveryAndPickupModel")));
                    System.out.println(bodyParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.initialize(getActivity());
                Fragment_Finalize_Your_Rental.context = getActivity();

                ApiService ApiService1 = new ApiService(getTransactionId, RequestType.POST,
                        BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
            }
            else {
                try {
                    String creditCardJSONStr = getArguments().getString("creditcard");
                    creditCardJSON = new JSONObject(creditCardJSONStr);

                    transactionId = getArguments().getString("transactionId");

                    String card_No = creditCardJSON.getString("card_No");
                    String card_Name = creditCardJSON.getString("card_Name");
                    String expiry_Date = creditCardJSON.getString("expiry_Date");

                    txtcardname.setText(card_Name);
                    txtcardNumber.setText("**** **** **** " + card_No.substring(card_No.length() - 4));
                    txtExpiryDate.setText(expiry_Date);

                    lblprocess.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            JSONObject bodyParam3 = new JSONObject();
                            try {
                                bodyParam3.accumulate("ForTransId", Integer.parseInt(transactionId));
                                bodyParam3.accumulate("CardName", creditCardJSON.get("card_Name"));
                                bodyParam3.accumulate("CreditCardNo", creditCardJSON.getString("card_No"));
                                bodyParam3.accumulate("ExpiryDate", creditCardJSON.getString("expiry_Date"));
                                bodyParam3.accumulate("CVSNo", creditCardJSON.getString("cvS_Code"));
                                bodyParam3.accumulate("Amount", BookingBundle.getDouble("total"));
                                bodyParam3.accumulate("Street", creditCardJSON.getString("card_PStreet"));
                                bodyParam3.accumulate("UnitNo", creditCardJSON.getString("card_PUnitNo"));
                                bodyParam3.accumulate("City", creditCardJSON.getString("card_PCity"));
                                bodyParam3.accumulate("CountryID", creditCardJSON.getInt("card_PCountry"));
                                bodyParam3.accumulate("StateID", creditCardJSON.getInt("card_PState"));
                                bodyParam3.accumulate("ZipCode", creditCardJSON.getString("card_SZipCode"));
                                bodyParam3.accumulate("TransType", 0);
                                bodyParam3.accumulate("ChargeType", 0);
                                bodyParam3.accumulate("Type", 0);
                                bodyParam3.accumulate("Remark", "");
                                bodyParam3.accumulate("CardType", "visa");
                                bodyParam3.accumulate("CountryCode", "CA");
                                bodyParam3.accumulate("StateName", "ONTARIO");
                                bodyParam3.accumulate("MobileNumber", "9921023213");
                                bodyParam3.accumulate("CurrencyISO", "USD");
                                bodyParam3.accumulate("CustomerId", Integer.parseInt(id));
                                bodyParam3.accumulate("Email", "info@customer.com");

                                System.out.println(bodyParam3);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ApiService ApiService1 = new ApiService(processPayment, RequestType.POST,
                                    PAYMENTPROCESS, BASE_URL_PAYMENT, new HashMap<String, String>(), bodyParam3);
                        }
                    });
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener getTransactionId = new OnResponseListener()
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
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");

                                transactionId = responseJSON.getString("transactionId");
                                String message = responseJSON.getString("message");

                                BookingBundle.putInt("ForTransId",Integer.parseInt(transactionId));
                                CustomToast.showToast(getActivity(),message,0);

                                if(!id.equals(""))
                                {


                                  /*  ApiService ApiService = new ApiService(GetDefaultCreditCard, RequestType.GET,
                                            GETDEFAULTCREDITCARD, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam1);*/
                                }
                                else
                                    {
                                    final JSONObject test = resultSet.getJSONObject("bookingModel");
                                    id = test.getInt("customerId") + "";

                                    SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString(getString(R.string.id), id);
                                    editor.commit();

                                    String bodyParam1 = "";
                                    try {
                                        bodyParam1 += "customerId=" + id;
                                        System.out.println(bodyParam1);
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    AndroidNetworking.initialize(getActivity());
                                    Fragment_Payment_checkout.context = getActivity();

                                    ApiService ApiService = new ApiService(GetDefaultCreditCard, RequestType.GET,
                                            GETDEFAULTCREDITCARD, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam1);
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            String msg = responseJSON.getString("message");
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

    OnResponseListener GetDefaultCreditCard = new OnResponseListener()
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
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");



                                final JSONObject creditcardDetails= resultSet.getJSONObject("CreditCardModel");
                                loginRes.storedata("CustomerProfile", creditcardDetails.toString());
                                UserData.UpdateCreditCard = loginRes.getModel(creditcardDetails.toString(), CreditCardModel.class) ;
                                creditCardJSON = creditcardDetails;

                               /* int card_ID=creditcardDetails.getInt("card_ID");
                                String card_No=creditcardDetails.getString("card_No");
                                String card_Name=creditcardDetails.getString("card_Name");
                                String expiry_Date=creditcardDetails.getString("expiry_Date");*/

                                txtcardname.setText(UserData.UpdateCreditCard.NameOn);
                                txtcardNumber.setText(UserData.UpdateCreditCard.Last4DigitNumber);
                                txtExpiryDate.setText(UserData.UpdateCreditCard.ExpiryMonth + "/"+ UserData.UpdateCreditCard.ExpiryYear);

                                lblprocess.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        reservationPMT.CreditCardId = UserData.UpdateCreditCard.Id;
                                        reservationPMT.CustomerId = UserData.loginResponse.User.UserFor;
                                        reservationPMT.InvoiceAmount = reservationPMT.Amount;
                                        reservationPMT.ReservationId = reserversationSummary.Id;
                                        pmtList.add(reservationPMT);
                                        /*ApiService2<List<ReservationPMT>> apiService2 = new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                                                RESERVATIONPMT, BASE_URL_LOGIN,header ,pmtList);*/

                                        if (Helper.reservationpmt) {
                                            ApiService2<List<ReservationPMT>> apiService2 = new ApiService2<List<ReservationPMT>>(processPayment, RequestType.POST,
                                                    RESERVATIONPMT, BASE_URL_LOGIN, header, pmtList);
                                        } else {
                                            bundle.putInt("test",2);
                                            NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                                                    .navigate(R.id.action_Payment_checkout_to_Payment_Success, bundle);
                                        }

                                     /*   JSONObject bodyParam3 = new JSONObject();
                                        try
                                        {
                                            bodyParam3.accumulate("ForTransId", Integer.parseInt(transactionId));
                                            bodyParam3.accumulate("CardName", creditCardJSON.get("card_Name"));
                                            bodyParam3.accumulate("CreditCardNo", creditCardJSON.getString("card_No"));
                                            bodyParam3.accumulate("ExpiryDate", creditCardJSON.getString("expiry_Date"));
                                            bodyParam3.accumulate("CVSNo", creditCardJSON.getString("cvS_Code"));
                                            bodyParam3.accumulate("Amount", BookingBundle.getDouble("total"));
                                            bodyParam3.accumulate("Street", creditCardJSON.getString("card_PStreet"));
                                            bodyParam3.accumulate("UnitNo", creditCardJSON.getString("card_PUnitNo"));
                                            bodyParam3.accumulate("City", creditCardJSON.getString("card_PCity"));
                                            bodyParam3.accumulate("CountryID", creditCardJSON.getInt("card_PCountry"));
                                            bodyParam3.accumulate("StateID", creditCardJSON.getInt("card_PState"));
                                            bodyParam3.accumulate("ZipCode", creditCardJSON.getString("card_SZipCode"));
                                            bodyParam3.accumulate("TransType", 0);
                                            bodyParam3.accumulate("ChargeType", 0);
                                            bodyParam3.accumulate("Type", 0);
                                            bodyParam3.accumulate("Remark", "");
                                            bodyParam3.accumulate("CardType", "visa");
                                            bodyParam3.accumulate("CountryCode", "CA");
                                            bodyParam3.accumulate("StateName", "ONTARIO");
                                            bodyParam3.accumulate("MobileNumber", "9921023213");
                                            bodyParam3.accumulate("CurrencyISO", "USD");
                                            bodyParam3.accumulate("CustomerId", Integer.parseInt(id));
                                            bodyParam3.accumulate("Email", "info@customer.com");
                                            System.out.println(creditCardJSON);
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        ApiService ApiService1 = new ApiService(processPayment, RequestType.POST,
                                                PAYMENTPROCESS, BASE_URL_PAYMENT, new HashMap<String, String>(), bodyParam3);*/
                                    }
                                });
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            //String msg = responseJSON.getString("message");
                           // CustomToast.showToast(getActivity(),msg,1);
                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId",transactionId);
                            bundle.putDouble("total",amountPayable);
                            bundle.putBundle("VehicleBundle",VehicleBundle);
                            bundle.putBundle("BookingBundle",BookingBundle);
                            bundle.putBundle("returnLocation", returnLocationBundle);
                            bundle.putBundle("location", locationBundle);
                            bundle.putBoolean("locationType", locationType);
                            bundle.putBoolean("initialSelect", initialSelect);
                            NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                                    .navigate(R.id.action_Payment_checkout_to_AddCreditCardDetails, bundle);
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

    OnResponseListener processPayment = new OnResponseListener()
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
                            try
                            {
                                transactionId = responseJSON.getString("Data");

                                String message = responseJSON.getString("Message");
                                CustomToast.showToast(getActivity(),message,0);
                                bundle.putInt("test",2);
                   /*             Bundle paymentBundle = new Bundle();
                                paymentBundle.putInt("sendTo",1);
                                paymentBundle.putString("message",message);
                                paymentBundle.putString("transactionId",transactionId);
                                paymentBundle.putDouble("total",BookingBundle.getDouble("total"));
                                paymentBundle.putBundle("returnLocation", returnLocationBundle);
                                paymentBundle.putBundle("location", locationBundle);
                                paymentBundle.putBoolean("locationType", locationType);
                                paymentBundle.putBoolean("initialSelect", initialSelect);
                                paymentBundle.putBundle("BookingBundle", BookingBundle);
                                paymentBundle.putBundle("VehicleBundle", VehicleBundle);*/
                                Helper.reservationpmt = false;
                                NavHostFragment.findNavController(Fragment_Payment_checkout.this)
                                        .navigate(R.id.action_Payment_checkout_to_Payment_Success, bundle);
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

    }
}
