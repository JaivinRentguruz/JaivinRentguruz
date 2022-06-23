package com.rentguruz.app.flexiicar.selfcheckin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.OptionMenu;
import com.rentguruz.app.adapters.SummaryDisplay;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentSelfCheckinBinding;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.flexiicar.user.Fragment_Summary_Of_Charges_For_Agreements;
import com.rentguruz.app.model.ReservationCheckout;
import com.rentguruz.app.model.common.DropDown;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.LocationList;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.rentguruz.app.model.response.VehicleModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rentguruz.app.model.response.ReservationSummaryModels;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.AVAILABLELOCATION;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKOUTODMETER;
import static com.rentguruz.app.apicall.ApiEndPoint.COMMONDROPDOWNSINGLE;
import static com.rentguruz.app.apicall.ApiEndPoint.SUMMARYCHARGE;
import static com.rentguruz.app.apicall.ApiEndPoint.TAXLIST;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLEGETBYID;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLELOCATION;

import com.rentguruz.app.R;
public class Fragment_Self_Check_In extends BaseFragment
{
    ImageView BackArrow,VehImg;
    //Handler handler = new Handler();
    public static Context context;
    TextView txtVehicleNumber,txtvehicleName,txtExtraFuelCharge,txtExtraMileageCharge,txtExtraDayRentalCharge;
    //,txt_OdoMeterIn;
    EditText txtoriginalCheckInDate,txtcheck_in_Location_Name, //txtcheck_Out_Location_Name,txtactualReturnDate,
     txt_OdoMeterIn, txtOdometerOut,txttotalMilesAllowed,txtTotalMilesDone,
    txtGasTankCapacity,txtGasTankOut,txtGasTankIn, txtGasTankUsed,txtGasTankCharge,txtTotalGasCharge;
    LinearLayout calculate;

    Spinner txtcheck_Out_Location_Name;
    TextView txtactualReturnDate;

    Bundle AgreementsBundle;
    JSONArray ImageList = new JSONArray();
    JSONArray finalImageList = new JSONArray();

    ImageLoader imageLoader;
    String serverpath="";
    SeekBar customSeekBar;
    LinearLayout lblAccept;
    int originalCheckInLocationId,actualReturnLocationId;
    String originalCheckInDate="",actualReturnDate="";
    ReservationSummarry reservationSummarry = new ReservationSummarry();
    Reservation reservations;
    //Bundle bundle = new Bundle();
    //DoHeader header;
    //CommonParams params;
    LocationList[]  locationListsData;
    List<String> location = new ArrayList<>();
    //public LoginRes loginRes;
    CustomeDialog dialog;
    ReservationCheckout reservationCheckout;
    //String TAG = "Fragment_Self_Check_In";
    public static ReservationSummaryModels[] charges;
    RelativeLayout rsummarry;
    FragmentSelfCheckinBinding binding;
    SummaryDisplay summaryDisplay;
    VehicleModel vehicleModel;
    Boolean validation;
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
       /* header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";
        params = new CommonParams();
        loginRes = new LoginRes(getContext());*/
        dialog = new CustomeDialog(getContext());
        reservationCheckout = new ReservationCheckout();
        binding = FragmentSelfCheckinBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        try {
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();
            reservations = new Reservation();
            vehicleModel = new VehicleModel();
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            summaryDisplay = new SummaryDisplay(getActivity());
           // ImageList = new JSONArray(getArguments().getString("ImageList"));
           // AgreementsBundle = getArguments().getBundle("AgreementsBundle");
           // reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservation");
            reservations = (Reservation) getArguments().getSerializable("reservation");
            validation = false;
            bundle.putInt("reservationpmt", 1);
            binding.header.screenHeader.setText(companyLabel.CheckIn);
            binding.header.back.setOnClickListener(this);
            binding.header.discard.setOnClickListener(this);
            calculate = view.findViewById(R.id.calculate);
            lblAccept=view.findViewById(R.id.lblAccept);
            BackArrow = view.findViewById(R.id.back);
            VehImg = view.findViewById(R.id.VehImage);
            customSeekBar = view.findViewById(R.id.simpleSeekBar1);
            rsummarry = view.findViewById(R.id.summarry);
            txtVehicleNumber = view.findViewById(R.id.txtVehicleNumber);
          //  txtVehicleNumber = view.findViewById(R.id.txtVehicleNumber);
            txtvehicleName = view.findViewById(R.id.txtvehicleName);
            /*txtExtraFuelCharge = view.findViewById(R.id.ExtraFuelCharge);
            txtExtraMileageCharge = view.findViewById(R.id.ExtraMileageCharge);
            txtExtraDayRentalCharge = view.findViewById(R.id.ExtraDayRentalCharge);*/
            txt_OdoMeterIn = view.findViewById(R.id.txt_OdoMeterIn);
            txtOdometerOut = view.findViewById(R.id.OdometerOut);
            txttotalMilesAllowed = view.findViewById(R.id.totalMilesAllowed);
            txtTotalMilesDone = view.findViewById(R.id.TotalMilesDone);

            txtoriginalCheckInDate = view.findViewById(R.id.originalCheckInDate);
            txtcheck_Out_Location_Name = view.findViewById(R.id.txtcheck_Out_Location_Name);
            txtactualReturnDate = view.findViewById(R.id.actualReturnDate);
            txtcheck_in_Location_Name = view.findViewById(R.id.txtcheck_in_Location_Name);
            txtGasTankCapacity = view.findViewById(R.id.GasTankCapacity);
            txtGasTankOut = view.findViewById(R.id.GasTankOut);
            txtGasTankIn = view.findViewById(R.id.GasTankIn);
            txtGasTankUsed = view.findViewById(R.id.GasTankUsed);
            txtGasTankCharge = view.findViewById(R.id.GasTankCharge);
            txtTotalGasCharge = view.findViewById(R.id.TotalGasCharge);

            AssetManager am = getActivity().getApplicationContext().getAssets();
            Typeface typefaceFSSiena = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "DS-DIGI.TTF"));
            txt_OdoMeterIn.setTypeface(typefaceFSSiena);
            txtOdometerOut.setTypeface(typefaceFSSiena);

            reservations = (Reservation) getArguments().getSerializable("reservation");
           // txtcheck_Out_Location_Name.setText(reservations.PickUpLocationName);
            txtcheck_in_Location_Name.setText(reservations.DropLocationName);
            txtvehicleName.setText(reservations.VehicleName);
            reservationSummarry  = Fragment_Summary_Of_Charges_For_Agreements.reservationSummarry;
            reservationSummarry.CustomerEmail = reservations.Email;
            Log.e(TAG, "onViewCreated: " + reservationSummarry.CustomerEmail );
            bundle.putSerializable("reservationSum",reservationSummarry);
            bundle.putSerializable("reservation",reservations);
            bundle.putSerializable("resrvation",reservationSummarry);
            bundle.putSerializable("checklist", (Serializable) getArguments().getSerializable("checklist"));
            txtoriginalCheckInDate.setText(DateConvert.DateConverter(DateType.fulldate,reservations.CheckOutDate,DateType.datetime));
            txtactualReturnDate.setText(DateConvert.DateConverter(DateType.fulldate,reservations.CheckInDate,DateType.datetime));
            enablechangeEdit(txtoriginalCheckInDate);
            enablechangeEdit(txtcheck_in_Location_Name);


            try {
                CustomBindingAdapter.loadImage(VehImg,reservations.VehicleImagePath);
            } catch (Exception e){
                e.printStackTrace();
            }

            txtactualReturnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // dialog.getFullDate(dialog.getToday(), "", string -> txtactualReturnDate.setText(string));
                    dialog.getFullDate(string -> txtactualReturnDate.setText(string));

                }
            });

            BackArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle SelfCheckInBundle=new Bundle();
                    SelfCheckInBundle.putBundle("AgreementsBundle",AgreementsBundle);
                    SelfCheckInBundle.putString("ImageList",ImageList.toString());
                    System.out.println(SelfCheckInBundle);
                    /*NavHostFragment.findNavController(Fragment_Self_Check_In.this)
                            .navigate(R.id.action_Self_check_In_to_Signature,SelfCheckInBundle);*/
                    NavHostFragment.findNavController(Fragment_Self_Check_In.this).popBackStack();
                }
            });


        /*      String bodyParam = "";

            try
            {
                bodyParam += "AgreementId=" + AgreementsBundle.getInt("agreement_ID");
                System.out.println(bodyParam);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            AndroidNetworking.initialize(getActivity());
            Fragment_Self_Check_out.context = getActivity();

          ApiService ApiService = new ApiService(GetSelfCheckIN, RequestType.GET,
                    GETSELFCHECKIN, BASE_URL_CHECKIN, new HashMap<String, String>(), bodyParam);*/

            ApiService apiService = new ApiService(Odometer, RequestType.POST,
                    CHECKOUTODMETER, BASE_URL_LOGIN,header ,  params.getCheckOutODmeter(reservations.Id, reservations.VehicleId));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                /*ApiService  apiService = new ApiService(AvailableLocation, RequestType.POST,
                            AVAILABLELOCATION, BASE_URL_LOGIN,header ,  params.getAvailableLocation());*/

                    new ApiService2<>(new OnResponseListener() {
                        @Override
                        public void onSuccess(String response, HashMap<String, String> headers) {
                            handler.post(() -> {
                                try {

                                    JSONObject responseJSON = new JSONObject(response);
                                    Boolean status = responseJSON.getBoolean("Status");
                                    final JSONArray getReservationList = responseJSON.getJSONArray("Data");
                                    OnDropDownList[] onDropDownLists;
                                    List<String> strings = new ArrayList<>();
                                    onDropDownLists = loginRes.getModel(getReservationList.toString(), OnDropDownList[].class);
                                    for (int i = 0; i < onDropDownLists.length; i++) {
                                        // data.add(new OnDropDownList(onDropDownLists[i].Id, onDropDownLists[i].Name));
                                        OnDropDownList onDropDownList = new OnDropDownList();
                                        onDropDownList = loginRes.getModel(getReservationList.get(i).toString(), OnDropDownList.class);
                                       // data.add(onDropDownList);

                                        strings.add(onDropDownLists[i].Name);
                                    }

                                    //   getSpinner(binding.makeId,strings);
                                    //listSpinner(data);

                                    int idd=0;
                                    for (int i = 0; i <onDropDownLists.length; i++) {
                                        location.add(onDropDownLists[i].Name);
                                        if (reservations.PickUpLocationName.matches(strings.get(i))){
                                            idd = i;
                                        }
                                    }
                                    ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1,location);
                                    txtcheck_Out_Location_Name.setAdapter(adapterCategories);
                                    txtcheck_Out_Location_Name.setSelection(idd);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {

                        }
                    }, RequestType.POST, COMMONDROPDOWNSINGLE, BASE_URL_LOGIN, header, new DropDown(VEHICLELOCATION, Integer.parseInt(loginRes.getData("CompanyId")), true, false));
                }
            },500);

           // bundle.putSerializable("checklist", (Serializable) checkOutLists);
            Bundle checklist = new Bundle();
            checklist.putSerializable("checklist",bundle.getSerializable("checklist"));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {

            String kms = String.format(Locale.US,"%.0f",reservationSummarry.ReservationRatesModel.RateFeaturesModel.DailyMilesAllowed);
            txttotalMilesAllowed.setText(kms);
            enablechangeEdit(txttotalMilesAllowed);
            txtvehicleName.setText(reservations.VehicleName);
            txtVehicleNumber.setText(reservations.ReservationNo);

            enablechangeEdit(txtGasTankCapacity);
        } catch (Exception e){
            e.printStackTrace();
        }

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* JSONObject object = new JSONObject();
                try {
                    object.accumulate("StartDate",DateConvert.DateConverter(DateType.datetime,txtoriginalCheckInDate.getText().toString(),DateType.fulldate));
                    object.accumulate("EndDate",DateConvert.DateConverter(DateType.datetime,txtactualReturnDate.getText().toString(),DateType.fulldate));
                    object.accumulate("IsCalculation4CheckIn",true);
                    object.accumulate("OverUsageChagred",1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
                        Log.e(TAG, "onSuccess: " + response );
                        Log.e(TAG, "onSuccess: " + headers );
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "onError: " + error );
                    }
                }, RequestType.POST,
                        TIMECALCULATE, BASE_URL_LOGIN,header ,object);*/

                txtTotalMilesDone.setText(String.valueOf(Integer.valueOf(txt_OdoMeterIn.getText().toString())  -  Integer.valueOf(txtOdometerOut.getText().toString())));
                Helper.checkinod =  Integer.valueOf(txt_OdoMeterIn.getText().toString());
                Helper.checkindate =  reservations.CheckInDate;
                Helper.checkinFuel = Integer.parseInt(binding.GasTankIn.getText().toString().replace("%",""));
              //  binding.GasTankCharge.getText().toString() *

                //Double currenttank = Double.valueOf(binding.GasTankCapacity.getText().toString());
                Double tankused = Double.valueOf(binding.GasTankOut.getText().toString().replace("%","")) - Double.valueOf(binding.GasTankIn.getText().toString().replace("%",""));
                //binding.GasTankUsed.setText(String.valueOf(vehicleModel.TankSize*tankused/100));
                binding.GasTankUsed.setText(Helper.getAmtount(vehicleModel.TankSize*tankused/100));

                binding.TotalGasCharge.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.ExtraFuelCharge*vehicleModel.TankSize*tankused/100, true));
                //txtTotalMilesDone.setText(String.valueOf(Integer.valueOf(txtOdometerOut.getText().toString())  -  Integer.valueOf(txt_OdoMeterIn.getText().toString())));

                //reservations.CheckInDate = DateConvert.DateConverter(DateType.datetime,txtactualReturnDate.getText().toString(),DateType.fulldate);
               // reservationSummarry.CheckOutDate = DateConvert.DateConverter(DateType.fulldate,reservations.CheckInDate,DateType.fulldate);
               // reservationSummarry.CheckOutDate = reservations.CheckInDate;
                reservationSummarry.CheckInDate = DateConvert.DateConverter(DateType.datetime,txtactualReturnDate.getText().toString(),DateType.fulldate);
                //reservationSummarry.DropLocation =
               // reservationSummarry.ReservationRatesModel.IsC
                reservationSummarry.IgnorCalculationSummaryTypes = new ArrayList<>();
                reservationSummarry.IgnorCalculationSummaryTypes.add(1);
                reservationSummarry.IgnorCalculationSummaryTypes.add(2);
                reservationSummarry.IgnorCalculationSummaryTypes.add(4);
                reservationSummarry.IgnorCalculationSummaryTypes.add(4);
                reservationSummarry.IgnorCalculationSummaryTypes.add(94);
                reservationSummarry.IgnorCalculationSummaryTypes.add(101);
                reservationSummarry.IgnorCalculationSummaryTypes.add(103);
                reservationSummarry.IgnorCalculationSummaryTypes.add(102);

                new ApiService2(SummaryCharge, RequestType.POST, SUMMARYCHARGE, BASE_URL_LOGIN, header, reservationSummarry);

            }
        });


        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtGasTankIn.setText(String.valueOf(progress+"%"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        lblAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validation) {
                    bundle.putSerializable("reservation", reservations);
                    bundle.putSerializable("reservationSum", reservationSummarry);
                    Log.e(TAG, "onClick: " + reservationSummarry.CustomerEmail);
                   /* NavHostFragment.findNavController(Fragment_Self_Check_In.this)
                            .navigate(R.id.action_Self_check_In_to_SummaryOfChargeForSelfCheckIn,bundle);*/
                    String bodyParam = "?id=" + reservations.CustomerId + "&isActive=true" + "&" + "IsWithSummary=true";
                    OptionMenu optionMenu = new OptionMenu(getActivity());
                    optionMenu.makePayment(Fragment_Self_Check_In.this, bundle, header, params, R.id.action_Self_check_In_to_Payment, 1);
                } else {
                    CustomToast.showToast(getActivity(), "Please Insert Current OdometerIn", 1);
                }
                /*new ApiService(new OnResponseListener() {
                    @Override
                    public void onSuccess(String response, HashMap<String, String> headers) {
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
                                            // JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                            final JSONObject customerProfile= responseJSON.getJSONObject("Data");
                                            loginRes.storedata("CustomerProfile", customerProfile.toString());
                                            UserData.UserDetail = customerProfile.toString();
                                            CustomerProfile customerProfile1 = new CustomerProfile();
                                            customerProfile1 =  loginRes.callFriend("CustomerProfile", CustomerProfile.class);
                                            UserData.customerProfile = customerProfile1;
                                            bundle.putSerializable("customerDetail",customerProfile1);
                                            NavHostFragment.findNavController(Fragment_Self_Check_In.this)
                                                    .navigate(R.id.action_Self_check_In_to_Payment,bundle);

                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }



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

                    }
                }, RequestType.GET, GETCUSTOMER, BASE_URL_CUSTOMER, header, bodyParam);*/


                 /*   try {
                        JSONObject bodyParam = new JSONObject();
                        try
                        {
                            for(int i=0;i<ImageList.length();i++)
                            {
                                try
                                {
                                    System.out.println(i);
                                    JSONObject imgObj = ImageList.getJSONObject(i);
                                    String imgPath = imgObj.getString("fileBase64");
                                    try {
                                        File imgFile = new File(imgPath);

                                            Uri selectedImage = Uri.fromFile(imgFile);
                                            System.out.println(selectedImage);

                                            Bitmap bitmap = getScaledBitmap(selectedImage, 400, 400);
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                            byte[] image = stream.toByteArray();
                                            String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                                            imgObj.put("fileBase64", img_str_base64);
                                      //  ImageList.put(imgObj);
                                    }catch (FileNotFoundException e)
                                    {
                                        e.printStackTrace();

                                    } catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            bodyParam.accumulate("AgreementId",AgreementsBundle.getInt("agreement_ID"));
                            bodyParam.accumulate("VehicleId",AgreementsBundle.getInt("vehicle_ID"));
                            bodyParam.accumulate("odometerOut",Integer.parseInt(txtOdometerOut.getText().toString()));
                            bodyParam.accumulate("odometerIn",Integer.parseInt(txt_OdoMeterIn.getText().toString()));
                            bodyParam.accumulate("gasTank",Integer.parseInt(txtGasTankOut.getText().toString().substring(0,txtGasTankOut.getText().length()-1)));
                            bodyParam.accumulate("gasTankIn",Integer.parseInt(txtGasTankIn.getText().toString().substring(0,txtGasTankIn.getText().length()-1)));
                            bodyParam.accumulate("totalMilesAllowed",Double.parseDouble(txttotalMilesAllowed.getText().toString()));
                            bodyParam.accumulate("extraMilesCharge",Double.parseDouble(txtExtraMileageCharge.getText().toString()));
                            bodyParam.accumulate("totalGasCharge",Double.parseDouble(txtTotalGasCharge.getText().toString().substring(0,txtTotalGasCharge.getText().length()-3)));
                            bodyParam.accumulate("extraDayCharge",Double.parseDouble(txtExtraDayRentalCharge.getText().toString()));


                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = dateFormat.parse(txtoriginalCheckInDate.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                            originalCheckInDate = sdf.format(date);

                            bodyParam.accumulate("originalCheckInDate",originalCheckInDate);

                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                            Date date1 = dateFormat1.parse(txtactualReturnDate.getText().toString());
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                            actualReturnDate = sdf1.format(date1);

                            bodyParam.accumulate("ActualReturnDate",actualReturnDate);
                            bodyParam.accumulate("originalCheckInLocationId",originalCheckInLocationId);
                            bodyParam.accumulate("actualReturnLocationId",actualReturnLocationId);

                            bodyParam.accumulate("ImageList", ImageList);
                            System.out.println("bodyParam"+bodyParam);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        ApiService ApiService = new ApiService(UpdatecheckIn, RequestType.POST,
                                UPDATESELFCHECKIN, BASE_URL_CHECKIN, new HashMap<String, String>(), bodyParam);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }*/
            }
        });

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            final JSONObject getReservationList = responseJSON.getJSONObject("Data");
                            vehicleModel = loginRes.getModel(getReservationList.toString(), VehicleModel.class);

                            binding.GasTankCapacity.setText(vehicleModel.TankValue);
                            binding.GasTankCharge.setText(Helper.getAmtount(reservationSummarry.ReservationRatesModel.ExtraFuelCharge, true) + " /"+vehicleModel.FuelUnit.substring(0,2));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.GET, VEHICLEGETBYID, BASE_URL_LOGIN, header, "?id=" + reservationSummarry.ReservationVehicleModel.VehicleId + "&isActive=true");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_self_checkin;
    }

    OnResponseListener GetSelfCheckIN = new OnResponseListener()
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
                            try
                            {
                                //selfCheckOutObject
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                JSONObject selfCheckIn=resultSet.getJSONObject("selfCheckInModel");

                                final String odometerOut = selfCheckIn.getString("odometerOut");
                                final String odometerIn = selfCheckIn.getString("odometerIn");
                                final int gasTank = selfCheckIn.getInt("gasTank");
                                final int gasTankIn = selfCheckIn.getInt("gasTankIn");
                                final int vehicleId = selfCheckIn.getInt("vehicleId");
                                final int agreementId = selfCheckIn.getInt("agreementId");
                                final String originalCheckInDate = selfCheckIn.getString("originalCheckInDate");
                                originalCheckInLocationId = selfCheckIn.getInt("originalCheckInLocationId");
                                final String actualReturnDate = selfCheckIn.getString("actualReturnDate");
                                actualReturnLocationId = selfCheckIn.getInt("actualReturnLocationId");

                                final int isUnlimitedMiles = selfCheckIn.getInt("isUnlimitedMiles");
                                final int isUnlimitedGas = selfCheckIn.getInt("isUnlimitedGas");
                                final double totalMilesAllowed = selfCheckIn.getDouble("totalMilesAllowed");
                                final double extraMilesCharge = selfCheckIn.getDouble("extraMilesCharge");
                                final double totalGasCharge = selfCheckIn.getDouble("totalGasCharge");
                                final double extraDayCharge = selfCheckIn.getDouble("extraDayCharge");
                                final double totalExtraDays = selfCheckIn.getDouble("totalExtraDays");
                                final double gasChargePerLtr = selfCheckIn.getDouble("gasChargePerLtr");
                                String vehicleName = selfCheckIn.getString("vehicleName");
                                final String vehicleNumber = selfCheckIn.getString("vehicleNumber");
                                final String vehicleImagePath = selfCheckIn.getString("vehicleImagePath");
                                final String tankCapacity = selfCheckIn.getString("tankCapacity");
                                final double extraChargePerMile = selfCheckIn.getDouble("extraChargePerMile");
                                final double extraChargePerDay = selfCheckIn.getDouble("extraChargePerDay");

                                String s = String.valueOf(gasTank);
                                txtGasTankOut.setText(s+"%");

                                customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                                {
                                    public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser)
                                    {
                                        try
                                        {
                                            txtGasTankOut.setText(String.valueOf(progress+"%"));
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    public void onStartTrackingTouch (SeekBar seekBar)
                                    {
                                    }

                                    public void onStopTrackingTouch (SeekBar seekBar)
                                    {
                                    }
                                });


                                String url1 = serverpath + vehicleImagePath.substring(2);
                                imageLoader.displayImage(url1, VehImg);

                                txtVehicleNumber.setText(vehicleNumber);

                                vehicleName = vehicleName.substring(10);
                                txtvehicleName.setText(vehicleName);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(originalCheckInDate);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
                                String originalCheckInDateStr = sdf.format(date);

                                txtoriginalCheckInDate.setText(originalCheckInDateStr);

                                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date1 = dateFormat1.parse(actualReturnDate);
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
                                String oactualReturnDateStr = sdf1.format(date1);

                                txtactualReturnDate.setText(oactualReturnDateStr);
                                txt_OdoMeterIn.setText(odometerIn);
                                txtOdometerOut.setText(odometerOut);

                                txttotalMilesAllowed.setText(((String.format(Locale.US,"%.2f",totalMilesAllowed))));

                                txtGasTankCapacity.setText(tankCapacity+" Liters");
                                String GasTankOutStr =String.valueOf(gasTank);
                                String GasTankInSrr = String.valueOf(gasTankIn);

                                txtGasTankOut.setText(GasTankOutStr+"%");
                                txtGasTankIn.setText(GasTankInSrr+"%");

                                String gasChargePerLtrStr=(((String.format(Locale.US,"%.2f",gasChargePerLtr))));
                                txtGasTankCharge.setText(gasChargePerLtrStr+" US$");

                                String totalGasChargeStr=(((String.format(Locale.US,"%.2f",totalGasCharge))));
                                txtTotalGasCharge.setText(totalGasChargeStr+" US$");



                                txtExtraDayRentalCharge.setText(((String.format(Locale.US,"%.2f",extraChargePerDay))));
                                txtExtraMileageCharge.setText(((String.format(Locale.US,"%.2f",extraChargePerMile))));

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

    //UpdateCheckIN
    OnResponseListener UpdatecheckIn = new OnResponseListener()
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
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            finalImageList = new JSONArray(getArguments().getString("ImageList"));
                            Bundle SelfCheckInBundle=new Bundle();
                            AgreementsBundle.putString("originalCheckInDate",originalCheckInDate);
                            AgreementsBundle.putString("ActualReturnDate",actualReturnDate);
                            AgreementsBundle.putInt("originalCheckInLocationId",originalCheckInLocationId);
                            AgreementsBundle.putInt("actualReturnLocationId",actualReturnLocationId);
                            SelfCheckInBundle.putBundle("AgreementsBundle",AgreementsBundle);
                            SelfCheckInBundle.putString("ImageList",finalImageList.toString());
                            System.out.println(SelfCheckInBundle);
                            NavHostFragment.findNavController(Fragment_Self_Check_In.this)
                                    .navigate(R.id.action_Self_check_In_to_SummaryOfChargeForSelfCheckIn,SelfCheckInBundle);

                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,0);

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

    OnResponseListener AvailableLocation = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("Status");
                        Log.d("TAG", "run: " + status);


                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray locationlist = resultSet.getJSONArray("Data");
                            locationListsData = loginRes.getModel(locationlist.toString(), LocationList[].class);
                            int idd=0;
                            for (int i = 0; i <locationlist.length() ; i++) {
                                JSONObject test = (JSONObject) locationlist.get(i);
                                location.add(locationListsData[i].Name);
                                if (reservations.PickUpLocationName.matches(locationListsData[i].Name)){
                                    idd = i;
                                }
                            }
                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1,location);
                            txtcheck_Out_Location_Name.setAdapter(adapterCategories);
                            txtcheck_Out_Location_Name.setSelection(idd);

                         /*   txtcheck_Out_Location_Name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity().getApplication(), R.layout.spinner_layout, R.id.text1,location);
                                    txtcheck_Out_Location_Name.setAdapter(adapterCategories);

                                }
                            });*/

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
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    OnResponseListener Odometer = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
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
                        Boolean status = responseJSON.getBoolean("Status");
                        Log.d("TAG", "run: " + status);


                        if (status) {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            reservationCheckout = loginRes.getModel(resultSet.toString(), ReservationCheckout.class);

                           // dsfsdfs
                                    setodmeter(txt_OdoMeterIn,String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo));
                            //txt_OdoMeterIn.setText(String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo));
                            setodmeter(txtOdometerOut,String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo));
                          //  txtOdometerOut.setText(String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo));
                            enablechangeEdit(txtOdometerOut);
                            int s= Integer.valueOf(reservationCheckout.ReservationCheckOutModel.CurrentFuel.intValue());

                            customSeekBar.setProgress(Integer.parseInt(String.valueOf(s)));
                            txtGasTankIn.setText(reservationCheckout.ReservationCheckOutModel.CurrentFuel+"%");
                            enablechangeEdit(txtGasTankIn);

                            txtGasTankOut.setText(reservationCheckout.ReservationCheckOutModel.CurrentFuel+"%");
                            enablechangeEdit(txtGasTankOut);

                            Log.e("TAG", "run: " + resultSet );
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
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    OnResponseListener SummaryCharge = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        validation = true;
                        if (status) {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            JSONArray summarry = resultSet.getJSONArray("ReservationSummaryModels");
                            charges = loginRes.getModel(summarry.toString(), ReservationSummaryModels[].class);

                            try {
                                rsummarry.removeAllViews();
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }

                            summaryDisplay.getB2BSummarry(bundle,charges,rsummarry);
                            Log.e(TAG, "run: " +  summaryDisplay.getDatafrom(charges,100) );
                            bundle.putString("netrate",   summaryDisplay.getDatafrom(charges,100));

                            /*for (int i = 0; i <charges.length ; i++){
                                RelativeLayout.LayoutParams subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                subparams.addRule(RelativeLayout.BELOW, (200 + i - 1));
                                subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                subparams.setMargins(0, 10, 0, 0);
                                LayoutInflater subinflater;
                                subinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                RowSummarryChargeHeadBinding rowSummarryChargeHeadBinding = RowSummarryChargeHeadBinding.inflate(subinflater, getActivity().findViewById(android.R.id.content), false );
                                rowSummarryChargeHeadBinding.getRoot().setId(200 + i);
                                rowSummarryChargeHeadBinding.getRoot().setLayoutParams(subparams);
                                rowSummarryChargeHeadBinding.chargename.setText(charges[i].SummaryName);
                                rowSummarryChargeHeadBinding.charge.setText(Helper.getAmtount( charges[i].TotalAmount,true));

                                for (int j = 0; j <charges[i].ReservationSummaryDetailModels.length ; j++) {
                                    RelativeLayout.LayoutParams subparams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    subparams1.addRule(RelativeLayout.BELOW, (200 + i - 1));
                                    subparams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    subparams1.setMargins(0, 10, 0, 0);
                                    LayoutInflater subinflater1;
                                    subinflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                    ListChargesBinding listChargesBinding = ListChargesBinding.inflate(subinflater1, getActivity().findViewById(android.R.id.content), false );
                                    listChargesBinding.getRoot().setId(200 + j);
                                    listChargesBinding.getRoot().setLayoutParams(subparams1);

                                    listChargesBinding.textHeader.setText(charges[i].ReservationSummaryDetailModels[j].Title);
                                    listChargesBinding.textdetail.setText(charges[i].ReservationSummaryDetailModels[j].Description);
                                    try {
                                        if (charges[i].ReservationSummaryDetailModels[j].Title.length() != 0) {
                                            rowSummarryChargeHeadBinding.listsummarry.addView(listChargesBinding.getRoot());
                                   *//* rowSummarryChargeHeadBinding.sumarrydetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (rowSummarryChargeHeadBinding.listsummarry.getVisibility() == View.VISIBLE){

                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.GONE);
                                            } else {
                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });*//*
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    //rowSummarryChargeHeadBinding.listsummarry.addView(listChargesBinding.getRoot());
                                }

                                rsummarry.addView(rowSummarryChargeHeadBinding.getRoot());
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "onError: " + error );
        }
    };

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

    private void enablechangeEdit(EditText edtEmail){
        edtEmail.setFocusable(false);
        edtEmail.setEnabled(false);
        edtEmail.setCursorVisible(false);
        edtEmail.setKeyListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discard:
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Self_Check_In.this)
                        .popBackStack();
                break;
        }
    }

    public void setodmeter(EditText e, String valuee){

        String va =String.valueOf(valuee);
        int p = va.trim().length();
        String t = "";
        if (p != 6){
            int tt = 6-p;
            for (int i = 0; i <tt; i++) {
                t += "0";
            }

        }

        for (int i = 0; i <va.length(); i++) {
            char c = va.charAt(i);
            t += String.valueOf(c);
        }
        e.setText(t);

    }
}
