package za.ac.cput.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import za.ac.cput.domain.Route;
import za.ac.cput.factory.RouteFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteServiceTest {
    @Autowired
    private RouteService routeService;

    private static Route route1 = RouteFactory.createRoute("R001", "D001", "Cape Town", "Atlantis", 65.3f, 51);
//    @Autowired
//    private DataSourceTransactionManager dataSourceTransactionManager;

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
        assertNotNull(updateRoute);
        System.out.println(updateRoute);
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