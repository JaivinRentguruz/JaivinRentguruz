package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class  SeasonalRateModel extends BaseModel implements Serializable {
    public String Name,EndDate,StartDate,Ids;
    public int ChargeType,DetailId,Id,OldId,PaymentType,RateId,Value,Week,TotalRecord;

    public Boolean IsApplyAnnual,IsApplyOnline,IsAutoApply,isShowNoData;



    public int SeasonalType;
    //private String SeasonalRateWeekModels = null;
    public String fIds;
  //  public int APIRequestType;
    public String DetailIds;

}
