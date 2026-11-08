package za.ac.cput.service.Shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.RouteRepo.RouteRepository;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;

import za.ac.cput.service.TrackingEvent.TrackingEventService;
import za.ac.cput.service.ShipmentStatusRules;

import java.util.List;

@Service
public class ShipmentService implements IShipmentService {

    private ShipmentRepository repository;
    private TrackingEventService trackingService;
    private DeliveryOrdersRepository orderRepository;
    private RouteRepository routeRepository;

    @Autowired
    ShipmentService(ShipmentRepository repository, TrackingEventService trackingService,
                    DeliveryOrdersRepository orderRepository, RouteRepository routeRepository) {
        this.repository = repository;
        this.trackingService = trackingService;
        this.orderRepository = orderRepository;
        this.routeRepository = routeRepository;
    }

    private void checkLinks(Shipment shipment) {
        if (shipment.getOrder() == null || shipment.getOrder().getOrderId() == null
                || !this.orderRepository.existsById(shipment.getOrder().getOrderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order does not exist");
        }
        if (shipment.getRoute() != null && shipment.getRoute().getRouteId() != null
                && !this.routeRepository.existsById(shipment.getRoute().getRouteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route does not exist");
        }
    }

    @Override
    public Shipment create(Shipment shipment) {
        checkLinks(shipment);
        Shipment saved = this.repository.save(shipment);
        // TRACKING: first history row
        this.trackingService.record(saved, saved.getStatus(), saved.getOriginAddress(), "Shipment created");
        return saved;
    }

    @Override
    public Shipment read(String id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Shipment update(Shipment shipment) {
        if (shipment.getShipmentId() == null || !this.repository.existsById(shipment.getShipmentId())) {
            return null;
        }
        checkLinks(shipment);
        // TRACKING: remember the old status so we only log real changes
        Shipment.Status oldStatus = this.repository.findById(shipment.getShipmentId())
                .map(Shipment::getStatus).orElse(null);

        ShipmentStatusRules.check(oldStatus, shipment.getStatus());

        Shipment saved = this.repository.save(shipment);

        // TRACKING: log the change
        if (saved.getStatus() != oldStatus) {
            this.trackingService.record(saved, saved.getStatus(), null, "Status updated by staff");
        }
        return saved;
    }

    @Override
    public boolean delete(String id) {
        if (!this.repository.existsById(id)) {
            return false;
        }
        // TRACKING: history rows reference the shipment, so they go first
        this.trackingService.deleteForShipment(id);
        this.repository.deleteById(id);
        return true;
    }

    @Override
    public List<Shipment> getAll() {
        return this.repository.findAll();
    }
}