package za.ac.cput.service;

/*
VehicleService.java
Vehicle service model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.repository.VehicleRepo.IVehicleRepository;

import java.util.List;

@Service
public class VehicleService implements IVehicleService {

    private final IVehicleRepository repository;


    @Autowired
    public VehicleService(IVehicleRepository repository){
        this.repository = repository;
    }


    @Override
    public Vehicle create(Vehicle vehicle){
        return repository.save(vehicle);
    }


    @Override
    public Vehicle read(String vehicleId){
        return repository.findById(vehicleId).orElse(null);
    }


    @Override
    public Vehicle update(Vehicle vehicle){
        if(repository.existsById(vehicle.getVehicleId())){
            return repository.save(vehicle);
        }

        return null;
    }


    @Override
    public boolean delete(String vehicleId){

        if(repository.existsById(vehicleId)){
            repository.deleteById(vehicleId);
            return true;
        }

        return false;
    }


    @Override
    public List<Vehicle> getAllVehicles(){
        return repository.findAll();
    }
}

