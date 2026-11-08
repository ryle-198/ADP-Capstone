package za.ac.cput.service.Stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.Driver;
import za.ac.cput.domain.Shipment;
import za.ac.cput.domain.Vehicle;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.DriverRepo.DriverRepository;
import za.ac.cput.repository.RouteRepo.RouteRepository;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import za.ac.cput.repository.VehicleRepo.VehicleRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class StatsService {

    private final DeliveryOrdersRepository orderRepository;
    private final ShipmentRepository shipmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final RouteRepository routeRepository;

    @Autowired
    StatsService(DeliveryOrdersRepository orderRepository,
                 ShipmentRepository shipmentRepository,
                 DriverRepository driverRepository,
                 VehicleRepository vehicleRepository,
                 CustomerRepository customerRepository,
                 RouteRepository routeRepository) {
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.routeRepository = routeRepository;
    }

    public Map<String, Object> getStats() {
        List<DeliveryOrders> orders = this.orderRepository.findAll();
        List<Shipment> shipments = this.shipmentRepository.findAll();
        List<Driver> drivers = this.driverRepository.findAll();
        List<Vehicle> vehicles = this.vehicleRepository.findAll();

        double paid = 0;
        double outstanding = 0;
        for (DeliveryOrders o : orders) {
            if (o.getPaymentStatus() == DeliveryOrders.PaymentStatus.COMPLETE) {
                paid += o.getTotalCost();
            } else if (o.getPaymentStatus() == DeliveryOrders.PaymentStatus.PENDING) {
                outstanding += o.getTotalCost();
            }
        }

        long availableDrivers = drivers.stream().filter(Driver::isAvailability).count();
        long availableVehicles = vehicles.stream()
                .filter(v -> v.getCurrentStatus() == Vehicle.VehicleStatus.AVAILABLE).count();
        long unassigned = shipments.stream().filter(s -> s.getRoute() == null).count();

        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put("customers", this.customerRepository.count());
        totals.put("orders", orders.size());
        totals.put("shipments", shipments.size());
        totals.put("routes", this.routeRepository.count());
        totals.put("drivers", drivers.size());
        totals.put("vehicles", vehicles.size());

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totals", totals);
        stats.put("ordersByStatus", countBy(orders, DeliveryOrders::getDeliveryStatus));
        stats.put("ordersByPayment", countBy(orders, DeliveryOrders::getPaymentStatus));
        stats.put("revenuePaid", round(paid));
        stats.put("revenueOutstanding", round(outstanding));
        stats.put("shipmentsByStatus", countBy(shipments, Shipment::getStatus));
        stats.put("shipmentsWithoutRoute", unassigned);
        stats.put("driversAvailable", availableDrivers);
        stats.put("driversUnavailable", drivers.size() - availableDrivers);
        stats.put("vehiclesAvailable", availableVehicles);
        stats.put("vehiclesNotAvailable", vehicles.size() - availableVehicles);
        return stats;
    }

    private static <T, E extends Enum<E>> Map<String, Long> countBy(List<T> items, Function<T, E> getter) {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (T item : items) {
            E value = getter.apply(item);
            counts.merge(value == null ? "UNKNOWN" : value.name(), 1L, Long::sum);
        }
        return counts;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}