package com.abel.app.b2b.model.parameter;

public enum RsvCancelDeductionOn {


    ChargeDeposit(1),
    ChargePayment (2),
    ChargeCreditCard (3),
    Cash (4),
    ManualRefund (5),
    NoCancellationFees (6);
    public int inte;
    RsvCancelDeductionOn(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }

}
