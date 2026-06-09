package com.bambino.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de CategoriaRequest.
 */
public record CategoriaRequest(
    @NotBlank(message = "nombre es obligatorio")
    @Size(max = 120, message = "nombre excede longitud")
    String nombre,

    @Size(max = 220, message = "descripcion excede longitud")
    String descripcion,

    @NotNull(message = "ordenVisual es obligatorio")
    Integer ordenVisual,

    @NotNull(message = "activa es obligatorio")
    Boolean activa
) {}
