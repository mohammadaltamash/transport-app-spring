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

//    "SELECT id, ( 3959 * acos( cos( radians(YOUR_LATITUDE) ) * cos( radians( YOUR_DB_LAT_FIELD ) ) * cos( radians( YOUR_DB_LNG_FIELD )\n" +
//            " - radians(YOUR_LONGITUDE) ) + sin( radians(YOUR_LATITUDE) ) * sin( radians( YOUR_DB_LAT_FIELD ) ) ) ) AS distance\n" +
//            " FROM YOUR_DB_TABLE HAVING distance < 25 ORDER BY distance ASC;"
//    @Query("select id, ( 3959 * acos( cos( radians(:refLatitude) ) * cos( radians( pickupLatitude ) ) * cos( radians( pickupLongitude )" +
//            " - radians(:refLongitude) ) + sin( radians(:refLatitude) ) * sin( radians( pickupLatitude ) ) ) ) AS distanc" +
//            " FROM Order having distanc < :distance order by distanc asc")
//    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
//    Example 63
    @Query(value = "SELECT *, ( 3959 * acos( cos( radians(:refLatitude) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )" +
            " - radians(:refLongitude) ) + sin( radians(:refLatitude) ) * sin( radians( pickup_latitude ) ) ) ) AS radiusDistance" +
            " FROM orders having radiusDistance < :distance ORDER BY radiusDistance ASC",
            countQuery = "SELECT count(*) from (SELECT ( 3959 * acos( cos( radians(:refLatitude) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )" +
                    " - radians(:refLongitude) ) + sin( radians(:refLatitude) ) * sin( radians( pickup_latitude ) ) ) ) AS radiusDistance" +
                    " FROM orders having radiusDistance < :distance) as total",
            nativeQuery = true)
//    SELECT count(*) from (select * from orders) as total;
    Page<Order> getInRadius(@Param("refLatitude") double refLatitude, @Param("refLongitude") double refLongitude, @Param("distance") int distance, Pageable pageable);
}
