/* ShipmentServiceTest.java
   Shipment Service Test class
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Shipment;
import za.ac.cput.factory.ShipmentFactory;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShipmentServiceTest {

    @Autowired
    private ShipmentService service;
    private static Shipment shipment;

    @BeforeAll
    static void setUp() {
        shipment = ShipmentFactory.createShipment(
                "SH001",
                "ORD001",
                "RT001",
                "123 Main St, Cape Town",
                "456 Long St, Johannesburg",
                Shipment.Status.CREATED,
                false
        );
    }

    @Test
    @Order(1)
    void create() {
        Shipment created = service.create(shipment);
        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Shipment read = service.read("SH001");
        assertNotNull(read);
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Shipment updated = new Shipment.Builder()
                .setShipmentId("SH001")
                .setOrderId("ORD001")
                .setRouteId("RT001")
                .setOriginAddress("123 Main St, Cape Town")
                .setDestinationAddress("456 Long St, Johannesburg")
                .setStatus(Shipment.Status.IN_TRANSIT)
                .setFragile(true)
                .build();
        Shipment result = service.update(updated);
        assertNotNull(result);
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAll() {
        assertFalse(service.getAll().isEmpty());
        System.out.println("All Shipments: " + service.getAll());
    }

    @Test
    @Order(5)
    void delete() {
        boolean deleted = service.delete("SH001");
        assertTrue(deleted);
        System.out.println("Deleted: " + deleted);
    }
}