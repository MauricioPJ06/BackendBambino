package com.bambino.comprobantes.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ComprobanteDetalleResponse.
 */
public record ComprobanteDetalleResponse(
    Long idComprobanteDetalle,
    String descripcionItem,
    BigDecimal cantidad,
    BigDecimal precioUnitario,
    BigDecimal descuentoUnitario,
    BigDecimal subtotalLinea
) {}
