package com.bambino.delivery.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de UbicacionRestauranteResponse.
 */
public record UbicacionRestauranteResponse(
    Long idZona,
    String nombreZona,
    BigDecimal latitud,
    BigDecimal longitud,
    String mapaEmbedUrl
) {}
