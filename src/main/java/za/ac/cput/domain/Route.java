package za.ac.cput.domain;

public class Route {
    private String routeId;
    private String driverId;
    private String startLocation;
    private String endLocation;
    private float distance;
    private int estimateTime;

    private Route() {
    }

    private Route(Builder builder) {
        this.routeId = builder.routeId;
        this.driverId = builder.driverId;
        this.startLocation = builder.startLocation;
        this.endLocation = builder.endLocation;
        this.distance = builder.distance;
        this.estimateTime = builder.estimateTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public float getDistance() {
        return distance;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    @Override
    public String toString() {
        return "Routes{" +
                "routeId='" + routeId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", distance=" + distance +
                ", estimateTime=" + estimateTime +
                '}';
    }

    public static class Builder {
        private String routeId;
        private String driverId;
        private String startLocation;
        private String endLocation;
        private float distance;
        private int estimateTime;

        public Builder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setDriverId(String driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder setStartLocation(String startLocation) {
            this.startLocation = startLocation;
            return this;
        }

        public Builder setEndLocation(String endLocation) {
            this.endLocation = endLocation;
            return this;
        }

        public Builder setDistance(float distance) {
            this.distance = distance;
            return this;
        }

        public Builder setEstimateTime(int estimateTime) {
            this.estimateTime = estimateTime;
            return this;
        }

        public Builder copy(Route route) {
            this.routeId = route.routeId;
            this.driverId = route.driverId;
            this.startLocation = route.startLocation;
            this.endLocation = route.endLocation;
            this.distance = route.distance;
            this.estimateTime = route.estimateTime;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
