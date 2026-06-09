package com.bambino.seguridad.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que maneja la funcionalidad de UsuarioCambiarEstadoRequest.
 */
public record UsuarioCambiarEstadoRequest(
    @NotBlank(message = "estado es obligatorio")
    String estado
) {
}
