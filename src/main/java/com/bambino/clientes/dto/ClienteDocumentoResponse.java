package com.bambino.clientes.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de ClienteDocumentoResponse.
 */
public record ClienteDocumentoResponse(
    Long idDocumento,
    String docTipo,
    String docNumero,
    Boolean esPrincipal,
    Boolean activo,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {
}
