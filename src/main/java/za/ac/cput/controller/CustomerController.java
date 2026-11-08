package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Customer;
import za.ac.cput.service.Customer.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService service;

    @Autowired
    CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Customer createCustomer(@RequestBody Customer customer) {
        return this.service.create(customer);
    }

    @GetMapping("/read/{customerId}")
    public Customer readCustomer(@PathVariable String customerId) {
        return this.service.read(customerId);
    }

    @PutMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return this.service.update(customer);
    }

    @DeleteMapping("/delete/{customerId}")
    public boolean deleteCustomer(@PathVariable String customerId) {
        return this.service.delete(customerId);
    }

    @GetMapping("/getAll")
    public List<Customer> getAllCustomers() {
        return this.service.getAll();
    }
}
