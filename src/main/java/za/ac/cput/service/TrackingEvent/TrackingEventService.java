package za.ac.cput.service.TrackingEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Shipment;
import za.ac.cput.domain.TrackingEvent;
import za.ac.cput.repository.TrackingEventRepo.TrackingEventRepository;

import java.util.List;

@Service
public class TrackingEventService implements ITrackingEventService {

    private TrackingEventRepository repository;

    @Autowired
    TrackingEventService(TrackingEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrackingEvent record(Shipment shipment, Shipment.Status status, String location, String note) {
        if (shipment == null || status == null) {
            return null;
        }

        TrackingEvent event = new TrackingEvent.Builder()
                .setShipment(shipment)
                .setStatus(status)
                .setLocation(blankToNull(location))
                .setNote(blankToNull(note))
                .setRecordedBy(currentUsername())
                .build();
        return this.repository.save(event);
    }

    @Override
    public List<TrackingEvent> getHistory(String shipmentId) {
        return this.repository.findByShipment_ShipmentIdOrderByEventTimeAsc(shipmentId);
    }

    @Override
    public boolean hasHistory(String shipmentId) {
        return this.repository.existsByShipment_ShipmentId(shipmentId);
    }

    @Override
    public void deleteForShipment(String shipmentId) {
        this.repository.deleteByShipment_ShipmentId(shipmentId);
    }

    private static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "system";
        }
        return auth.getName();
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}