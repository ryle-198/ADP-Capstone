package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.factory.VehicleFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
VehicleServiceTest.java
Vehicle service test model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class VehicleServiceTest {

    @Autowired
    private VehicleService service;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {

        vehicle = VehicleFactory.createVehicle(
                "V001",
                "CAA24680",
                Vehicle.VehicleType.TRUCK,
                16000.0f,
                Vehicle.VehicleStatus.AVAILABLE,
                18000.0f,
                LocalDate.of(2025, 12, 10)
        );

        assertNotNull(vehicle);
    }

    @Test
    void a_create() {

        Vehicle created = service.create(vehicle);

        assertNotNull(created);
        assertEquals("V001", created.getVehicleId());
        assertEquals("CAA24680", created.getNumberPlate());

        System.out.println("Created Vehicle: " + created);
    }

    @Test
    void b_read() {

        // Create vehicle first
        service.create(vehicle);

        Vehicle read = service.read(vehicle.getVehicleId());

        assertNotNull(read);
        assertEquals(
                vehicle.getVehicleId(),
                read.getVehicleId()
        );

        assertEquals(
                vehicle.getNumberPlate(),
                read.getNumberPlate()
        );

        System.out.println("Read Vehicle: " + read);
    }

    @Test
    void c_update() {

        // Create vehicle first
        service.create(vehicle);

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

        Vehicle updated = service.update(updatedVehicle);

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
    void d_getAllVehicles() {

        // Create vehicle first
        service.create(vehicle);

        List<Vehicle> vehicles = service.getAllVehicles();

        assertNotNull(vehicles);
        assertFalse(vehicles.isEmpty());

        System.out.println("All Vehicles:");

        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    @Test
    void e_delete() {

        service.create(vehicle);

        boolean deleted =
                service.delete(vehicle.getVehicleId());

        assertTrue(deleted);

        Vehicle deletedVehicle =
                service.read(vehicle.getVehicleId());

        assertNull(deletedVehicle);

        System.out.println("Vehicle deleted successfully.");
    }
}