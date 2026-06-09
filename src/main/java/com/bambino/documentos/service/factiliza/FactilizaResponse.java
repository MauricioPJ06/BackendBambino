package com.bambino.documentos.service.factiliza;

public record FactilizaResponse<T>(
    Integer status,
    Boolean success,
    String message,
    T data
) {}
