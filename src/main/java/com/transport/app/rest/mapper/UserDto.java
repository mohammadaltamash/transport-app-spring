package com.transport.app.rest.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String userName;
    private String password;
    private String resetToken;
    private String jwtToken;
    private String fullName;
    private String companyName;
    private String address;
    private String zip;
    private Double latitude;
    private Double longitude;
    private Map<String, String> phones;
    private String email;
    private String type;
    private List<Long> orders;
}
