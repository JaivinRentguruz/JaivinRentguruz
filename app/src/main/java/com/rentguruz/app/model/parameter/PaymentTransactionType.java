package com.rentguruz.app.model.parameter;

public enum PaymentTransactionType {

    Payment  (1),
    Deposit   (2),
    PreAuthorization (3),
    Refund (4),
    Void (5),
    Capture (6);

    public int inte;
    PaymentTransactionType(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
