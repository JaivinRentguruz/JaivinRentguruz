package com.abel.app.b2b.model.reservation;

import com.abel.app.b2b.model.response.ReservationOriginDataModels;

import java.util.ArrayList;

public class ReservationDataChanges {

    public int ReservationId;

    public String CheckInDate,CheckOutDate;

    public Boolean SendNotificationToCustomer;

    public ArrayList<ReservationOriginDataModels> ReservationOriginDataModels = new ArrayList<ReservationOriginDataModels>();
}
