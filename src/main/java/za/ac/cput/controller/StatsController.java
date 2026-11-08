package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.service.Stats.StatsService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class StatsController {

    private StatsService service;

    @Autowired
    StatsController(StatsService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public Map<String, Object> stats() {
        return this.service.getStats();
    }
}