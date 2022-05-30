package com.rentguruz.app.flexiicar.selfcheckin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.LoginRes;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentSelfCheckinBinding;
import com.rentguruz.app.databinding.FragmentSelfChekoutBinding;
import com.rentguruz.app.flexiicar.user.Fragment_Summary_Of_Charges_For_Agreements;
import com.rentguruz.app.model.AttachmentsModel;
import com.rentguruz.app.model.CheckOutList;
import com.rentguruz.app.model.ReservationCheckListModels;
import com.rentguruz.app.model.ReservationCheckin;
import com.rentguruz.app.model.response.ReservationSummarry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKINN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKLISTFORACCESORRIES;
import static com.rentguruz.app.apicall.ApiEndPoint.FlEETCHECKLIST;

import com.rentguruz.app.R;
public class Fragment_Check_in extends BaseFragment //implements View.OnClickListener
{
    private static final int RECORD_AUDIOO = 100;
    public static final int RequestPermissionCode = 1;
    LinearLayout linearLayout;
    ImageView imgback,menu_Icon,Speaker;
    Button StartRecord;
    ImageView imageViewStop,imageViewPlay;
    Chronometer chronometer;
    private int lastProgress = 0;
    private boolean isPlaying = false;
   // Handler handler = new Handler();
    public static Context context;
    TextView  txt_progressvalue,txtDiscard; //txt_OdoMeter, ;
    EditText txt_OdoMeter;
    SeekBar AudiorecordSeekbar,customSeekBar;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private String fileName;
    EditText Edt_Notes;
    Bundle AgreementsBundle;
    Bundle back;
    // Bundle bundle = new Bundle();
    List<AttachmentsModel> attachmentsModelList = new ArrayList<>();
    //DoHeader header;
    //CommonParams params;
    //ReservationCheckout reservationCheckout;
    ReservationCheckin reservationCheckout;
    ReservationSummarry reservationSummarry = new ReservationSummarry();
    //LoginRes loginRes;
    //HashMap<Integer,Integer > listCheck = new HashMap<>();
  //  CheckOutList[] CheckListS;
    int id;
    List<CheckOutList> checkOutLists = new ArrayList<>();
    FragmentSelfChekoutBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*params = new CommonParams();
        header = new DoHeader();
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.ut = "PYOtYmuTsLQ=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";*/
        binding = FragmentSelfChekoutBinding.inflate(inflater,container,false);
//        return inflater.inflate(R.layout.fragment_self_chekout, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);
         //   Log.e("TAG1", "onViewCreated: " + CheckList.Agreement);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding.setUiColor(UiColor);
            mPlayer=new MediaPlayer();
            loginRes = new LoginRes(getContext());
            reservationCheckout = new ReservationCheckin();
            bundle.putSerializable("image", (Serializable) attachmentsModelList);
            bundle.putInt("length", 10);
            bundle.putInt("temp",1);
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("resrvation");
            bundle.putSerializable("resrvation",getArguments().getSerializable("resrvation"));
            bundle.putSerializable("reservation",getArguments().getSerializable("reservation"));
            checkOutLists = (List<CheckOutList>)  getArguments().getSerializable("checklist");
            //bundle.putSerializable("resrvation", reservationSummarry);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            AgreementsBundle = getArguments().getBundle("AgreementsBundle");
            bundle.putInt( "Id", getArguments().getInt("Id"));
            Log.e(TAG, "RID " + getArguments().getInt("Id"));
            back = getArguments().getBundle(getActivity().getResources().getString(R.string.bundle));
            if (reservationSummarry != null){

            } else {
                back.getBundle(getActivity().getResources().getString(R.string.bundle));
                reservationSummarry = (ReservationSummarry) back.getSerializable("resrvation");
                checkOutLists =  (List<CheckOutList>)  back.getSerializable("checklist");
            }
            if (reservationSummarry.ReservationVehicleModel.VehicleTypeId == 0 ){
               reservationSummarry =  Fragment_Summary_Of_Charges_For_Agreements.reservationSummarry;
            }
            bundle.putSerializable("resrvation",reservationSummarry);
            Log.e(TAG, "onViewCreated: " + reservationSummarry.ReservationVehicleModel.VehicleTypeId + " " +  reservationSummarry.ReservationVehicleModel.VehicleId);
            id = getArguments().getInt("Id");
            id = reservationSummarry.Id;
            bundle.putInt("Id",reservationSummarry.Id);
            reservationCheckout.ReservationId = id;
            reservationCheckout.ReservationCheckInModel.ReservationId=id;
            reservationCheckout.ReservationVehicleModel.VehicleTypeId = reservationSummarry.ReservationVehicleModel.VehicleTypeId;
            reservationCheckout.ReservationVehicleModel.VehicleId = reservationSummarry.ReservationVehicleModel.VehicleId;
            reservationCheckout.ReservationCheckInModel.ReservationId = id;
            reservationCheckout.ReservationVehicleModel.ReservationId = id;
            reservationCheckout.ReservationVehicleModel.VehicleId = reservationSummarry.ReservationVehicleModel.VehicleId;
            reservationCheckout.ReservationCheckInModel.VehicleId = reservationSummarry.ReservationVehicleModel.VehicleId;

         /*   if (ActivityCompat.checkSelfPermission(getActivity(), RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, RECORD_AUDIOO);
            }*/

            linearLayout = view.findViewById(R.id.backlbl_Selfcheckout);
            imgback = view.findViewById(R.id.back);
            customSeekBar = view.findViewById(R.id.simpleSeekBar);

            txt_OdoMeter = view.findViewById(R.id.txt_OdoMeter);
            txt_progressvalue = view.findViewById(R.id.txt_progressvalue);
            Edt_Notes=view.findViewById(R.id.Edt_Notes);


            Speaker=view.findViewById(R.id.Speaker);
            menu_Icon=view.findViewById(R.id.menu_Icon);
            AudiorecordSeekbar=view.findViewById(R.id.SeekBar);
            chronometer=view.findViewById(R.id.cronometer);

            StartRecord=view.findViewById(R.id.Startrecord);
            imageViewStop=view.findViewById(R.id.imageViewStop);
            imageViewPlay=view.findViewById(R.id.imageViewPlay);

            StartRecord.setOnClickListener(this);
            imageViewStop.setOnClickListener(this);
            imageViewPlay.setOnClickListener(this);

            TextView screenHeader = view.findViewById(R.id.screenHeader);
            screenHeader.setText(companyLabel.CheckIn);
            TextView oddo = view.findViewById(R.id.oddo);
            oddo.setText("Odometer In");
            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    /*NavHostFragment.findNavController(Fragment_Self_Check_out.this)
                            .navigate(R.id.action_Self_check_Out_to_SummaryOfChargesForAgreements,Agreements);*/
                    NavHostFragment.findNavController(Fragment_Check_in.this).popBackStack();
                }
            });
            txtDiscard=view.findViewById(R.id.discard);

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Check_in.this).popBackStack();
                   /* NavHostFragment.findNavController(Fragment_Self_Check_out.this)
                            .navigate(R.id.action_Self_check_Out_to_Agreements);*/
                }
            });

            AssetManager am = getActivity().getApplicationContext().getAssets();
            Typeface typefaceFSSiena = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "DS-DIGI.TTF"));
            txt_OdoMeter.setTypeface(typefaceFSSiena);
            String va =String.valueOf(Helper.checkinod);
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

           // txt_OdoMeter.setText(String.valueOf(Helper.checkinod));
            txt_OdoMeter.setText(t);
          //  txt_OdoMeter.setText("001600");


            linearLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                                     /*   AgreementsBundle.putString("AudioFileBase64",fileName);
                                        AgreementsBundle.putString("Notes",Edt_Notes.getText().toString());
                                        AgreementsBundle.putString("gasTank",txt_progressvalue.getText().toString());
                                        Bundle Agreements=new Bundle();
                                        Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                                        System.out.println(Agreements);*/
                //    if (reservationCheckout.ReservationVehicleModel.CurrentOdo <Integer.parseInt(txt_OdoMeter.getText().toString())) {
                        reservationCheckout.ReservationCheckInModel.CheckInOdo = Integer.parseInt(txt_OdoMeter.getText().toString());
                        reservationCheckout.ReservationCheckInModel.
                                ReturnFuelInPR = customSeekBar.getProgress();
                    //reservationCheckout.ReservationCheckOutModel.CheckOutOdo = Integer.parseInt(txt_OdoMeter.getText().toString());
                        reservationCheckout.ReservationCheckInModel.CheckInOdo = Integer.parseInt(txt_OdoMeter.getText().toString());
                    reservationCheckout.ReservationCheckInModel.Note = Edt_Notes.getText().toString();
                        reservationCheckout.ReservationVehicleModel.CurrentOdo = Integer.parseInt(txt_OdoMeter.getText().toString());

                    reservationCheckout.ReservationCheckInModel.ReturnDate =  Helper.checkindate;
                    reservationCheckout.ReservationCheckInModel.ExtraMiles = Helper.checkinod;
                    reservationCheckout.ReservationCheckInModel.IsActive = true;
                    reservationCheckout.ReservationCheckInModel.IsNoExtraFuel = true;
                    reservationCheckout.ReservationCheckInModel.IsNoExtraMiles = true;
                    reservationCheckout.ReservationCheckInModel.ReturnLocation = reservationSummarry.DropLocation;
                    //reservationCheckout.ReservationCheckInModel.ReturnSummary = loginRes.modeltostring(Fragment_Self_Check_In.charges);
                    reservationCheckout.ReservationCheckInModel.ReturnSummary = loginRes.modeltostring(reservationSummarry.ReservationSummaryModels);
                    reservationCheckout.ReservationCheckInModel.VehicleStatus = 0;


                   // ApiService apiService = new ApiService(GetSelfCheckOuts,RequestType.POST,CHECKLISTFORACCESORRIES, BASE_URL_LOGIN, header, params.getCheckListAccessories());
                    loginRes.testingLog(TAG,reservationCheckout);
                    Log.e(TAG, "onClick: " + reservationCheckout );

                    if (validation()){
                        bundle.putSerializable("checkoutmodel", reservationCheckout);
                       /* NavHostFragment.findNavController(Fragment_Check_in.this)
                                .navigate(R.id.payment_sucess_to_vehicleimage,bundle);*/

                        NavHostFragment.findNavController(Fragment_Check_in.this)
                                .navigate(R.id.extrachecklist,bundle);
                       // ApiService2 apiService2 = new ApiService2(ReservatioCheckouts, RequestType.POST, CHECKINN, BASE_URL_LOGIN, header ,reservationCheckout);
                    } else {
                        CustomToast.showToast(getActivity(), "Please Select Check Box", 1);
                    }



                   /* } else {
                        CustomToast.showToast(getActivity(), "Please Fill Odometer greater Than "+ reservationCheckout.ReservationVehicleModel.CurrentOdo, 1);
                    }*/
                   /* NavHostFragment.findNavController(Fragment_Self_Check_out.this)
                            .navigate(R.id.action_Self_check_Out_to_Vehicle_Image_1,bundle);*/
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

            ApiService ApiService = new ApiService(GetSelfCheckOut, RequestType.GET,
                    GETSELFCHECKOUT, BASE_URL_CHECKOUT, new HashMap<String, String>(), bodyParam);*/

            //String s = String.valueOf("60");
            //txt_progressvalue.setText(s+"%");
            customSeekBar.setProgress(Integer.parseInt(String.valueOf(Helper.checkinFuel)));
            txt_progressvalue.setText(String.valueOf(Helper.checkinFuel+" %"));
            customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser)
                {
                    try
                    {
                        txt_progressvalue.setText(String.valueOf(progress+"%"));
                        reservationCheckout.ReservationCheckInModel.
                                ReturnFuelInPR = progress;


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

            RelativeLayout rl_Accessories = view.findViewById(R.id.rl_Accessories);
            for (int i = 0; i < checkOutLists.size(); i++) {
                int idd = checkOutLists.get(i).Id;
               // int idd = id;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.setMargins(0, 0, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout views = (LinearLayout) inflater.inflate(R.layout.list_selfcheckout, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                views.setId(200 + i);
                views.setLayoutParams(lp);

                TextView txt_chkOptionName;
                CheckBox chkred = (CheckBox) views.findViewById(R.id.chk_red);
                CheckBox chkgreen = (CheckBox) views.findViewById(R.id.chk_green);
                CheckBox chkyellow = (CheckBox) views.findViewById(R.id.chk_yellow);

                chkred.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            chkgreen.setChecked(false);
                            chkyellow.setChecked(false);
                            // reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id,idd,2,2));
                            checkList(idd,1);
                        }
                    }
                });

                chkyellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            chkgreen.setChecked(false);
                            chkred.setChecked(false);
                            checkList(idd,2);
                        }
                    }
                });

                chkgreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            chkred.setChecked(false);
                            chkyellow.setChecked(false);
                            checkList(idd,3);
                        }
                    }
                });

                txt_chkOptionName = (TextView) views.findViewById(R.id.txt_chkOptionName);
                txt_chkOptionName.setText(checkOutLists.get(i).Name);

                rl_Accessories.addView(views);
            }


            //ApiService apiService = new ApiService(GetSelfCheckOuts,RequestType.POST,CHECKLISTFORACCESORRIES, BASE_URL_CHECKOUT, header, params.getCheckListAccessories());

            new ApiService(new OnResponseListener() {
                @Override
                public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                Boolean status = responseJSON.getBoolean("Status");
                                JSONArray resultSet = responseJSON.getJSONArray("Data");
                                Log.e(TAG, "run: " + resultSet );
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(String error) {

                }
            }, RequestType.POST, FlEETCHECKLIST, BASE_URL_LOGIN, header,
                    params.getCheckListAccessories(reservationSummarry.ReservationVehicleModel.VehicleId, reservationSummarry.ReservationVehicleModel.VehicleTypeId));
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

    OnResponseListener ReservatioCheckouts = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                //UserData.reservationCheckout =  loginRes.getModel(resultSet.toString(), ReservationCheckout.class);

                                /*NavHostFragment.findNavController(Fragment_Check_in.this)
                                        .navigate(R.id.action_Self_check_Out_to_Vehicle_Image_1,bundle);*/

                                NavHostFragment.findNavController(Fragment_Check_in.this)
                                        .navigate(R.id.payment_sucess_to_vehicleimage,bundle);

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
        public void onError(String error) {

        }
    };


    OnResponseListener GetSelfCheckOuts = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.e("TAG", "onSuccess: " + response );
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
                                //selfCheckOutObject
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                JSONArray array = resultSet.getJSONArray("Data");
                                final RelativeLayout rl_Accessories = getActivity().findViewById(R.id.rl_Accessories);
                                for (int i = 0; i < array.length(); i++) {
                                    final JSONObject test = (JSONObject) array.get(i);
                                    int idd = test.getInt("Id");
                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_selfcheckout, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    view.setId(200 + i);
                                    view.setLayoutParams(lp);

                                    TextView txt_chkOptionName;
                                    CheckBox chkred = (CheckBox) view.findViewById(R.id.chk_red);
                                    CheckBox chkgreen = (CheckBox) view.findViewById(R.id.chk_green);
                                    CheckBox chkyellow = (CheckBox) view.findViewById(R.id.chk_yellow);

                                    chkred.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                chkgreen.setChecked(false);
                                                chkyellow.setChecked(false);
                                               // reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id,idd,2,2));
                                                checkList(idd,1);
                                            }
                                        }
                                    });

                                    chkyellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                chkgreen.setChecked(false);
                                                chkred.setChecked(false);
                                                checkList(idd,2);
                                            }
                                        }
                                    });

                                    chkgreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                chkred.setChecked(false);
                                                chkyellow.setChecked(false);
                                                checkList(idd,3);
                                            }
                                        }
                                    });

                                    txt_chkOptionName = (TextView) view.findViewById(R.id.txt_chkOptionName);
                                    txt_chkOptionName.setText(test.getString("Name"));

                                    rl_Accessories.addView(view);
                                }
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
        public void onError(String error) {
            Log.e("TAG", "onError: " + error );
        }
    };

    public void checkList(int idd,int i){
        Boolean value = false;
        for (int j = 0; j < reservationCheckout.ReservationCheckListModels.size() ; j++) {
            if (reservationCheckout.ReservationCheckListModels.get(j).CheckListId==idd){
                reservationCheckout.ReservationCheckListModels.get(j).Condition = i;
                value  =  true;

            }
        }

       // reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id, idd, i,i));
        if (!value){
            reservationCheckout.ReservationCheckListModels.add(new ReservationCheckListModels(id, idd, i,i));
        }
    }


    public Boolean validation(){
        Boolean value = false;

        if (reservationCheckout.ReservationCheckListModels.size() == checkOutLists.size()){
            value = true;
        }

        return value;
    }

    OnResponseListener GetSelfCheckOut = new OnResponseListener()
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
                                if(resultSet.equals("t0220_Vehicle_Damage_CheckList"))
                                {
                                    JSONObject Vehicle_Damage_CheckList = resultSet.getJSONObject("t0220_Vehicle_Damage_CheckList");
                                    final String checkListOptionIDS = Vehicle_Damage_CheckList.getString("checkListOptionIDS");
                                    final String checkListOtherDamage = Vehicle_Damage_CheckList.getString("checkListOtherDamage");
                                    final int type = Vehicle_Damage_CheckList.getInt("type");
                                }

                                //selfCheckOutModel
                                JSONObject selfCheckOutModel=resultSet.getJSONObject("selfCheckOutModel");
                                final int selfCheckOut = selfCheckOutModel.getInt("selfCheckOut");
                                final int vehicleID = selfCheckOutModel.getInt("vehicleId");
                                final int agreementId = selfCheckOutModel.getInt("agreementId");
                                final String odometerOut = selfCheckOutModel.getString("odometerOut");
                                final int gasTank=selfCheckOutModel.getInt("gasTank");
                                final String imageList=selfCheckOutModel.getString("imageList");

                                String s = String.valueOf(gasTank);
                                txt_progressvalue.setText(s+"%");

                                customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                                {
                                    public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser)
                                    {
                                        try
                                        {
                                            txt_progressvalue.setText(String.valueOf(progress+"%"));
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

                                txt_OdoMeter.setText(odometerOut);

                                linearLayout.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                     /*   AgreementsBundle.putString("AudioFileBase64",fileName);
                                        AgreementsBundle.putString("Notes",Edt_Notes.getText().toString());
                                        AgreementsBundle.putString("gasTank",txt_progressvalue.getText().toString());
                                        Bundle Agreements=new Bundle();
                                        Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                                        System.out.println(Agreements);*/
                                        NavHostFragment.findNavController(Fragment_Check_in.this)
                                                .navigate(R.id.action_Self_check_Out_to_Vehicle_Image_1);
                                    }
                                });

                                //checkListOptMasterModel
                                final JSONArray getcheckListOptMasterModel = resultSet.getJSONArray("checkListOptMasterModel");
                                final RelativeLayout rl_Accessories = getActivity().findViewById(R.id.rl_Accessories);

                                int len;
                                len = getcheckListOptMasterModel.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getcheckListOptMasterModel.get(j);

                                    final String chkOptionId = test.getString("chkOptionId");
                                    final String chkOptionName = test.getString("chkOptionName");
                                    String[] optionIds = chkOptionId.split(",");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.list_selfcheckout, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    view.setId(200 + j);
                                    view.setLayoutParams(lp);

                                    TextView txt_chkOptionName;
                                    CheckBox chkred = (CheckBox) view.findViewById(R.id.chk_red);
                                    CheckBox chkgreen = (CheckBox) view.findViewById(R.id.chk_green);
                                    CheckBox chkyellow = (CheckBox) view.findViewById(R.id.chk_yellow);

                                    for (int m = 0; m < optionIds.length; m++)
                                    {
                                        if (optionIds[m].equals(chkOptionId + "#1"))
                                        {
                                            chkgreen.setChecked(true);
                                        } else if (optionIds[m].equals(chkOptionId + "#2"))
                                        {
                                            chkyellow.setChecked(true);
                                        } else if (optionIds[m].equals(chkOptionId + "#3"))
                                        {
                                            chkred.setChecked(true);
                                        }
                                    }
                                    chkgreen.setEnabled(true);
                                    chkyellow.setEnabled(true);
                                    chkred.setEnabled(true);

                                    txt_chkOptionName = (TextView) view.findViewById(R.id.txt_chkOptionName);
                                    txt_chkOptionName.setText(chkOptionName);

                                    rl_Accessories.addView(view);
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

  /*  @Override
    public void onClick(View view)
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_AUDIO);
        } else {
            try {
                if (view == StartRecord)
                {
                    prepareforRecording();
                    startRecording();
                } else if (view == imageViewStop)
                {
                    prepareforStop();
                    stopRecording();
                } else if (view == imageViewPlay)
                {
                    if (!isPlaying && fileName != null)
                    {
                        isPlaying = true;
                        startPlaying();
                    } else {
                        isPlaying = false;
                        stopPlaying();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }*/


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.Startrecord:
                if (checkPermission()){
                    prepareforRecording();
                    startRecording();
                } else {
                    requestPermission();
                }
                break;

            case R.id.imageViewStop:
                prepareforStop();
                stopRecording();
                break;

            case R.id.imageViewPlay:
                if (!isPlaying && fileName != null)
                {
                    isPlaying = true;
                    startPlaying();
                } else {
                    isPlaying = false;
                    stopPlaying();
                }
                break;
        }
    }

    private void prepareforRecording()
    {
        imageViewStop.setVisibility(View.VISIBLE);
        imageViewStop.setImageResource(R.drawable.audio_resume);
        imageViewPlay.setVisibility(View.GONE);
    }


    private void startRecording()
    {
        try {
            //we use the MediaRecorder class to record


           // File root = android.os.Environment.getExternalStorageDirectory();
            File root = new File(Environment.getExternalStorageDirectory()
                    +"/Android/data/"
                    + getContext().getPackageName()
                    + "/Files");
          /*  File file = new File(root.getAbsolutePath()+ "/VoiceRecorderSimplifiedCoding/Audios");
            if (!file.exists())
            {
                file.mkdirs();
            }*/

            //fileName =root.getAbsolutePath()+ "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
            fileName =root.getAbsolutePath()  + String.valueOf(System.currentTimeMillis() + ".mp3");

            CustomToast.showToast(getActivity(),"filename"+fileName,1);
            Log.d(TAG,"filename "+ fileName);

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(fileName);


            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lastProgress = 0;
            AudiorecordSeekbar.setProgress(0);
            startPlaying();
            //stopPlaying();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            seekUpdation();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void stopPlaying()
    {
        try {
            if (mPlayer != null)
            {
                if (mPlayer.isPlaying())
                {
                    mPlayer.stop();
                    mPlayer.release();
                }
                mPlayer.release();
                mPlayer=null;
                imageViewPlay.setImageResource(R.drawable.audio_resume);
                chronometer.stop();
            }
        }
        catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    private void prepareforStop()
    {
        imageViewStop.setVisibility(View.GONE);
        imageViewPlay.setVisibility(View.VISIBLE);
    }
    private void stopRecording()
    {
        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        CustomToast.showToast(getActivity(),"Recording saved successfully.",1);
    }

    private void startPlaying()
    {
        mPlayer = new MediaPlayer();
        Log.d(TAG,"instartPlaying " +fileName);
        try
        {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.audio_pause);
        AudiorecordSeekbar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        AudiorecordSeekbar.setMax(mPlayer.getDuration());

        chronometer.start();
        seekUpdation();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                imageViewPlay.setImageResource(R.drawable.audio_resume);
                isPlaying = false;
                chronometer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        AudiorecordSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                Log.e(TAG, "onProgressChanged: " +  progress );
                if( mPlayer!=null && fromUser )
                {
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation()
    {
        if(mPlayer != null)
        {
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            AudiorecordSeekbar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
            Log.e(TAG, "seekUpdation: " + AudiorecordSeekbar.getProgress());
            handler.postDelayed(runnable, 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        try {
            fullProgressbar.hide();
            switch (requestCode)
            {
                case RequestPermissionCode:
                {
                    if (grantResults.length> 0) {
                        boolean StoragePermission = grantResults[0] ==
                                PackageManager.PERMISSION_GRANTED;
                        boolean RecordPermission = grantResults[1] ==
                                PackageManager.PERMISSION_GRANTED;
                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }


}
