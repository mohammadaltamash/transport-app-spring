package com.transport.app.rest.controller;

import com.transport.app.rest.domain.User;
import com.transport.app.rest.mapper.UserDto;
import com.transport.app.rest.mapper.UserMapper;
import com.transport.app.rest.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public UserDto update(@RequestBody User user) {
        return UserMapper.toUserDto(userService.update(user));
    }

    @GetMapping("/get/{email}")
    public UserDto findByEmail(@PathVariable("email") String email) {
        return UserMapper.toUserDto(userService.findByEmail(email));
    }

    @GetMapping("/get/type/{type}")
    public List<UserDto> findByType(@PathVariable("type") String type) {
        return UserMapper.toUserDtos(userService.findAllByType(type));
    }

    @GetMapping("/get/company/{company}")
    public List<UserDto> findDriversByCompany(@PathVariable("company") String company) {
        return UserMapper.toUserDtos(userService.findDriversInCompany("DRIVER", company));
    }

    @GetMapping("/get")
    public List<UserDto> findAll() {
        return UserMapper.toUserDtos(userService.findAll());
    }
}
