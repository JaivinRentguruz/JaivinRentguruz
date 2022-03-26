package com.abel.app.b2b.model.parameter;

public enum AttachmentType {

    DrivingLicenseFront (30),
    DrivingLicenseBack  (31)/*,
    Booking(3),
    Agreement(4),
    Checkin(5),
    Checkout(6)*/;

    public int inte;
    AttachmentType(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
