package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Route;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteFactoryTest {
    private static Route route1, route2;

    @BeforeEach
    void setUp() {
        route1 = RouteFactory.createRoute("R001", "D001", "Cape Town", "Atlantis", 65.3f, 51);
        route2 = RouteFactory.createRoute("", "D002", "Cape Town", "Stellenbosch", 50.4f, 44);
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
}