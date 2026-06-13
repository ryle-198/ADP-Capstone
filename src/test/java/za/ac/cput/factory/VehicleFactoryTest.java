package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Vehicle;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/*
VehicleFactoryTest.java
Vehicle model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

public class VehicleFactoryTest {

    LocalDate lastService = LocalDate.of(2025,12,10);

    @Test
    void testCreatVehicle_Success() {
        Vehicle vehicle = VehicleFactory.createVehicle(
                "V001",
                "CAA24680",
                Vehicle.VehicleType.TRUCK,
                16000.0f,
                Vehicle.VehicleStatus.AVAILABLE,
                18000.0f,
                lastService
                );

        assertNotNull(vehicle);
        assertEquals("V001", vehicle.getVehicleId());
        assertEquals("CAA24680", vehicle.getNumberPlate());
        assertEquals(Vehicle.VehicleType.TRUCK, vehicle.getType());
        assertEquals(16000.0f, vehicle.getCapacity());
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getCurrentStatus());
        assertEquals(18000.0f, vehicle.getMileage());
        assertNotNull(vehicle.getLastService());
        System.out.println("successfully created a vehicle");
    }
    @Test
            void testCreateVehicle_NullVehicleId() {
        Vehicle vehicle = VehicleFactory.createVehicle(
                null,
                "CAA24680",
                Vehicle.VehicleType.TRUCK,
                16000.0f,
                Vehicle.VehicleStatus.AVAILABLE,
                18000.0f,
                lastService
        );

    }
        @Test
    void testCreateVehicle_NullLastService() {
            Vehicle vehicle = VehicleFactory.createVehicle(
                    "V001",
                    "CAA24680",
                    Vehicle.VehicleType.TRUCK,
                    16000.0f,
                    Vehicle.VehicleStatus.AVAILABLE,
                    18000.0f,
                    null
            );

            assertNull(vehicle);
        }
}
