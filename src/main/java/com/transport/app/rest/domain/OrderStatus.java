package com.transport.app.rest.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static boolean contains(String statuses) {
        return Stream.of(OrderStatus.values())
                .map(os -> os.getName())
                .collect(Collectors.toList())
                .containsAll(Arrays.stream(statuses.split(","))
                    .map(m -> m.trim())
                    .collect(Collectors.toList()));
    }
}
