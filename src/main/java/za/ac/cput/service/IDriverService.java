package za.ac.cput.service;

import za.ac.cput.domain.Driver;

import java.util.List;

public interface IDriverService extends IService<Driver, String> {
    List<Driver> getAll();
}
