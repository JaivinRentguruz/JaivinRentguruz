package com.abel.app.b2b.model.checkinout;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class ReservationCheckInModel extends BaseModel implements Serializable {

    public int VehicleId,ReservationId,      CheckInOdo, IsNoExtraMiles ,ReturnFuelInPR ,ReturnFuelInUnit ,IsNoExtraFuel     ;
    public String ReturnDate ,ReturnLocation ;
}
