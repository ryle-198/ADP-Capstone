package za.ac.cput.repository.RouteRepo;

import za.ac.cput.domain.Route;
import za.ac.cput.repository.IRepository;

import java.util.List;

/*
IRouteRepository.java
Route module class
Author: Ntombozuko Palisa Mchophele(240455789)
Date: 2026
 */
public interface IRouteRepository extends IRepository<Route, String> {
    List<Route> getAll();
}
