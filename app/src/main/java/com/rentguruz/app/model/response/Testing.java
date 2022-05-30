package com.rentguruz.app.model.response;

import java.io.Serializable;

public class Testing<T> implements Serializable {
    public Boolean Status;
    public String Message;
    public T Data;
}
