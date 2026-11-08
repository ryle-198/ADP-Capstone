package za.ac.cput.service.Route;

import za.ac.cput.domain.Route;
import za.ac.cput.service.IService;

import java.util.List;

public interface IRouteService extends IService<Route, String> {
    List<Route> getAllRoutes();
}
