package za.ac.cput.service.Driver;

import za.ac.cput.domain.Driver;
import za.ac.cput.service.IService;

import java.util.List;

public interface IDriverService extends IService<Driver, String> {
    List<Driver> getAll();
}
