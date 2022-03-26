package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class ReserversationSummary extends BaseModel implements Serializable {
    public String Title,Description,ShortDescription;

    public int ReservationSummaryDetailType;
    public Double Total,PricePerTime;
}
