package com.abel.app.b2b.model.insert;

import androidx.annotation.NonNull;

import com.abel.app.b2b.model.base.BaseModel;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class TrafficTickets extends BaseModel implements Serializable {
    public int Id,ReservationId,VehicleId;

    public String Name,Notes,OffenseCode,TicketDate;

    public Double Surcharge,TicketCharges,TotalPayable;

    public Boolean ChargeCustomer,IsTrasferTicketToDriver;

}
