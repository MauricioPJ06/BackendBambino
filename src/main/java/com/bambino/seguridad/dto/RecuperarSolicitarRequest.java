package com.bambino.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de RecuperarSolicitarRequest.
 */
public record RecuperarSolicitarRequest(
    @NotBlank(message = "email es obligatorio")
    @Email(message = "email invalido")
    @Size(max = 190, message = "email excede longitud")
    String email
) {
}
