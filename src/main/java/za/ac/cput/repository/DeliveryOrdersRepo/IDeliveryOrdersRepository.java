package za.ac.cput.repository.DeliveryOrdersRepo;

import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface IDeliveryOrdersRepository extends IRepository<DeliveryOrders , String> {
    List<DeliveryOrders> getAllOrders();
}
