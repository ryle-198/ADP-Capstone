package za.ac.cput.factory;

import za.ac.cput.domain.Driver;
import za.ac.cput.domain.Route;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.util.Helper;

/*
RouteFactory.java
Route module class
Author: Ntombozuko Palisa Mchophele(240455789)
Date: 2026
 */

public class RouteFactory {
    public static Route createRoute(String routeId, Driver driver, Vehicle vehicle, String startLocation, String endLocation, float distance, int estimateTime) {
        if (Helper.isEmptyOrNull(routeId) || Helper.isEmptyOrNull(startLocation) || Helper.isEmptyOrNull(endLocation) || Helper.isValidType(driver)) {
            return null;
        }

        if (Helper.isNumNeg(distance)) {
            return null;
        }

        if (estimateTime <= 0) {
            return null;
        }

        return new Route.Builder().setRouteId(routeId)
                .setDriver(driver)
                .setVehicle(vehicle)
                .setStartLocation(startLocation)
                .setEndLocation(endLocation)
                .setDistance(distance)
                .setEstimateTime(estimateTime)
                .build();
    }
}
