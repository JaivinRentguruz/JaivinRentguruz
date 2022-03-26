package com.abel.app.b2b.adapters;

import java.text.DecimalFormat;

public class DigitConvert {

    public static String getDoubleDigit(Double data){
        DecimalFormat df = new DecimalFormat("#.00");
        String text = df.format(data);
        return text;
    }
}
