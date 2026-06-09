package com.bambino.configuracion.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Record que maneja la funcionalidad de TransicionPedidoConfigRequest.
 */
public record TransicionPedidoConfigRequest(
    @NotNull(message = "activo es obligatorio")
    Boolean activo
) {}
