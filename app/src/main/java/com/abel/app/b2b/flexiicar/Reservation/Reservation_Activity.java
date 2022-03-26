package com.abel.app.b2b.flexiicar.Reservation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.abel.app.b2b.R;
import com.abel.app.b2b.flexiicar.TimeLine.Timeline_MainActivity;
import com.abel.app.b2b.flexiicar.booking2.Customer_Booking_Activity;
import com.abel.app.b2b.flexiicar.user.User_Profile;

public class Reservation_Activity extends AppCompatActivity {
    ImageView home_icon;
    ImageView Profile_icon,timeline,reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation2);

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        try {
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = hostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph_user);
            navGraph.setStartDestination(R.id.Reservation);
            navController.setGraph(navGraph);
        } catch (Exception e){
            e.printStackTrace();
        }


        home_icon=(ImageView) findViewById(R.id.home_icon);
        Profile_icon=(ImageView) findViewById(R.id.profile_icon);
        Profile_icon.setColorFilter(getResources().getColor(R.color.bottommenudeactivecolor));
        Profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=(new Intent(getApplicationContext(), User_Profile.class));
                startActivity(intent1);
            }
        });

        home_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (home_icon.isActivated()){
                    home_icon.setActivated(false);
                } else {
                    home_icon.setActivated(true);
                }

                //Intent intent1=(new Intent(getApplicationContext(), Booking_Activity.class));
                Intent intent1=(new Intent(getApplicationContext(), Customer_Booking_Activity.class));
                startActivity(intent1);
            }
        });
        reservation = findViewById(R.id.reservation);
        timeline = findViewById(R.id.timeline);
        reservation.setColorFilter(getResources().getColor(R.color.bottommenuactivecolor));
        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=(new Intent(getApplicationContext(), Timeline_MainActivity.class));
                startActivity(intent1);
            }
        });
    }

    public void BottomnavVisible()
    {
        LinearLayout lblcontinue1 = (LinearLayout) findViewById(R.id.lblcontinue1);
        lblcontinue1.setVisibility(View.VISIBLE);

        LinearLayout MainFragment = (LinearLayout) findViewById(R.id.MainFragment);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) MainFragment.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
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