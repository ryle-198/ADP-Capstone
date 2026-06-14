package za.ac.cput.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.factory.DeliveryOrdersFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class DeliveryOrdersServiceTest {
    @Autowired
    private DeliveryOrdersService service;

    static LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
    static LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);
    static DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    static DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    private static DeliveryOrders order1 = DeliveryOrdersFactory.createDeliveryOrder(
                "#001",
                "12345",
                orderDate, deliveryDate,
                deliveryStatus, paymentStatus,
                19990.0f,"no special instructions"
        );


    @Test
    void a_create() {
        DeliveryOrders create = this.service.create(order1);
        assertNotNull(create);
        System.out.println(create);
    }

    @Test
    void b_read() {
        DeliveryOrders read= this.service.read(order1.getOrderId());
        assertNotNull(read);
        System.out.println(read);
    }

    @Test
    void c_update() {
        DeliveryOrders updatedOrder = new DeliveryOrders.Builder().copy(order1).setSpecialInstructions("Bring it to the small gate").build();
        DeliveryOrders update = this.service.update(updatedOrder);
        assertNotNull(update);
        System.out.println(update);
    }

    @Test
    void e_delete() {
        boolean deleted=this.service.delete(order1.getOrderId());
        assertTrue(deleted);
        System.out.println(deleted);
    }

    @Test
    void d_getAllDeliveryOrders() {
        List<DeliveryOrders> getAllOrders=this.service.getAllDeliveryOrders();
        System.out.println(getAllOrders);
    }
}