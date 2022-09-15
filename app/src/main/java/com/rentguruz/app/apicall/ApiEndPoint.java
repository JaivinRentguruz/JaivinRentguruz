package com.rentguruz.app.apicall;

import android.graphics.Bitmap;

public class ApiEndPoint {
    // public static final String demo = "demoapi.";
    public static final String demo = "api.";
    // ApiEndPoint
    public static final String BASE_URL_LOGIN = "https://" + demo + "rentguruz.online/api/";
    public static final String BASE_URL_BOOKING = "https://" + demo + "rentguruz.online/api/";
    public static final String BASE_URL_PAYMENT = "https://" + demo + ".rentguruz.online/api/";
    //public static final String BASE_URL_CUSTOMER = "https://api.flexiicar.com/api/Customer/";
    public static final String BASE_URL_CUSTOMER = "https://" + demo + "rentguruz.online/api/";
    public static final String BASE_URL_SETTINGS = "https://" + demo + "rentguruz.online/api/";
    public static final String BASE_URL_CHECKOUT = "https://" + demo + "rentguruz.online/api/";
    public static final String BASE_URL_CHECKIN = "https://" + demo + "rentguruz.online/api/";



    public static final String BASE_URL_HUFKEY = "https://" + demo + "rentguruz.online/api/HUFKeyAccessLog/";

    /*public static final String BASE_URL_LOGIN = "https://api.rentguruz.online/";
    public static final String BASE_URL_BOOKING = "https://api.rentguruz.online/";
    public static final String BASE_URL_PAYMENT = "https://api.rentguruz.online/";
    //public static final String BASE_URL_CUSTOMER = "https://api.flexiicar.com/api/Customer/";
    public static final String BASE_URL_CUSTOMER = "https://api.rentguruz.online/";
    public static final String BASE_URL_SETTINGS = "https://api.rentguruz.online/";
    public static final String BASE_URL_CHECKOUT = "https://api.rentguruz.online/";
    public static final String BASE_URL_CHECKIN= "https://api.rentguruz.online/";

    public static final String BASE_URL_HUFKEY="https://api.rentguruz.online/";*/
    public static final String GET_MOBILEKEY_DETAILS = "GetMobileKeyDetail";

    public static final String UPLOAD_BASE_URL = "";

    public static final String UPLOADIMAGE = "Attachment/Upload";
    public static final String GETIMAGE = "attachment/GetAttachments";
    public static final String UPLOADSIGNATURE = "ReservationSignature/Insert";
    public static final String GETSIGNATURE = "ReservationSignature/GetAll";
    //Reservation
    public static final String RESERVATIONINSERT = "Reservation/Insert";
    public static final String RESERVATIONUPDATE = "Reservation/Update";
    public static final String RESERVATIONDATECHANGE= "Reservation/DateChange";
    public static final String RESERVATIONVEHICLECHANGE="Reservation/VehicleChange";
    public static final String RESERVATIONVALIDATION = "Reservation/IsValidReservation";

    public static final String CUSTOMERLIST = "Customer/GetAll";
    //testing
    public static final String TESTING = "RateMaster/GetById?id=";

    public static final String COMPANYINSERT = "Company/QuickInsert";

    public static final String SMSSENT = "Common/SendSMSOtp";
    public static final String SMSVERIFY = "Common/VerifySMSOtp";

    //rentguru common
    public static final String COUNTRYLIST = "common/GetCountry";
    public static final String STATELIST = "common/GetState";

    public static final String RANDUMNUMBER = "Common/GenerateRandomString?length=8&prefix=8";
    public static final String RANDUMNUMBERS = "Common/GenerateRandomString";

    public static final String BusinessSourceMasterALL = "BusinessSourceMaster/GetAll";
    public static final String BusinessSourceMaster = "BusinessSourceMaster/GetSingle";
    public static final String REPORT = "Report/GetReport";

    public static final String VALIDPICKUP  = "Reservation/IsValidProcessPickup";

    //insert
    public static final String INSERT = "customer/QuickInsert";

    //login
    public static final String LOGIN = "User/Login";
    public static final String SPLASH = "CMSManagement/GetCMSData";

    //add card
    public static final String ADDCARD = "CreditCard/Insert";
    public static final String GETALLCARD = "CreditCard/GetAll";
    public static final String GETBYIDCARD = "CreditCard/GetById";
    public static final String UPDATECARD = "CreditCard/Update";
    //  public static final String DELETE = "Common/ActiveDeActive";

    //reservation status
    public static final String RESERVATIONSTATUS = "Reservation/GetTotalRsvInStatus";
    public static final String RESERVATIONSTATUSUPDATE ="Reservation/UpdateStatus";
    public static final String RESERVATIONCHECKOUT = "Reservation/GetDocuments";

    //customer
    public static final String GETCUSTOMER = "Customer/GetById";
    public static final String GETCUSTOMERDETAIL = "Customer/GetSingle";
    public static final String CUSTOMERUPDATE = "Customer/Update";
    public static final String CHANGEPASSWORD = "User/ChangePasswordUsingOldPassword";

    //vendor
    public static final String VENDORGET = "VendorMaster/GetById";

    //Licence
    public static final String GETLICENSE = "DriverMaster/GetById";
    public static final String GETLICENSEALL = "DriverMaster/GetAll";
    public static final String INSERTLICENCE = "DriverMaster/Insert";
    public static final String GETSINGLELICENCE = "DriverMaster/GetById";
    public static final String UPDATELICENCE = "DriverMaster/Update";
    public static final String UPDATEDEFAULTDRIVER = "DriverMaster/UpdateDrivingLicence";

    //reservation access pmt
    public static final String TIMECALCULATE = "Reservation/CalculateTime";

    // preference
    public static final String PREF = "NexPort";

    //location
    public static final String AVAILABLELOCATION = "Location/GetAll";

    public static final String TOLLCHARGE  = "TollCharge/Insert";
    public static final String TRAFFICE = "TrafficTicket/Insert";
    //vehicle
    public static final String AVAILABLEVICHICLE = "Vehicle/GetAll";
    public static final String AVAILABLEVEHICLETYPE = "VehicleType/GetAll";
    public static final String VEHICLEDETAIL  = "Vehicle/GetVinDetail";
    //delete
    public static final String DELETE = "Common/ActiveDeActive";

    public static final String CHECKOUTVALIDATION = "Reservation/IsValidProcessPickup";

    //company
    public static final String GETCOMPANY = "Company/GetById";

    //insurance
    public static final String INSURANCECOMPANYLIST = "InsuranceCompany/GetAll";
    public static final String ADDINSURANCEDETAIL = "InsuranceDetails/Insert";
    public static final String GETINSURANCE = "InsuranceDetails/GetInsurance";
    public static final String UPDATEINSURANCE = "InsuranceDetails/Update";
    public static final String INSURANCECOVER = "InsuranceCoverMaster/GetAll";

    public static final String TERMSCONDITION = "TermsAndConditions/GetSingle";

    public static final int TableType = 67, Id = 5957;
    public static final Boolean IsActive = false;

    ///api/Company/AppInitiation
    public static final String INITIATION = "Company/AppInitiation";

    //get timeline
    public static final String USERTIMELINE = "Customer/GetTimeLine";
    public static final String VEHICLETIMELINE = "Vehicle/GetTimeLine";

    //reservation
    public static final String RESERVATIONGETALL = "Reservation/GetAll";
    public static final String RESERVATIONGETBYID = "Reservation/GetById";
    public static final String RESERVATIONGETSINGLE = "Reservation/GetSingle";

    //reservation payment
    public static final String PMTGETALL = "ReservationPayments/GetAll";

    //inventory
    public static final String INVENTORY = "InventoryMaster/GetAll";
    public static final String MISCCHARGES = "MiscellaneousCharge/GetAll";
    public static final String MISCCHARGESSINGLE = "MiscellaneousCharge/GetSingle";
    public static final String EQUIPMENT = "EquipmentMaster/GetAll";

    public static final String EQUIPMENTINSERT = "EquipmentMaster/Insert";

    //GetSummaryOfCharges total
    public static final String SUMMARYCHARGE = "Reservation/GetSummaryOfCharges";

    public static final String RESERVATIONCAL = "Reservation/CalculateTime";

    public static final String RATE = "RentalRateMaster/GetSingle";

    public static final String RESERVATIONPMT = "ReservationPayments/InsertList";

    public static final String CHECKLISTFORACCESORRIES = "FleetCheckList/GetAll";

    public static final String  FlEETCHECKLIST  = "FleetCheckList/GetAllForReservation";

    public static final String CHECKOUT = "Reservation/CheckOut";

    public static final String CHECKIN = "Reservation/GetCheckInData";

    public static final String CHECKINN = "Reservation/CheckIn";
    public static final String CHECKOUTODMETER = "Reservation/GetCheckOutData";
    public static final String CHECKINODMETER = "Reservation/GetCheckInData";
    public static final String READYFORCHECKOUT = "Reservation/updateTypeOf";

    //rentguru common
    public static final String COMMONDROPDOWN = "Common/GetDDLList";
    public static final String COMMONDROPDOWNSINGLE = "Common/GetDDL";


    //reservation chackout
    public static final String CHECKOUTTIME = "RsvReadyForCheckoutHours";

    //insert
    public static final String INSERTCUSTOMER = "customer/QuickInsert";
    public static final String INSERTVEHICLE = "Vehicle/QuickInsert";
    public static final String VEHICLEGETBYID = "Vehicle/GetById";
    public static final String VEHICLEUPDATE = "Vehicle/PartialUpdate";
    public static final String VEHICLEPURCHASEUPDATE = "Vehicle/Update";
    public static final String VEHICLEFILTER = "VehicleType/GetAllWithVehicleCount";

    public static final String EMAIL = "Reservation/SendBookingEmail";

    //delete
    public static final String IMAGEDELETE = "Attachment/DeleteAttachment";

    //http://api.rentguruz.com/api/login/
    //LOGIN
    public static final String LOGIN_VERIFICATION = "LoginVerification";
    public static final String APP_INTIALIZATION = "AppInitialization";
    public static final String REGISTRATION = "Registration";
    //public static final String CHANGEPASSWORD = "ChangePassword";

    //BOOKING
    public static final String LOCATION_LIST = "locationlist";
    public static final String BOOKING = "booking";
    public static final String LOCATION_SEARCH_LIST = "locationsearchlist";
    public static final String LOCATION_SEARCH_BY_DISTANCE = "LocationSearchByDistance";
    public static final String FILTERLIST = "filterlist";
    public static final String GETDEFAULTCREDITCARD = "GetDefaultCreditCard";
    public static final String GETDCREDITCARDLIST = "GetCreditCardList";
    public static final String GETTERMSCONDITION = "GetTermsCondition";
    public static final String GETPAYMENT = "PaymentProcess";
    public static final String UPDATECREDITCARD = "UpdateCreditCard";
    public static final String DELETECREDITCARD = "DeleteCreditCard";
    public static final String ADDCREDITCARD = "AddCreditCard";
    public static final String GETPICKUPLIST = "GetPickupList";
    public static final String GETDELIVERYLIST = "GetDeliveryList";
    public static final String CALCULATE_DISTANCE = "CalculatesDistance";
    public static final String CANCELBOOKING = "CancelBooking";
    //PAYMENT
    public static final String PAYMENTPROCESS = "PaymentProcess";

    //CUSTOMER
    public static final String GETCUSTOMERPROFILE = "CustomerProfile";
    public static final String UPDATECUSTOMERPROFILE = "UpdateCustomerProfile";
    public static final String GETCUSTOMERSUMMARY = "customerSummary";
    public static final String GETRESERVATIONLIST = "ReservationList";
    public static final String GETAGREEMENTLIST = "AgreementsList";
    public static final String GETCUSTOMERINSURANCE = "CustomerInsurance";
    public static final String UPDATECUSTOMERINSURANCE = "UpdateCustomerInsurance";
    public static final String DRIVINGLICENSE = "DrivingLicense";
    public static final String UPDATEDRIVINGLICENSE = "UpdateDrivingLicense";
    public static final String GETACCOUNTSTATEMENT = "AccountStatementList";

    public static final String ACTIVITYTIMELINELIST = "ActivityTimeLineList";
    public static final String ADDCUSTOMERACTIVITY = "AddCustomerActivity";
    public static final String UPDATECUSTOMERACTIVITY = "UpdateCustomerActivity";
    public static final String ACTIVITYTIMELINE = "ActivityTimeLine";

    public static final String FORGETPASSWORD = "User/ForgotPassword";
    public static final String ADDPROFILEPICTURE = "AddProfilePicture";
    public static final String REMOVEPROFILEPICTURE = "RemoveProfilePicture";

    public static final String ACCOUNTSTATEMENTSTATUSUPDATE = "AccountStatementStatusUpdate";

    //SETTINGS
    public static final String GETCOUNTRYLIST = "CountryList";
    // public static final String STATELIST="StateList";
    public static final String ACTIVITYTYPELIST = "ActivityTypeList";
    //  public static final String INSURANCECOMPANYLIST="InsuranceCompanyList";
    //CheckOut
    public static final String GETSELFCHECKOUT = "GetSelfCheckOut";
    public static final String UPDATESELFCHECKOUT = "UpdateSelfCheckOut";

    //CheckIn
    public static final String GETSELFCHECKIN = "GetSelfCheckIn";
    public static final String UPDATESELFCHECKIN = "UpdateSelfCheckIn";
    public static final String GETAGREEMENTREPORT = "GetAgreementReport";
    public static final String VERSION_API = "v1/";


    //Drop Down Vehicle
    public static final int MAKEVEHICLE = 22;
    public static final int VEHICLECLASS = 28;
    public static final int VEHICLELOCATION = 7;
    public static final int VEHICLEENGINE = 32;
    public static final int VEHICLEFEATURE = 24;
    public static final int VEHICLEMAKE = 23;
    public static final int VEHICLEFLEET = 18;
    public static final int BUSINESSBOOKINGTYPE = 71;

    public static final int TAXLIST = 55;

    public static final int CUSTOMERDDL = 3;
    public static final int INSURANCECOMPANY = 6;
    public static final int VENDOR = 76;

    public static final int VEHICLELIST = 29;
    public static final int CANCELAUTORISEDBY = 1;

    public static Bitmap firstImage = null;
    public static Bitmap secondImage = null;


}
