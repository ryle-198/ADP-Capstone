package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
DeliveryOrdersFactoryTest.java
Delivery Orders module class
Author: Ryle Peter May (230333907)
Date: 2026
 */

class DeliveryOrdersFactoryTest {

    DeliveryOrders order1;

    LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
    LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);

    DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    @BeforeEach
    void setUp() {
        Customer customer = CustomerFactory.createCustomer("CUST-23451"
                , "Yamkela", "0732510842"
                ,"yamkela197@gmail.com"
                ,"14 Aquarius Av Sandrift Milnerton 7441", new ArrayList<>());

        order1 = DeliveryOrdersFactory.createDeliveryOrder(
                "001",
                customer,
                orderDate, deliveryDate,
                deliveryStatus, paymentStatus,
                19990.0f,"no special instructions"
        );
        assertNotNull(order1);
    }

    @Test
    void createDeliveryOrder() {
        System.out.println(order1);
    }

    @Test
    void createDeliveryOrder_NullCustomer(){
        assertNull(DeliveryOrdersFactory.createDeliveryOrder(
                "001",
                null,
                orderDate, deliveryDate,
                deliveryStatus, paymentStatus,
                19990.0f,"no special instructions"));
    }
}