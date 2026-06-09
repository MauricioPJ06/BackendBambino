package com.bambino.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de PedidoCrearDesdeCheckoutRequest.
 */
public record PedidoCrearDesdeCheckoutRequest(
    @NotBlank(message = "modalidad es obligatoria")
    String modalidad,

    @NotBlank(message = "tipoComprobante es obligatorio")
    String tipoComprobante,

    Long idDireccionEntrega,
    String docNumero,
    String razonSocial,
    String direccionFiscal,

    @NotNull(message = "subtotal es obligatorio")
    BigDecimal subtotal,

    @NotNull(message = "descuentoTotal es obligatorio")
    BigDecimal descuentoTotal,

    @NotNull(message = "impuestoTotal es obligatorio")
    BigDecimal impuestoTotal,

    @NotNull(message = "total es obligatorio")
    BigDecimal total
) {}
