package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactoryTest {
    @Test
    void createCustomer_Success() {
        Customer customer = CustomerFactory.createCustomer("CUST-23451"
                , "Yamkela", "0732510842"
                ,"yamkela197@gmail.com"
                ,"14 Aquarius Av Sandrift Milnerton 7441");

        assertNotNull(customer);
        assertEquals("CUST-23451", customer.getCustomerId());
        assertEquals("Yamkela",customer.getCustomerName());
        assertEquals("0732510842",customer.getPhoneNumber());
        assertEquals("yamkela197@gmail.com", customer.getEmail() );
        assertEquals("14 Aquarius Av Sandrift Milnerton 7441",customer.getAddress());
        System.out.println("created customer successfully");
    }

    @Test
    void createCustomer_NullValues() {
        Customer customer = CustomerFactory.createCustomer(null, "Yamkela","0732510842"
                ,"yamkela197@gmail.com"
                ,"14 Aquarius Av Sandrift Milnerton 7441");
        assertNull(customer);
        System.out.println("customer has null value");
    }

}
