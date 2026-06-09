package com.bambino.configuracion.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Record que maneja la funcionalidad de ZonaDeliveryResponse.
 */
public record ZonaDeliveryResponse(
    Long idZona,
    String nombre,
    Boolean activo,
    BigDecimal tarifaBase,
    BigDecimal montoMinimo,
    Integer tiempoEstimadoMinutos,
    String coberturaDescripcion,
    String mapaEmbedUrl,
    BigDecimal latitudCentro,
    BigDecimal longitudCentro,
    BigDecimal radioKm,
    LocalTime horaInicioAtencion,
    LocalTime horaFinAtencion
) {}
