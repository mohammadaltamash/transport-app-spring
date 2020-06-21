package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByResetToken(String resetToken);
    List<User> findAllByType(String type);
    List<User> findAll(Specification<User> spec);
}