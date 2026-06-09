package com.bambino.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de CambiarPasswordRequest.
 */
public record CambiarPasswordRequest(
    @NotBlank(message = "passwordActual es obligatorio")
    @Size(min = 8, max = 100, message = "passwordActual debe tener entre 8 y 100 caracteres")
    String passwordActual,

    @NotBlank(message = "passwordNueva es obligatorio")
    @Size(min = 8, max = 100, message = "passwordNueva debe tener entre 8 y 100 caracteres")
    String passwordNueva,

    @NotBlank(message = "passwordConfirmacion es obligatorio")
    @Size(min = 8, max = 100, message = "passwordConfirmacion debe tener entre 8 y 100 caracteres")
    String passwordConfirmacion
) {
}
