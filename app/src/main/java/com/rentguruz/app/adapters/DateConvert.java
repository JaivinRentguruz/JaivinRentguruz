package com.rentguruz.app.adapters;

import android.provider.ContactsContract;

import com.rentguruz.app.model.parameter.DateType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConvert {

    public Date gerConvertDate(String dateFormat,String userdate){
        Date date = new Date();
        SimpleDateFormat datewithtime = new SimpleDateFormat(dateFormat);
        try {
            date= datewithtime.parse(userdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String gerServerDate(String userdate){
        String s = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
     //   datewithtime.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            s= datewithtime.parse(userdate).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String datechange(String date){
        String[] d = date.split("/");
        String year = d[2];
        String month = d[1];
        String day = d[0];
        return year+"/"+month+"/"+day;
    }

    public String allDateConverter (DateType userdate, String date, DateType getDate){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(String.valueOf(userdate), Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(String.valueOf(getDate), Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String DateConverter (DateType userdate, String date, DateType getDate){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(String.valueOf(userdate), Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(String.valueOf(getDate), Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return s;
    }
}
