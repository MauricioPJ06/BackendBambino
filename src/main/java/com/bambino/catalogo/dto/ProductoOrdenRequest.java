package com.bambino.catalogo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Record que maneja la funcionalidad de ProductoOrdenRequest.
 */
public record ProductoOrdenRequest(
    @NotEmpty(message = "idsProductos es obligatorio")
    List<@NotNull(message = "idProducto invalido") Long> idsProductos
) {}
