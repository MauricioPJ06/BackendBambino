package com.bambino.configuracion.dto;

/**
 * Record que maneja la funcionalidad de SerieComprobanteResponse.
 */
public record SerieComprobanteResponse(
    Long idSerie,
    Long idEmpresa,
    String tipoComprobante,
    String serie,
    Long correlativoActual,
    Boolean activo
) {}
