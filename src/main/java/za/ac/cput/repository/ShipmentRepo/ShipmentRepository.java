package za.ac.cput.repository.ShipmentRepo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Shipment;

import java.util.ArrayList;
import java.util.List;

/*
ShipmentRepository.java
Shipment module class
Author: NOMPUMELELO MBATHA (240256727)
Date: 2026
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}