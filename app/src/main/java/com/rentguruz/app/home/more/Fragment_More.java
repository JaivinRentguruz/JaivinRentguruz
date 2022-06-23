package com.rentguruz.app.home.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentMoreDetailsBinding;
import com.rentguruz.app.flexiicar.login.Login;
import com.rentguruz.app.home.Activity_Home;
import com.rentguruz.app.home.agreement.Activity_Agreements;
import com.rentguruz.app.home.reservation.Activity_Reservation;
import com.rentguruz.app.home.vehicles.Activity_Vehicles;
import com.rentguruz.app.model.base.UserData;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_More extends BaseFragment
{

    LinearLayout lblCustomer,lblVehicles,lblReservation,lblAgreemet;
    ImageView Back;
    FragmentMoreDetailsBinding binding;
    Intent i;
    String serverpath="";
    SharedPreferences sp;
    String default_Email="",default_Password="";
    public String id = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentMoreDetailsBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        binding.header.screenHeader.setText("More");
        binding.header.discard.setVisibility(View.GONE);
        
        try {
            sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath","");
            id = sp.getString(getString(R.string.id), "");
            default_Email=sp.getString("default_Email","");
            default_Password=sp.getString("default_Password","");
            binding.email.setText(default_Email);
            binding.name.setText(UserData.loginResponse.User.UserName);
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.man.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.car.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.reservationicon.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("two").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("three").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("four").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("five").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("six").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("seven").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("eight").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("nine").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("ten").setFillColor(Color.parseColor(UiColor.primary));
        binding.map.getPathModelByName("neone").setFillColor(Color.parseColor(UiColor.secondary));
        binding.map.getPathModelByName("neone1").setFillColor(Color.parseColor(UiColor.secondary));
        binding.map.getPathModelByName("neone2").setFillColor(Color.parseColor(UiColor.secondary));
        binding.map.getPathModelByName("neone3").setFillColor(Color.parseColor(UiColor.secondary));
        binding.map.getPathModelByName("neone4").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.inspection.getPathModelByName("two").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two1").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two2").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two3").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two4").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two5").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two6").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two7").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two8").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two9").setFillColor(Color.parseColor(UiColor.secondary));
        binding.inspection.getPathModelByName("two10").setFillColor(Color.parseColor(UiColor.secondary));
        binding.maninspection.getPathModelByName("1").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("2").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("3").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("4").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("5").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("6").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("7").setFillColor(Color.parseColor(UiColor.primary));
        binding.maninspection.getPathModelByName("line").setFillColor(Color.parseColor(UiColor.secondary));
        binding.car2.getPathModelByName("one").setFillColor(Color.parseColor(UiColor.primary));
        binding.reports.getPathModelByName("1").setFillColor(Color.parseColor(UiColor.primary));
        binding.reports.getPathModelByName("2").setFillColor(Color.parseColor(UiColor.primary));
        binding.reports.getPathModelByName("3").setFillColor(Color.parseColor(UiColor.primary));
        binding.reports.getPathModelByName("4").setFillColor(Color.parseColor(UiColor.primary));
        binding.reports.getPathModelByName("5").setFillColor(Color.parseColor(UiColor.primary));
        binding.reportss.getPathModelByName("1").setFillColor(Color.parseColor(UiColor.primary));
        binding.reportss.getPathModelByName("2").setFillColor(Color.parseColor(UiColor.primary));
        binding.reportss.getPathModelByName("3").setFillColor(Color.parseColor(UiColor.primary));
        binding.reportss.getPathModelByName("4").setFillColor(Color.parseColor(UiColor.primary));
        binding.reportss.getPathModelByName("5").setFillColor(Color.parseColor(UiColor.primary));
        //((Activity_MoreTab) getActivity()).BottomnavVisible();

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = getActivity().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       // binding.man.getBackground().setColorFilter(Color.parseColor(UserData.UiColor.primary), PorterDuff.Mode.DARKEN);
       // binding.man.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(UserData.UiColor.primary)));
        lblCustomer=view.findViewById(R.id.lblCustomer);
        lblVehicles=view.findViewById(R.id.lblVehicles);
        lblReservation=view.findViewById(R.id.lblReservation);
        lblAgreemet=view.findViewById(R.id.lblAgreemet);
        binding.header.back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Home.class);
                startActivity(i);
            }
        });
        lblCustomer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_More.this)
                        .navigate(R.id.action_MoreInfo_to_CustomerList);
            }
        });

        lblVehicles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Vehicles.class);
                startActivity(i);
            }
        });

        lblReservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Reservation.class);
                startActivity(i);
            }
        });

        lblAgreemet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Activity_Agreements.class);
                startActivity(i);
            }
        });

        binding.lbllogout.setOnClickListener(this);

        try {
            binding.agr.setText(companyLabel.Agreement);
            binding.res.setText(companyLabel.Reservation);
            binding.cus.setText(companyLabel.Customer);
            binding.veh.setText(companyLabel.Vehicle);
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
            case R.id.lbllogout:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String msg = "You Have Been Successfully Logged Out!";
                                CustomToast.showToast(getActivity(),msg,0);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.apply();

                                editor.putString("default_Email",default_Email);
                                editor.putString("default_Password",default_Password);
                                editor.commit();

                                Intent i=new Intent (getActivity(), Login.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
      /*  switch (v.getId()){
            case R.id.BackToHome:
                i = new Intent(getActivity(), Activity_Home.class);
                startActivity(i);
                break;

            case R.id.lblCustomer:
                NavHostFragment.findNavController(Fragment_More.this)
                        .navigate(R.id.action_MoreInfo_to_CustomerList);
                break;

            case R.id.lblVehicles:
                i = new Intent(getActivity(), Activity_Vehicles.class);
                startActivity(i);
                break;

            case R.id.lblReservation:
                i = new Intent(getActivity(), Activity_Reservation.class);
                startActivity(i);
                break;

                case R.id.lblAgreemet

        }*/
    }
}
