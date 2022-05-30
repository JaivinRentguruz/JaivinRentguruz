package com.rentguruz.app.model.base;

import com.rentguruz.app.model.AttachmentsModel;
import com.rentguruz.app.model.Customer;
import com.rentguruz.app.model.ReservationCheckin;
import com.rentguruz.app.model.ReservationCheckout;
import com.rentguruz.app.model.companyModel;
import com.rentguruz.app.model.CreditCardModel;
import com.rentguruz.app.model.InsuranceCompanyDetailsModel;
import com.rentguruz.app.model.InsuranceModel;
import com.rentguruz.app.model.display.ThemeColors;
import com.rentguruz.app.model.reservation.ReservationEquipment;
import com.rentguruz.app.model.response.CustomerProfile;
import com.rentguruz.app.model.response.LoginResponse;
import com.rentguruz.app.model.response.UpdateDL;
import com.rentguruz.app.model.ReservationBusinessSource;

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
    public static ReservationCheckin reservationCheckin = new ReservationCheckin();
    public static ReservationBusinessSource reservationBusinessSource = new ReservationBusinessSource();
    public static String billingdetail;
    public static ReservationEquipment[] equipment;
    public static CreditCardModel activepmt = new CreditCardModel();
    public static ThemeColors UiColor = new ThemeColors();
}
