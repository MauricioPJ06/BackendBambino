package com.bambino.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de PagoIniciarRequest.
 */
public record PagoIniciarRequest(
    @NotNull(message = "idPedido es obligatorio")
    Long idPedido,

    @NotBlank(message = "metodo es obligatorio")
    String metodo,

    @NotNull(message = "monto es obligatorio")
    BigDecimal monto,

    @NotBlank(message = "idempotencyKey es obligatorio")
    @Size(max = 36, message = "idempotencyKey excede longitud")
    String idempotencyKey,

    @Size(max = 80, message = "proveedor excede longitud")
    String proveedor
) {}
