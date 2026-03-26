package za.ac.cput.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Route;
import za.ac.cput.factory.RouteFactory;
import za.ac.cput.repository.RouteRepo.IRouteRepository;
import za.ac.cput.repository.RouteRepo.RouteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteRepositoryTest {
    private static IRouteRepository repository = RouteRepository.getRepository();
    private static Route route = RouteFactory.createRoute("R003", "D003", "Cape Town", "Delft", 25.4f, 28);

    @Test
    void a_create() {
        Route created = repository.create(route);
        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void b_read() {
        Route read = repository.read(route.getRouteId());
        assertEquals(route.getRouteId(), read.getRouteId());
        System.out.println("Read: " + read);
    }

    @Test
    void c_update() {
        Route newRoute = new Route.Builder().copy(route)
                .setDistance(95.7f)
                .build();
        Route updated = repository.update(newRoute);
        assertNotNull(updated);
        System.out.println("Updated: " + updated);
    }

    @Test
    @Disabled
    void d_delete() {
        boolean deleted = repository.delete(route.getRouteId());
        assertTrue(deleted);
        System.out.println("Deleted: " + deleted);
    }

    @Test
    void e_getAll() {
        List<Route> routes = repository.getAll();
        System.out.println("Routes: " + routes);
    }
}