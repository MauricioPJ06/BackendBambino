package com.bambino.carrito.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Record que maneja la funcionalidad de CarritoResumenResponse.
 */
public record CarritoResumenResponse(
    Long idCarrito,
    String estado,
    BigDecimal subtotal,
    BigDecimal descuentoTotal,
    BigDecimal impuestoTotal,
    BigDecimal costoDelivery,
    BigDecimal total,
    BigDecimal totalItems,
    List<CarritoItemResponse> items
) {}
