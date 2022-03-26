package com.abel.app.b2b.model;

import java.io.Serializable;

public class ReservationCheckListModels implements Serializable {
    public int ReservationId,CheckListId,CheckListType,Condition;

    public ReservationCheckListModels(int reservationId, int checkListId, int checkListType, int condition) {
        ReservationId = reservationId;
        CheckListId = checkListId;
        CheckListType = checkListType;
        Condition = condition;
    }
}
