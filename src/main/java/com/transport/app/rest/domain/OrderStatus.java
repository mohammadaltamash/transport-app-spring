package com.transport.app.rest.domain;

public enum OrderStatus {
    NEW("NEW"), BOOKING_REQUEST("BOOKING_REQUEST"), BOOKED("BOOKED"), ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED"), ASSIGNED("ASSIGNED"), PICKED_UP("PICKED UP"), DELIVERED("DELIVERED");
    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
