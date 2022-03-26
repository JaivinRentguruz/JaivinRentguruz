package com.abel.app.b2b.model;

import com.abel.app.b2b.model.base.BaseModel;

import java.io.Serializable;

public class DrivingLicenseBack extends BaseModel implements Serializable {

    public String AttachmentPath,Filename,FileType;

    public int Id,AttachmentType,AttachmentFor,FileUploadMasterId;
    public Boolean IsDeleteOld,IsTempFile,IsWithOutBaseUrl;
}
