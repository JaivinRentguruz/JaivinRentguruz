package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.AddressesModel;
import com.abel.app.b2b.model.AttachmentsModel;

import java.io.Serializable;

public class LogedInCustomer implements Serializable {
    public String Email, FullName,MobileNo;
    public AddressesModel AddressesModel = new AddressesModel();
    public AttachmentsModel AttachmentsModel = new AttachmentsModel();
}
