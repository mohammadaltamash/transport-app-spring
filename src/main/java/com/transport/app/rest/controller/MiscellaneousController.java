package com.transport.app.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.Preferences;
import com.transport.app.rest.service.PreferencesService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class MiscellaneousController {

    private PreferencesService preferencesService;

    public MiscellaneousController(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @PutMapping("/preferences")
    public Preferences saveOrUpdatePreferences(@RequestBody Preferences preferences) {
        return preferencesService.saveOrUpdate(preferences);
    }

    @GetMapping("/preferences/get")
    public Preferences findFirst() {
        return preferencesService.findFirst();
    }
}
