package com.abel.app.b2b.model.response;

import java.io.Serializable;

public class StateModel implements Serializable {
    public String Name,TwoLetterIsoCode;
    public int Id,CallingCode,DetailId,TotalRecord;
    public Boolean IsActive;
    public String auditLogModel,dataTableRequestModel,fIds;
}
