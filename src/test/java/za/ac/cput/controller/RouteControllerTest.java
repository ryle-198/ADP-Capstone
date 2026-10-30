package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.Route;
import za.ac.cput.factory.DriverFactory;
import za.ac.cput.factory.RouteFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
class RouteControllerTest {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected static String BASE_URL = "http://localhost:8080/freightanddelivery/route";

    private final String ROOT = "http://localhost:8080/freightanddelivery";
    private Driver driver;
    private Route route1;

    @BeforeAll
     void setUp() {
        RestTemplate rest = new RestTemplate();
        driver = rest.postForObject(ROOT + "/driver/create",
                DriverFactory.buildDriver("Peter", "Burger",
                        "LIC-RC-" + UUID.randomUUID().toString().substring(0, 8),
                        "0821234567", true),
                Driver.class);

        route1 = RouteFactory.createRoute("R-RC01", driver, null,
                "Cape Town", "Atlantis", 65.3f, 51);
    }

    @Test
    void a_createRoute() {
        String url = BASE_URL+"/create";
        ResponseEntity<Route> response = this.restTemplate.postForEntity(url,route1,Route.class);
        assertNotNull(response);
        System.out.println(response.getBody());
    }

    @Test
    void b_readRoute() {
        String url = BASE_URL+"/read/"+route1.getRouteId();
        ResponseEntity<Route> response = this.restTemplate.getForEntity(url,Route.class);
        assertNotNull(response);
        System.out.println(response.getBody());
    }

    @Test
    void c_updateRoute() {
        String url = BASE_URL+"/update";
        Route updateRoute = new Route.Builder().copy(route1).setEndLocation("Kraaifontein").build();
        this.restTemplate.put(url,updateRoute);
        String getUpdatedRoute = BASE_URL+"/read/"+route1.getRouteId();
        ResponseEntity<Route> response = this.restTemplate.getForEntity(getUpdatedRoute,Route.class);
        assertNotNull(response);
        System.out.println(response.getBody());
    }

    @Test
    void e_deleteRoute() {
        String url = BASE_URL+"/delete/"+route1.getRouteId();
        this.restTemplate.delete(url);
        ResponseEntity<Route> response = this.restTemplate.getForEntity(BASE_URL+"/read/"+route1.getRouteId(), Route.class);

    }

    @Test
    void d_getAllRoutes() {
        String url = BASE_URL+"/getAllRoute";
        ResponseEntity<Route[]> response = this.restTemplate.getForEntity(url, Route[].class);
        System.out.println("All Routes");
        for(Route allRoutes:response.getBody()){
            System.out.println(allRoutes);
        }
    }
}