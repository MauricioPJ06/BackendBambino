package com.bambino.seguridad.dto;

/**
 * Record que maneja la funcionalidad de PerfilInternoResponse.
 */
public record PerfilInternoResponse(
    Long idUsuario,
    String email,
    String nombres,
    String apellidos,
    String telefono,
    String rol,
    String estado
) {
}
