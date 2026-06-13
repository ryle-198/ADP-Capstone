package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;

import java.util.List;
@Service
public class DeliveryOrdersService implements IDeliveryOrdersService {

    private DeliveryOrdersRepository repository;

    @Autowired
    DeliveryOrdersService(DeliveryOrdersRepository repository){this.repository=repository;}

    @Override
    public DeliveryOrders create(DeliveryOrders deliveryOrders) {
        return this.repository.save(deliveryOrders);
    }

    @Override
    public DeliveryOrders read(String s) {
        return this.repository.findById(s).orElse(null);
    }

    @Override
    public DeliveryOrders update(DeliveryOrders deliveryOrders) {
        return this.repository.save(deliveryOrders);
    }

    @Override
    public boolean delete(String s) {
        this.repository.deleteById(s);
        return true;
    }

    @Override
    public List<DeliveryOrders> getAllDeliveryOrders() {
        return this.repository.findAll();
    }
}
