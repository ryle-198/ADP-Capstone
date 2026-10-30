package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.factory.CustomerFactory;
import za.ac.cput.factory.DeliveryOrdersFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
class DeliveryOrdersControllerTest {

    protected final RestTemplate restTemplate = new RestTemplate();
//    protected final RestTemplate restTemplate = authenticated();
    protected static String BASE_URL = "http://localhost:8080/freightanddelivery/deliveryOrders";
    protected static String CUSTOMER_URL = "http://localhost:8080/freightanddelivery/customer";

//Due to spring security, admin needs to be logged in to access the paths, authenticated method.

//    static RestTemplate authenticated() {
//        RestTemplate rt = new RestTemplate();
//        rt.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "admin"));
//        return rt;
//    }

//    private static List<DeliveryOrders> orderList = new ArrayList<>();
    private DeliveryOrders order1;
     private Customer customer;
//    private static boolean initialised = false;

    @BeforeAll
    void setUp() {
        RestTemplate rest = new RestTemplate();
        customer = rest.postForObject(CUSTOMER_URL + "/create",
                CustomerFactory.createCustomer("CUST-DC01", "Yamkela", "0732510842",
                        "orders.ctrl@example.com", "14 Aquarius Av", new ArrayList<>()),
                Customer.class);

        order1 = DeliveryOrdersFactory.createDeliveryOrder("ORD-DC01", customer,
                LocalDate.of(2026, 3, 25), LocalDate.of(2026, 4, 1),
                DeliveryOrders.Status.OrderPlaced, DeliveryOrders.PaymentStatus.PENDING,
                19990.0f, "no special instructions");
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