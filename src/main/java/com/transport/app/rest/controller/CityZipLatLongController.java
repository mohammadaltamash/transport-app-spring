package com.transport.app.rest.controller;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.service.CityZipLatLongService;
import com.transport.app.rest.service.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityZipLatLongController {

    @Autowired
    private CityZipLatLongService service;

    @GetMapping("/location/{text}")
    public List<CityZipLatLong> findByZipOrCityLike(@PathVariable("text") String text) {
        return service.findByZipOrCityLike(text);
    }
}
