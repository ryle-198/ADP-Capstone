package za.ac.cput.service.My;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Route;
import za.ac.cput.domain.Shipment;
import za.ac.cput.dto.MyDtos.CustomerShipmentView;
import za.ac.cput.dto.MyDtos.DriverShipmentView;
import za.ac.cput.dto.MyDtos.PlaceOrderRequest;
import za.ac.cput.dto.MyDtos.TrackingEventView;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.RouteRepo.RouteRepository;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import za.ac.cput.service.DeliveryOrders.OrderPricing;
import za.ac.cput.service.ShipmentStatusRules;
import za.ac.cput.service.TrackingEvent.TrackingEventService;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MyService implements IMyService {

    // What a driver may set. admin handle the rest (cancel, return, lost...).
    private static final Set<Shipment.Status> DRIVER_ALLOWED = EnumSet.of(
            Shipment.Status.PICKED_UP,
            Shipment.Status.IN_TRANSIT,
            Shipment.Status.AT_HUB,
            Shipment.Status.OUT_FOR_DELIVERY,
            Shipment.Status.DELIVERED,
            Shipment.Status.DELAYED,
            Shipment.Status.FAILED_DELIVERY);

    // A driver cannot change a shipment that is already finished
    private static final Set<Shipment.Status> FINISHED = EnumSet.of(
            Shipment.Status.DELIVERED,
            Shipment.Status.CANCELLED,
            Shipment.Status.RETURNED,
            Shipment.Status.LOST);

    private DeliveryOrdersRepository orderRepository;
    private CustomerRepository customerRepository;
    private RouteRepository routeRepository;
    private ShipmentRepository shipmentRepository;
    private TrackingEventService trackingService;

    @Autowired
    MyService(DeliveryOrdersRepository orderRepository,
              CustomerRepository customerRepository,
              RouteRepository routeRepository,
              ShipmentRepository shipmentRepository,
              TrackingEventService trackingService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.routeRepository = routeRepository;
        this.shipmentRepository = shipmentRepository;
        this.trackingService = trackingService;
    }

    // customer

    @Override
    public List<DeliveryOrders> getMyOrders(String customerId) {
        return this.orderRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    public DeliveryOrders getMyOrder(String customerId, String orderId) {
        DeliveryOrders order = this.orderRepository.findById(orderId).orElse(null);
        return ownsOrder(customerId, order) ? order : null;
    }

    @Override
    public DeliveryOrders placeOrder(String customerId, PlaceOrderRequest request) {
        if (request == null || request.deliveryDate() == null) {
            throw bad("deliveryDate is required");
        }
        if (request.deliveryDate().isBefore(LocalDate.now())) {
            throw bad("deliveryDate cannot be in the past");
        }
        if (isBlank(request.pickupAddress()) || isBlank(request.deliveryAddress())) {
            throw bad("pickup and delivery addresses are required");
        }
        if (request.weightKg() <= 0) {
            throw bad("weight must be above 0");
        }
        Customer customer = this.customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }

        DeliveryOrders order = new DeliveryOrders.Builder()
                .setCustomer(customer)
                .setOrderDate(LocalDate.now())
                .setDeliveryDate(request.deliveryDate())
                .setDeliveryStatus(DeliveryOrders.Status.OrderPlaced)
                .setPaymentStatus(DeliveryOrders.PaymentStatus.PENDING)
                .setPickupAddress(request.pickupAddress().trim())
                .setDeliveryAddress(request.deliveryAddress().trim())
                .setWeightKg(request.weightKg())
                .setParcelDescription(request.parcelDescription())
                .setTotalCost(OrderPricing.calculate(request.weightKg()))
                .setSpecialInstructions(request.specialInstructions() == null ? "" : request.specialInstructions())
                .build();
        DeliveryOrders saved = this.orderRepository.save(order);

        Shipment shipment = new Shipment.Builder()
                .setOrder(saved)
                .setOriginAddress(saved.getPickupAddress())
                .setDestinationAddress(saved.getDeliveryAddress())
                .setStatus(Shipment.Status.CREATED)
                .setFragile(false)
                .build();
        Shipment savedShipment = this.shipmentRepository.save(shipment);
        this.trackingService.record(savedShipment, Shipment.Status.CREATED, saved.getPickupAddress(), "Order placed");
        return saved;
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    @Override
    public List<CustomerShipmentView> getMyOrderShipments(String customerId, String orderId) {
        DeliveryOrders order = getMyOrder(customerId, orderId);
        if (order == null) {
            return null;
        }
        return this.shipmentRepository.findByOrder_OrderId(orderId).stream()
                .map(CustomerShipmentView::from)
                .toList();
    }

    @Override
    public CustomerShipmentView trackMyShipment(String customerId, String shipmentId) {
        Shipment shipment = this.shipmentRepository.findById(shipmentId).orElse(null);
        if (shipment == null || !ownsOrder(customerId, shipment.getOrder())) {
            return null;
        }
        return CustomerShipmentView.from(shipment);
    }

    @Override
    public List<TrackingEventView> getMyShipmentHistory(String customerId, String shipmentId) {
        Shipment shipment = this.shipmentRepository.findById(shipmentId).orElse(null);
        if (shipment == null || !ownsOrder(customerId, shipment.getOrder())) {
            return null;
        }
        return this.trackingService.getHistory(shipmentId).stream()
                .map(TrackingEventView::from)
                .toList();
    }

    // driver

    @Override
    public List<Route> getMyRoutes(String driverId) {
        return this.routeRepository.findByDriver_Id(driverId);
    }

    @Override
    public Route getMyRoute(String driverId, String routeId) {
        Route route = this.routeRepository.findById(routeId).orElse(null);
        return ownsRoute(driverId, route) ? route : null;
    }

    @Override
    public List<DriverShipmentView> getMyRouteShipments(String driverId, String routeId) {
        Route route = getMyRoute(driverId, routeId);
        if (route == null) {
            return null;
        }
        return this.shipmentRepository.findByRoute_RouteId(routeId).stream()
                .map(DriverShipmentView::from)
                .toList();
    }

    @Override
    public DriverShipmentView updateMyShipmentStatus(String driverId, String shipmentId,
                                                     Shipment.Status status, String location, String note) {
        Shipment shipment = this.shipmentRepository.findById(shipmentId).orElse(null);
        if (shipment == null || !ownsRoute(driverId, shipment.getRoute())) {
            return null;
        }
        if (status == null || !DRIVER_ALLOWED.contains(status)) {
            throw bad("Drivers can only set: " + DRIVER_ALLOWED);
        }
        if (FINISHED.contains(shipment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Shipment is already " + shipment.getStatus() + " and cannot be changed");
        }

        ShipmentStatusRules.check(shipment.getStatus(), status);

        Shipment updated = new Shipment.Builder().copy(shipment).setStatus(status).build();
        Shipment saved = this.shipmentRepository.save(updated);
        this.trackingService.record(saved, status, location, note);
        return DriverShipmentView.from(saved);
    }

    @Override
    public DeliveryOrders payMyOrder(String customerId, String orderId) {
        DeliveryOrders order = getMyOrder(customerId, orderId);
        if (order == null) {
            return null;
        }
        if (order.getPaymentStatus() != DeliveryOrders.PaymentStatus.PENDING || order.getTotalCost() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Order cannot be paid (status " + order.getPaymentStatus() + ")");
        }
        DeliveryOrders paid = new DeliveryOrders.Builder().copy(order)
                .setPaymentStatus(DeliveryOrders.PaymentStatus.COMPLETE)
                .build();
        return this.orderRepository.save(paid);
    }

    // ownership

    private boolean ownsOrder(String customerId, DeliveryOrders order) {
        return customerId != null
                && order != null
                && order.getCustomer() != null
                && customerId.equals(order.getCustomer().getCustomerId());
    }

    private boolean ownsRoute(String driverId, Route route) {
        return driverId != null
                && route != null
                && route.getDriver() != null
                && driverId.equals(route.getDriver().getId());
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}