package com.rentguruz.app.model.base;

import java.io.Serializable;

public class DataTableRequestModel  implements Serializable {
    public  int pageSize,limit,offset;
    public String orderDir,orderBy,filter,filterObj;
}
