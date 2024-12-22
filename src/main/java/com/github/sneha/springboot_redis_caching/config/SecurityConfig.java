package com.github.sneha.springboot_redis_caching.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfig class configures the security settings for the application.
 * This includes authentication, authorization, and session management.
 *
 * @author sneharavikumartl
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.admin.name}")
    private String adminUsername;

    @Value("${spring.security.admin.password}")
    private String adminPassword;

    /**
     * Bean for creating an in-memory user details service with an admin user.
     * The admin's password is encoded using BCrypt and added to the in-memory user details manager.
     *
     * @return UserDetailsService containing the admin user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword)) // Encoding the password using BCrypt
                .roles("ADMIN") // Assigning the ADMIN role
                .build();
        return new InMemoryUserDetailsManager(Collections.singleton(adminUser)); // Storing user details in memory
    }

    /**
     * Bean for BCrypt password encoder used for encoding passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCrypt algorithm for encoding
    }

    /**
     * Bean for DaoAuthenticationProvider used for authentication with the in-memory user details service.
     * It also uses the BCryptPasswordEncoder for password matching.
     *
     * @return DaoAuthenticationProvider instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService()); // Setting the user details service
        auth.setPasswordEncoder(passwordEncoder()); // Setting the password encoder for authentication
        return auth;
    }

    /**
     * Bean for AuthenticationManager used for managing authentication in the application.
     * This authentication manager is built using the authentication provider.
     *
     * @param http HttpSecurity object
     * @return AuthenticationManager instance
     * @throws Exception if any error occurs during authentication manager creation
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(authenticationProvider()); // Configuring the authentication provider
        return authManagerBuilder.build(); // Returning the built AuthenticationManager
    }

    /**
     * Bean for configuring the security filter chain.
     * This defines the security settings for HTTP requests, such as CSRF, session management, and request authorization.
     *
     * @param http HttpSecurity object
     * @return SecurityFilterChain instance
     * @throws Exception if any error occurs during security filter chain creation
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults()) // Enabling basic HTTP authentication
                .csrf(AbstractHttpConfigurer::disable) // Disabling CSRF protection (use with caution)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs/*", "/swagger-resources/*", "/webjars/*", "/actuator/*", "/error", "/favicon.ico", "/users/*", "/users/*/*").permitAll() // Permitting specific URLs
                        .requestMatchers("/admin/*", "/admin/*/*").authenticated() // Restricting access to /admin/* to authenticated users
                        .anyRequest().authenticated() // All other requests require authentication
                );
        return http.build(); // Building and returning the security filter chain
    }
}
