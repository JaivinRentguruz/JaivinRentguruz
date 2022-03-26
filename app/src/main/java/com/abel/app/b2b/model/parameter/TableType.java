package com.abel.app.b2b.model.parameter;

public enum TableType {

      //[Descriptions("User", typeof(Messages))]
    User (1),
       //     [Descriptions("Company", typeof(Messages))]
    Company(2),
         //   [Descriptions("Customer", typeof(Messages))]
    Customer(3),
          //  [Descriptions("CustomerCompany", typeof(Messages))]
    CustomerCompany(4),
           // [Descriptions("CustomerType", typeof(Messages))]
    CustomerType (5),
          //  [Descriptions("InsuranceCompany", typeof(Messages))]
    InsuranceCompanyDetails (6),
          //  [Descriptions("Location", typeof(Messages))]
    Location (7),
          //  [Descriptions("Rate", typeof(Messages))]
    RateMaster (8),
          //  [Descriptions("SpecialNote", typeof(Messages))]
    SpecialNotes (9),
          //  [Descriptions("Address", typeof(Messages))]
    Addresses (10),
          //  [Descriptions("DrivingLicense", typeof(Messages))]
    DrivingLicence (11),
          //  [Descriptions("Timeline", typeof(Messages))]
    Timeline (12),
           // [Descriptions("CorporateCompany", typeof(Messages))]
    CorporateCompany (13),
           // [Descriptions("Insurance", typeof(Messages))]
    InsuranceDetails (14),
            //[Descriptions("CustomerIdProof", typeof(Messages))]
    CustomerIdProof (15),
            //[Descriptions("Customer", typeof(Messages))]
    CustomerDetail (16),
            //[Descriptions("Referral", typeof(Messages))]
    ReferralMaster (17),
           // [Descriptions("VehicleCategory", typeof(Messages))]
    VehicleCategory (18),
          //  [Descriptions("VehicleCertificateandLicense", typeof(Messages))]
    VehicleCertificateAndLicense (19),
           // [Descriptions("VehicleLeasingCompany", typeof(Messages))]
    VehicleLeasingCompany (20),
           // [Descriptions("VehicleLocation", typeof(Messages))]
    VehicleLocation (21),
           // [Descriptions("VehicleMake", typeof(Messages))]
    VehicleMake (22),
           // [Descriptions("VehicleModel", typeof(Messages))]
    VehicleModel (23),
           // [Descriptions("VehicleOption", typeof(Messages))]
    VehicleOptions (24),
    VehicleOptionMapping (25),
           // [Descriptions("Vehicle", typeof(Messages))]
    VehicleOtherDetails (26),
           // [Descriptions("VehicleTrack", typeof(Messages))]
    VehicleTrack (27),
           // [Descriptions("VehicleType", typeof(Messages))]
    VehicleType (28),
           // [Descriptions("Vehicle", typeof(Messages))]
    Vehicle (29),
           // [Descriptions("Status", typeof(Messages))]
    Status (30),
           // [Descriptions("Attachments", typeof(Messages))]
    Attachments (31),
           // [Descriptions("VehicleEngine", typeof(Messages))]
    VehicleEngine (32),
           // [Descriptions("Driver", typeof(Messages))]
    DriverMaster (33),
           // [Descriptions("Equipment", typeof(Messages))]
    EquipmentMaster (34),
           // [Descriptions("Inventory", typeof(Messages))]
    InventoryMaster (35),
           // [Descriptions("Reservation", typeof(Messages))]
    Reservation (36),
           // [Descriptions("ReservationVehicle", typeof(Messages))]
    ReservationVehicle (37),
           // [Descriptions("ReservationRate", typeof(Messages))]
    ReservationRates (38),


    ReservationInsurance (39),
    ReservationReferral(40),
    ReservationBillingInformation (41),
    ReservationEquipmentInventory (42),
    ReservationFlightAndHotel (43),
    ReservationNote (44),
    ReservationDrivers (45),
    ReservationDeliveryPickup (46),
    RentalRateMaster (47),
    DeliveryPickUpMaster (48),
    ReservationTypeMaster (49),
    BillingToEntityMaster (50),
    InsuranceCoverMasterDetails (51),
    InsuranceCoverMaster (52),
    ReservationDealershipDetails (53),
    DealershipMaster (54),
    TaxMaster (55),
    TaxMasterDetails (56),
    MiscellaneousCharge (57),
    ChauffeurMaster (58),
    RateFeatures (59),
    LocationStoreHours (60),
    TimeZone (61),
    ReservationClaim (62),
    ReservationPayments (63),
    PaymentFor (64),
    Emails (65),
    TextSMS (66),
    CreditCard (67),
    TermsAndConditions (68),
    AgreementTypeMaster (69),
    //PostOnWall ((70)
   /*         ,
    BusinessSourceMaster (71),
    BusinessSourceLocationMapping (72),
    BusinessSourceVehicleTypeMapping (73),
    ApiCallsCount (74),
    StoreHourProfileMaster (75),
    VendorMaster (76),
    CustomerActivity (77),
    CommissionSlabMaster (78),
    FleetOwner (79),
    VehicleDocument (80),
    FleetTrim (81),
    FleetSuspension (82),
    FleetCertification (83),
    StoreHourHolidayMaster (84),
    VehicleCertificateAndLicenseRenewal (85),
    VehicleExpense (86),
    VehicleOdometerTracking (87),
    FleetMaintenance (88),
    VehicleRevenueShare (89),
    VehiclePurchaseDetails (90),
    FleetCheckList (91),
    FleetInspectionRequest (92),
    FleetOwnersAccount (93),
    PromotionalCode (94),
    AgreementCharges (95),
    FleetCheckListHeader (97),
    ReservationToken (98),
    ReservationVehicleDamage (99),
    MiscellaneousChargeDetails (100),
    FleetOptionHeader (101),
    ReservationSignature (102),
    FleetInspectionDamage (103),
    ReservationSummaryCharges (104),
    ReservationCheckList (105),
    ReservationCheckIn (106),
    FleetCertificationLocationMapping (107),
    FleetMaintenanceLocationMapping (108),
    PromoCodeLocationMapping (109),
    VehicleCertificationLocationMapping (110),
    AgreementChargeLocationMapping (111),
    FleetCertificationVehicleTypeMapping (112),
    FleetCheckListVehicleTypeMap (113),
    FleetMaintenanceVehicleTypeMapping (114),
    PromoCodeVehicleTypeMapping (115),
    VehicleCertificationVehicleTypeMapping (116),
    FleetCheckListOwnerMap (117),
    FleetCheckListVehicleCategoryMap (118),
    EmailGateway (119),
    EmailTemplate (120),
    TemplateFields (121),
    EmailTemplateFieldsMapping (122),
    EmailScheduler (123),
    EmailSchedulerDaysMapping (124),
    EmailTemplateTrigger (125),
    SMSGateway (126),
    SMSTemplate (127),
    SMSTemplateFieldsMapping (128),
    CurrencyMaster (129),
    LanguageMaster (130),
    TrafficTicket (131),
    TrafficTicketDetail (132),
    TollCharge (133),
    TollChargeDetail (134),
    Expense (135),
    PaymentMaster (136),
    VehicleMaintenance (137),
    Claim (138),
    ProductAndServices (139),
    SuperAdminTaxDetails (140),
    SubscriptionInvoice (141),
    PaymentGateway (142),
    EmailSendScheduler (143),
    QuickBook (144),
    QuickBookAccounts (145),
    QuickBookSyncDetail (146),
    PreferenceMasterMapping (147),
    LabelMasterMapping (148),
    CustomerReviewMaster (149),
    CustomerDeposit (150),
    YieldMaster (151),
    YieldFormulation (152),
    ShiftMaster (153),
    SeasonalRate (154),
    KnowledgeBaseTransaction (155),
    UserPreferenceMapping (156),
    ReservationShift (157),
    DesignationMaster (158),
    DepartmentMaster (159),
    UserDetails (160),
    ReportEmailScheduler (161),
    ReportsEmail (162),
    SupportTicket (163),
    SupportTicketReplies (164),
    CustomerVerification (165);*/
  //  ;
            ;

            public int anInt;
     TableType(int i) {this.anInt = i;
    }

    @Override
    public String toString() {
        return String.valueOf(anInt);
    }


    public enum TimelineDescriptionTypes
    {
        Created (1),
        Updated (2),
        Deleted (3),
        LastLogin (4),
        MailSend (5),
        MailDraft (6);

        public int inte;
        TimelineDescriptionTypes(int i) {
            this.inte = i;
        }
        @Override
        public String toString() {
            return String.valueOf(inte);
        }

    }

    public enum UserTypes
    {
        SuperAdmin (1),
        Admin (2),
        User (3),
        Customer (4);

        public int anInt;
        UserTypes(int i) {
            this.anInt = i;
        }
        @Override
        public String toString() {
            return String.valueOf(anInt);
        }
    }
}
