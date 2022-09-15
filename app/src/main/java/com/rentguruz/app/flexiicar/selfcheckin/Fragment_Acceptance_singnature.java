package com.rentguruz.app.flexiicar.selfcheckin;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService2;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentAcceptanceSignatureBinding;
import com.rentguruz.app.flexiicar.user.Fragment_Summary_Of_Charges_For_Agreements;
import com.rentguruz.app.model.ReservationSignatureModel;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.rentguruz.app.R;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOADSIGNATURE;

public class Fragment_Acceptance_singnature extends BaseFragment
{
    LinearLayout lblNext;
    ImageView imgback;
    SignaturePad signaturePad;
    TextView txtclear;
    CheckBox checkBoxTC;
    //Bundle AgreementsBundle;
    //JSONArray ImageList = new JSONArray();
    TextView txt_Savesign,txt_date;
    Bitmap bitmap;
    String path;
    ReservationSignatureModel signatureModel;
    private static final String IMAGE_DIRECTORY ="/signdemo";
 //   Bundle bundle = new Bundle();
 ReservationSummarry reservationSummarry = new ReservationSummarry();
    FragmentAcceptanceSignatureBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentAcceptanceSignatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.setUiColor(UiColor);
        lblNext = view.findViewById(R.id.lblback_acceptance_sign);
        imgback = view.findViewById(R.id.backimg_acceptance_sign);
        signaturePad = view.findViewById(R.id.signaturePad);
        txtclear = view.findViewById(R.id.txt_clearsign);
        checkBoxTC = view.findViewById(R.id.CheckBoxTC);
        txt_Savesign = view.findViewById(R.id.txt_Savesign);
        txt_date = view.findViewById(R.id.txt_date);
        bundle.putSerializable( "reservation",getArguments().getSerializable("reservation"));
        reservationSummarry = Fragment_Summary_Of_Charges_For_Agreements.reservationSummarry;
        Log.e(TAG, "onViewCreated: "+ reservationSummarry.CustomerId);
        bundle.putSerializable("signature",1);
        txtclear.setEnabled(false);
        signatureModel = new ReservationSignatureModel();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String datetime = dateformat.format(c.getTime());
        txt_date.setText(datetime);

        try {
         //   ImageList = new JSONArray(getArguments().getString("ImageList"));
         //   AgreementsBundle = getArguments().getBundle("AgreementsBundle");

            txt_Savesign.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    bitmap = signaturePad.getSignatureBitmap();
                    path = saveImage(bitmap);
                    try {
                        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String img_strbase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                   /*     try {
                            JSONObject signObj = new JSONObject();
                            signObj.put("Doc_For", 22);
                            signObj.put("fileBase64",img_strbase64);

                            System.out.println(signObj);
                            //ImageList.put(signObj);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                        signatureModel.img64 = img_strbase64;

                     //   signatureModel.ReservationId = getArguments().getInt("Id");
                        signatureModel.ReservationId =reservationSummarry.Id;
                        //reservation customer id
                        signatureModel.SignBy = reservationSummarry.CustomerId;
                        signatureModel.Action = 1;
                        signatureModel.SignType = 3;
                        signatureModel.Latitude = Fragment_Vehicle_Image_1.lat;
                        signatureModel.Longitude = Fragment_Vehicle_Image_1.log;
                        ApiService2 apiService = new ApiService2(uploadSign, RequestType.POST, UPLOADSIGNATURE, BASE_URL_LOGIN,header,signatureModel);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println(path);
                }
            });
            lblNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try{
                        if (signaturePad.isEmpty()){
                            String msg = "Please Signature";
                            CustomToast.showToast(getActivity(),msg,1);
                        } else {
                            if(checkBoxTC.isChecked())
                            {
                                Bundle SelfCheckInBundle = new Bundle();
                                // SelfCheckInBundle.putString("ImageList", ImageList.toString());
                                // SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                                System.out.println(SelfCheckInBundle);
                            /*NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                    .navigate(R.id.action_Signature_to_Self_check_In, bundle);*/
/*
                            NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                    .navigate(R.id.locationkey,bundle);*/

                                bitmap = signaturePad.getSignatureBitmap();
                                path = saveImage(bitmap);
                                try {
                                    Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                                    String img_strbase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                   /*     try {
                            JSONObject signObj = new JSONObject();
                            signObj.put("Doc_For", 22);
                            signObj.put("fileBase64",img_strbase64);

                            System.out.println(signObj);
                            //ImageList.put(signObj);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                                    signatureModel.img64 = img_strbase64;

                                    // signatureModel.ReservationId = getArguments().getInt("Id");
                                    signatureModel.ReservationId = reservationSummarry.Id;
                                    signatureModel.SignBy = reservationSummarry.CustomerId;
                                    signatureModel.Action = 1;
                                    signatureModel.SignType = 3;
                                    signatureModel.Latitude = Fragment_Vehicle_Image_1.lat;
                                    signatureModel.Longitude = Fragment_Vehicle_Image_1.log;
                                    ApiService2 apiService = new ApiService2(uploadSign, RequestType.POST, UPLOADSIGNATURE, BASE_URL_LOGIN,header,signatureModel);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                        .navigate(R.id.action_Signature_to_summary,bundle);

                            }
                            else {
                                String msg = "Please accept term & condition";
                                CustomToast.showToast(getActivity(),msg,1);
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle SelfCheckInBundle=new Bundle();
                  //  SelfCheckInBundle.putString("ImageList",ImageList.toString());
                  //  SelfCheckInBundle.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(SelfCheckInBundle);
                    NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                            .navigate(R.id.action_Signature_to_Vehicle_All_image_SelfCheckIn,SelfCheckInBundle);
                }
            });

            signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener()
            {
                @Override
                public void onStartSigning()
                {
                }
                @Override
                public void onSigned()
                {
                    txtclear.setEnabled(true);
                    signaturePad.setEnabled(true);
                }

                @Override
                public void onClear()
                {
                    txtclear.setEnabled(false);
                }
            });
            txtclear.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    signaturePad.clear();
                }
            });

            TextView  termsconditions = view.findViewById(R.id.termsconditions);
            termsconditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                            .navigate(R.id.action_Signature_to_Termscondition,bundle);
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            //CustomToast.showToast(getActivity(),"File Saved::--->"+f.getAbsolutePath(),1);

            Uri SignImage = Uri.fromFile(f);

            return f.getAbsolutePath();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

    }

    OnResponseListener uploadSign = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d("TAG", "onSuccess: " + response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status) {
                            JSONObject data = responseJSON.getJSONObject("Data");

                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(), msg, 0);
                            NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                    .navigate(R.id.action_Signature_to_summary);

                        } else {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(), msg, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d("TAG", "onError: " + error);
        }
    };

}
