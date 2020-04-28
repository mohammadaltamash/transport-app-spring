package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderStatus;

import java.util.List;

//@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public List<Order> findAllByOrderStatus(OrderStatus orderStatus) {
//        Query query = entityManager.createNativeQuery()
        return null;
    }
}
