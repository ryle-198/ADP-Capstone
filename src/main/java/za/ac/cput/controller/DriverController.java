package za.ac.cput.controller;
/*
DriverController.java
Driver controller class
Author: Angel Dineo Masonganye (223008869)
Date: 2026
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Driver;
import za.ac.cput.service.DriverService;

import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverService service;

    @Autowired
    public DriverController(DriverService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Driver> create(@RequestBody Driver driver) {
        return ResponseEntity.ok(service.create(driver));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Driver> read(@PathVariable String id) {
        Driver driver = service.read(id);
        if (driver == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(driver);
    }

    @PutMapping("/update")
    public ResponseEntity<Driver> update(@RequestBody Driver driver) {
        Driver updated = service.update(driver);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @GetMapping("/getAll")
    public List<Driver> getAll(){
        return this.service.getAll();
    }
}