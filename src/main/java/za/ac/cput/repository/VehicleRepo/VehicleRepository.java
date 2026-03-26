package za.ac.cput.repository.VehicleRepo;

/*
VehicleFactoryTest.java
Vehicle model class
Author: Litha Owethu Mazibuko (240143485)
Date:March 2026
 */

import za.ac.cput.domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private static IVehicleRepository repository = null;
    private List<Vehicle> vehicleList;

    private VehicleRepository() {
        vehicleList = new ArrayList<>();
    }

    public static IVehicleRepository getRepository() {
        if (repository == null) {
            repository = new VehicleRepository();
        }
        return repository;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
     if (vehicle == null) {
         return null;
     }

     if(vehicleList.add(vehicle)) {
         return vehicle;
     }
        return null;
    }

    @Override
    public Vehicle read(String vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return vehicle;
            }
            }
            return null;
        }

    @Override
    public Vehicle update(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
                }

        Vehicle existing = read(vehicle.getVehicleId());
        if(existing == null) {
            return null;
        }

        boolean success = vehicleList.remove(existing);
        if(!success){
            return null;
        }
        if(vehicleList.add(vehicle)){
            return vehicle;
        }

        return null;
    }

    @Override
    public boolean delete(String id) {
        Vehicle vehicleToDelete = read(id);
        if (vehicleToDelete == null) {
            return false;
        }
        return (vehicleList.remove(vehicleToDelete));
    }

    @Override
    public List<Vehicle> getAll(){
        return vehicleList;
    }

}
