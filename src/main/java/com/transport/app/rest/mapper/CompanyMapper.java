package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Company;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static Company toCompany(CompanyDto companyDto) {
        return Company.builder()
                .id(companyDto.getId())
                .contactName(companyDto.getContactName())
                .companyName(companyDto.getCompanyName())
                .address(companyDto.getAddress())
                .addressState(companyDto.getAddressState())
                .zip(companyDto.getZip())
                .latitude(companyDto.getLatitude())
                .longitude(companyDto.getLongitude())
                .phones(companyDto.getPhones())
                .companyEmail(companyDto.getCompanyEmail())
                .createdAt(companyDto.getCreatedAt())
                .updatedAt(companyDto.getUpdatedAt())
                .build();
    }

    public static CompanyDto toCompanyDto(Company company) {
        if (company == null) {
            return null;
        }
        return CompanyDto.builder()
                .id(company.getId())
                .contactName(company.getContactName())
                .companyName(company.getCompanyName())
                .address(company.getAddress())
                .addressState(company.getAddressState())
                .zip(company.getZip())
                .latitude(company.getLatitude())
                .longitude(company.getLongitude())
                .phones(company.getPhones())
                .companyEmail(company.getCompanyEmail())
                .createdBy(UserMapper.toUserDto(company.getCreatedBy()))
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public static Company toUpdatedCompany(Company company, Company companyUpdate) {
        company.setContactName(companyUpdate.getContactName() == null ? company.getContactName() : companyUpdate.getContactName());
        company.setCompanyName(companyUpdate.getCompanyName() == null ? company.getCompanyName() : companyUpdate.getCompanyName());
        company.setAddress(companyUpdate.getAddress() == null ? company.getAddress() : companyUpdate.getAddress());
        company.setAddressState(companyUpdate.getAddressState() == null ? company.getAddressState() : companyUpdate.getAddressState());
        company.setZip(companyUpdate.getZip() == null ? company.getZip() : companyUpdate.getZip());
        company.setLatitude(companyUpdate.getLatitude() == null ? company.getLatitude() : companyUpdate.getLatitude());
        company.setLongitude(companyUpdate.getLongitude() == null ? company.getLongitude() : companyUpdate.getLongitude());
        company.setPhones(companyUpdate.getPhones() == null ? company.getPhones() : companyUpdate.getPhones());
        company.setCompanyEmail(companyUpdate.getCompanyEmail() == null ? company.getCompanyEmail() : companyUpdate.getCompanyEmail());
        return company;
    }

    public static List<Company> toCompanies(List<CompanyDto> companyDtos) {
        return companyDtos.stream().map(o -> toCompany(o)).collect(Collectors.toList());
    }

    public static List<CompanyDto> toCompanyDtos(List<Company> companies) {
        return companies.stream().map(c -> toCompanyDto(c)).collect(Collectors.toList());
    }
}
