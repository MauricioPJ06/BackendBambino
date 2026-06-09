package com.bambino.documentos.service.factiliza;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class FactilizaProperties {

    private final String apiToken;
    private final String apiBaseUrl;
    private final Duration timeout;

    public FactilizaProperties(
        @Value("${factiliza.api-token:}") String apiToken,
        @Value("${factiliza.api-base-url:https://api.factiliza.com/v1}") String apiBaseUrl,
        @Value("${factiliza.timeout-seconds:10}") long timeoutSeconds
    ) {
        this.apiToken = limpiar(apiToken);
        this.apiBaseUrl = limpiar(apiBaseUrl);
        this.timeout = Duration.ofSeconds(Math.max(1L, timeoutSeconds));
    }

    public String apiToken() {
        return apiToken;
    }

    public String apiBaseUrl() {
        return apiBaseUrl.isBlank() ? "https://api.factiliza.com/v1" : apiBaseUrl;
    }

    public Duration timeout() {
        return timeout;
    }

    public boolean enabled() {
        return !apiToken.isBlank();
    }

    private String limpiar(String value) {
        return value == null ? "" : value.trim();
    }
}
