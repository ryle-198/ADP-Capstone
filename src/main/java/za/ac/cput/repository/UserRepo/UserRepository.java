package za.ac.cput.repository.UserRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(User.Role role);

    // used to stop two user accounts being linked to the same driver / customer
    boolean existsByDriver_Id(String driverId);

    boolean existsByCustomer_CustomerId(String customerId);

    // deleting a customer / driver also removes their login account
    @Transactional
    void deleteByCustomer_CustomerId(String customerId);

    @Transactional
    void deleteByDriver_Id(String driverId);
}