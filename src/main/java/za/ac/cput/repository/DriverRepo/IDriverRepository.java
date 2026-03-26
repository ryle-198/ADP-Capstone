package za.ac.cput.repository.DriverRepo;

import za.ac.cput.domain.Driver;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface IDriverRepository extends IRepository<Driver, String> {
    List<Driver> getAllDrivers();
}