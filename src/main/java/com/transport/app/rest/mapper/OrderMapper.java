package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderCarrier;
import com.transport.app.rest.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toOrder(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .brokerOrderId(orderDto.getBrokerOrderId())
                .enclosedTrailer(orderDto.getEnclosedTrailer())
                .m22Inspection(orderDto.getM22Inspection())
                .pickupContactName(orderDto.getPickupContactName())
                .pickupCompanyName(orderDto.getPickupCompanyName())
                .pickupAddress(orderDto.getPickupAddress())
                .pickupAddressState(orderDto.getPickupAddressState())
                .pickupZip(orderDto.getPickupZip())
                .pickupLatitude(orderDto.getPickupLatitude())
                .pickupLongitude(orderDto.getPickupLongitude())
                .pickupPhones(orderDto.getPickupPhones())
                .pickupSignatureNotRequired(orderDto.getPickupSignatureNotRequired())

                .pickupDates(orderDto.getPickupDates())
                .preferredPickupDate(orderDto.getPreferredPickupDate())
                .committedPickupDate(orderDto.getCommittedPickupDate())

//                .pickupStartDate(orderDto.getPickupStartDate())
//                .pickupEndDate(orderDto.getPickupEndDate())
                .pickupDatesRestrictions(orderDto.getPickupDatesRestrictions())
                .deliveryContactName(orderDto.getDeliveryContactName())
                .deliveryCompanyName(orderDto.getDeliveryCompanyName())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .deliveryAddressState(orderDto.getDeliveryAddressState())
                .deliveryZip(orderDto.getDeliveryZip())
                .deliveryLatitude(orderDto.getDeliveryLatitude())
                .deliveryLongitude(orderDto.getDeliveryLongitude())
                .deliveryPhones(orderDto.getDeliveryPhones())
                .deliverySignatureNotRequired(orderDto.getDeliverySignatureNotRequired())

                .deliveryDates(orderDto.getDeliveryDates())
                .preferredDeliveryDate(orderDto.getPreferredDeliveryDate())
                .committedDeliveryDate(orderDto.getCommittedDeliveryDate())

//                .deliveryStartDate(orderDto.getDeliveryStartDate())
//                .deliveryEndDate(orderDto.getDeliveryEndDate())
                .deliveryDatesRestrictions(orderDto.getDeliveryDatesRestrictions())
                .vehicleYear(orderDto.getVehicleYear())
                .vehicleMake(orderDto.getVehicleMake())
                .vehicleModel(orderDto.getVehicleModel())
                .vehicleAutoType(orderDto.getVehicleAutoType())
                .vehicleColor(orderDto.getVehicleColor())
                .vehicleVIN(orderDto.getVehicleVIN())
                .vehicleLOTNumber(orderDto.getVehicleLOTNumber())
                .vehicleBuyerId(orderDto.getVehicleBuyerId())
                .vehicleInoperable(orderDto.getVehicleInoperable())
                .dispatchInstructions(orderDto.getDispatchInstructions())
                .carrierPay(orderDto.getCarrierPay())
                .daysToPay(orderDto.getDaysToPay())
                .amountOnPickup(orderDto.getAmountOnPickup())
                .paymentOnPickupMethod(orderDto.getPaymentOnPickupMethod())
                .amountOnDelivery(orderDto.getAmountOnDelivery())
                .paymentOnDeliveryMethod(orderDto.getPaymentOnDeliveryMethod())

                .paymentTermBusinessDays(orderDto.getPaymentTermBusinessDays())
                .paymentMethod(orderDto.getPaymentMethod())
                .paymentTermBegins(orderDto.getPaymentTermBegins())
                .paymentNotes(orderDto.getPaymentNotes())

                .brokerContactName(orderDto.getBrokerContactName())
                .brokerCompanyName(orderDto.getBrokerCompanyName())
                .brokerAddress(orderDto.getBrokerAddress())
                .brokerZip(orderDto.getBrokerZip())
                .brokerLatitude(orderDto.getBrokerLatitude())
                .brokerLongitude(orderDto.getBrokerLongitude())
                .shipperPhones(orderDto.getShipperPhones())
                .brokerEmail(orderDto.getBrokerEmail())

                .orderStatus(orderDto.getOrderStatus())
                .orderCategory(orderDto.getOrderCategory())
                .bookingRequestCarriers(OrderCarrierMapper.toOrderCarriers(orderDto.getBookingRequestCarriers()))
                .bookedCarriers(OrderCarrierMapper.toOrderCarriers(orderDto.getBookedCarriers()))
//                .assignedToCarrier(UserMapper.toUser(orderDto.getAssignedToCarrier()))
//                .assignedToDriver(UserMapper.toUser(orderDto.getAssignedToDriver()))
//                .createdBy(UserMapper.toUser(orderDto.getCreatedBy()))
                .distance(orderDto.getDistance())
                .perMile(orderDto.getPerMile())
                .radiusPickupDistance(orderDto.getRadiusPickupDistance())
                .radiusDeliveryDistance(orderDto.getRadiusDeliveryDistance())

                .termsAndConditions(orderDto.getTermsAndConditions())

                .createdAt(orderDto.getCreatedAt())
                .updatedAt(orderDto.getUpdatedAt())
                .build();
    }

    public static OrderDto toOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDto.builder()
                .id(order.getId())
                .brokerOrderId(order.getBrokerOrderId())
                .enclosedTrailer(order.getEnclosedTrailer())
                .m22Inspection(order.getM22Inspection())
                .pickupContactName(order.getPickupContactName())
                .pickupCompanyName(order.getPickupCompanyName())
                .pickupAddress(order.getPickupAddress())
                .pickupAddressState(order.getPickupAddressState())
                .pickupZip(order.getPickupZip())
                .pickupLatitude(order.getPickupLatitude())
                .pickupLongitude(order.getPickupLongitude())
                .pickupPhones(order.getPickupPhones())
                .pickupSignatureNotRequired(order.getPickupSignatureNotRequired())

                .pickupDates(order.getPickupDates())
                .preferredPickupDate(order.getPreferredPickupDate())
                .committedPickupDate(order.getCommittedPickupDate())
//                .pickupStartDate(order.getPickupStartDate())
//                .pickupEndDate(order.getPickupEndDate())
                .pickupDatesRestrictions(order.getPickupDatesRestrictions())
                .deliveryContactName(order.getDeliveryContactName())
                .deliveryCompanyName(order.getDeliveryCompanyName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryAddressState(order.getDeliveryAddressState())
                .deliveryZip(order.getDeliveryZip())
                .deliveryLatitude(order.getDeliveryLatitude())
                .deliveryLongitude(order.getDeliveryLongitude())
                .deliveryPhones(order.getDeliveryPhones())
                .deliverySignatureNotRequired(order.getDeliverySignatureNotRequired())

                .deliveryDates(order.getDeliveryDates())
                .preferredDeliveryDate(order.getPreferredDeliveryDate())
                .committedDeliveryDate(order.getCommittedDeliveryDate())

//                .deliveryStartDate(order.getDeliveryStartDate())
//                .deliveryEndDate(order.getDeliveryEndDate())
                .deliveryDatesRestrictions(order.getDeliveryDatesRestrictions())
                .vehicleYear(order.getVehicleYear())
                .vehicleMake(order.getVehicleMake())
                .vehicleModel(order.getVehicleModel())
                .vehicleAutoType(order.getVehicleAutoType())
                .vehicleColor(order.getVehicleColor())
                .vehicleVIN(order.getVehicleVIN())
                .vehicleLOTNumber(order.getVehicleLOTNumber())
                .vehicleBuyerId(order.getVehicleBuyerId())
                .vehicleInoperable(order.getVehicleInoperable())
                .dispatchInstructions(order.getDispatchInstructions())
                .carrierPay(order.getCarrierPay())
                .daysToPay(order.getDaysToPay())
                .amountOnPickup(order.getAmountOnPickup())
                .paymentOnPickupMethod(order.getPaymentOnPickupMethod())
                .amountOnDelivery(order.getAmountOnDelivery())
                .paymentOnDeliveryMethod(order.getPaymentOnDeliveryMethod())

                .paymentTermBusinessDays(order.getPaymentTermBusinessDays())
                .paymentMethod(order.getPaymentMethod())
                .paymentTermBegins(order.getPaymentTermBegins())
                .paymentNotes(order.getPaymentNotes())

                .brokerContactName(order.getBrokerContactName())
                .brokerCompanyName(order.getBrokerCompanyName())
                .brokerAddress(order.getBrokerAddress())
                .brokerZip(order.getBrokerZip())
                .brokerLatitude(order.getBrokerLatitude())
                .brokerLongitude(order.getBrokerLongitude())
                .shipperPhones(order.getShipperPhones())
                .brokerEmail(order.getBrokerEmail())

                .orderStatus(order.getOrderStatus())
                .orderCategory(order.getOrderCategory())
                .bookingRequestCarriers(OrderCarrierMapper.toOrderCarrierDtos(order.getBookingRequestCarriers()))
                .bookedCarriers(OrderCarrierMapper.toOrderCarrierDtos(order.getBookedCarriers()))
//                .assignedToCarrierId(order.getAssignedToCarrier() != null ? order.getAssignedToCarrier().getId() : null)
//                .assignedToDriverId(order.getAssignedToDriver() != null ? order.getAssignedToDriver().getId() : null)
                .assignedToCarrier(UserMapper.toUserDto(order.getAssignedToCarrier()))
                .assignedToDriver(UserMapper.toUserDto(order.getAssignedToDriver()))
                .distance(order.getDistance())
                .perMile(order.getPerMile())
                .radiusPickupDistance(order.getRadiusPickupDistance())
                .radiusDeliveryDistance(order.getRadiusDeliveryDistance())

                .termsAndConditions(order.getTermsAndConditions())

//                .askedToBook(order.getAskedToBook())
                .createdBy(UserMapper.toUserDto(order.getCreatedBy()))
//                .updatedById(order.getUpdatedBy().getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static Order toUpdatedOrder(Order order, Order orderUpdate) {
        order.setBrokerOrderId(orderUpdate.getBrokerOrderId() == null ? order.getBrokerOrderId() : orderUpdate.getBrokerOrderId());
        order.setEnclosedTrailer(orderUpdate.getEnclosedTrailer() == null ? order.getEnclosedTrailer() : orderUpdate.getEnclosedTrailer());
        order.setM22Inspection(orderUpdate.getM22Inspection() == null ? order.getM22Inspection() : orderUpdate.getM22Inspection());
        order.setPickupContactName(orderUpdate.getPickupContactName() == null ? order.getPickupContactName() : orderUpdate.getPickupContactName());
        order.setPickupCompanyName(orderUpdate.getPickupCompanyName() == null ? order.getPickupCompanyName() : orderUpdate.getPickupCompanyName());
        order.setPickupAddress(orderUpdate.getPickupAddress() == null ? order.getPickupAddress() : orderUpdate.getPickupAddress());
        order.setPickupAddressState(orderUpdate.getPickupAddressState() == null ? order.getPickupAddressState() : orderUpdate.getPickupAddressState());
        order.setPickupZip(orderUpdate.getPickupZip() == null ? order.getPickupZip() : orderUpdate.getPickupZip());
        order.setPickupLatitude(orderUpdate.getPickupLatitude() == null ? order.getPickupLatitude() : orderUpdate.getPickupLatitude());
        order.setPickupLongitude(orderUpdate.getPickupLongitude() == null ? order.getPickupLongitude() : orderUpdate.getPickupLongitude());
        order.setPickupPhones(orderUpdate.getPickupPhones() == null ? order.getPickupPhones() : orderUpdate.getPickupPhones());
        order.setPickupSignatureNotRequired(orderUpdate.getPickupSignatureNotRequired() == null ? order.getPickupSignatureNotRequired() : orderUpdate.getPickupSignatureNotRequired());
        order.setPickupDates(orderUpdate.getPickupDates() == null ? order.getPickupDates() : orderUpdate.getPickupDates());
        order.setPreferredPickupDate(orderUpdate.getPreferredPickupDate() == null ? order.getPreferredPickupDate() : orderUpdate.getPreferredPickupDate());
        order.setCommittedPickupDate(orderUpdate.getCommittedPickupDate() == null ? order.getCommittedPickupDate() : orderUpdate.getCommittedPickupDate());
//        order.setPickupStartDate(orderUpdate.getPickupStartDate() == null ? order.getPickupStartDate() : orderUpdate.getPickupStartDate());
//        order.setPickupEndDate(orderUpdate.getPickupEndDate() == null ? order.getPickupEndDate() : orderUpdate.getPickupEndDate());
        order.setPickupDatesRestrictions(orderUpdate.getPickupDatesRestrictions() == null ? order.getPickupDatesRestrictions() : orderUpdate.getPickupDatesRestrictions());
        order.setDeliveryContactName(orderUpdate.getDeliveryContactName() == null ? order.getDeliveryContactName() : orderUpdate.getDeliveryContactName());
        order.setDeliveryCompanyName(orderUpdate.getDeliveryCompanyName() == null ? order.getDeliveryCompanyName() : orderUpdate.getDeliveryCompanyName());
        order.setDeliveryAddress(orderUpdate.getDeliveryAddress() == null ? order.getDeliveryAddress() : orderUpdate.getDeliveryAddress());
        order.setDeliveryAddressState(orderUpdate.getDeliveryAddressState() == null ? order.getDeliveryAddressState() : orderUpdate.getDeliveryAddressState());
        order.setDeliveryZip(orderUpdate.getDeliveryZip() == null ? order.getDeliveryZip() : orderUpdate.getDeliveryZip());
        order.setDeliveryLatitude(orderUpdate.getDeliveryLatitude() == null ? order.getDeliveryLatitude() : orderUpdate.getDeliveryLatitude());
        order.setDeliveryLongitude(orderUpdate.getDeliveryLongitude() == null ? order.getDeliveryLongitude() : orderUpdate.getDeliveryLongitude());
        order.setDeliveryPhones(orderUpdate.getDeliveryPhones() == null ? order.getDeliveryPhones() : orderUpdate.getDeliveryPhones());
        order.setDeliverySignatureNotRequired(orderUpdate.getDeliverySignatureNotRequired() == null ? order.getDeliverySignatureNotRequired() : orderUpdate.getDeliverySignatureNotRequired());
        order.setDeliveryDates(orderUpdate.getDeliveryDates() == null ? order.getDeliveryDates() : orderUpdate.getDeliveryDates());
        order.setPreferredDeliveryDate(orderUpdate.getPreferredDeliveryDate() == null ? order.getPreferredDeliveryDate() : orderUpdate.getPreferredDeliveryDate());
        order.setCommittedDeliveryDate(orderUpdate.getCommittedDeliveryDate() == null ? order.getCommittedDeliveryDate() : orderUpdate.getCommittedDeliveryDate());
//        order.setDeliveryStartDate(orderUpdate.getDeliveryStartDate() == null ? order.getDeliveryStartDate() : orderUpdate.getDeliveryStartDate());
//        order.setDeliveryEndDate(orderUpdate.getDeliveryEndDate() == null ? order.getDeliveryEndDate() : orderUpdate.getDeliveryEndDate());
        order.setDeliveryDatesRestrictions(orderUpdate.getDeliveryDatesRestrictions() == null ? order.getDeliveryDatesRestrictions() : orderUpdate.getDeliveryDatesRestrictions());
        order.setVehicleYear(orderUpdate.getVehicleYear() == null ? order.getVehicleYear() : orderUpdate.getVehicleYear());
        order.setVehicleMake(orderUpdate.getVehicleMake() == null ? order.getVehicleMake() : orderUpdate.getVehicleMake());
        order.setVehicleModel(orderUpdate.getVehicleModel() == null ? order.getVehicleModel() : orderUpdate.getVehicleModel());
        order.setVehicleAutoType(orderUpdate.getVehicleAutoType() == null ? order.getVehicleAutoType() : orderUpdate.getVehicleAutoType());
        order.setVehicleColor(orderUpdate.getVehicleColor() == null ? order.getVehicleColor() : orderUpdate.getVehicleColor());
        order.setVehicleVIN(orderUpdate.getVehicleVIN() == null ? order.getVehicleVIN() : orderUpdate.getVehicleVIN());
        order.setVehicleLOTNumber(orderUpdate.getVehicleLOTNumber() == null ? order.getVehicleLOTNumber() : orderUpdate.getVehicleLOTNumber());
        order.setVehicleBuyerId(orderUpdate.getVehicleBuyerId() == null ? order.getVehicleBuyerId() : orderUpdate.getVehicleBuyerId());
        order.setVehicleInoperable(orderUpdate.getVehicleInoperable() == null ? order.getVehicleInoperable() : orderUpdate.getVehicleInoperable());
        order.setDispatchInstructions(orderUpdate.getDispatchInstructions() == null ? order.getDispatchInstructions() : orderUpdate.getDispatchInstructions());
        order.setCarrierPay(orderUpdate.getCarrierPay() == null ? order.getCarrierPay() : orderUpdate.getCarrierPay());
        order.setDaysToPay(orderUpdate.getDaysToPay() == null ? order.getDaysToPay() : orderUpdate.getDaysToPay());
        order.setAmountOnPickup(orderUpdate.getAmountOnPickup() == null ? order.getAmountOnPickup() : orderUpdate.getAmountOnPickup());
        order.setPaymentOnPickupMethod(orderUpdate.getPaymentOnPickupMethod() == null ? order.getPaymentOnPickupMethod() : orderUpdate.getPaymentOnPickupMethod());
        order.setAmountOnDelivery(orderUpdate.getAmountOnDelivery() == null ? order.getAmountOnDelivery() : orderUpdate.getAmountOnDelivery());
        order.setPaymentOnDeliveryMethod(orderUpdate.getPaymentOnDeliveryMethod() == null ? order.getPaymentOnDeliveryMethod() : orderUpdate.getPaymentOnDeliveryMethod());
        order.setBrokerContactName(orderUpdate.getBrokerContactName() == null ? order.getBrokerContactName() : orderUpdate.getBrokerContactName());
        order.setBrokerCompanyName(orderUpdate.getBrokerCompanyName() == null ? order.getBrokerCompanyName() : orderUpdate.getBrokerCompanyName());
        order.setBrokerAddress(orderUpdate.getBrokerAddress() == null ? order.getBrokerAddress() : orderUpdate.getBrokerAddress());
        order.setBrokerZip(orderUpdate.getBrokerZip() == null ? order.getBrokerZip() : orderUpdate.getBrokerZip());
        order.setBrokerLatitude(orderUpdate.getBrokerLatitude() == null ? order.getBrokerLatitude() : orderUpdate.getBrokerLatitude());
        order.setBrokerLongitude(orderUpdate.getBrokerLongitude() == null ? order.getBrokerLongitude() : orderUpdate.getBrokerLongitude());
        order.setShipperPhones(orderUpdate.getShipperPhones() == null ? order.getShipperPhones() : orderUpdate.getShipperPhones());
        order.setBrokerEmail(orderUpdate.getBrokerEmail() == null ? order.getBrokerEmail() : orderUpdate.getBrokerEmail());

        order.setOrderStatus(orderUpdate.getOrderStatus() == null ? order.getOrderStatus() : orderUpdate.getOrderStatus());
        order.setOrderCategory(orderUpdate.getOrderCategory() == null ? order.getOrderCategory() : orderUpdate.getOrderCategory());
        order.setBookingRequestCarriers(orderUpdate.getBookingRequestCarriers() == null ? order.getBookingRequestCarriers() : orderUpdate.getBookingRequestCarriers());
        order.setBookedCarriers(orderUpdate.getBookedCarriers() == null ? order.getBookedCarriers() : orderUpdate.getBookedCarriers());
        order.setAssignedToCarrier(orderUpdate.getAssignedToCarrier() == null ? order.getAssignedToCarrier() : orderUpdate.getAssignedToCarrier());
        order.setAssignedToDriver(orderUpdate.getAssignedToDriver() == null ? order.getAssignedToDriver() : orderUpdate.getAssignedToDriver());
        order.setDistance(orderUpdate.getDistance() == null ? order.getDistance() : orderUpdate.getDistance());
        order.setPerMile(orderUpdate.getPerMile() == null ? order.getPerMile() : orderUpdate.getPerMile());
        order.setRadiusPickupDistance(orderUpdate.getRadiusPickupDistance() == null ? order.getRadiusPickupDistance() : orderUpdate.getRadiusPickupDistance());
        order.setRadiusDeliveryDistance(orderUpdate.getRadiusDeliveryDistance() == null ? order.getRadiusDeliveryDistance() : orderUpdate.getRadiusDeliveryDistance());
        order.setTermsAndConditions(orderUpdate.getTermsAndConditions() == null ? order.getTermsAndConditions() : orderUpdate.getTermsAndConditions());
//        order.setAskedToBook(orderUpdate.getAskedToBook() == null ? order.getAskedToBook() : orderUpdate.getAskedToBook());
        order.setCreatedBy(orderUpdate.getCreatedBy() == null ? order.getCreatedBy() : orderUpdate.getCreatedBy());
        order.setCreatedAt(orderUpdate.getCreatedAt() == null ? order.getCreatedAt() : orderUpdate.getCreatedAt());
        order.setUpdatedAt(orderUpdate.getUpdatedAt() == null ? order.getUpdatedAt() : orderUpdate.getUpdatedAt());
        return order;
    }

    public static List<Order> toOrders(List<OrderDto> orderDtos) {
        return orderDtos.stream().map(o -> toOrder(o)).collect(Collectors.toList());
    }

    public static List<OrderDto> toOrderDtos(List<Order> orders) {
        return orders.stream().map(o -> toOrderDto(o)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        OrderDto orderDto = OrderDto.builder().pickupContactName("Name 1").build();
        List<Order> orders = toOrders(Arrays.asList(orderDto));
        System.out.println(orders.size());
        Order order = Order.builder().pickupContactName("Name 1").build();
        List<OrderDto> orderDtos = toOrderDtos(Arrays.asList(order));
        System.out.println(orderDtos.size());
    }
}
