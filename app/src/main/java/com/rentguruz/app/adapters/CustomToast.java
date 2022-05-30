package com.rentguruz.app.adapters;
import com.rentguruz.app.R;
import com.rentguruz.app.model.base.UserData;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

    public static void showToast(Activity activity, String msg, int type)
    {

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) activity.findViewById(android.R.id.content), false);

        if(type == 1)
            layout.setBackground(activity.getDrawable(R.drawable.curve_box_error));
        if (type == 0 ){
            layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(UserData.UiColor.primary)));
        }
        TextView tv = (TextView) layout.findViewById(R.id.txtMsg);
        tv.setText(msg);
        Toast toast = new Toast(activity.getBaseContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }

}
