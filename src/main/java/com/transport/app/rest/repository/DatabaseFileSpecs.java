package com.transport.app.rest.repository;

import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.domain.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DatabaseFileSpecs {

    public static Specification<DatabaseFile> withFileType(String fileType) {

        return new Specification<DatabaseFile>() {
            @Override
            public Predicate toPredicate(Root<DatabaseFile> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("fileType"), fileType);
            }
        };
    }
}
