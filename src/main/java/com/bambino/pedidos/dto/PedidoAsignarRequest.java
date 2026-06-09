package com.bambino.pedidos.dto;

import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PedidoAsignarRequest.
 */
public record PedidoAsignarRequest(
    @Size(max = 255, message = "motivo excede longitud")
    String motivo
) {}
