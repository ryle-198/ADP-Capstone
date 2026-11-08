package za.ac.cput.service.Customer;

import java.util.Map;

public interface ICustomerAccountService {

    Map<String, Object> getProfile(String userId, String customerId);

    Map<String, Object> updateProfile(String userId, String customerId, Map<String, String> body);

    void changePassword(String userId, Map<String, String> body);

    Map<String, Object> cancelOrder(String customerId, String orderId);
}