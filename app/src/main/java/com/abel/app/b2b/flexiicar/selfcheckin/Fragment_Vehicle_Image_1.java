package com.abel.app.b2b.flexiicar.selfcheckin;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.model.AttachmentsModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.abel.app.b2b.apicall.ApiEndPoint.UPLOADIMAGE;

public class Fragment_Vehicle_Image_1 extends Fragment
{
    LinearLayout lblNext;
    ImageView imgback, UploadImg,defaultimage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CAPTURE_IMAGE = 2;
    Handler handler = new Handler(Looper.getMainLooper());
    public String id = "";
    JSONArray ImageList = new JSONArray();
    TextView txt_DateTime;
    //Bundle AgreementsBundle;
    Bundle bundle = new Bundle();
    Boolean next = false;
    int screen,count = 1;
    TextView imagecount,carImageType;
    List<AttachmentsModel> attachmentsModelList = new ArrayList<>();
    LocationManager locationManager ;
    boolean GpsStatus ;
    private static final int REQUEST_LOCATION = 1;
    public String useraddress;
    public Dialog fullProgressbar;
    LoginRes loginRes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginRes = new LoginRes(getContext());
        return inflater.inflate(R.layout.fragment_vehicle_images_1, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       // AgreementsBundle = getArguments().getBundle("AgreementsBundle");
        try {
            fullProgressbar = new Dialog(getContext());
            fullProgressbar.setCancelable(false);
            fullProgressbar.setContentView(R.layout.custom_progresss);

            if (!CheckGpsStatus()) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentLocation();
                }
            }, 500);

            bundle.putInt("Id", getArguments().getInt("Id"));
            Log.e("TAG", "RID " + getArguments().getInt("Id"));
            screen = getArguments().getInt("length");
            count = getArguments().getInt("temp");
            attachmentsModelList = (List<AttachmentsModel>) getArguments().getSerializable("image");
            Log.d("TAG", "onViewCreated: " + attachmentsModelList.size());

            txt_DateTime = view.findViewById(R.id.txt_DateTimeForPic);
            bundle.putSerializable("reservation", getArguments().getSerializable("reservation"));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            String datetime = dateformat.format(c.getTime());
            txt_DateTime.setText(datetime);
            bundle.putInt("Id", getArguments().getInt("Id"));
            screen = getArguments().getInt("length");
            count = getArguments().getInt("temp");
            lblNext = view.findViewById(R.id.lblback_forntside);
            imgback = view.findViewById(R.id.backimg_frontside);
            UploadImg = view.findViewById(R.id.FrontImage_Upload);
            imagecount = view.findViewById(R.id.imagecount);
            defaultimage = view.findViewById(R.id.FrontImageView);
            carImageType = view.findViewById(R.id.carImageType);
            imagecount.setText("Image " + count + " of 10");

 /*       if (!CheckGpsStatus()){
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }*/


            if (count == 1) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_1));
                carImageType.setText("Left Side");
            } else if (count == 2) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_2));
                carImageType.setText("Right Side");
            } else if (count == 3) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_3));
                carImageType.setText("Front Side");
            } else if (count == 4) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_4));
                carImageType.setText("Right & Back Side");
            } else if (count == 5) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_5));
                carImageType.setText("Left Side");
            } else if (count == 6) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_6));
                carImageType.setText("Back Side");
            } else if (count == 7) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_7));
                carImageType.setText("Steering");
            } else if (count == 8) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_8));
                carImageType.setText("Interior Front Seat");
            } else if (count == 9) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_9));
                carImageType.setText("Interior Back Seat");
            } else if (count == 10) {
                defaultimage.setImageDrawable(getContext().getDrawable(R.drawable.car_image_10));
                carImageType.setText("Boot Space");
            }

            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              /*  Bundle SelfCheckInBundle = new Bundle();
                SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                System.out.println(SelfCheckInBundle);*/
              /*  NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this)
                        .navigate(R.id.action_VehImage_SelfCheckIn_1_to_LocationKey_ForSelfCheckOut);*/
                    NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this).popBackStack();
                }
            });

            UploadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                   /* Intent mintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(mintent,RESULT_CAPTURE_IMAGE);*/


                }
            });
            lblNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            /*    Bundle SelfCheckInBundle = new Bundle();
                SelfCheckInBundle.putString("ImageList", ImageList.toString());
                SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                System.out.println(SelfCheckInBundle);*/
               /* NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this)
                        .navigate(R.id.action_VehImage_SelfCheckIn_1_to_VehImage_SelfCheckIn_2);*/
                    bundle.putSerializable("image", (Serializable) attachmentsModelList);
                    if (screen == 1) {
                        NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this)
                                .navigate(R.id.allvechicelimage2, bundle);
                    } else {
                        bundle.putInt("length", getArguments().getInt("length") - 1);
                        bundle.putInt("temp", getArguments().getInt("temp") + 1);
                        NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this)
                                .navigate(R.id.same1, bundle);
                    }
                }
            });

            TextView txtDiscard = view.findViewById(R.id.DiscardVeh1);

            txtDiscard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NavHostFragment.findNavController(Fragment_Vehicle_Image_1.this)
                            .navigate(R.id.action_VehImage_SelfCheckIn_1_to_Agreements);
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            File file=null;
            String[] imageProjection = {MediaStore.Images.Media.DATA};
            Cursor cursor =getContext().getContentResolver().query(selectedImage, imageProjection, null, null, null);
            if(cursor != null) {
                try {
                    bitmap = getScaledBitmap(selectedImage,400,400);

                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    String  part_image = cursor.getString(indexImage);

                    file = new File(part_image);
                    bitmap = decodeFile(part_image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

              /*  File file = new File(part_image);
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(part_image,op);
                bitmap = bitmap.copy(op.inPreferredConfig, true);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(20);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.RED);
                canvas.drawText(txt_DateTime.getText().toString(),bitmap.getWidth()-250,30,paint);
               // canvas.drawText("Rajkot", 10, bitmap.getWidth()-100, paint);

                Canvas canvas2 = new Canvas(bitmap);
                Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint2.setTextSize(20);
                paint2.setStyle(Paint.Style.FILL);
                paint2.setColor(Color.RED);
                canvas2.drawText(useraddress, 10, bitmap.getHeight()-30,paint2);*/

            }
        /*    try
            {
                bitmap = getScaledBitmap(selectedImage,400,400);
                JSONObject docObj = new JSONObject();
                docObj.put("Doc_For",9);
                docObj.put("VhlPictureSide", 11);
                docObj.put("fileBase64",getImagePathFromUri(selectedImage) );
                ImageList.put(docObj);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
           // Bitmap bitmap1 = textAsBitmap("jjj", 100,R.color.AppRed);



            /*bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            canvas.drawText("J",100,100,paint);*/

        /*    try {
                bitmap = getScaledBitmap(selectedImage,400,400);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/

            storeImage(bitmap);
            UploadImg.setImageBitmap(bitmap);
        }

        if (requestCode == RESULT_CAPTURE_IMAGE && resultCode ==Activity.RESULT_OK && data != null ){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            UploadImg.setImageBitmap(photo);
        }
    }

    public String getImagePathFromUri(Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

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

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public Boolean CheckGpsStatus(){
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
     /*   if(GpsStatus == true) {
            textview.setText("GPS Is Enabled");
        } else {
            textview.setText("GPS Is Disabled");
        }*/
       return GpsStatus;
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLastLocation() != null){
                            Log.e("TAG", "onLocationResult: " + locationResult.getLocations().get(0).getLatitude() +  " : " + locationResult.getLocations().get(0).getLongitude() );
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getContext(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                Log.e("TAG", "onLocationResult: " + locationResult.getLastLocation().getLatitude() +  " : " + locationResult.getLastLocation().getLongitude() );
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                Log.e("TAG", "onLocationResult: " +  addresses.get(0));

                                Log.e("TAG", "onLocationResult: " + addresses.get(0).getMaxAddressLineIndex());

                               // useraddress =  addresses.get(0).getPremises() + " " +  knownName + " " + city + " " + state + " " + postalCode;
                                String[] a = address.split(",");
                                Log.e("TAG", "onLocationResult: " + a.length);
                                Log.e("TAG", "onLocationResult: " + a[0] + " " + a[1] + " " + a[2]   );
                                useraddress = address;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }, Looper.getMainLooper());

    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            imgUpload(pictureFile);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "Error accessing file: " + e.getMessage());
        }
    }
    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp + count +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public Bitmap decodeFile(String part_image){
        Bitmap bitmap =null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(part_image,op);
        bitmap = bitmap.copy(op.inPreferredConfig, true);
        Canvas canvas = new Canvas(bitmap);
     //   Rect rect = new Rect();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(100);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawLine(0,0,bitmap.getWidth(),0,paint);


        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        //rect.set(10,10,10,10);
       // paint.getTextBounds(txt_DateTime.getText().toString(),0,txt_DateTime.getText().toString().length(),rect);
       // int x = (bitmap.getWidth() - rect.width())/2;
       // int y = (bitmap.getHeight() + rect.height())/2;



      //  Paint paint = new Paint();
        canvas.drawText(txt_DateTime.getText().toString(),bitmap.getWidth()-200,30,paint);

/*
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);*/


        //canvas.drawText(txt_DateTime.getText().toString(),bitmap.getWidth()-200,30,paint1);

       // canvas.drawText(txt_DateTime.getText().toString(),x,y,paint);

        // canvas.drawText("Rajkot", 10, bitmap.getWidth()-100, paint);

/*        final Paint paints = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        paints.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paints.setColor(Color.BLACK);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, bitmap.getWidth() / 2, paints);
        canvas.drawArc();
        paints.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paints);*/


        Canvas canvas2 = new Canvas(bitmap);
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint2.setStrokeWidth(100);
        paint2.setColor(Color.BLACK);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawLine(0 ,bitmap.getHeight()-30,bitmap.getWidth(),bitmap.getHeight()-30,paint2);

        //canvas.drawLine(0,0,bitmap.getWidth(),0,paint);

        paint2.setTextSize(20);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.RED);
        canvas2.drawText(useraddress, 10, bitmap.getHeight()-30,paint2);



        return bitmap;
    }

    public void imgUpload(File file){
        HashMap<String, String> header = new HashMap<>();
        header.put("FileUploadMasterId", String.valueOf(getArguments().getInt("Id")));
        header.put("Id", String.valueOf(getArguments().getInt("Id")));
        header.put("IsActive","true");
        header.put("CompanyId", String.valueOf(Helper.id));
        header.put("fileUploadType", "18");
        AndroidNetworking.initialize(getActivity());
        ApiService apiService = new ApiService();
        apiService.UPLOAD_REQUEST(uploadImage,UPLOADIMAGE, header, file);
    }

    OnResponseListener uploadImage = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            Log.d("TAG", "onSuccess: " + response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONArray data = responseJSON.getJSONArray("Data");
                            for (int i = 0; i <data.length() ; i++) {
                                AttachmentsModel attachmentsModel = new AttachmentsModel();
                                attachmentsModel =    loginRes.getModel(data.get(i).toString(), AttachmentsModel.class);
                                attachmentsModelList.add(attachmentsModel);
                                next = true;
                                fullProgressbar.hide();
                            }



                        }
                        else
                        {
                            String msg = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),msg,1);
                            fullProgressbar.hide();
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
            Log.d("TAG", "onError: " + error);
        }
    };
}
