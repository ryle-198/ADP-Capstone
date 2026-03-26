package za.ac.cput.repository.RouteRepo;

import za.ac.cput.domain.Route;
import za.ac.cput.repository.IRepository;

import java.util.List;


public interface IRouteRepository extends IRepository<Route, String> {
    List<Route> getAll();
}
