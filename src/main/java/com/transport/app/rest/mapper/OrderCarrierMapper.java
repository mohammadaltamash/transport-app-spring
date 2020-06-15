package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.OrderCarrier;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCarrierMapper {

    public static OrderCarrier toOrderCarrier(OrderCarrierDto orderCarrierDto) {
        return OrderCarrier.builder()
                .id(orderCarrierDto.getId())
//                .order(OrderMapper.toOrder(orderCarrierDto.getOrder()))
//                .carrier(UserMapper.toUser(orderCarrierDto.getCarrier()))
                .status(orderCarrierDto.getStatus())
                .carrierPay(orderCarrierDto.getCarrierPay())
                .daysToPay(orderCarrierDto.getDaysToPay())
                .paymentTermBegins(orderCarrierDto.getPaymentTermBegins())
                .committedPickupDate(orderCarrierDto.getCommittedPickupDate())
                .committedDeliveryDate(orderCarrierDto.getCommittedDeliveryDate())
                .offerReason(orderCarrierDto.getOfferReason())
                .offerValidity(orderCarrierDto.getOfferValidity())
                .termsAndConditions(orderCarrierDto.getTermsAndConditions())
                .createdAt(orderCarrierDto.getCreatedAt())
                .updatedAt(orderCarrierDto.getUpdatedAt())
                .build();
    }

    public static OrderCarrierDto toOrderCarrierDto(OrderCarrier orderCarrier) {
        if (orderCarrier == null) {
            return null;
        }
        return OrderCarrierDto.builder()
                .id(orderCarrier.getId())
                .orderId(orderCarrier.getOrder().getId())
                .bookedOrderId(orderCarrier.getBookedOrder() != null ? orderCarrier.getBookedOrder().getId() : null)
                .carrierId(orderCarrier.getCarrier().getId())
                .carrierFullName(orderCarrier.getCarrier().getFullName())
                .carrierEmail(orderCarrier.getCarrier().getEmail())
                .status(orderCarrier.getStatus())
                .carrierPay(orderCarrier.getCarrierPay())
                .daysToPay(orderCarrier.getDaysToPay())
                .paymentTermBegins(orderCarrier.getPaymentTermBegins())
                .committedPickupDate(orderCarrier.getCommittedPickupDate())
                .committedDeliveryDate(orderCarrier.getCommittedDeliveryDate())
                .offerReason(orderCarrier.getOfferReason())
                .offerValidity(orderCarrier.getOfferValidity())
                .termsAndConditions(orderCarrier.getTermsAndConditions())
                .createdAt(orderCarrier.getCreatedAt())
                .updatedAt(orderCarrier.getUpdatedAt())
                .build();
    }

    public static OrderCarrier toUpdatedOrderCarrier(OrderCarrier orderCarrier, OrderCarrier orderCarrierUpdate) {
        orderCarrier.setId(orderCarrierUpdate.getId() == null ? orderCarrier.getId() : orderCarrierUpdate.getId());
        orderCarrier.setOrder(orderCarrierUpdate.getOrder() == null ? orderCarrier.getOrder() : orderCarrierUpdate.getOrder());
        orderCarrier.setCarrier(orderCarrierUpdate.getCarrier() == null ? orderCarrier.getCarrier() : orderCarrierUpdate.getCarrier());
        orderCarrier.setStatus(orderCarrierUpdate.getStatus() == null ? orderCarrier.getStatus() : orderCarrierUpdate.getStatus());
        orderCarrier.setCarrierPay(orderCarrierUpdate.getCarrierPay() == null ? orderCarrier.getCarrierPay() : orderCarrierUpdate.getCarrierPay());
        orderCarrier.setDaysToPay(orderCarrierUpdate.getDaysToPay() == null ? orderCarrier.getDaysToPay() : orderCarrierUpdate.getDaysToPay());
        orderCarrier.setPaymentTermBegins(orderCarrierUpdate.getPaymentTermBegins() == null ? orderCarrier.getPaymentTermBegins() : orderCarrierUpdate.getPaymentTermBegins());
        orderCarrier.setCommittedPickupDate(orderCarrierUpdate.getCommittedPickupDate() == null ? orderCarrier.getCommittedPickupDate() : orderCarrierUpdate.getCommittedPickupDate());
        orderCarrier.setCommittedDeliveryDate(orderCarrierUpdate.getCommittedDeliveryDate() == null ? orderCarrier.getCommittedDeliveryDate() : orderCarrierUpdate.getCommittedDeliveryDate());
        orderCarrier.setOfferReason(orderCarrierUpdate.getOfferReason() == null ? orderCarrier.getOfferReason() : orderCarrierUpdate.getOfferReason());
        orderCarrier.setOfferValidity(orderCarrierUpdate.getOfferValidity() == null ? orderCarrier.getOfferValidity() : orderCarrierUpdate.getOfferValidity());
        orderCarrier.setTermsAndConditions(orderCarrierUpdate.getTermsAndConditions() == null ? orderCarrier.getTermsAndConditions() : orderCarrierUpdate.getTermsAndConditions());
        return orderCarrier;
    }

    public static List<OrderCarrier> toOrderCarriers(List<OrderCarrierDto> orderCarrierDtos) {
        return orderCarrierDtos.stream().map(c -> toOrderCarrier(c)).collect(Collectors.toList());
    }

    public static List<OrderCarrierDto> toOrderCarrierDtos(List<OrderCarrier> orderCarriers) {
        if (orderCarriers == null) {
            return null;
        }
        return orderCarriers.stream().map(c -> toOrderCarrierDto(c)).collect(Collectors.toList());
    }
}
