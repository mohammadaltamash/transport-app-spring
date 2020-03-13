package com.transport.app.rest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DRIVERS")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "VEHICLE_AUTO_TYPE")
    private String vehicleAutoType;
    @Column(name = "NUMBER_OF_VEHICLES")
    private Integer numberOfVehicles;
}
