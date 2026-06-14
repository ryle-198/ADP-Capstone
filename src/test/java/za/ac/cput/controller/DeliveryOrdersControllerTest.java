package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.factory.DeliveryOrdersFactory;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class DeliveryOrdersControllerTest {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected static String BASE_URL = "http://localhost:8080/freightanddelivery/deliveryOrders";

    protected static  DeliveryOrders order1;

    @BeforeAll
    public static void setUp() {
         LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
         LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);
         DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
         DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

                order1 = DeliveryOrdersFactory.createDeliveryOrder(
                "001",
                "12345",
                orderDate, deliveryDate,
                deliveryStatus, paymentStatus,
                19990.0f,"no special instructions"
        );
    }

    @Test
    void a_createDeliveryOrder() {
        String url = BASE_URL+"/create";
        ResponseEntity<DeliveryOrders> response = this.restTemplate.postForEntity(url,order1,DeliveryOrders.class);
        assertNotNull(response);
        DeliveryOrders created = response.getBody();
        System.out.println(created);
    }

    @Test
    void b_readDeliveryOrder() {
        String url = BASE_URL+"/read/"+order1.getOrderId();
        ResponseEntity<DeliveryOrders> response = this.restTemplate.getForEntity(url,DeliveryOrders.class);
        assertNotNull(response);
        DeliveryOrders read = response.getBody();
        System.out.println(read);
    }

    @Test
    void c_updateDeliveryOrder() {
        String url = BASE_URL+"/update";
        DeliveryOrders updatedOrder = new DeliveryOrders.Builder().copy(order1).setSpecialInstructions("Bring it to the small gate").build();
        this.restTemplate.put(url,updatedOrder);
        ResponseEntity<DeliveryOrders> response = this.restTemplate.getForEntity(BASE_URL+"/read/"+updatedOrder.getOrderId(),DeliveryOrders.class);
        System.out.println(response.getBody());
    }

    @Test
    void e_deleteOrder() {
        String url = BASE_URL+"/delete/"+order1.getOrderId();
        this.restTemplate.delete(url);
        ResponseEntity<DeliveryOrders> response = this.restTemplate.getForEntity(BASE_URL+"/read/"+order1.getOrderId(),DeliveryOrders.class);
        assertNull(response.getBody());
        System.out.println("Deleted: true");
    }

    @Test
    void d_getAllDeliveryOrders() {
        String url = BASE_URL+"/getAll";
        ResponseEntity<DeliveryOrders[]> response = this.restTemplate.getForEntity(url,DeliveryOrders[].class);
        System.out.println("All Orders");
        for(DeliveryOrders allOrders:response.getBody()){
            System.out.println(allOrders);
        }
    }
}