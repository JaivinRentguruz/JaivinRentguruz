package com.abel.app.b2b.model.reservation;

import com.abel.app.b2b.model.AttachmentsModel;
import com.abel.app.b2b.model.base.BaseModel;

public class ReservationInventory extends BaseModel {
   public String Name,Code,Description,Size,ColorCode,Make,Model;
   public int TaxMasterId,DetailId,Id;
   public Boolean IsTaxable,isShowNoData;
   public Double Price;
   public AttachmentsModel AttachmentsModel;
}
