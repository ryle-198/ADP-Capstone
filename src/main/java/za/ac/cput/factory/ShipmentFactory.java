package za.ac.cput.factory;

import za.ac.cput.domain.Shipment;

public class ShipmentFactory {

    public static Shipment createShipment(String shipmentId,
                                          String origin,
                                          String destination,
                                          double weight,
                                          String status,
                                          String estimatedDeliveryDate) {
        if (shipmentId == null || shipmentId.isEmpty()) return null;
        if (origin == null || origin.isEmpty()) return null;
        if (destination == null || destination.isEmpty()) return null;
        if (status == null || status.isEmpty()) return null;

        return new Shipment.Builder()
                .setShipmentId(shipmentId)
                .setOrigin(origin)
                .setDestination(destination)
                .setWeight(weight)
                .setStatus(status)
                .setEstimatedDeliveryDate(estimatedDeliveryDate)
                .build();
    }
}