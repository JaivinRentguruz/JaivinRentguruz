package com.abel.app.b2b.model.response;

import java.io.Serializable;

public class RIchauffer implements Serializable {
    public String Name;
    public int ChargeType,DetailId,Amount,TotalUnit;


    public RIchauffer() {
    }

    public RIchauffer(String name, int chargeType, int detailId, int amount, int totalUnit) {
        Name = name;
        ChargeType = chargeType;
        DetailId = detailId;
        Amount = amount;
        TotalUnit = totalUnit;
    }
}
