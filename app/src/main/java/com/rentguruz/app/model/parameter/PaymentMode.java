package com.rentguruz.app.model.parameter;

public enum PaymentMode {

    Cash   (1),
    DebitCard (2),
    Cheque  (3),
    CreditCard  (4);

    public int inte;
    PaymentMode(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }

}
