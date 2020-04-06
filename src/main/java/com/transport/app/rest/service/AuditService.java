package com.transport.app.rest.service;

import com.transport.app.rest.domain.AuditResponse;
import com.transport.app.rest.domain.CustomRevisionEntity;
import com.transport.app.rest.domain.Order;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager em;

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

    public List<AuditResponse> getAllActivities(Class clazz, long orderId) {
        List<AuditResponse> auditResponses = new ArrayList<>();

        AuditReader reader = AuditReaderFactory.get(em);
        AuditQuery query = reader.createQuery().forRevisionsOfEntityWithChanges(clazz, true);
        query.add(AuditEntity.property("id").eq(orderId));
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            AuditResponse.AuditResponseBuilder response = AuditResponse.builder();
            Order order = (Order) result[0];
            CustomRevisionEntity revEntity = (CustomRevisionEntity) result[1];
            RevisionType revType = (RevisionType) result[2];
            Set<String> properties = (Set<String>) result[3];
//            clazz.getMethod("getOrderStatus").invoke(order)
            response.revision(revEntity.getId());
            System.out.println("Revision            :" + revEntity.getId());
            response.timestamp(revEntity.getTimestamp());
            System.out.println("Revision Date       :" + revEntity.getRevisionDate());
            response.userName(revEntity.getUserName());
            System.out.println("User                :" + revEntity.getUserName());
            response.operation(revType.toString());
            System.out.println("Type                :" + revType);
            System.out.println("Changed properties");

            List<AuditResponse.PropertyValue> propertyValues = new ArrayList<>();
            for (String property : properties) {
                AuditResponse.PropertyValue propertyValue = new AuditResponse.PropertyValue();
                propertyValue.setPropertyName(property);
                Object[] nameValue = getNameValue(property, order);
                propertyValue.setValue(nameValue[0]);
                propertyValue.setFormattedPropertyName((String) nameValue[1]);
                System.out.println("============ " + property + " : " + nameValue[0] + "    Formatted property: " + nameValue[1]);
                propertyValues.add(propertyValue);
            }
//            System.out.println("Order               :" + order.getId() + "      Order status:         " + order.getOrderStatus());
            response.changedProperties(propertyValues);
            auditResponses.add(response.build());
        }
//        Object initialValue = null;
//        for (int i = 1; i < results.size(); i++) {
//            getAuditRecord(results.get(i), response);
//        }
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

    private static Object[] getNameValue(String fieldName, Order order) {
        switch(fieldName) {
            case "id":
                return new Object[] {order.getId(), "Order #"};
            //    Broker Order ID               required
            case "brokerOrderId"://    Broker Order ID               required
                return new Object[] {order.getBrokerOrderId(), "Broker Order ID"}; //    Broker Order ID               required
            //    Enclosed trailer
            case "enclosedTrailer"://    Enclosed trailer
                return new Object[] {order.getEnclosedTrailer(), "Enclosed Trailer"}; //    Enclosed trailer
            //    M-22 inspection
            case "m22Inspection":
                return new Object[] {order.getM22Inspection(), "M-22 Inspection"};

            //    Pickup Contact & Location
//    Contact name
            case "pickupContactName":
                return new Object[] {order.getPickupContactName(), "Pickup Contact Name"};
            //    Company name
            case "pickupCompanyName":
                return new Object[] {order.getPickupCompanyName(), "Pickup Company Name"};
            //    Pickup address                required
            case "pickupAddress":
                return new Object[] {order.getPickupAddress(), "Pickup Address"};
            //    Zip                           required
            case "pickupZip":
                return new Object[] {order.getPickupZip(), "Pickup Zip"};
            case "pickupLatitude":
                return new Object[] {order.getPickupLatitude(), "Pickup Latitude"};
            case "pickupLongitude":
                return new Object[] {order.getPickupLongitude(), "Pickup Longitude"};
            //    Phone 1 (can be multiple)     required
            /*case "List<String> pickupPhones, "*/
//    Phone 1 notes
            /*case "List<String> pickupPhoneNotes, "*/
            case "pickupPhones":
                return new Object[] {order.getPickupPhones(), "Pickup Phones"};
            //    Signature not required
            case "pickupSignatureNotRequired":
                return new Object[] {order.getPickupSignatureNotRequired(), "Pickup Signature Not Required"};
            //    Pickup dates                  required
//    @Temporal(TemporalType.TIMESTAMP)
            case "pickupDates":
                return new Object[] {order.getPickupDates(), "Pickup Dates"};
            //    case "Date pickupStartDate, "
//    case "Date pickupEndDate, "
            //    Pickup dates restrictions
            case "pickupDatesRestrictions":
                return new Object[] {order.getPickupDatesRestrictions(), "Pickup Dates Restrictions"};

            //    Delivery Contact & Location
//    Contact name
            case "deliveryContactName":
                return new Object[] {order.getDeliveryContactName(), "Delivery Nontact Name"};
            //    Company name
            case "deliveryCompanyName":
                return new Object[] {order.getDeliveryCompanyName(), "Delivery Company Name"};
            //    Delivery address              required
            case "deliveryAddress":
                return new Object[] {order.getDeliveryAddress(), "Delivery Address"};
            //    Zip                           required
            case "deliveryZip":
                return new Object[] {order.getDeliveryZip(), "Delivery Zip"};
            case "deliveryLatitude":
                return new Object[] {order.getDeliveryLatitude(), "Delivery Latitude"};
            case "deliveryLongitude":
                return new Object[] {order.getDeliveryLongitude(), "Delivery Longitude"};
            //    Phone 1 (can be multiple)     required
            /*case "List<String> deliveryPhones, "*/
//    Phone 1 notes
            /*case "List<String> deliveryPhoneNotes, "*/
            case "deliveryPhones":
                return new Object[] {order.getDeliveryPhones(), "Delivery Phones"};
            //    Signature not required
            case "deliverySignatureNotRequired":
                return new Object[] {order.getDeliverySignatureNotRequired(), "Delivery Signature Not Required"};
            //    Delivery dates                required
//    case "List<Date> deliveryDates, "
            case "deliveryDates":
                return new Object[] {order.getDeliveryDates(), "Delivery Dates"};
            //    case "Date deliveryStartDate, "
//    case "Date deliveryEndDate, "
            //    Delivery dates restrictions
            case "deliveryDatesRestrictions":
                return new Object[] {order.getDeliveryDatesRestrictions(), "Delivery Dates Restrictions"};

            //    Add New Vehicle
//    Year
            case "vehicleYear":
                return new Object[] {order.getVehicleYear(), "Vehicle Year"};
            //    Make                          required
            case "vehicleMake":
                return new Object[] {order.getVehicleMake(), "Vehicle Make"};
            //    Model
            case "vehicleModel":
                return new Object[] {order.getVehicleModel(), "Vehicle Model"};
            //    Autotype
            case "vehicleAutoType":
                return new Object[] {order.getVehicleAutoType(), "Auto Type"};
            //    Color
            case "vehicleColor":
                return new Object[] {order.getVehicleColor(), "Vehicle Color"};
            //    VIN
            case "vehicleVIN":
                return new Object[] {order.getVehicleVIN(), "Vehicle VIN"};
            //    LOT number
            case "vehicleLOTNumber":
                return new Object[] {order.getVehicleLOTNumber(), "Vehicle LOT Number"};
            //    Buyer ID
            case "vehicleBuyerId":
                return new Object[] {order.getVehicleBuyerId(), "Vehicle Buyer Id"};
            //    Inoperable
            case "vehicleInoperable":
                return new Object[] {order.getVehicleInoperable(), "Vehicle Inoperable"};

            //    Dispatch Information
//    Dispatch Instructions
            case "dispatchInstructions":
                return new Object[] {order.getDispatchInstructions(), "Dispatch Instructions"};

            //    Pricing Information
//    Carrier pay                   required
            case "carrierPay":
                return new Object[] {order.getCarrierPay(), "Carrier Pay"};
            //    Amount on pickup
            case "amountOnPickup":
                return new Object[] {order.getAmountOnPickup(), "Amount On Pickup"};
            //    Payment on pickup method
            case "paymentOnPickupMethod":
                return new Object[] {order.getPaymentOnPickupMethod(), "Payment On Pickup Method"};
            //    Amount on delivery
            case "amountOnDelivery":
                return new Object[] {order.getAmountOnDelivery(), "Amount On Delivery"};
            //    Payment on delivery method
            case "paymentOnDeliveryMethod":
                return new Object[] {order.getPaymentOnDeliveryMethod(), "Payment On Delivery Method"};
            /////////////////////////////////////////////
            case "paymentTermBusinessDays":
                return new Object[] {order.getPaymentTermBusinessDays(), "Payment Term Business Days"};
            case "paymentMethod":
                return new Object[] {order.getPaymentMethod(), "Payment Method"};
            case "paymentTermBegins":
                return new Object[] {order.getPaymentTermBegins(), "Payment Term Begins"};
            case "paymentNotes":
                return new Object[] {order.getPaymentNotes(), "Payment Notes"};
            /////////////////////////////////////////////

            //    Shipper Information
//    Broker contact name
            case "brokerContactName":
                return new Object[] {order.getBrokerContactName(), "Broker Contact Name"};
            //    Broker company name           required
            case "brokerCompanyName":
                return new Object[] {order.getBrokerCompanyName(), "Broker Company Name"};
            //    Broker address                required
            case "brokerAddress":
                return new Object[] {order.getBrokerAddress(), "Broker Address"};
            //    Zip                           required
            case "brokerZip":
                return new Object[] {order.getBrokerZip(), "Broker Zip"};
            case "brokerLatitude":
                return new Object[] {order.getBrokerLatitude(), "Broker Latitude"};
            case "brokerLongitude":
                return new Object[] {order.getBrokerLongitude(), "Broker Longitude"};
            //    Phone 1 (can be multiple)     required
            /*case "List<String> shipperPhones, "*/
//    Phone 1 notes
            /*case "List<String> shipperPhoneNotes, "*/
            case "shipperPhones":
                return new Object[] {order.getShipperPhones(), "Shipper Phones"};
            //    Broker email                  required
            case "brokerEmail":
                return new Object[] {order.getBrokerEmail(), "Broker Email"};

            case "orderStatus":
                return new Object[] {order.getOrderStatus(), "Order Status"};
            case "orderCategory":
                return new Object[] {order.getOrderCategory(), "Order Category"};
            case "orderDriver":
                return new Object[] {order.getOrderDriver(), "Order Driver"};
            case "askedToBook":
                return new Object[] {order.getAskedToBook(), "Asked To Book"};
            case "createdBy":
                return new Object[] {order.getCreatedBy(), "Created By"};
//    case "Long updatedById, "
            case "createdAt":
                return new Object[] {order.getCreatedAt(), "Created At"};
            case "updatedAt":
                return new Object[] {order.getUpdatedAt(), "Updated At"};
            default:
                return null;
        }
    }
}
