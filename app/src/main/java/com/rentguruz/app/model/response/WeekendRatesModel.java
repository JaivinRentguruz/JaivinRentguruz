package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class WeekendRatesModel extends BaseModel implements Serializable {

    public int  TotalFriday, TotalSaturday, TotalSunday, TotalWeekendDays ;

    public Double DailyRate, FridayRate, HalfDayRate, HourlyRate, MonthlyRate, SaturdayRate, SundayRate,WeekendAverageRate,WeeklyRate;
}
