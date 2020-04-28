package com.transport.app.rest.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Audited
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
    @Column(name = "CREATED_ORDERS")
    @OneToMany
            (mappedBy = "createdBy",
            cascade = CascadeType.ALL
//            orphanRemoval = true
            )
//    @JoinColumn(name="CREATED_BY_ID")
    private List<Order> createdOrders; // As broker

    @Column(name = "BOOKING_REQUEST_ORDERS")
    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL)
    private List<OrderCarrier> bookingRequestOrders; // As carrier
    @Column(name = "ASSIGNED_ORDERS_AS_CARRIER")
    @OneToMany(mappedBy = "assignedToCarrier", cascade = CascadeType.ALL)
    private List<Order> assignedOrdersAsCarrier; // As carrier
    @Column(name = "ASSIGNED_ORDERS_AS_DRIVER")
    @OneToMany(mappedBy = "assignedToDriver", cascade = CascadeType.ALL)
    private List<Order> assignedOrdersAsDriver; // As driver

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Type {
        BROKER, CARRIER, DRIVER
    }
}
