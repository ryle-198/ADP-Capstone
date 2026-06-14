package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.service.DeliveryOrdersService;

import java.util.List;

@RestController
@RequestMapping("/deliveryOrders")//to-do
public class DeliveryOrdersController {
    private DeliveryOrdersService service;

    @Autowired
    DeliveryOrdersController(DeliveryOrdersService service){this.service=service;}

    @PostMapping("/create")
    public DeliveryOrders createDeliveryOrder(@RequestBody DeliveryOrders deliveryOrders){
        return this.service.create(deliveryOrders);
    }

    @GetMapping("/read/{deliveryOrderId}")
    public DeliveryOrders readDeliveryOrder(@PathVariable String deliveryOrderId){
        return this.service.read(deliveryOrderId);
    }

    @PutMapping("/update")
    public DeliveryOrders updateDeliveryOrder(@RequestBody DeliveryOrders updatedDeliveryOrders){
        return this.service.update(updatedDeliveryOrders);
    }

    @DeleteMapping("/delete/{deliveryOrderId}")
    public boolean deleteOrder(@PathVariable String deliveryOrderId){
        return this.service.delete(deliveryOrderId);
    }

    @GetMapping("/getAll")
    public List<DeliveryOrders> getAllDeliveryOrders(){
        return this.service.getAllDeliveryOrders();
    }

}
