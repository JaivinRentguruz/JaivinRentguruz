package com.rentguruz.app.model.response;

import com.rentguruz.app.model.AddressesModel;
import com.rentguruz.app.model.CreditCardModel;
import com.rentguruz.app.model.DrivingLicenceModel;
import com.rentguruz.app.model.InsuranceDetailsModel;
import com.rentguruz.app.model.UserModel;

import java.io.Serializable;

public class CustomerProfile implements Serializable {
    public int CompanyId, CustomerTypeId, Age, Id;
    public String Fname, Lname, Email, FullName, MobileNo;
    public String DOB;

    public AddressesModel AddressesModel;
    public DrivingLicenceModel DrivingLicenceModel;
    public UserModel UserModel;
    public CreditCardModel CreditCardModel;

    public InsuranceDetailsModel InsuranceDetailsModel;

    public Boolean IsTaxExemption;

    public CustomerSummary CustomerSummary;
}
