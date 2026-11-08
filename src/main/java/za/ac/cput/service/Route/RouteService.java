package za.ac.cput.service.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Route;
import za.ac.cput.repository.RouteRepo.RouteRepository;
import za.ac.cput.service.RouteAssignmentCheck;

import java.util.List;

@Service
public class RouteService implements IRouteService{
    private RouteRepository repository;
    private RouteAssignmentCheck assignmentCheck;

    @Autowired
    RouteService(RouteRepository repository, RouteAssignmentCheck assignmentCheck){
        this.repository=repository;
        this.assignmentCheck = assignmentCheck;
    }

    @Override
    public Route create(Route route) {
        assignmentCheck.check(route);
        return this.repository.save(route);
    }

    @Override
    public Route read(String s) {
        return this.repository.findById(s).orElse(null);
    }

    @Override
    public Route update(Route route) {
        assignmentCheck.check(route);
        return this.repository.save(route);
    }

    @Override
    public boolean delete(String s) {
        if (!this.repository.existsById(s)) {
            return false;
        }
        try {
            this.repository.deleteById(s);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Route still has shipments");
        }
        return true;
    }

    @Override
    public List<Route> getAllRoutes() {
        return this.repository.findAll();
    }
}