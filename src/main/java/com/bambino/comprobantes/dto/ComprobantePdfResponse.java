package com.bambino.comprobantes.dto;

/**
 * Respuesta interna para entregar un comprobante en PDF.
 */
public record ComprobantePdfResponse(
    byte[] contenido,
    String filename
) {
}
