package za.ac.cput.factory;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class DeliveryOrdersFactory {
    public static DeliveryOrders createDeliveryOrder(String orderId, String customerId,
                                                     LocalDate orderDate, LocalDate deliveryDate,
                                                     DeliveryOrders.Status deliveryStatus, DeliveryOrders.PaymentStatus paymentStatus,
                                                     float totalCost, String specialInstructions){

        if(Helper.isEmptyOrNull(orderId)||
           Helper.isEmptyOrNull(customerId) ||
           Helper.isEmptyOrNull(specialInstructions)
        ){
            return null;
        }

        if(Helper.isValidType(orderDate) || Helper.isValidType(deliveryDate) ||
           Helper.isValidType(deliveryStatus) || Helper.isValidType(paymentStatus)){
            return null;
        }

        if(Helper.isNumNeg(totalCost)){
            return null;
        }

        return new DeliveryOrders.Builder()
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .setOrderDate(orderDate)
                .setDeliveryDate(deliveryDate)
                .setDeliveryStatus(deliveryStatus)
                .setPaymentStatus(paymentStatus)
                .setTotalCost(totalCost)
                .setSpecialInstructions(specialInstructions)
                .build();

    }
}
