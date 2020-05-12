package com.transport.app.rest.domain;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class LatitudeLongitudeDistance {

    private double latitude;
    private double longitude;
    private int distance;
}
