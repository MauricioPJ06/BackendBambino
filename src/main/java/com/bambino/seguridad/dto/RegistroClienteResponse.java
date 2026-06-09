package com.bambino.seguridad.dto;

/**
 * Record que maneja la funcionalidad de RegistroClienteResponse.
 */
public record RegistroClienteResponse(
    Long idCliente,
    String email,
    String mensaje
) {
}
