package com.transport.app.rest;

import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {

		/*orderService.findById(12l);

		Order order = Order.builder()
			.pickupContactName("XYZ")
				.build();
		orderRepository.save(order);
		List<Order> orders = orderRepository.findAll();
		logger.info("Total orders: {}", orders.size());*/
	}

}
