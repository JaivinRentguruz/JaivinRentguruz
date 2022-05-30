package com.rentguruz.app.home.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.home.Activity_Home;
import com.rentguruz.app.home.Fragment_Selected_Location;
import com.rentguruz.app.home.agreement.Activity_Agreements;
import com.rentguruz.app.home.more.Activity_MoreTab;
import com.rentguruz.app.home.vehicles.Activity_Vehicles;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.CompanyLabel;

public class Activity_Reservation extends AppCompatActivity {

    LinearLayout tab_home,tabReservation,tab_agreement,tab_Vehicles,tab_More;
    LinearLayout bottommenu;
    ImageView reservationIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        tab_home=findViewById(R.id.tab_home);
        tabReservation=findViewById(R.id.tabReservation);
        tab_agreement=findViewById(R.id.tab_agreement);
        tab_Vehicles=findViewById(R.id.tab_Vehicles);
        tab_More=findViewById(R.id.tab_More);
        reservationIcon = findViewById(R.id.reservationIcon);
       // reservationIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_reservation2));
        bottommenu = findViewById(R.id.bottommenu);
        //homeIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_home2));
        bottommenu.setBackgroundColor(Color.parseColor(UserData.UiColor.secondary));
        reservationIcon.setColorFilter(Color.parseColor(UserData.UiColor.primary));
        tab_More.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_MoreTab.class);
                startActivity(i);
                finish();
            }
        });

        tab_agreement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Agreements.class);
                startActivity(i);
                finish();
            }
        });

        tab_Vehicles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Vehicles.class);
                startActivity(i);
                finish();
            }
        });


        tab_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Activity_Reservation.this, Activity_Home.class);
                startActivity(i);
                finish();
            }
        });

        try {
            setLable(UserData.loginResponse.CompanyLabel);
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