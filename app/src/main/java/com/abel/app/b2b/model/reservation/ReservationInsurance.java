package com.abel.app.b2b.model.reservation;

import com.abel.app.b2b.model.base.BaseModel;
import com.abel.app.b2b.model.response.ReserversationSummary;

import java.io.Serializable;

public class ReservationInsurance extends BaseModel implements Serializable {

    public String ColorCode, Description, Name;

    public int DetailId,  LocationId;

    public ReserversationSummary ReservationSummaryDetailModel;

    public Double ExcessCharge,PerDayCharge;

    public Boolean IsActiveDetail, IsDefault, IsSelected, isShowNoData;
}
