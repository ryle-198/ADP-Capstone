package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Route;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import za.ac.cput.repository.VehicleRepo.VehicleRepository;

/**
 * Checks that the driver and vehicle on a route really exist, so the caller gets a clear
 * 400 instead of a foreign-key 500. Call check(route) at the start of RouteService create/update.
 */
@Component
public class RouteAssignmentCheck {

    private DriverRepository driverRepository;
    private VehicleRepository vehicleRepository;

    @Autowired
    RouteAssignmentCheck(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void check(Route route) {
        if (route == null) {
            throw bad("Route is required");
        }
        if (route.getDriver() == null || route.getDriver().getId() == null
                || !this.driverRepository.existsById(route.getDriver().getId())) {
            throw bad("Driver does not exist");
        }
        if (route.getVehicle() != null && route.getVehicle().getVehicleId() != null
                && !this.vehicleRepository.existsById(route.getVehicle().getVehicleId())) {
            throw bad("Vehicle does not exist");
        }
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}