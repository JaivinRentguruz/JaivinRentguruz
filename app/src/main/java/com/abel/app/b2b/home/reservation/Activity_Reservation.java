package com.abel.app.b2b.home.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abel.app.b2b.R;
import com.abel.app.b2b.adapters.Helper;
import com.abel.app.b2b.home.Activity_Home;
import com.abel.app.b2b.home.Fragment_Selected_Location;
import com.abel.app.b2b.home.agreement.Activity_Agreements;
import com.abel.app.b2b.home.more.Activity_MoreTab;
import com.abel.app.b2b.home.vehicles.Activity_Vehicles;
import com.abel.app.b2b.model.base.UserData;

public class Activity_Reservation extends AppCompatActivity {

    LinearLayout tab_home,tabReservation,tab_agreement,tab_Vehicles,tab_More;
    LinearLayout bottommenu;
    ImageView reservationIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

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


        tab_home=findViewById(R.id.tab_home);
        tabReservation=findViewById(R.id.tabReservation);
        tab_agreement=findViewById(R.id.tab_agreement);
        tab_Vehicles=findViewById(R.id.tab_Vehicles);
        tab_More=findViewById(R.id.tab_More);
        reservationIcon = findViewById(R.id.reservationIcon);
       // reservationIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_reservation2));
        reservationIcon.setColorFilter(getResources().getColor(R.color.bottommenuactivecolor));
        tab_More.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_MoreTab.class);
                startActivity(i);
            }
        });

        tab_agreement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Agreements.class);
                startActivity(i);
            }
        });

        tab_Vehicles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Vehicles.class);
                startActivity(i);
            }
        });


        tab_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Home.class);
                startActivity(i);
            }
        });

        try {
           new  Activity_Home().setLable(UserData.loginResponse.CompanyLabel);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_reservation_tab);

            if (Helper.B2BRESERVATION){
                new Fragment_Selected_Location().startdatejourney = null;
                navGraph.setStartDestination(R.id.Location);
                navController.setGraph(navGraph);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}