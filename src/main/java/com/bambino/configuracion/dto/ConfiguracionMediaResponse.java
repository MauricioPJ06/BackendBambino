package com.bambino.configuracion.dto;

/**
 * Record que maneja la funcionalidad de ConfiguracionMediaResponse.
 */
public record ConfiguracionMediaResponse(
        Long idMedia,
        String clave,
        String nombre,
        String descripcion,
        String tipo,
        String url,
        String publicId,
        String versionTag,
        Boolean activa
) {
}
