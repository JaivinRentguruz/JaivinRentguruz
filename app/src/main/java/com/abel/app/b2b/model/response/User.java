package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.AddressesModel;

import java.io.Serializable;

public class User implements Serializable {
    public int CompanyId,UserType,UserFor,Id;
    public String UserName,Email;
    public Boolean IsSuperAdmin,IsAdmin;
    public AddressesModel addressesModel = new AddressesModel();
}
