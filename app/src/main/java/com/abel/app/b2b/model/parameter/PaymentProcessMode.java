package com.abel.app.b2b.model.parameter;

public enum PaymentProcessMode {

    Online (1),
    Offline  (2);

    public int inte;
    PaymentProcessMode(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }

/*    private final int id;
    PaymentProcessMode(int id) { this.id = id; }
    public int getValue() { return id; }*/
}
