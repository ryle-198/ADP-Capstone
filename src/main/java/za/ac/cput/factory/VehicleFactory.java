package za.ac.cput.factory;

import za.ac.cput.domain.Vehicle;
import za.ac.cput.util.Helper;


import java.time.LocalDate;

import static za.ac.cput.util.Helper.*;

/*
VehicleFactory.java
Vehicle model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */


public class VehicleFactory {


    public static Vehicle createVehicle(String vehicleId, String numberPlate, Vehicle.VehicleType type, float capacity, Vehicle.VehicleStatus currentStatus, float mileage, LocalDate lastService) {
        if (isEmptyOrNull(vehicleId) ||
                isEmptyOrNull(numberPlate) ||
                type == null||
                currentStatus == null ||
                 lastService == null) {
            return null;
        }


        if (isNumNeg(capacity) || isNumNeg(mileage)) {
            return null;
        }

        if(Helper.isValidType(type)|| Helper.isValidType(currentStatus)){
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
