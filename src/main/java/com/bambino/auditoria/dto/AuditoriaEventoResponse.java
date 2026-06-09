package com.bambino.auditoria.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de AuditoriaEventoResponse.
 */
public record AuditoriaEventoResponse(
    Long idEvento,
    String entidad,
    String entidadId,
    String accion,
    String actorTipo,
    Long idActor,
    String canal,
    String metadataJson,
    LocalDateTime fechaCreacion
) {}
