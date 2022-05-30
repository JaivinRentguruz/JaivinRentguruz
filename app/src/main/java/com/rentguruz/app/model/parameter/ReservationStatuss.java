package com.rentguruz.app.model.parameter;

public enum ReservationStatuss {
    Draft (1),
    Rejected (2),
    Cancelled (3),
    Confirmed (4),
    CheckOut (5),
    CheckIn (6),
    PendingPayment (7),
    PaymentOutStanding (8),
    DepositOutStanding (9),
    ReadyForCheckOut (10),
    NoShow  (11),
    OverDue   (12),
    Closed (15)
    ;


    public int inte;
    ReservationStatuss(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
