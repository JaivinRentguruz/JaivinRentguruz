package com.rentguruz.app.model;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class DoRegistration extends BaseModel implements Serializable {

    public String Fname, Lname, Email, MobileNo,DOB;
   // public int CompanyId;
    public Boolean IsDNDSMS, IsDNDEmail;
    public int CustomerType;

    public DrivingLicenceModel DrivingLicenceModel = new DrivingLicenceModel();
    public AddressesModel AddressesModel = new AddressesModel();
    public UserModel UserModel = new UserModel();
    public CreditCardModel CreditCardModel = new CreditCardModel();
    //public Boolean IsActive

    //public String dob;
}
