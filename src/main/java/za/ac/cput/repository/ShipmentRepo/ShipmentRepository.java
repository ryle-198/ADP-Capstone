package za.ac.cput.repository.ShipmentRepo;



import za.ac.cput.domain.Shipment;

import java.util.ArrayList;
import java.util.List;

public class ShipmentRepository implements IShipmentRepository{

    private static ShipmentRepository instance;
    private List<Shipment> shipmentList;

    private ShipmentRepository() {
        shipmentList = new ArrayList<>();
    }

    public static ShipmentRepository getInstance() {
        if (instance == null) {
            instance = new ShipmentRepository();
        }
        return instance;
    }

    @Override
    public Shipment create(Shipment shipment) {
        boolean success = shipmentList.add(shipment);
        if(success){
            return shipment;
        }
        return null;
    }

    @Override
    public Shipment read(String shipmentId) {
        for(Shipment shipments: shipmentList){
            if(shipments.getShipmentId().equals(shipmentId)){
                return shipments;
            }
        }
        return null;
    }

    @Override
    public Shipment update(Shipment shipment) {
        String id = shipment.getShipmentId();
        Shipment oldShipment = read(id);

        if(oldShipment==null){
            return null;
        }

        boolean success = shipmentList.remove(oldShipment);

        if(!success){
            return null;
            }

        if(shipmentList.add(shipment)){
            return shipment;
        }
        return null;
        }



    @Override
    public boolean delete(String shipmentId) {
        Shipment shipmentToDelete = read(shipmentId);

        if(shipmentToDelete==null){
            return false;
        }
        return shipmentList.remove(shipmentToDelete);
    }

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentList;
    }
}