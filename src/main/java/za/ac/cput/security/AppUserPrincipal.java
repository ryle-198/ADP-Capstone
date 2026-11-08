package za.ac.cput.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import za.ac.cput.domain.User;

import java.util.Collection;
import java.util.List;

public class AppUserPrincipal implements UserDetails {

    private final String userId;
    private final String username;
    private final String passwordHash;
    private final User.Role role;
    private final boolean enabled;
    private final String driverId;
    private final String customerId;

    public AppUserPrincipal(User user){
        this.userId = user.getId();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.role = user.getRole();
        this.enabled = user.isEnable();
        this.driverId = user.getDriver() != null ? user.getDriver().getId() : null;
        this.customerId = user.getCustomer() != null ? user.getCustomer().getCustomerId() : null;
    }


    public String getUserId() {
        return userId;
    }

    public User.Role getRole() {
        return role;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
