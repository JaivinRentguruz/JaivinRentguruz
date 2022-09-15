package com.rentguruz.app.model.checkinout;

import com.rentguruz.app.model.AttachmentsModel;
import com.rentguruz.app.model.base.BaseModel;

public class CheckoutSignatureDisplay extends BaseModel {
    public int ReservationId, SignType;

   public AttachmentsModel AttachmentsModel = new AttachmentsModel();
}
