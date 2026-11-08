package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.factory.CustomerFactory;
import za.ac.cput.factory.DeliveryOrdersFactory;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.service.DeliveryOrders.DeliveryOrdersService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class DeliveryOrdersServiceTest {
    @Autowired
    private DeliveryOrdersService service;

    @Autowired
    private CustomerRepository customerRepository;

    static LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
    static LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);

    static DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    static DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    static DeliveryOrders order1;
    static DeliveryOrders order2;
    private static Customer customer ;


    @BeforeEach
    void setup() {
        customer = CustomerFactory.createCustomer("CUST-SVC01", "Yamkela", "0732510842",
                "customer.svc@example.com", "14 Aquarius Av", new ArrayList<>());
        customerRepository.save(customer);
        order1 = DeliveryOrdersFactory.createDeliveryOrder("001", customer, orderDate, deliveryDate, deliveryStatus, paymentStatus, 444f, "None");
        order2 = DeliveryOrdersFactory.createDeliveryOrder("002", customer , orderDate, deliveryDate, deliveryStatus, paymentStatus, 500f, "Place at front of the door");

    }

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

    @Disabled
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

    @Test
    void f_findByCustomerID(){
        String customerId ="CUST-23451";
        List<DeliveryOrders> customersOrders = this.service.findByCustomerID(customerId);
        System.out.println(customersOrders);

    }

    @Test
    void g_findByStatus(){
        DeliveryOrders.Status status = DeliveryOrders.Status.OrderPlaced;
        List<DeliveryOrders> findByStatus = this.service.findByDeliveryStatus(status);
        System.out.println(findByStatus);

    }

    @Test
    void h_findByDeliveryDate(){
        LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);
        List<DeliveryOrders> findyByDeliveryDate = this.service.findByDeliveryDate(deliveryDate);
        System.out.println(findyByDeliveryDate);

    }
}