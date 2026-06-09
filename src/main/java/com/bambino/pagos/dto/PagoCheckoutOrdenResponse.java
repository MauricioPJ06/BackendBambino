package com.bambino.pagos.dto;

import java.math.BigDecimal;

/**
 * Respuesta para abrir el checkout Culqi multipago.
 */
public record PagoCheckoutOrdenResponse(
    String orderId,
    BigDecimal total,
    String currency
) {}
