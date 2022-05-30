package com.rentguruz.app.model.response;

import java.io.Serializable;

public class SeasonRatesModels  implements Serializable {

    public SeasonalRateModel SeasonalRateModel = new SeasonalRateModel();

    public Double DailyRate,HalfDayRate,HourlyRate,MonthlyRate,WeeklyRate;
}
