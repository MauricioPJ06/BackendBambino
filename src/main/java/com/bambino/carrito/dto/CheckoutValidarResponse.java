package com.bambino.carrito.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de CheckoutValidarResponse.
 */
public record CheckoutValidarResponse(
    boolean valido,
    String mensaje,
    String modalidad,
    String tipoComprobante,
    BigDecimal subtotal,
    BigDecimal descuentoTotal,
    BigDecimal impuestoTotal,
    BigDecimal costoDelivery,
    BigDecimal total
) {}
