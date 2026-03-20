package za.ac.cput.repository.impl;



import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.IRepository;

import java.util.HashMap;
import java.util.Map;

public class ShipmentRepositoryImpl implements IRepository<Shipment, String> {

    private static ShipmentRepositoryImpl instance;
    private Map<String, Shipment> shipmentMap;

    private ShipmentRepositoryImpl() {
        shipmentMap = new HashMap<>();
    }

    public static ShipmentRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ShipmentRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Shipment create(Shipment shipment) {
        shipmentMap.put(shipment.getShipmentId(), shipment);
        return shipment;
    }

    @Override
    public Shipment read(String shipmentId) {
        return shipmentMap.get(shipmentId);
    }

    @Override
    public Shipment update(Shipment shipment) {
        if (shipmentMap.containsKey(shipment.getShipmentId())) {
            shipmentMap.put(shipment.getShipmentId(), shipment);
            return shipment;
        }
        return null;
    }

    @Override
    public boolean delete(String shipmentId) {
        if (shipmentMap.containsKey(shipmentId)) {
            shipmentMap.remove(shipmentId);
            return true;
        }
        return false;
    }
}