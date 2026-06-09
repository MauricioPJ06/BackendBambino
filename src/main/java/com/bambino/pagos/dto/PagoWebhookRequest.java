package com.bambino.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PagoWebhookRequest.
 */
public record PagoWebhookRequest(
    @NotBlank(message = "idempotencyKey es obligatorio")
    @Size(max = 36, message = "idempotencyKey excede longitud")
    String idempotencyKey,

    @NotBlank(message = "estado es obligatorio")
    String estado,

    @Size(max = 120, message = "proveedorTxnId excede longitud")
    String proveedorTxnId,

    @Size(max = 255, message = "motivo excede longitud")
    String motivo
) {}
