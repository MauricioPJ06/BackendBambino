package com.bambino.carrito.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que maneja la funcionalidad de CheckoutValidarRequest.
 */
public record CheckoutValidarRequest(
    @NotBlank(message = "modalidad es obligatoria")
    String modalidad,

    @NotBlank(message = "tipoComprobante es obligatorio")
    String tipoComprobante,

    Long idDireccion,
    String docNumero,
    String razonSocial,
    String direccionFiscal
) {}
