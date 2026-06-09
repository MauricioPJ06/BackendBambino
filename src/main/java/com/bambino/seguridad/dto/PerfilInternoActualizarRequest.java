package com.bambino.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de PerfilInternoActualizarRequest.
 */
public record PerfilInternoActualizarRequest(
    @NotBlank(message = "email es obligatorio")
    @Email(message = "email invalido")
    @Size(max = 190, message = "email excede longitud")
    String email,

    @NotBlank(message = "nombres es obligatorio")
    @Size(max = 120, message = "nombres excede longitud")
    String nombres,

    @NotBlank(message = "apellidos es obligatorio")
    @Size(max = 120, message = "apellidos excede longitud")
    String apellidos,

    @Size(max = 20, message = "telefono excede longitud")
    @Pattern(regexp = "^$|^[0-9+\\-\\s]{7,20}$", message = "telefono invalido")
    String telefono
) {
}
