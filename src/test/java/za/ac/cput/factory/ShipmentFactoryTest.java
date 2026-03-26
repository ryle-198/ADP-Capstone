package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Shipment;
import static org.junit.jupiter.api.Assertions.*;

public class ShipmentFactoryTest {

    @Test
    public void testCreateShipment_Success() {
        Shipment shipment = ShipmentFactory.createShipment(
                "SHP001",
                "ORD001",
                "RT001",
                "Cape Town",
                "Johannesburg",
                Shipment.Status.IN_TRANSIT,
                true
        );

        assertNotNull(shipment);
        assertEquals("SHP001", shipment.getShipmentId());
        assertEquals("ORD001", shipment.getOrderId());
        assertEquals("RT001", shipment.getRouteId());
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
                "ORD002",
                "RT002",
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
                "ORD003",
                "RT003",
                "",
                "Pretoria",
                Shipment.Status.DELIVERED,
                false
        );

        assertNull(shipment);
    }
}