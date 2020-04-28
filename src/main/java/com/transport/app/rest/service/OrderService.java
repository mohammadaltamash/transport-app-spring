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
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Order> findAllByOrderStatusIn(String statuses) {
        return orderRepository.findAllByOrderStatusIn(Arrays.stream(statuses.split(","))
                .map(m -> m.trim())
                .collect(Collectors.toList()));
    }

    public int countByOrderStatus(String status) {
        return orderRepository.countByOrderStatus(status);
    }

    public List<Order> findAllPaginated(int page) {
        /*Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, DemoConstants.PAGE_SIZE));
        return OrderMapper.toOrderDtos(orderPage.toList());*/
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, Constants.PAGE_SIZE));
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

    ///////////// Mock
    List<User> users = new ArrayList<>();
    public void generateData() {
//        List<User> users = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        IntStream.range(1, 6).forEach(i -> {
            users.add(generateUser(i));
        });
        users = userRepository.saveAll(users);
        IntStream.range(1, 801).forEach(i -> {
            orders.add(generateOrder(i, users.get(0).getId()));
        });
        IntStream.range(801, 1001).forEach(i -> {
            orders.add(generateOrder(i, users.get(1).getId()));
        });
        orderRepository.saveAll(orders);
    }

    private String[] userTypes = new String[]{"BROKER", "BROKER", "CARRIER", "CARRIER", "DRIVER", "DRIVER"};

    public User generateUser(int i) {
        Pair pair = DistanceMatrixService.getLocation(42.997075, -103.074280, 8000);
        return User.builder()
                .fullName("First" + i + " " + "Second" + i)
                .userName("username" + i)
                .email("email" + i + "@example.com")
                .password(new BCryptPasswordEncoder().encode("abc123"))
                .type(userTypes[i - 1])
                .companyName("User Company" + i)
                .phones(new HashMap<String, String>() {{ put("35725345345", "UserPhones note"); }})
                .latitude((double) pair.getKey())
                .longitude((double) pair.getValue())
                .zip("24234")
                .address("User address " + count())
                .build();
    }
    public Order generateOrder(int i, long userId) {
        Pair pickupPair = DistanceMatrixService.getLocation(42.997075, -103.074280, 9000);
        Pair deliveryPair = DistanceMatrixService.getLocation(42.997075, -103.074280, 10000);
        return Order.builder()
                .brokerOrderId("BrokerOrderId-" + i)
                .pickupAddress("PickupAddress-" + i)
                .pickupZip("12345")
                .pickupPhones(new HashMap<String, String>() {{ put("12833456789", "PickupPhones note"); }})
                .pickupDates(new HashMap<String, Date>() {{ put("begin", new Date()); put("end", new Date()); }})
                .deliveryAddress("DeliveryAddress-" + i)
                .deliveryZip("65835")
                .deliveryPhones(new HashMap<String, String>() {{ put("35725345345", "DeliveryPhones note"); }})
                .deliveryDates(new HashMap<String, Date>() {{ put("begin", new Date()); put("end", new Date()); }})
                .vehicleMake("VehicleMake-" + i)
                .carrierPay(1000d + i)
                .paymentTermBusinessDays("PaymentTermBusinessDays-" + i)
                .paymentMethod("PaymentMethod-" + i)
                .paymentTermBegins("PaymentTermBegins-" + i)
                .brokerCompanyName("BrokerCompanyName-" + i)
                .brokerAddress("BrokerAddress-" + i)
                .brokerZip("96445")
                .shipperPhones(new HashMap<String, String>() {{ put("12382456789", "ShipperPhones note"); }})
                .brokerEmail("email" + i + "@example.com")
                .pickupLatitude((double) pickupPair.getKey())
                .pickupLongitude((double) pickupPair.getValue())
                .deliveryLatitude((double) deliveryPair.getKey())
                .deliveryLongitude((double) deliveryPair.getValue())
                .createdBy(userRepository.findById(userId).get())
                .build();
    }
}
