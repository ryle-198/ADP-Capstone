package za.ac.cput.repository;

import org.junit.jupiter.api.*;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.factory.DeliveryOrdersFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.MethodName.class)
class DeliveryOrdersRepositoryTest {

    static LocalDate orderDate = LocalDate.of(2026,Month.MARCH,25);
    static LocalDate deliveryDate = LocalDate.of(2026,Month.APRIL,1);

    static DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    static DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    static IDeliveryOrdersRepository repository = DeliveryOrdersRepository.getRepository();
    static DeliveryOrders order1= DeliveryOrdersFactory.createDeliveryOrder("#001",
            "12345",
            orderDate, deliveryDate,
            deliveryStatus, paymentStatus,
            19990.0f,"no special instructions");



    @Test
    void b_create() {
        DeliveryOrders created = repository.create(order1);
        assertNotNull(created);
        System.out.println("Created: " + created);
    }

    @Test
    void c_read() {
        DeliveryOrders read = repository.read(order1.getOrderId());
        assertEquals(order1.getOrderId(), read.getOrderId());
        System.out.println("Order: "+ read);
    }

    @Test
    void d_update() {
        DeliveryOrders newDeliveryOrder = DeliveryOrdersFactory.createDeliveryOrder("#001",
                "12345",
                order1.getOrderDate(), order1.getDeliveryDate(),
                order1.getDeliveryStatus(),order1.getPaymentStatus(),
                19990.0f,"Drop off infront of door");

        DeliveryOrders update = repository.update(newDeliveryOrder);
        assertNotNull(update);
        System.out.println("Updated Order: "+update);

    }

    @Test
    void e_delete() {
        boolean deleted= repository.delete(order1.getOrderId());
        assertTrue(deleted);
        System.out.println("Deleted: "+ deleted);
    }
    @Test
    void f_getAllOrders() {
        List<DeliveryOrders> allOrders = repository.getAllOrders();
        System.out.println("All Orders: "+ allOrders);
    }
}