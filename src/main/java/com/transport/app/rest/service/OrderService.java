package com.transport.app.rest.service;

import com.google.maps.errors.ApiException;
import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.*;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.OrderMapper;
import com.transport.app.rest.repository.OrderCarrierRepository;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.repository.OrderSpecs;
import com.transport.app.rest.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserAuthRepository userRepository;
    @Autowired
    private OrderCarrierRepository orderCarrierRepository;

    @Autowired
    private DistanceMatrixService distanceMatrixService;

//    @PersistenceContext
//    private EntityManager em;

//    @Autowired
//    private IAuthenticationFacade authenticationFacade;

    public Order create(Order order, User createdBy) throws InterruptedException, ApiException, IOException {
        /*Order order = orderRepository.save(OrderMapper.toOrder(orderDto));
        return OrderMapper.toOrderDto(order);*/
        order.setCreatedBy(createdBy);
        order.setCreatedByName(createdBy.getFullName());
//        order.setUpdatedBy(createdBy);
//        ArrayList<Order> userOrders = new ArrayList<>();
//        userOrders.add(order);
//        createdBy.setOrders(userOrders);
        long distance = distanceMatrixService.getDriveDist(order.getPickupLatitude(), order.getPickupLongitude(), order.getDeliveryLatitude(), order.getDeliveryLongitude());
        order.setDistance(distance);
        Order ordr = orderRepository.save(order);
        return ordr;
    }

    public Order update(Order order) throws InterruptedException, ApiException, IOException {
        /*OrderDto oDto = findById(orderDto.getId());
        Order order = orderRepository.save(OrderMapper.toOrder(oDto));
        return OrderMapper.toOrderDto(order);*/
        Order o = findById(order.getId());
        if (order.getPickupZip() != null || order.getDeliveryZip() != null) {
            long distance = distanceMatrixService.getDriveDist(order.getPickupLatitude(), order.getPickupLongitude(), order.getDeliveryLatitude(), order.getDeliveryLongitude());
            order.setDistance(distance);
        }
        return orderRepository.save(OrderMapper.toUpdatedOrder(o, order));
    }

//    public Order recordBookingRequest(Long orderId, String email) {
//    public OrderCarrier recordBookingRequest(Order order, User user) {
//        List<OrderCarrier> carriers = order.getBookingRequestCarriers();
//        OrderCarrier orderCarrier = new OrderCarrier();
//        orderCarrier.setOrder(order);
//        orderCarrier.setCarrier(user);
//        orderCarrier.setStatus(OrderStatus.BOOKING_REQUEST.getName());
////        ArrayList<Order> userOrders = new ArrayList<>();
////        List<OrderCarrier> userBookingRequests = user.getBookingRequestOrders();
////        em.merge(user);
////        em.merge(order);
//        return orderCarrierRepository.save(orderCarrier);
////        carriers.add(orderCarrier);
////        order.setBookingRequestCarriers(carriers);
////        em.merge(order);
////        return orderRepository.save(order);
////        return orderRepository.findById(order.getId()).get();
////        return null;
//    }

    public OrderCarrier recordBookingRequest(OrderCarrier orderCarrier, Order order, User user) {
        orderCarrier.setStatus(OrderStatus.BOOKING_REQUEST.getName());
        orderCarrier.setOrder(order);
        orderCarrier.setCarrier(user);
        return orderCarrierRepository.save(orderCarrier);
    }

    public void bookOrder(OrderCarrier orderCarrier, Long orderId) {
//        Order order = findById(orderId);
//        order.setOrderStatus(OrderStatus.BOOKED.getName());
//        orderRepository.save(order);
        orderCarrier.setStatus(OrderStatus.BOOKED.getName());
        updateOrderCarrier(orderCarrier);
    }

    private void updateOrderCarrier(OrderCarrier orderCarrier) {
        OrderCarrier oc = orderCarrierRepository.findById(orderCarrier.getId()).orElseThrow(() -> new NotFoundException(OrderCarrier.class, orderCarrier.getId()));
        oc.setStatus(orderCarrier.getStatus());
        oc.setCarrierPay(orderCarrier.getCarrierPay());
        oc.setDaysToPay(orderCarrier.getDaysToPay());
        oc.setPaymentTermBegins(orderCarrier.getPaymentTermBegins());
        oc.setCommittedPickupDate(orderCarrier.getCommittedPickupDate());
        oc.setCommittedDeliveryDate(orderCarrier.getCommittedDeliveryDate());
        oc.setOfferReason(orderCarrier.getOfferReason());
        oc.setOfferValidity(orderCarrier.getOfferValidity());
        orderCarrierRepository.save(oc);
    }

    public OrderCarrier acceptOrDecline(Long orderId, Long orderCarrierId, String acceptOrDecline) {
//        Order order = findById(orderId);
        OrderCarrier orderCarrier = orderCarrierRepository.findById(orderCarrierId).orElseThrow(() -> new NotFoundException(OrderCarrier.class, orderCarrierId));
        if (OrderStatus.DECLINED.getName().equals(acceptOrDecline)) {
//            order.setOrderStatus(OrderStatus.NEW.getName());
//            orderRepository.save(order);
            orderCarrier.setStatus(OrderStatus.DECLINED.getName());
            orderCarrierRepository.save(orderCarrier);
        } else if (OrderStatus.ACCEPTED.getName().equals(acceptOrDecline)) {
            Order order = findById(orderId);
            order.setOrderStatus(OrderStatus.ACCEPTED.getName());
            User carrier = orderCarrier.getCarrier();
            order.setAssignedToCarrier(carrier);
            order.setAssignedToCarrierName(carrier.getFullName()); // For search
            orderRepository.save(order);
            orderCarrier.setStatus(OrderStatus.ACCEPTED.getName());
            orderCarrierRepository.save(orderCarrier);
//            carrier.getAssignedOrdersAsCarrier().add(order);
//            userRepository.save(carrier);
        }
        return orderCarrier;
    }

    public Order assignDriver(Long driverId, Long orderId) {
        User driver = userRepository.findById(driverId).orElseThrow(() -> new NotFoundException(User.class, driverId));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(Order.class, orderId));
        order.setAssignedToDriver(driver);
        order.setAssignedToDriverName(driver.getFullName()); // For search
        return orderRepository.save(order);
    }

    public Order findById(Long orderId) {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
        /*Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(Order.class, orderId));
        return OrderMapper.toOrderDto(order);*/
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(Order.class, orderId));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByOrderStatus(String status) {
        return orderRepository.findAllByOrderStatus(status);
    }

//    public List<Order> findAllByOrderStatusIn(String statuses) {
//        return orderRepository.findAllByOrderStatusIn(Arrays.stream(statuses.split(","))
//                .map(m -> m.trim())
//                .collect(Collectors.toList()));
//    }

    public Page<Order> findAllByOrderStatusInPaginated(String statuses, int page, Integer pageSize) {
//        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
//                Sort.by(Sort.Direction.DESC, "updatedAt"));
//        return orderRepository.findAllByOrderStatusIn(
//                Arrays.stream(statuses.split(","))
//                        .map(m -> m.trim())
//                        .collect(Collectors.toList()), pageable);

        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.findAll(Specification.where(OrderSpecs.withStatuses(statuses)), pageable);
    }

    public Page<Order> findAllByStatesInPaginated(String pickupState, String deliveryStates, int page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
                Sort.by(Sort.Direction.DESC, "updatedAt"));
        return orderRepository.findAll(Specification.where(OrderSpecs.withPickupStates(pickupState))
                .and(OrderSpecs.withDeliveryStates(deliveryStates)), pageable);
    }

    public Page<Order> searchOrders(String statuses, String searchText, int page, Integer pageSize) {
        /*SearchKeyword keyword = SearchKeyword.get(searchKeyword);
        switch(keyword) {
            case ORDER_ID:
                return orderRepository.findAllById(Collections.singleton(Long.parseLong(searchText)));
            case DRIVER:
                return orderRepository.findByAssignedToDriverNameContainingIgnoreCase(searchText);
            case MAKE:
                return orderRepository.findByVehicleMakeContainingIgnoreCase(searchText);
            case MODEL:
                return orderRepository.findByVehicleModelContainingIgnoreCase(searchText);
            default:
                return null;
        }*/
/*        List<Order> orders = new ArrayList<>();
        if (StringUtils.isNumeric(searchText)) {
            orders.addAll(orderRepository.findAllById(Collections.singleton(Long.parseLong(searchText))));
        }
        orders.addAll(orderRepository.findByAssignedToDriverNameContainingIgnoreCase(searchText));
        orders.addAll(orderRepository.findByVehicleMakeContainingIgnoreCase(searchText));
        orders.addAll(orderRepository.findByVehicleModelContainingIgnoreCase(searchText));
        Collections.sort(orders);

        return orders;*/
        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
                Sort.by(Sort.Direction.DESC, "updatedAt"));
        if (OrderStatus.contains(statuses)) {
            return orderRepository.findAll(Specification
                    .where(OrderSpecs.withStatuses(statuses))
                    .and(OrderSpecs.textInAllColumns(searchText)), pageable);
        } else {
            return orderRepository.findAll(Specification.where(OrderSpecs.textInAllColumns(searchText)), pageable);
        }
    }

    /*public int searchOrdersCount(String searchKeyword, String searchText) {

//        orderRepository.count(Specification.where(OrderSpecs.textInAllColumns(searchText)));

        return orderRepository.findAll(Specification.where(OrderSpecs.textInAllColumns(searchText)), Pageable.unpaged()).size();
    }*/

    public int countByOrderStatusIn(String statuses) {
        return orderRepository.countByOrderStatusIn(Arrays.stream(statuses.split(","))
                .map(m -> m.trim())
                .collect(Collectors.toList()));
    }

    public Page<Order> findAllPaginated(int page, Integer pageSize) {
        /*Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, DemoConstants.PAGE_SIZE));
        return OrderMapper.toOrderDtos(orderPage.toList());*/
        return orderRepository.findAll(PageRequest.of(
                page, pageSize == null ? Constants.PAGE_SIZE : pageSize, Sort.by(Sort.Direction.DESC, "updatedAt")));
//        return orderPage.toList();
    }

//    public PagedOrders getCircularDistance(LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs, int page, Integer pageSize) {
//        return distanceMatrixService.getCircularDistance(latitudeLongitudeDistanceRefs, page, pageSize);
//    }

    public PagedOrders getFilteredOrders(LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs, String originStatesCsv,
                                         String destinationStatesCsv, int page, Integer pageSize) {
        List<LatitudeLongitudeDistance> pickupRefLatLongList = latitudeLongitudeDistanceRefs.getPickupLatLongs();
        List<LatitudeLongitudeDistance> deliveryRefLatLongList = latitudeLongitudeDistanceRefs.getDeliveryLatLongs();
        Page<Order> orderPage = Page.empty();
        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        if ((pickupRefLatLongList != null && pickupRefLatLongList.size() > 0 ||
                deliveryRefLatLongList != null && deliveryRefLatLongList.size() > 0) && (!"null".equals(originStatesCsv) || !"null".equals(destinationStatesCsv))) {

            List<String> originStates = Collections.EMPTY_LIST;
            List<String> destinationStates = Collections.EMPTY_LIST;
            if (!"null".equals(originStatesCsv)) {
                originStates = Arrays.stream(originStatesCsv.split(","))
                                                                  .map(m -> m.trim())
                                                                  .collect(Collectors.toList());
            }
            if (!"null".equals(destinationStatesCsv)) {
                destinationStates = Arrays.stream(destinationStatesCsv.split(","))
                                                                  .map(m -> m.trim())
                                                                  .collect(Collectors.toList());
            }
            String inQuery;
            if (!originStates.isEmpty() && !destinationStates.isEmpty()) {
                inQuery = String.format("where pickup_address_state in (%s)", "'" + String.join("', '", originStates.toArray(new String[0])) + "'")
                            + String.format(" and delivery_address_state in (%s)", "'" + String.join("', '", destinationStates.toArray(new String[0])) + "'");
            } else if (!originStates.isEmpty()) {
                inQuery = String.format("where pickup_address_state in (%s)", "'" + String.join("', '", originStates.toArray(new String[0])) + "'");
            } else {
                inQuery = String.format("where delivery_address_state in (%s)", "'" + String.join("', '", destinationStates.toArray(new String[0])) + "'");
            }
            return distanceMatrixService.getCircularDistance(latitudeLongitudeDistanceRefs, inQuery, page, pageSize);
        } else if ((pickupRefLatLongList != null && pickupRefLatLongList.size() > 0) ||
                (deliveryRefLatLongList != null && deliveryRefLatLongList.size() > 0)) {
            return distanceMatrixService.getCircularDistance(latitudeLongitudeDistanceRefs, null, page, pageSize);
        } else {
            if (!"null".equals(originStatesCsv) && !"null".equals(destinationStatesCsv)) {
                orderPage = orderRepository.findAll(Specification.where(OrderSpecs.withPickupStates(originStatesCsv))
                        .and(OrderSpecs.withDeliveryStates(destinationStatesCsv)), pageable);
            } else if (!"null".equals(originStatesCsv)) {
                orderPage = orderRepository.findAll(Specification.where(OrderSpecs.withPickupStates(originStatesCsv)), pageable);
            } else if (!"null".equals(destinationStatesCsv)) {
                orderPage = orderRepository.findAll(Specification.where(OrderSpecs.withDeliveryStates(destinationStatesCsv)), pageable);
            }
        }

        return PagedOrders.builder().totalItems(orderPage.getTotalElements()).orders(orderPage.getContent()).build();
//        return distanceMatrixService.getCircularDistance(latitudeLongitudeDistanceRefs, page, pageSize);
    }

    /*public PagedOrders getCircularDistanceBoth(Double pickupLatitude,
                                               Double pickupLongitude,
                                               Double deliveryLatitude,
                                               Double deliveryLongitude, int distance, int page, Integer pageSize) {
        return distanceMatrixService.getCircularDistanceBoth(pickupLatitude,
                pickupLongitude,
                deliveryLatitude,
                deliveryLongitude, distance, page, pageSize);
    }*/

    public void deleteById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresent(order -> orderRepository.delete(orderOptional.get()));
        if (orderOptional.isPresent()) {
            orderRepository.delete(orderOptional.get());
        }
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    public long count() {
        return orderRepository.count();
    }

    public OrderCarrier findOrderCarrierById(Long orderCarrierId) {
        return orderCarrierRepository.findById(orderCarrierId).orElseThrow(() -> new NotFoundException(OrderCarrier.class, orderCarrierId));
    }
}
