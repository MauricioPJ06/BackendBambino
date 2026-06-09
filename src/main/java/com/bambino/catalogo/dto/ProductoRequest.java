package com.bambino.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ProductoRequest.
 */
public record ProductoRequest(
    @NotBlank(message = "nombre es obligatorio")
    @Size(max = 160, message = "nombre excede longitud")
    String nombre,

    @NotBlank(message = "descripcion es obligatoria")
    String descripcion,

    @NotNull(message = "idCategoria es obligatorio")
    Long idCategoria,

    @NotNull(message = "precioBase es obligatorio")
    @DecimalMin(value = "0.01", message = "precioBase debe ser mayor a 0")
    BigDecimal precioBase,

    @NotNull(message = "visibleWeb es obligatorio")
    Boolean visibleWeb,

    @NotNull(message = "disponible es obligatorio")
    Boolean disponible,

    @NotBlank(message = "estado es obligatorio")
    String estado,

    String imagenUrl,

    @NotNull(message = "ordenVisual es obligatorio")
    @Min(value = 1, message = "ordenVisual debe ser mayor a 0")
    Integer ordenVisual
) {}
