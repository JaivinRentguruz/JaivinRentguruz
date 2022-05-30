package com.rentguruz.app.model.reservation;

import com.rentguruz.app.model.response.ReservationOriginDataModels;

import java.util.ArrayList;

public class ReservationVehicleChange {

    public Boolean IsNoExtraCharge,SendNotificationToCustomer;
    public int ReservationId,VehicleId,VehicleTypeId;

    public ArrayList<ReservationOriginDataModels> ReservationOriginDataModels = new ArrayList<ReservationOriginDataModels>();
}
