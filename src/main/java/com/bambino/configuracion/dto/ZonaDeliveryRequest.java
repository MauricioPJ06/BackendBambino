package com.bambino.configuracion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ZonaDeliveryRequest.
 */
public record ZonaDeliveryRequest(
    @NotBlank(message = "nombre es obligatorio")
    @Size(max = 120, message = "nombre excede longitud")
    String nombre,

    @NotNull(message = "activo es obligatorio")
    Boolean activo,

    @NotNull(message = "tarifaBase es obligatoria")
    @DecimalMin(value = "0.00", message = "tarifaBase no valida")
    BigDecimal tarifaBase,

    @NotNull(message = "montoMinimo es obligatorio")
    @DecimalMin(value = "0.00", message = "montoMinimo no valido")
    BigDecimal montoMinimo,

    @NotNull(message = "tiempoEstimadoMinutos es obligatorio")
    @Min(value = 1, message = "tiempoEstimadoMinutos no valido")
    Integer tiempoEstimadoMinutos,

    @Size(max = 300, message = "coberturaDescripcion excede longitud")
    String coberturaDescripcion,

    String mapaEmbedUrl,

    BigDecimal latitudCentro,
    BigDecimal longitudCentro,
    @DecimalMin(value = "0.10", message = "radioKm no valido")
    BigDecimal radioKm,

    String horaInicioAtencion,
    String horaFinAtencion
) {}
