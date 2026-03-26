package za.ac.cput.repository.DriverRepo;

import za.ac.cput.domain.Driver;

import java.util.ArrayList;
import java.util.List;

public class DriverRepository implements IDriverRepository {
//    private final HashMap<String, Driver> store = new HashMap<>();
//    private final HashMap<String, Driver> store;
    private List<Driver> store;
    private static DriverRepository instance;

    private DriverRepository() {
        store= new ArrayList<>();
    }

    public static DriverRepository getInstance() {
        if (instance == null)
            instance = new DriverRepository();
        return instance;
    }

    @Override
    public Driver create(Driver driver) {
//        store.put(driver.getId(), driver);
        store.add(driver);
        return driver;
    }

    @Override
    public Driver read(String id) {
//
        for(Driver drivers:store){
            if(drivers.getId().equals(id)){
                return drivers;
            }
        }
        return null;
    }

    @Override
    public Driver update(Driver driver) {
        String id = driver.getId();
        Driver oldDriver = read(id);

        if(oldDriver==null){
            return null;
        }

        boolean success = store.remove(oldDriver);

        if(!success){
            return null;
        }

        if(store.add(driver)){
            return driver;
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        Driver driverToDelete = read(id);

        if(driverToDelete==null){
            return false;
        }
        return store.remove(driverToDelete);
    }

    @Override
    public List<Driver> getAllDrivers() {
            return store;
    }

}
