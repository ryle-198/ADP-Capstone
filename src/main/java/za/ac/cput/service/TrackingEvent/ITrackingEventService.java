package za.ac.cput.service.TrackingEvent;

import za.ac.cput.domain.Shipment;
import za.ac.cput.domain.TrackingEvent;

import java.util.List;

public interface ITrackingEventService {

    TrackingEvent record(Shipment shipment, Shipment.Status status, String location, String note);

    List<TrackingEvent> getHistory(String shipmentId);

    boolean hasHistory(String shipmentId);

    void deleteForShipment(String shipmentId);
}