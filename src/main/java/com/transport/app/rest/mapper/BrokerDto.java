package com.transport.app.rest.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class BrokerDto {

    private Long id;
    private String contactName;
    private String companyName;
    private String address;
    private String zip;
    private Double latitude;
    private Double longitude;
    private Map<String, String> phones;
    private String email;
}
