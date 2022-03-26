package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class ReservationChargesModels extends BaseModel implements Serializable {
    public int ChargeFor,Amount,AmountType,ChargeType;
    public Boolean IsTaxable;
}
