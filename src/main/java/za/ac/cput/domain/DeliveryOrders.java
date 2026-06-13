package za.ac.cput.domain;

import java.time.LocalDate;

/*
DeliveryOrders.java
Delivery Orders module class
Author: Ryle Peter May (230333907)
Date: 2026
 */

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

    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private Status deliveryStatus;
    private PaymentStatus paymentStatus;
    private float totalCost;
    private String specialInstructions;

    public DeliveryOrders() {
    }

    public DeliveryOrders(String orderId, String customerId, LocalDate orderDate,
                          LocalDate deliveryDate, Status deliveryStatus, PaymentStatus paymentStatus,
                          float totalCost, String specialInstructions) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.totalCost = totalCost;
        this.specialInstructions = specialInstructions;
    }

    public DeliveryOrders(Builder builder){
        this.orderId=builder.orderId;
        this.customerId=builder.customerId;
        this.orderDate=builder.orderDate;
        this.deliveryDate=builder.deliveryDate;
        this.deliveryStatus=builder.deliveryStatus;
        this.paymentStatus=builder.paymentStatus;
        this.totalCost=builder.totalCost;
        this.specialInstructions=builder.specialInstructions;

    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
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

    @Override
    public String toString() {
        return "DeliveryOrders{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
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
        private String customerId;
        private LocalDate orderDate;
        private LocalDate deliveryDate;
        private Status deliveryStatus;
        private PaymentStatus paymentStatus;
        private float totalCost;
        private String specialInstructions;

        public Builder setOrderId(String orderId){
            this.orderId=orderId;
            return this;
        }

        public Builder setCustomerId(String customerId){
            this.customerId=customerId;
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

        public Builder copy(DeliveryOrders order){
            this.orderId=order.orderId;
            this.customerId=order.customerId;
            this.orderDate=order.orderDate;
            this.deliveryDate=order.deliveryDate;
            this.deliveryStatus=order.deliveryStatus;
            this.paymentStatus=order.paymentStatus;
            this.totalCost=order.totalCost;
            this.specialInstructions=order.specialInstructions;

            return this;
        }

        public DeliveryOrders build(){return new DeliveryOrders(this);}
    }

}
