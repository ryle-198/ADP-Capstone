package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Driver;
import za.ac.cput.service.User.RegistrationService;

import java.util.Map;

@RestController
public class RegistrationController {

    private RegistrationService service;

    @Autowired
    RegistrationController(RegistrationService service) {
        this.service = service;
    }

    // Public: a visitor signs up and gets a customer record plus a CUSTOMER login
    @PostMapping("/auth/register")
    public ResponseEntity<Customer> register(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(this.service.registerCustomer(body));
    }

    // create a customer and its login in at once
    @PostMapping("/customer/createWithLogin")
    public ResponseEntity<Customer> createWithLogin(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(this.service.registerCustomer(body));
    }

    // Admin only: create a driver and the login they use for the driver page
    @PostMapping("/driver/createWithLogin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Driver> createDriverWithLogin(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(this.service.registerDriver(body));
    }
}