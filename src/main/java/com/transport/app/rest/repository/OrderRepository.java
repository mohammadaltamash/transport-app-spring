package com.transport.app.rest.repository;

import com.transport.app.rest.domain.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatus(String orderStatus);
    @Query("select o from Order o where orderStatus in :statuses")
    Page<Order> findAllByOrderStatusIn(@Param("statuses") List<String> orderStatusList, Pageable pageable);
    @Query("select count(*) from Order o where orderStatus in :statuses")
    int countByOrderStatusIn(@Param("statuses") List<String> orderStatusList);
//    @Query("select o from Order o where assignedToDriver like :statuses")
    List<Order> findByAssignedToDriverNameContainingIgnoreCase(String driver);
    List<Order> findByVehicleMakeContainingIgnoreCase(String vehicleMake);
    List<Order> findByVehicleModelContainingIgnoreCase(String vehicleModel);

//    @Query("select o from Order o where id = ?1 or o.brokerOrderId = ?2 or o.assignedToDriverName like ?3 or o.vehicleMake like ?4 or o.vehicleModel like ?5")
//    List<Order> searchOrders(List<String> orderStatusList, String id, String brokerOrderId, String assignedToDriverName, String);
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);
}
