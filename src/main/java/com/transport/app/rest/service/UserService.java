package com.transport.app.rest.service;

import com.transport.app.rest.domain.User;
import com.transport.app.rest.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    public User findByEmail(String email) {
        return userAuthRepository.findByEmail(email);
    }

    public User findByToken(String token) {
        return userAuthRepository.findByResetToken(token);
    }

    public void save(User user) {
        userAuthRepository.save(user);
    }
}
