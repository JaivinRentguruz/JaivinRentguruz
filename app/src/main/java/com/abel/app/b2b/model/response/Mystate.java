package com.abel.app.b2b.model.response;

public class Mystate {
   public String statename;
   public int countrycode, statecode;

    public Mystate(int countrycode, String statename,  int statecode) {
        this.statename = statename;
        this.countrycode = countrycode;
        this.statecode = statecode;
    }
}
