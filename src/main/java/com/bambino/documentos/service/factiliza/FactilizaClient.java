package com.bambino.documentos.service.factiliza;

import com.bambino.shared.exception.IntegracionExternaException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class FactilizaClient {

    private static final String MENSAJE_NO_ENCONTRADO = "No se encontraron datos para el documento ingresado.";
    private static final String MENSAJE_SERVICIO_NO_DISPONIBLE = "No se pudo consultar el servicio externo en este momento. Intenta nuevamente.";

    private final FactilizaProperties properties;

    public FactilizaClient(FactilizaProperties properties) {
        this.properties = properties;
    }

    public FactilizaResponse<FactilizaDniData> consultarDni(String dni) {
        return get("/dni/info/" + dni, new ParameterizedTypeReference<>() {});
    }

    public FactilizaResponse<FactilizaRucData> consultarRuc(String ruc) {
        return get("/ruc/info/" + ruc, new ParameterizedTypeReference<>() {});
    }

    private <T> FactilizaResponse<T> get(String path, ParameterizedTypeReference<FactilizaResponse<T>> responseType) {
        if (!properties.enabled()) {
            throw new IntegracionExternaException(HttpStatus.SERVICE_UNAVAILABLE, MENSAJE_SERVICIO_NO_DISPONIBLE);
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.timeout());
        requestFactory.setReadTimeout(properties.timeout());

        RestClient restClient = RestClient.builder()
            .baseUrl(properties.apiBaseUrl())
            .requestFactory(requestFactory)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.apiToken())
            .build();

        try {
            FactilizaResponse<T> response = restClient.get()
                .uri(path)
                .retrieve()
                .body(responseType);

            if (response == null || Boolean.FALSE.equals(response.success())) {
                throw new IntegracionExternaException(HttpStatus.NOT_FOUND, MENSAJE_NO_ENCONTRADO);
            }
            return response;
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404 || e.getStatusCode().value() == 422 || e.getStatusCode().is4xxClientError()) {
                throw new IntegracionExternaException(HttpStatus.NOT_FOUND, MENSAJE_NO_ENCONTRADO);
            }
            if (e.getStatusCode().is5xxServerError()) {
                throw new IntegracionExternaException(HttpStatus.SERVICE_UNAVAILABLE, MENSAJE_SERVICIO_NO_DISPONIBLE);
            }
            throw new IntegracionExternaException(HttpStatus.BAD_GATEWAY, MENSAJE_SERVICIO_NO_DISPONIBLE);
        } catch (RestClientException e) {
            throw new IntegracionExternaException(HttpStatus.BAD_GATEWAY, MENSAJE_SERVICIO_NO_DISPONIBLE);
        }
    }
}
