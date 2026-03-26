package za.ac.cput.repository.CustomerRepo;

import za.ac.cput.domain.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {

    private static CustomerRepository repository = null;
    private List<Customer> customerList;

    private CustomerRepository() {
        customerList = new ArrayList<>();
    }

    public static CustomerRepository getRepository() {
        if (repository == null) {
            repository = new CustomerRepository();
        }
        return repository;

    }

    @Override
    public Customer create(Customer customer) {
        customerList.add(customer);
        return customer;
    }

    @Override
    public Customer read(String customerId) {
        for (Customer customer1 : customerList) {
            if (customer1.getCustomerId().equals(customerId)) {
                return customer1;
            }
        }
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        Customer existingCustomer = read(customer.getCustomerId());
        if (existingCustomer != null) {
            customerList.remove(existingCustomer);
            customerList.add(customer);
            return customer;
        }
        return null;
    }

    @Override
    public boolean delete(String customerId) {
        Customer CustomerToDelete = read(customerId);
        if (CustomerToDelete != null) {
            customerList.remove(CustomerToDelete);
            return true;
        }
        return false;
    }

    @Override
    public List<Customer> getAll() {
        return customerList;
    }

}




