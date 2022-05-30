package com.rentguruz.app.model.response;

import java.io.Serializable;

public class ReservationDriversModel implements Serializable {
    public int DriverId;

    public ReservationDriversModel(int driverId) {
        DriverId = driverId;
    }
}
