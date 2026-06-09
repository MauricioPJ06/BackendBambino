package com.bambino.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
/**
 * Clase que maneja la funcionalidad de SecurityConfig.
 */
public class SecurityConfig {

    @Bean
    /**
     * Metodo que realiza la operacion de securityFilterChain.
     * @param http parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/cocina/**").hasAnyRole("COCINA", "ADMIN")
                .requestMatchers("/api/seguridad/**").hasAnyRole("COCINA", "ADMIN")
                .requestMatchers("/api/cliente/**").hasAnyRole("CLIENTE", "ADMIN")
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    /**
     * Metodo que realiza la operacion de passwordEncoder.
     * @return resultado de la operacion
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    /**
     * Metodo que realiza la operacion de corsConfigurationSource.
     * @return resultado de la operacion
     */
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://bambino.proyectoutp.com",
            "http://localhost:4200",
            "http://localhost:5173",
            "http://localhost:5174"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
