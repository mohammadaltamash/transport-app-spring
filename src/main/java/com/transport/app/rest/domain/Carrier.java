package com.transport.app.rest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARRIERS")
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Pickup Contact & Location
    @Column(name = "CONTACT_NAME")
    private String contactName;                                           // Contact name
    @Column(name = "COMPANY_NAME")
    private String companyName;                                           // Company name
//    @NotEmpty(message = "pickupAddress is required")
    @Column(name = "ADDRESS"
//            , nullable = false
    )
    private String address;                                               // Pickup address               required
//    @NotEmpty(message = "pickupZip is required")
    @Column(name = "ZIP"
//            , nullable = false
    )
    private String zip;                                                   // Zip                          required
//    @Column(name = "PICKUP_LATITUDE", precision = 20, columnDefinition = "DECIMAL(20,4)")
    @Column(name = "LATITUDE")
    private Double latitude;
    @Column(name = "LONGITUDE")
    private Double longitude;
    /*Phone 1 (can be multiple) required,
    Phone 1 notes optional*/
//    @NotEmpty(message = "pickupPhones is required")
    @Column(name = "PHONES"
//            , nullable = false
    )
    @ElementCollection
    private Map<String, String> phones;                                   // Phone 1 (can be multiple)    required

    private String email;                                                 // Broker email                 required
}
