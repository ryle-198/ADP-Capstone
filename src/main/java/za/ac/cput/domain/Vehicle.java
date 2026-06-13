package za.ac.cput.domain;

import java.time.LocalDate;


/*
Vehicle.java
Vehicle model class
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

public class Vehicle {
    public enum VehicleType{
        TRUCK,
        VAN,
        BAKKIE,
        MOTORBIKE,
        TRAILER
    }

    public enum VehicleStatus{
        AVAILABLE,
        IN_USE,
        IN_SERVICE,
        OUT_OF_SERVICE,
        RESERVED
    }

    private String vehicleId;
     private String numberPlate;
     private VehicleType type;
     private float capacity;
     private VehicleStatus currentStatus;
     private float mileage;
     private LocalDate lastService;

    private Vehicle(){
    }

public Vehicle(String vehicleId, String numberPlate, VehicleType type, float capacity, VehicleStatus currentStatus, float mileage, LocalDate lastService) {
        this.vehicleId = vehicleId;
        this.numberPlate = numberPlate;
        this.type = type;
        this.capacity = capacity;
        this.currentStatus = currentStatus;
        this.mileage = mileage;
        this.lastService = lastService;
}

    private Vehicle(Builder builder) {
        this.vehicleId = builder.vehicleId;
        this.numberPlate = builder.numberPlate;
        this.type = builder.type;
        this.capacity = builder.capacity;
        this.currentStatus = builder.currentStatus;
        this.mileage = builder.mileage;
        this.lastService = builder.lastService;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public VehicleType getType() {
        return type;
    }

    public float getCapacity() {
        return capacity;
    }

    public VehicleStatus getCurrentStatus() {
        return currentStatus;
    }

    public float getMileage() {
        return mileage;
    }

    public LocalDate getLastService() {
        return lastService;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", numberPlate" + numberPlate + '\'' +
                ", type='" + type + '\'' +
                ", capacity='" + capacity + '\'' +
                ", currentStatus=" + currentStatus + '\'' +
                ", mileage=" + mileage + '\'' +
                ", lastService" + lastService +
                '}';
    }

    public static class Builder {
        private String vehicleId;
        private String numberPlate;
        private VehicleType type;
        private float capacity;
        private VehicleStatus currentStatus;
        private float mileage;
        private LocalDate lastService;

        public Builder setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder setNumberPlate(String numberPlate) {
            this.numberPlate = numberPlate;
            return this;
        }

        public Builder setType(VehicleType type) {
            this.type = type;
            return this;
        }

        public Builder setCapacity(float capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setCurrentStatus(VehicleStatus currentStatus) {
            this.currentStatus = currentStatus;
            return this;
        }

        public Builder setMileage(float mileage) {
            this.mileage = mileage;
            return this;
        }

        public Builder setLastService(LocalDate lastService) {
            this.lastService = lastService;
            return this;
        }

        public Builder copy(Vehicle vehicle) {
            this.vehicleId = vehicle.vehicleId;
            this.numberPlate = vehicle.numberPlate;
            this.type = vehicle.type;
            this.capacity = vehicle.capacity;
            this.currentStatus = vehicle.currentStatus;
            this.mileage = vehicle.mileage;
            this.lastService =vehicle.lastService;
            return this;

        }

        public Vehicle build() {
            return new Vehicle(this);
        }
    }

}

































