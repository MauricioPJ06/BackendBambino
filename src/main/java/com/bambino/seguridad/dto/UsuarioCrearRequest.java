package com.bambino.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de UsuarioCrearRequest.
 */
public record UsuarioCrearRequest(
    @NotBlank(message = "email es obligatorio")
    @Email(message = "email invalido")
    @Size(max = 190, message = "email excede longitud")
    String email,

    @NotBlank(message = "password es obligatorio")
    @Size(min = 8, max = 100, message = "password debe tener entre 8 y 100 caracteres")
    String password,

    @NotBlank(message = "nombres es obligatorio")
    @Size(max = 120, message = "nombres excede longitud")
    String nombres,

    @NotBlank(message = "apellidos es obligatorio")
    @Size(max = 120, message = "apellidos excede longitud")
    String apellidos,

    @Size(max = 20, message = "telefono excede longitud")
    String telefono,

    @NotBlank(message = "rol es obligatorio")
    String rol
) {
}
