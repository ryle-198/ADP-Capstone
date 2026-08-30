package za.ac.cput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.ac.cput.domain.*;
import za.ac.cput.factory.*;
import za.ac.cput.service.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
    // Customer Service Set Up
    @Autowired
    private CustomerService customerService;

    //Route Service Set Up
    @Autowired
    private RouteService routeService;
    // Shipment Service Set Up

    @Autowired
    private ShipmentService shipmentService;
    private static Shipment shipment;

    //Vehicle Service Set Up
    @Autowired
    private VehicleService vehicleService;
    private Vehicle vehicle;

    //Driver Service Set Up
    @Autowired
    private DriverService driverService;
    private Driver driver;

    //Delivery Order Data Set Up For Customer
    static List<DeliveryOrders> orderList = new ArrayList<>();
    static LocalDate orderDate = LocalDate.of(2026, Month.MARCH, 25);
    static LocalDate deliveryDate = LocalDate.of(2026, Month.APRIL, 1);
    static DeliveryOrders.Status deliveryStatus = DeliveryOrders.Status.OrderPlaced;
    static DeliveryOrders.PaymentStatus paymentStatus = DeliveryOrders.PaymentStatus.PENDING;

    private static Route route1 = RouteFactory.createRoute("R001", "D001", "Cape Town", "Atlantis", 65.3f, 51);

    @Override
    public void run(String... args) throws Exception {
        Customer customer = CustomerFactory.createCustomer("CUST-23451", "Yamkela", "0732510842"
                ,"yamkela197@gmail.com"
                ,"14 Aquarius Av Sandrift Milnerton 7441",orderList);

        DeliveryOrders order1 = DeliveryOrdersFactory.createDeliveryOrder("001",customer,orderDate,deliveryDate,deliveryStatus,paymentStatus,444f,"None");
        DeliveryOrders order2 = DeliveryOrdersFactory.createDeliveryOrder("002",customer,orderDate,deliveryDate,deliveryStatus,paymentStatus,500f,"Place at front of the door");
        DeliveryOrders order3 = DeliveryOrdersFactory.createDeliveryOrder("003",customer,orderDate,deliveryDate,deliveryStatus,paymentStatus,500f,"Place at front of the door");

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        shipment = ShipmentFactory.createShipment(
                "SH001",
                "ORD001",
                "RT001",
                "123 Main St, Cape Town",
                "456 Long St, Johannesburg",
                Shipment.Status.CREATED,
                false
        );

        vehicle = VehicleFactory.createVehicle(
                "V001",
                "CAA24680",
                Vehicle.VehicleType.TRUCK,
                16000.0f,
                Vehicle.VehicleStatus.AVAILABLE,
                18000.0f,
                LocalDate.of(2025, 12, 10)
        );

        driver = DriverFactory.buildDriver(
                "Angel", "Masonganye", "LIC223008869", "0821234567", true);

        this.customerService.create(customer);
        this.routeService.create(route1);
        this.shipmentService.create(shipment);
        this.vehicleService.create(vehicle);
        this.driverService.create(driver);

    }
}
