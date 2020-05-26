package com.transport.app.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.errors.ApiException;
import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.*;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.*;
import com.transport.app.rest.service.OrderService;
import com.transport.app.rest.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
//        (origins = "*", allowedHeaders = "*", maxAge = 1800 * 2 * 24)
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;
    private UserService userService;

    OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public OrderDto create(@Valid @RequestBody Order order, Authentication authentication) throws InterruptedException, ApiException, IOException {
//        OrderDto orderDto = OrderMapper.toOrderDto(order);
        /*Map<String, String> pickupPhones = new HashMap<>();
        pickupPhones.put("Phone 1", "abcd");
        pickupPhones.put("Phone 2", "xyz");
        order.setPickupPhones(pickupPhones);*/
//        Map<String, String> map = new HashMap<>();
//        map.put("1234560", "Phone 1 notes");
//        map.put("2423424", "Phone 2 notes");
//        order.getPickupPhones().putAll(map);
//        deleteAll();
        String userName = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User createdBy = userService.findByEmail(userName);
        if (createdBy == null) {
            throw new NotFoundException(User.class, userName);
        }
        return OrderMapper.toOrderDto(orderService.create(order, createdBy));
    }

    @PutMapping("/update")
//    public OrderDto update(@RequestBody Order order) {
    public void update(@RequestBody Order order) throws InterruptedException, ApiException, IOException {
//        OrderDto orderDto = OrderMapper.toOrderDto(order);
//        return OrderMapper.toOrderDto(orderService.update(order));
        orderService.update(order);
    }

    @GetMapping("/todays/{field}")
    public List<OrderDto> getTodaysOrders(@PathVariable("field") String field) {
        return OrderMapper.toOrderDtos(orderService.getTodaysOrders(field));
    }

    /*@PostMapping("/bookingrequest/{orderId}/{email}")
    public OrderCarrierDto recordBookingRequest(@PathVariable("orderId") Long orderId, @PathVariable("email") String email) {
        Order order = orderService.findById(orderId);
        User user = userService.findByEmail(email);
        return OrderCarrierMapper.toOrderCarrierDto(orderService.recordBookingRequest(order, user));
    }*/

    @PostMapping("/bookingrequest/{orderId}/{email}")
    public OrderCarrierDto recordBookingRequest(@Valid @RequestBody OrderCarrier orderCarrier, @PathVariable("orderId") Long orderId, @PathVariable("email") String email) {
        Order order = orderService.findById(orderId);
        User user = userService.findByEmail(email);
        return OrderCarrierMapper.toOrderCarrierDto(orderService.recordBookingRequest(orderCarrier, order, user));
    }

    @PutMapping("/bookingrequest/book/{orderId}/{carrierId}")
    public OrderCarrierDto bookOrder(@RequestBody OrderCarrier orderCarrier, @PathVariable("orderId") Long orderId, @PathVariable("carrierId") Long carrierId) {
        return OrderCarrierMapper.toOrderCarrierDto(orderService.bookOrder(orderCarrier, orderId, carrierId));
    }

    @PutMapping("/bookingrequest/{orderId}/{orderCarrierId}/{acceptOrDecline}")
    public OrderCarrier acceptOrDecline(@PathVariable("orderId") Long orderId,
                                        @PathVariable("orderCarrierId") Long orderCarrierId,
                                        @PathVariable("acceptOrDecline") String acceptOrDecline) {
        return orderService.acceptOrDecline(orderId, orderCarrierId, acceptOrDecline);
    }

    @PutMapping("/assigndriver/{driverId}/{orderId}")
    public OrderDto assignDriver(
            @PathVariable("driverId") Long driverId,
            @PathVariable("orderId") Long orderId) {
        return OrderMapper.toOrderDto(orderService.assignDriver(driverId, orderId));
    }

    @GetMapping("/ordercarrier/{id}")
    public OrderCarrier getOrderCarrier(@PathVariable("orderId") Long id) {
        return orderService.findOrderCarrierById(id);
    }

    @GetMapping("/get/{id}")
    public OrderDto findById(@PathVariable("id") long orderId) {
        return OrderMapper.toOrderDto(orderService.findById(orderId));
    }

    @GetMapping("/get")
    public List<OrderDto> findAll() {
        return OrderMapper.toOrderDtos(orderService.findAll());
    }

    @GetMapping("/get/status/{status}")
    public List<OrderDto> findAllByOrderStatus(@PathVariable("status") String status) {
        return OrderMapper.toOrderDtos(orderService.findAllByOrderStatus(status));
    }

    @GetMapping("/get/statusin/{statuses}/{primarysort}/{secondarysort}/{page}/{pagesize}")
    public PagedOrdersDto findAllByOrderStatuses(@PathVariable("statuses") String statuses,
                                                 @PathVariable("primarysort") String primarySort,
                                                 @PathVariable("secondarysort") String secondarySort,
                                                 @PathVariable("page") int pageNumber,
                                                 @PathVariable("pagesize") Integer pageSize) {
//        return OrderMapper.toOrderDtos(orderService.findAllByOrderStatusInPaginated(statuses, page, pageSize));
        primarySort = primarySort.equals("null") ? null : primarySort;
        secondarySort = secondarySort.equals("null") ? null : secondarySort;
        Sort sortBy = Sort.by(Sort.Direction.DESC, primarySort == null ? "createdAt" : primarySort);
        if (secondarySort != null) {
            sortBy.and(Sort.by(Sort.Direction.DESC, secondarySort));
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize == null ? Constants.PAGE_SIZE : pageSize, sortBy);
        Page<Order> page = orderService.findAllByOrderStatusInPaginated(statuses, pageNumber, pageSize, pageable);
        return PagedOrdersDto.builder()
                .totalItems(page.getTotalElements())
                .orders(OrderMapper.toOrderDtos(page.getContent()))
                .build();
    }

//    @GetMapping("/get/statesin/{pickupstates}/{deliverystates}/{page}/{pagesize}")
//    public PagedOrdersDto findAllByStatesInPaginated(@PathVariable("pickupstates") String pickupStates,
//                                                     @PathVariable("deliverystates") String deliveryStates,
//                                                 @PathVariable("page") int pageNumber,
//                                                 @PathVariable("pagesize") Integer pageSize) {
////        return OrderMapper.toOrderDtos(orderService.findAllByOrderStatusInPaginated(statuses, page, pageSize));
//        Page<Order> page = orderService.findAllByStatesInPaginated(pickupStates, deliveryStates, pageNumber, pageSize);
//        return PagedOrdersDto.builder()
//                .totalItems(page.getTotalElements())
//                .orders(OrderMapper.toOrderDtos(page.getContent()))
//                .build();
//    }

//    @GetMapping("/get/statusin/{statuses}/{pagesize}")
//    public List<OrderDto> findAllByOrderStatusesPaginated(@PathVariable("statuses") String statuses) {
//        return OrderMapper.toOrderDtos(orderService.findAllByOrderStatusInPaginated(statuses));
//    }

    @GetMapping("/get/statuscount/{status}")
    public int countByOrderStatus(@PathVariable("status") String status) {
        return orderService.countByOrderStatusIn(status);
    }

//    @GetMapping("getpage/{page}/{pagesize}")
//    public List<OrderDto> findAllPaginated(@PathVariable("page") int page, @PathVariable("pagesize") Integer pageSize) {
//        return OrderMapper.toOrderDtos(orderService.findAllPaginated(page, pageSize));
//    }
//    @GetMapping("getpage/{page}/{pagesize}")
//    public ResponseEntity<PagedOrders> findAllPaginated(@PathVariable("page") int page, @PathVariable("pagesize") Integer pageSize) {
//        return ResponseEntity.ok().body(PagedOrders.builder()
//                .totalItems(orderService.count())
//                .orders(OrderMapper.toOrderDtos(orderService.findAllPaginated(page, pageSize)))
//                .build());
//    }
    @GetMapping("getpage/{page}/{pagesize}")
    public PagedOrdersDto findAllPaginated(@PathVariable("page") int pageNumber, @PathVariable("pagesize") Integer pageSize) {
        Page<Order> page = orderService.findAllPaginated(pageNumber, pageSize);
        return PagedOrdersDto.builder()
                .totalItems(page.getTotalElements())
                .orders(OrderMapper.toOrderDtos(page.getContent()))
                .build();
    }
//    @GetMapping("getpage/{page}/{pagesize}")
//    public List<OrderDto> findAllPaginated(@PathVariable("page") int page, @PathVariable("pagesize") Integer pageSize) {
//        return OrderMapper.toOrderDtos(orderService.findAllPaginated(page, pageSize));
//    }

    @GetMapping("search/{statuses}/{searchtext}/{page}/{pagesize}")
    public PagedOrdersDto searchOrders(@PathVariable("statuses") String statuses,
                                       @PathVariable("searchtext") String searchText,
                                       @PathVariable("page") int pageNumber,
                                       @PathVariable("pagesize") Integer pageSize) {
//        return PagedOrders.builder()
//                .totalItems(orderService.searchOrdersCount(searchKeyword, searchText))
//                .orders(OrderMapper.toOrderDtos(orderService.searchOrders(searchKeyword, searchText, page, pageSize)))
//                .build();
        Page<Order> page = orderService.searchOrders(statuses, searchText, pageNumber, pageSize);
        return PagedOrdersDto.builder().totalItems(page.getTotalElements()).orders(
                OrderMapper.toOrderDtos(page.getContent())).build();
    }

    @GetMapping("getfilteredorders/{originstatescsv}/{destinationstatescsv}/{primarysort}/{secondarysort}/{page}/{pagesize}")
    public PagedOrdersDto getFilteredOrders(@RequestParam("refs") String refs,
                                            @RequestParam("fieldequalto") String fieldEqualTo,
                                            @RequestParam("fieldgreaterthanequalto") String fieldGreaterThanEqualTo,
                                            @PathVariable("originstatescsv") String originStatesCsv,
                                            @PathVariable("destinationstatescsv") String destinationStatesCsv,
                                            @PathVariable("primarysort") String primarySort,
                                            @PathVariable("secondarysort") String secondarySort,
//                                          @PathVariable("type") String type,
//                                          @PathVariable("latitude") Double latitude,
//                                          @PathVariable("longitude") Double longitude,
//                                          @PathVariable("distance") int distance, // in miles
                                            @PathVariable("page") int page,
                                            @PathVariable("pagesize") Integer pageSize) throws JsonProcessingException {
        LatitudeLongitudeDistanceRefs latitudeLongitudeDistanceRefs = new ObjectMapper().readValue(refs, LatitudeLongitudeDistanceRefs.class);
        Map<String, Object> fieldEqualToMap = new ObjectMapper().readValue(fieldEqualTo, Map.class);
        Map<String, String> fieldGreaterThanEqualToMap = new ObjectMapper().readValue(fieldGreaterThanEqualTo, Map.class);
        primarySort = primarySort.equals("null") ? null : primarySort;
        secondarySort = secondarySort.equals("null") ? null : secondarySort;
        PagedOrders pagedOrders = orderService.getFilteredOrders(latitudeLongitudeDistanceRefs, originStatesCsv, destinationStatesCsv,
                                                                 primarySort, secondarySort,
                                                                 fieldEqualToMap, fieldGreaterThanEqualToMap,
                                                                 page, pageSize);
        return PagedOrdersMapper.toPagedOrdersDto(pagedOrders);
    }

    /*@GetMapping("getinradius/{pickupLatitude}/{pickupLongitude}/{deliveryLatitude}/{deliveryLongitude}/{distance}/{page}/{pagesize}")
    public PagedOrdersDto getCircularDistanceBoth(@PathVariable("pickupLatitude") Double pickupLatitude,
                                                  @PathVariable("pickupLongitude") Double pickupLongitude,
                                                  @PathVariable("deliveryLatitude") Double deliveryLatitude,
                                                  @PathVariable("deliveryLongitude") Double deliveryLongitude,
                                                  @PathVariable("distance") int distance, // in miles
                                                  @PathVariable("page") int page,
                                                  @PathVariable("pagesize") Integer pageSize) {
        PagedOrders pagedOrders = orderService.getCircularDistanceBoth(pickupLatitude,
                pickupLongitude,
                deliveryLatitude,
                deliveryLongitude, distance, page, pageSize);
        return PagedOrdersMapper.toPagedOrdersDto(pagedOrders);
    }*/

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long orderId) {
        orderService.deleteById(orderId);
    }

    @DeleteMapping("/")
    public void deleteAll() {
        orderService.deleteAllOrders();
    }

    @GetMapping("/count")
    public long count() {
        return orderService.count();
    }
}
