package za.ac.cput.repository;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Driver;

import java.util.List;

public interface IDriverRepository extends IRepository<Driver, String> {
    List<Driver> getAllDrivers();
}