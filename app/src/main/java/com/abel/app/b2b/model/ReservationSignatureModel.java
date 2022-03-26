package com.abel.app.b2b.model;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class ReservationSignatureModel extends BaseModel implements Serializable {
   public String img64,Latitude,Longitude;
   public int ReservationId,SignBy;
   public int SignType,Action;
/*   public static final int SignType = 2;
   public static final int Action = 1;
   public static final Boolean IsActive = true;*/
}
