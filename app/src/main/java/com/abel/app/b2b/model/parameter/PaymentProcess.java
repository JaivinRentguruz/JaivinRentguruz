package com.abel.app.b2b.model.parameter;

public enum PaymentProcess {

    Charge  (1),
    Refund   (2);

    public int inte;
    PaymentProcess(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
