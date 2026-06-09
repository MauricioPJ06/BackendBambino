package com.bambino.configuracion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de EmpresaRequest.
 */
public record EmpresaRequest(
    @NotBlank(message = "ruc es obligatorio")
    @Size(max = 20, message = "ruc excede longitud")
    String ruc,

    @NotBlank(message = "razonSocial es obligatoria")
    @Size(max = 220, message = "razonSocial excede longitud")
    String razonSocial,

    @Size(max = 220, message = "nombreComercial excede longitud")
    String nombreComercial,

    @NotBlank(message = "direccionFiscal es obligatoria")
    @Size(max = 300, message = "direccionFiscal excede longitud")
    String direccionFiscal,

    @Size(max = 30, message = "telefono excede longitud")
    String telefono,

    @Size(max = 190, message = "correo excede longitud")
    String correo,

    @NotNull(message = "activo es obligatorio")
    Boolean activo
) {}
