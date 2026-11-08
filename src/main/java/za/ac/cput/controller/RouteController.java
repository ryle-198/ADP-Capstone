package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Route;
import za.ac.cput.service.Route.RouteService;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController{
    private RouteService service;

    @Autowired
    RouteController(RouteService service){this.service = service;}

    @PostMapping("/create")
    public Route createRoute(@RequestBody Route route){
        return this.service.create(route);
    }

    @GetMapping("/read/{routeId}")
    public Route readRoute(@PathVariable String routeId){
        return this.service.read(routeId);
    }

    @PutMapping("/update")
    public Route updateRoute(@RequestBody Route route){
        return this.service.create(route);
    }

    @DeleteMapping("/delete/{routeId}")
    public boolean deleteRoute(@PathVariable String routeId){
        return this.service.delete(routeId);
    }

    @GetMapping("/getAllRoute")
    public List<Route> getAllRoutes(){
        return this.service.getAllRoutes();
    }
}
