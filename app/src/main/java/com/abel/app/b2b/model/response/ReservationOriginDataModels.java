package com.abel.app.b2b.model.response;


import java.io.Serializable;

public class ReservationOriginDataModels implements Serializable {
    public int TableType;
    public String JsonData;

    public ReservationOriginDataModels(int tableType, String jsonData) {
        TableType = tableType;
        JsonData = jsonData;
    }
}
