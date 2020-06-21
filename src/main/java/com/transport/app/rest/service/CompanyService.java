package com.transport.app.rest.service;

import com.transport.app.rest.domain.Company;
import com.transport.app.rest.exception.AlreadyExistsException;
import com.transport.app.rest.repository.CompanyRepository;
import com.transport.app.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company save(Company company) {
        if (companyRepository.findByCompanyName(company.getCompanyName()) != null) {
//            throw new AlreadyExistsException()
        }
        return companyRepository.save(company);
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public List<Company> findByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }
}
