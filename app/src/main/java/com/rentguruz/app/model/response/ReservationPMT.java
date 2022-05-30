package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;

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
