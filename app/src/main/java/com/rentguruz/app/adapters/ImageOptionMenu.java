package com.rentguruz.app.adapters;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

public class ImageOptionMenu {
    public Activity activity;
    public String TAG = "OptionMenu";
    public static final int RESULT_LOAD_IMAGE = 1;
    public ImageOptionMenu(Activity activity) {
        this.activity = activity;
    }


    public void optionmenulist(RelativeLayout sucessfullRegi, View view){
        getText(view, R.id.cancel2).setOnClickListener(v -> optionVisible(sucessfullRegi,false));
        getText(view,R.id.cancel).setOnClickListener(v -> optionVisible(sucessfullRegi,false));

        /*ImageView DLFronsideImg = view.findViewById(R.id.DLFronsideImg);
        DLFronsideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionVisible(sucessfullRegi,true);
                Helper.imagee = "2";
            }
        });*/

       /* getText(view,R.id.cameraopen).setOnClickListener(v ->
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, RESULT_LOAD_IMAGE));*/


      /*  getText(view,R.id.cameraopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, 1);

                optionVisible(sucessfullRegi,false);
            }
        });


        getText(view,R.id.gallaryopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(Intent.ACTION_PICK,
                      //  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //intent.putExtra(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent,
                        RESULT_LOAD_IMAGE);

                optionVisible(sucessfullRegi,false);
            }
        });
*/



    }



    public void setText(TextView view, String text){
        try {
            view.setText(text);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public TextView getText(View v, int id){
        return v.findViewById(id);
    }

    public void optionVisible(RelativeLayout sucessfullRegi,Boolean value){
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(600);
        transition.addTarget( sucessfullRegi);
        TransitionManager.beginDelayedTransition(sucessfullRegi,transition);
        if (value){
            sucessfullRegi.setVisibility(View.VISIBLE);
        } else {
            sucessfullRegi.setVisibility(View.GONE);
        }
    }
}
