package com.transport.app.rest.repository;

import com.transport.app.rest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByResetToken(String resetToken);
}