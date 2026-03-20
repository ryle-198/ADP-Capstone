package za.ac.cput.domain;

public class Shipment {
    private String shipmentId;
    private String origin;
    private String destination;
    private double weight;
    private String status;
    private String estimatedDeliveryDate;

    private Shipment(Builder builder) {
        this.shipmentId = builder.shipmentId;
        this.origin = builder.origin;
        this.destination = builder.destination;
        this.weight = builder.weight;
        this.status = builder.status;
        this.estimatedDeliveryDate = builder.estimatedDeliveryDate;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId='" + shipmentId + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", weight=" + weight +
                ", status='" + status + '\'' +
                ", estimatedDeliveryDate='" + estimatedDeliveryDate + '\'' +
                '}';
    }

    public static class Builder {
        private String shipmentId;
        private String origin;
        private String destination;
        private double weight;
        private String status;
        private String estimatedDeliveryDate;

        public Builder setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
            return this;
        }

        public Builder setOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setEstimatedDeliveryDate(String estimatedDeliveryDate) {
            this.estimatedDeliveryDate = estimatedDeliveryDate;
            return this;
        }

        public Shipment build() {
            return new Shipment(this);
        }
    }
}