package backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// This class contains configuration code.
// Look inside it for objects/beans that should be managed by Spring.
@Configuration
public class SecurityConfig {
    // Bean == Create this object and store it in the Spring application context.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // The springboot security dependency we added in pom blocks unathorized requests for now we make:
    // /auth/register public
    // /auth/login public
    // everything else is also public for now
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .anyRequest().permitAll()
            )
            .build();
    }
}
