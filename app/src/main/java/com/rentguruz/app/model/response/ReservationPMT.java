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

    public ReservationPMT() { }

    public ReservationPMT(Double amount) {
        Amount = amount;
    }

    public ReservationPMT(String agreementNumber, int billTo, int creditCardId, int customerId, int paymentForId, int paymentProcess, int paymentProcessMode, int reservationId, int splitAmount, int splitAmountType, String billToInfoJSON, Boolean isSplit, int transactionType, Double amount, Double invoiceAmount, int paymentTransactionType, int paymentMode) {
        AgreementNumber = agreementNumber;
        BillTo = billTo;
        CreditCardId = creditCardId;
        CustomerId = customerId;
        PaymentForId = paymentForId;
        PaymentProcess = paymentProcess;
        PaymentProcessMode = paymentProcessMode;
        ReservationId = reservationId;
        SplitAmount = splitAmount;
        SplitAmountType = splitAmountType;
        BillToInfoJSON = billToInfoJSON;
        IsSplit = isSplit;
        TransactionType = transactionType;
        Amount = amount;
        InvoiceAmount = invoiceAmount;
        PaymentTransactionType = paymentTransactionType;
        PaymentMode = paymentMode;
    }
}
