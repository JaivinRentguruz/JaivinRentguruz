package com.rentguruz.app.model.checkinout;

import java.io.Serializable;

public class CheckInCalculationModel implements Serializable {
    public Double Amount;
    public int CalculationType;
    public Boolean IsRequiredCalculation;

    public CheckInCalculationModel(Double amount, int calculationType, Boolean isRequiredCalculation) {
        Amount = amount;
        CalculationType = calculationType;
        IsRequiredCalculation = isRequiredCalculation;
    }
}
