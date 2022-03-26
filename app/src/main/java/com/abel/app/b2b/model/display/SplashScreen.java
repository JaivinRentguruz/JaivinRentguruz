package com.abel.app.b2b.model.display;

import com.abel.app.b2b.model.AttachmentsModel;
import com.abel.app.b2b.model.base.BaseModel;

import java.util.ArrayList;

public class SplashScreen extends BaseModel {

    public int Id;
    public String ContentType;
    public ArrayList<AttachmentsModel> AttachmentsModels = new ArrayList<>();
    public ArrayList<CMSManagementModels> CMSManagementModels = new ArrayList<>();
}
