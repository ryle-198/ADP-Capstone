package za.ac.cput.service;

/*
IVehicleService.java
Vehicle service interface
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
*/

import za.ac.cput.domain.Vehicle;

import java.util.List;

public interface IVehicleService extends IService<Vehicle, String> {
List<Vehicle> getAllVehicles();}
