package za.ac.cput.factory;

import za.ac.cput.domain.Vehicle;

import java.time.LocalDate;

import static za.ac.cput.util.Helper.isEmptyOrNull;
import static za.ac.cput.util.Helper.isNumNeg;

/*
VehicleFactory.java
Vehicle factory model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

public class VehicleFactory {

    public static Vehicle createVehicle(
            String vehicleId,
            String numberPlate,
            Vehicle.VehicleType type,
            float capacity,
            Vehicle.VehicleStatus currentStatus,
            float mileage,
            LocalDate lastService) {

        if (isEmptyOrNull(vehicleId)
                || isEmptyOrNull(numberPlate)
                || type == null
                || currentStatus == null
                || lastService == null) {
            return null;
        }

        if (isNumNeg(capacity) || isNumNeg(mileage)) {
            return null;
        }

        return new Vehicle.Builder()
                .setVehicleId(vehicleId)
                .setNumberPlate(numberPlate)
                .setType(type)
                .setCapacity(capacity)
                .setCurrentStatus(currentStatus)
                .setMileage(mileage)
                .setLastService(lastService)
                .build();

    }

}
