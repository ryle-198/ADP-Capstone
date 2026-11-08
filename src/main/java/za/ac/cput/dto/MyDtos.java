package za.ac.cput.dto;

import za.ac.cput.domain.Shipment;

import za.ac.cput.domain.TrackingEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class MyDtos {

    private MyDtos() {
    }

    // Customer places an order; id, customer, order date, status and price are set by the server
    public record PlaceOrderRequest(LocalDate deliveryDate,
                                    String specialInstructions,
                                    String pickupAddress,
                                    String deliveryAddress,
                                    float weightKg,
                                    String parcelDescription) {
    }

    // Driver reports a status change, optionally with where they are and a note
    public record UpdateStatusRequest(Shipment.Status status, String location, String note) {
    }

    // One history row as the customer sees it (no recordedBy)
    public record TrackingEventView(Shipment.Status status,
                                    String location,
                                    String note,
                                    LocalDateTime eventTime) {

        public static TrackingEventView from(TrackingEvent e) {
            return new TrackingEventView(e.getStatus(), e.getLocation(), e.getNote(), e.getEventTime());
        }
    }

    // What a customer may see about a shipment (no driver or vehicle details)
    public record CustomerShipmentView(String shipmentId,
                                       String orderId,
                                       Shipment.Status status,
                                       String originAddress,
                                       String destinationAddress,
                                       boolean fragile) {

        public static CustomerShipmentView from(Shipment s) {
            return new CustomerShipmentView(
                    s.getShipmentId(),
                    s.getOrder() != null ? s.getOrder().getOrderId() : null,
                    s.getStatus(),
                    s.getOriginAddress(),
                    s.getDestinationAddress(),
                    s.isFragile());
        }
    }

    // What a driver may see about a shipment on their route (contact details, no email)
    public record DriverShipmentView(String shipmentId,
                                     String orderId,
                                     String routeId,
                                     Shipment.Status status,
                                     String originAddress,
                                     String destinationAddress,
                                     boolean fragile,
                                     String customerName,
                                     String customerPhone,
                                     String specialInstructions) {

        public static DriverShipmentView from(Shipment s) {
            var order = s.getOrder();
            var customer = order != null ? order.getCustomer() : null;
            return new DriverShipmentView(
                    s.getShipmentId(),
                    order != null ? order.getOrderId() : null,
                    s.getRoute() != null ? s.getRoute().getRouteId() : null,
                    s.getStatus(),
                    s.getOriginAddress(),
                    s.getDestinationAddress(),
                    s.isFragile(),
                    customer != null ? customer.getCustomerName() : null,
                    customer != null ? customer.getPhoneNumber() : null,
                    order != null ? order.getSpecialInstructions() : null);
        }
    }
}