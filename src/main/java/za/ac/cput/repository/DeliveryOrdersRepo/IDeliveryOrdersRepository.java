package za.ac.cput.repository.DeliveryOrdersRepo;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.repository.IRepository;

import java.util.List;
/*
IDeliveryOrdersRepository.java
Delivery Orders module class
Author: Ryle Peter May (230333907)
Date: 2026
 */

public interface IDeliveryOrdersRepository extends IRepository<DeliveryOrders , String> {
    List<DeliveryOrders> getAllOrders();
}
