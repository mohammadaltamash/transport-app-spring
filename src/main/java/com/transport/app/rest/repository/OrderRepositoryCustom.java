package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface OrderRepositoryCustom {
    List<Order> findAllByOrderStatus(Order.ORDER_STATUS orderStatus);
}
