package com.rentguruz.app.model.parameter.enums;

public enum RecurringRsvBillingCycle {

    Daily   (1),
    Weekly   (6),
    BiWeekly (14),
    FourWeekly (28),
    ThirtyDays (30);

    public int inte;
    RecurringRsvBillingCycle(int i) {
        this.inte = i;
    }

    @Override
    public String toString() {
        return String.valueOf(inte);
    }

}
