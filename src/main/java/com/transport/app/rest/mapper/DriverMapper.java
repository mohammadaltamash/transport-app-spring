package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Driver;

import java.util.List;
import java.util.stream.Collectors;

public class DriverMapper {

    public static Driver toDriver(DriverDto driverDto) {
        return Driver.builder()
                .id(driverDto.getId())
                .firstName(driverDto.getFirstName())
                .lastName(driverDto.getLastName())
                .email(driverDto.getEmail())
                .vehicleAutoType(driverDto.getVehicleAutoType())
                .numberOfVehicles(driverDto.getNumberOfVehicles())
                .build();
    }

    public static DriverDto toDriverDto(Driver driver) {
        return DriverDto.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .email(driver.getEmail())
                .vehicleAutoType(driver.getVehicleAutoType())
                .numberOfVehicles(driver.getNumberOfVehicles())
                .build();
    }

    public static Driver toUpdatedDriver(Driver driver, Driver driverUpdate) {
        driver.setId(driverUpdate.getId() == null ? driver.getId() : driverUpdate.getId());
        driver.setFirstName(driverUpdate.getFirstName() == null ? driver.getFirstName() : driverUpdate.getFirstName());
        driver.setLastName(driverUpdate.getLastName() == null ? driver.getLastName() : driverUpdate.getLastName());
        driver.setEmail(driverUpdate.getEmail() == null ? driver.getEmail() : driverUpdate.getEmail());
        driver.setVehicleAutoType(driverUpdate.getVehicleAutoType() == null ? driver.getVehicleAutoType() : driverUpdate.getVehicleAutoType());
        driver.setNumberOfVehicles(driverUpdate.getNumberOfVehicles() == null ? driver.getNumberOfVehicles() : driverUpdate.getNumberOfVehicles());
        return driver;
    }

    public static List<Driver> toDrivers(List<DriverDto> driverDtos) {
        return driverDtos.stream().map(d -> toDriver(d)).collect(Collectors.toList());
    }

    public static List<DriverDto> toDriverDtos(List<Driver> drivers) {
        return drivers.stream().map(d -> toDriverDto(d)).collect(Collectors.toList());
    }
}
