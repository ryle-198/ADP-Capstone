package za.ac.cput.controller;

/*
VehicleControllerTest.java
Vehicle controller test model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
*/

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.factory.VehicleFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    @TestMethodOrder(MethodOrderer.MethodName.class)
    class VehicleControllerTest {

        protected final RestTemplate restTemplate = new RestTemplate();

        protected static final String BASE_URL =
                "http://localhost:8080/freightanddelivery/vehicle";

        protected static Vehicle vehicle;

        @BeforeAll
        static void setUp() {

            vehicle = VehicleFactory.createVehicle(
                    "V001",
                    "CAA24680",
                    Vehicle.VehicleType.TRUCK,
                    16000.0f,
                    Vehicle.VehicleStatus.AVAILABLE,
                    18000.0f,
                    LocalDate.of(2025,12,10)
            );
        }

        @Test
        void a_createVehicle() {

            String url = BASE_URL + "/create";

            ResponseEntity<Vehicle> response =
                    restTemplate.postForEntity(url, vehicle, Vehicle.class);

            assertNotNull(response.getBody());

            System.out.println(response.getBody());

        }

        @Test
        void b_readVehicle() {

            ResponseEntity<Vehicle> response =
                    restTemplate.getForEntity(
                            BASE_URL + "/read/" + vehicle.getVehicleId(),
                            Vehicle.class);

            assertNotNull(response.getBody());

            System.out.println(response.getBody());

        }

        @Test
        void c_updateVehicle() {

            Vehicle updated = new Vehicle.Builder()
                    .copy(vehicle)
                    .setCurrentStatus(Vehicle.VehicleStatus.IN_SERVICE)
                    .build();

            restTemplate.put(BASE_URL + "/update", updated);

            ResponseEntity<Vehicle> response =
                    restTemplate.getForEntity(
                            BASE_URL + "/read/" + vehicle.getVehicleId(),
                            Vehicle.class);

            assertNotNull(response.getBody());

            System.out.println(response.getBody());

        }

        @Test
        void d_getAllVehicles() {

            ResponseEntity<Vehicle[]> response =
                    restTemplate.getForEntity(
                            BASE_URL + "/getAll",
                            Vehicle[].class);

            assertNotNull(response.getBody());

            for (Vehicle vehicle : response.getBody()) {
                System.out.println(vehicle);
            }

        }

        @Test
        void e_deleteVehicle() {

            restTemplate.delete(BASE_URL + "/delete/" + vehicle.getVehicleId());

            System.out.println("Vehicle deleted successfully.");

        }

    }
