package com.rentguruz.app.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.rentguruz.app.R;
import com.rentguruz.app.model.display.ThemeColors;

public class CustomDrawable {

    public Context context;
    public ThemeColors Uicolor;


    public CustomDrawable(Context context, ThemeColors uicolor) {
        this.context = context;
        this.Uicolor = uicolor;
    }

    public LayerDrawable getImageUpload(){
        LayerDrawable layerDrawable = (LayerDrawable)  context.getResources()
                .getDrawable(R.drawable.custome_cam_select);
        Drawable drawable =   layerDrawable.findDrawableByLayerId(R.id.sample);
        drawable.setTint(Color.parseColor(Uicolor.primary));

        return layerDrawable;
    }

    public void toggle(ToggleButton button, Boolean value){
        if (value) {
            button.getBackground().setColorFilter(Color.parseColor(Uicolor.primary), PorterDuff.Mode.SCREEN);
        } else {
            button.getBackground().setColorFilter(Color.parseColor(Uicolor.secondary), PorterDuff.Mode.SCREEN);
        }
    }

    public void enablechangeEdit(EditText edtEmail, Boolean value){
        edtEmail.setFocusable(value);
        edtEmail.setEnabled(value);
        edtEmail.setCursorVisible(value);
        /*if (value)
        edtEmail.setKeyListener(null);*/
    }

    public void checkbtn(RadioButton btn){
        btn.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Uicolor.primary)));
    }

    public void checkbtn(CheckBox btn){
        btn.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Uicolor.primary)));
    }

    public Drawable getShortSpinner(){
      return context.getResources().getDrawable(R.drawable.ic_small_dropdown);
    }

}
