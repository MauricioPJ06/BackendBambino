package com.bambino.configuracion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de SerieComprobanteRequest.
 */
public record SerieComprobanteRequest(
    @NotNull(message = "idEmpresa es obligatorio")
    Long idEmpresa,

    @NotBlank(message = "tipoComprobante es obligatorio")
    String tipoComprobante,

    @NotBlank(message = "serie es obligatoria")
    @Size(max = 10, message = "serie excede longitud")
    String serie,

    @NotNull(message = "correlativoActual es obligatorio")
    @Min(value = 0, message = "correlativoActual no valido")
    Long correlativoActual,

    @NotNull(message = "activo es obligatorio")
    Boolean activo
) {}
