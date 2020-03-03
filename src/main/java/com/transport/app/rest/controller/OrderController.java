package com.transport.app.rest.controller;

import com.transport.app.rest.domain.Order;
import com.transport.app.rest.mapper.OrderDto;
import com.transport.app.rest.mapper.OrderMapper;
import com.transport.app.rest.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @CrossOrigin
//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/create")
    public OrderDto create(@Valid @RequestBody Order order) {
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
        return OrderMapper.toOrderDto(orderService.create(order));
    }

    @PutMapping("/update")
    public OrderDto update(@RequestBody Order order) {
        OrderDto orderDto = OrderMapper.toOrderDto(order);
        return OrderMapper.toOrderDto(orderService.update(order));
    }

    @GetMapping("/get/{id}")
    public OrderDto findOrderById(@PathVariable("id") long orderId) {
        return OrderMapper.toOrderDto(orderService.findById(orderId));
    }

    @GetMapping("/get")
    public List<OrderDto> findAllOrders() {
        return OrderMapper.toOrderDtos(orderService.findAll());
    }

    @GetMapping("getpage/{page}")
    public List<OrderDto> findAllPaginated(@PathVariable("page") int page) {
        return OrderMapper.toOrderDtos(orderService.findAllPaginated(page));
    }

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
