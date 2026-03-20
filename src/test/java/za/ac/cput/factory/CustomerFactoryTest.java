package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactoryTest {
    @Test
    void createCustomer_Success() {
        Customer customer = CustomerFactory.createCustomer("CUST-23451", "Yamkela", "0732510842",
                "14 Aquarius Av Sandrift Milnerton 7441");

        assertNotNull(customer);
        assertEquals("CUST-23451", customer.getCustomerId());
        assertEquals("Yamkela",customer.getCustomerName());
        assertEquals("0732510842",customer.getPhoneNumber());
        assertEquals("14 Aquarius Av Sandrift Milnerton 7441",customer.getAddress());
    }
    /*@Test
    void create_InvalidPhoneNumber() {
        Customer customer = CustomerFactory.createCustomer("CUST-23451", "Yamkela", "1234",
                "14 Aquarius Av Sandrift Milnerton 7441");
        assertNull(customer);
    }*/
    @Test
    void createCustomer_NullValues() {
        Customer customer = CustomerFactory.createCustomer(null, "Yamkela","0732510842",
                "14 Aquarius Av Sandrift Milnerton 7441");
        assertNull(customer);
    }

}
