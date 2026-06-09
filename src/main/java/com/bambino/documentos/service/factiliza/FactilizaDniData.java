package com.bambino.documentos.service.factiliza;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FactilizaDniData(
    String numero,
    String nombres,
    @JsonProperty("apellido_paterno")
    String apellidoPaterno,
    @JsonProperty("apellido_materno")
    String apellidoMaterno,
    @JsonProperty("nombre_completo")
    String nombreCompleto,
    String departamento,
    String provincia,
    String distrito,
    String direccion,
    @JsonProperty("direccion_completa")
    String direccionCompleta
) {}
