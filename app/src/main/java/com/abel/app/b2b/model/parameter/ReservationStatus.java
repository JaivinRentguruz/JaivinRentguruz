package com.abel.app.b2b.model.parameter;

public enum ReservationStatus {
    Draft("1","Draft"),
    Rejected ("2","Rejected"),
    Cancelled("3","Cancelled"),
    Confirmed("4","Confirmed"),
    CheckOut("5","CheckOut"),
    CheckIn("6","CheckIn"),
    PendingPayment("7","PendingPayment"),
    PaymentOutStanding("8","PaymentOutStanding"),
    DepositOutStanding("9","DepositOutStanding"),
    Closed("15","Closed")
    ;


    String status;
    String res;
    ReservationStatus(String i, String s) {
        this.res = s;
        this.status=i;
    }

    @Override
    public String toString() {
        return res;
    }


    public static ReservationStatus fromString(String text) {
        for (ReservationStatus b : ReservationStatus.values()) {
            if (b.status.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
