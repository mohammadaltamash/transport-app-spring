package com.transport.app.rest.service;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.repository.CityZipLatLongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityZipLatLongService {

    @Autowired
    private CityZipLatLongRepository repository;

    public List<CityZipLatLong> findByZipOrCityLike(String text) {
        return repository.findByZipContainsOrCityContainsIgnoreCase(text, text);
    }
}
