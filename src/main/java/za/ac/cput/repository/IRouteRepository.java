package za.ac.cput.repository;

import za.ac.cput.domain.Route;

import java.util.List;


public interface IRouteRepository extends IRepository<Route, String> {
    List<Route> getAll();
}
