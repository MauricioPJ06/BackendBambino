package com.bambino.catalogo.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ProductoResponse.
 */
public record ProductoResponse(
    Long idProducto,
    String nombre,
    String descripcion,
    Long idCategoria,
    String categoriaNombre,
    BigDecimal precioBase,
    Boolean visibleWeb,
    Boolean disponible,
    String estado,
    String imagenUrl,
    Integer ordenVisual,
    BigDecimal precioFinal,
    BigDecimal descuentoAplicado,
    Long idOfertaActiva,
    String ofertaNombre,
    String ofertaTipo
) {}
