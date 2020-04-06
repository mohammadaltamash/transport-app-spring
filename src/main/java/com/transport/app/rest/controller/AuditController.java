package com.transport.app.rest.controller;

import com.transport.app.rest.domain.AuditResponse;
import com.transport.app.rest.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/audit")
public class AuditController {

    private AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/get/{class}")
    public void getActivities(@PathVariable("class") String clazz) throws ClassNotFoundException {
        auditService.getAllActivities(Class.forName("com.transport.app.rest.domain." + clazz));
    }

    @GetMapping("/get/{class}/{id}")
    public List<AuditResponse> getActivitiesForId(@PathVariable("class") String clazz, @PathVariable("id") long id) throws ClassNotFoundException {
        return auditService.getAllActivities(Class.forName("com.transport.app.rest.domain." + clazz), id);
    }
}
