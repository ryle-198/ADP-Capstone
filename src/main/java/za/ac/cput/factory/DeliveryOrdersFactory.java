package za.ac.cput.factory;

import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

/*
DeliveryOrdersFactory.java
Delivery Orders module class
Author: Ryle Peter May (230333907)
Date: 2026
 */

public class DeliveryOrdersFactory {
    public static DeliveryOrders createDeliveryOrder(String orderId, Customer customer,
                                                     LocalDate orderDate, LocalDate deliveryDate,
                                                     DeliveryOrders.Status deliveryStatus, DeliveryOrders.PaymentStatus paymentStatus,
                                                     float totalCost, String specialInstructions){

        if(Helper.isEmptyOrNull(orderId)||
                Helper.isEmptyOrNull(specialInstructions)
        ){
            return null;
        }

        if(Helper.isValidType(orderDate) || Helper.isValidType(deliveryDate) ||
                Helper.isValidType(deliveryStatus) || Helper.isValidType(paymentStatus) || Helper.isValidType(customer)){
            return null;
        }

        if(Helper.isNumNeg(totalCost)){
            return null;
        }

        return new DeliveryOrders.Builder()
                .setOrderId(orderId)
                .setCustomer(customer)
                .setOrderDate(orderDate)
                .setDeliveryDate(deliveryDate)
                .setDeliveryStatus(deliveryStatus)
                .setPaymentStatus(paymentStatus)
                .setTotalCost(totalCost)
                .setSpecialInstructions(specialInstructions)
                .build();

    }
}
