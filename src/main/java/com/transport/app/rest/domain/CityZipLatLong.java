package com.transport.app.rest.domain;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CityZipLatLong")
public class CityZipLatLong {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String zip;
    private String city;
    private String state;
    private double latitude;
    private double longitude;
    private int timezone;
    private String geopoint;
}
