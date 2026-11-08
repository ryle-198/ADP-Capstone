package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.TrackingEvent;
import za.ac.cput.service.TrackingEvent.ITrackingEventService;

import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentHistoryController {

    private ITrackingEventService service;

    @Autowired
    ShipmentHistoryController(ITrackingEventService service) {
        this.service = service;
    }

    @GetMapping("/history/{shipmentId}")
    public ResponseEntity<List<TrackingEvent>> history(@PathVariable String shipmentId) {
        List<TrackingEvent> events = this.service.getHistory(shipmentId);
        return ResponseEntity.ok(events);
    }
}