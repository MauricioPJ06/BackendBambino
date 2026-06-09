package com.bambino.documentos.dto;

public record ConsultaDocumentoResponse(
    String tipoDocumento,
    String numeroDocumento,
    String nombreORazonSocial,
    String nombres,
    String apellidoPaterno,
    String apellidoMaterno,
    String direccion,
    String direccionCompleta,
    String departamento,
    String provincia,
    String distrito,
    String estadoContribuyente,
    String condicionContribuyente
) {}
