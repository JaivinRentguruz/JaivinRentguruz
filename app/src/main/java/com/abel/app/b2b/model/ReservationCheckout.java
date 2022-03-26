package com.abel.app.b2b.model;

import com.abel.app.b2b.model.response.RIvehicleModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationCheckout implements Serializable {
    public int ReservationId;
    public RIvehicleModel ReservationVehicleModel = new RIvehicleModel();

   // public ReservationCheckListModels ReservationCheckListModels = new ReservationCheckListModels();

    public ReservationCheckOutModel ReservationCheckOutModel = new ReservationCheckOutModel();

    public ArrayList<ReservationCheckListModels> ReservationCheckListModels = new ArrayList<ReservationCheckListModels>();
}
