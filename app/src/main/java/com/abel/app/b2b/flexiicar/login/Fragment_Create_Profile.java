package com.abel.app.b2b.flexiicar.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.acuant.acuantcamera.camera.AcuantBaseCameraFragment;
import com.acuant.acuantcamera.camera.AcuantCameraActivity;
import com.acuant.acuantcamera.camera.AcuantCameraOptions;
import com.acuant.acuantcamera.camera.mrz.AcuantMrzCameraFragment;
import com.acuant.acuantcamera.overlay.MrzRectangleView;
import com.abel.app.b2b.R;
import com.abel.app.b2b.ScanDrivingLicense;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.home.more.Activity_MoreTab;
import com.abel.app.b2b.model.DoRegistration;

import static com.acuant.acuantcamera.constant.Constants.ACUANT_EXTRA_CAMERA_OPTIONS;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.LOGIN;

public class Fragment_Create_Profile  extends Fragment
{
    public static DoRegistration registration;
    String TAG = "JVM";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        registration = new DoRegistration();
        return inflater.inflate(R.layout.fragment_create_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        LinearLayout lbllogin = view.findViewById(R.id.lbl_enter_details);

        ImageView BackToLogin=view.findViewById(R.id.BackToLogin);



        BackToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               /* Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                startActivity(i);*/

                if (Helper.RegistrationD){
                    Helper.RegistrationD = false;
                    Intent i = new Intent(getActivity(), Login.class);
                    startActivity(i);
                } else {
                    Helper.isRegistrationDone = true;
                    Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                    startActivity(i);
                }

              //  NavHostFragment.findNavController(Fragment_Create_Profile.this).popBackStack();
            }
        });
        lbllogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Create_Profile.this)
                        .navigate(R.id.action_CreateProfile_to_DriverProfile);
            }
        });

        ImageView imgScanDrivingLicense = view.findViewById(R.id.imgScanDrivingLicense);

      /*  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
        }*/

       /* imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                i.putExtra("afterScanBackTo", 1);
                startActivity(i);

            }
        });
*/

  /*      AcuantCameraOptions mrzCameraOptionsBuilder = new AcuantCameraOptions.MrzCameraOptionsBuilder().setAllowBox(true)
                .setBracketLengthInHorizontal(100)
                .setBracketLengthInVertical(100)
                .setDefaultBracketMarginHeight(400)
                .setDefaultBracketMarginWidth(400)
                .build();


        AcuantBaseCameraFragment.CameraState cameraState = AcuantBaseCameraFragment.CameraState.Align;
        AcuantMrzCameraFragment.newInstance().setMenuVisibility(true);*/

        //MrzRectangleView mrzRectangleView = view.findViewById(R.id.acu_mrz_rectangle);

        //mrzRectangleView.setVisibility(View.VISIBLE);
        //mrzRectangleView.setViewFromState(AcuantBaseCameraFragment.CameraState.Capturing);

        LinearLayout scanimglayout = view.findViewById(R.id.scanimglayout);
        scanimglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanimglayout.setVisibility(View.GONE);
                //mrzRectangleView.setVisibility(View.VISIBLE);
                //mrzRectangleView.setViewFromState(cameraState);
               /* Intent cameraIntent = new Intent(getActivity(), AcuantCameraActivity.class);
                cameraIntent.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS,
                        new AcuantCameraOptions.DocumentCameraOptionsBuilder()
                                .build());*/

                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                i.putExtra("afterScanBackTo", 1);
                startActivity(i);
            }
        });
    }


}

