package za.ac.cput.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.User;
import za.ac.cput.dto.UserDto.CreateUserRequest;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import za.ac.cput.repository.UserRepo.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private UserRepository repository;
    private DriverRepository driverRepository;
    private CustomerRepository customerRepository;
    private PasswordEncoder encoder;

    @Autowired
    UserService(UserRepository repository,
                DriverRepository driverRepository,
                CustomerRepository customerRepository,
                PasswordEncoder encoder) {
        this.repository = repository;
        this.driverRepository = driverRepository;
        this.customerRepository = customerRepository;
        this.encoder = encoder;
    }

    @Override
    public User create(CreateUserRequest request) {
        if (request == null || isBlank(request.username())) {
            throw bad("Username is required");
        }
        String username = request.username().trim();
        checkPassword(request.password());
        if (request.role() == null) {
            throw bad("Role is required");
        }
        if (this.repository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        Driver driver = null;
        Customer customer = null;

        switch (request.role()) {
            case DRIVER -> {
                if (isBlank(request.driverId()) || !isBlank(request.customerId())) {
                    throw bad("A DRIVER user needs a driverId and must not have a customerId");
                }
                driver = this.driverRepository.findById(request.driverId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
                if (this.repository.existsByDriver_Id(driver.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "That driver already has a user account");
                }
            }
            case CUSTOMER -> {
                if (isBlank(request.customerId()) || !isBlank(request.driverId())) {
                    throw bad("A CUSTOMER user needs a customerId and must not have a driverId");
                }
                customer = this.customerRepository.findById(request.customerId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
                if (this.repository.existsByCustomer_CustomerId(customer.getCustomerId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "That customer already has a user account");
                }
            }
            default -> {
                if (!isBlank(request.driverId()) || !isBlank(request.customerId())) {
                    throw bad("ADMIN and DISPATCHER users cannot be linked to a driver or customer");
                }
            }
        }

        User user = new User.Builder()
                .setUsername(username)
                .setPasswordHash(this.encoder.encode(request.password()))
                .setRole(request.role())
                .setEnabled(true)
                .setDriver(driver)
                .setCustomer(customer)
                .build();

        try {
            return this.repository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or driver/customer link already in use");
        }
    }

    @Override
    public User read(String id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        if (this.repository.existsById(user.getId())) {
            return this.repository.save(user);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        return this.repository.findAll();
    }

    @Override
    public User setEnabled(String id, boolean enabled) {
        User existing = this.repository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        User updated = new User.Builder().copy(existing).setEnabled(enabled).build();
        return this.repository.save(updated);
    }

    @Override
    public boolean resetPassword(String id, String newPassword) {
        checkPassword(newPassword);
        User existing = this.repository.findById(id).orElse(null);
        if (existing == null) {
            return false;
        }
        User updated = new User.Builder().copy(existing)
                .setPasswordHash(this.encoder.encode(newPassword))
                .build();
        this.repository.save(updated);
        return true;
    }

    private void checkPassword(String password) {
        if (isBlank(password) || password.length() < MIN_PASSWORD_LENGTH) {
            throw bad("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}