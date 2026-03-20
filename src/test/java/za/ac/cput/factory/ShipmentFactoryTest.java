package za.ac.cput.factory;



import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Shipment;
import static org.junit.jupiter.api.Assertions.*;

public class ShipmentFactoryTest {

    @Test
    public void testCreateShipment_Success() {
        Shipment shipment = ShipmentFactory.createShipment(
                "SHP001",
                "Cape Town",
                "Johannesburg",
                150.5,
                "In Transit",
                "2026-03-25"
        );
        assertNotNull(shipment);
        assertEquals("SHP001", shipment.getShipmentId());
        assertEquals("Cape Town", shipment.getOrigin());
        assertEquals("Johannesburg", shipment.getDestination());
        assertEquals(150.5, shipment.getWeight());
        assertEquals("In Transit", shipment.getStatus());
    }

    @Test
    public void testCreateShipment_NullShipmentId() {
        Shipment shipment = ShipmentFactory.createShipment(
                null,
                "Cape Town",
                "Johannesburg",
                150.5,
                "In Transit",
                "2026-03-25"
        );
        assertNull(shipment);
    }

    @Test
    public void testCreateShipment_EmptyOrigin() {
        Shipment shipment = ShipmentFactory.createShipment(
                "SHP002",
                "",
                "Johannesburg",
                150.5,
                "In Transit",
                "2026-03-25"
        );
        assertNull(shipment);
    }
}