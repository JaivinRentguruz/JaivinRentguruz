package com.abel.app.b2b.model.display;

import com.abel.app.b2b.model.AttachmentsModel;
import com.abel.app.b2b.model.base.BaseModel;

import java.util.ArrayList;

public class MobileBasicDetails extends BaseModel {
    public int Id;
    public String ContentType;

    public ArrayList<AttachmentsModel> IconAttachmentsModels = new ArrayList<>();
    public ArrayList<AttachmentsModel> LogoAttachmentsModels = new ArrayList<>();

}
