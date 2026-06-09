package com.bambino.seguridad.dto;

import java.util.List;

/**
 * Record que maneja la funcionalidad de PerfilActualResponse.
 */
public record PerfilActualResponse(
    String usuario,
    String nombres,
    String apellidos,
    String telefono,
    List<String> roles
) {
}
