package com.transport.app.rest.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;
    @NotEmpty(message = "'password' is required")
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "RESET_TOKEN")
    private String resetToken;
    @Transient
    private String jwtToken;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ZIP")
    private String zip;
    @Column(name = "LATITUDE")
    private Double latitude;
    @Column(name = "LONGITUDE")
    private Double longitude;
    @Column(name = "PHONES")
    @ElementCollection
    private Map<String, String> phones;
    @NotEmpty(message = "'email' is required")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "TYPE")
    private String type;
//    @Column(name = "ORDERS")
    @OneToMany
            (mappedBy = "createdBy",
            cascade = CascadeType.ALL
//            orphanRemoval = true
            )
//    @JoinColumn(name="CREATED_BY_ID")
    private List<Order> orders;

    public enum Type {
        BROKER, CARRIER, DRIVER
    }
}
