package za.ac.cput.service;

import za.ac.cput.domain.DeliveryOrders;

import java.util.List;

public interface IDeliveryOrdersService extends IService<DeliveryOrders,String>{
 List<DeliveryOrders> getAllDeliveryOrders();
}
