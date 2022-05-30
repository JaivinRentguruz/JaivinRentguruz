package com.rentguruz.app.model.response;

import com.rentguruz.app.model.AddressesModel;

import java.io.Serializable;

public class User implements Serializable {
    public int CompanyId,UserType,UserFor,Id;
    public String UserName,Email;
    public Boolean IsSuperAdmin,IsAdmin;
    public AddressesModel addressesModel = new AddressesModel();
}
