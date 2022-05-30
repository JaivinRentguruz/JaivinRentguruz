package com.rentguruz.app.model;

import android.text.Html;

import androidx.databinding.Observable;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class AddressesModel extends BaseModel implements Serializable, Observable {
    public String Street, UnitNo, City, ZipCode,  StateName, CountryName;
    public int AddressType, AddressFor, CountryId, StateId, ZoneId;

    public String Drivername, Fname,Lname;
    public int Id;

    public String Latitude, Longitude;
    public String PreviewAddress;


}
