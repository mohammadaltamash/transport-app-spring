package com.transport.app.rest.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Audited(withModifiedFlag = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @NotEmpty(message = "brokerOrderId is required")
    @Column(name = "BROKER_ORDER_ID"
//            , nullable = false
//            , unique = true
    )
    private String brokerOrderId;                                               // Broker Order ID              required
    @Column(name = "ENCLOSED_TRAILER")
    private Boolean enclosedTrailer;                                            // Enclosed trailer
    @Column(name = "M22_INSPECTION")
    private Boolean m22Inspection;                                              // M-22 inspection

    // Pickup Contact & Location
    @Column(name = "PICKUP_CONTACT_NAME")
    private String pickupContactName;                                           // Contact name
    @Column(name = "PICKUP_COMPANY_NAME")
    private String pickupCompanyName;                                           // Company name
//    @NotEmpty(message = "pickupAddress is required")
    @Column(name = "PICKUP_ADDRESS"
//            , nullable = false
    )
    private String pickupAddress;                                               // Pickup address               required
    @Column(name = "PICKUP_ADDRESS_STATE")
    private String pickupAddressState;
//    @NotEmpty(message = "pickupZip is required")
    @Column(name = "PICKUP_ZIP"
//            , nullable = false
    )
    private String pickupZip;                                                   // Zip                          required
//    @Column(name = "PICKUP_LATITUDE", precision = 20, columnDefinition = "DECIMAL(20,4)")
    @Column(name = "PICKUP_LATITUDE")
    private Double pickupLatitude;
    @Column(name = "PICKUP_LONGITUDE")
    private Double pickupLongitude;
    /*Phone 1 (can be multiple) required,
    Phone 1 notes optional*/
//    @NotEmpty(message = "pickupPhones is required")
    @Column(name = "PICKUP_PHONES"
//            , nullable = false
    )
    @ElementCollection
    private Map<String, String> pickupPhones;                                   // Phone 1 (can be multiple)    required
    @Column(name = "PICKUP_SIGNATURE_NOT_REQUIRED")
    private Boolean pickupSignatureNotRequired;                                       // Signature not required
    //    @Temporal(TemporalType.TIMESTAMP)
//    @NotEmpty(message = "pickupDates is required")
    @Column(name = "PICKUP_DATES"
//            , nullable = false
    )
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private Map<String, Date> pickupDates;                                             // Pickup dates                 required
    @Column(name = "PREFERRED_PICKUP_DATE")
    private Date preferredPickupDate;
    @Column(name = "COMMITTED_PICKUP_DATE")
    private Date committedPickupDate;
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
//    private Date pickupStartDate;                                               // Pickup start date            required
//    //    @NotEmpty(message = "pickupDates is required")
//    @Column(name = "PICKUP_END_DATE"
////            , nullable = false
//    )
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
//    private Date pickupEndDate;                                                 // Pickup end date              required
    @Column(name = "PICKUP_DATES_RESTRICTIONS")
    private String pickupDatesRestrictions;                                     // Pickup dates restrictions

    // Delivery Contact & Location
    @Column(name = "DELIVERY_CONTACT_NAME")
    private String deliveryContactName;                                         // Contact name
    @Column(name = "DELIVERY_COMPANY_NAME")
    private String deliveryCompanyName;                                         // Company name
//    @NotEmpty(message = "deliveryAddress is required")
    @Column(name = "DELIVERY_ADDRESS"
//            , nullable = false
    )
    private String deliveryAddress;                                             // Delivery address             required
    @Column(name = "DELIVERY_ADDRESS_STATE")
    private String deliveryAddressState;
//    @NotEmpty(message = "deliveryZip is required")
    @Column(name = "DELIVERY_ZIP"
//            , nullable = false
    )
    private String deliveryZip;                                               // Zip                          required
    @Column(name = "DELIVERY_LATITUDE")
    private Double deliveryLatitude;
    @Column(name = "DELIVERY_LONGITUDE")
    private Double deliveryLongitude;
    /*Phone 1 (can be multiple) required,
    Phone 1 notes optional*/
//    @NotEmpty(message = "deliveryPhones is required")
    @Column(name = "DELIVERY_PHONES"
//            , nullable = false
    )
    @ElementCollection
    private Map<String, String> deliveryPhones;                                 // Phone 1 (can be multiple)    required
    @Column(name = "DELIVERY_SIGNATURE_NOT_REQUIRED")
    private Boolean deliverySignatureNotRequired;                               // Signature not required

    @Column(name = "DELIVERY_DATES"
//            , nullable = false
    )
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private Map<String, Date> deliveryDates;                                           // Delivery dates               required
    @Column(name = "PREFERRED_DELIVERY_DATE")
    private Date preferredDeliveryDate;
    @Column(name = "COMMITTED_DELIVERY_DATE")
    private Date committedDeliveryDate;
    //////////////


//    @NotEmpty(message = "deliveryDates is required")
//    @Column(name = "DELIVERY_START_DATE"
////            , nullable = false
//    )
    //    @ElementCollection
////    private List<Date> deliveryDates;                                           // Delivery dates               required
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
//    private Date deliveryStartDate;                                             // Delivery start date          required
//    @Column(name = "DELIVERY_END_DATE"
////            , nullable = false
//    )
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
//    private Date deliveryEndDate;                                               // Delivery end date            required
    @Column(name = "DELIVERY_DATES_RESTRICTIONS")
    private String deliveryDatesRestrictions;                                   // Delivery dates restrictions

    // Add New Vehicle
    @Column(name = "VEHICLE_YEAR")
    private Integer vehicleYear;                                                    // Year
//    @NotEmpty(message = "vehicleMake is required")
    @Column(name = "VEHICLE_MAKE"
//            , nullable = false
    )
    private String vehicleMake;                                                 // Make                         required
    @Column(name = "VEHICLE_MODEL")
    private String vehicleModel;                                                // Model
    //    @NotEmpty(message = "vehicleAutoType is required")
    @Column(name = "VEHICLE_AUTO_TYPE")
    private String vehicleAutoType;                                             // Autotype                     required
    @Column(name = "VEHICLE_COLOR")
    private String vehicleColor;                                                // Color
    @Column(name = "VEHICLE_VIN")
    private String vehicleVIN;                                                  // VIN
    @Column(name = "VEHICLE_LOT_NUMBER")
    private String vehicleLOTNumber;                                            // LOT number
    @Column(name = "VEHICLE_BUYER_ID")
    private String vehicleBuyerId;                                              // Buyer ID
    @Column(name = "VEHICLE_INOPERABLE")
    private Boolean vehicleInoperable;                                          // Inoperable

    // Dispatch Information
    @Column(name = "DISPATCH_INSTRUCTIONS")
    private String dispatchInstructions;                                        // Dispatch Instructions

    // Pricing Information
//    @NotNull(message = "carrierPay is required")
    @Column(name = "CARRIER_PAY"
//            , nullable = false
    )
    private Double carrierPay;                                                  // Carrier pay                  required
    @Column(name = "AMOUNT_ON_PICKUP")
    private Double amountOnPickup;                                              // Amount on pickup
    @Column(name = "PAYMENT_ON_PICKUP_METHOD")
    private String paymentOnPickupMethod;                                       // Payment on pickup method
    @Column(name = "AMOUNT_ON_DELIVERY")
    private Double amountOnDelivery;                                            // Amount on delivery
    @Column(name = "PAYMENT_ON_DELIVERY_METHOD")
    private String paymentOnDeliveryMethod;                                     // Payment on delivery method
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

    // Shipper Information
    @Column(name = "BROKER_CONTACT_NAME")
    private String brokerContactName;                                           // Broker contact name
//    @NotEmpty(message = "brokerCompanyName is required")
    @Column(name = "BROKER_COMPANY_NAME"
//            , nullable = false
    )
    private String brokerCompanyName;                                           // Broker company name          required
//    @NotEmpty(message = "brokerAddress is required")
    @Column(name = "BROKER_ADDRESS"
//            , nullable = false
    )
    private String brokerAddress;                                               // Broker address               required
//    @NotEmpty(message = "brokerZip is required")
    @Column(name = "BROKER_ZIP"
//            , nullable = false
    )
    private String brokerZip;                                                   // Zip                          required
    @Column(name = "BROKER_LATITUDE")
    private Double brokerLatitude;
    @Column(name = "BROKER_LONGITUDE")
    private Double brokerLongitude;
    /*Phone 1 (can be multiple) required,
    Phone 1 notes optional*/
//    @NotEmpty(message = "shipperPhones is required")
    @Column(name = "SHIPPER_PHONES"
//            , nullable = false
    )
    @ElementCollection
    private Map<String, String> shipperPhones;                                  // Phone 1 (can be multiple)    required
//    @Email(message = "BROKER_EMAIL is invalid")
//    @NotEmpty(message = "brokerEmail is required")
    @Column(name = "BROKER_EMAIL"
//            , nullable = false
    )
    private String brokerEmail;                                                 // Broker email                 required

    @Column(name = "ORDER_STATUS")
    @Builder.Default
    private String orderStatus = OrderStatus.NEW.getName();
    @Column(name = "ORDER_CATEGORY")
    private String orderCategory;

    @ManyToOne
//            (fetch = FetchType.LAZY)
    @JoinColumn(name="CREATED_BY_ID")
    @NotAudited
    private User createdBy;
    @Column(name = "CREATED_BY_NAME")
    private String createdByName; // For search

    @Column(name = "BOOKING_REQUEST_CARRIERS")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderCarrier> bookingRequestCarriers; // by carriers (to assign order to carrier)
//    @Column(name = "ASSIGNED_TO_CARRIER")
//    @NotAudited
    @ManyToOne
    @JoinColumn(name="ASSIGNED_TO_CARRIER_ID")
    private User assignedToCarrier; // By broker to carrier. orderStatus changes to ASSIGNED
    @Column(name = "ASSIGNED_TO_CARRIER_NAME")
    private String assignedToCarrierName; // For search
//    @Column(name = "ASSIGNED_TO_DRIVER")
    @ManyToOne
    @JoinColumn(name="ASSIGNED_TO_DRIVER_ID")
    private User assignedToDriver; // By carrier
    @Column(name = "ASSIGNED_TO_DRIVER_NAME")
    private String assignedToDriverName; // For search
    @Column(name = "DISTANCE")
    private Long distance;
    @Transient
    private Double radiusPickupDistance;
    @Transient
    private Double radiusDeliveryDistance;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ORDER_CATEGORY {
        LOGISTICS
    }
}
