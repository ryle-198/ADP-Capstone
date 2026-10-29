package za.ac.cput.factory;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Route;
import za.ac.cput.domain.Shipment;
import za.ac.cput.util.Helper;

/*
ShipmentFactory.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */

public class ShipmentFactory {

    public static Shipment createShipment(String shipmentId, DeliveryOrders order,
                                          Route route,
                                          String originAddress,
                                          String destinationAddress,
                                          Shipment.Status status,
                                          boolean fragile) {

        if(Helper.isEmptyOrNull(shipmentId)|| Helper.isEmptyOrNull(originAddress)|| Helper.isEmptyOrNull(destinationAddress) || Helper.isValidType(order)){
            return null;
        }

        if(Helper.isValidType(status)){
            return null;
        }


        return new Shipment.Builder()
                .setShipmentId(shipmentId)
                .setOrder(order)
                .setRoute(route)
                .setOriginAddress(originAddress)
                .setDestinationAddress(destinationAddress)
                .setStatus(status)
                .setFragile(fragile)
                .build();
    }
}