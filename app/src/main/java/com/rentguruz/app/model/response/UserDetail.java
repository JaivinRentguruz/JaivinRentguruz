package com.rentguruz.app.model.response;

import java.io.Serializable;

public class UserDetail implements Serializable {

    public int Id;
    public String Token;
    public String Created;
    public int UserId;
    public String ExpiredInTime;
    public boolean IsAdmin;

}
