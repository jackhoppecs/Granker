package backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.model.User;
import backend.repository.UserRepository;

@Configuration
public class AdminInitializer {

    @Value("${app.admin.create-enabled}")
    private boolean adminCreateEnabled;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initializeAdmin(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!adminCreateEnabled) {
                return;
            }

            if (adminUsername == null || adminUsername.isBlank()
                    || adminEmail == null || adminEmail.isBlank()
                    || adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException(
                    "Admin creation is enabled, but ADMIN_USERNAME, ADMIN_EMAIL, or ADMIN_PASSWORD is missing."
                );
            }

            if (userRepository.findByEmail(adminEmail).isPresent()) {
                return;
            }

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setAdmin(true);

            userRepository.save(admin);
        };
    }
}