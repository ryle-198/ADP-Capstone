package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
ShipmentFactoryTest.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */

public class ShipmentFactoryTest {
    private DeliveryOrders order;
    private Route route;

    @BeforeEach
    void setUp() {
        Customer customer = CustomerFactory.createCustomer("CUST-1", "Yamkela", "0732510842",
                "yamkela197@gmail.com", "14 Aquarius Av", new ArrayList<>());
        order = DeliveryOrdersFactory.createDeliveryOrder("ORD001", customer, LocalDate.of(2026, 3, 25),
                LocalDate.of(2026, 4, 1), DeliveryOrders.Status.OrderPlaced,
                DeliveryOrders.PaymentStatus.PENDING, 444f, "None");
        Driver driver = DriverFactory.buildDriver("Peter", "Burger", "LIC123456", "0821234567", true);
        route = RouteFactory.createRoute("RT001", driver, null, "Cape Town", "Johannesburg", 1400f, 900);
    }

    @Test
    public void testCreateShipment_Success() {
        Shipment shipment = ShipmentFactory.createShipment(
                "SHP001",
                order,
                route,
                "Cape Town",
                "Johannesburg",
                Shipment.Status.IN_TRANSIT,
                true
        );

        assertNotNull(shipment);
        assertEquals("SHP001", shipment.getShipmentId());
        assertEquals("ORD001", shipment.getOrder().getOrderId());
        assertEquals("RT001", shipment.getRoute().getRouteId());
        assertEquals("Cape Town", shipment.getOriginAddress());
        assertEquals("Johannesburg", shipment.getDestinationAddress());
        assertEquals(Shipment.Status.IN_TRANSIT, shipment.getStatus());
        assertTrue(shipment.isFragile());
        System.out.println("successfully created shipment");
    }

    @Test
    public void testCreateShipment_NullShipmentId() {
        Shipment shipment = ShipmentFactory.createShipment(
                null,
                order,
                route,
                "Cape Town",
                "Durban",
                Shipment.Status.IN_TRANSIT,
                false
        );

        assertNull(shipment);
    }

    @Test
    public void testCreateShipment_EmptyOriginAddress() {
        Shipment shipment = ShipmentFactory.createShipment(
                "SHP003",
                order,
                route,
                "",
                "Pretoria",
                Shipment.Status.DELIVERED,
                false
        );

        assertNull(shipment);
    }
}