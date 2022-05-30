package com.rentguruz.app.model.parameter;

import android.util.Log;

import com.google.gson.JsonArray;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.model.base.BaseModel;
import com.rentguruz.app.model.base.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.rentguruz.app.apicall.ApiEndPoint.TableType;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLECLASS;

public class CommonParams {
    public static int companyid = Helper.di;
    private int limit = 10,pageSize =10;
    private String orderDir ="desc";
   // private List<Integer> ints = Arrays.asList(10, 20, 30, 40, 50);

    //List<Integer> ints = new ArrayList<>();
    private JSONArray ints = new JSONArray();
    private JSONObject common(){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", "[10, 20, 30, 40, 50]");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }
    private JSONObject common(Boolean value){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    private JSONObject common(int offset,int count,String search){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", "[10, 20, 30, 40, 50]");

            if (offset>0){
                object.accumulate("offset", offset);
            }

            if (count>0){
                object.accumulate("count", count);
            }

            if (!search.trim().isEmpty()){
                object.accumulate("filter", search);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }


    public JSONObject payment(int customerid){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
        object = common();
        filter.accumulate("CustomerId", customerid);
        object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject statmentvbyvehicle(int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            object.accumulate("orderBy","CreatedDate");
          //  filter.accumulate("CustomerId", companyid);
            filter.accumulate("VehicleId",id);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getReservationById (int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
     /*       filter.accumulate("CompanyId", 1);
            filter.accumulate("CustomerId", 16022);
            filter.accumulate("IsActive", true);*/
            filter.accumulate("id", id);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getDrivingLicenseList(int customerId){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CustomerId", customerId);
            filter.accumulate("IsActive", true);
            filter.accumulate("GetWithDrivingLicence", true);
            object.accumulate("filterObj", filter);
            Log.d("Mungara", "getDrivingLicenseList: " + filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getCreditCardList(int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CreditCardType", 3);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CreditCardFor", id);
            filter.accumulate("IsActive", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getVechicleList(int pickuplocation, String checkout, String checkindate){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        data.put(1);
        data.put(2);
        data.put(3);
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
           // filter.accumulate("VehicleTypeId", 0);
           // filter.accumulate("fVehicleTypeIds", data);
            filter.accumulate("GetVehicleForReservation", true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("CurrentLocation", pickuplocation);
            filter.accumulate("Status", 1);
            filter.accumulate("GetWithDefaultImg", true);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Mungara", "getVechicleList: " + object.toString());
        return object;
    }

    public JSONObject getVehicleType(int pickuplocation, String checkout, String checkindate,JSONArray fIds){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        try
        {
            object = common();

            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("GetForReservation", true);
            filter.accumulate("IsGetStandaredImage", true);
            filter.accumulate("APIRequestType",4);
            filter.accumulate("TableType",VEHICLECLASS);
            filter.accumulate("fIds",fIds);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }
    public JSONObject getVehicleType(int pickuplocation, String checkout, String checkindate){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        try
        {
            object = common();

            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("GetForReservation", true);
            filter.accumulate("IsGetStandaredImage", true);
            filter.accumulate("APIRequestType",4);
            filter.accumulate("TableType",VEHICLECLASS);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getVehicleType(int pickuplocation, String checkout, String checkindate,JSONArray fIds,String character){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        JSONArray ints = new JSONArray();
        try
        {
            //object = common();
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("offset", 0);
            object.accumulate("filter",character);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("GetForReservation", true);
            filter.accumulate("IsGetStandaredImage", true);
            filter.accumulate("APIRequestType",4);
            filter.accumulate("fIds",fIds);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getVechicleList(int pickuplocation, String checkout, String checkindate, int VehicleType){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        data.put(1);
        data.put(2);
        data.put(3);
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
           // filter.accumulate("VehicleTypeId", 0);
           // filter.accumulate("fVehicleTypeIds", data);
            filter.accumulate("GetVehicleForReservation", true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("CurrentLocation", pickuplocation);
            filter.accumulate("VehicleTypeId",VehicleType);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("Status", 1);
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Mungara", "getVechicleList: " + object.toString());
        return object;
    }

    public JSONObject getVechicleList(int pickuplocation, String checkout, String checkindate, int VehicleType, int reservationid){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray data = new JSONArray();
        data.put(1);
        data.put(2);
        data.put(3);
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("Id", reservationid);
           // filter.accumulate("VehicleTypeId", 0);
           // filter.accumulate("fVehicleTypeIds", data);
            filter.accumulate("GetVehicleForReservation", true);
            filter.accumulate("GetAvailableVehicleOnly",true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("CurrentLocation", pickuplocation);
            filter.accumulate("VehicleTypeId",VehicleType);
            reservationModel.accumulate("CheckOutDate", checkout);
            reservationModel.accumulate("CheckInDate", checkindate);
            reservationModel.accumulate("PickUpLocation", pickuplocation);
            filter.accumulate("Status", 1);
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Mungara", "getVechicleList: " + object.toString());
        return object;
    }

    public JSONObject getInsuranceCover(int id, int time, int locationId){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject ReservationSummaryDetailModel = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActiveDetail",true);
            filter.accumulate("VehicleTypeId",id);
            filter.accumulate("IsActive", true);
            filter.accumulate("LocationId",locationId);
            ReservationSummaryDetailModel.accumulate("TotalTime",time);
            filter.accumulate("ReservationSummaryDetailModel", ReservationSummaryDetailModel);
            object.accumulate("filterObj", filter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getInventory(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject ReservationSummaryDetailModel = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsActiveDetail",true);
            ReservationSummaryDetailModel.accumulate("TotalTime",3);
            ReservationSummaryDetailModel.accumulate("ReservationSummaryDetailType",10);
            filter.accumulate("ReservationSummaryDetailModel", ReservationSummaryDetailModel);
            object.accumulate("filterObj", filter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getEquipment(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getMisc(int pickupLoc, int VehicleTypeId){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
       // JSONObject ReservationSummaryDetailModel = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsActiveDetail",true);
            filter.accumulate("LocationId",pickupLoc );
            filter.accumulate("VehicleTypeId",VehicleTypeId);
            object.accumulate("filterObj", filter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Mungara", "getMisc: " +object);
        return object;
    }

    public JSONObject getRate(int RateId, int VehicleTypeId, int LocationId){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("RateId", RateId);
            filter.accumulate("VehicleTypeId", VehicleTypeId);
            filter.accumulate("LocationId", LocationId);
            filter.accumulate("GetRateFeature", true);
            object.accumulate("filterObj", filter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getRate(int RateId, int VehicleTypeId, int LocationId, String checkout, String checkin){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
//            object = common();

            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits",ints);

            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("RateId", RateId);
            filter.accumulate("VehicleTypeId", VehicleTypeId);
            filter.accumulate("LocationId", LocationId);
            filter.accumulate("GetRateFeature", true);
            filter.accumulate("GetRateForReservation", true);
            filter.accumulate("CheckInDate",checkin);
            filter.accumulate("CheckOutDate",checkout);
            object.accumulate("filterObj", filter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getAvailableLocation(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
          //  object = common();
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("pageLimits", "[10, 20, 30, 40, 50]");
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getAvailableLocation(JSONArray jsonArray){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("fIds", jsonArray);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getReservationList(int userFor){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CustomerId", userFor);
            filter.accumulate("IsActive", true);
           // filter.accumulate("IsGetReservation", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getReservationList(int userFor,Boolean onrent){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CustomerId", userFor);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsGetAgreement", false);
            filter.accumulate("IsGetReservation", true);
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("IsGetStandaredImage",true);
            if (onrent) {
                filter.accumulate("ReservationStatus", ReservationStatuss.CheckOut.inte);
            }
            // filter.accumulate("IsGetReservation", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getsingle(int userFor,int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CustomerId", userFor);
            filter.accumulate("IsActive", true);
            filter.accumulate("Id",id);
//            filter.accumulate("IsGetAgreement", false);
//            filter.accumulate("IsGetReservation", true);
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("IsGetStandaredImage",true);
            // filter.accumulate("IsGetReservation", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getAggrementList(int userFor){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("CustomerId", userFor);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsGetAgreement", true);
            filter.accumulate("IsGetReservation", false);
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("IsGetStandaredImage",true);
            /*filter.accumulate("ReservationStatus",5);*/
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getInsuranceCompanyList(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getDelete(int tabletype, int id){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("TableType", tabletype);
            object.accumulate("Id", id);
            object.accumulate("IsActive", false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getCheckListAccessories(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsTrackAtCheckOutOrIn", true);
            JSONArray array = new JSONArray();
            //reservation status
            array.put(1);
            array.put(3);
            array.put(6);
            filter.accumulate("fAppliedIn", array);
            //, CheckList.Booking , CheckList.Checkout);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getCheckListAccessories(int vehicleid, int vehicletypeid){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("WithoutTrackAtCheckOutIn", true);
            JSONArray array = new JSONArray();
            //reservation status
            array.put(1);
            array.put(3);
            array.put(6);
            filter.accumulate("fAppliedIn", array);
            filter.accumulate("VehicleId", vehicleid);
            filter.accumulate("VehicleTypeId", vehicletypeid);
            //, CheckList.Booking , CheckList.Checkout);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }
    public JSONObject getCheckListAccessories(int vehicleid, int vehicletypeid, boolean value){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("WithoutTrackAtCheckOutIn", true);
            JSONArray array = new JSONArray();
            //reservation status
            array.put(1);
            array.put(3);
            array.put(6);
            array.put(5);
            filter.accumulate("fAppliedIn", array);
            filter.accumulate("VehicleId", vehicleid);
            filter.accumulate("VehicleTypeId", vehicletypeid);
            //, CheckList.Booking , CheckList.Checkout);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return object;
    }

    public JSONObject getCheckOutODmeter(int reservationid, String vechicleid){
        //JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
          //  object = common();
        /*    filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);*/
            filter.accumulate("ReservationId", reservationid);
            filter.accumulate("VehicleId", Integer.valueOf(vechicleid));
            //object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return filter;
    }


    public JSONObject getAggrementList(Boolean IsGetAgreement, Boolean IsGetReservation){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
           /* filter.accumulate("IsGetAgreement", IsGetAgreement);
            filter.accumulate("IsGetReservation", IsGetReservation);*/
            filter.accumulate("IsGetStandaredImage",true);
            filter.accumulate("GetWithDefaultImg", true);
            /*filter.accumulate("ReservationStatus",5);*/
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getAggrementList(Boolean IsGetAgreement, Boolean IsGetReservation,int id, int i){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
           /* filter.accumulate("IsGetAgreement", IsGetAgreement);
            filter.accumulate("IsGetReservation", IsGetReservation);*/
            filter.accumulate("ReservationStatus", id);
            filter.accumulate("IsGetStandaredImage",true);
            filter.accumulate("GetWithDefaultImg", true);
            /*filter.accumulate("ReservationStatus",5);*/
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getAggrementList(Boolean IsGetAgreement, Boolean IsGetReservation,int offset,int count,String search){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common(offset,count,search);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            //filter.accumulate("IsGetAgreement", IsGetAgreement);
            //filter.accumulate("IsGetReservation", IsGetReservation);
            /*filter.accumulate("ReservationStatus",5);*/
            filter.accumulate("IsGetStandaredImage",true);
            filter.accumulate("GetWithDefaultImg", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getAggrementList(Boolean IsGetAgreement, Boolean IsGetReservation,int Vehicle){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
           /* filter.accumulate("IsGetAgreement", IsGetAgreement);
            filter.accumulate("IsGetReservation", IsGetReservation);*/
            filter.accumulate("VehicleId", Vehicle);
            filter.accumulate("IsGetStandaredImage",true);
            filter.accumulate("GetWithDefaultImg", true);
            /*filter.accumulate("ReservationStatus",5);*/
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }


    public JSONObject getVehicleList(){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("GetWithDefaultImg", true);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getVehicleList(int i){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("offset", i);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("APIRequestType", 2);
            reservationModel.accumulate("CheckOutDate", Helper.setPostDate(DateType.defaultdate, new Date().toString()));
            reservationModel.accumulate("CheckInDate",Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() + 1000*60*60*24 ).toString()));
            /*reservationModel.accumulate("PickUpLocation", 8);*/
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("GetVehicleForReservation",false);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getVehicleList(int i, int status, int vehicletype){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("offset", i);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("Status",status);
            filter.accumulate("VehicleTypeId",vehicletype);
            reservationModel.accumulate("CheckOutDate", Helper.setPostDate(DateType.defaultdate, new Date().toString()));
            reservationModel.accumulate("CheckInDate",Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() + 1000*60*60*24 ).toString()));
            /*reservationModel.accumulate("PickUpLocation", 8);*/
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("GetVehicleForReservation",false);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }

    public JSONObject getVehicleList(int i, int status, int vehicletype,Boolean value,Boolean checking){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("offset", i);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("APIRequestType", 2);
            filter.accumulate("Status",status);
            filter.accumulate("VehicleTypeId",vehicletype);
            if (value){
                filter.accumulate("fEndDate", Helper.setPostDate(DateType.defaultdate, new Date().toString()));
                filter.accumulate("fStartDate",Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() - 1000*60*60*24*10).toString()));
            }
            if (checking){
                filter.accumulate("IsUnderCheckIn",true);
            }
            reservationModel.accumulate("CheckOutDate", Helper.setPostDate(DateType.defaultdate, new Date().toString()));
            reservationModel.accumulate("CheckInDate",Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() + 1000*60*60*24 ).toString()));
            /*reservationModel.accumulate("PickUpLocation", 8);*/
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("GetVehicleForReservation",false);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }


    public JSONObject getVehicleList(int i, String charater){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject reservationModel = new JSONObject();
        JSONArray ints = new JSONArray();
        try
        {
            object.accumulate("limit", 20);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 20);
            object.accumulate("offset", i);
            object.accumulate("filter",charater);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            object.accumulate("pageLimits", ints);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("APIRequestType", 2);
            reservationModel.accumulate("CheckOutDate", Helper.setPostDate(DateType.defaultdate, new Date().toString()));
            reservationModel.accumulate("CheckInDate",Helper.setPostDate(DateType.defaultdate, new Date( new Date().getTime() + 1000*60*60*24 ).toString()));
            /*reservationModel.accumulate("PickUpLocation", pickuplocation);*/
            filter.accumulate("GetWithDefaultImg", true);
            filter.accumulate("ReservationModel", reservationModel);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("TAG", "getAggrementList: " + object );
        return object;
    }


    public JSONObject getCustomerList(int i){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            object.accumulate("offset", i);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
          //  filter.accumulate("IsDoNotRent",true);
            //filter.accumulate("APIRequestType", 2);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getCustomerList(int i, String data){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            object.accumulate("offset", i);
            object.accumulate("filter",data);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
          //  filter.accumulate("IsDoNotRent",true);
            //filter.accumulate("APIRequestType", 2);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getBusinessSource(int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("Id", id);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }


    public JSONObject getAdditionalDriver(int locationId, int vehicletypeid){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("IsActiveDetail", true);
            filter.accumulate("isShowNoData", false);
            filter.accumulate("ChargeType", 1);
            filter.accumulate("LocationId", locationId);
            filter.accumulate("VehicleTypeId", vehicletypeid);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject gettermscondition(int locationId, int type, int DocumentType){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object.accumulate("pageSize",1);
            object.accumulate("limit",1);
            object.accumulate("offset",1);
            filter.accumulate("CompanyId", companyid);
            filter.accumulate("IsActive", true);
            filter.accumulate("LocationId", locationId);
            filter.accumulate("Type", type);
            filter.accumulate("DocumentType", DocumentType);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getReport(int reportid, int id){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("ReportType",reportid);
            object.accumulate("Id",id);
            object.accumulate("RenderFormat","PDF");
            object.accumulate("FilterBy",1);
            object.accumulate("IsExternalReport",false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getBasicDetail(String data){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId",companyid);
            filter.accumulate("ContentType",data);
            filter.accumulate("Language","en");
            filter.accumulate("isShowNoData",false);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }
    public JSONObject getBasicDetail(String data, int company){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        try
        {
            object = common();
            filter.accumulate("CompanyId",company);
            filter.accumulate("ContentType",data);
            filter.accumulate("Language","en");
            filter.accumulate("isShowNoData",false);
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject getemail(int id, String email){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("CompanyId",companyid);
            object.accumulate("EmailType",36);
            object.accumulate("EmailFor",id);
            object.accumulate("EmailTo",email);
            object.accumulate("Subject","ABEL");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject readyforcheckout(int id){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("Id",id);
            object.accumulate("TypeOf",2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject reservationstatusupdate(int id, int reservation){
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate("Id",id);
            object.accumulate("CompanyId",companyid);
            object.accumulate("ReservationStatus",reservation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject gettimeline(int id){
        JSONObject object = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONArray ints = new JSONArray();

        try
        {
            object = common(true);
            ints.put(10);
            ints.put(20);
            ints.put(30);
            ints.put(40);
            ints.put(50);
            //object.accumulate("pageLimits",ints);
           // filter.accumulate("CompanyId",companyid);
            filter.accumulate("Id",id);
            filter.accumulate("fStartDate", "2022-01-01T00:00:00");
            filter.accumulate("fEndDate", Helper.setPostDate(DateType.defaultdate, new Date().toString())+"T00:00:00");
            object.accumulate("filterObj", filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public HashMap<String, String> getCheckoutImage(int id){
        HashMap<String, String> header = new HashMap<>();
        header.put("FileUploadMasterId",String.valueOf(id));
        header.put("Id", String.valueOf(id));
        header.put("fileUploadType", "17");
        return header;
    }

    public HashMap<String, String> getCheckoutSign(int id, String image){
        HashMap<String, String> header = new HashMap<>();
        header.put("FileUploadMasterId",String.valueOf(id));
        header.put("Id", String.valueOf(id));
        header.put("fileUploadType", "19");
        header.put("img64", image);
        header.put("IsActive","true");
        header.put("Latitude","48.5753");
        header.put("Longitude", "7.5546");
        header.put("ReservationId","174");
        header.put("SignType", "1");
        header.put("SignedBy","1622");

        return header;
    }
    public HashMap<String, String> getUserImage(int id){
        HashMap<String, String> header = new HashMap<>();
        header.put("Id", String.valueOf(id));
        header.put("fileUploadType", "1");
        header.put("CompanyId", String.valueOf(companyid));
        return header;
    }
}
