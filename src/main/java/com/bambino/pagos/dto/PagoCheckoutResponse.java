package com.bambino.pagos.dto;

import com.bambino.pedidos.dto.PedidoResponse;

/**
 * Record que retorna el pago aprobado y pedido formal creado desde checkout.
 */
public record PagoCheckoutResponse(
    PedidoResponse pedido,
    PagoResponse pago
) {}
