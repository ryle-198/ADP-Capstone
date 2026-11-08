package za.ac.cput.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.factory.VehicleFactory;
import za.ac.cput.repository.VehicleRepo.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
VehicleRepositoryTest.java
Vehicle repository test model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository repository;

    private Vehicle createTestVehicle() {

        return VehicleFactory.createVehicle(
                "V001",
                "CAA24680",
                Vehicle.VehicleType.TRUCK,
                16000.0f,
                Vehicle.VehicleStatus.AVAILABLE,
                18000.0f,
                LocalDate.of(2025, 12, 10)
        );
    }

    @Test
    void a_create() {

        Vehicle vehicle = createTestVehicle();

        Vehicle saved = repository.save(vehicle);

        assertNotNull(saved);
        assertEquals("V001", saved.getVehicleId());
        assertEquals("CAA24680", saved.getNumberPlate());

        System.out.println("Created Vehicle: " + saved);
    }

    @Test
    void b_read() {

        Vehicle vehicle = createTestVehicle();

        repository.save(vehicle);

        Optional<Vehicle> result =
                repository.findById("V001");

        assertTrue(result.isPresent());

        Vehicle found = result.get();

        assertEquals("V001", found.getVehicleId());
        assertEquals("CAA24680", found.getNumberPlate());

        System.out.println("Read Vehicle: " + found);
    }

    @Test
    void c_update() {

        Vehicle vehicle = createTestVehicle();

        repository.save(vehicle);

        Vehicle updatedVehicle = new Vehicle.Builder()
                .copy(vehicle)
                .setNumberPlate("CAA13690")
                .setCapacity(15000.0f)
                .setCurrentStatus(
                        Vehicle.VehicleStatus.IN_SERVICE
                )
                .setMileage(19000.0f)
                .setLastService(LocalDate.now())
                .build();

        Vehicle updated =
                repository.save(updatedVehicle);

        assertNotNull(updated);

        assertEquals(
                "CAA13690",
                updated.getNumberPlate()
        );

        assertEquals(
                15000.0f,
                updated.getCapacity()
        );

        assertEquals(
                Vehicle.VehicleStatus.IN_SERVICE,
                updated.getCurrentStatus()
        );

        System.out.println("Updated Vehicle: " + updated);
    }

    @Test
    void d_getAll() {

        Vehicle vehicle = createTestVehicle();

        repository.save(vehicle);

        List<Vehicle> vehicles =
                repository.findAll();

        assertNotNull(vehicles);
        assertFalse(vehicles.isEmpty());

        System.out.println("All Vehicles:");

        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    @Test
    void e_delete() {

        Vehicle vehicle = createTestVehicle();

        repository.save(vehicle);

        assertTrue(
                repository.existsById(vehicle.getVehicleId())
        );

        repository.deleteById(vehicle.getVehicleId());

        assertFalse(
                repository.existsById(vehicle.getVehicleId())
        );

        System.out.println("Vehicle deleted successfully.");
    }

}
