package za.ac.cput.service.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.DeliveryOrders;
import za.ac.cput.domain.DeliveryOrders.PaymentStatus;
import za.ac.cput.domain.Shipment;
import za.ac.cput.domain.User;
import za.ac.cput.repository.CustomerRepo.CustomerRepository;
import za.ac.cput.repository.DeliveryOrdersRepo.DeliveryOrdersRepository;
import za.ac.cput.repository.ShipmentRepo.ShipmentRepository;
import za.ac.cput.repository.UserRepo.UserRepository;
import za.ac.cput.service.TrackingEvent.TrackingEventService;
import za.ac.cput.service.ShipmentStatusRules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerAccountService implements ICustomerAccountService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private CustomerRepository customerRepository;
    private DeliveryOrdersRepository orderRepository;
    private ShipmentRepository shipmentRepository;
    private UserRepository userRepository;
    private TrackingEventService trackingService;
    private PasswordEncoder encoder;

    @Autowired
    CustomerAccountService(CustomerRepository customerRepository,
                           DeliveryOrdersRepository orderRepository,
                           ShipmentRepository shipmentRepository,
                           UserRepository userRepository,
                           TrackingEventService trackingService,
                           PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.userRepository = userRepository;
        this.trackingService = trackingService;
        this.encoder = encoder;
    }

    //profile

    @Override
    public Map<String, Object> getProfile(String userId, String customerId) {
        return profile(loadUser(userId), loadCustomer(customerId));
    }

    @Override
    public Map<String, Object> updateProfile(String userId, String customerId, Map<String, String> body) {
        if (body == null) {
            throw bad("Details are required");
        }
        Customer existing = loadCustomer(customerId);
        String name = clean(body.get("customerName"));
        String email = clean(body.get("email"));
        if (name == null || email == null) {
            throw bad("Name and email are required");
        }

        Customer saved;
        try {
            saved = this.customerRepository.save(new Customer.Builder().copy(existing)
                    .setCustomerName(name)
                    .setEmail(email)
                    .setPhoneNumber(clean(body.get("phoneNumber")))
                    .setAddress(clean(body.get("address")))
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That email is already registered");
        }
        return profile(loadUser(userId), saved);
    }

    @Override
    public void changePassword(String userId, Map<String, String> body) {
        if (body == null) {
            throw bad("Details are required");
        }
        String current = body.get("currentPassword");
        String next = body.get("newPassword");
        if (current == null || next == null) {
            throw bad("Current and new password are required");
        }
        if (next.length() < MIN_PASSWORD_LENGTH) {
            throw bad("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        User user = loadUser(userId);
        if (!this.encoder.matches(current, user.getPasswordHash())) {
            throw bad("Current password is wrong");
        }
        this.userRepository.save(new User.Builder().copy(user)
                .setPasswordHash(this.encoder.encode(next))
                .build());
    }

    // cancel order

    @Override
    @Transactional
    public Map<String, Object> cancelOrder(String customerId, String orderId) {
        DeliveryOrders order = this.orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getCustomer() == null
                || !customerId.equals(order.getCustomer().getCustomerId())) {
            return null;
        }
        if (order.getPaymentStatus() == PaymentStatus.CANCELLED
                || order.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order is already cancelled");
        }

        List<Shipment> shipments = this.shipmentRepository.findAll().stream()
                .filter(s -> s.getOrder() != null && orderId.equals(s.getOrder().getOrderId()))
                .toList();

        for (Shipment s : shipments) {
            Shipment.Status from = s.getStatus();
            boolean notLeftYet = from == Shipment.Status.CREATED || from == Shipment.Status.READY_FOR_PICKUP;
            if (!notLeftYet) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "This order can no longer be cancelled (shipment is " + from + ")");
            }
            ShipmentStatusRules.check(from, Shipment.Status.CANCELLED);
        }

        for (Shipment s : shipments) {
            Shipment saved = this.shipmentRepository.save(new Shipment.Builder().copy(s)
                    .setStatus(Shipment.Status.CANCELLED)
                    .build());
            this.trackingService.record(saved, Shipment.Status.CANCELLED, null, "Cancelled by customer");
        }

        PaymentStatus payment = order.getPaymentStatus() == PaymentStatus.COMPLETE
                ? PaymentStatus.REFUNDED : PaymentStatus.CANCELLED;
        DeliveryOrders saved = this.orderRepository.save(new DeliveryOrders.Builder().copy(order)
                .setPaymentStatus(payment)
                .build());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", saved.getOrderId());
        result.put("paymentStatus", saved.getPaymentStatus());
        result.put("shipmentsCancelled", shipments.size());
        return result;
    }

    // helpers

    private User loadUser(String userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Customer loadCustomer(String customerId) {
        if (customerId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private static Map<String, Object> profile(User user, Customer c) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("username", user.getUsername());
        p.put("customerName", c.getCustomerName());
        p.put("phoneNumber", c.getPhoneNumber());
        p.put("email", c.getEmail());
        p.put("address", c.getAddress());
        return p;
    }

    private static String clean(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private static ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}