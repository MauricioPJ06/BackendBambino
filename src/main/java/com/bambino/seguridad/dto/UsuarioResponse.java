package com.bambino.seguridad.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de UsuarioResponse.
 */
public record UsuarioResponse(
    Long idUsuario,
    String uuidUsuario,
    String email,
    String nombres,
    String apellidos,
    String telefono,
    String estado,
    String rol,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {
}
