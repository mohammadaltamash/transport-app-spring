package com.transport.app.rest.repository;

import com.transport.app.rest.domain.LatitudeLongitude;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderStatus;
import com.transport.app.rest.domain.PagedOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
public interface OrderRepositoryCustom {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
    PagedOrders getInRadiusOfPickup2(String type, List<LatitudeLongitude> lanLongList, int distance, int page, Integer pageSize);
}
