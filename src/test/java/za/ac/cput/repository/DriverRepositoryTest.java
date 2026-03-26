package za.ac.cput.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Driver;
import za.ac.cput.factory.DriverFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DriverRepositoryTest {
    private DriverRepository repository;
    private Driver driver;

    @BeforeEach
    void setUp() {
        repository = DriverRepository.getInstance();
        driver = DriverFactory.buildDriver(
                "Peter", "Burger", "LIC123456", "0821234567", true);
    }

    @Test
    void testCreate() {
        Driver created = repository.create(driver);
        assertNotNull(created);
        assertEquals(driver.getId(), created.getId());
        System.out.println(created);
    }

    @Test
    void testRead() {
        repository.create(driver);
        Driver read = repository.read(driver.getId());
        assertNotNull(read);
        assertEquals(driver.getId(), read.getId());
        System.out.println(read);
    }

    @Test
    void testUpdate() {
        repository.create(driver);
        Driver updated = new Driver.Builder()
                .setId(driver.getId())
                .setFirstName("Jane")
                .setLastName("Burger")
                .setLicenseNumber("LIC999")
                .setPhoneNumber("0839999999")
                .setAvailability(false)
                .build();
        Driver result = repository.update(updated);
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        System.out.println(updated);
    }

    @Test
    @Disabled
    void testDelete() {
        repository.create(driver);
        boolean deleted = repository.delete(driver.getId());
        assertTrue(deleted);
        assertNull(repository.read(driver.getId()));
    }
    @Test
    void testGetAllDrivers() {
        List<Driver> allDrivers = repository.getAllDrivers();
        System.out.println("All Drivers: "+ allDrivers);
    }
}
