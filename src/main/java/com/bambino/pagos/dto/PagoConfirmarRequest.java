package com.bambino.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PagoConfirmarRequest.
 */
public record PagoConfirmarRequest(
    @NotBlank(message = "estado es obligatorio")
    String estado,

    @Size(max = 120, message = "proveedorTxnId excede longitud")
    String proveedorTxnId,

    @Size(max = 255, message = "motivo excede longitud")
    String motivo
) {}
