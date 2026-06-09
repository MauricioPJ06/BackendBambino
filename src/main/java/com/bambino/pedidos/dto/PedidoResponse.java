package com.bambino.pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de PedidoResponse.
 */
public record PedidoResponse(
    Long idPedido,
    String codigoPedido,
    String estadoActual,
    String modalidad,
    String tipoComprobante,
    BigDecimal subtotal,
    BigDecimal descuentoTotal,
    BigDecimal impuestoTotal,
    BigDecimal total,
    LocalDateTime fechaCreacion
) {}
