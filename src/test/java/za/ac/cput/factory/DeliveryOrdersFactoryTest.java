package za.ac.cput.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.DeliveryOrders;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryOrdersFactoryTest {

    DeliveryOrders order1;

    LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
    LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);



    DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    @BeforeEach
    void setUp() {
        order1 = DeliveryOrdersFactory.createDeliveryOrder(
                "#001",
                "12345",
                orderDate, deliveryDate,
                deliveryStatus, paymentStatus,
                19990.0f,"no special instructions"
        );
    }

    @Test
    void createDeliveryOrder() {
        System.out.println(order1);
    }
}