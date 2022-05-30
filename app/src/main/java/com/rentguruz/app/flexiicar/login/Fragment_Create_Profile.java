package com.rentguruz.app.flexiicar.login;

import android.content.Intent;
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

import com.rentguruz.app.ScanDrivingLicense;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentCreateProfileBinding;
import com.rentguruz.app.home.more.Activity_MoreTab;
import com.rentguruz.app.model.DoRegistration;
import com.rentguruz.app.R;
public class Fragment_Create_Profile  extends BaseFragment
{
    public static DoRegistration registration;
    String TAG = "JVM";
    FragmentCreateProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        registration = new DoRegistration();
        //return inflater.inflate(R.layout.fragment_create_profile, container, false);
        binding = FragmentCreateProfileBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        LinearLayout lbllogin = view.findViewById(R.id.lbl_enter_details);

        ImageView BackToLogin=view.findViewById(R.id.BackToLogin);
        binding.setUiColor(UiColor);


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
                    getActivity().finish();
                } else {
                    Helper.isRegistrationDone = true;
                    Intent i = new Intent(getActivity(), Activity_MoreTab.class);
                    startActivity(i);
                    getActivity().finish();

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



     /*   AcuantTokenService acuantTokenService = new AcuantTokenService(Credential.get(), new AcuantTokenServiceListener() {
            @Override
            public void onSuccess(@NotNull String token) {
                AcuantInitializer.initializeWithToken("acuant.config.xml",
                        token, getContext(),
               // new ListBuilder<>(ImageProcessorInitializer.class , MrzCameraInitializer.class)
                )
            }

            @Override
            public void onFail(int responseCode) {

            }
        });*/

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

             /*   val digitsToShow: Int = 2,
                        val allowBox : Boolean = true,
                        val autoCapture : Boolean = true,
                        val bracketLengthInHorizontal : Int = 155,
                        val bracketLengthInVertical : Int = 255,
                        val defaultBracketMarginWidth : Int = 160,
                        val defaultBracketMarginHeight : Int = 160,
                        val colorHold : Int = Color.YELLOW,
                        val colorCapturing : Int = Color.GREEN,
                        val colorBracketAlign : Int = Color.BLACK,
                        val colorBracketCloser : Int = Color.RED,
                        val colorBracketHold : Int = Color.YELLOW,
                        val colorBracketCapturing : Int = Color.GREEN,
                        var useGMS: Boolean = true,


                AcuantCameraOptions.DocumentCameraOptionsBuilder  cameraOptionsBuilder =new AcuantCameraOptions.DocumentCameraOptionsBuilder();
                cameraOptionsBuilder.setTimeInMsPerDigit(450);
                cameraOptionsBuilder.setAllowBox(true);
                cameraOptionsBuilder.setAutoCapture(true);
                cameraOptionsBuilder.setBracketLengthInHorizontal(75);
                cameraOptionsBuilder.setBracketLengthInVertical(125);

                cameraOptionsBuilder.setDefaultBracketMarginWidth(80);
                cameraOptionsBuilder.setDefaultBracketMarginHeight(80);
                cameraOptionsBuilder.setUseGms(true);

                Intent camera = new Intent(getActivity(), AcuantCameraActivity.class);
                camera.putExtra(ACUANT_EXTRA_CAMERA_OPTIONS,
                        cameraOptionsBuilder.build()
                );

                startActivityForResult(camera, 1);*/
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {

    }
}

