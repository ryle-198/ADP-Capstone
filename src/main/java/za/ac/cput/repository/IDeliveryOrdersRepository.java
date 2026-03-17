package za.ac.cput.repository;

import za.ac.cput.domain.DeliveryOrders;

import java.util.List;

public interface IDeliveryOrdersRepository extends IRepository<DeliveryOrders , String>{
    List<DeliveryOrders> getAllOrders();
}
