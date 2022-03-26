package com.abel.app.b2b.model.vehicle;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class VehicleOtherDetailsModel extends BaseModel implements Serializable {

    public Double AdditionalExpenses;

    public String Description,MOTExpiryDate,Memo,ModelCode,PHVExpiryDate,PHVNumber,TaxDiscExpiryDate;

    public int RateId,VehicleId;
}
