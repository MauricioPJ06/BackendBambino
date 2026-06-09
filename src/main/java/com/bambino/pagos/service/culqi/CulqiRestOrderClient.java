package com.bambino.pagos.service.culqi;

import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CulqiRestOrderClient implements CulqiOrderClient {

    private final RestClient restClient;
    private final CulqiProperties properties;

    public CulqiRestOrderClient(RestClient.Builder restClientBuilder, CulqiProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.apiBaseUrl()).build();
        this.properties = properties;
    }

    @Override
    public CulqiOrderResponse crearOrden(CulqiOrderRequest request) {
        if (!properties.enabled()) {
            throw new NegocioException("Culqi no esta configurado");
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("amount", toCents(request.amount()));
        payload.put("currency_code", request.currency());
        payload.put("description", request.description());
        payload.put("order_number", request.orderNumber());
        payload.put("client_details", clientDetails(request));
        payload.put("expiration_date", Instant.now().plusSeconds(900).getEpochSecond());
        payload.put("confirm", false);
        payload.put("metadata", metadata(request));

        try {
            CulqiOrderResponse response = restClient.post()
                .uri("/v2/orders")
                .header("Authorization", "Bearer " + properties.secretKey())
                .header("Content-Type", "application/json")
                .body(payload)
                .retrieve()
                .body(CulqiOrderResponse.class);

            if (response == null || response.id() == null || response.id().isBlank()) {
                throw new NegocioException("Culqi no devolvio una orden valida");
            }
            return response;
        } catch (RestClientResponseException e) {
            throw new NegocioException("Culqi no pudo crear la orden de pago: " + resolveCulqiMessage(e));
        } catch (RestClientException e) {
            throw new NegocioException("Culqi no pudo crear la orden de pago");
        }
    }

    private Integer toCents(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private Map<String, String> clientDetails(CulqiOrderRequest request) {
        Map<String, String> client = new LinkedHashMap<>();
        client.put("first_name", request.firstName());
        client.put("last_name", request.lastName());
        client.put("email", request.email());
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            client.put("phone_number", request.phoneNumber());
        }
        return client;
    }

    private Map<String, String> metadata(CulqiOrderRequest request) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("checkout_order_number", request.orderNumber());
        if (request.documentNumber() != null && !request.documentNumber().isBlank()) {
            metadata.put("documentNumber", request.documentNumber());
        }
        return metadata;
    }

    private String resolveCulqiMessage(RestClientResponseException e) {
        String body = e.getResponseBodyAsString();
        if (body == null || body.isBlank()) {
            return "respuesta HTTP " + e.getStatusCode().value();
        }
        String compact = body
            .replaceAll("(?i)sk_(test|live)_[A-Za-z0-9]+", "sk_***")
            .replaceAll("\\s+", " ")
            .trim();
        return compact.length() > 240 ? compact.substring(0, 240) : compact;
    }
}
