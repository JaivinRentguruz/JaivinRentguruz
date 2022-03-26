package com.abel.app.b2b.model.insert;

import com.abel.app.b2b.model.base.BaseModel;

import java.util.ArrayList;

public class InsertEquipment extends BaseModel {

    public String Description, Make, Model, Name;
    public Boolean IsChargePerDay, UseMaxPrice;
    public Double MaxPrice, Price, ReplacementCost;

    public EquipmentDetailModels EquipmentDetailModels;
    public ArrayList<EquipmentTaxMappingModel> EquipmentTaxMappingModel = new ArrayList<EquipmentTaxMappingModel>();

}
