package com.bambino.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la confirmacion de pago desde checkout.
 */
public record PagoCheckoutConfirmarRequest(
    @NotBlank(message = "modalidad es obligatoria")
    String modalidad,

    @NotBlank(message = "tipoComprobante es obligatorio")
    String tipoComprobante,

    Long idDireccion,
    String docNumero,
    String razonSocial,
    String direccionFiscal,

    @NotBlank(message = "metodo es obligatorio")
    String metodo,

    @NotBlank(message = "idempotencyKey es obligatorio")
    @Size(max = 36, message = "idempotencyKey excede longitud")
    String idempotencyKey,

    @Size(max = 80, message = "proveedor excede longitud")
    String proveedor,

    @Size(max = 80, message = "culqiToken excede longitud")
    String culqiToken
) {}
