package com.abel.app.b2b.home.more;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.home.agreement.Fragment_Agreement;
import com.abel.app.b2b.home.reservation.Activity_Reservation;
import com.abel.app.b2b.home.vehicles.Activity_Vehicles;
import com.abel.app.b2b.model.base.UserData;

public class Activity_MoreTab extends AppCompatActivity {

    LinearLayout tab_home,tabReservation,tab_agreement,tab_Vehicles;
    private int currentApiVersion;
    ImageView activityMoreIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tab);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }


        if (Helper.isRegistrationDone) {
            Log.e("TAG", "onCreate: " + "with scan" );
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_driver_registration);
            navGraph.setStartDestination(R.id.CustomerList);
            navController.setGraph(navGraph);
        }
        Log.e("TAG", "onCreate: " + "with scan 2" );
        Helper.isRegistrationDone = false;
        tab_home=findViewById(R.id.tab_home);
        tabReservation=findViewById(R.id.tabReservation);
        tab_agreement=findViewById(R.id.tab_agreement);
        tab_Vehicles=findViewById(R.id.tab_Vehicles);
        activityMoreIcon = findViewById(R.id.activityMoreIcon);
       // activityMoreIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_more2));
        activityMoreIcon.setColorFilter(getResources().getColor(R.color.bottommenuactivecolor));
        tabReservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(Activity_MoreTab.this, Activity_Reservation.class);
                startActivity(i);
            }
        });

        tab_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_MoreTab.this, Activity_Home.class);
                startActivity(i);
            }
        });

        tab_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_MoreTab.this, Activity_Agreements.class);
                startActivity(i);
            }
        });

        tab_Vehicles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_MoreTab.this, Activity_Vehicles.class);
                startActivity(i);
            }
        });
        try {
            new  Activity_Home().setLable(UserData.loginResponse.CompanyLabel);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void BottomnavVisible()
    {
        LinearLayout lblcontinue1 = (LinearLayout) findViewById(R.id.lblcontinue1);
        lblcontinue1.setVisibility(View.VISIBLE);

        LinearLayout MainFragment = (LinearLayout) findViewById(R.id.MainFragment);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
        params.setMargins(0, 0, 0, 115);
        MainFragment.setLayoutParams(params);
    }

    public void BottomnavInVisible()
    {
        LinearLayout lblcontinue1 = (LinearLayout) findViewById(R.id.lblcontinue1);
        lblcontinue1.setVisibility(View.GONE);

        LinearLayout MainFragment = (LinearLayout) findViewById(R.id.MainFragment);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        MainFragment.setLayoutParams(params);
    }
}