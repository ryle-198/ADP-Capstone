package za.ac.cput.repository;



import org.junit.jupiter.api.*;
import za.ac.cput.domain.Shipment;
import za.ac.cput.factory.ShipmentFactory;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ShipmentRepositoryTest {

    private ShipmentRepository repository;
    private Shipment shipment;

    @BeforeEach
    void a_setUp() {
        repository = ShipmentRepository.getInstance();

        shipment = ShipmentFactory.createShipment(
                "SHP001",
                "ORD001",
                "RT001",
                "Cape Town",
                "Johannesburg",
                Shipment.Status.IN_TRANSIT,
                true
        );
    }

    @Test
    void b_testCreate() {
        Shipment created = repository.create(shipment);
        assertNotNull(created);
        assertEquals("SHP001", created.getShipmentId());
        System.out.println("Created: "+created);
    }

    @Test
    void c_testRead() {
        repository.create(shipment);
        Shipment found = repository.read("SHP001");
        assertNotNull(found);
        assertEquals("SHP001", found.getShipmentId());
        System.out.println("Read: "+found);
    }

    @Test
    void d_testUpdate() {
        repository.create(shipment);

        Shipment updated = ShipmentFactory.createShipment(
                "SHP001",
                "ORD001",
                "RT002",
                "Cape Town",
                "Durban",
                Shipment.Status.DELIVERED,
                false
        );

        Shipment result = repository.update(updated);
        assertNotNull(result);
        assertEquals("Durban", result.getDestinationAddress());
        assertEquals(Shipment.Status.DELIVERED, result.getStatus());
        System.out.println("Update: "+updated);
    }

    @Test
    @Disabled
    void e_testDelete() {
        repository.create(shipment);
        boolean deleted = repository.delete("SHP001");
        assertTrue(deleted);
        System.out.println("deleted successfully");
    }

    @Test
    void f_testGetAll(){
        List<Shipment> allShipments = repository.getAllShipments();
        System.out.println("All Shipments:"+"\n"+allShipments);
    }
}