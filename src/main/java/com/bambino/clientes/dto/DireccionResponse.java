package com.bambino.clientes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de DireccionResponse.
 */
public record DireccionResponse(
    Long idDireccion,
    String direccionLinea1,
    String referencia,
    String distrito,
    String ciudad,
    BigDecimal latitud,
    BigDecimal longitud,
    String googlePlaceId,
    String googlePlusCode,
    Boolean esPrincipal,
    Boolean activo,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {
}
