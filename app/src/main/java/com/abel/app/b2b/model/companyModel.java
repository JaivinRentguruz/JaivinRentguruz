package com.abel.app.b2b.model;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class companyModel extends BaseModel implements Serializable {
    public String Name,CurrencySymbol,CurrencyAlias,MUT,DisplayCurrency,DistanceDesc;
    public int MinDOBAge,MinReservationDays,DefaultDateFormat,FuelMeasurement,id,Id,Distance;
    public AddressesModel AddressesModel = new AddressesModel();

    public String ContactEmailId,ContactTelephone,EmailId,Telephone;

    public int DefaultReservationPickUpLocId ,DefaultReservationDropLocId;

    public CompanyPreference CompanyPreference = new CompanyPreference();
}
