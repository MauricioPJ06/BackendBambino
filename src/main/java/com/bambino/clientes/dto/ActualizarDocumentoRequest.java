package com.bambino.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de ActualizarDocumentoRequest.
 */
public record ActualizarDocumentoRequest(
    @NotBlank(message = "docTipo es obligatorio")
    String docTipo,

    @NotBlank(message = "docNumero es obligatorio")
    @Size(max = 20, message = "docNumero excede longitud")
    String docNumero
) {
}
