package com.transport.app.rest.controller;

import com.transport.app.rest.domain.Company;
import com.transport.app.rest.mapper.CompanyDto;
import com.transport.app.rest.mapper.CompanyMapper;
import com.transport.app.rest.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public CompanyDto update(@RequestBody Company company) {
        return CompanyMapper.toCompanyDto(companyService.save(company));
    }

    @GetMapping("/get")
    public List<CompanyDto> findAll() {
        return CompanyMapper.toCompanyDtos(companyService.getAll());
    }
}
