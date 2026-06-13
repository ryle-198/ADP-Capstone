package za.ac.cput.repository.DriverRepo;

import za.ac.cput.domain.Driver;
import za.ac.cput.repository.IRepository;

import java.util.List;

/*
IDriverRepository.java
Driver module class
Author: Angel Dineo Masonganye (223008869)
Date: 2026
 */

public interface IDriverRepository extends IRepository<Driver, String> {
    List<Driver> getAllDrivers();
}