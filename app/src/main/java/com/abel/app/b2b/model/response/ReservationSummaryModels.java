package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class ReservationSummaryModels extends BaseModel implements Serializable {
    public int ReservationSummaryType,TotalDays;
    public String SummaryName;
    public Double TotalAmount;

    public ReserversationSummary[] ReservationSummaryDetailModels;
}
