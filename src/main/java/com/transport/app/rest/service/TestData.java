package com.transport.app.rest.service;

import com.google.maps.errors.ApiException;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.repository.UserAuthRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class TestData {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserAuthRepository userRepository;
    @Autowired
    private DistanceMatrixService distanceMatrixService;

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
            try {
                orders.add(generateOrder(i, users.get(0).getId()));
            } catch (InterruptedException e) {
            } catch (ApiException e) {
            } catch (IOException e) {
            }
        });
        IntStream.range(801, 1001).forEach(i -> {
            try {
                orders.add(generateOrder(i, users.get(1).getId()));
            } catch (InterruptedException e) {
            } catch (ApiException e) {
            } catch (IOException e) {
            }
        });
        orderRepository.saveAll(orders);
    }

    private String[] userTypes = new String[]{"BROKER", "BROKER", "CARRIER", "CARRIER", "DRIVER", "DRIVER"};

    public User generateUser(int i) {
        System.out.println(i);
        Pair pair = getLocation(42.997075, -103.074280, 8000);
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
                .address("User address " + i)
                .build();
    }
    public Order generateOrder(int i, long userId) throws InterruptedException, ApiException, IOException {
        System.out.println(i);
        Pair pickupPair = getLocation(42.997075, -103.074280, 9000);
        Pair deliveryPair = getLocation(42.997075, -103.074280, 10000);
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
                .distance(distanceMatrixService.getDriveDist(
                        (double) pickupPair.getKey(), (double) pickupPair.getValue(),
                        (double) deliveryPair.getKey(), (double) deliveryPair.getValue()))
                .createdBy(userRepository.findById(userId).get())
                .build();
    }

    public static Pair getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLatitude = new_x + x0;
        double foundLongitude = y + y0;
//        System.out.println(foundLatitude + ", " + foundLongitude );
        return new Pair<>(foundLatitude, foundLongitude);
    }
}
