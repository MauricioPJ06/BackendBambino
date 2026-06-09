package com.bambino.delivery.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de CoberturaDeliveryResponse.
 */
public record CoberturaDeliveryResponse(
    Long idZona,
    boolean dentroCobertura,
    BigDecimal distanciaKm,
    BigDecimal radioKm,
    BigDecimal tarifaBase,
    BigDecimal montoMinimo,
    String mensaje
) {}
