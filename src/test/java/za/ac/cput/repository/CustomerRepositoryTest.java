package za.ac.cput.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Customer;
import za.ac.cput.factory.CustomerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryTest {
    private CustomerRepository repository;
    private Customer customer;

    @BeforeEach
    void setUp() {
        repository = CustomerRepository.getRepository();
        repository.getAll().clear();
        customer = CustomerFactory.createCustomer("CUST-23451", "Yamkela", "0732510842",
                "14 Aquarius Av Sandrift Milnerton 7441");
    }
    @Test
    void testCreate() {
       Customer created = repository.create(customer);
       assertNotNull(created);
       assertEquals(customer.getCustomerId(), created.getCustomerId());

    }

    @Test
    void testRead() {
        repository.create(customer);
        Customer read = repository.read(customer.getCustomerId());
        assertNotNull(read);
        assertEquals(customer.getCustomerId(), read.getCustomerId());

    }

    @Test
    void testUpdate() {
        repository.create(customer);
        Customer updated = new Customer.Builder()
                .copy(customer)
                .setCustomerName("The updated name is: ")
                .build();
        Customer result = repository.update(updated);
        assertNotNull(result);
        assertEquals("The updated name is: ", result.getCustomerName());
    }

    @Test
    void testDelete() {
        repository.create(customer);
        boolean deleted = repository.delete(customer.getCustomerId());
        assertTrue(deleted);
        assertNull(repository.read(customer.getCustomerId()));

    }



}
