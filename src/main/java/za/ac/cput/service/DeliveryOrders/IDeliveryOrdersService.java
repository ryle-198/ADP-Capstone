package za.ac.cput.service.DeliveryOrders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.service.IService;

import java.time.LocalDate;
import java.util.List;

public interface IDeliveryOrdersService extends IService<DeliveryOrders,String> {
 List<DeliveryOrders> findByCustomerID(String id);
 List<DeliveryOrders> findByDeliveryStatus(DeliveryOrders.Status deliveryStatus);
 List<DeliveryOrders> findByDeliveryDate(LocalDate deliveryDate);
 List<DeliveryOrders> getAllDeliveryOrders();
 Page<DeliveryOrders> getPagedOrders(Pageable pageable);
 Page<DeliveryOrders> searchOrders(String search, Pageable pageable);
}
