package com.rentguruz.app.model.checkinout;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class ReservationCheckInModel extends BaseModel implements Serializable {

    public int VehicleId,ReservationId,      CheckInOdo, IsNoExtraMiles ,ReturnFuelInPR ,ReturnFuelInUnit ,IsNoExtraFuel     ;
    public String ReturnDate ,ReturnLocation ;
}
