package com.rentguruz.app.model.common;

import java.io.Serializable;

public class OnDropDownList implements Serializable {

    public int Id,TableType;
    public String Name;

    public OnDropDownList(int id, String name) {
        Id = id;
        Name = name;
    }

    public OnDropDownList() {
    }
}
