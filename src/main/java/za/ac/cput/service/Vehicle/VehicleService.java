package za.ac.cput.service.Vehicle;

/*
VehicleService.java
Vehicle service model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.repository.VehicleRepo.VehicleRepository;

import java.util.List;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository repository;


    @Autowired
    public VehicleService(VehicleRepository repository){
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
            try {
                repository.deleteById(vehicleId);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is still used by routes");
            }
            return true;
        }

        return false;
    }


    @Override
    public List<Vehicle> getAllVehicles(){
        return repository.findAll();
    }
}