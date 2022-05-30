package com.rentguruz.app.model.response;

import com.rentguruz.app.model.AddressesModel;
import com.rentguruz.app.model.AttachmentsModel;

import java.io.Serializable;

public class LogedInCustomer implements Serializable {
    public String Email, FullName,MobileNo;
    public AddressesModel AddressesModel = new AddressesModel();
    public AttachmentsModel AttachmentsModel = new AttachmentsModel();
}
