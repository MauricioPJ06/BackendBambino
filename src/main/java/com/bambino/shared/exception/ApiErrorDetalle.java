package com.bambino.shared.exception;

/**
 * Record que maneja la funcionalidad de ApiErrorDetalle.
 */
public record ApiErrorDetalle(
    String campo,
    String mensaje
) {
}
