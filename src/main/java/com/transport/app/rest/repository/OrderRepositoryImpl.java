package com.transport.app.rest.repository;

import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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

//    @Override
   /* public PagedOrders getInRadiusOfPickup2(String type, List<LatitudeLongitude> lanLongList, int distance, int page, Integer pageSize) {
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
            if ("pickup".equals(type)) {
                order.setRadiusPickupDistance((Double) (((Object[]) object)[1]));
            } else if ("delivery".equals(type)) {
                order.setRadiusDeliveryDistance((Double) (((Object[]) object)[1]));
            }
            orders.add(order);
        }

        Query q = em.createNativeQuery(countQuery.toString());
        IntStream.range(0, lanLongList.size())
                .forEach(i -> {
                    q.setParameter(String.format("refLatitude%s", i + 1), lanLongList.get(i).getLatitude());
                    q.setParameter(String.format("refLongitude%s", i + 1), lanLongList.get(i).getLongitude());
                    q.setParameter("distance", distance);
                });
        int count = ((BigInteger) q.getSingleResult()).intValue();

        return PagedOrders.builder().totalItems(count).orders(orders).build();
    }*/

    @Override
    public PagedOrders getInRadiusOfPickup2(LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs, String inQuery, int page, Integer pageSize) {
//        "SELECT id, ( 3959 * acos( cos( radians(:refLatitude) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )" +
//                " - radians(:refLongitude) ) + sin( radians(:refLatitude) ) * sin( radians( pickup_latitude ) ) ) ) AS radiusPickupDistance" +
//                " FROM orders having radiusPickupDistance < :distance ORDER BY radiusPickupDistance ASC";

        List<LatitudeLongitudeDistance> pickupRefLatLongList = latitudeLongitudeDistanceRefs.getPickupLatLongs();
        List<LatitudeLongitudeDistance> deliveryRefLatLongList = latitudeLongitudeDistanceRefs.getDeliveryLatLongs();
        if ((pickupRefLatLongList == null || pickupRefLatLongList.size() == 0) &&
                (deliveryRefLatLongList == null || deliveryRefLatLongList.size() == 0)) {
            return PagedOrders.builder().totalItems(0).orders(Collections.emptyList()).build();
        }
        String latitude = null;
        String longitude = null;
        String radiusDistance = null;

        StringBuilder commonQuery = new StringBuilder();
        StringBuilder selectQuery = new StringBuilder("SELECT id,");
        StringBuilder countQuery = new StringBuilder("SELECT count(*) from (SELECT");

        // First alias
        if (!pickupRefLatLongList.isEmpty()) {
            commonQuery.append(" ( 3959 * acos( cos( radians(:refLatitude1) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )" +
                    " - radians(:refLongitude1) ) + sin( radians(:refLatitude1) ) * sin( radians( pickup_latitude ) ) ) ) AS radiusPickupDistance1");
        } else if (!deliveryRefLatLongList.isEmpty()) {
            commonQuery.append(" ( 3959 * acos( cos( radians(:refLatitude1) ) * cos( radians( delivery_latitude ) ) * cos( radians( delivery_longitude )" +
                    " - radians(:refLongitude1) ) + sin( radians(:refLatitude1) ) * sin( radians( delivery_latitude ) ) ) ) AS radiusDeliveryDistance1");
        }

        String finalLatitude = latitude;
        String finalLongitude = longitude;
        String finalRadiusDistance = radiusDistance;

        if (!pickupRefLatLongList.isEmpty()) {
            appendAlias(commonQuery, "pickup", pickupRefLatLongList, true);
        }
        if (!deliveryRefLatLongList.isEmpty()) {
            appendAlias(commonQuery, "delivery", deliveryRefLatLongList, pickupRefLatLongList.isEmpty() ? true : false);
        }

//      (or origin) and (or destination)
        commonQuery.append(String.format(" FROM orders %s having", inQuery != null ? inQuery : ""));
        if (!pickupRefLatLongList.isEmpty()) {
            appendHavingCondition(commonQuery, "pickup", pickupRefLatLongList.size(), 0);
            if (!deliveryRefLatLongList.isEmpty()) {
                commonQuery.append(" and ");
            }
        }
        if (!deliveryRefLatLongList.isEmpty()) {
            appendHavingCondition(commonQuery, "delivery", deliveryRefLatLongList.size(), pickupRefLatLongList.size());
        }
        //////////////////////
        /*selectQuery.append(commonQuery.toString()).append(" ORDER BY ");
        if (!pickupRefLatLongList.isEmpty()) {
            selectQuery.append("radiusPickupDistance1");
        } else if (!deliveryRefLatLongList.isEmpty()) {
            selectQuery.append("radiusDeliveryDistance1");
        }
        if (!pickupRefLatLongList.isEmpty()) {
            addOrderBy(selectQuery, "pickup", pickupRefLatLongList, true);
        }
        if (!deliveryRefLatLongList.isEmpty()) {
            addOrderBy(selectQuery, "delivery", deliveryRefLatLongList, pickupRefLatLongList.isEmpty() ? true : false);
        }
        selectQuery.append(" ASC");*/
        //////////////////////

        countQuery.append(commonQuery.toString()).append(") as total");

        Query query = em.createNativeQuery(selectQuery.toString());
        bindParameters(query, pickupRefLatLongList, 0);
        bindParameters(query, deliveryRefLatLongList, pickupRefLatLongList.size());
        int pSize = pageSize == null ? Constants.PAGE_SIZE : pageSize.intValue();
        query.setFirstResult(pSize * page);
//        query.setMaxResults(page + (pageSize == null ? Constants.PAGE_SIZE : pageSize.intValue()));
        query.setMaxResults(pSize);

        List<Order> orders = new ArrayList<>();
        for (Object object : query.getResultList()) {
            Order order = orderRepository.findById(((BigInteger) ((Object[]) object)[0]).longValue()).get();
            orders.add(order);
        }

        Query q = em.createNativeQuery(countQuery.toString());
        bindParameters(q, pickupRefLatLongList, 0);
        bindParameters(q, deliveryRefLatLongList, pickupRefLatLongList.size());
        int count = ((BigInteger) q.getSingleResult()).intValue();

        return PagedOrders.builder().totalItems(count).orders(orders).build();
    }

    private void appendAlias(StringBuilder commonQuery, String type, List<LatitudeLongitudeDistance> latLongList, boolean skipFirst) {
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
        String finalLatitude = latitude;
        String finalLongitude = longitude;
        String finalRadiusDistance = radiusDistance;
        IntStream.range(skipFirst ? 1 : 0, latLongList.size())
                .forEach(i -> {
                    commonQuery.append(String.format(", ( 3959 * acos( cos( radians(:refLatitude%s) ) * cos( radians( %s ) ) * cos( radians( %s )" +
                                    " - radians(:refLongitude%s) ) + sin( radians(:refLatitude%s) ) * sin( radians( %s ) ) ) ) AS %s"
                            , i + 1, finalLatitude, finalLongitude, i + 1, i + 1, finalLatitude, finalRadiusDistance + (i + 1)));
                });
    }

    private void appendHavingCondition(StringBuilder commonQuery, String type, int aliasCount, int distanceIndexIncrement) {
        String radiusDistance = null;
        if ("pickup".equals(type)) {
            radiusDistance = "radiusPickupDistance";
        } else if ("delivery".equals(type)) {
            radiusDistance = "radiusDeliveryDistance";
        }
        String finalRadiusDistance = radiusDistance;

        if (aliasCount > 1) {
            commonQuery.append("(");
        }
        commonQuery.append(String.format(" %s1 < :distance%s", radiusDistance, distanceIndexIncrement + 1));
        IntStream.range(1, aliasCount)
                .forEach(i -> {
                    commonQuery.append(String.format(" or %s < :distance%s", finalRadiusDistance + (i + 1), (i + 1 + distanceIndexIncrement)));
                });
        if (aliasCount > 1) {
            commonQuery.append(")");
        }
    }

    private void addOrderBy(StringBuilder selectQuery, String type, List<LatitudeLongitudeDistance> latLongList, boolean skipFirst) {
        String radiusDistance = null;
        if ("pickup".equals(type)) {
            radiusDistance = "radiusPickupDistance";
        } else if ("delivery".equals(type)) {
            radiusDistance = "radiusDeliveryDistance";
        }

        String finalRadiusDistance = radiusDistance;
        IntStream.range(skipFirst ? 1 : 0, latLongList.size())
                .forEach(i -> {
                    selectQuery.append(String.format(", %s", finalRadiusDistance + (i + 1)));
                });
    }

    private void bindParameters(Query q, List<LatitudeLongitudeDistance> refLatLongList, int distanceIndexIncrement) {
        IntStream.range(0, refLatLongList.size())
                .forEach(i -> {
                    q.setParameter(String.format("refLatitude%s", i + 1), refLatLongList.get(i).getLatitude());
                    q.setParameter(String.format("refLongitude%s", i + 1), refLatLongList.get(i).getLongitude());
//                    q.setParameter("distance", distance);
                    q.setParameter(String.format("distance%s", i + 1 + distanceIndexIncrement), refLatLongList.get(i).getDistance());
                });
    }
}
