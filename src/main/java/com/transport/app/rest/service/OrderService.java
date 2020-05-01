package com.transport.app.rest.service;

import com.google.maps.errors.ApiException;
import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderCarrier;
import com.transport.app.rest.domain.OrderStatus;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.OrderMapper;
import com.transport.app.rest.repository.OrderCarrierRepository;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public List<Order> findAllByOrderStatusInPaginated(String statuses, int page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize == null ? Constants.PAGE_SIZE : pageSize,
                Sort.by(Sort.Direction.DESC, "updatedAt"));
        return orderRepository.findAllByOrderStatusIn(
                Arrays.stream(statuses.split(","))
                        .map(m -> m.trim())
                        .collect(Collectors.toList()), pageable);
    }

    public int countByOrderStatusIn(String statuses) {
        return orderRepository.countByOrderStatusIn(Arrays.stream(statuses.split(","))
                .map(m -> m.trim())
                .collect(Collectors.toList()));
    }

    public List<Order> findAllPaginated(int page, Integer pageSize) {
        /*Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, DemoConstants.PAGE_SIZE));
        return OrderMapper.toOrderDtos(orderPage.toList());*/
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(
                page, pageSize == null ? Constants.PAGE_SIZE : pageSize, Sort.by(Sort.Direction.DESC, "updatedAt")));
        return orderPage.toList();
    }

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
