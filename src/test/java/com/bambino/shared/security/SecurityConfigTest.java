package com.bambino.shared.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigTest {

    @Test
    void corsConfigurationSource_permiteOrigenesLocalesDeDesarrollo() {
        SecurityConfig securityConfig = new SecurityConfig();

        CorsConfiguration configuration = securityConfig
            .corsConfigurationSource()
            .getCorsConfiguration(new MockHttpServletRequest("GET", "/api/public/catalogo/productos"));

        assertNotNull(configuration);
        assertNotNull(configuration.getAllowedOrigins());
        assertTrue(configuration.getAllowedOrigins().contains("http://localhost:4200"));
        assertTrue(configuration.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(configuration.getAllowedOrigins().contains("http://localhost:5174"));
    }
}
