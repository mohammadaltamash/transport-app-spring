package com.transport.app.rest.mapper;

import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String userName;
    private String password;
    private String resetToken;
    private String fullName;
    private String companyName;
    private String address;
    private String zip;
    private Double latitude;
    private Double longitude;
    private Map<String, String> phones;
    private String email;
    private String type;
}
