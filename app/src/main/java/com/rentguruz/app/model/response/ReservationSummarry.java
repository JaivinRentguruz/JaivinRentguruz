package com.rentguruz.app.model.response;

import com.rentguruz.app.model.base.BaseModel;
import com.rentguruz.app.model.checkinout.CheckInCalculationModel;
import com.rentguruz.app.model.parameter.enums.ReservationRecurringDetailModel;
import com.rentguruz.app.model.reservation.ReservationFlightAndHotelModel;
import com.rentguruz.app.model.reservation.ReservationNoteModel;
import com.rentguruz.app.model.parameter.enums.ReservationRecurringDetailModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationSummarry extends BaseModel implements Serializable {
    public String CheckInDate = "2021-08-24T04:47:45.495Z";
    public String CheckOutDate = "2021-08-21T04:47:45.495Z";
    public int DropLocation = 8;
    public int PickUpLocation = 10;
    public Boolean RateCalculateOnShift = false;
    public int TotalDays;
    public Boolean IsIgnoreLocationClose = true;

    public RIvehicleModel ReservationVehicleModel = new RIvehicleModel();
    public RateModel ReservationRatesModel = new RateModel();
    public RInsuranceModel ReservationInsuranceModel = new RInsuranceModel();
    public ArrayList<RIequipment> ReservationEquipmentInventoryModel = new ArrayList<RIequipment>();
    public ArrayList<RIchauffer> MiscellaneousChargeModels = new ArrayList<RIchauffer>();


    public String CustomerEmail,CustomerPhone,ReservationNo;
    public int CustomerId,ReservationStatus,ReservationType,TypeOf;
    public int Id;

    public String PickUpLocationName,DropLocationName;

    public ArrayList<ReservationOriginDataModels>  ReservationOriginDataModels = new ArrayList<ReservationOriginDataModels>();

    public ArrayList<ReservationSummaryModels> ReservationSummaryModels = new ArrayList<ReservationSummaryModels>();
    //test
   // public ArrayList<Mystate> ReservationChargesModels = new ArrayList<>();

    public ArrayList<ReservationChargesModels> ReservationChargesModels = new ArrayList<ReservationChargesModels>();

    public ArrayList<ReservationDriversModel> ReservationDriversModel = new ArrayList<ReservationDriversModel>();

    public int BusinessSourceId;
    public Boolean GetSummaryForView,IsTaxExemption;

    public ReservationFlightAndHotelModel ReservationFlightAndHotelModel = new ReservationFlightAndHotelModel();
    public ReservationNoteModel ReservationNoteModel = new ReservationNoteModel();

    public ArrayList<Integer> IgnorCalculationSummaryTypes;

    public ReservationRecurringDetailModel ReservationRecurringDetailModel = new ReservationRecurringDetailModel();

    public String SelectedRecurringRsvBillingCycle;

    public ArrayList<SeasonRatesModels> SeasonRatesModels = new ArrayList<>();

    //public RateModel ReservationRatesModel = new RateModel();


    public Boolean IsExtraDayCalculation = true;

    public Boolean IsLoanerMainType = false;

    public ArrayList<CheckInCalculationModel> CheckInCalculationModels = new ArrayList<CheckInCalculationModel>();
}
