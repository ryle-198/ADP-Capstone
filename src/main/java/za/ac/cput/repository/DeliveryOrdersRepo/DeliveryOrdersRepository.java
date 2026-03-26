package za.ac.cput.repository.DeliveryOrdersRepo;

import za.ac.cput.domain.DeliveryOrders;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOrdersRepository implements IDeliveryOrdersRepository{

    public static IDeliveryOrdersRepository repository = null;
    private List<DeliveryOrders> ordersList;

    private DeliveryOrdersRepository(){
        ordersList = new ArrayList<>();
    }

    public static IDeliveryOrdersRepository getRepository(){
        if(repository==null){
            repository = new DeliveryOrdersRepository();
        }
        return repository;
    }

    @Override
    public DeliveryOrders create(DeliveryOrders deliveryOrder) {
        boolean success = ordersList.add(deliveryOrder);
        if(success){
            return deliveryOrder;
        }
        return null;
    }

    @Override
    public DeliveryOrders read(String orderId) {

       for(DeliveryOrders orders:ordersList){
           if(orders.getOrderId().equals(orderId)){
               return orders;
           }
       }
        return null;
    }

    @Override
    public DeliveryOrders update(DeliveryOrders deliveryOrders) {
        String id = deliveryOrders.getOrderId();
        DeliveryOrders oldOrder = read(id);

        if(oldOrder==null){
            return null;
        }

        boolean success = ordersList.remove(oldOrder);

        if(!success){
            return null;
        }

        if(ordersList.add(deliveryOrders)){
            return deliveryOrders;
        }
        return null;
    }

    @Override
    public boolean delete(String orderId) {
        DeliveryOrders orderToDelete = read(orderId);

        if(orderToDelete==null){
            return false;
        }
        return ordersList.remove(orderToDelete);
    }

    @Override
    public List<DeliveryOrders> getAllOrders() {
        return ordersList;
    }
}
