package com.abel.app.b2b.model.parameter;

public enum ReservationEditStatus {

    DATE   (1),
    Amount    (2);

    public int inte;
    ReservationEditStatus(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
