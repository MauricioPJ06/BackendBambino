package com.bambino.configuracion.dto;

/**
 * Record que maneja la funcionalidad de TransicionPedidoConfigResponse.
 */
public record TransicionPedidoConfigResponse(
    Long idTransicion,
    String estadoOrigen,
    String estadoDestino,
    String actorTipo,
    Boolean activo
) {}
