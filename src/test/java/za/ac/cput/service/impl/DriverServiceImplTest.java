package za.ac.cput.service.impl;
/*
DriverServiceImplTest.java
Driver service test
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Driver;
import za.ac.cput.factory.DriverFactory;
import za.ac.cput.service.DriverService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DriverServiceImplTest {
    @Autowired
    private DriverService service;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.buildDriver(
                "Angel", "Masonganye", "LIC223008869", "0821234567", true);
    }

    @Test
    void testCreate() {
        Driver created = service.create(driver);
        assertNotNull(created);
        assertEquals(driver.getId(), created.getId());
        System.out.println(created);
    }

    @Test
    void testRead() {
        service.create(driver);
        Driver read = service.read(driver.getId());
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void testUpdate() {
        service.create(driver);
        Driver updated = new Driver.Builder()
                .copy(driver)
                .setFirstName("Dineo")
                .setAvailability(false)
                .build();
        Driver result = service.update(updated);
        assertNotNull(result);
        assertEquals("Dineo", result.getFirstName());
        System.out.println(result);
    }

    @Test
    void testDelete() {
        service.create(driver);
        boolean deleted = service.delete(driver.getId());
        assertTrue(deleted);
        assertNull(service.read(driver.getId()));
    }
}