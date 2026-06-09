package com.bambino.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PedidoCambiarEstadoRequest.
 */
public record PedidoCambiarEstadoRequest(
    @NotBlank(message = "estadoDestino es obligatorio")
    String estadoDestino,

    @Size(max = 255, message = "motivo excede longitud")
    String motivo
) {}
