package com.abel.app.b2b.model;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class UserModel extends BaseModel implements Serializable {

   // public int Id,DetailId;
   // public boolean IsActive;

    public int UserType,UserFor;
    public String UserName,Email,Password;
}
