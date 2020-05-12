package com.transport.app.rest.repository;

import com.transport.app.rest.domain.*;

import java.util.List;

//@Repository
public interface OrderRepositoryCustom {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
    PagedOrders getInRadiusOfPickup2(LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs, int page, Integer pageSize);
}
