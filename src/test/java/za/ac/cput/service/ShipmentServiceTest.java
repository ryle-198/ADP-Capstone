/* ShipmentServiceTest.java
   Shipment Service Test class
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.*;
import za.ac.cput.factory.*;
import za.ac.cput.service.Customer.CustomerService;
import za.ac.cput.service.DeliveryOrders.DeliveryOrdersService;
import za.ac.cput.service.Driver.DriverService;
import za.ac.cput.service.Route.RouteService;
import za.ac.cput.service.Shipment.ShipmentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShipmentServiceTest {

    @Autowired
    private ShipmentService service;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeliveryOrdersService orderService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private RouteService routeService;

    private Shipment shipment;
    private Customer customer;
    private DeliveryOrders order;
    private Driver driver;
    private Route route;

    @BeforeAll
    void setUp() {
        customer = customerService.create(CustomerFactory.createCustomer("CUST-SH01", "Yamkela",
                "0732510842", "shipment.svc@example.com", "14 Aquarius Av", new ArrayList<>()));
        order = orderService.create(DeliveryOrdersFactory.createDeliveryOrder("ORD-SH01", customer,
                LocalDate.of(2026, 3, 25), LocalDate.of(2026, 4, 1),
                DeliveryOrders.Status.OrderPlaced, DeliveryOrders.PaymentStatus.PENDING, 444f, "None"));
        driver = driverService.create(DriverFactory.buildDriver("Peter", "Burger",
                "LIC-SH-" + UUID.randomUUID().toString().substring(0, 8), "0821234567", true));
        route = routeService.create(RouteFactory.createRoute("RT-SH01", driver, null,
                "Cape Town", "Johannesburg", 1400f, 900));

        shipment = ShipmentFactory.createShipment("SH001", order, route,
                "123 Main St, Cape Town", "456 Long St, Johannesburg", Shipment.Status.CREATED, false);
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

    @Test @Order(3)
    void update() {
        Shipment updated = new Shipment.Builder().copy(shipment)
                .setStatus(Shipment.Status.IN_TRANSIT).setFragile(true).build();
        assertNotNull(service.update(updated));
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
    @AfterAll
    void cleanUp() {
        routeService.delete(route.getRouteId());
        orderService.delete(order.getOrderId());
        driverService.delete(driver.getId());
        customerService.delete(customer.getCustomerId());
    }
}