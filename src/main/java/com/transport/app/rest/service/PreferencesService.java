package com.transport.app.rest.service;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.domain.Preferences;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.PreferencesMapper;
import com.transport.app.rest.repository.CityZipLatLongRepository;
import com.transport.app.rest.repository.PreferencesRepository;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PreferencesService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PreferencesRepository repository;

    public Preferences saveOrUpdate(Preferences preferences) {
        if (preferences.getId() == null) {
            return this.repository.save(preferences);
        } else {
            Preferences fromDb = repository.findById(preferences.getId()).orElseThrow(() -> new NotFoundException(
                    Preferences.class, preferences.getId()));
            Preferences p = PreferencesMapper.toUpdatedPreferences(fromDb, preferences);
            return this.repository.save(p);
        }
    }

    public Preferences findFirst() {
        List<Preferences> preferences = this.repository.findAll();
        if (preferences.size() > 0) {
            return preferences.get(0);
        } else {
            return null;
        }
    }

    public void delete() {
        repository.deleteAll();
    }
}
