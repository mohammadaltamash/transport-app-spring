package com.transport.app.rest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/greet")
    public String getUser(Authentication authentication) {
        return "Authenticated as " + ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    }

}
