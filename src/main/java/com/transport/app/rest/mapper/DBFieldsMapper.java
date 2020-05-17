package com.transport.app.rest.mapper;

public class DBFieldsMapper {

    public static String getDBField(String modelField) {
        switch(modelField) {
            case "perMile":
                return "per_mile";
            case "createdAt":
                return "created_at";
            case "trailerCondition":
                return "";
            case "vehicleInoperable":
                return "vehicle_inoperable";
            case "vehicleAutoType":
                return "vehicle_auto_type";
            case "carrierPay":
                return "carrier_pay";
            default:
                return null;
        }
    }
}
