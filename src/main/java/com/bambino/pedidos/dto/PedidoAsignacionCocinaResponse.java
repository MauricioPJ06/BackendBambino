package com.bambino.pedidos.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de PedidoAsignacionCocinaResponse.
 */
public record PedidoAsignacionCocinaResponse(
    Long idAsignacion,
    Long idPedido,
    Long idUsuarioCocina,
    String motivo,
    LocalDateTime fechaAsignacion
) {}
