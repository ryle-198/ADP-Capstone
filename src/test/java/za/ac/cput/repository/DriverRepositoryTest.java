package za.ac.cput.repository;
/*
DriverRepositoryTest.java
Driver repository test
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Driver;
import za.ac.cput.factory.DriverFactory;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DriverRepositoryTest {
    @Autowired
    private DriverRepository repository;

    private Driver driver;

    @BeforeEach
    void a_setUp() {
        driver = DriverFactory.buildDriver(
                "Angel", "Masonganye", "LIC223008869", "0821234567", true);
    }

    @Test
    void b_testCreate() {
        Driver created = repository.save(driver);
        assertNotNull(created);
        System.out.println(created);
    }

    @Test
    void c_testRead() {
        repository.save(driver);
        Driver read = repository.findById(driver.getId()).orElse(null);
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void d_testUpdate() {
        repository.save(driver);
        Driver updated = new Driver.Builder()
                .copy(driver)
                .setFirstName("Dineo")
                .build();
        Driver result = repository.save(updated);
        assertNotNull(result);
        assertEquals("Dineo", result.getFirstName());
        System.out.println(result);
    }

    @Test
    void e_testDelete() {
        repository.save(driver);
        repository.deleteById(driver.getId());
        assertFalse(repository.existsById(driver.getId()));
    }
}