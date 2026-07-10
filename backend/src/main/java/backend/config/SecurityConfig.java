package backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

// This class contains configuration code.
// Look inside it for objects/beans that should be managed by Spring.
@Configuration
public class SecurityConfig {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    // Bean == Create this object and store it in the Spring application context.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // The springboot security dependency we added in pom blocks unauthorized requests.
    // /auth/register public
    // /auth/login public
    // everything else is also public for now

    // Spring Security is currently used for CORS, CSRF config, and password encoding.
    // Route authorization is handled manually in controllers/services using HttpSession
    // and AuthService checks such as requireAdminUser(session).
    //
    // Future improvement: move route-level authorization into Spring Security filters/roles.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .anyRequest().permitAll()
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        // Allows cookies/session IDs to be sent
        config.setAllowCredentials(true);

        // This creates an object that lets you attach CORS rules to URL patterns
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Use this CORS config for every backend route.
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}