package za.ac.cput.repository.VehicleRepo;

/*
IVehicleRepository.java
Vehicle repository interface
Author: Litha Owethu Mazibuko (240143485)
Date: 2026
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Vehicle;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, String> {
}