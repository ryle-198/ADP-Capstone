package za.ac.cput.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.Shipment;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import za.ac.cput.service.TrackingEvent.ITrackingEventService;

@Component
public class TrackingBackfill implements ApplicationRunner {

    private ShipmentRepository shipmentRepository;
    private ITrackingEventService trackingService;

    @Autowired
    TrackingBackfill(ShipmentRepository shipmentRepository, ITrackingEventService trackingService) {
        this.shipmentRepository = shipmentRepository;
        this.trackingService = trackingService;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (Shipment s : this.shipmentRepository.findAll()) {
            if (!this.trackingService.hasHistory(s.getShipmentId())) {
                this.trackingService.record(s, s.getStatus(), null, "History started (existing shipment)");
            }
        }
    }
}