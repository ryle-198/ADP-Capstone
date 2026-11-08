package za.ac.cput.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.service.DeliveryOrders.OrderPricing;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    @PostMapping
    public Map<String, Object> quote(@RequestBody Map<String, Object> body) {
        float weight = parseWeight(body == null ? null : body.get("weight"));
        if (weight <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weight must be more than 0");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("weightKg", weight);
        result.put("baseFee", OrderPricing.BASE_FEE);
        result.put("ratePerKg", OrderPricing.RATE_PER_KG);
        result.put("price", OrderPricing.calculate(weight));
        return result;
    }

    private static float parseWeight(Object value) {
        if (value == null) return 0f;
        try {
            return Float.parseFloat(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}