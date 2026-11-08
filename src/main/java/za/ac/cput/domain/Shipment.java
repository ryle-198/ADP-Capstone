package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.UUID;

/*
Shipment.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */
@Entity
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

    @Id
    private String shipmentId;
    private String originAddress;
    private String destinationAddress;
//    private Status status;
    private boolean fragile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id")
    @JsonIgnoreProperties({"shipments", "hibernateLazyInitializer", "handler"})
    private DeliveryOrders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="route_id")
    @JsonIgnoreProperties({"shipments", "hibernateLazyInitializer", "handler"})
    private Route route;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Shipment() {

    }

    public Shipment(String shipmentId, DeliveryOrders order, Route route, String originAddress, String destinationAddress, Status status, boolean fragile) {
        this.shipmentId = shipmentId;
        this.order = order;
        this.route = route;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.fragile = fragile;
    }

    public Shipment(Builder builder){
        this.shipmentId= builder.shipmentId;
        this.order= builder.order;
        this.route= builder.route;
        this.originAddress= builder.originAddress;
        this.destinationAddress = builder.destinationAddress;
        this.status = builder.status;
        this.fragile = builder.fragile;
    }

    @PrePersist
    public void generateId(){
        if(this.shipmentId == null || this.shipmentId.isBlank()){
            this.shipmentId = "SHP-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        }
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public DeliveryOrders getOrder() {
        return order;
    }

    public Route getRoute() {
        return route;
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
                ", orderId='" + (order != null ? order.getOrderId() : null) + '\'' +
                ", routeId='" + (route != null ? route.getRouteId() : null) + '\'' +
                ", originAddress='" + originAddress + '\'' +
                ", destinationAddress='" + destinationAddress + '\'' +
                ", status=" + status +
                ", fragile=" + fragile +
                '}';
    }

    public static class Builder {
        private String shipmentId;
        private DeliveryOrders order;
        private Route route;
        private String originAddress;
        private String destinationAddress;
        private Status status;
        private boolean fragile;

        public Builder setShipmentId(String shipmentId){
            this.shipmentId=shipmentId;
            return this;
        }

        public Builder setOrder(DeliveryOrders order){
            this.order=order;
            return this;
        }

        public Builder setRoute(Route route){
            this.route=route;
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

        public Builder copy(Shipment shipment){
            this.shipmentId=shipment.shipmentId;
            this.order = shipment.order;
            this.route=shipment.route;
            this.originAddress=shipment.originAddress;
            this.destinationAddress=shipment.destinationAddress;
            this.status=shipment.status;
            this.fragile=shipment.fragile;
            return this;
        }

        public Shipment build(){
            return new Shipment(this);
        }
    }
}
