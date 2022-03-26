package com.abel.app.b2b.model;

import java.io.Serializable;

public class VehicleOptionMappingModel implements Serializable {

   public int VehicleId,VehicleOptionId;
   public String  Name;

    public VehicleOptionMappingModel(int vehicleId, int vehicleOptionId, String name) {
        VehicleId = vehicleId;
        VehicleOptionId = vehicleOptionId;
        Name = name;
    }

    public VehicleOptionMappingModel( int vehicleOptionId, String name) {
        VehicleOptionId = vehicleOptionId;
        Name = name;
    }
}
