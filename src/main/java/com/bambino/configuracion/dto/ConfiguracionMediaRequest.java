package com.bambino.configuracion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record que maneja la funcionalidad de ConfiguracionMediaRequest.
 */
public record ConfiguracionMediaRequest(
        @NotBlank @Size(max = 160) String nombre,
        @Size(max = 220) String descripcion,
        @NotBlank String tipo,
        @NotNull String url,
        @Size(max = 220) String publicId,
        @Size(max = 120) String versionTag,
        @NotNull Boolean activa
) {
}
