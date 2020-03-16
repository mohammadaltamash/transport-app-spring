package com.transport.app.rest.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long id;
    //    Broker Order ID               required
    private String brokerOrderId;//    Broker Order ID               required
    //    Enclosed trailer
    private Boolean enclosedTrailer;//    Enclosed trailer
    //    M-22 inspection
    private Boolean m22Inspection;

    //    Pickup Contact & Location
//    Contact name
    private String pickupContactName;
    //    Company name
    private String pickupCompanyName;
    //    Pickup address                required
    private String pickupAddress;
    //    Zip                           required
    private String pickupZip;
    private Double pickupLatitude;
    private Double pickupLongitude;
    //    Phone 1 (can be multiple)     required
    /*private List<String> pickupPhones;*/
//    Phone 1 notes
    /*private List<String> pickupPhoneNotes;*/
    private Map<String, String> pickupPhones;
    //    Signature not required
    private Boolean pickupSignatureNotRequired;
    //    Pickup dates                  required
//    @Temporal(TemporalType.TIMESTAMP)
    private Map<String, Date> pickupDates;
//    private Date pickupStartDate;
//    private Date pickupEndDate;
    //    Pickup dates restrictions
    private String pickupDatesRestrictions;

    //    Delivery Contact & Location
//    Contact name
    private String deliveryContactName;
    //    Company name
    private String deliveryCompanyName;
    //    Delivery address              required
    private String deliveryAddress;
    //    Zip                           required
    private String deliveryZip;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    //    Phone 1 (can be multiple)     required
    /*private List<String> deliveryPhones;*/
//    Phone 1 notes
    /*private List<String> deliveryPhoneNotes;*/
    private Map<String, String> deliveryPhones;
    //    Signature not required
    private Boolean deliverySignatureNotRequired;
    //    Delivery dates                required
//    private List<Date> deliveryDates;
    private Map<String, Date> deliveryDates;
//    private Date deliveryStartDate;
//    private Date deliveryEndDate;
    //    Delivery dates restrictions
    private String deliveryDatesRestrictions;

    //    Add New Vehicle
//    Year
    private Integer vehicleYear;
    //    Make                          required
    private String vehicleMake;
    //    Model
    private String vehicleModel;
    //    Autotype
    private String vehicleAutoType;
    //    Color
    private String vehicleColor;
    //    VIN
    private String vehicleVIN;
    //    LOT number
    private String vehicleLOTNumber;
    //    Buyer ID
    private String vehicleBuyerId;
    //    Inoperable
    private Boolean vehicleInoperable;

    //    Dispatch Information
//    Dispatch Instructions
    private String dispatchInstructions;

    //    Pricing Information
//    Carrier pay                   required
    private Double carrierPay;
    //    Amount on pickup
    private Double amountOnPickup;
    //    Payment on pickup method
    private String paymentOnPickupMethod;
    //    Amount on delivery
    private Double amountOnDelivery;
    //    Payment on delivery method
    private String paymentOnDeliveryMethod;
    /////////////////////////////////////////////
    @Column(name = "PAYMENT_TERM_BUSINESS_DAYS")
    private String paymentTermBusinessDays;
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Column(name = "PAYMENT_TERM_BEGINS")
    private String paymentTermBegins;
    @Column(name = "PAYMENT_NOTES")
    private String paymentNotes;
    /////////////////////////////////////////////

    //    Shipper Information
//    Broker contact name
    private String brokerContactName;
    //    Broker company name           required
    private String brokerCompanyName;
    //    Broker address                required
    private String brokerAddress;
    //    Zip                           required
    private String brokerZip;
    private Double brokerLatitude;
    private Double brokerLongitude;
    //    Phone 1 (can be multiple)     required
    /*private List<String> shipperPhones;*/
//    Phone 1 notes
    /*private List<String> shipperPhoneNotes;*/
    private Map<String, String> shipperPhones;
    //    Broker email                  required
    private String brokerEmail;

    private String orderStatus;
    private String orderCategory;
    private String orderDriver;
}
