package za.ac.cput.dto;

import za.ac.cput.domain.User;

import java.time.LocalDateTime;

public final class UserDto {

    private UserDto() {
    }

    public record CreateUserRequest(String username,
                                    String password,
                                    User.Role role,
                                    String driverId,
                                    String customerId) {
    }

    public record ResetPasswordRequest(String newPassword) {
    }
}