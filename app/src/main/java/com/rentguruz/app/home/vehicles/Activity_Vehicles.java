package com.rentguruz.app.home.vehicles;

import androidx.appcompat.app.AppCompatActivity;

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
import com.rentguruz.app.home.Activity_Home;
import com.rentguruz.app.home.agreement.Activity_Agreements;
import com.rentguruz.app.home.more.Activity_MoreTab;
import com.rentguruz.app.home.reservation.Activity_Reservation;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.CompanyLabel;

public class Activity_Vehicles extends AppCompatActivity {
    LinearLayout tab_home,tabReservation,tab_agreement,tab_Vehicles,tab_More;
    LinearLayout bottommenu;
    ImageView vehicleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

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
        bottommenu = findViewById(R.id.bottommenu);
        vehicleIcon = findViewById(R.id.vehicleIcon);
       // vehicleIcon.setImageDrawable(getDrawable(R.drawable.ic_tab_vehicles2));
        bottommenu.setBackgroundColor(Color.parseColor(UserData.UiColor.secondary));
        //vehicleIcon.setColorFilter(getResources().getColor(R.color.bottommenuactivecolor));
        vehicleIcon.setColorFilter(Color.parseColor(UserData.UiColor.primary));
        tab_More.setOnClickListener(view -> {
            Intent i = new Intent(Activity_Vehicles.this, Activity_MoreTab.class);
            startActivity(i);
        });

        tab_agreement.setOnClickListener(view -> {
            Intent i = new Intent(Activity_Vehicles.this, Activity_Agreements.class);
            startActivity(i);
        });

        tabReservation.setOnClickListener(view -> {
            Intent i = new Intent(Activity_Vehicles.this, Activity_Reservation.class);
            startActivity(i);
        });


        tab_home.setOnClickListener(view -> {
            Intent i = new Intent(Activity_Vehicles.this, Activity_Home.class);
            startActivity(i);
        });

        try {
            setLable(UserData.loginResponse.CompanyLabel);
            new  Activity_Home().setLable(UserData.loginResponse.CompanyLabel);
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