package za.ac.cput.domain;

/*
Shipment.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */

public class Shipment {

    public enum Status{
        CREATED,
        READY_FOR_PICKUP,
        PICKED_UP,
        IN_TRANSIT,
        AT_HUB,
        OUT_FOR_DELIVERY,
        DELIVERED,
        DELAYED,
        ON_HOLD,
        FAILED_DELIVERY,
        RETURN_INITIATED,
        RETURN_IN_TRANSIT,
        RETURNED,
        CANCELLED,
        LOST,
        DAMAGED
    }

    private String shipmentId;
    private String orderId;
    private String routeId;
    private String originAddress;
    private String destinationAddress;
    private Status status;
    private boolean fragile;

    public Shipment() {

    }

    public Shipment(String shipmentId, String orderId, String routeId, String originAddress, String destinationAddress, Status status, boolean fragile) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.routeId = routeId;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.fragile = fragile;
    }

    public Shipment(Builder builder){
        this.shipmentId= builder.shipmentId;
        this.orderId= builder.orderId;
        this.routeId= builder.routeId;
        this.originAddress= builder.originAddress;
        this.destinationAddress = builder.destinationAddress;
        this.status = builder.status;
        this.fragile = builder.fragile;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isFragile() {
        return fragile;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId='" + shipmentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", originAddress='" + originAddress + '\'' +
                ", destinationAddress='" + destinationAddress + '\'' +
                ", status=" + status +
                ", fragile=" + fragile +
                '}';
    }

    public static class Builder {
        private String shipmentId;
        private String orderId;
        private String routeId;
        private String originAddress;
        private String destinationAddress;
        private Status status;
        private boolean fragile;

        public Builder setShipmentId(String shipmentId){
            this.shipmentId=shipmentId;
            return this;
        }

        public Builder setOrderId(String orderId){
            this.orderId=orderId;
            return this;
        }

        public Builder setRouteId(String routeId){
            this.routeId=routeId;
            return this;
        }

        public Builder setOriginAddress(String originAddress){
            this.originAddress=originAddress;
            return this;
        }

        public Builder setDestinationAddress(String destinationAddress){
            this.destinationAddress = destinationAddress;
            return this;
        }

        public Builder setStatus(Status status){
            this.status = status;
            return this;
        }

        public Builder setFragile(boolean fragile){
            this.fragile = fragile;
            return this;
        }

        public Shipment build(){
            return new Shipment(this);
        }
    }
}
