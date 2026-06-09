package com.bambino.pagos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de PagoResponse.
 */
public record PagoResponse(
    Long idPago,
    Long idPedido,
    String metodo,
    String estado,
    BigDecimal monto,
    String proveedor,
    String proveedorTxnId,
    String idempotencyKey,
    String urlPago,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}
