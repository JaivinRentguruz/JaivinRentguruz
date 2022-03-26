package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.InsuranceModel;

import java.io.Serializable;

public class RInsuranceModel implements Serializable {
    public boolean IsSureInsurance = false,IsInsuranceDecline = false;
    public int NoOfDays,InsuranceCoverDetailId;
    public String Remarks,InsuranceDate;

    public InsuranceModel InsuranceDetailsModel = new InsuranceModel();

    public String Name;
}
