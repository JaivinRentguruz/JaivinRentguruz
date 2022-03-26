package com.abel.app.b2b.model.response;

import com.abel.app.b2b.model.base.BaseModel;

import org.json.JSONObject;

import java.io.Serializable;

public class ReservationPMT extends BaseModel implements Serializable {

    public String AgreementNumber;
    public int BillTo,CreditCardId,CustomerId,PaymentForId,PaymentProcess,PaymentProcessMode,ReservationId,SplitAmount,SplitAmountType;
    public String BillToInfoJSON;
    public Boolean IsSplit;
    public int TransactionType = 2;

    public Double Amount,InvoiceAmount;

    public int PaymentTransactionType,PaymentMode;
}
