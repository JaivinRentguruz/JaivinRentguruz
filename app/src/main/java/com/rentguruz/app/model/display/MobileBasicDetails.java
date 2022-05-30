package com.rentguruz.app.model.display;

import com.rentguruz.app.model.AttachmentsModel;
import com.rentguruz.app.model.base.BaseModel;

import java.util.ArrayList;

public class MobileBasicDetails extends BaseModel {
    public int Id;
    public String ContentType;
    public String DetailJson;
    public ArrayList<AttachmentsModel> IconAttachmentsModels = new ArrayList<>();
    public ArrayList<AttachmentsModel> LogoAttachmentsModels = new ArrayList<>();

}
