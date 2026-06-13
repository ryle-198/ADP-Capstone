package za.ac.cput.factory;

import za.ac.cput.domain.Shipment;
import za.ac.cput.util.Helper;

/*
ShipmentFactory.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */

public class ShipmentFactory {

    public static Shipment createShipment(String shipmentId, String orderId,
                                          String routeId,
                                          String originAddress,
                                          String destinationAddress,
                                          Shipment.Status status,
                                          boolean fragile) {

        if(Helper.isEmptyOrNull(shipmentId)|| Helper.isEmptyOrNull(orderId)
           || Helper.isEmptyOrNull(routeId)|| Helper.isEmptyOrNull(originAddress)|| Helper.isEmptyOrNull(destinationAddress)){
            return null;
        }

        if(Helper.isValidType(status)){
            return null;
        }


        return new Shipment.Builder()
                .setShipmentId(shipmentId)
                .setOrderId(orderId)
                .setRouteId(routeId)
                .setOriginAddress(originAddress)
                .setDestinationAddress(destinationAddress)
                .setStatus(status)
                .setFragile(fragile)
                .build();
    }
}