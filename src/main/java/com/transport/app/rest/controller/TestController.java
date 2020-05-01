package com.transport.app.rest.controller;

import com.transport.app.rest.service.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestData data;

    @RequestMapping("/greet")
    public String getUser(Authentication authentication) {
        return "Authenticated as " + ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    }

    ///////////////// Mock
    @GetMapping("/generate")
    public void generateOrders() {
        data.generateData();
    }
}
