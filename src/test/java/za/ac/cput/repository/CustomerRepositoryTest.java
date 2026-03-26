package za.ac.cput.repository;

import org.junit.jupiter.api.*;
import za.ac.cput.domain.Customer;
import za.ac.cput.factory.CustomerFactory;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CustomerRepositoryTest {
    private CustomerRepository repository;
    private Customer customer;

    @BeforeEach
    void a_setUp() {
        repository = CustomerRepository.getRepository();
        customer = CustomerFactory.createCustomer("CUST-23451", "Yamkela", "0732510842"
                ,"yamkela197@gmail.com"
                ,"14 Aquarius Av Sandrift Milnerton 7441");
    }

    @Test
    void b_testCreate() {
       Customer created = repository.create(customer);
       assertNotNull(created);
       assertEquals(customer.getCustomerId(), created.getCustomerId());
        System.out.println("Customer created.");

    }

    @Test
    void c_testRead() {
        repository.create(customer);
        Customer read = repository.read(customer.getCustomerId());
        assertNotNull(read);
        assertEquals(customer.getCustomerId(), read.getCustomerId());
        System.out.println(read);
    }

    @Test
    void d_testUpdate() {
        repository.create(customer);
        Customer updated = new Customer.Builder()
                .copy(customer)
                .setCustomerName("The updated name is: ")
                .build();
        Customer result = repository.update(updated);
        assertNotNull(result);
        assertEquals("The updated name is: ", result.getCustomerName());
        System.out.println("Customer Updated:");
        System.out.println(result);
    }

    @Test
    @Disabled
    void e_testDelete() {
        repository.create(customer);
        boolean deleted = repository.delete(customer.getCustomerId());
        assertTrue(deleted);
        assertNull(repository.read(customer.getCustomerId()));
        System.out.println("deleted successfully");

    }

    @Test
    void f_testGetAllCustomers(){
        List<Customer> getAllCustomers = repository.getAll();
        System.out.println(getAllCustomers);
    }
}
