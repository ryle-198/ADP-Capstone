package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TrackingEvent {

    @Id
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    @JsonIgnore
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    private Shipment.Status status;

    private String location;
    private String note;
    private LocalDateTime eventTime;
    private String recordedBy;

    protected TrackingEvent() {
    }

    private TrackingEvent(Builder builder) {
        this.eventId = builder.eventId;
        this.shipment = builder.shipment;
        this.status = builder.status;
        this.location = builder.location;
        this.note = builder.note;
        this.eventTime = builder.eventTime;
        this.recordedBy = builder.recordedBy;
    }

    @PrePersist
    void onCreate() {
        if (this.eventId == null) this.eventId = UUID.randomUUID().toString();
        if (this.eventTime == null) this.eventTime = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public String getShipmentId() {
        return shipment != null ? shipment.getShipmentId() : null;
    }

    public Shipment.Status getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public static class Builder {
        private String eventId;
        private Shipment shipment;
        private Shipment.Status status;
        private String location;
        private String note;
        private LocalDateTime eventTime;
        private String recordedBy;

        public Builder setEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder setShipment(Shipment shipment) {
            this.shipment = shipment;
            return this;
        }

        public Builder setStatus(Shipment.Status status) {
            this.status = status;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setNote(String note) {
            this.note = note;
            return this;
        }

        public Builder setEventTime(LocalDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder setRecordedBy(String recordedBy) {
            this.recordedBy = recordedBy;
            return this;
        }

        public Builder copy(TrackingEvent e) {
            this.eventId = e.eventId;
            this.shipment = e.shipment;
            this.status = e.status;
            this.location = e.location;
            this.note = e.note;
            this.eventTime = e.eventTime;
            this.recordedBy = e.recordedBy;
            return this;
        }

        public TrackingEvent build() {
            return new TrackingEvent(this);
        }
    }
}