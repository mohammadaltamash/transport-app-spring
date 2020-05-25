package com.transport.app.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.app.rest.domain.*;
import com.transport.app.rest.mapper.OrderCarrierMapper;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    public void getActivities(Class clazz) {
        AuditReader reader = AuditReaderFactory.get(em);
        List<Number> revisions = reader.getRevisions(clazz, 2l);
        for (Number rev : revisions) {
            System.out.println(rev);
        }
    }

    public void getAllActivities(Class clazz) {
        AuditReader reader = AuditReaderFactory.get(em);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(clazz, false, true);
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Order order = (Order) result[0];
            CustomRevisionEntity revEntity = (CustomRevisionEntity) result[1];
            RevisionType revType = (RevisionType) result[2];

            System.out.println("Revision        :" + revEntity.getId());
            System.out.println("Revision Date   :" + revEntity.getRevisionDate());
            System.out.println("User            :" + revEntity.getUserName());
            System.out.println("Type            :" + revType);
            System.out.println("Order           :" + order.getId() + "      Order status:         " + order.getOrderStatus());
        }
    }

    public List<AuditResponse> getAllActivities(Class clazz, long orderId) throws JsonProcessingException {
        List<AuditResponse> auditResponses = new ArrayList<>();

        AuditReader reader = AuditReaderFactory.get(em);
        AuditQuery query = reader.createQuery().forRevisionsOfEntityWithChanges(clazz, true);
        query.add(AuditEntity.property("id").eq(orderId));
        query.addOrder(AuditEntity.revisionNumber().desc());
        List<Object[]> results = query.getResultList();
//        for (Object[] result : results) {
        for (int i = 0; i < results.size(); i++) {
            AuditResponse.AuditResponseBuilder response = AuditResponse.builder();
            Order order = (Order) results.get(i)[0];
            CustomRevisionEntity revEntity = (CustomRevisionEntity) results.get(i)[1];
            RevisionType revType = (RevisionType) results.get(i)[2];
            Set<String> properties = (Set<String>) results.get(i)[3];
//            clazz.getMethod("getOrderStatus").invoke(order)
            response.revision(revEntity.getId());
            System.out.println("Revision            :" + revEntity.getId());
            response.timestamp(revEntity.getTimestamp());
            System.out.println("Revision Date       :" + revEntity.getRevisionDate());
            response.userName(revEntity.getUserName());
            System.out.println("User                :" + revEntity.getUserName());
            User user = userService.findByEmail(revEntity.getUserName());
            response.fullName(user.getFullName());
            System.out.println("User                :" + user.getFullName());
            response.operation(revType.toString());
            System.out.println("Type                :" + revType);
            System.out.println("Changed properties");

            List<AuditResponse.PropertyValue> propertyValues = new ArrayList<>();
            Order previousRevOrder = null;
            if (RevisionType.MOD == revType) {
//                previousRevOrder = (Order) results.get(i - 1)[0];
                previousRevOrder = (Order) results.get(i + 1)[0];
            }
            for (String property : properties) {
                if ("updatedAt".equals(property)) {
                    continue;
                }
                AuditResponse.PropertyValue propertyValue = new AuditResponse.PropertyValue();
                propertyValue.setPropertyName(property);
                Object value = getValue(property, order);
                propertyValue.setValue(value);
                String formattedName = getFormattedName(property, order);
                propertyValue.setFormattedPropertyName(formattedName);
                System.out.println("============ " + property + " : " + value + "    Formatted property: " + formattedName);
                if (RevisionType.MOD == revType) {
                    propertyValue.setPreviousValue(getValue(property, previousRevOrder));
                }
                propertyValues.add(propertyValue);
            }
//            System.out.println("Order               :" + order.getId() + "      Order status:         " + order.getOrderStatus());
            response.changedProperties(propertyValues);
            auditResponses.add(response.build());
        }

        /*for (int i = 0; i < auditResponses.size(); i++) {
            if (RevisionType.MOD.toString().equals(auditResponses.get(i).getOperation())) {
                List<AuditResponse.PropertyValue> changedProperties = auditResponses.get(i).getChangedProperties();
                for (AuditResponse.PropertyValue changedProperty : changedProperties) {
                }
            }
        }*/
        return auditResponses;
    }

    /*private void getAuditRecord(Object[] result, AuditResponse.AuditResponseBuilder response) {
        Order order = (Order) result[0];
        CustomRevisionEntity revEntity = (CustomRevisionEntity) result[1];
        RevisionType revType = (RevisionType) result[2];
        Set<String> properties = (Set<String>) result[3];
//            clazz.getMethod("getOrderStatus").invoke(order)
        response.id(revEntity.getId());
        System.out.println("Revision            :" + revEntity.getId());
        response.timestamp(revEntity.getTimestamp());
        System.out.println("Revision Date       :" + revEntity.getRevisionDate());
        response.userName(revEntity.getUserName());
        System.out.println("User                :" + revEntity.getUserName());
        response.operation(revType.toString());
        System.out.println("Type                :" + revType);
        System.out.println("Changed properties");
        AuditResponse.PropertyValue.PropertyValueBuilder valueBuilder = AuditResponse.PropertyValue.builder();
        for (String property : properties) {
            valueBuilder.propertyName(property);
            Object value = getValue(property, order);
            valueBuilder.value(value);
            System.out.println("============ " + property + " : " + value);
        }
        System.out.println("Order               :" + order.getId() + "      Order status:         " + order.getOrderStatus());
    }*/

    private static Object getValue(String fieldName, Order order) throws JsonProcessingException {
        switch (fieldName) {
            case "id":
                return order.getId();
            case "brokerOrderId":
                return order.getBrokerOrderId();
            case "enclosedTrailer":
                return order.getEnclosedTrailer();
            case "m22Inspection":
                return order.getM22Inspection();
            case "pickupContactName":
                return order.getPickupContactName();
            case "pickupCompanyName":
                return order.getPickupCompanyName();
            case "pickupAddress":
                return order.getPickupAddress();
            case "pickupZip":
                return order.getPickupZip();
            case "pickupLatitude":
                return order.getPickupLatitude();
            case "pickupLongitude":
                return order.getPickupLongitude();
            case "pickupPhones":
                return order.getPickupPhones();
            case "pickupSignatureNotRequired":
                return order.getPickupSignatureNotRequired();
            case "pickupDates":
                return order.getPickupDates();
            case "pickupDatesRestrictions":
                return order.getPickupDatesRestrictions();
            case "deliveryContactName":
                return order.getDeliveryContactName();
            case "deliveryCompanyName":
                return order.getDeliveryCompanyName();
            case "deliveryAddress":
                return order.getDeliveryAddress();
            case "deliveryZip":
                return order.getDeliveryZip();
            case "deliveryLatitude":
                return order.getDeliveryLatitude();
            case "deliveryLongitude":
                return order.getDeliveryLongitude();
            case "deliveryPhones":
                return order.getDeliveryPhones();
            case "deliverySignatureNotRequired":
                return order.getDeliverySignatureNotRequired();
            case "deliveryDates":
                return order.getDeliveryDates();
            case "deliveryDatesRestrictions":
                return order.getDeliveryDatesRestrictions();
            case "vehicleYear":
                return order.getVehicleYear();
            case "vehicleMake":
                return order.getVehicleMake();
            case "vehicleModel":
                return order.getVehicleModel();
            case "vehicleAutoType":
                return order.getVehicleAutoType();
            case "vehicleColor":
                return order.getVehicleColor();
            case "vehicleVIN":
                return order.getVehicleVIN();
            case "vehicleLOTNumber":
                return order.getVehicleLOTNumber();
            case "vehicleBuyerId":
                return order.getVehicleBuyerId();
            case "vehicleInoperable":
                return order.getVehicleInoperable();
            case "dispatchInstructions":
                return order.getDispatchInstructions();
            case "carrierPay":
                return order.getCarrierPay();
            case "amountOnPickup":
                return order.getAmountOnPickup();
            case "paymentOnPickupMethod":
                return order.getPaymentOnPickupMethod();
            case "amountOnDelivery":
                return order.getAmountOnDelivery();
            case "paymentOnDeliveryMethod":
                return order.getPaymentOnDeliveryMethod();
            case "paymentTermBusinessDays":
                return order.getPaymentTermBusinessDays();
            case "paymentMethod":
                return order.getPaymentMethod();
            case "paymentTermBegins":
                return order.getPaymentTermBegins();
            case "paymentNotes":
                return order.getPaymentNotes();
            case "brokerContactName":
                return order.getBrokerContactName();
            case "brokerCompanyName":
                return order.getBrokerCompanyName();
            case "brokerAddress":
                return order.getBrokerAddress();
            case "brokerZip":
                return order.getBrokerZip();
            case "brokerLatitude":
                return order.getBrokerLatitude();
            case "brokerLongitude":
                return order.getBrokerLongitude();
            case "shipperPhones":
                return order.getShipperPhones();
            case "brokerEmail":
                return order.getBrokerEmail();
            case "orderStatus":
                return order.getOrderStatus();
            case "orderCategory":
                return order.getOrderCategory();
            case "createdBy":
                return order.getCreatedBy();
//            case "bookingRequestCarriers":
//                return order.getBookingRequestCarriers().size() > 0 ? order.getBookingRequestCarriers()
//                        .get(order.getBookingRequestCarriers().size() - 1).getStatus() : 0;
            case "bookingRequestCarriers":
//                OrderCarrier orderCarrier = order.getBookingRequestCarriers()
//                        .get(order.getBookingRequestCarriers().size() - 1);
//                String jsonString = new ObjectMapper().writeValueAsString(orderCarrier);
                OrderCarrier oc = null;
                if (order.getBookingRequestCarriers().size() > 0) {
                    oc = (order.getBookingRequestCarriers().get(order.getBookingRequestCarriers().size() - 1));
//                    Hibernate.initialize(oc.getCarrier());
//                    Long id = (order.getBookingRequestCarriers().get(order.getBookingRequestCarriers().size() - 1)).getCarrier().getId();
////                    Hibernate.initialize((order.getBookingRequestCarriers().get(order.getBookingRequestCarriers().size() - 1)).getCarrier());
//                    (order.getBookingRequestCarriers().get(order.getBookingRequestCarriers().size() - 1)).getCarrier().setId(id);
                }
                /*return order.getBookingRequestCarriers().size() > 0 ? new ObjectMapper().writeValueAsString(
                        order.getBookingRequestCarriers().get(order.getBookingRequestCarriers().size() - 1).getId()) : null;*/
                return new ObjectMapper().writeValueAsString(OrderCarrierMapper.toOrderCarrierDto(oc));
            /*case "assignedToCarrier":
//                return order.getAssignedToCarrier();
                return order.getAssignedToCarrier() != null ?
                    order.getAssignedToCarrier().getFullName() + "|" + order.getAssignedToCarrier().getEmail()
                    : null;*/
            case "bookedCarriers":
                return order.getBookedCarriers().size() > 0 ? new ObjectMapper().writeValueAsString(
                        order.getBookedCarriers().get(order.getBookedCarriers().size() - 1)) : null;
            case "assignedToDriver":
//                return order.getAssignedToDriver();
                return order.getAssignedToDriver() != null ?
                    order.getAssignedToDriver().getFullName() != null && !"".equals(order.getAssignedToDriver().getFullName()) ?
                            order.getAssignedToDriver().getFullName() : order.getAssignedToDriver().getEmail()
                    : null;
//    case "Long updatedById, "
            case "createdAt":
                return order.getCreatedAt();
            case "updatedAt":
                return order.getUpdatedAt();
            default:
                return null;
        }
    }

    private static String getFormattedName(String fieldName, Order order) {
        switch (fieldName) {
            case "id":
                return "Order #";
            case "brokerOrderId":
                return "Broker Order ID";
            case "enclosedTrailer":
                return "Enclosed Trailer";
            case "m22Inspection":
                return "M-22 Inspection";
            case "pickupContactName":
                return "Pickup Contact Name";
            case "pickupCompanyName":
                return "Pickup Company Name";
            case "pickupAddress":
                return "Pickup Address";
            case "pickupZip":
                return "Pickup Zip";
            case "pickupLatitude":
                return "Pickup Latitude";
            case "pickupLongitude":
                return "Pickup Longitude";
            case "pickupPhones":
                return "Pickup Phones";
            case "pickupSignatureNotRequired":
                return "Pickup Signature Not Required";
            case "pickupDates":
                return "Pickup Dates";
            case "pickupDatesRestrictions":
                return "Pickup Dates Restrictions";
            case "deliveryContactName":
                return "Delivery Nontact Name";
            case "deliveryCompanyName":
                return "Delivery Company Name";
            case "deliveryAddress":
                return "Delivery Address";
            case "deliveryZip":
                return "Delivery Zip";
            case "deliveryLatitude":
                return "Delivery Latitude";
            case "deliveryLongitude":
                return "Delivery Longitude";
            case "deliveryPhones":
                return "Delivery Phones";
            case "deliverySignatureNotRequired":
                return "Delivery Signature Not Required";
            case "deliveryDates":
                return "Delivery Dates";
            case "deliveryDatesRestrictions":
                return "Delivery Dates Restrictions";
            case "vehicleYear":
                return "Vehicle Year";
            case "vehicleMake":
                return "Vehicle Make";
            case "vehicleModel":
                return "Vehicle Model";
            case "vehicleAutoType":
                return "Auto Type";
            case "vehicleColor":
                return "Vehicle Color";
            case "vehicleVIN":
                return "Vehicle VIN";
            case "vehicleLOTNumber":
                return "Vehicle LOT Number";
            case "vehicleBuyerId":
                return "Vehicle Buyer Id";
            case "vehicleInoperable":
                return "Vehicle Inoperable";
            case "dispatchInstructions":
                return "Dispatch Instructions";
            case "carrierPay":
                return "Carrier Pay";
            case "amountOnPickup":
                return "Amount On Pickup";
            case "paymentOnPickupMethod":
                return "Payment On Pickup Method";
            case "amountOnDelivery":
                return "Amount On Delivery";
            case "paymentOnDeliveryMethod":
                return "Payment On Delivery Method";
            case "paymentTermBusinessDays":
                return "Payment Term Business Days";
            case "paymentMethod":
                return "Payment Method";
            case "paymentTermBegins":
                return "Payment Term Begins";
            case "paymentNotes":
                return "Payment Notes";
            case "brokerContactName":
                return "Broker Contact Name";
            case "brokerCompanyName":
                return "Broker Company Name";
            case "brokerAddress":
                return "Broker Address";
            case "brokerZip":
                return "Broker Zip";
            case "brokerLatitude":
                return "Broker Latitude";
            case "brokerLongitude":
                return "Broker Longitude";
            case "shipperPhones":
                return "Shipper Phones";
            case "brokerEmail":
                return "Broker Email";
            case "orderStatus":
                return "Order Status";
            case "orderCategory":
                return "Order Category";
            case "createdBy":
                return "Created By";
            case "bookingRequestCarriers":
                return "Booking Request By Carriers";
            case "bookedCarriers":
                return "Offered by broker";
//            case "assignedToCarrier":
//                return "Assigned To Carrier";
            case "assignedToDriver":
                return "Assigned To Driver";
            case "createdAt":
                return "Created At";
            case "updatedAt":
                return "Updated At";
            default:
                return null;
        }
    }
}
