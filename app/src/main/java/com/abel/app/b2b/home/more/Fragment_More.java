package com.abel.app.b2b.home.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.CustomToast;
import com.abel.app.b2b.base.BaseFragment;
import com.abel.app.b2b.databinding.FragmentMoreDetailsBinding;
import com.abel.app.b2b.flexiicar.login.Login;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.home.reservation.Activity_Reservation;
import com.abel.app.b2b.home.vehicles.Activity_Vehicles;
import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.response.CompanyLabel;

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

        lblCustomer=view.findViewById(R.id.lblCustomer);
        lblVehicles=view.findViewById(R.id.lblVehicles);
        lblReservation=view.findViewById(R.id.lblReservation);
        lblAgreemet=view.findViewById(R.id.lblAgreemet);
        Back=view.findViewById(R.id.BackToHome);
        Back.setOnClickListener(new View.OnClickListener()
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
