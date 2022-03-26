package com.abel.app.b2b.model.reservation;

import com.abel.app.b2b.model.AttachmentsModel;
import com.abel.app.b2b.model.base.BaseModel;

public class ReservationEquipment extends BaseModel {
    public String Name,Description,SerialNo,Make,Model;
    public int Id,Quantity,TaxMasterId,LocationId,DetailId;
    public Boolean IsChargePerDay,UseMaxPrice,IsTaxable,GetForReservation;
    public Double Price,MaxPrice,ReplacementCost;

    public AttachmentsModel AttachmentsModel;
}
