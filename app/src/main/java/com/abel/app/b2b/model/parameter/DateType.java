package com.abel.app.b2b.model.parameter;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public enum DateType {
    // "/" start date = 1  "-" 101

    yyyyMMddD("yyyy-MM-dd"),
    yyyyMMddS("yyyy/MM/dd"),

    ddMMyyyyD("dd-MM-yyyy"),
    ddMMyyyyS("dd/MM/yyyy"),

    MMddyyyyS("MM/dd/yyyy"),

    time("HH:mm"),
    time2("hh:mm aa"),

    ddMMyyyyD1("dd-MM-yyyy hh:mm aa"),
    ddMMyyyyS1("dd/MM/yyyy hh:mm aa"),

    fulldate("yyyy-MM-dd'T'HH:mm:ss"),
    ymdd("MMMM dd yyyy EEEE"),

    defaultdate("EEE MMM dd HH:mm:ss zzz yyyy"),
    monthwithdate("MMM dd yyyy"),

    day("EEEE"),

    datetime("MM/dd/yyyy , hh:mm")
    ;

    public String anInt;
    DateType(String i) {this.anInt = i;}

    @Override
    public String toString() {
        return String.valueOf(anInt);
    }
}
