package com.bambino.configuracion.dto;

/**
 * Record que maneja la funcionalidad de EmpresaResponse.
 */
public record EmpresaResponse(
    Long idEmpresa,
    String ruc,
    String razonSocial,
    String nombreComercial,
    String direccionFiscal,
    String telefono,
    String correo,
    Boolean activo
) {}
