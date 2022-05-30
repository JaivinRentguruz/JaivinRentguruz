package com.rentguruz.app.home;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.home.agreement.Activity_Agreements;
import com.rentguruz.app.home.more.Activity_MoreTab;
import com.rentguruz.app.home.reservation.Activity_Reservation;
import com.rentguruz.app.home.vehicles.Activity_Vehicles;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.CompanyLabel;


public class Activity_Home extends AppCompatActivity {

    LinearLayout tab_home, tabReservation, tab_agreement, tab_Vehicles, tab_More;
    LinearLayout bottommenu;
    ImageView homeIcon;
  /*  Res res;
    @Override public Resources getResources() {
        if (res == null) {
            res = new Res(super.getResources());
        }
        return res;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Helper.isDropdown = true;
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
       // res.getColor(R.color.black);


        int resourceID = getResources().getIdentifier("black", "color", getPackageName());


/*
        //color parser
        String white = "#ffffff";
        int whiteInt = Color.parseColor(white);
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
*/

   /*     if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            Log.e("HomeActivity̥", "onCreate: ");
        }*/

        Helper.B2BRESERVATION = false;

        if (Helper.screenNumber > 1) {
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_home_tab);
            navGraph.setStartDestination(R.id.AddDriver1);
            navController.setGraph(navGraph);
        }

        tab_home = findViewById(R.id.tab_home);
        //tab_home.setBackgroundColor(res.getColor(R.color.splash_screen_bg_color));
        tabReservation = findViewById(R.id.tabReservation);
        tab_agreement = findViewById(R.id.tab_agreement);
        tab_Vehicles = findViewById(R.id.tab_Vehicles);
        tab_More = findViewById(R.id.tab_More);
        bottommenu = findViewById(R.id.bottommenu);
        homeIcon = findViewById(R.id.homeIcon);
        //homeIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_home2));
        try {
            bottommenu.setBackgroundColor(Color.parseColor(UserData.UiColor.secondary));
            homeIcon.setColorFilter(Color.parseColor(UserData.UiColor.primary));
        } catch (Exception e){
            e.printStackTrace();
        }

        tab_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Home.this, Activity_MoreTab.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        tab_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Home.this, Activity_Agreements.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        tabReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Home.this, Activity_Reservation.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        tab_Vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Home.this, Activity_Vehicles.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        setLable(UserData.loginResponse.CompanyLabel);


    }

    public void BottomnavVisible() {
        LinearLayout lblcontinue1 = findViewById(R.id.lblcontinue11);
        lblcontinue1.setVisibility(View.VISIBLE);

        LinearLayout MainFragment = findViewById(R.id.MainFragment);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        MainFragment.setLayoutParams(params);
    }

    public void BottomnavInVisible() {
        LinearLayout lblcontinue1 = findViewById(R.id.lblcontinue11);
        lblcontinue1.setVisibility(View.GONE);
        LinearLayout MainFragment = findViewById(R.id.MainFragment);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        MainFragment.setLayoutParams(params);
    }

    public void setLable( CompanyLabel companyLabel){
        try {
            if (companyLabel !=null){

            } else {
                companyLabel = new CompanyLabel();
            }
            TextView textView =findViewById(R.id.txthome);
            TextView reservation = findViewById(R.id.txtreservation);
            reservation.setText(companyLabel.Reservation);
            TextView agreement =findViewById(R.id.txtagreement);
            agreement.setText(companyLabel.Agreement);
            TextView vehicle = findViewById(R.id.txtvehicle);
            vehicle.setText(companyLabel.Vehicle);
            //textView.setText(UserData.loginResponse.CompanyLabel.);

        } catch (Exception e){
            e.printStackTrace();
        }

    }


}