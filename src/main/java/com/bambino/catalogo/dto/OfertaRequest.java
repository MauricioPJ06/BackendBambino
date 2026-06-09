package com.bambino.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Record que maneja la funcionalidad de OfertaRequest.
 */
public record OfertaRequest(
    @NotBlank(message = "nombre es obligatorio")
    @Size(max = 180, message = "nombre excede longitud")
    String nombre,

    @NotBlank(message = "tipo es obligatorio")
    String tipo,

    BigDecimal valorDescuento,

    BigDecimal precioEspecial,

    @NotBlank(message = "estado es obligatorio")
    String estado,

    @NotNull(message = "fechaInicio es obligatorio")
    LocalDateTime fechaInicio,

    @NotNull(message = "fechaFin es obligatorio")
    LocalDateTime fechaFin,

    List<Long> idsProductos
) {}
