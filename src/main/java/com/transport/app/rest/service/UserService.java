package com.transport.app.rest.service;

import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.UserMapper;
import com.transport.app.rest.repository.OrderSpecs;
import com.transport.app.rest.repository.UserRepository;
import com.transport.app.rest.repository.UserSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public List<User> findAllByType(String type) {
        return userRepository.findAllByType(type);
    }

    public User findById(Long orderId) {
        return userRepository.findById(orderId).orElseThrow(() -> new NotFoundException(User.class, orderId));
    }

    public List<User> findDriversInCompany(String type, String companyName) {
        return userRepository.findAll(Specification.where(UserSpecs.withType(type))
                .and(UserSpecs.withCompany(companyName)));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User update(User user) {
        User u = findById(user.getId());
        return userRepository.save(UserMapper.toUpdatedUser(u, user));
    }
}
