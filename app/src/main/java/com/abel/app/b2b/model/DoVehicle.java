package com.abel.app.b2b.model;


import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DoVehicle extends BaseModel implements Serializable {
    public String ColorExterior,ColorInterior,KeyCode,LicenseNumber,
            MakeName,Manufacturer,ModelName,Number,VinNumber;

    public int CurrentLocation,EngineId,FuelType,NoOfBags,NoOfDoors,
            NoOfSeats,OwningLocation,Status,TankSize,Transmission,VehicleCategoryId,
            VehicleMakeId,VehicleModelId,VehicleTypeId,Year;

    public int Id;

    public String TransmissionDesc,FuelTypeDesc;

    public List<VehicleOptionMappingModel>  VehicleOptionMappingModel = new ArrayList<>();

    public VehicleOtherDetailsModel VehicleOtherDetailsModel;
}
