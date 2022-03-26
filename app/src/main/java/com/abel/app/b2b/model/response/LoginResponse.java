package com.abel.app.b2b.model.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public ApiUserLogin apiUserLogin;
    public User User;
    public LogedInCustomer LogedInCustomer;
    public CompanyLabel CompanyLabel;
}
