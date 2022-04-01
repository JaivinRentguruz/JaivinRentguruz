package com.abel.app.b2b.adapters;

import android.text.Html;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.abel.app.b2b.model.base.UserData;
import com.bumptech.glide.Glide;
import com.abel.app.b2b.model.parameter.DateType;
import com.abel.app.b2b.model.parameter.ReservationStatus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomBindingAdapter {
    //public static String name;
    @BindingAdapter({"bind:_imageUrl"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @BindingAdapter({"bind:ExpireMonth","bind:ExpireYear"})
    public static void showExpireDate(TextView text, String month, String year)
    {
        if (month.length() !=0 && year.length() != 0)
            text.setText(month + "/" + year);
    }

    @BindingAdapter({"bind:_value"})
    public static void convertTwoDigit(TextView textView, Double amt)
    {
        /*DecimalFormat df = new DecimalFormat("#.00");
        String _total = df.format(amt);
        textView.setText(_total);*/
        textView.setText( Helper.getAmtount(amt, false));
    }

    @BindingAdapter({"bind:_values"})
    public static void convertTwoDigits(TextView textView, Double amt)
    {
        /*DecimalFormat df = new DecimalFormat("#.00");
        String _total = df.format(amt);
        textView.setText(_total);*/
        textView.setText(Helper.getAmtount(amt, false));
    }

    @BindingAdapter({"bind:_displayDate"})
    public static void dateFullConvert (TextView textView, String date)
    {
        //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);
    }

    @BindingAdapter({"bind:_DayPrice"})
    public static void perday (TextView textView, Double amt)
    {
    /*    //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);*/
        //textView.setText( Helper.getAmtount(amt, false) + " / Per Day");
        textView.setText( Helper.getAmtount(amt, false) + " / Day");
    }

    @BindingAdapter({"bind:_Diposits"})
    public static void deposits (TextView textView, Double amt)
    {
    /*    //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);*/
        //textView.setText( Helper.getAmtount(amt, false) + " / Deposit");
        textView.setText(" "  +Helper.getAmtount(amt, false) + " " + UserData.loginResponse.CompanyLabel.Deposit);
    }

    @BindingAdapter({"bind:_date"})
    public static void date (TextView textView, String  date)
    {
    /*    //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);*/
        textView.setText( Helper.getDateDisplay(DateType.fulldate, date));
    }

 /*   @BindingAdapter({"bind:_reservationstatus"})
    public static void reservationstatus (TextView textView, String  date)
    {
    *//*    //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);*//*
        textView.setText(String.valueOf(ReservationStatuss.fromString(date)));
    }*/

    @BindingAdapter({"bind:_call"})
    public static void mobilenumber(TextView textView,String number)
    {
        StringBuilder   stringBuilder = new StringBuilder();
        try {
            for (int i = 0; i < number.trim().length(); i++) {
                stringBuilder.append(number.charAt(i));
                if (i == 2){
                    stringBuilder.append(" ");
                }
                if ( i == 5){
                    stringBuilder.append(" ");
                }
            }
            textView.setText(stringBuilder);
        } catch (Exception e){
            e.printStackTrace();
        }
        // textView.setText(number);
    }

    @BindingAdapter({"bind:_captlise"})
    public static void captlise(TextView textView, String data){
        try {
            StringBuffer capBuffer = new StringBuffer();
            Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(data);
            while (capMatcher.find()){
                capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
            }
            textView.setText(capMatcher.appendTail(capBuffer).toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @BindingAdapter({"bind:_camelcase"})
    public static void camelcase(TextView textView, String data){
        try {
            boolean whiteSpace = true;

            StringBuilder builder = new StringBuilder(data); // String builder to store string
            final int builderLength = builder.length();

            // Loop through builder
            for (int i = 0; i < builderLength; ++i) {

                char c = builder.charAt(i); // Get character at builders position

                if (whiteSpace) {

                    // Check if character is not white space
                    if (!Character.isWhitespace(c)) {

                        // Convert to title case and leave whitespace mode.
                        builder.setCharAt(i, Character.toTitleCase(c));
                        whiteSpace = false;
                    }
                } else if (Character.isWhitespace(c)) {

                    whiteSpace = true; // Set character is white space

                } else {

                    builder.setCharAt(i, Character.toLowerCase(c)); // Set character to lowercase
                }
            }
            textView.setText(builder.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

/*    @BindingAdapter({"bind:_cap"})
    public static void cap(TextView textView, String data){
        StringBuilder builder = new StringBuilder(data); // String builder to store string
        final int builderLength = builder.length();
        for (int i = 0; i < builderLength; ++i) {

            char c = builder.charAt(i);
            builder.setCharAt(i, Character.toTitleCase(c));
            break;
        }
        // name = String.valueOf(builder.charAt(0));
        textView.setText(String.valueOf(builder.charAt(0)));
    }*/

    @BindingAdapter({"bind:_cap"})
    public static void caps(TextView textView, String data){
        try {
            StringBuilder builder = new StringBuilder(data); // String builder to store string
            final int builderLength = builder.length();
            for (int i = 0; i < builderLength; ++i) {

                char c = builder.charAt(i);
                builder.setCharAt(i, Character.toTitleCase(c));
                break;
            }
            textView.setText(String.valueOf(builder.charAt(0)));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @BindingAdapter({"bind:_capss"})
    public static void capss(TextView textView, String data){
        try {
            String[] slit = data.split(" ");
            for (int j = 0; j < slit.length ; j++) {

                StringBuilder builder = new StringBuilder(slit[j]); // String builder to store string
                final int builderLength = builder.length();
                for (int i = 0; i < builderLength; ++i) {

                    char c = builder.charAt(i);
                    builder.setCharAt(i, Character.toTitleCase(c));
                    break;
                }
                StringBuilder builders = new StringBuilder(data); // String builder to store string
                builders.setCharAt(0, Character.toTitleCase(builders.charAt(0)));
                textView.setText(String.valueOf(builders.charAt(0)) +String.valueOf(builder.charAt(0)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @BindingAdapter({"bind:_kms"})
    public static void kilometers(TextView textView, String data){
        try {
            if (data.matches("0")){
                textView.setText("Unlimited "+ Helper.fueltype  + " Included");
            } else {
              //  textView.setText(Helper.fueltype +" " + data);
                textView.setText(data + " " + Helper.fueltype  + " Included");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }





    @BindingAdapter({"bind:_reservationstatus"})
    public static void reservationstatus (TextView textView, String  date)
    {
    /*    //textView.setText(DateConvert.DateConverter(DateType.fulldate,date,DateType.ddMMyyyyS1));
        String d1 = Helper.getDateDisplay(DateType.fulldate,date);
        String d2 = Helper.getTimeDisplay(DateType.fulldate,date);
        textView.setText(d1 + " , " + d2);*/
        textView.setText(String.valueOf(ReservationStatus.fromString(date)));
    }



  /*  @BindingAdapter({"bind:_cap"})
    public static void cap(TextView textView, String data){
        try{
        StringBuilder builder = new StringBuilder(data); // String builder to store string
        final int builderLength = builder.length();
        for (int i = 0; i < builderLength; ++i) {

            char c = builder.charAt(i);
            builder.setCharAt(i, Character.toTitleCase(c));
            break;
        }
       // name = String.valueOf(builder.charAt(0));
        textView.setText(String.valueOf(builder.charAt(0)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/



    @BindingAdapter({"bind:_html"})
    public static void html(TextView textView, String data){
        try {
            textView.setText(Html.fromHtml(data));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



}
