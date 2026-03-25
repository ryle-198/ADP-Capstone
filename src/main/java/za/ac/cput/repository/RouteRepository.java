package za.ac.cput.repository;

import za.ac.cput.domain.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RouteRepository implements IRouteRepository {

    private static IRouteRepository repository = null;
    private List<Route> routeList;

    private RouteRepository() {
        routeList = new ArrayList<>();
    }

    public static IRouteRepository getRepository() {
        if (repository == null) {
            repository = new RouteRepository();
        }
        return repository;
    }

    @Override
    public Route create(Route route) {
        boolean success = routeList.add(route);
        if (success) {
            return route;
        }
        return null;
    }

    @Override
    public Route read(String routeId) {
        Route route = routeList.stream().filter(e -> e.getRouteId()
                .equals(routeId))
                .findFirst()
                .orElse(null);
        return route;

        /*for (Route route : routeList) {
            if (route.getRouteId().equals(routeId)) {
                return route;
            }
        }
        return null;*/
    }

    @Override
    public Route update(Route route) {
        String id = route.getRouteId();
        Route oldRoute = read(id);
        if (oldRoute != null) {
            if (delete(id)) {
                if (routeList.add(route)) {
                    return route;
                }
            }
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        Route routeToDelete = read(id);
        if (routeToDelete == null) {
            return false;
        }
        return (routeList.remove(routeToDelete));
    }

    @Override
    public List<Route> getAll() {
        return routeList;
    }
}
