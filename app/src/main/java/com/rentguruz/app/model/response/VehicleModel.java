package com.rentguruz.app.model.response;

import android.widget.Toast;

import com.rentguruz.app.model.base.BaseModel;
import com.rentguruz.app.model.vehicle.VehicleOtherDetailsModel;
import com.rentguruz.app.model.vehicle.VehiclePurchaseDetailsModel;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class VehicleModel extends BaseModel implements Serializable {
    public String VehicleShortName,DefaultImagePath,VehicleName,VehDescription;
    public int Id,NoOfSeats,NoOfBags,NoOfDoors,Year,Status;
    public Double TotalAmount,PerDayAmount;
    //public Boolean ;
    public int RateId,SecurityDeposit;

    public int VehicleId,VehicleTypeId,CurrentOdo;

    public String TransmissionDesc,FuelTypeDesc;

    //public ReserversationSummary ReservationSummaryDetailModel = new ReserversationSummary();

  /*  public DecimalFormat df = new DecimalFormat("#.00");
    public String _total = df.format(String.valueOf(TotalAmount));*/

    public String ParkedLocation;
    public String VehicleNumber,VinNumber,LicenseNumber;

    public String MakeName,Manufacturer,ModelName,VehicleClass,EngineName,VehicleCategory;

    public String KeyCode,Number,TankValue,FuelUnit;

    public Double TankSize;

    public int TotalRecord;

    public ArrayList<AttachmentsModel> AttachmentsModels = new ArrayList<>();

    public VehiclePurchaseDetailsModel VehiclePurchaseDetailsModel = new VehiclePurchaseDetailsModel();

    public VehicleOtherDetailsModel VehicleOtherDetailsModel = new VehicleOtherDetailsModel();

    public String OwnerCompanyName,OwnerEmail,OwnerMobileNo,OwnerName,OwnerShipName;
            //,OwningLocation;


    public int dvalue = 0;

    public Boolean IsOnline,IsTemSuspend;
}
