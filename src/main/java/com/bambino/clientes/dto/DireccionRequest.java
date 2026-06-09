package com.bambino.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de DireccionRequest.
 */
public record DireccionRequest(
    @NotBlank(message = "direccionLineal es obligatorio")
    @Size(max = 220, message = "direccionLineal excede longitud")
    String direccionLinea1,

    @Size(max = 220, message = "referencia excede longitud")
    String referencia,

    @Size(max = 120, message = "distrito excede longitud")
    String distrito,

    @Size(max = 120, message = "ciudad excede longitud")
    String ciudad,

    BigDecimal latitud,
    BigDecimal longitud,

    @Size(max = 120, message = "googlePlaceId excede longitud")
    String googlePlaceId,

    @Size(max = 40, message = "googlePlusCode excede longitud")
    String googlePlusCode
) {
}
