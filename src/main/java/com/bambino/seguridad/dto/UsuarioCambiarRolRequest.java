package com.bambino.seguridad.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que maneja la funcionalidad de UsuarioCambiarRolRequest.
 */
public record UsuarioCambiarRolRequest(
    @NotBlank(message = "rol es obligatorio")
    String rol
) {}
