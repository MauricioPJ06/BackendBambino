package com.bambino.carrito.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de CarritoItemAgregarRequest.
 */
public record CarritoItemAgregarRequest(
    @NotNull(message = "idProducto es obligatorio")
    Long idProducto,

    @NotNull(message = "cantidad es obligatoria")
    @DecimalMin(value = "0.001", message = "cantidad debe ser mayor a 0")
    BigDecimal cantidad,

    @Size(max = 220, message = "observacion excede longitud")
    String observacion
) {}
