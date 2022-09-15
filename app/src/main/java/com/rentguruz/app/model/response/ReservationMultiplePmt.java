package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationMultiplePmt implements Serializable {

    public ArrayList<ReservationPMT> ReservationPmt = new ArrayList<ReservationPMT>();
}
