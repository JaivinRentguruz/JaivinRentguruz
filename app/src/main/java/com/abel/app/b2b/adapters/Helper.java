package com.abel.app.b2b.adapters;

import android.os.Bundle;

import com.abel.app.b2b.model.base.UserData;
import com.abel.app.b2b.model.parameter.DateType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static Bundle bundle = new Bundle();
   // public static InsuranceModel insuranceModel = new InsuranceModel();
    public static Boolean insuranceValue = false;
    public static Boolean VISIBLE = false;
    public static Boolean isCustomerVisible = false;
    public static Boolean isRegistrationDone = false;
    public static Boolean isDropdown = true;
    public static Boolean flightDetail = false;
    public static int screenNumber = 0;
    public static Boolean pmt = false;
    public static Boolean defaultInsurance = true;
    public static Boolean isActiveCustomer = false;

    public static Boolean reservationpmt = true;
    public static Boolean reservationwithoutmap = false;
    public static Boolean onRent = false;
    public static Boolean isFirstB2CReservation = true;
    public static Boolean SelectDateDisplay  = false;
    public static Boolean BACKTO = false;
    public static Boolean B2BRESERVATION = false;
    public static Boolean RegistrationD = false;

    public static Boolean AllowCustomerInsurance = false;


    /*public static final String currencySymbol =  UserData.companyModel.CurrencySymbol;
    public static final String currencyName = UserData.companyModel.DisplayCurrency;
    public static final int fuel=UserData.companyModel.FuelMeasurement;
    public static final int id = UserData.companyModel.CompanyId;
    public static final String displayDate = getDisplayDateFormat(UserData.companyModel.DefaultDateFormat);
    public static final String displayTime = getDisplayTimeFormat(2);
    public static final String postDate = "yyyy-MM-dd";
    public static final String fueltype = getfuel(UserData.companyModel.FuelMeasurement);*/


    public static String currencySymbol =  "$";
    public static String currencyName = "AU";
    public static String displaycurrency ="AU$";
    public static String fueel;
    public static int fuel=2;
    //public static final int id = UserData.loginResponse.User.CompanyId;
    public static int id = UserData.companyModel.Id;
    public static int di = 0;
    public static final int screenid =1;
    public static int dateformat=1;
    public static String displayDate = getDisplayDateFormat(dateformat);
    public static String displayTime = getDisplayTimeFormat(dateformat);
    public static String postDate = "yyyy-MM-dd";
    //public static final String fueltype = getfuel(fuel);
    public static String fueltype;
    public static Boolean insertinsuarancefromreservation = false;

    public static String getAmtount(Double amt, Boolean symbol){
        String value = null;
        String amt2 = String.format(Locale.US,"%.2f",amt);
        if (symbol){
            value = displaycurrency + " " + amt2;
        } else {
            value = currencySymbol + " " + amt2;
        }
        return value;
    }

    public static String getAmtount(Double amt){
        String value = null;
        String amt2 = String.format(Locale.US,"%.2f",amt);
        value =  amt2;
        return value;
    }

    public static String getDistance(Double value){
        int i = (int) Math.round(value);
        if (fuel==1){
            return  i +  " " + fueel;
        } else {
            return  i +  " " + fueel;
        }
    }

    public static String getDateDisplay (DateType userdate, String date){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(String.valueOf(userdate), Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(displayDate, Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String setPostDate(String date){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(displayDate, Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(postDate, Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String setPostDate (DateType userdate, String date){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(String.valueOf(userdate), Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(postDate, Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getTimeDisplay (DateType userdate, String date){
        String s = null;
        Date date1 = null;
        SimpleDateFormat datewithtime = new SimpleDateFormat(String.valueOf(userdate), Locale.getDefault());
        SimpleDateFormat returndate =  new SimpleDateFormat(displayTime, Locale.getDefault());
        try {
            date1= datewithtime.parse(date);
            s = returndate.format(date1);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String getDisplayDateFormat(int value){
        switch (value){
            case 1: return  "MM/dd/yyyy";
            case 2: return  "MM/dd/yy";
            case 3: return  "M/d/yyyy";
            case 4: return  "M/d/yy";
            case 5: return  "yyyy-MM-dd";
            case 6: return  "yy/MM/dd";
            case 7: return  "dd-MMM-yy";
        }
        return null;
    }
    public static String getDisplayTimeFormat(int value){
        switch (value){
            case 1: return  "HH:mm";
            case 2: return  "hh:mm aa";
        }
        return null;
    }

    private static String getfuel(int value){
        switch (value){
           /* case 1: return "Kms";
            case 2: return "Mileage";*/
            case 1: return fueel;
            case 2: return fueel;
        }
        return null;
    }

    public static String getCapitalise(String data){
      /*  String upperString = data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
        return  upperString;*/
        String[] splits = data.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
}
