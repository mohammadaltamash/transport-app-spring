package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Carrier;

import java.util.List;
import java.util.stream.Collectors;

public class CarrierMapper {

    public static Carrier toCarrier(CarrierDto carrierDto) {
        return Carrier.builder()
                .id(carrierDto.getId())
                .contactName(carrierDto.getContactName())
                .companyName(carrierDto.getCompanyName())
                .address(carrierDto.getAddress())
                .zip(carrierDto.getZip())
                .latitude(carrierDto.getLatitude())
                .longitude(carrierDto.getLongitude())
                .phones(carrierDto.getPhones())
                .email(carrierDto.getEmail())
                .build();
    }

    public static CarrierDto toCarrierDto(Carrier carrier) {
        return CarrierDto.builder()
                .id(carrier.getId())
                .contactName(carrier.getContactName())
                .companyName(carrier.getCompanyName())
                .address(carrier.getAddress())
                .zip(carrier.getZip())
                .latitude(carrier.getLatitude())
                .longitude(carrier.getLongitude())
                .phones(carrier.getPhones())
                .email(carrier.getEmail())
                .build();
    }

    public static Carrier toUpdatedCarrier(Carrier carrier, Carrier carrierUpdate) {
        carrier.setId(carrierUpdate.getId() == null ? carrier.getId() : carrierUpdate.getId());
        carrier.setContactName(carrierUpdate.getContactName() == null ? carrier.getContactName() : carrierUpdate.getContactName());
        carrier.setCompanyName(carrierUpdate.getCompanyName() == null ? carrier.getCompanyName() : carrierUpdate.getCompanyName());
        carrier.setAddress(carrierUpdate.getAddress() == null ? carrier.getAddress() : carrierUpdate.getAddress());
        carrier.setZip(carrierUpdate.getZip() == null ? carrier.getZip() : carrierUpdate.getZip());
        carrier.setLatitude(carrierUpdate.getLatitude() == null ? carrier.getLatitude() : carrierUpdate.getLatitude());
        carrier.setLongitude(carrierUpdate.getLongitude() == null ? carrier.getLongitude() : carrierUpdate.getLongitude());
        carrier.setPhones(carrierUpdate.getPhones() == null ? carrier.getPhones() : carrierUpdate.getPhones());
        carrier.setEmail(carrierUpdate.getEmail() == null ? carrier.getEmail() : carrierUpdate.getEmail());
        return carrier;
    }

    public static List<Carrier> toCarriers(List<CarrierDto> carrierDtos) {
        return carrierDtos.stream().map(c -> toCarrier(c)).collect(Collectors.toList());
    }

    public static List<CarrierDto> toCarrierDtos(List<Carrier> carriers) {
        return carriers.stream().map(c -> toCarrierDto(c)).collect(Collectors.toList());
    }
}
