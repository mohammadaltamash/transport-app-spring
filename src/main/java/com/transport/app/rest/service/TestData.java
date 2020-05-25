package com.transport.app.rest.service;

import com.google.maps.errors.ApiException;
import com.transport.app.rest.config.authenticationfacade.IAuthenticationFacade;
import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.repository.UserRepository;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class TestData {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String[] paymentTermBusinessDays = {
            "Immediately",
            "2 business days (Quick Pay)",
            "5 business days",
            "10 business days",
            "15 business days",
            "30 business days"
    };
    private String[] paymentMethod = {
            "Cash",
            "Certified Funds",
            "ACH (direct deposit)",
            "Company Check",
            "Wire Transfer",
            "Comchek"
    };
    private String[] paymentTermBegins = {
            "Pickup",
            "Delivery",
            "Receiving a uShip code",
            "Receiving a signed BOL"
    };
    private String[] autoTypes = {
            "Sedan",
            "Mini-van",
            "Motorcycle",
            "Pickup",
            "Suv",
            "Van",
    };
    private Map<String, String[]> makesAndModels = new LinkedHashMap<String, String[]>() {
        {
            put("BMW", new String[]{"X3", "X4", "X5", "X6", "X7"});
            put("Mercedes - Benz", new String[]{"C-Class", "GLE-Class", "GLS-Class"});
            put("Dodge", new String[]{"Durango"});
            put("Jeep", new String[]{"Cherokee", "Gladiator", "Grand Cherokee", "Grand Cherokee", "Wrangler"});
            put("Ram", new String[]{"1500", "1500 Classic"});
            put("Ford", new String[]{"Bronco", "Escape", "Expedition", "Expedition MAX", "Explorer", "F-150", "Mustang", "Ranger", "Super Duty", "Transit"});
            put("Lincoln", new String[]{"Aviator", "Continental", "Corsair", "Navigator"});
            put("Buick", new String[]{"Enclave"});
            put("Cadillac", new String[]{"CT4", "CT5", "CT6", "Escalade", "Escalade ESV", "XT4", "XT5", "XT6"});
            put("Chevrolet", new String[]{"Bolt", "Camaro", "Colorado", "Corvette", "Express", "Malibu", "Silverado", "Sonic", "Suburban", "Tahoe", "Traverse"});
            put("GMC", new String[]{"Acadia", "Canyon", "Savana", "Sierra", "Yukon", "Yukon XL"});
            put("Acura", new String[]{"ILX", "MDX", "NSX", "RDX", "TLX"});
            put("Honda", new String[]{"Accord", "Civic", "CR-V", "Insight", "Odyssey", "Passport", "Pilot", "Ridgeline"});
            put("Hyundai", new String[]{"Elantra", "Santa Fe", "Sonata"});
            put("Kia", new String[]{"Optima", "Sorento", "Telluride"});
            put("Infiniti", new String[]{"QX60"});
            put("Nissan", new String[]{"Altima", "Frontier", "Leaf", "Maxima", "Murano", "NV", "Pathfinder", "Rogue", "Titan"});
            put("Rivian", new String[]{"R1S", "R1T"});
            put("Subaru", new String[]{"Ascent", "Impreza", "Legacy", "Outback"});
            put("Tesla", new String[]{"Model 3", "Model S", "Model X"});
            put("Lexus", new String[]{"ES"});
            put("Toyota", new String[]{"Avalon", "Camry", "Corolla", "Highlander", "Sequoia", "Sienna", "Tacoma", "Tundra"});
            put("Volkswagen", new String[]{"Atlas", "Passat"});
            put("Volvo", new String[]{"S60"});
        }
    };
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DistanceMatrixService distanceMatrixService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    ///////////// Mock
    List<User> users = new ArrayList<>();
    public void generateData() {
//        List<User> users = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
//        IntStream.range(1, 6).forEach(i -> {
//            users.add(generateUser(i));
//        });
//        users = userRepository.saveAll(users);

        File file = new File("src/main/resources/static/orders-generate.csv");
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        String userName = ((org.springframework.security.core.userdetails.User) authenticationFacade.getAuthentication().getPrincipal()).getUsername();
        User createdBy = userRepository.findByEmail(userName);

        List<CityZipLatLong> list = new ArrayList<>();
        try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
            CsvRow row;
            int count = 0;
            while ((row = csvParser.nextRow()) != null) {
                System.out.println(++count);
                Order.OrderBuilder builder = Order.builder()
                        .pickupAddress(row.getField(0))
                        .pickupAddressState(row.getField(1))
                        .pickupZip(row.getField(2))
                        .pickupLatitude(Double.parseDouble(row.getField(3)))
                        .pickupLongitude(Double.parseDouble(row.getField(4)))
                        .deliveryAddress(row.getField(5))
                        .deliveryAddressState(row.getField(6))
                        .deliveryZip(row.getField(7))
                        .deliveryLatitude(Double.parseDouble(row.getField(8)))
                        .deliveryLongitude(Double.parseDouble(row.getField(9)))
                        .distance(Long.parseLong(row.getField(10)));

                orders.add(generateOrder(builder, Long.parseLong(row.getField(10)), count, createdBy));
            }
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        } catch (ApiException e) {
            logger.warn(e.getMessage());
        }
//        cityZipLatLongRepository.saveAll(list);


//        IntStream.range(1, 101).forEach(i -> {
//            try {
//                orders.add(generateOrder(i, users.get(0).getId()));
//            } catch (InterruptedException e) {
//            } catch (ApiException e) {
//            } catch (IOException e) {
//            }
//        });
//        IntStream.range(101, 201).forEach(i -> {
//            try {
//                orders.add(generateOrder(i, users.get(1).getId()));
//            } catch (InterruptedException e) {
//            } catch (ApiException e) {
//            } catch (IOException e) {
//            }
//        });
        orderRepository.saveAll(orders);
    }

    private String[] userTypes = new String[]{"BROKER", "BROKER", "CARRIER", "CARRIER", "DRIVER", "DRIVER"};

    public User generateUser(int i) {
        System.out.println(i);
        double[] pair = getLocation(42.997075, -103.074280, 8000);
        return User.builder()
                .fullName("First" + i + " " + "Second" + i)
                .userName("username" + i)
                .email("email" + i + "@example.com")
                .password(new BCryptPasswordEncoder().encode("abc123"))
                .type(userTypes[i - 1])
                .companyName("User Company" + i)
                .phones(new HashMap<String, String>() {{ put("35725345345", "UserPhones note"); }})
                .latitude((double) pair[0])
                .longitude((double) pair[1])
                .zip("24234")
                .address("User address " + i)
                .build();
    }
    public Order generateOrder(Order.OrderBuilder builder, long distance, int i, User user) throws InterruptedException, ApiException, IOException {
        System.out.println(i);
        double[] pickupPair = getLocation(42.997075, -103.074280, 5000);
        double[] deliveryPair = getLocation(42.997075, -103.074280, 10000);
//        long distance = distanceMatrixService.getDriveDist(
//                pickupPair[0], pickupPair[1],
//                deliveryPair[0], deliveryPair[1]);
        Random r = new Random();
        double carrierPay = new BigDecimal(100D + (5000D - 100D) * r.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double amountOnPickup = carrierPay / 4;
        double amountOnDelivery = carrierPay - amountOnPickup;

        int firstIndex = 0 + r.nextInt(23 - 0 + 1);
        String[] models = makesAndModels.get((makesAndModels.keySet().toArray())[firstIndex]);
        int secondIndex = 0 + r.nextInt((models.length - 1) - 0 + 1);

        String make = (String) (makesAndModels.keySet().toArray())[firstIndex];
        String model = models[secondIndex];
        System.out.println(models[secondIndex]);
        return builder
                .brokerOrderId("BrokerOrderId-" + i)
//                .pickupAddress("PickupAddress-" + i)
//                .pickupZip("12345")
                .pickupPhones(new HashMap<String, String>() {{ put(String.valueOf(11111111111l + (long)(Math.random() * ((99999999999l - 11111111111l) + 1))), "PickupPhones note"); }})
                .pickupDates(new HashMap<String, Date>() {{ put("begin", new Date()); put("end", new Date()); }})
//                .deliveryAddress("DeliveryAddress-" + i)
//                .deliveryZip("65835")
                .deliveryPhones(new HashMap<String, String>() {{ put(String.valueOf(11111111111l + (long)(Math.random() * ((99999999999l - 11111111111l) + 1))), "DeliveryPhones note"); }})
                .deliveryDates(new HashMap<String, Date>() {{ put("begin", new Date()); put("end", new Date()); }})
                .vehicleMake("VehicleMake-" + i)
                .carrierPay(carrierPay)
                .amountOnPickup(amountOnPickup)
                .amountOnDelivery(amountOnDelivery)
                .perMile(carrierPay / (distance * 0.00062137))
                .paymentTermBusinessDays(paymentTermBusinessDays[0 + r.nextInt(5 - 0 + 1)])
                .paymentMethod(paymentMethod[0 + r.nextInt(5 - 0 + 1)])
                .paymentTermBegins(paymentTermBegins[0 + r.nextInt(3 - 0 + 1)])
                .brokerCompanyName("Broker Company Name")
                .brokerAddress("Broker Address")
                .brokerZip("96445")
                .shipperPhones(new HashMap<String, String>() {{ put(String.valueOf(11111111111l + (long)(Math.random() * ((99999999999l - 11111111111l) + 1))), "ShipperPhones note"); }})
                .brokerEmail(user.getEmail())
                .vehicleInoperable(false)
                .vehicleAutoType(autoTypes[0 + r.nextInt(5 - 0 + 1)])
                .vehicleMake(make)
                .vehicleModel(model)
//                .pickupLatitude((double) pickupPair[0])
//                .pickupLongitude((double) pickupPair[1])
//                .deliveryLatitude((double) deliveryPair[0])
//                .deliveryLongitude((double) deliveryPair[1])
//                .distance(distance) // meters
                .createdBy(user)
                .createdByName(user.getFullName()) // For search
                .build();
    }

    public static double[] getLocation(double x0, double y0, int radius) {
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
//        return new Pair<>(foundLatitude, foundLongitude);
        double[] latLng = new double[2];
        latLng[0] = foundLatitude;
        latLng[1] = foundLongitude;
        return latLng;
    }
    private static Map<String, String[]> getMakesModels() {
        return new LinkedHashMap<String, String[]>() {
            {
                put("BMW", new String[]{"X3", "X4", "X5", "X6", "X7"});
                put("Mercedes - Benz", new String[]{"C-Class", "GLE-Class", "GLS-Class"});
                put("Dodge", new String[]{"Durango"});
                put("Jeep", new String[]{"Cherokee", "Gladiator", "Grand Cherokee", "Grand Cherokee", "Wrangler"});
                put("Ram", new String[]{"1500", "1500 Classic"});
                put("Ford", new String[]{"Bronco", "Escape", "Expedition", "Expedition MAX", "Explorer", "F-150", "Mustang", "Ranger", "Super Duty", "Transit"});
                put("Lincoln", new String[]{"Aviator", "Continental", "Corsair", "Navigator"});
                put("Buick", new String[]{"Enclave"});
                put("Cadillac", new String[]{"CT4", "CT5", "CT6", "Escalade", "Escalade ESV", "XT4", "XT5", "XT6"});
                put("Chevrolet", new String[]{"Bolt", "Camaro", "Colorado", "Corvette", "Express", "Malibu", "Silverado", "Sonic", "Suburban", "Tahoe", "Traverse"});
                put("GMC", new String[]{"Acadia", "Canyon", "Savana", "Sierra", "Yukon", "Yukon XL"});
                put("Acura", new String[]{"ILX", "MDX", "NSX", "RDX", "TLX"});
                put("Honda", new String[]{"Accord", "Civic", "CR-V", "Insight", "Odyssey", "Passport", "Pilot", "Ridgeline"});
                put("Hyundai", new String[]{"Elantra", "Santa Fe", "Sonata"});
                put("Kia", new String[]{"Optima", "Sorento", "Telluride"});
                put("Infiniti", new String[]{"QX60"});
                put("Nissan", new String[]{"Altima", "Frontier", "Leaf", "Maxima", "Murano", "NV", "Pathfinder", "Rogue", "Titan"});
                put("Rivian", new String[]{"R1S", "R1T"});
                put("Subaru", new String[]{"Ascent", "Impreza", "Legacy", "Outback"});
                put("Tesla", new String[]{"Model 3", "Model S", "Model X"});
                put("Lexus", new String[]{"ES"});
                put("Toyota", new String[]{"Avalon", "Camry", "Corolla", "Highlander", "Sequoia", "Sienna", "Tacoma", "Tundra"});
                put("Volkswagen", new String[]{"Atlas", "Passat"});
                put("Volvo", new String[]{"S60"});
            }
        };
    }
    public static void main(String[] args) {
        Map<String, String[]> map = getMakesModels();
        Random r = new Random();
        int firstIndex = 0 + r.nextInt(23 - 0 + 1);
        String[] item = map.get((map.keySet().toArray())[firstIndex]);
        int secondIndex = 0 + r.nextInt((item.length - 1) - 0 + 1);
        System.out.println((map.keySet().toArray())[firstIndex]);
        System.out.println(item[secondIndex]);
    }
}
