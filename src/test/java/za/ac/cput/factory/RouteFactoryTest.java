package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.Route;
import za.ac.cput.domain.Vehicle;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
RouteFactoryTest.java
Route module class
Author: Ntombozuko Palisa Mchophele(240455789)
Date: 2026
 */

@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteFactoryTest {
    private static Route route1, route2;
    private Driver driver;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.buildDriver("Peter", "Burger", "LIC123456", "0821234567", true);
        vehicle = VehicleFactory.createVehicle("V001", "CAA24680", Vehicle.VehicleType.TRUCK,
                16000f, Vehicle.VehicleStatus.AVAILABLE, 18000f, LocalDate.of(2025, 12, 10));
        route1 = RouteFactory.createRoute("R001", driver, vehicle, "Cape Town", "Atlantis", 65.3f, 51);
        route2 = RouteFactory.createRoute("", driver, vehicle, "Cape Town", "Stellenbosch", 50.4f, 44);
    }

    @Test
    void a_createRoute() {
        assertNotNull(route1);
        System.out.println(route1.toString());
    }

    @Test
    void b_testRouteThatFails() {
        assertNull(route2);
        System.out.println(route2);
    }

    @Test
    void c_routeWithoutVehicleIsAllowed() {
        assertNotNull(RouteFactory.createRoute("R003", driver, null, "Cape Town", "Paarl", 60f, 50));
    }

    @Test
    void d_routeWithoutDriverFails() {
        assertNull(RouteFactory.createRoute("R004", null, vehicle, "Cape Town", "Paarl", 60f, 50));
    }
}