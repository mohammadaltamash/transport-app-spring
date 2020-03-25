package com.transport.app.rest.controller;

import com.transport.app.rest.config.JwtToken;
import com.transport.app.rest.domain.JwtRequest;
import com.transport.app.rest.domain.JwtResponse;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.AlreadyExistsException;
import com.transport.app.rest.service.JwtUserDetailsService;
import com.transport.app.rest.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;

@CrossOrigin
@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserService userService;

    @RequestMapping("/*")
    public String getUser(Authentication authentication) {
        return "Authenticated as " + ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
    }

    @PostMapping("/register")
    public Boolean register(@RequestBody User user) {
        User userExists = userService.findByEmail(user.getEmail());
        if (userExists != null) {
            throw new AlreadyExistsException(user.getEmail());
        }
        String password = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        userService.save(user);
        return true;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody JwtRequest data) throws Exception {
        authenticate(data.getEmail(), data.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(data.getEmail());
        final String token = jwtToken.generateToken(userDetails);
//        return ResponseEntity.ok(new JwtResponse(token));
        User user = userService.findByEmail(data.getEmail());
        user.setJwtToken(token);
        return ResponseEntity.ok(user);
    }

    private void authenticate(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping("/forgot")
    public void forgotPassword(@RequestParam(value = "email") String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new WebApplicationException("User with email " + email + " not found!");
        }
        user.setResetToken(RandomStringUtils.randomAlphanumeric(60));
        userService.save(user);
        // Send this via email
        String resetLink = "http://localhost:8084/convertein/v1/reset?token=" + user.getResetToken();
        System.out.println("Click this link to reset password");
        System.out.println("\n " + resetLink);
    }

    @RequestMapping("/reset")
    public String resetPassword(@RequestParam(value = "token") String token) {
        User user = userService.findByToken(token);
        if (user == null) {
            throw new WebApplicationException("Invalid token!");
        }
        user.setResetToken(null);
        userService.save(user);
        return "reset";
    }
}
