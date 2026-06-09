package com.bambino.pedidos.entity;

/**
 * Enumerado que maneja la funcionalidad de EstadoPedido.
 */
public enum EstadoPedido {
    CREADO,
    PAGO_PENDIENTE,
    PAGO_RECHAZADO,
    PAGO_APROBADO,
    CONFIRMADO,
    EN_PREPARACION,
    LISTO_RECOJO,
    LISTO_DESPACHO,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO,
    ANULADO
}
