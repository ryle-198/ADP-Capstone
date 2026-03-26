package za.ac.cput.repository.ShipmentRepo;

import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface IShipmentRepository extends IRepository<Shipment, String> {
    List<Shipment> getAllShipments();
}
