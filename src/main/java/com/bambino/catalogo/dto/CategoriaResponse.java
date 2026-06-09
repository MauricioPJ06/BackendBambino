package com.bambino.catalogo.dto;

/**
 * Record que maneja la funcionalidad de CategoriaResponse.
 */
public record CategoriaResponse(
    Long idCategoria,
    String nombre,
    String descripcion,
    Integer ordenVisual,
    Boolean activa
) {}
