package za.ac.cput.repository.CustomerRepo;

import za.ac.cput.domain.Customer;
import za.ac.cput.repository.IRepository;

import java.util.List;

/*
CustomerRepository.java
Customer module class
Author: YAMKELA MGCUBHE (222040114)
Date: 2026
 */

public interface ICustomerRepository extends IRepository<Customer, String> {
    List<Customer> getAll();
}
