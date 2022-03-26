package com.abel.app.b2b.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.CustomeDialog;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentNewAgreementFlightdetailBinding;
import com.abel.app.b2b.databinding.FragmentNewAgreementNotesBinding;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;

import java.io.File;
import java.io.IOException;

public class Fragment_New_Agreement_FlightDetail extends BaseFragment {
    private static final int RECORD_AUDIO = 100;
    FragmentNewAgreementFlightdetailBinding binding;
    private MediaPlayer mPlayer=new MediaPlayer();
    private MediaRecorder mRecorder;
    private String fileName;
    private boolean isPlaying = false;
    ReservationSummarry reservationSummarry;
    CustomeDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementFlightdetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText(getResources().getString(R.string.add) + " " + companyLabel.FlightDetails);
        binding.header.discard.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.play.setOnClickListener(this);
        binding.recorder.setOnClickListener(this);
        binding.startRecord.setOnClickListener(this);
        dialog = new CustomeDialog(context);
        reservationSummarry = new ReservationSummarry();
        bundle.putSerializable("reservationSum", (ReservationSummarry) getArguments().getSerializable("reservationSum"));
        bundle.putSerializable("Model", (VehicleModel) getArguments().getSerializable("Model"));
        bundle.putSerializable("models",(LocationList) getArguments().getSerializable("models"));
        bundle.putSerializable("model",(LocationList) getArguments().getSerializable("model"));
        bundle.putSerializable("timemodel",(ReservationTimeModel) getArguments().getSerializable("timemodel"));
        bundle.putSerializable("customer",(Customer) getArguments().getSerializable("customer"));
        bundle.putString("pickupdate", getArguments().getString("pickupdate"));
        bundle.putString("dropdate", getArguments().getString("dropdate"));
        bundle.putString("droptime", getArguments().getString("droptime"));
        bundle.putString("pickuptime",  getArguments().getString("pickuptime"));
        reservationSummarry =(ReservationSummarry) getArguments().getSerializable("reservationSum");

        binding.flightarrivaltime.setText(getArguments().getString("pickupdate") + " , " + getArguments().getString("pickuptime"));

        binding.departuredate.setOnClickListener(this);
        binding.flightarrivaltime.setOnClickListener(this);
        binding.save.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }


    @Override
    public void onClick(View view)
    {
      /*  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_AUDIO);
        } else {
            try {
                if (view == binding.startRecord)
                {
                    prepareforRecording();
                    startRecording();
                } else if (view == binding.recorder)
                {
                    prepareforStop();
                    stopRecording();
                } else if (view == binding.play)
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
        }*/

        switch (view.getId()){
            case R.id.departuredate:
                dialog.getFullDate(string -> binding.departuredate.setText(string));
                break;

            case R.id.flightarrivaltime:
                dialog.getFullDate(string -> binding.flightarrivaltime.setText(string));
                break;

            case R.id.save:
                String ddd = binding.flightarrivaltime.getText().toString().trim().replace(" , ", "T").trim().toString();
                String dddd = binding.departuredate.getText().toString().trim().replace(" , ", "T").trim().toString();
                Log.e(TAG, "onClick: " + binding.flightarrivaltime.getText().toString().trim().replace(" , ", "T").trim().toString());
                Log.e(TAG, "onClick: " + ddd.trim());
                reservationSummarry.ReservationFlightAndHotelModel.ArrFlightDate = ddd.trim();
                reservationSummarry.ReservationFlightAndHotelModel.ArrFlightNo = binding.flightarrivalNumber.getText().toString();
                reservationSummarry.ReservationFlightAndHotelModel.IsArrAirportShuttle = binding.flightarrivalshuttle.isChecked();
                reservationSummarry.ReservationFlightAndHotelModel.IsAirportDelivery = binding.flightarrivaldelivery.isChecked();

                reservationSummarry.ReservationFlightAndHotelModel.DepFlightDate = dddd.trim();
                reservationSummarry.ReservationFlightAndHotelModel.DepFlightNo = binding.departurenumber.getText().toString();
                reservationSummarry.ReservationFlightAndHotelModel.IsDepAirportShuttle = binding.departureshuttle.isChecked();
                reservationSummarry.ReservationFlightAndHotelModel.IsAirportDelivery = binding.departuredelivery.isChecked();

                reservationSummarry.ReservationFlightAndHotelModel.HotelName = binding.hotelname.getText().toString();
                reservationSummarry.ReservationFlightAndHotelModel.PhoneNumber = binding.hotelnumber.getText().toString();
                reservationSummarry.ReservationFlightAndHotelModel.Remarks = binding.instruction.getText().toString();
                reservationSummarry.ReservationFlightAndHotelModel.FlightBookingReference = binding.bookingreference.getText().toString();

                bundle.putSerializable("reservationSum", reservationSummarry);
                Helper.flightDetail = true;
                NavHostFragment.findNavController(Fragment_New_Agreement_FlightDetail.this).navigate(R.id.flight_to_booking, bundle);

                break;

            case R.id.back:
            case R.id.discard:
                NavHostFragment.findNavController(Fragment_New_Agreement_FlightDetail.this).navigate(R.id.flight_to_booking, bundle);
                break;
        }
    }

    private void prepareforRecording()
    {
        binding.recorder.setVisibility(View.VISIBLE);
     //   binding.recorder.setImageResource(R.drawable.audio_resume);
       // binding.play.setVisibility(View.GONE);
    }


    private void startRecording()
    {
        try {
            //we use the MediaRecorder class to record
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            File root = android.os.Environment.getExternalStorageDirectory();
          /*  File file = new File(root.getAbsolutePath()+ "/VoiceRecorderSimplifiedCoding/Audios");
            if (!file.exists())
            {
                file.mkdirs();
            }*/

            fileName =root.getAbsolutePath()+ "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
            CustomToast.showToast(getActivity(),"filename"+fileName,1);
            Log.d("filename", fileName);
            mRecorder.setOutputFile(fileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
           // lastProgress = 0;
           // AudiorecordSeekbar.setProgress(0);
            stopPlaying();
           /* chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();*/
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
             //   binding.play.setImageResource(R.drawable.audio_resume);
              //  chronometer.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void prepareforStop()
    {
        binding.recorder.setVisibility(View.GONE);
        binding.recorder.setVisibility(View.VISIBLE);
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
        //showing the play button
        CustomToast.showToast(getActivity(),"Recording saved successfully.",1);
    }

    private void startPlaying()
    {
        mPlayer = new MediaPlayer();
        Log.d("instartPlaying",fileName);
        try
        {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
      //  binding.play.setImageResource(R.drawable.audio_pause);

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(mp -> {
        //    binding.play.setImageResource(R.drawable.audio_resume);
            isPlaying = false;
        });

    }




}
