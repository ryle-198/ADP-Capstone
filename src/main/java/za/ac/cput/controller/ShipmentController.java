/* ShipmentController.java
   Shipment Controller class
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Shipment;
import za.ac.cput.service.ShipmentService;
import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping("/create")
    public Shipment create(@RequestBody Shipment shipment) {
        return shipmentService.create(shipment);
    }

    @GetMapping("/read/{id}")
    public Shipment read(@PathVariable String id) {
        return shipmentService.read(id);
    }

    @PutMapping("/update")
    public Shipment update(@RequestBody Shipment shipment) {
        return shipmentService.update(shipment);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable String id) {
        return shipmentService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Shipment> getAll() {
        return shipmentService.getAll();
    }
}