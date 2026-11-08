package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Route;
import za.ac.cput.dto.MyDtos.CustomerShipmentView;
import za.ac.cput.dto.MyDtos.DriverShipmentView;
import za.ac.cput.dto.MyDtos.PlaceOrderRequest;
import za.ac.cput.dto.MyDtos.TrackingEventView;
import za.ac.cput.dto.MyDtos.UpdateStatusRequest;
import za.ac.cput.security.AppUserPrincipal;
import za.ac.cput.service.My.MyService;

import java.util.List;

@RestController
@RequestMapping("/my")
public class MyController {

    private MyService service;

    @Autowired
    MyController(MyService service) {
        this.service = service;
    }

    //customer

    @GetMapping("/customer/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<DeliveryOrders> myOrders(@AuthenticationPrincipal AppUserPrincipal me) {
        return this.service.getMyOrders(me.getCustomerId());
    }

    @GetMapping("/customer/orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<DeliveryOrders> myOrder(@PathVariable String orderId,
                                                  @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.getMyOrder(me.getCustomerId(), orderId));
    }

    @PostMapping("/customer/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<DeliveryOrders> placeOrder(@RequestBody PlaceOrderRequest request,
                                                     @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.placeOrder(me.getCustomerId(), request));
    }

    @GetMapping("/customer/orders/{orderId}/shipments")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<CustomerShipmentView>> myOrderShipments(@PathVariable String orderId,
                                                                       @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.getMyOrderShipments(me.getCustomerId(), orderId));
    }

    @GetMapping("/customer/shipments/{shipmentId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerShipmentView> trackShipment(@PathVariable String shipmentId,
                                                              @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.trackMyShipment(me.getCustomerId(), shipmentId));
    }

    @GetMapping("/customer/shipments/{shipmentId}/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<TrackingEventView>> shipmentHistory(@PathVariable String shipmentId,
                                                                   @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.getMyShipmentHistory(me.getCustomerId(), shipmentId));
    }

    @PostMapping("/customer/orders/{orderId}/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<DeliveryOrders> payOrder(@PathVariable String orderId,
                                                   @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.payMyOrder(me.getCustomerId(), orderId));
    }

    // driver

    @GetMapping("/driver/routes")
    @PreAuthorize("hasRole('DRIVER')")
    public List<Route> myRoutes(@AuthenticationPrincipal AppUserPrincipal me) {
        return this.service.getMyRoutes(me.getDriverId());
    }

    @GetMapping("/driver/routes/{routeId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Route> myRoute(@PathVariable String routeId,
                                         @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.getMyRoute(me.getDriverId(), routeId));
    }

    @GetMapping("/driver/routes/{routeId}/shipments")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<DriverShipmentView>> myRouteShipments(@PathVariable String routeId,
                                                                     @AuthenticationPrincipal AppUserPrincipal me) {
        return found(this.service.getMyRouteShipments(me.getDriverId(), routeId));
    }

    @PutMapping("/driver/shipments/{shipmentId}/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverShipmentView> updateShipmentStatus(@PathVariable String shipmentId,
                                                                   @RequestBody UpdateStatusRequest request,
                                                                   @AuthenticationPrincipal AppUserPrincipal me) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        return found(this.service.updateMyShipmentStatus(
                me.getDriverId(), shipmentId, request.status(), request.location(), request.note()));
    }

    private <T> ResponseEntity<T> found(T body) {
        if (body == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(body);
    }
}