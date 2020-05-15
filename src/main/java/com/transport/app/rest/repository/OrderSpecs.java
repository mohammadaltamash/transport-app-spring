package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

public class OrderSpecs {

    public static Specification<Order> withStatuses(String statuses) {

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("orderStatus")).value(
                        Arrays.stream(statuses.split(","))
                        .map(m -> m.trim())
                        .collect(Collectors.toList()
                ));
            }
        };
    }

    public static Specification<Order> withPickupStates(String states) {

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("pickupAddressState")).value(
                        Arrays.stream(states.split(","))
                        .map(m -> m.trim())
                        .collect(Collectors.toList()
                ));
            }
        };
    }

    public static Specification<Order> withDeliveryStates(String states) {

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("deliveryAddressState")).value(
                        Arrays.stream(states.split(","))
                        .map(m -> m.trim())
                        .collect(Collectors.toList()
                ));
            }
        };
    }

    public static Specification<Order> fieldEqualTo(Map<String, Object> map) {

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public static Specification<Order> fieldGreaterThanEqualTo(Map<String, String> map) {

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(entry.getKey()), entry.getValue()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public static Specification<Order> textInAllColumns(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(root.getModel().getDeclaredSingularAttributes().stream().filter(a -> {
                            if (a.getJavaType().getSimpleName().equalsIgnoreCase("string")) {
                                return true;
                            } else {
                                return false;
                            }
                        }).map(a -> criteriaBuilder.like(root.get(a.getName()), finalText)
                        ).toArray(Predicate[]::new)
                );
            }
        };
    }
}
