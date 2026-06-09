package com.bambino.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de RecuperarConfirmarRequest.
 */
public record RecuperarConfirmarRequest(
    @NotBlank(message = "email es obligatorio")
    @Email(message = "email invalido")
    @Size(max = 190, message = "email excede longitud")
    String email,

    @NotBlank(message = "codigo es obligatorio")
    @Size(max = 10, message = "codigo excede longitud")
    String codigo,

    @NotBlank(message = "nuevaPassword es obligatorio")
    @Size(min = 8, max = 100, message = "nuevaPassword debe tener entre 8 y 100 caracteres")
    String nuevaPassword
) {
}
