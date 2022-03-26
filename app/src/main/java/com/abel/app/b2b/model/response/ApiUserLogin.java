package com.abel.app.b2b.model.response;

import java.io.Serializable;
import java.util.Date;

public class ApiUserLogin implements Serializable {
    public int Id,UserId;
    public String Token;
    public Date Created,ExpiredInTime;
    public boolean IsAdmin;
}
