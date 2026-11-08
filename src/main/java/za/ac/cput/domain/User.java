package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User {

    public enum Role {
        ADMIN,
        DISPATCHER,
        DRIVER,
        CUSTOMER
    }

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enable = true;

    private LocalDateTime createdAt;

    //for die driver
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", unique = true)
    @JsonIgnoreProperties({"routes", "hibernateLazyInitializer", "handler"})
    private Driver driver;

    //for die customer
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", unique = true)
    @JsonIgnoreProperties({"orders", "hibernateLazyInitializer", "handler"})
    private Customer customer;

    public User() {
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.passwordHash = builder.passwordHash;
        this.role = builder.role;
        this.enable = builder.enable;
        this.driver = builder.driver;
        this.customer = builder.customer;
        this.createdAt = builder.createdAt;

    }


        @PrePersist
        void onCreate () {
            if (this.id == null || this.id.isBlank()) {
                this.id = UUID.randomUUID().toString();
            }
            if (this.createdAt == null) {
                this.createdAt = LocalDateTime.now();
            }

        }

        public String getId () {
            return id;
        }

        public String getUsername () {
            return username;
        }

        public String getPasswordHash () {
            return passwordHash;
        }

        public Role getRole () {
            return role;
        }

        public boolean isEnable () {
            return enable;
        }

        public LocalDateTime getCreatedAt () {
            return createdAt;
        }

        public Driver getDriver () {
            return driver;
        }

        public Customer getCustomer () {
            return customer;
        }

        @Override
        public String toString () {
            return "User{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", role=" + role +
                    ", enable=" + enable +
                    '}';
        }

        public static class Builder {
            private String id;
            private String username;
            private String passwordHash;
            private Role role;
            private boolean enable = true;
            private Driver driver;
            private Customer customer;
            private LocalDateTime createdAt;

            public Builder setId(String id) {
                this.id = id;
                return this;
            }

            public Builder setUsername(String username) {
                this.username = username;
                return this;
            }

            public Builder setPasswordHash(String passwordHash) {
                this.passwordHash = passwordHash;
                return this;
            }

            public Builder setRole(Role role) {
                this.role = role;
                return this;
            }

            public Builder setEnabled(boolean enable) {
                this.enable = enable;
                return this;
            }

            public Builder setDriver(Driver driver) {
                this.driver = driver;
                return this;
            }

            public Builder setCustomer(Customer customer) {
                this.customer = customer;
                return this;
            }

            public Builder copy(User user) {
                this.id = user.id;
                this.username = user.username;
                this.passwordHash = user.passwordHash;
                this.role = user.role;
                this.enable = user.enable;
                this.driver = user.driver;
                this.customer = user.customer;
                this.createdAt = user.createdAt;

                return this;
            }

            public User build() {return new User(this);}
    }
}