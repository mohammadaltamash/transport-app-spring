package com.transport.app.rest.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
public class CompanyDto {

    private Long id;

    private String contactName;
    private String companyName;
    private String address;
    private String addressState;
    private String zip;
    private Double latitude;
    private Double longitude;
    private Map<String, String> phones;
    private String companyEmail;

    private UserDto createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
