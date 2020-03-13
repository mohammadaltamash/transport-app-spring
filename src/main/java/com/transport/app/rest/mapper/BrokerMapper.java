package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Broker;

import java.util.List;
import java.util.stream.Collectors;

public class BrokerMapper {

    public static Broker toBroker(BrokerDto brokerDto) {
        return Broker.builder()
                .id(brokerDto.getId())
                .contactName(brokerDto.getContactName())
                .companyName(brokerDto.getCompanyName())
                .address(brokerDto.getAddress())
                .zip(brokerDto.getZip())
                .latitude(brokerDto.getLatitude())
                .longitude(brokerDto.getLongitude())
                .phones(brokerDto.getPhones())
                .email(brokerDto.getEmail())
                .build();
    }

    public static BrokerDto toBrokerDto(Broker broker) {
        return BrokerDto.builder()
                .id(broker.getId())
                .contactName(broker.getContactName())
                .companyName(broker.getCompanyName())
                .address(broker.getAddress())
                .zip(broker.getZip())
                .latitude(broker.getLatitude())
                .longitude(broker.getLongitude())
                .phones(broker.getPhones())
                .email(broker.getEmail())
                .build();
    }

    public static Broker toUpdatedBroker(Broker broker, Broker brokerUpdate) {
        broker.setId(brokerUpdate.getId() == null ? broker.getId() : brokerUpdate.getId());
        broker.setContactName(brokerUpdate.getContactName() == null ? broker.getContactName() : brokerUpdate.getContactName());
        broker.setCompanyName(brokerUpdate.getCompanyName() == null ? broker.getCompanyName() : brokerUpdate.getCompanyName());
        broker.setAddress(brokerUpdate.getAddress() == null ? broker.getAddress() : brokerUpdate.getAddress());
        broker.setZip(brokerUpdate.getZip() == null ? broker.getZip() : brokerUpdate.getZip());
        broker.setLatitude(brokerUpdate.getLatitude() == null ? broker.getLatitude() : brokerUpdate.getLatitude());
        broker.setLongitude(brokerUpdate.getLongitude() == null ? broker.getLongitude() : brokerUpdate.getLongitude());
        broker.setPhones(brokerUpdate.getPhones() == null ? broker.getPhones() : brokerUpdate.getPhones());
        broker.setEmail(brokerUpdate.getEmail() == null ? broker.getEmail() : brokerUpdate.getEmail());
        return broker;
    }

    public static List<Broker> toBrokers(List<BrokerDto> brokerDtos) {
        return brokerDtos.stream().map(b -> toBroker(b)).collect(Collectors.toList());
    }

    public static List<BrokerDto> toBrokerDtos(List<Broker> brokers) {
        return brokers.stream().map(b -> toBrokerDto(b)).collect(Collectors.toList());
    }
}
