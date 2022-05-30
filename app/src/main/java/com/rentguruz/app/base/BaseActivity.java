package com.rentguruz.app.base;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.rentguruz.app.R;

public class BaseActivity extends AppCompatActivity {


    public void getBottomview(View view){
        view = view.findViewById(R.id.lblcontinue1);
    }

}
