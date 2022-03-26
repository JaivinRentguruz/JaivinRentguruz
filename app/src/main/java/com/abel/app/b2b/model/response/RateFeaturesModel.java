package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class RateFeaturesModel extends BaseModel implements Serializable {
    public String Name;
    public Boolean IsUnlimitedMiles;
    public int GraceMinutes,OverUsageChagred,OverUsageChagredValue,PayNowDiscount,PayNowDiscountType,
            PrepaidTankCharge,TotalRecord,UpgradeCostAmount;

    public Double MonthlyMilesAllowed,DailyMilesAllowed,WeeklyMilesAllowed,ExtraMilesCharges,FuelCharge,SecurityDeposit;
}
