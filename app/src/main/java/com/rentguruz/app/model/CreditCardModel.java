package com.rentguruz.app.model;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class CreditCardModel extends BaseModel implements Serializable {
    public int Id,DetailId;
    /*   public boolean IsActive;*/
    //public int creditCardType = 3 type,creditCardFor;
    public String CardCompany,Number,NameOn;
    public int ExpiryMonth,ExpiryYear,CVVCode;
    public boolean IsDefault;
    public String ZipCode;
    public boolean IsFirstInsert;

    public String _expiry = ExpiryMonth+"/"+ExpiryYear;
    public int CreditCardFor;
    public int CreditCardType;

    public AddressesModel AddressesModel;

    public String Fname, Lname,MobileNo,Email;
    public String Last4DigitNumber;

    public String get_expiry() {
        _expiry = ExpiryMonth+"/"+ExpiryYear;
        return _expiry;
    }

    public void set_expiry(String _expiry) {
        this._expiry = _expiry;
    }
}
