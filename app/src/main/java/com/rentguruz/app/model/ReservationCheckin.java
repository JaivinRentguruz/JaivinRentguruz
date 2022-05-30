package com.rentguruz.app.model;

import com.rentguruz.app.model.response.RIvehicleModel;
import com.rentguruz.app.model.response.ReservationOriginDataModels;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationCheckin implements Serializable {
    public int ReservationId;
    public Boolean IsSaveFinalCheckInProcess;
    public RIvehicleModel ReservationVehicleModel = new RIvehicleModel();

   // public ReservationCheckListModels ReservationCheckListModels = new ReservationCheckListModels();

    public ReservationCheckInModel ReservationCheckInModel = new ReservationCheckInModel();

    public ArrayList<ReservationCheckListModels> ReservationCheckListModels = new ArrayList<ReservationCheckListModels>();

    public ArrayList<ReservationOriginDataModels>  ReservationOriginDataModels = new ArrayList<ReservationOriginDataModels>();
}
