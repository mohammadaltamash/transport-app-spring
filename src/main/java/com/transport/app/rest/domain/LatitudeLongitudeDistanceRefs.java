package com.transport.app.rest.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LatitudeLongitudeDistanceRefs {

    private List<LatitudeLongitudeDistance> pickupLatLongs;
    private List<LatitudeLongitudeDistance> deliveryLatLongs;
}
