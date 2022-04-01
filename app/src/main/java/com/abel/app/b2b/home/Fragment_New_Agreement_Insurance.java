package com.abel.app.b2b.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.apicall.OnResponseListener;
import com.abel.app.b2b.apicall.RequestType;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.DetailInsuranceBinding;
import com.abel.app.b2b.databinding.FragmentNewAgreementInsuranceBinding;
import com.abel.app.b2b.databinding.ListCustomerInsuranceBinding;
import com.abel.app.b2b.databinding.RowCustomerlist2Binding;
import com.abel.app.b2b.flexiicar.user.Fragment_Customer_Insurance_list;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.InsuranceCompanyDetailsModel;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.base.UserReservationData;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.reservation.ReservationInsurance;
import com.abel.app.b2b.model.response.LocationList;
import com.abel.app.b2b.model.response.ReservationOriginDataModels;
import com.abel.app.b2b.model.response.ReservationSummarry;
import com.abel.app.b2b.model.response.ReservationTimeModel;
import com.abel.app.b2b.model.response.VehicleModel;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.abel.app.b2b.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.abel.app.b2b.apicall.ApiEndPoint.GETINSURANCE;
import static com.abel.app.b2b.apicall.ApiEndPoint.INSURANCECOVER;

public class Fragment_New_Agreement_Insurance extends BaseFragment {

    FragmentNewAgreementInsuranceBinding binding;
    ReservationInsurance[] reservationInsurances;
    ReservationSummarry reservationSummarry;
    ReservationTimeModel reservationTimeModel;
    VehicleModel vm;
    LocationList pickuploc;
    public static HashMap<Integer, String> activationDetail = new HashMap<>();
    InsuranceModel insuranceModel;
    InsuranceCompanyDetailsModel insuranceCompanyDetailsModel;
    Customer customer;
    private Boolean updateIns = false;
    private Boolean addupdate = false;
    private static final int ii = 20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAgreementInsuranceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText(getResources().getString(R.string.select) + " " + companyLabel.Insurance);
        binding.lblnext.setOnClickListener(this);
        binding.header.back.setOnClickListener(this);
        binding.header.discard.setOnClickListener(this);
        reservationSummarry = new ReservationSummarry();
        vm = new VehicleModel();
        reservationTimeModel = new ReservationTimeModel();
        pickuploc = new LocationList();
        customer = new Customer();
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
        bundle.putString("reservationInsurances",getArguments().getString("reservationInsurances"));
        reservationSummarry = (ReservationSummarry) getArguments().getSerializable("reservationSum");
        vm = (VehicleModel) getArguments().getSerializable("Model");
        reservationTimeModel = (ReservationTimeModel) getArguments().getSerializable("timemodel");
        pickuploc =(LocationList) getArguments().getSerializable("model");
        customer = (Customer) getArguments().getSerializable("customer");

      //  bundle.putSerializable("reservationInsurances",(ReservationInsurance)getArguments().getSerializable("reservationInsurances"));

        String  path = "?tableType=3&insuranceFor=" + customer.Id;
        //String  path = "?tableType=3&insuranceFor=" + UserData.customer.Id;


       // binding.insuranceDecline.setChecked(false);
        /*apiService = new ApiService(getInsurance, RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,
                params.getInsuranceCover(vm.VehicleTypeId, reservationTimeModel.TotalDays,pickuploc.Id));*/
      //  getInsurance();

        try {
           /* Helper.AllowCustomerInsurance = true;
            if (Helper.AllowCustomerInsurance){
                binding.lblInsurancePolicy.setVisibility(View.GONE);
                binding.insuranceline.setVisibility(View.GONE);
            }*/

            if (UserData.companyModel.CompanyPreference.AllowCustomerInsurance){
                binding.insurnaceData.setVisibility(View.VISIBLE);
            } else {
                binding.insurnaceData.setVisibility(View.GONE);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.insuranceDecline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                /*    String  path = "?tableType=3&insuranceFor=" + customer.Id;
                    //String  path = "?tableType=3&insuranceFor=" + UserData.customer.Id;
                    apiService = new ApiService(GetCustomerInsurance, RequestType.GET,
                            GETINSURANCE, BASE_URL_CUSTOMER, header,path);*/
                    //getUserInsurance();
                    reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = true;
                    apiService = new ApiService(GetCustomerInsurance, RequestType.GET,
                            GETINSURANCE, BASE_URL_CUSTOMER, header,path);

                } else {
                    /*apiService = new ApiService(getInsurance, RequestType.POST ,INSURANCECOVER, BASE_URL_LOGIN,header,
                            params.getInsuranceCover(vm.VehicleTypeId, reservationTimeModel.TotalDays,pickuploc.Id));*/
                    reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = false;
                    getInsurance();

                }
            }
        });

        try {
            if (reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline){
                getUserInsurance();
                binding.insuranceDecline.setChecked(true);
            } else {
                getInsurance();
                binding.insuranceDecline.setChecked(false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lblnext:
                bundle.putBoolean("screen", updateIns);
                NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_addinsurance,bundle);
                break;

            case R.id.back:
                NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_booking,bundle);
                break;

            case R.id.discard:
                Intent intent = new Intent( getActivity(), Activity_Home.class);
                startActivity(intent);
                break;
        }
    }

    OnResponseListener getInsurance = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {

            handler.post(() -> {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");

                    if (status)
                    {
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        JSONArray array = resultSet.getJSONArray("Data");
                        binding.rlInsurancePolicyList.removeAllViews();
                        binding.lblnext.setVisibility(View.GONE);
                        reservationInsurances = loginRes.getModel(array.toString(), ReservationInsurance[].class);
                        for (int i = 0; i < reservationInsurances.length ; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            getSubview(i);
                            DetailInsuranceBinding detailInsuranceBinding = DetailInsuranceBinding.inflate(subinflater,
                                    getActivity().findViewById(android.R.id.content), false);
                            detailInsuranceBinding.getRoot().setId(200 + i);
                            detailInsuranceBinding.getRoot().setLayoutParams(subparams);
                            detailInsuranceBinding.setInsurance(reservationInsurances[i]);

                            //Integer.toString(reservationInsurances[i].ColorCode)

                            int finalI = i;
                            detailInsuranceBinding.getRoot().setOnClickListener(v -> {
                                reservationSummarry.ReservationInsuranceModel.IsSureInsurance = false;
                                reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = false;
                                reservationSummarry.ReservationInsuranceModel.NoOfDays = reservationTimeModel.TotalDays;
                                reservationSummarry.ReservationInsuranceModel.Remarks = reservationInsurances[finalI].Description;
                                reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId = reservationInsurances[finalI].DetailId;
                                reservationSummarry.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));

                                activationDetail.put(52, obj.toString());
                                updateReservationSummarry(52);

                                bundle.putSerializable("reservationSum",reservationSummarry);
                                NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_booking,bundle);
                            });
                            binding.rlInsurancePolicyList.addView(detailInsuranceBinding.getRoot());
                        }
                    }
                    else
                    {
                        String errorString = responseJSON.getString("Message");
                        CustomToast.showToast(getActivity(),errorString,1);
                        fullProgressbar.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    fullProgressbar.hide();
                }
            });
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "onError: " + error);
        }
    };

    public void updateReservationSummarry(int key){
        int data = reservationSummarry.ReservationOriginDataModels.size();
        for (int i = 0; i <data; i++) {
            if (reservationSummarry.ReservationOriginDataModels.get(i).TableType == key) {
                reservationSummarry.ReservationOriginDataModels.remove(i);
                break;
            }
        }
        reservationSummarry.ReservationOriginDataModels.add(new ReservationOriginDataModels(key, activationDetail.get(key))); // activationDetail.get(52).toString();
    }

    OnResponseListener GetCustomerInsurance = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(() -> {
                try {
                    System.out.println("Success");
                    System.out.println(response);

                    JSONObject responseJSON = new JSONObject(response);
                    Boolean status = responseJSON.getBoolean("Status");
                    binding.rlInsurancePolicyList.removeAllViews();
                    binding.lblnext.setVisibility(View.VISIBLE);
                    if (status)
                    {
                        updateIns = true;
                        JSONObject resultSet = responseJSON.getJSONObject("Data");
                        //     final JSONArray getcustomerInsurance = resultSet.getJSONArray("Data");
                        final RelativeLayout rlInsurancePolicy = getActivity().findViewById(R.id.rl_InsurancePolicyList);
                        loginRes.storedata("insurance", resultSet.toString() );
                        insuranceModel = loginRes.callFriend("insurance", InsuranceModel.class);
                        JSONObject jsonObject = resultSet.getJSONObject("InsuranceCompanyDetailsModel");
                        loginRes.storedata("insurance1", jsonObject.toString());
                        insuranceCompanyDetailsModel = loginRes.callFriend("insurance1", InsuranceCompanyDetailsModel.class);
                        UserData.insuranceModel = insuranceModel;
                        UserData.insuranceCompanyDetailsModel = insuranceCompanyDetailsModel;

                        bundle.putSerializable("insuranceData1", insuranceModel);
                        bundle.putSerializable("insuranceData2", insuranceCompanyDetailsModel);

                        //getUserInsurance();

                        try {
                            if (reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline){

                                getUserInsurance();
                               // binding.insuranceDecline.setChecked(true);
                            } else {
                                getInsurance();
                               // binding.insuranceDecline.setChecked(false);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }


                     /*   getSubview(1);

                        ListCustomerInsuranceBinding listCustomerInsuranceBinding = ListCustomerInsuranceBinding.inflate(subinflater,
                                getActivity().findViewById(android.R.id.content), false);
                        listCustomerInsuranceBinding.getRoot().setId(200+1);
                        listCustomerInsuranceBinding.getRoot().setLayoutParams(subparams);
                        listCustomerInsuranceBinding.ViewInsDetails.setVisibility(View.GONE);

                        listCustomerInsuranceBinding.txtprimaryName.setText(customer.FullName);
                        listCustomerInsuranceBinding.txtTelephoneNo.setText(customer.MobileNo);
                        listCustomerInsuranceBinding.txtUserEmail.setText(customer.Email);

                        listCustomerInsuranceBinding.txtInsuranceCmpName.setText(insuranceCompanyDetailsModel.Name);
                        listCustomerInsuranceBinding.txtPolicyNoINs.setText(insuranceModel.PolicyNo);
                        //listCustomerInsuranceBinding.txtExpiryDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.ExpiryDate, DateType.ddMMyyyyS));
                        listCustomerInsuranceBinding.txtExpiryDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.ExpiryDate));
                        //listCustomerInsuranceBinding.txtIssueDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.IssueDate, DateType.ddMMyyyyS));
                        listCustomerInsuranceBinding.txtIssueDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.IssueDate));
                        listCustomerInsuranceBinding.insuranceData.setVisibility(View.GONE);
                        listCustomerInsuranceBinding.line.setVisibility(View.GONE);

                        listCustomerInsuranceBinding.llInsurancePolicy.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                reservationSummarry.ReservationInsuranceModel.IsSureInsurance = false;
                                reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = true;
                                reservationSummarry.ReservationInsuranceModel.NoOfDays = reservationSummarry.TotalDays;
                                reservationSummarry.ReservationInsuranceModel.Remarks = "null";
                                reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId = insuranceModel.Id;
                                reservationSummarry.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                                reservationSummarry.ReservationInsuranceModel.InsuranceDetailsModel = UserData.insuranceModel;

                                bundle.putSerializable("reservationSum",reservationSummarry);
                                NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_booking,bundle);
                            }
                        });*/

                     /*   listCustomerInsuranceBinding.ViewInsDetails.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Bundle InsurancePolicyBundle = new Bundle();
                                Bundle InsurancePolicy = new Bundle();
                                InsurancePolicy.putInt("key",getArguments().getInt("key"));
                                InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                        .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy, InsurancePolicy);
                            }
                        });*/

                      //  binding.rlInsurancePolicyList.addView(listCustomerInsuranceBinding.getRoot());



                    } else {
                        binding.text.setText(getResources().getString(R.string.add));
                        String errorString = responseJSON.getString("Message");
                            CustomToast.showToast(getActivity(),errorString,1);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    private void getInsurance(){
        try {
            JSONObject resultSet = new JSONObject(loginRes.getData("reservationInsurances"));
            JSONArray array = resultSet.getJSONArray("Data");
            binding.rlInsurancePolicyList.removeAllViews();
            binding.lblnext.setVisibility(View.GONE);
            reservationInsurances = loginRes.getModel(array.toString(), ReservationInsurance[].class);
            for (int i = 0; i < reservationInsurances.length; i++) {
                JSONObject obj = array.getJSONObject(i);
                getSubview(i);
                DetailInsuranceBinding detailInsuranceBinding = DetailInsuranceBinding.inflate(subinflater,
                        getActivity().findViewById(android.R.id.content), false);
                detailInsuranceBinding.getRoot().setId(200 + i);


                /*Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.aquamarine_blue_bg);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.parseColor(reservationInsurances[i].ColorCode));*/

                //Color.parseColor(reservationInsurances[i].ColorCode);

                Drawable circleDrawable = getResources().getDrawable(R.drawable.aquamarine_blue_bg);
                circleDrawable.setTint(Color.parseColor(reservationInsurances[i].ColorCode));
                //BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                detailInsuranceBinding.call.setBackground(circleDrawable);
               // detailInsuranceBinding.call.setBackgroundColor(Color.parseColor(reservationInsurances[i].ColorCode));


                try {
                    if (reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId == reservationInsurances[i].DetailId) {
                    //    binding.rlInsurancePolicyList.setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));
                        detailInsuranceBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                        detailInsuranceBinding.line.setPadding(ii,ii,ii,ii);
                      //  detailInsuranceBinding.getRoot().setBackground(getResources().getDrawable(R.drawable.round_image));
                       // Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.round_image);
                       // Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                       // DrawableCompat.setTint(unwrappedDrawable, getResources().getColor(R.color.green));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                   /* if (reservationInsurances[i].IsSelected){
                        detailInsuranceBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                    }*/
                }

                if (reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId==0 & reservationInsurances[i].IsSelected){
                    detailInsuranceBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                    detailInsuranceBinding.line.setPadding(ii,ii,ii,ii);
                }

                detailInsuranceBinding.setInsurance(reservationInsurances[i]);

                detailInsuranceBinding.getRoot().setLayoutParams(subparams);
                int finalI = i;


                int finalI1 = ii;
                detailInsuranceBinding.getRoot().setOnClickListener(v -> {

                    detailInsuranceBinding.line.setBackground(getResources().getDrawable(R.drawable.round_image));
                    detailInsuranceBinding.line.setPadding(finalI1,finalI1,finalI1,finalI1);
                    Helper.defaultInsurance = false;
                    Fragment_New_Agreement_booking.isDefaultins = true;
                    reservationSummarry.ReservationInsuranceModel.IsSureInsurance = false;
                    reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = false;
                    reservationSummarry.ReservationInsuranceModel.NoOfDays = reservationTimeModel.TotalDays;
                    reservationSummarry.ReservationInsuranceModel.Remarks = reservationInsurances[finalI].Description;
                    reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId = reservationInsurances[finalI].DetailId;
                    reservationSummarry.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));

                    reservationSummarry.ReservationInsuranceModel.Name = reservationInsurances[finalI].Name;

                    activationDetail.put(52, obj.toString());
                    updateReservationSummarry(52);

                    bundle.putSerializable("reservationSum", reservationSummarry);
                    NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_booking, bundle);
                });
                binding.rlInsurancePolicyList.addView(detailInsuranceBinding.getRoot());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUserInsurance(){
        binding.rlInsurancePolicyList.removeAllViews();
        binding.lblnext.setVisibility(View.VISIBLE);

        getSubview(1);

        ListCustomerInsuranceBinding listCustomerInsuranceBinding = ListCustomerInsuranceBinding.inflate(subinflater,
                getActivity().findViewById(android.R.id.content), false);
        listCustomerInsuranceBinding.getRoot().setId(200+1);
        listCustomerInsuranceBinding.getRoot().setLayoutParams(subparams);
        listCustomerInsuranceBinding.ViewInsDetails.setVisibility(View.GONE);

        listCustomerInsuranceBinding.txtprimaryName.setText(customer.FullName);
        listCustomerInsuranceBinding.txtTelephoneNo.setText(customer.MobileNo);
        listCustomerInsuranceBinding.txtUserEmail.setText(customer.Email);

        try {

            listCustomerInsuranceBinding.txtInsuranceCmpName.setText(insuranceCompanyDetailsModel.Name);
            listCustomerInsuranceBinding.txtPolicyNoINs.setText(insuranceModel.PolicyNo);
            //listCustomerInsuranceBinding.txtExpiryDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.ExpiryDate, DateType.ddMMyyyyS));
            listCustomerInsuranceBinding.txtExpiryDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.ExpiryDate));
            //listCustomerInsuranceBinding.txtIssueDateIns.setText(DateConvert.DateConverter(DateType.fulldate, insuranceModel.IssueDate, DateType.ddMMyyyyS));
            listCustomerInsuranceBinding.txtIssueDateIns.setText(Helper.getDateDisplay(DateType.fulldate, insuranceModel.IssueDate));
            listCustomerInsuranceBinding.insuranceData.setVisibility(View.GONE);
            listCustomerInsuranceBinding.line.setVisibility(View.GONE);

        } catch (Exception e){
            e.printStackTrace();
        }



        listCustomerInsuranceBinding.llInsurancePolicy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.defaultInsurance = false;
                reservationSummarry.ReservationInsuranceModel.IsSureInsurance = false;
                reservationSummarry.ReservationInsuranceModel.IsInsuranceDecline = true;
                reservationSummarry.ReservationInsuranceModel.NoOfDays = reservationSummarry.TotalDays;
                reservationSummarry.ReservationInsuranceModel.Remarks = "null";
                reservationSummarry.ReservationInsuranceModel.InsuranceCoverDetailId = insuranceModel.Id;
                reservationSummarry.ReservationInsuranceModel.InsuranceDate = (getArguments().getString("dropdate"));
                reservationSummarry.ReservationInsuranceModel.InsuranceDetailsModel = UserData.insuranceModel;

                bundle.putSerializable("reservationSum",reservationSummarry);
                NavHostFragment.findNavController(Fragment_New_Agreement_Insurance.this).navigate(R.id.insurance_to_booking,bundle);
            }
        });

       /* if (addupdate){
            binding.text.setText(getResources().getString(R.string.add));
            binding.text.setVisibility(View.VISIBLE);
        }*/

        binding.rlInsurancePolicyList.addView(listCustomerInsuranceBinding.getRoot());
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
