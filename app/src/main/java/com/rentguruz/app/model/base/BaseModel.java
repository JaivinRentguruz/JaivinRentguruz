package com.rentguruz.app.model.base;

import android.util.Log;

import androidx.databinding.BaseObservable;

import com.rentguruz.app.adapters.DateConvert;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.model.AddressesModel;
import com.rentguruz.app.model.parameter.DateType;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseModel  extends BaseObservable implements Serializable {
   //public int id, detailId, totalRecord,
   public int APIRequestType= 2;
   public Boolean IsActive = true;
   public int CompanyId = cid();
   public String _imageUrl;
 //  public ArrayList<String> ignoreOnUpdate;
  // public String fIds;

 //  public AuditLogModel auditLogModel;
  // public DataTableRequestModel dataTableRequestModel;

   public String _date;// = DateConvert.DateConverter(DateType.fulldate,CheckInDate, DateType.yyyyMMddS);
   public String _value;
   public int test = cid();
   public int cid(){
      return Helper.di;
   };
  // public   int getRateOfInterest(){return 0;}

}
