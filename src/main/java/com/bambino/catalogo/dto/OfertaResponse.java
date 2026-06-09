package com.bambino.catalogo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Record que maneja la funcionalidad de OfertaResponse.
 */
public record OfertaResponse(
    Long idOferta,
    String nombre,
    String tipo,
    BigDecimal valorDescuento,
    BigDecimal precioEspecial,
    String estado,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    List<Long> idsProductos
) {}
