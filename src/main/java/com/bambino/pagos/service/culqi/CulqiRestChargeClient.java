package com.bambino.pagos.service.culqi;

import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CulqiRestChargeClient implements CulqiChargeClient {

    private final RestClient restClient;
    private final CulqiProperties properties;

    public CulqiRestChargeClient(RestClient.Builder restClientBuilder, CulqiProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.apiBaseUrl()).build();
        this.properties = properties;
    }

    @Override
    public CulqiChargeResponse crearCargo(CulqiChargeRequest request) {
        if (!properties.enabled()) {
            throw new NegocioException("Culqi no esta configurado");
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("amount", toCents(request.amount()));
        payload.put("currency_code", request.currency());
        payload.put("email", request.email());
        payload.put("source_id", request.sourceId());
        payload.put("capture", true);
        payload.put("description", request.description());
        payload.put("metadata", metadata(request));

        try {
            CulqiChargeResponse response = restClient.post()
                .uri("/v2/charges")
                .header("Authorization", "Bearer " + properties.secretKey())
                .header("Content-Type", "application/json")
                .body(payload)
                .retrieve()
                .body(CulqiChargeResponse.class);

            if (response == null || response.id() == null || response.id().isBlank()) {
                throw new NegocioException("Culqi no devolvio un cargo valido");
            }
            return response;
        } catch (RestClientException e) {
            throw new NegocioException("Culqi no pudo confirmar el pago");
        }
    }

    private Integer toCents(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private Map<String, String> metadata(CulqiChargeRequest request) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("order_id", request.orderId());
        if (request.documentNumber() != null && !request.documentNumber().isBlank()) {
            metadata.put("documentNumber", request.documentNumber());
        }
        return metadata;
    }
}
