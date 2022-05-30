package com.rentguruz.app.model.parameter.enums;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class ReservationRecurringDetailModel extends BaseModel implements Serializable {

    public int BillingCycle,Duration,ReservationId,RecurringType,Id;
    public Boolean IsNoReturnDate;

}
