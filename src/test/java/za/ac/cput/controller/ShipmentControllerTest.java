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
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.Shipment;
import za.ac.cput.factory.ShipmentFactory;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShipmentControllerTest {


    private final RestTemplate restTemplate=new RestTemplate();

    private static Shipment shipment;
    protected static String BASE_URL = "http://localhost:8080/freightanddelivery/shipment";

    @BeforeAll
    static void setUp() {
        shipment = ShipmentFactory.createShipment(
                "SH002",
                "ORD002",
                "RT002",
                "789 Beach Rd, Durban",
                "321 Church St, Pretoria",
                Shipment.Status.CREATED,
                true
        );
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
        Shipment updated = new Shipment.Builder()
                .setShipmentId("SH002")
                .setOrderId("ORD002")
                .setRouteId("RT002")
                .setOriginAddress("789 Beach Rd, Durban")
                .setDestinationAddress("321 Church St, Pretoria")
                .setStatus(Shipment.Status.OUT_FOR_DELIVERY)
                .setFragile(true)
                .build();
        HttpEntity<Shipment> entity = new HttpEntity<>(updated);
        ResponseEntity<Shipment> response = restTemplate.exchange(
                BASE_URL + "/update", HttpMethod.PUT, entity, Shipment.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Order(4)
    void getAll() {
        ResponseEntity<Shipment[]> response = restTemplate.getForEntity(
                BASE_URL + "/getall", Shipment[].class);
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