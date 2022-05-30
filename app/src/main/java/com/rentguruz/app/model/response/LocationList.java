package com.rentguruz.app.model.response;

import com.rentguruz.app.model.AddressesModel;
import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class LocationList extends BaseModel implements Serializable {
    public String Name,PhoneNo;
    public AddressesModel AddressesModel = new AddressesModel();
    public int Id;
}
