package com.transport.app.rest.repository;

import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, Long> {}