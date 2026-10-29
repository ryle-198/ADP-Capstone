package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
Route.java
Route module class
Author: Ntombozuko Palisa Mchophele(240455789)
Date: 2026
 */
@Entity
public class Route {
    @Id
    private String routeId;
    private String startLocation;
    private String endLocation;
    private float distance;
    private int estimateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id")
    @JsonIgnoreProperties({"routes", "hibernateLazyInitializer", "handler"})
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @JsonIgnoreProperties({"routes", "hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    @OneToMany(mappedBy = "route")
    @JsonIgnore
    private List<Shipment> shipments = new ArrayList<>();


    public Route() {
    }

    private Route(Builder builder) {
        this.routeId = builder.routeId;
        this.driver = builder.driver;
        this.vehicle = builder.vehicle;
        this.shipments = builder.shipments;
        this.startLocation = builder.startLocation;
        this.endLocation = builder.endLocation;
        this.distance = builder.distance;
        this.estimateTime = builder.estimateTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public Driver getDriver() {
        return driver;
    }

    public Vehicle getVehicle() {return vehicle;}

    public List<Shipment> getShipments() {return shipments;}

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public float getDistance() {
        return distance;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", distance=" + distance +
                ", estimateTime=" + estimateTime +
                ", driverId='" + (driver != null ? driver.getId() : null) + '\'' +
                ", vehicleId='" + (vehicle != null ? vehicle.getVehicleId() : null) + '\'' +
                '}';
    }

    public static class Builder {
        private String routeId;
        private Driver driver;
        private Vehicle vehicle;
        private List<Shipment> shipments = new ArrayList<>();
        private String startLocation;
        private String endLocation;
        private float distance;
        private int estimateTime;

        public Builder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setDriver(Driver driver) {
            this.driver = driver;
            return this;
        }

        public Builder setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public Builder setShipments(List<Shipment> shipments){
            this.shipments=shipments == null ? new ArrayList<>() : shipments;
            return this;
        }

        public Builder setStartLocation(String startLocation) {
            this.startLocation = startLocation;
            return this;
        }

        public Builder setEndLocation(String endLocation) {
            this.endLocation = endLocation;
            return this;
        }

        public Builder setDistance(float distance) {
            this.distance = distance;
            return this;
        }

        public Builder setEstimateTime(int estimateTime) {
            this.estimateTime = estimateTime;
            return this;
        }

        public Builder copy(Route route) {
            this.routeId = route.routeId;
            this.driver = route.driver;
            this.vehicle = route.vehicle;
            this.shipments = route.shipments == null ? new ArrayList<>() : new ArrayList<>(route.shipments);
            this.startLocation = route.startLocation;
            this.endLocation = route.endLocation;
            this.distance = route.distance;
            this.estimateTime = route.estimateTime;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
