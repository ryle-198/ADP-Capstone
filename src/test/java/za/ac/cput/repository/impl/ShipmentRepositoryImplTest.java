package za.ac.cput.repository.impl;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Shipment;
import za.ac.cput.factory.ShipmentFactory;
import static org.junit.jupiter.api.Assertions.*;

public class ShipmentRepositoryImplTest {

    private ShipmentRepositoryImpl repository;
    private Shipment shipment;

    @BeforeEach
    void setUp() {
        repository = ShipmentRepositoryImpl.getInstance();
        shipment = ShipmentFactory.createShipment(
                "SHP001",
                "Cape Town",
                "Johannesburg",
                150.5,
                "In Transit",
                "2026-03-25"
        );
    }

    @Test
    void testCreate() {
        Shipment created = repository.create(shipment);
        assertNotNull(created);
        assertEquals("SHP001", created.getShipmentId());
    }

    @Test
    void testRead() {
        repository.create(shipment);
        Shipment found = repository.read("SHP001");
        assertNotNull(found);
        assertEquals("SHP001", found.getShipmentId());
    }

    @Test
    void testUpdate() {
        repository.create(shipment);
        Shipment updated = ShipmentFactory.createShipment(
                "SHP001",
                "Cape Town",
                "Durban",
                200.0,
                "Delivered",
                "2026-03-26"
        );
        Shipment result = repository.update(updated);
        assertNotNull(result);
        assertEquals("Durban", result.getDestination());
    }

    @Test
    void testDelete() {
        repository.create(shipment);
        boolean deleted = repository.delete("SHP001");
        assertTrue(deleted);
    }
}