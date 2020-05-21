package com.transport.app.rest.controller;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.service.CityZipLatLongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityZipLatLongController {

    @Autowired
    private CityZipLatLongService service;

    @GetMapping("/location/{text}")
    public List<CityZipLatLong> findByZipOrCityLike(@PathVariable("text") String text) {
        return service.findByZipOrCityLike(text);
    }

    @GetMapping("/location/count")
    public Long getCount() {
        return service.getCount();
    }

    @GetMapping("/location/first")
    public CityZipLatLong getFirst() {
        return service.getFirst();
    }

    @GetMapping("/location/last")
    public CityZipLatLong getLast() {
        return service.getLast();
    }

    @PostMapping("/addlocations")
    public Integer add(@RequestParam("filename") String fileName) {
        return service.add(fileName);
    }
}
