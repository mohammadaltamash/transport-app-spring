package com.transport.app.rest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BROKERS")
public class Broker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Shipper Information
    @Column(name = "CONTACT_NAME")
    private String contactName;                                           // Broker contact name
//    @NotEmpty(message = "brokerCompanyName is required")
    @Column(name = "COMPANY_NAME"
//            , nullable = false
    )
    private String companyName;                                           // Broker company name          required
//    @NotEmpty(message = "brokerAddress is required")
    @Column(name = "ADDRESS"
//            , nullable = false
    )
    private String address;                                               // Broker address               required
//    @NotEmpty(message = "brokerZip is required")
    @Column(name = "ZIP"
//            , nullable = false
    )
    private String zip;                                                   // Zip                          required
    @Column(name = "LATITUDE")
    private Double latitude;
    @Column(name = "LONGITUDE")
    private Double longitude;
    /*Phone 1 (can be multiple) required,
    Phone 1 notes optional*/
//    @NotEmpty(message = "shipperPhones is required")
    @Column(name = "PHONES"
//            , nullable = false
    )
    @ElementCollection
    private Map<String, String> phones;                                  // Phone 1 (can be multiple)    required
//    @Email(message = "BROKER_EMAIL is invalid")
//    @NotEmpty(message = "brokerEmail is required")
    @Column(name = "EMAIL"
//            , nullable = false
    )
    private String email;                                                 // Broker email                 required
}
