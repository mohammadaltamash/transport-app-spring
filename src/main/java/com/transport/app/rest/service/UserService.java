package com.transport.app.rest.service;

import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.UserMapper;
import com.transport.app.rest.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<User> findAllByType(String type) {
        return userAuthRepository.findAllByType(type);
    }

    public User findById(Long orderId) {
        return userAuthRepository.findById(orderId).orElseThrow(() -> new NotFoundException(User.class, orderId));
    }

    public List<User> findAll() {
        return userAuthRepository.findAll();
    }

    public void save(User user) {
        userAuthRepository.save(user);
    }

    public User update(User user) {
        User u = findById(user.getId());
        return userAuthRepository.save(UserMapper.toUpdatedUser(u, user));
    }
}
