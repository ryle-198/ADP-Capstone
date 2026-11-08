package za.ac.cput.service.User;

import za.ac.cput.domain.User;
import za.ac.cput.dto.UserDto;

import java.util.List;

public interface IUserService {
    User create(UserDto.CreateUserRequest request);

    User read(String id);

    User update(User user);

    boolean delete(String id);

    List<User> getAll();

    User setEnabled(String id, boolean enabled);

    boolean resetPassword(String id, String password);
}
