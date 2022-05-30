package com.rentguruz.app.model;

import android.util.Log;

import com.github.gcacace.signaturepad.BR;
import com.rentguruz.app.adapters.CustomeDialog;
import com.rentguruz.app.model.base.BaseModel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DrivingLicenceModel extends BaseModel implements Serializable {
   public int LicenceType,LicenceFor,IssueByCountry,IssuedByState;
   public int Id;
   public String Number,Category,IssueByCountryName,IssuedByStateName;
   //public Date IssueDate,ExpiryDate,DOB;
   public String IssueDate,ExpiryDate,DOB;
 //  public String dob,expirydate,issuedate;

   //public boolean IsActive;

   public int RelationId;
   public String DisplayIssuedBy,RelationDesc;


   public String getDOB() {
      if (!DOB.isEmpty()){
         String[] userdate = DOB.split("/");
         String month = userdate[0];
         String day = userdate[1];
         String year = userdate[2];
         DOB = year+"-"+month+"-"+day;

         Log.d("Mungara", "getDOB: " + DOB);
         return DOB;
      }
      return DOB;
   }

   public void setDOB(String DOB) {
      this.DOB = DOB;
      Log.d("Mungara", "setDOB: " + DOB);
   }

   public String getExpiryDate() {
      Log.d("MungaraDate", "getDOB:1 " + ExpiryDate);
      try {
         if (ExpiryDate.length()==19){
            return ExpiryDate;
         }
         else {
            String[] userdate = ExpiryDate.split("/");
            String month = userdate[0];
            String day = userdate[1];
            String year = userdate[2];
            ExpiryDate = year+"-"+month+"-"+day;
            return ExpiryDate;
         }
      } catch (NullPointerException e){
         e.getMessage();
      } catch (Exception e){
         e.printStackTrace();
      }

      return ExpiryDate;
   }

   public void setExpiryDate(String expiryDate) {
      Log.d("MungaraDate", "setDOB:2 " + expiryDate);
 /*     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
      try {
         Date date = sdf.parse(ExpiryDate);
         ExpiryDate = CustomeDialog.dateConvert(date.toString());

      } catch (ParseException e) {
         e.printStackTrace();
      }*/

      if (ExpiryDate.length()==19)
      ExpiryDate = expiryDate;
      ExpiryDate = expiryDate;
   }

/*   public String getExpirydate() {
      expirydate = "i am get";
      return expirydate;
   }

   public void setExpirydate(String expirydate) {
      expirydate = "i am set";

      this.expirydate = expirydate;
   }*/

   public DrivingLicenseFront DrivingLicenseFront = new DrivingLicenseFront();
   public DrivingLicenseBack DrivingLicenseBack= new DrivingLicenseBack();
}
