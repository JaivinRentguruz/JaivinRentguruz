package com.abel.app.b2b.model.base;

import com.abel.app.b2b.model.AttachmentsModel;
import com.abel.app.b2b.model.Customer;
import com.abel.app.b2b.model.ReservationCheckout;
import com.abel.app.b2b.model.companyModel;
import com.abel.app.b2b.model.CreditCardModel;
import com.abel.app.b2b.model.InsuranceCompanyDetailsModel;
import com.abel.app.b2b.model.InsuranceModel;
import com.abel.app.b2b.model.reservation.ReservationEquipment;
import com.abel.app.b2b.model.response.CustomerProfile;
import com.abel.app.b2b.model.response.LoginResponse;
import com.abel.app.b2b.model.response.UpdateDL;
import com.abel.app.b2b.model.ReservationBusinessSource;

public class UserData {
    public static String UserDetail;
    public static CustomerProfile customerProfile = new CustomerProfile();
    public static CreditCardModel UpdateCreditCard = new CreditCardModel();
    public static LoginResponse loginResponse = new LoginResponse();
    public static InsuranceCompanyDetailsModel insuranceCompanyDetailsModel = new InsuranceCompanyDetailsModel();
    public static InsuranceModel insuranceModel = new InsuranceModel();
    public static companyModel companyModel = new companyModel();
    public static UpdateDL updateDL = new UpdateDL();
    public static Customer customer = new Customer();
    public static ReservationCheckout reservationCheckout = new ReservationCheckout();
    public static ReservationBusinessSource reservationBusinessSource = new ReservationBusinessSource();
    public static String billingdetail;
    public static ReservationEquipment[] equipment;
}
