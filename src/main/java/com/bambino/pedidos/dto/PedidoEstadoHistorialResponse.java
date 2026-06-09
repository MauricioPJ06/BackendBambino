package com.bambino.pedidos.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de PedidoEstadoHistorialResponse.
 */
public record PedidoEstadoHistorialResponse(
    String estadoOrigen,
    String estadoDestino,
    String actorTipo,
    Long actorId,
    String motivo,
    LocalDateTime fechaCreacion
) {}
