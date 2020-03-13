package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

//@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public List<Order> findAllByOrderStatus(Order.ORDER_STATUS orderStatus) {
//        Query query = entityManager.createNativeQuery()
        return null;
    }
}
