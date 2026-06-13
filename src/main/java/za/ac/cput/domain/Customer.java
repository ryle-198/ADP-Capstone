package za.ac.cput.domain;

/*
Customer.java
Customer module class
Author: YAMKELA MGCUBHE (222040114)
Date: 2026
 */
public class Customer {
 private String customerId;
 private String customerName;
 private String phoneNumber;
 private String email;
 private String address;

 public Customer() {

 }

    public Customer(String customerId, String customerName, String phoneNumber, String email ,String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public Customer(Builder builder){
        this.customerId = builder.customerId;
        this.customerName = builder.customerName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.address = builder.address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public static class Builder {
        private String customerId;
        private String customerName;
        private String phoneNumber;
        private String email;
        private String address;


        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder copy(Customer customer){
            this.customerId = customer.customerId;
            this.customerName = customer.customerName;
            this.phoneNumber = customer.phoneNumber;
            this.email = customer.email;
            this.address = customer.address;

            return this;
        }
        public Customer build(){
            return new Customer(this);
        }

    }
}
