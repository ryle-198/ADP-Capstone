package za.ac.cput.factory;

import za.ac.cput.domain.Route;
import za.ac.cput.util.Helper;

public class RouteFactory {
    public static Route createRoute(String routeId, String driverId, String startLocation, String endLocation, float distance, int estimateTime) {
        if (Helper.isEmptyOrNull(routeId) || Helper.isEmptyOrNull(driverId) || Helper.isEmptyOrNull(startLocation) || Helper.isEmptyOrNull(endLocation)) {
            return null;
        }

        if (Helper.isNumNeg(distance)) {
            return null;
        }

        if (estimateTime <= 0) {
            return null;
        }

        return new Route.Builder().setRouteId(routeId)
                .setDriverId(driverId)
                .setStartLocation(startLocation)
                .setEndLocation(endLocation)
                .setDistance(distance)
                .setEstimateTime(estimateTime)
                .build();
    }
}
