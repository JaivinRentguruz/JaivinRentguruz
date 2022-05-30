package com.rentguruz.app.model.base;

import java.io.File;
import java.io.Serializable;

public class UploadImage implements Serializable {
    public int Id,fileUploadType,CompanyId,FileUploadMasterId;
    public File file;
    public String test;
   // public File Path;
}
