package com.bambino.pagos.dto;

/**
 * Configuracion publica necesaria para cargar Culqi en el frontend.
 */
public record CulqiPublicConfigResponse(
    String publicKey,
    String checkoutScriptUrl,
    String currency,
    boolean habilitado
) {}
