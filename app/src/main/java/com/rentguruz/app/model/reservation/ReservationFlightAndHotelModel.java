package com.rentguruz.app.model.reservation;

import java.io.Serializable;

public class ReservationFlightAndHotelModel implements Serializable {

    public String ArrFlightNo,DepFlightNo,HotelName,ArrFlightDate,DepFlightDate,PhoneNumber,FlightBookingReference,Remarks;

    public Boolean IsAirportDelivery,IsAirportDropOff,IsArrAirportShuttle,IsDepAirportShuttle;

    public int FlightPersonInCharge;
}
