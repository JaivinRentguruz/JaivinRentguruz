package com.rentguruz.app.model;

import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;

public class InsuranceModel extends BaseModel implements Serializable {
    public int InsuranceType,InsuranceFor,InsuranceCompanyId,Id;
    public String PolicyNo,Description;
    public String IssueDate,ExpiryDate;
    public int Deductible,CoverLimit,VerifiedBy;
 //   public InsuranceCompanyDetailsModel InsuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();

    public Boolean GetCompanyDetail;

    public AttachmentsModel AttachmentsModel = new AttachmentsModel();
}
