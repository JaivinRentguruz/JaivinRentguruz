package com.rentguruz.app.model.parameter.enums;

public enum ReservationMainType {
    Retail    (1),
    Insurance    (2),
    Loaner (3),
    Recurring  (4);

    public int inte;
    ReservationMainType(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
