package za.ac.cput.repository.DriverRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Driver;

/*
IDriverRepository.java
Driver repository interface
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
}