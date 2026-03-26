package za.ac.cput.repository.CustomerRepo;

import za.ac.cput.domain.Customer;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface ICustomerRepository extends IRepository<Customer, String> {
    List<Customer> getAll();
}
