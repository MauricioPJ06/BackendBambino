package com.bambino.configuracion.dto;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ConfiguracionGlobalResponse.
 */
public record ConfiguracionGlobalResponse(
    Short idConfig,
    String moneda,
    BigDecimal igvPorcentaje,
    BigDecimal deliveryMontoMinimo,
    Integer deliveryTiempoMinMinutos,
    Integer deliveryTiempoMaxMinutos,
    String timezone
) {}
