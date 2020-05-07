package com.transport.app.rest.repository;

import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.LatitudeLongitude;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderStatus;
import com.transport.app.rest.domain.PagedOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

//@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findAllByOrderStatus(OrderStatus orderStatus) {
//        Query query = entityManager.createNativeQuery()
        return null;
    }

    @Override
    public PagedOrders getInRadiusOfPickup2(String type, List<LatitudeLongitude> lanLongList, int distance, int page, Integer pageSize) {
//        "SELECT id, ( 3959 * acos( cos( radians(:refLatitude) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )" +
//                " - radians(:refLongitude) ) + sin( radians(:refLatitude) ) * sin( radians( pickup_latitude ) ) ) ) AS radiusPickupDistance" +
//                " FROM orders having radiusPickupDistance < :distance ORDER BY radiusPickupDistance ASC";

        String latitude = null;
        String longitude = null;
        String radiusDistance = null;
        if ("pickup".equals(type)) {
            latitude = "pickup_latitude";
            longitude = "pickup_longitude";
            radiusDistance = "radiusPickupDistance";
        } else if ("delivery".equals(type)) {
            latitude = "delivery_latitude";
            longitude = "delivery_longitude";
            radiusDistance = "radiusDeliveryDistance";
        }
        StringBuilder commonQuery = new StringBuilder();
        StringBuilder selectQuery = new StringBuilder("SELECT id,");
        StringBuilder countQuery = new StringBuilder("SELECT count(*) from (SELECT");

        commonQuery.append(String.format(
                " ( 3959 * acos( cos( radians(:refLatitude1) ) * cos( radians( %s ) ) * cos( radians( %s )" +
                " - radians(:refLongitude1) ) + sin( radians(:refLatitude1) ) * sin( radians( %s ) ) ) ) AS %s1",
                latitude, longitude, latitude, radiusDistance)
        );
        String finalLatitude = latitude;
        String finalLongitude = longitude;
        String finalRadiusDistance = radiusDistance;
        IntStream.range(1, lanLongList.size())
                .forEach(i -> {
                    commonQuery.append(String.format(", ( 3959 * acos( cos( radians(:refLatitude%s) ) * cos( radians( %s ) ) * cos( radians( %s )" +
                                    " - radians(:refLongitude%s) ) + sin( radians(:refLatitude%s) ) * sin( radians( %s ) ) ) ) AS %s"
                            , i + 1, finalLatitude, finalLongitude, i + 1, i + 1, finalLatitude, finalRadiusDistance + (i + 1)));
                });
        commonQuery.append(String.format(" FROM orders having %s1 < :distance", radiusDistance));
        IntStream.range(1, lanLongList.size())
                .forEach(i -> {
                    commonQuery.append(String.format(" or %s < :distance", finalRadiusDistance + (i + 1)));
                });

        selectQuery.append(commonQuery.toString()).append(String.format(" ORDER BY %s1", radiusDistance));
        IntStream.range(1, lanLongList.size())
                .forEach(i -> {
                    selectQuery.append(String.format(", %s", finalRadiusDistance + (i + 1)));
                });
        selectQuery.append(" ASC");

        countQuery.append(commonQuery.toString()).append(") as total");

        Query query = em.createNativeQuery(selectQuery.toString());
        IntStream.range(0, lanLongList.size())
                .forEach(i -> {
                    query.setParameter(String.format("refLatitude%s", i + 1), lanLongList.get(i).getLatitude());
                    query.setParameter(String.format("refLongitude%s", i + 1), lanLongList.get(i).getLongitude());
                    query.setParameter("distance", distance);
                });
        query.setFirstResult(page);
        query.setMaxResults(page + (pageSize == null ? Constants.PAGE_SIZE : pageSize.intValue()));

        List<Order> orders = new ArrayList<>();
        for (Object object : query.getResultList()) {
            Order order = orderRepository.findById(((BigInteger) ((Object[]) object)[0]).longValue()).get();
//            Order order = orderRepository.findById((long) id).get();
            if ("pickup".equals(type)) {
                order.setRadiusPickupDistance((Double) (((Object[]) object)[1]));
            } else if ("delivery".equals(type)) {
                order.setRadiusDeliveryDistance((Double) (((Object[]) object)[1]));
            }
            orders.add(order);
        }
//        List<Object> content = orders.getContent();
//        page.getContent().clear();
//        page.getContent().addAll(ordrs);
//        orders.getContent().forEach(o -> {
//            Order order = orderRepository.findById((Object[])o[0]);
//        });

        Query q = em.createNativeQuery(countQuery.toString());
        IntStream.range(0, lanLongList.size())
                .forEach(i -> {
                    q.setParameter(String.format("refLatitude%s", i + 1), lanLongList.get(i).getLatitude());
                    q.setParameter(String.format("refLongitude%s", i + 1), lanLongList.get(i).getLongitude());
                    q.setParameter("distance", distance);
                });
        int count = ((BigInteger) q.getSingleResult()).intValue();

        return PagedOrders.builder().totalItems(count).orders(orders).build();
    }
}
