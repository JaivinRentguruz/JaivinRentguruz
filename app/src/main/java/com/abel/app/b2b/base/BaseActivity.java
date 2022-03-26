package com.abel.app.b2b.base;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.abel.app.b2b.R;

public class BaseActivity extends AppCompatActivity {


    public void getBottomview(View view){
        view = view.findViewById(R.id.lblcontinue1);
    }

}
