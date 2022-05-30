package com.rentguruz.app.model;

import java.io.Serializable;

public class ReservationCheckInModel implements Serializable {

    public int CheckInOdo,ExtraMiles,ReservationId,ReturnFuelInPR,ReturnFuelInUnit,ReturnLocation,VehicleId,VehicleStatus;

    public Boolean IsActive = true;
    public Boolean IsNoExtraFuel,IsNoExtraMiles;

    public String Note,ReturnDate,ReturnSummary;

    public Double TotalFuelCharge,TotalMilesCharge;

    public int Id;

}
