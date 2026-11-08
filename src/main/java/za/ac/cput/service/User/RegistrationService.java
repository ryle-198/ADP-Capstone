package za.ac.cput.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.User;
import za.ac.cput.dto.UserDto.CreateUserRequest;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import za.ac.cput.repository.UserRepo.UserRepository;

import java.util.Map;

@Service
public class RegistrationService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private CustomerRepository customerRepository;
    private DriverRepository driverRepository;
    private UserRepository userRepository;
    private IUserService userService;

    @Autowired
    RegistrationService(CustomerRepository customerRepository,
                        DriverRepository driverRepository,
                        UserRepository userRepository,
                        IUserService userService) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Customer registerCustomer(Map<String, String> body) {
        if (body == null) {
            throw bad("Details are required");
        }
        String username = clean(body.get("username"));
        String password = body.get("password");
        String name = clean(body.get("customerName"));
        String email = clean(body.get("email"));

        if (username == null || name == null || email == null || password == null) {
            throw bad("Username, password, name and email are required");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw bad("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (this.userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        Customer customer;
        try {

            customer = this.customerRepository.save(new Customer.Builder()
                    .setCustomerName(name)
                    .setPhoneNumber(clean(body.get("phoneNumber")))
                    .setEmail(email)
                    .setAddress(clean(body.get("address")))
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        try {
            this.userService.create(new CreateUserRequest(
                    username, password, User.Role.CUSTOMER, null, customer.getCustomerId()));
        } catch (RuntimeException e) {
            this.customerRepository.deleteById(customer.getCustomerId());
            throw e;
        }
        return customer;
    }

    public Driver registerDriver(Map<String, Object> body) {
        if (body == null) {
            throw bad("Details are required");
        }
        String username = clean(text(body, "username"));
        String password = text(body, "password");
        String firstName = clean(text(body, "firstName"));
        String lastName = clean(text(body, "lastName"));
        String license = clean(text(body, "licenseNumber"));

        if (username == null || password == null || firstName == null || lastName == null || license == null) {
            throw bad("Username, password, first name, last name and license number are required");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw bad("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (this.userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        boolean available = !"false".equalsIgnoreCase(text(body, "availability"));

        Driver driver;
        try {
            driver = this.driverRepository.save(new Driver.Builder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setLicenseNumber(license)
                    .setPhoneNumber(clean(text(body, "phoneNumber")))
                    .setAvailability(available)
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "License number already registered");
        }

        try {
            this.userService.create(new CreateUserRequest(
                    username, password, User.Role.DRIVER, driver.getId(), null));
        } catch (RuntimeException e) {
            this.driverRepository.deleteById(driver.getId());
            throw e;
        }
        return driver;
    }

    private static String text(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private static String clean(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}