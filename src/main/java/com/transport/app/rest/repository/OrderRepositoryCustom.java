package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderStatus;

import java.util.List;

//@Repository
public interface OrderRepositoryCustom {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
