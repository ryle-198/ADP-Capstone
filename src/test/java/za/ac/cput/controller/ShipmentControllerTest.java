/* ShipmentControllerTest.java
   Shipment Controller Test class
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.*;
import za.ac.cput.factory.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShipmentControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();

    private  Shipment shipment;
    protected static String BASE_URL = "http://localhost:8080/freightanddelivery/shipment";
    private final String ROOT = "http://localhost:8080/freightanddelivery";
    private Customer customer;
    private DeliveryOrders order;
    private  Driver driver;
    private  Route route;

// explanation in DeliveryOrdersControllerTest
//    static RestTemplate authenticated() {
//        RestTemplate rt = new RestTemplate();
//        rt.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "admin"));
//        return rt;
//    }

    @BeforeAll
    void setUp() {
        RestTemplate rest = new RestTemplate();

        customer = rest.postForObject(ROOT + "/customer/create",
                CustomerFactory.createCustomer("CUST-SC01", "Yamkela", "0732510842",
                        "shipment.ctrl@example.com", "14 Aquarius Av", new ArrayList<>()),
                Customer.class);

        order = rest.postForObject(ROOT + "/deliveryOrders/create",
                DeliveryOrdersFactory.createDeliveryOrder("ORD-SC01", customer,
                        LocalDate.of(2026, 3, 25), LocalDate.of(2026, 4, 1),
                        DeliveryOrders.Status.OrderPlaced, DeliveryOrders.PaymentStatus.PENDING,
                        500f, "None"),
                DeliveryOrders.class);

        driver = rest.postForObject(ROOT + "/driver/create",
                DriverFactory.buildDriver("Peter", "Burger",
                        "LIC-SC-" + UUID.randomUUID().toString().substring(0, 8),
                        "0821234567", true),
                Driver.class);

        route = rest.postForObject(ROOT + "/route/create",
                RouteFactory.createRoute("RT-SC01", driver, null,
                        "Durban", "Pretoria", 600f, 400),
                Route.class);

        shipment = ShipmentFactory.createShipment("SH002", order, route,
                "789 Beach Rd, Durban", "321 Church St, Pretoria",
                Shipment.Status.CREATED, true);
    }

    @Test
    @Order(1)
    void create() {
        ResponseEntity<Shipment> response = restTemplate.postForEntity(
                BASE_URL + "/create", shipment, Shipment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("Created: " + response.getBody());
    }

    @Test
    @Order(2)
    void read() {
        ResponseEntity<Shipment> response = restTemplate.getForEntity(
                BASE_URL + "/read/SH002", Shipment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(3)
    void update() {
        Shipment updated = new Shipment.Builder().copy(shipment)
                .setStatus(Shipment.Status.OUT_FOR_DELIVERY).build();
        HttpEntity<Shipment> entity = new HttpEntity<>(updated);
        ResponseEntity<Shipment> response = restTemplate.exchange(
                BASE_URL + "/update", HttpMethod.PUT, entity, Shipment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    void getAll() {
        ResponseEntity<Shipment[]> response = restTemplate.getForEntity(
                BASE_URL + "/getAll", Shipment[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("All: " + response.getBody());
    }

    @Test
    @Order(5)
    void delete() {
        restTemplate.delete(BASE_URL + "/delete/SH002");
        System.out.println("Deleted SH002");
    }
}