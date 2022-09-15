package com.rentguruz.app.model.parameter;

public enum  VehicleStatus {
    /*Available("1","Available"),
    Unavailable ("2","Unavailable"),
    Accident("3","Accident"),
    Maintenance("4","Maintenance"),
    OnRent("5","OnRent"),
    CertiAndLicExpire("6","CertiAndLicExpire"),
    OnHold("7","OnHold"),
    Sell("8","Sell"),*/

   /* Available(1),
    Unavailable (2),
    Accident(3),
    Maintenance(4),
    OnRent(5),
    CertiAndLicExpire(6),
    OnHold(7),
    Sell(8);*/


    Availables,
    Available,
    Unavailable,
    Accident,
    Maintenance,
    OnRent,
    CertiAndLicExpire,
    OnHold,
    Sell;

  /*  String status;
    String res;
    VehicleStatus(String i, String s) {
        this.res = s;
        this.status=i;
    }
    public int inte;
    VehicleStatus(int i) {
        this.inte = i;
    }

    VehicleStatus( String s) {
        this.res = s;
    }

    @Override
    public String toString() {
        return res;
    }


    public static VehicleStatus fromString(String text) {
        for (VehicleStatus b : VehicleStatus.values()) {
            if (b.status.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }*/
}

