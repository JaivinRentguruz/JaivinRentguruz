package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class RateModel extends BaseModel implements Serializable {

 /*   public int RateId;
    public int VehicleTypeId;
    public int LocationId;
    public float HourlyRate;
    public float ExtraHourlyCharge;
    public float HalfDayRate;
    public float MilesCharge;
    public float ExtraDailyRate;
    public float DailyRate;
    public float DailyMilesAllowed;
    public float ExtraWeeklyRate;
    public float WeeklyRate;
    public float WeeklyMilesAllowed;
    public float ExtraWeekendRate;
    public float MonthlyRate;
    public float MonthlyMilesAllowed;
    public float WeekendRate;
    public float OneWayCharge;
    public boolean IsApplyCustomRate,IsCustomRateApply;
    public float StandardDailyRate;
    public float StandardWeeklyRate;
    public float StandardMonthlyRate;
    public float MinDailyRate;
    public float MinWeeklyRate;
    public float MinMonthlyRate;
    public String RateCode;
    public String RateDescription;
    public float HourlyMilesAllowed;
    public float HalfDayMilesAllowed;
    public boolean IsOverUsageCharge;
    public int RateFeatureId;
    public String StartDate ;
    public String EndDate ;
    public boolean IsDefault;
    public boolean IsComplexRate;
    public String lstLocationIds ;
    public String lstVehicleCategoryIds ;
    public String LocationVehicleName ;

    public boolean GetRateFeature;
    public String IsUpdateAppliedInAllRecord ;
    public boolean GetRateForReservation;
  //  public String fStartDate;
  //  public String fEndDate;
    public String RentalRateForShiftModels ;
    public String RentalRateForComplexRateModels ;
    public String IsAlreadyExistLocAndVhlCls ;
    public String fVehicleTypeIds ;
*/
    public String EndDate,fVehicleTypeIds,IsAlreadyExistLocAndVhlCls,IsUpdateAppliedInAllRecord,LocationVehicleName,lstLocationIds,lstVehicleCategoryIds,RateCode,
         RateDescription,RentalRateForComplexRateModels,RentalRateForShiftModels,StartDate;
    public boolean GetRateFeature,GetRateForReservation,IsApplyCustomRate,IsComplexRate,IsDefault,IsOverUsageCharge,isShowNoData,IsWeekendRate,IsCustomRateApply;
    public int DetailId,LocationId,OldId,RateFeatureId,RateId,VehicleTypeId,TotalRecord;
    public double DailyMilesAllowed,DailyRate,ExtraDailyRate,ExtraHourlyCharge,ExtraWeekendRate,ExtraWeeklyRate,FridayRate,HalfDayMilesAllowed,HalfDayRate,HourlyMilesAllowed,
                  HourlyRate,MilesCharge,MinDailyRate,MinMonthlyRate,MinWeeklyRate,MonthlyMilesAllowed,MonthlyRate,OneWayCharge,SaturdayRate,StandardDailyRate,StandardMonthlyRate
            ,StandardWeeklyRate,SundayRate,WeekendRate,WeeklyMilesAllowed,WeeklyRate,ExtraMilesAmount,ExtraFuelCharge;


    public RateFeaturesModel RateFeaturesModel = new RateFeaturesModel();
    public WeekendRatesModel WeekendRatesModel = new WeekendRatesModel();

   public ArrayList<SeasonRatesModels> SeasonRatesModels = new ArrayList<>();
   //001.SeasonRatesModels,

    //public String ;
    public Double CorporateRate,ExtraHourCount,ExtraMonthlyRate,ExtraHalfDayRate,KMCharges,MandatoryDepositAmt,Monthly;
    public int Daily,ExtraDayCount,ExtraHalfDayCount,ExtraMonthCount,ExtraWeekCount,GraceMinutes,HalfDay,Hourly,Id,ReservationId;
    public boolean IsCalculation4CheckIn,IsCorporate,IsDepositMandatory,IsMonthlyRateInclude,IsPrepaidTank,IsSeasonalRate,IsUnlimitedMiles,IsUpgradeCost,IsUsePayNow,PrepaidTankCharge;



    public double Weekly;
    public double UpgradeCostAmount;
    public double PayNowDiscount;
    //public int OverUsageBy;
    public double SecurityDeposit;
   // public double OverUsageChagredValue;
   // public double OverUsageChagred;
    //RateFeaturesModel RateFeaturesModelObject;
    //private String RentalRateForDailyModels = null;
    //private String DailyRentalRateModels = null;
    public String Ids;
    public String fIds;
    //public double APIRequestType;
    public String DetailIds;
}
