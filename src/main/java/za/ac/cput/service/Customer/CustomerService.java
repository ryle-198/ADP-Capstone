package za.ac.cput.service.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Customer;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.UserRepo.UserRepository;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private CustomerRepository repository;
    private DeliveryOrdersRepository orderRepository;
    private UserRepository userRepository;

    @Autowired
    CustomerService(CustomerRepository repository,
                    DeliveryOrdersRepository orderRepository,
                    UserRepository userRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Customer create(Customer customer) {
        return this.repository.save(customer);
    }

    @Override
    public Customer read(String customerId) {
        return this.repository.findById(customerId).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        return this.repository.save(customer);
    }

    @Override
    public boolean delete(String customerId) {
        if (!this.repository.existsById(customerId)) {
            return false;
        }

        if (!this.orderRepository.findByCustomer_CustomerId(customerId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer still has orders");
        }
        // their login account goes with them
        this.userRepository.deleteByCustomer_CustomerId(customerId);
        this.repository.deleteById(customerId);
        return true;
    }

    @Override
    public List<Customer> getAll() {
        return this.repository.findAll();
    }
}