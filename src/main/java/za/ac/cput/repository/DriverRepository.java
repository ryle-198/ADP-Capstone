package za.ac.cput.repository;

import za.ac.cput.domain.Driver;

import java.util.HashMap;

public class DriverRepository implements IDriverRepository {
    private final HashMap<String, Driver> store = new HashMap<>();
    private static DriverRepository instance;

    private DriverRepository() {}

    public static DriverRepository getInstance() {
        if (instance == null)
            instance = new DriverRepository();
        return instance;
    }

    @Override
    public Driver create(Driver driver) {
        store.put(driver.getId(), driver);
        return driver;
    }

    @Override
    public Driver read(String id) {
        return store.get(id);
    }

    @Override
    public Driver update(Driver driver) {
        if (store.containsKey(driver.getId())) {
            store.put(driver.getId(), driver);
            return driver;
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}