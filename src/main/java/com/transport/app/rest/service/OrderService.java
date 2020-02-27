package com.transport.app.rest.service;

import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.OrderMapper;
import com.transport.app.rest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order create(Order order) {
        /*Order order = orderRepository.save(OrderMapper.toOrder(orderDto));
        return OrderMapper.toOrderDto(order);*/
        Order ordr = orderRepository.save(order);
        return orderRepository.save(ordr);
    }

    public Order update(Order order) {
        /*OrderDto oDto = findById(orderDto.getId());
        Order order = orderRepository.save(OrderMapper.toOrder(oDto));
        return OrderMapper.toOrderDto(order);*/
        Order o = findById(order.getId());
        return orderRepository.save(OrderMapper.toUpdatedOrder(o, order));
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

}
