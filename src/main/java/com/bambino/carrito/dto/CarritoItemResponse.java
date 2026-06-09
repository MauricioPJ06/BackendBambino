package com.bambino.carrito.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de CarritoItemResponse.
 */
public record CarritoItemResponse(
    Long idCarritoItem,
    Long idProducto,
    String nombreProducto,
    BigDecimal cantidad,
    BigDecimal precioUnitario,
    BigDecimal descuentoUnitario,
    BigDecimal subtotal,
    String observacion,
    String imagenUrl
) {}
