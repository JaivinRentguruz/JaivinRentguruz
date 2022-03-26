package com.abel.app.b2b.model.parameter;

public enum SplitAmountType {

    Percentage   (1),
    Amount    (2);

    public int inte;
    SplitAmountType(int i) {
        this.inte = i;
    }
    @Override
    public String toString() {
        return String.valueOf(inte);
    }
}
