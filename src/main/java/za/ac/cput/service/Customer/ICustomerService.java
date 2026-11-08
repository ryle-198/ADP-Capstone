package za.ac.cput.service.Customer;

import za.ac.cput.domain.Customer;
import za.ac.cput.service.IService;

import java.util.List;

public interface ICustomerService extends IService<Customer, String> {

    List<Customer> getAll();
}