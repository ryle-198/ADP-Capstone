package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.Route;
import za.ac.cput.factory.DriverFactory;
import za.ac.cput.factory.RouteFactory;
import za.ac.cput.service.Driver.DriverService;
import za.ac.cput.service.Route.RouteService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteServiceTest {
    @Autowired
    private RouteService routeService;
    @Autowired
    private DriverService driverService;
    private static Route route1;


//    RouteFactory.createRoute("R001", "D001", "Cape Town", "Atlantis", 65.3f, 51);
//    @Autowired
//    private DataSourceTransactionManager dataSourceTransactionManager;

    @BeforeAll
     void setUp(){
        Driver driver = driverService.create(DriverFactory.buildDriver("Peter", "Burger",
                "LIC-RT-" + UUID.randomUUID().toString().substring(0, 8), "0821234567", true));
        route1 = RouteFactory.createRoute("R001", driver, null, "Cape Town", "Atlantis", 65.3f, 51);
    }

    @Test
    void a_create() {
        Route create = this.routeService.create(route1);
        assertNotNull(create);
        System.out.println(create);
    }

    @Test
    void b_read() {
        Route read = this.routeService.read(route1.getRouteId());
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void c_update() {
        Route updateRoute = new Route.Builder().copy(route1).setEndLocation("Kraaifontein").build();
        Route updated = routeService.update(updateRoute);
        assertEquals("Kraaifontein", updated.getEndLocation());
    }
    @Test
    void e_delete() {
        boolean delete = this.routeService.delete(route1.getRouteId());
        assertTrue(delete);
        System.out.println(delete);
    }

    @Test
    void d_getAllRoutes() {
        List<Route> getAll = this.routeService.getAllRoutes();
        System.out.println(getAll);
    }
}