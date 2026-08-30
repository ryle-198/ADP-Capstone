package za.ac.cput.service;
/*
DriverServiceImpl.java
Driver service implementation
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Driver;
import za.ac.cput.repository.DriverRepo.IDriverRepository;

import java.util.List;

@Service
public class DriverService implements IDriverService {
    private final IDriverRepository repository;


    public DriverService(IDriverRepository repository) {
        this.repository = repository;
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
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Driver> getAll() {
        return repository.findAll();
    }
}
