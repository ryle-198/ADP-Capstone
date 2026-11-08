package za.ac.cput.service.My;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Route;
import za.ac.cput.domain.Shipment;
import za.ac.cput.dto.MyDtos.CustomerShipmentView;
import za.ac.cput.dto.MyDtos.DriverShipmentView;
import za.ac.cput.dto.MyDtos.PlaceOrderRequest;
import za.ac.cput.dto.MyDtos.TrackingEventView;

import java.util.List;


public interface IMyService {

    //customer
    List<DeliveryOrders> getMyOrders(String customerId);

    DeliveryOrders getMyOrder(String customerId, String orderId);

    DeliveryOrders placeOrder(String customerId, PlaceOrderRequest request);

    List<CustomerShipmentView> getMyOrderShipments(String customerId, String orderId);

    CustomerShipmentView trackMyShipment(String customerId, String shipmentId);

    List<TrackingEventView> getMyShipmentHistory(String customerId, String shipmentId);

    //driver
    List<Route> getMyRoutes(String driverId);

    Route getMyRoute(String driverId, String routeId);

    List<DriverShipmentView> getMyRouteShipments(String driverId, String routeId);

    DriverShipmentView updateMyShipmentStatus(String driverId, String shipmentId,
                                              Shipment.Status status, String location, String note);

    DeliveryOrders payMyOrder(String customerId, String orderId);
}