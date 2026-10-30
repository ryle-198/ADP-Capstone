package za.ac.cput.controller;
/*
DriverControllerTest.java
Driver controller test
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Driver;
import za.ac.cput.factory.DriverFactory;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DriverControllerTest {
    @Autowired
    private DriverController controller;

    private static Driver driver;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.buildDriver("Angel", "Masonganye",
                "LIC" + UUID.randomUUID().toString().substring(0, 8), "0821234567", true);
    }

    @Test
    void a_testCreate() {
        ResponseEntity<Driver> response = controller.create(driver);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        System.out.println(response.getBody());
    }

    @Test
    void b_testRead() {
        controller.create(driver);
        ResponseEntity<Driver> response = controller.read(driver.getId());
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
    }

    @Test
    void c_testUpdate() {
        controller.create(driver);
        Driver updated = new Driver.Builder()
                .copy(driver)
                .setFirstName("Dineo")
                .build();
        ResponseEntity<Driver> response = controller.update(updated);
        assertNotNull(response.getBody());
        assertEquals("Dineo", response.getBody().getFirstName());
        System.out.println(response.getBody());
    }

    @Test
    void d_testDelete() {
        controller.create(driver);
        ResponseEntity<Boolean> response = controller.delete(driver.getId());
        assertTrue(response.getBody());
    }
}