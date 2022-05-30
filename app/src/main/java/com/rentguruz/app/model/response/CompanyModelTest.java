package com.rentguruz.app.model.response;

import com.rentguruz.app.model.AddressesModel;

import java.io.Serializable;

public class CompanyModelTest implements Serializable {
    public String Name,Domain,Lancode,DefaultSMSNo,DefaultCurrency,TradeAs,Website,EmailId,Telephone,RegNumber,OwnerFullName,OwnerTitle,OwnerTelephone,OwnerEmailId;
    public int DefaultDateFormat,Distance,FuelMeasurement,Environment,MinReservationDays,DefaultAgreementType,DefaultReservationType;
    public Boolean AllowFutureDateReser,IsReservationSignReq,IsDuplicateReservation,AllowCustMultiRes,IsDailySummaryAlert,IsSuperCompany;
    public Double LatePaymentFee,IsSelfManaged,HasPrimaryContact;
    public int LatePayAfterDay,DefaultReservationPickUpLocId,DefaultReservationDropLocId,NumberOfLocationAllowed,NumberOfVehicleAllowed,MinDOBAge,TimeZoneId;
    public int BaseDefaultCurrencyId,LanguageMasterId,DefaultVehicleTypeId;
    public String ContactFullName,ContactTitle,ContactTelephone,ContactEmailId;
    public AddressesModel AddressesModel;

}
