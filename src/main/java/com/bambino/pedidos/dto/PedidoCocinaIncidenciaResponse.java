package com.bambino.pedidos.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de PedidoCocinaIncidenciaResponse.
 */
public record PedidoCocinaIncidenciaResponse(
    Long idIncidencia,
    Long idPedido,
    Long idUsuarioCocina,
    String tipoIncidencia,
    String detalle,
    LocalDateTime fechaCreacion
) {}
