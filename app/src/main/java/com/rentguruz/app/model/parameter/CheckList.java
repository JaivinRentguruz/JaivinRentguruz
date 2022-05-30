package com.rentguruz.app.model.parameter;

public enum CheckList {
    All(1),
    CRM (2),
    Booking(3),
    Agreement(4),
    Checkin(5),
    Checkout(6);

    public int inte;
    CheckList(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
