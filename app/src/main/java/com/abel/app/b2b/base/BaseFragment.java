package com.abel.app.b2b.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.abel.app.b2b.adapters.CustomBindingAdapter;
import com.abel.app.b2b.adapters.CustomeView;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.companyModel;
import com.abel.app.b2b.model.response.CompanyLabel;
import com.abel.app.b2b.model.response.LoginResponse;
import com.androidnetworking.AndroidNetworking;
import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomPreference;
import com.abel.app.b2b.adapters.LoginRes;
import com.abel.app.b2b.apicall.ApiService;
import com.abel.app.b2b.model.DoHeader;
import com.abel.app.b2b.model.parameter.CommonParams;

import org.jetbrains.annotations.NotNull;


abstract public class BaseFragment extends Fragment implements View.OnClickListener {

    public Context context;
    public DoHeader header;
    public CommonParams params;
    public String TAG, TAG2;
    public LoginRes loginRes;
    public CustomPreference preference;
    public ApiService apiService;
    public Handler handler;
    public Bundle bundle;
    public Dialog fullProgressbar;
    public LayoutInflater subinflater;
    public RelativeLayout.LayoutParams subparams;
    public CustomeView  getview;
    public CompanyLabel companyLabel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }


    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = this.getContext();
        bundle = new Bundle();
        TAG = getClass().getSimpleName();
        preference = new CustomPreference(getContext());
        loginRes = new LoginRes(getContext());
        header = new DoHeader();
        header.exptime = "12/22/2021 11:47:18 PM";
       // header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.token = loginRes.getData("TOKEN");
        header.islogin = "1";
        //header.ut = "PYOtYmuTsLQ=";
        header.ut = loginRes.getData("UT");
        header.mut = loginRes.getData("MUT");
        AndroidNetworking.initialize(getActivity());
        this.context = getActivity();
        handler = new Handler(Looper.getMainLooper());
        fullProgressbar = new Dialog(context);
        fullProgressbar.setCancelable(true);
        fullProgressbar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fullProgressbar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fullProgressbar.getWindow().setGravity(Gravity.CENTER);
        fullProgressbar.setContentView(R.layout.custom_progresss);
        fullProgressbar.hide();
       /* Window window = fullProgressbar.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);*/
        fullProgressbar.dismiss();
        fullProgressbar.cancel();
        //BaseModel.CompanyId = Integer.valueOf(loginRes.getData("CompanyId"));
        if (Helper.di == 0){
            Helper.di = Integer.valueOf(loginRes.getData("CompanyId"));
            UserData.loginResponse  = loginRes.getModel(loginRes.getData("Login"),LoginResponse.class);
            UserData.companyModel =  loginRes.getModelSystem(getResources().getString(R.string.scompany),companyModel.class);
            Helper.di = UserData.companyModel.Id;
            Helper.currencySymbol = UserData.companyModel.CurrencySymbol;
            Helper.currencyName = UserData.companyModel.DisplayCurrency;
            Helper.displaycurrency = UserData.companyModel.DisplayCurrency;
            Helper.fueel = UserData.companyModel.DistanceDesc;
            Helper.fuel = UserData.companyModel.Distance;
            Helper.fueltype = UserData.companyModel.DistanceDesc;
            Helper.dateformat = UserData.companyModel.DefaultDateFormat;
        }
        params = new CommonParams();
     //   Observable. = Integer.valueOf(loginRes.getData("CompanyId"));

       /* if(Helper.currencySymbol == null || Helper.currencySymbol.length() == 0){

        }*/
        try {
            companyLabel = new CompanyLabel();
            getview = new CustomeView();
            companyLabel = getview.getCompanyLabel(view, UserData.loginResponse.CompanyLabel);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            getview.uploadImage(view,loginRes,getActivity());
            ImageView icon = view.findViewById(R.id.icon);
            CustomBindingAdapter.loadImage(icon,loginRes.getData(getResources().getString(R.string.icon)));
            ImageView logo = view.findViewById(R.id.logo);
            CustomBindingAdapter.loadImage(logo,loginRes.getData(getResources().getString(R.string.logo)));
            ImageView homeImage = view.findViewById(R.id.homeImage);
            CustomBindingAdapter.loadImage(homeImage,loginRes.getData(getResources().getString(R.string.homescreenimage)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    protected abstract int getFragmentLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fullProgressbar.hide();
        fullProgressbar.cancel();
        fullProgressbar.dismiss();
    }

    public void getSubview(int i){
        subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        subparams.addRule(RelativeLayout.BELOW, (200 + i - 1));
        subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        subparams.setMargins(0, 10, 0, 0);
        subinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


}
