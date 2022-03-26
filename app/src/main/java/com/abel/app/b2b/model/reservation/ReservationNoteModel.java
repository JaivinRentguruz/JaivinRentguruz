package com.abel.app.b2b.model.reservation;

import java.io.Serializable;

public class ReservationNoteModel implements Serializable {

    public String ExternalNote,ExternalRemDate,InternalNote,InternalRemDate;
    public Boolean ExternalReminder,ExternalPrintOnAGR,ExternalSendSMS,InternalPrintOnAGR,InternalReminder,InternalSendSMS;
}
