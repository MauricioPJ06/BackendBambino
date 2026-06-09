package com.bambino.configuracion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record que maneja la funcionalidad de ConfiguracionGlobalRequest.
 */
public record ConfiguracionGlobalRequest(
    @NotBlank(message = "moneda es obligatoria")
    @Size(max = 10, message = "moneda excede longitud")
    String moneda,

    @NotNull(message = "igvPorcentaje es obligatorio")
    @DecimalMin(value = "0.00", message = "igvPorcentaje no valido")
    BigDecimal igvPorcentaje,

    @NotNull(message = "deliveryMontoMinimo es obligatorio")
    @DecimalMin(value = "0.00", message = "deliveryMontoMinimo no valido")
    BigDecimal deliveryMontoMinimo,

    @NotNull(message = "deliveryTiempoMinMinutos es obligatorio")
    @Min(value = 1, message = "deliveryTiempoMinMinutos no valido")
    Integer deliveryTiempoMinMinutos,

    @NotNull(message = "deliveryTiempoMaxMinutos es obligatorio")
    @Min(value = 1, message = "deliveryTiempoMaxMinutos no valido")
    Integer deliveryTiempoMaxMinutos,

    @NotBlank(message = "timezone es obligatorio")
    @Size(max = 60, message = "timezone excede longitud")
    String timezone
) {}
