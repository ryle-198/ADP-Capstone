package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.service.DeliveryOrders.DeliveryOrdersService;

import java.util.List;

@RestController
@RequestMapping("/deliveryOrders")//to-do
@CrossOrigin("http://localhost:5173/")
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

    @GetMapping("/search")
    public Page<DeliveryOrders> getOrdersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search
    ){
        Sort sort = direction.equalsIgnoreCase("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);

        if(search != null && !search.isBlank()){
            return this.service.searchOrders(search, pageable);
        }

        return this.service.getPagedOrders(pageable);
    }

}
