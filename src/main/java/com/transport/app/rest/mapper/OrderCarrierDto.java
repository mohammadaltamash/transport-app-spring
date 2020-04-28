package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class OrderCarrierDto {

    private Long id;
    private Long orderId;
    private Long carrierId;
    private String status;
    private Double carrierPay;
    private String daysToPay;
    private String paymentTermBegins;
    private Date committedPickupDate;
    private Date committedDeliveryDate;
    private String offerReason;
    private String offerValidity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
