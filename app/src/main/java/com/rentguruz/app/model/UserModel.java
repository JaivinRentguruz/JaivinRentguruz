package com.rentguruz.app.model;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class UserModel extends BaseModel implements Serializable {

   // public int Id,DetailId;
   // public boolean IsActive;

    public int UserType,UserFor,Id;
    public String UserName,Email,Password;
}
