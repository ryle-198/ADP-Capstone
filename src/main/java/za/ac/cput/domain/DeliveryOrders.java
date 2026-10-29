package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
DeliveryOrders.java
Delivery Orders module class
Author: Ryle Peter May (230333907)
Date: 2026
 */
@Entity
public class DeliveryOrders {

    public enum Status{
        OrderPlaced,
        OrderConfirmed,
        OrderProcessing,
        Shipped,
        InTransit,
        OutForDelivery,
        Delivered
    }

    public enum PaymentStatus{
        PENDING,
        COMPLETE,
        REFUNDED,
        FAILED,
        REVOKED,
        CANCELLED
    }
    @Id
    private String orderId;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    @Enumerated(EnumType.STRING)
    private Status deliveryStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private float totalCost;
    private String specialInstructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
//    @JsonBackReference // to stop stackoverflow due to bidirectional relationship but this causes issue of not retrieving customer id
    @JsonIgnoreProperties({"orders", "hibernateLazyInitializer", "handler"})
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Shipment> shipments = new ArrayList<>();


    public DeliveryOrders() {
    }

//    public DeliveryOrders(String orderId, Customer customer, LocalDate orderDate,
//                          LocalDate deliveryDate, Status deliveryStatus, PaymentStatus paymentStatus,
//                          float totalCost, String specialInstructions, List<Shipment> shipments) {
//
//        this.orderId = orderId;
//        this.customer = customer;
//        this.orderDate = orderDate;
//        this.deliveryDate = deliveryDate;
//        this.deliveryStatus = deliveryStatus;
//        this.paymentStatus = paymentStatus;
//        this.totalCost = totalCost;
//        this.specialInstructions = specialInstructions;
//        this.shipments = shipments;
//    }

    public DeliveryOrders(Builder builder){
        this.orderId=builder.orderId;
        this.customer=builder.customer;
        this.orderDate=builder.orderDate;
        this.deliveryDate=builder.deliveryDate;
        this.deliveryStatus=builder.deliveryStatus;
        this.paymentStatus=builder.paymentStatus;
        this.totalCost=builder.totalCost;
        this.specialInstructions=builder.specialInstructions;
        this.shipments = builder.shipments;

    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public Status getDeliveryStatus() {
        return deliveryStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public List<Shipment> getShipments(){return shipments;}

    @Override
    public String toString() {
        return "DeliveryOrders{" +
                "orderId='" + orderId + '\'' +
//                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", deliveryStatus=" + deliveryStatus +
                ", paymentStatus=" + paymentStatus +
                ", totalCost=" + totalCost +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }

    public static class Builder{
        private String orderId;
        private Customer customer;
        private LocalDate orderDate;
        private LocalDate deliveryDate;
        private Status deliveryStatus;
        private PaymentStatus paymentStatus;
        private float totalCost;
        private String specialInstructions;
        private List<Shipment> shipments = new ArrayList<>();

        public Builder setOrderId(String orderId){
            this.orderId=orderId;
            return this;
        }

        public Builder setCustomer(Customer customer){
            this.customer=customer;
            return this;
        }

        public Builder setOrderDate(LocalDate orderDate){
            this.orderDate=orderDate;
            return this;
        }

        public Builder setDeliveryDate(LocalDate deliveryDate){
            this.deliveryDate=deliveryDate;
            return this;
        }

        public Builder setDeliveryStatus(Status deliveryStatus){
            this.deliveryStatus=deliveryStatus;
            return this;
        }

        public Builder setPaymentStatus(PaymentStatus paymentStatus ){
            this.paymentStatus=paymentStatus;
            return this;
        }

        public Builder setTotalCost(float totalCost){
            this.totalCost=totalCost;
            return this;
        }
        public Builder setSpecialInstructions(String specialInstructions){
            this.specialInstructions=specialInstructions;
            return this;
        }

        public Builder setShipments(List<Shipment> shipments){
            this.shipments = shipments == null ? new ArrayList<>() : shipments;
            return this;
        }

        public Builder copy(DeliveryOrders order){
            this.orderId=order.orderId;
            this.customer=order.customer;
            this.orderDate=order.orderDate;
            this.deliveryDate=order.deliveryDate;
            this.deliveryStatus=order.deliveryStatus;
            this.paymentStatus=order.paymentStatus;
            this.totalCost=order.totalCost;
            this.specialInstructions=order.specialInstructions;
            this.shipments = order.shipments == null ? new ArrayList<>() : new ArrayList<>(order.shipments);

            return this;
        }

        public DeliveryOrders build(){return new DeliveryOrders(this);}
    }

}
