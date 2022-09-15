package com.rentguruz.app.flexiicar.selfcheckout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.rentguruz.app.adapters.CustomBindingAdapter;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentVehicleCheckoutConfirmationBinding;
import com.rentguruz.app.flexiicar.user.Fragment_Summary_Of_Charges_For_Agreements;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.ReservationCheckout;
import com.rentguruz.app.model.checkinout.CheckoutSignatureDisplay;
import com.rentguruz.app.model.parameter.DateType;
import com.rentguruz.app.model.response.AttachmentsModel;
import com.rentguruz.app.model.response.Reservation;
import com.rentguruz.app.model.response.ReservationSummarry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.CHECKOUTODMETER;
import static com.rentguruz.app.apicall.ApiEndPoint.GETSIGNATURE;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONCHECKOUT;
import static com.rentguruz.app.apicall.ApiEndPoint.RESERVATIONGETSINGLE;

public class Fragment_Checkout_Success extends BaseFragment {

    FragmentVehicleCheckoutConfirmationBinding binding;
    ReservationSummarry reservationSummarry = new ReservationSummarry();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentVehicleCheckoutConfirmationBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);

       // reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationsum");
        try {
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservation");
        } catch (Exception e){
            e.printStackTrace();
            reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationsum");
            reservationSummarry = Fragment_Summary_Of_Charges_For_Agreements.reservationSummarry;
        }

        loginRes.testingLog(TAG,reservationSummarry);

        binding.status.setText(companyLabel.CheckOut+ " \n" + "Confirmation");

        binding.imagelist.carImage1.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage2.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage3.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage4.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage5.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage6.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage7.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage8.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage9.setBackground(userDraw.getImageUpload());
        binding.imagelist.carImage10.setBackground(userDraw.getImageUpload());

        new ApiService(new OnResponseListener() {
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
                            Log.d("TAG", "run: " + status);


                            if (status) {
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                ReservationCheckout reservationCheckout = new ReservationCheckout();
                                reservationCheckout = loginRes.getModel(resultSet.toString(), ReservationCheckout.class);

                               // binding.txtOdoMeter.setTypeface(userDraw.getOdMeterFont());
                                //binding.txtOdoMeter.setText(String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo));

                                userDraw.setodmeter(binding.txtOdoMeter,String.valueOf(reservationCheckout.ReservationCheckOutModel.CheckOutOdo) );

                                int s= Integer.valueOf(reservationCheckout.ReservationCheckOutModel.CurrentFuel.intValue());
                                binding.txtProgressvalue.setText(reservationCheckout.ReservationCheckOutModel.CurrentFuel+"%");
                                binding.simpleSeekBar.setProgress(Integer.parseInt(String.valueOf(s)));
                                binding.simpleSeekBar.setClickable(false);
                                binding.simpleSeekBar.setFocusable(false);
                                binding.simpleSeekBar.setEnabled(false);
                                binding.simpleSeekBar.setOnSeekBarChangeListener(null);


                                binding.EdtNotes.setText(reservationCheckout.ReservationCheckOutModel.Note);

                               /* // dsfsdfs
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
                                enablechangeEdit(txtGasTankOut);*/

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

            }
        }, RequestType.POST,
                CHECKOUTODMETER, BASE_URL_LOGIN, header, params.getCheckOutODmeter(reservationSummarry.Id, String.valueOf(reservationSummarry.ReservationVehicleModel.VehicleId)));

        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");
                            if (status){
                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                Reservation reservation = new Reservation();
                                reservation =    loginRes.getModel(resultSet.toString(), Reservation.class);

                                binding.carimage.txtCheckOutLocName.setText(reservation.PickUpLocationName);
                                binding.carimage.checkinLocName.setText(reservation.DropLocationName);
                                binding.carimage.txtCheckOutDateTime.setText(DateConvert.DateConverter(DateType.fulldate, reservation.CheckOutDate,DateType.ddMMyyyyS1));
                                binding.carimage.checkInDateTime.setText(DateConvert.DateConverter(DateType.fulldate, reservation.CheckInDate,DateType.ddMMyyyyS1));
                                CustomBindingAdapter.loadImage(binding.carimage.VehImage, reservation.VehicleImagePath);
                                binding.carimage.vehicleName.setText(reservation.VehicleName);
                                binding.carimage.reservationNumber.setText(reservation.ReservationNo);

                                Customer customer = new Customer();
                                customer.Email = reservation.Email;
                                customer.MobileNo = reservation.MobileNo;
                                customer.FullName = reservation.CustomerName;
                                customer.Fname = "Name";
                                customer.Lname = "Eman";
                                binding.customer.firstlastname.setVisibility(View.GONE);
                                binding.customer.setCustomer(customer);
                                binding.customer.singlename.setVisibility(View.VISIBLE);
                                //Agreement copy sent by email to\nThe ID on account
                                binding.mailsent.setText(companyLabel.Reservation + " copy sent by email to\n" + reservation.Email);


                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST,
                RESERVATIONGETSINGLE, BASE_URL_LOGIN, header, params.getsingle(reservationSummarry.CustomerId, reservationSummarry.Id));

        //?ReservationId=1171&VehicleId=739&fileUploadType=17,18

        String path =  "?ReservationId=" + reservationSummarry.Id + "&VehicleId=" + reservationSummarry.ReservationVehicleModel.VehicleId +"&fileUploadType=" + "17";
        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject responseJSON = new JSONObject(response);
                            Boolean status = responseJSON.getBoolean("Status");

                            if (status){
                                JSONArray listdata = responseJSON.getJSONArray("Data");
                                AttachmentsModel[]  attachmentsModels;
                                attachmentsModels = loginRes.getModel(listdata.toString(), AttachmentsModel[].class);
                                for (int i = 0; i < attachmentsModels.length; i++) {
                                    if (i==0){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage1, attachmentsModels[i].AttachmentPath);
                                    } else if (i==1){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage2, attachmentsModels[i].AttachmentPath);
                                    } else if (i==2){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage3, attachmentsModels[i].AttachmentPath);
                                    } else if (i==3){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage4, attachmentsModels[i].AttachmentPath);
                                    } else if (i==4){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage5, attachmentsModels[i].AttachmentPath);
                                    } else if (i==5){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage6, attachmentsModels[i].AttachmentPath);
                                    } else if (i==6){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage7, attachmentsModels[i].AttachmentPath);
                                    } else if (i==7){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage8, attachmentsModels[i].AttachmentPath);
                                    } else if (i==8){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage9, attachmentsModels[i].AttachmentPath);
                                    } else if (i==9){
                                        CustomBindingAdapter.loadImage(binding.imagelist.carImage10, attachmentsModels[i].AttachmentPath);
                                    }
                                }

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.GET, RESERVATIONCHECKOUT,BASE_URL_LOGIN,header,path);



        new ApiService(new OnResponseListener() {
            @Override
            public void onSuccess(String response, HashMap<String, String> headers) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        JSONObject responseJSON = new JSONObject(response);
                                        Boolean status = responseJSON.getBoolean("Status");

                                        if (status){
                                            JSONObject data =  responseJSON.getJSONObject("Data");
                                            JSONArray listdata = data.getJSONArray("Data");
                                            CheckoutSignatureDisplay[]  signatureDisplays;
                                            signatureDisplays = loginRes.getModel(listdata.toString(), CheckoutSignatureDisplay[].class);
                                            for (int i = 0; i < signatureDisplays.length; i++) {
                                                if (signatureDisplays[i].SignType == 2){
                                                    CustomBindingAdapter.loadImage(binding.customerSignature, signatureDisplays[i].AttachmentsModel.AttachmentPath);
                                                }
                                            }

                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });
            }

            @Override
            public void onError(String error) {

            }
        }, RequestType.POST, GETSIGNATURE,BASE_URL_LOGIN,header,params.getSignature(reservationSummarry.Id) );

    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {

    }
}
