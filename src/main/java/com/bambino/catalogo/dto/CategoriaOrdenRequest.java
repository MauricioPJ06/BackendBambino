package com.bambino.catalogo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Record que maneja la funcionalidad de CategoriaOrdenRequest.
 */
public record CategoriaOrdenRequest(
    @NotEmpty(message = "idsCategorias es obligatorio")
    List<@NotNull(message = "idCategoria invalido") Long> idsCategorias
) {}
