package com.bambino.documentos.service.factiliza;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FactilizaRucData(
    String numero,
    @JsonProperty("nombre_o_razon_social")
    String nombreORazonSocial,
    @JsonProperty("tipo_contribuyente")
    String tipoContribuyente,
    String estado,
    String condicion,
    String departamento,
    String provincia,
    String distrito,
    String direccion,
    @JsonProperty("direccion_completa")
    String direccionCompleta
) {}
