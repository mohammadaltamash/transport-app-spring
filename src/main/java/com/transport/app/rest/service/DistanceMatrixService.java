package com.transport.app.rest.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.LatitudeLongitude;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.PagedOrders;
import com.transport.app.rest.mapper.OrderMapper;
import com.transport.app.rest.mapper.PagedOrdersDto;
import com.transport.app.rest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistanceMatrixService {

    @Value("${google.api.key}")
    private String API_KEY;

    @Autowired
    private OrderRepository orderRepository;
//	private static final GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
//
//
//	public DistanceMatrix estimateRouteTime(DateTime time, Boolean isForCalculateArrivalTime, DirectionsApi.RouteRestriction routeRestriction, LatLng departure, LatLng... arrivals) {
//		try {
//			DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
//			if (isForCalculateArrivalTime) {
//				req.departureTime(time);
//			} else {
//				req.arrivalTime(time);
//			}
//			if (routeRestriction == null) {
//				routeRestriction = DirectionsApi.RouteRestriction.TOLLS;
//			}
//			DistanceMatrix trix = req.origins(departure)
//					.destinations(arrivals)
//					.mode(TravelMode.DRIVING)
//					.avoid(routeRestriction)
//					.language("fr-FR")
//					.await();
//			return trix;
//
//		} catch (ApiException e) {
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}
//
//	private static final String API_KEY="YOUR KEY";
//	OkHttpClient client = new OkHttpClient();
//	public String calculate(String source ,String destination) throws IOException {
//		String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+source+"&destinations="+destination+"&key="+ API_KEY;
//		Request request = new Request.Builder()
//				.url(url)
//				.build();
//
//		Response response = client.newCall(request).execute();
//		return response.body().string();
//	}

    //	public static long getDriveDist(String addrOne, String addrTwo) throws ApiException, InterruptedException, IOException{
    public long getDriveDist(double lat1, double long1, double lat2, double long2) throws ApiException, InterruptedException, IOException {

        LatLng source = new LatLng(lat1, long1);
        LatLng destination = new LatLng(lat2, long2);
        //set up key
        GeoApiContext distCalcer = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(distCalcer);
        DistanceMatrix result = req.origins(source)
                .destinations(destination)
                .mode(TravelMode.DRIVING)
//                .avoid(DirectionsApi.RouteRestriction.TOLLS)
//                .units(Unit.IMPERIAL)
                .language("en-US")
                .await();

        int length = result.rows.length;
        DistanceMatrixRow row0 = result.rows[0];
        int elementsLength = row0.elements.length;
        DistanceMatrixElement elements0 = row0.elements[0];
//        long distApart = result.rows[0].elements[0].distance.inMeters;
//        long distApart = elements0.distance.inMeters;
        long distApart = 0;

        if (result.rows[0].elements[0] != null && result.rows[0].elements[0].distance != null) {
            distApart = result.rows[0].elements[0].distance.inMeters;
        }
        return distApart;
    }

    //    SELECT id, ( 3959 * acos( cos( radians(YOUR_LATITUDE) ) * cos( radians( YOUR_DB_LAT_FIELD ) ) * cos( radians( YOUR_DB_LNG_FIELD )
//    - radians(YOUR_LONGITUDE) ) + sin( radians(YOUR_LATITUDE) ) * sin( radians( YOUR_DB_LAT_FIELD ) ) ) ) AS distance
//    FROM YOUR_DB_TABLE HAVING distance < 25 ORDER BY distance ASC;
//    distance in miles
    public PagedOrders getCircularDistance(String type, List<LatitudeLongitude> list, double refLatitude, double refLongitude, int distance, int pageNumber, Integer pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize == null ? Constants.PAGE_SIZE : pageSize);
//        Page<Object> page = Page.empty();
//        if ("pickup".equals(type)) {
//            page = orderRepository.getInRadiusOfPickup(refLatitude, refLongitude, distance, pageable);
//        } else if ("delivery".equals(type)) {
//            page = orderRepository.getInRadiusOfDelivery(refLatitude, refLongitude, distance, pageable);
//        }
//        List<Order> orders = new ArrayList<>();
//        for (Object object : page.getContent()) {
//            Order order = orderRepository.findById(((BigInteger) ((Object[]) object)[0]).longValue()).get();
////            Order order = orderRepository.findById((long) id).get();
//            order.setRadiusPickupDistance((Double) (((Object[]) object)[1]));
//            orders.add(order);
//        }
////        List<Object> content = orders.getContent();
////        page.getContent().clear();
////        page.getContent().addAll(ordrs);
////        orders.getContent().forEach(o -> {
////            Order order = orderRepository.findById((Object[])o[0]);
////        });
//        System.out.println(page.getTotalElements());
//        return PagedOrders.builder().totalItems(page.getTotalElements()).orders(orders).build();
        return orderRepository.getInRadiusOfPickup2(type, list, distance, pageNumber, pageSize);
    }

    public PagedOrders getCircularDistanceBoth(Double pickupLatitude,
                                               Double pickupLongitude,
                                               Double deliveryLatitude,
                                               Double deliveryLongitude, int distance, int pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize == null ? Constants.PAGE_SIZE : pageSize);
        Page<Object> page = orderRepository.getInRadiusOfBoth(pickupLatitude,
                pickupLongitude,
                deliveryLatitude,
                deliveryLongitude, distance, pageable);
        List<Order> orders = new ArrayList<>();
        for (Object object : page.getContent()) {
            Order order = orderRepository.findById(((BigInteger) ((Object[]) object)[0]).longValue()).get();
//            Order order = orderRepository.findById((long) id).get();
            order.setRadiusPickupDistance((Double) (((Object[]) object)[1]));
            orders.add(order);
        }
//        List<Object> content = orders.getContent();
//        page.getContent().clear();
//        page.getContent().addAll(ordrs);
//        orders.getContent().forEach(o -> {
//            Order order = orderRepository.findById((Object[])o[0]);
//        });
        System.out.println(page.getTotalElements());
        return PagedOrders.builder().totalItems(page.getTotalElements()).orders(orders).build();
    }
//    SELECT *, ( 3959 * acos( cos( radians(40.74860) ) * cos( radians( pickup_latitude ) ) * cos( radians( pickup_longitude )
//    - radians(-73.99040) ) + sin( radians(40.74860) ) * sin( radians( pickup_latitude ) ) ) ) AS distance
//    FROM orders HAVING distance < 5 ORDER BY distance ASC;
}
