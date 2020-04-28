package com.transport.app.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_CARRIERS")
public class OrderCarrier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ORDER_ID")
    @JsonIgnore
    private Order order;
    @ManyToOne
    @JoinColumn(name="CARRIER_ID")
    @JsonIgnore
    private User carrier;
    @Column(name = "STATUS")
    private String status;

    @Column(name = "CARRIER_PAY")
    private Double carrierPay;
    @Column(name = "DAYS_TO_PAY")
    private String daysToPay;
    @Column(name = "PAYMENT_TERM_BEGINS")
    private String paymentTermBegins;
    @Column(name = "COMMITTED_PICKUP_DATE")
    private Date committedPickupDate;
    @Column(name = "COMMITTED_DELIVERY_DATE")
    private Date committedDeliveryDate;
    @Column(name = "OFFER_REASON")
    private String offerReason;
    @Column(name = "OFFER_VALIDITY")
    private String offerValidity;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
