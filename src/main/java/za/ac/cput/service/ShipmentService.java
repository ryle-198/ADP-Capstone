/* ShipmentService.java
   Shipment Service implementation
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import java.util.List;

@Service
public class ShipmentService implements IShipmentService {

    private final ShipmentRepository shipmentRepository;

    ShipmentService(ShipmentRepository shipmentRepository){
        this.shipmentRepository=shipmentRepository;
    }

    @Override
    public Shipment create(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment read(String shipmentId) {
        return shipmentRepository.findById(shipmentId).orElse(null);
    }

    @Override
    public Shipment update(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    @Override
    public boolean delete(String shipmentId) {
         shipmentRepository.deleteById(shipmentId);
         return true;
    }

    @Override
    public List<Shipment> getAll() {
        return shipmentRepository.findAll();
    }
}