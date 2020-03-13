package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatus(String orderStatus);
    @Query("select o from Order o where orderStatus in :statuses")
    List<Order> findAllByOrderStatusIn(@Param("statuses") List<String> orderStatusList);
    @Query("select count(*) from Order o where orderStatus = :status")
    int countByOrderStatus(@Param("status") String Status);
}
