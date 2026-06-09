package com.bambino.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de RecuperarValidarCodigoRequest.
 */
public record RecuperarValidarCodigoRequest(
    @NotBlank(message = "email es obligatorio")
    @Email(message = "email invalido")
    @Size(max = 190, message = "email excede longitud")
    String email,

    @NotBlank(message = "codigo es obligatorio")
    @Size(max = 10, message = "codigo excede longitud")
    String codigo
) {
}
