package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.security.AppUserPrincipal;
import za.ac.cput.service.Customer.ICustomerAccountService;

import java.util.Map;

@RestController
@RequestMapping("/my/customer")
public class CustomerAccountController {

    private ICustomerAccountService service;

    @Autowired
    CustomerAccountController(ICustomerAccountService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Map<String, Object> profile(@AuthenticationPrincipal AppUserPrincipal me) {
        return this.service.getProfile(me.getUserId(), me.getCustomerId());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> body,
                                             @AuthenticationPrincipal AppUserPrincipal me) {
        return this.service.updateProfile(me.getUserId(), me.getCustomerId(), body);
    }

    @PostMapping("/password")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> changePassword(@RequestBody Map<String, String> body,
                                               @AuthenticationPrincipal AppUserPrincipal me) {
        this.service.changePassword(me.getUserId(), body);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable String orderId,
                                                           @AuthenticationPrincipal AppUserPrincipal me) {
        Map<String, Object> result = this.service.cancelOrder(me.getCustomerId(), orderId);
        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }
}