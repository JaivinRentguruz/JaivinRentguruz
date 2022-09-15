package com.rentguruz.app.model.company;

import java.io.Serializable;

public class CompanyModel  implements Serializable {

    public boolean IsActive,IsSelfManaged,HasPrimaryContact,Entry4Booking;
    public String Name,TradeAs,Website,EmailId,Telephone,RegNumber,OwnerFirstName,OwnerLastName,OwnerTitle,OwnerTelephone,OwnerEmailId,
            ContactFullName,ContactTitle,ContactTelephone,ContactEmailId,CurrencyAlias;
    public int TimeZoneId,BaseDefaultCurrencyId,Distance,FuelMeasurement,DefaultDateFormat,LanguageMasterId;

    public CompanyAddressModel AddressesModel = new CompanyAddressModel();

}
