package za.ac.cput.service.DeliveryOrders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import za.ac.cput.service.TrackingEvent.ITrackingEventService;

import java.time.LocalDate;
import java.util.List;
@Service
public class DeliveryOrdersService implements IDeliveryOrdersService {

    private DeliveryOrdersRepository repository;
    private ShipmentRepository shipmentRepository;
    private ITrackingEventService trackingService;

    @Autowired
    DeliveryOrdersService(DeliveryOrdersRepository repository,
                          ShipmentRepository shipmentRepository,
                          ITrackingEventService trackingService){
        this.repository=repository;
        this.shipmentRepository = shipmentRepository;
        this.trackingService = trackingService;
    }

    @Override
    public DeliveryOrders create(DeliveryOrders deliveryOrders) {

        if (deliveryOrders.getTotalCost() <= 0 && deliveryOrders.getWeightKg() > 0) {
            deliveryOrders = new DeliveryOrders.Builder().copy(deliveryOrders)
                    .setTotalCost(OrderPricing.calculate(deliveryOrders.getWeightKg()))
                    .build();
        }
        return this.repository.save(deliveryOrders);
    }

    @Override
    public DeliveryOrders read(String s) {
        return this.repository.findById(s).orElse(null);
    }

    @Override
    public DeliveryOrders update(DeliveryOrders deliveryOrders) {
        return this.repository.save(deliveryOrders);
    }

    @Override
    public boolean delete(String s) {
        if (!this.repository.existsById(s)) {
            return false;
        }

        for (Shipment shipment : this.shipmentRepository.findByOrder_OrderId(s)) {
            this.trackingService.deleteForShipment(shipment.getShipmentId());
        }
        this.repository.deleteById(s);
        return true;
    }

    @Override
    public List<DeliveryOrders> getAllDeliveryOrders() {
        return this.repository.findAll();
    }

    @Override
    public List<DeliveryOrders> findByCustomerID(String customerId) {
        return this.repository.findByCustomer_CustomerId(customerId);
    }

    @Override
    public List<DeliveryOrders> findByDeliveryStatus(DeliveryOrders.Status deliveryStatus) {
        return this.repository.findByDeliveryStatus(deliveryStatus);
    }

    @Override
    public List<DeliveryOrders> findByDeliveryDate(LocalDate deliveryDate) {
        return this.repository.findByDeliveryDate(deliveryDate);
    }

//    @Override
//    public DeliveryOrders assignDriver() {
//        return null;
//    }


    @Override
    public Page<DeliveryOrders> getPagedOrders(Pageable pageable){
        return this.repository.findAll(pageable);
    }

    @Override
    public  Page<DeliveryOrders> searchOrders(String search, Pageable pageable){
        return this.repository.searchOrders(search, pageable);
    }

}