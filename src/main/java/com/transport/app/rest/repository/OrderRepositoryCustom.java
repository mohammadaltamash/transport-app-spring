package com.transport.app.rest.repository;

import com.transport.app.rest.domain.*;

import java.util.List;

//@Repository
public interface OrderRepositoryCustom {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
    PagedOrders getInRadius(LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs, String inQuery, String orderQuery, int page, Integer pageSize);
}
