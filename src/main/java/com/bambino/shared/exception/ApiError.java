package com.bambino.shared.exception;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Record que maneja la funcionalidad de ApiError.
 */
public record ApiError(
    OffsetDateTime fecha,
    int estado,
    String error,
    String mensaje,
    String ruta,
    List<ApiErrorDetalle> detalles
) {
}
