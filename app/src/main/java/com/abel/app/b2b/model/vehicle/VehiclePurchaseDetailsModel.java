package com.abel.app.b2b.model.vehicle;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class VehiclePurchaseDetailsModel extends BaseModel implements Serializable {

    public Double AmountMonthly,DownPayment,PreTaxAmount,TaxAmount,TotalAmount,VehicleId,VendorMasterId;

    public int AnnualMilesAllowed,ApprovedBy,CarPriceBeforeTax,PurchasedBy;

    public String BankName,ChequeNumber,CompanyName,DateOfPayment,Email,LeaseTermsMonth,MobileNo,PaidBy,PaymentFrequency;

    public Boolean Reminder;

}
