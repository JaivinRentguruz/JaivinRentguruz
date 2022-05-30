package com.rentguruz.app.flexiicar.selfcheckout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleImagesAdditionalAllBinding;
import com.bumptech.glide.Glide;
import com.rentguruz.app.model.AttachmentsModel;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Vehicle_Additional_Image_All extends BaseFragment
{
    LinearLayout llNext;
    ImageView imgback,Veh_Image1,Veh_Image2,Veh_Image3,Veh_Image4,Veh_Image5,Veh_Image6,
            Veh_Image7,Veh_Image8,Veh_Image9,Veh_Image10;
    JSONArray ImageList = new JSONArray();
    public String id="";
    public static Context context;
    Bundle AgreementsBundle;
    TextView txt_DateTime,txtDiscard;
    List<AttachmentsModel> attachmentsModelList = new ArrayList<>();
    Bundle bundle = new Bundle();
    FragmentVehicleImagesAdditionalAllBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentVehicleImagesAdditionalAllBinding.inflate(inflater,container,false);
//        return inflater.inflate(R.layout.fragment_vehicle_images_additional_all, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
       try {
           super.onViewCreated(view, savedInstanceState);
           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
           binding.setUiColor(UiColor);
           binding.header.screenHeader.setText(companyLabel.Vehicle + " Images" );
           bundle.putInt( "Id", getArguments().getInt("Id"));
           Log.e("TAG", "RID " + getArguments().getInt("Id"));
           bundle.putSerializable("resrvation",getArguments().getSerializable("resrvation"));
           bundle.putSerializable("reservation",getArguments().getSerializable("reservation"));
           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
           txt_DateTime = view.findViewById(R.id.text_DateTime12);

           Calendar c = Calendar.getInstance();
           SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
           String datetime = dateformat.format(c.getTime());
           txt_DateTime.setText(datetime);

           llNext = view.findViewById(R.id.backNext_additionalimg);
           Veh_Image1 = view.findViewById(R.id.car_image_1);
           Veh_Image2 = view.findViewById(R.id.car_image_2);
           Veh_Image3 = view.findViewById(R.id.car_image_3);
           Veh_Image4 = view.findViewById(R.id.car_image_4);
           Veh_Image5 = view.findViewById(R.id.car_image_5);
           Veh_Image6 = view.findViewById(R.id.car_image_6);
           Veh_Image7 = view.findViewById(R.id.car_image_7);
           Veh_Image8 = view.findViewById(R.id.car_image_8);
           Veh_Image9 = view.findViewById(R.id.car_image_9);
           Veh_Image10 = view.findViewById(R.id.car_image_10);
           imgback = view.findViewById(R.id.back);



           SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
           id = sp.getString(getString(R.string.id), "");

           try {

               attachmentsModelList = (List<AttachmentsModel>) getArguments().getSerializable("image");
               int totalimage = attachmentsModelList.size();
               Log.d("TAG", "onViewCreated: " + attachmentsModelList.size());
               for (int i = 0; i <totalimage ; i++) {
                   if (i==0){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image1);
                   } else if (i==1){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image2);
                   }  else if (i==2){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image3);
                   }else if (i==3){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image4);
                   }else if (i==4){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image5);
                   }else if (i==5){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image6);
                   }else if (i==6){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image7);
                   }else if (i==7){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image8);
                   }else if (i==8){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image9);
                   }else if (i==9){
                       Glide.with(getContext()).load(attachmentsModelList.get(i).AttachmentPath).into(Veh_Image10);
                   }
               }
      /*             Glide.with(getContext()).load(attachmentsModelList.get(0).AttachmentPath).into(Veh_Image1);
                   Glide.with(getContext()).load(attachmentsModelList.get(1).AttachmentPath).into(Veh_Image2);
                   Glide.with(getContext()).load(attachmentsModelList.get(2).AttachmentPath).into(Veh_Image3);
                   Glide.with(getContext()).load(attachmentsModelList.get(3).AttachmentPath).into(Veh_Image4);
                   Glide.with(getContext()).load(attachmentsModelList.get(4).AttachmentPath).into(Veh_Image5);
                   Glide.with(getContext()).load(attachmentsModelList.get(5).AttachmentPath).into(Veh_Image6);
                   Glide.with(getContext()).load(attachmentsModelList.get(6).AttachmentPath).into(Veh_Image7);
                   Glide.with(getContext()).load(attachmentsModelList.get(7).AttachmentPath).into(Veh_Image8);
                   Glide.with(getContext()).load(attachmentsModelList.get(8).AttachmentPath).into(Veh_Image9);
                   Glide.with(getContext()).load(attachmentsModelList.get(9).AttachmentPath).into(Veh_Image10);*/



        /*       AgreementsBundle = getArguments().getBundle("AgreementsBundle");
               ImageList = new JSONArray(AgreementsBundle.getString("ImageList"));

               if (ImageList.length() > 0)
               {

                   final String doc_Details = ((JSONObject) (ImageList.get(0))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists())
                   {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image1.setImageBitmap(myBitmap);
                   }

               }
               if (ImageList.length() > 1) {
                   final String doc_Details = ((JSONObject) (ImageList.get(1))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists())
                   {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image2.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 2) {
                   final String doc_Details = ((JSONObject) (ImageList.get(2))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists())
                   {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image3.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 3)
               {
                   final String doc_Details = ((JSONObject) (ImageList.get(3))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image4.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 4) {
                   final String doc_Details = ((JSONObject) (ImageList.get(4))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image5.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 5) {
                   final String doc_Details = ((JSONObject) (ImageList.get(5))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image6.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 6) {
                   final String doc_Details = ((JSONObject) (ImageList.get(6))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image7.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 7) {
                   final String doc_Details = ((JSONObject) (ImageList.get(7))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image8.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 8)
               {
                   final String doc_Details = ((JSONObject) (ImageList.get(8))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists()) {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image9.setImageBitmap(myBitmap);
                   }
               }
               if (ImageList.length() > 9)
               {
                   final String doc_Details = ((JSONObject) (ImageList.get(9))).getString("fileBase64");

                   File imgFile = new File(doc_Details);

                   if (imgFile.exists())
                   {
                       Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(doc_Details), 300, 300, true);
                       Veh_Image10.setImageBitmap(myBitmap);
                   }
               }*/



               llNext.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View v)
                   {
                      /* AgreementsBundle.putString("ImageList",ImageList.toString());
                       Bundle Agreements=new Bundle();
                       Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                       System.out.println(Agreements);*/
                       NavHostFragment.findNavController(Fragment_Vehicle_Additional_Image_All.this)
                               .navigate(R.id.action_Vehicle_Additional_image_to_Acceptance_signature,bundle);
                   }
               });

               imgback.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
              /*         AgreementsBundle.putString("ImageList", ImageList.toString());
                       Bundle Agreements = new Bundle();
                       Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                       System.out.println(Agreements);
                       NavHostFragment.findNavController(Fragment_Vehicle_Additional_Image_All.this)
                               .navigate(R.id.action_Vehicle_Additional_image_to_Vehicle_Image_10, Agreements);*/
                       NavHostFragment.findNavController(Fragment_Vehicle_Additional_Image_All.this).popBackStack();
                              // .navigate(R.id.action_Vehicle_Additional_image_to_Image, bundle);
                   }
               });

               txtDiscard = view.findViewById(R.id.discard);

               txtDiscard.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       NavHostFragment.findNavController(Fragment_Vehicle_Additional_Image_All.this)
                               .navigate(R.id.action_Vehicle_Additional_image_to_Agreements);
                   }
               });

           } catch (Exception e)
           {
               e.printStackTrace();
           }
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {

    }
}
