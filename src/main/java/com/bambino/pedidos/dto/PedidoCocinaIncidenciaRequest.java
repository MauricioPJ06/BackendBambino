package com.bambino.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PedidoCocinaIncidenciaRequest.
 */
public record PedidoCocinaIncidenciaRequest(
    @NotBlank(message = "tipoIncidencia es obligatorio")
    @Size(max = 40, message = "tipoIncidencia excede longitud")
    String tipoIncidencia,

    @NotBlank(message = "detalle es obligatorio")
    @Size(max = 255, message = "detalle excede longitud")
    String detalle
) {}
