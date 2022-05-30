package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class RateFeaturesModel extends BaseModel implements Serializable {
    public String Name;
    public Boolean IsUnlimitedMiles;
    public int GraceMinutes,OverUsageChagred,OverUsageChagredValue,PayNowDiscount,PayNowDiscountType,
            PrepaidTankCharge,TotalRecord,UpgradeCostAmount;

    public Double MonthlyMilesAllowed,DailyMilesAllowed,WeeklyMilesAllowed,ExtraMilesCharges,FuelCharge,SecurityDeposit;

    public int DetailId;
    public String DetailIds;

}
