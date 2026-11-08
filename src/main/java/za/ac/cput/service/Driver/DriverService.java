package za.ac.cput.service.Driver;
/*
DriverServiceImpl.java
Driver service implementation
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Driver;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import za.ac.cput.repository.UserRepo.UserRepository;

import java.util.List;

@Service
public class DriverService implements IDriverService {
    private final DriverRepository repository;
    private final UserRepository userRepository;


    public DriverService(DriverRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Driver create(Driver driver) {
        return repository.save(driver);
    }

    @Override
    public Driver read(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Driver update(Driver driver) {
        if (repository.existsById(driver.getId())) {
            return repository.save(driver);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (!repository.existsById(id)) {
            return false;
        }
        try {
            userRepository.deleteByDriver_Id(id);
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Driver is still assigned to routes");
        }
        return true;
    }

    @Override
    public List<Driver> getAll() {
        return repository.findAll();
    }
}