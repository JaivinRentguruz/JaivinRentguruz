package com.rentguruz.app.model;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class Checkitems extends BaseModel implements Serializable  {
   public int AppliedIn,DetailId,HeaderId,Id;
   public String CheckListHeaderName,Description,Name;

   public Boolean IsMandatory;
}
