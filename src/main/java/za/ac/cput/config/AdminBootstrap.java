package za.ac.cput.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepo.UserRepository;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final String username;
    private final String password;

    public AdminBootstrap(
            UserRepository repository,
            PasswordEncoder encoder,
            @Value("${APP_ADMIN_USERNAME:admin}")String username,
            @Value("${APP_ADMIN_PASSWORD:admin}")String password){

        this.repository=repository;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(repository.existsByRole(User.Role.ADMIN)){
            return;
        }
        if(password==null || password.isBlank()){
            log.warn("No Admin user exists and APP_ADMIN_PASSWORD is not set" + "Set it and restart to create the first admin.");
            return;
        }

        User admin = new User.Builder()
                .setUsername(username)
                .setPasswordHash(encoder.encode(password))
                .setRole(User.Role.ADMIN)
                .setEnabled(true)
                .build();
        repository.save(admin);
        log.info("Created initial ADMIN user '{}'", username);
    }
}
