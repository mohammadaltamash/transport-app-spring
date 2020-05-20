package com.transport.app.rest;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.repository.CityZipLatLongRepository;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.service.OrderService;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private volatile int runCount = 0;

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CityZipLatLongRepository cityZipLatLongRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {

//		if (runCount == 0) {
//			File file = new File("src/main/resources/static/us-zip-code-latitude-and-longitude2.csv");
//			CsvReader csvReader = new CsvReader();
//			csvReader.setContainsHeader(true);
//
//			List<CityZipLatLong> list = new ArrayList<>();
//			long count = 0;
//			try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
//				CsvRow row;
//				while ((row = csvParser.nextRow()) != null) {
//					System.out.println("Reading line: " + ++count);
////					System.out.println("First column of line: " + row.getField(0));
//
//					list.add(CityZipLatLong.builder()
//							.zip(row.getField(0))
//							.city(row.getField(1))
//							.state(row.getField(2))
//							.latitude(Double.parseDouble(row.getField(3)))
//							.longitude(Double.parseDouble(row.getField(4)))
//							.timezone(Integer.parseInt(row.getField(5)))
//							.geopoint(row.getField(7)).build());
//				}
//			} catch (IOException e) {
//				logger.warn(e.getMessage());
//			}
//			cityZipLatLongRepository.saveAll(list);
//			runCount++;
//		}
		/*orderService.findById(12l);

		Order order = Order.builder()
			.pickupContactName("XYZ")
				.build();
		orderRepository.save(order);
		List<Order> orders = orderRepository.findAll();
		logger.info("Total orders: {}", orders.size());*/
	}

}
