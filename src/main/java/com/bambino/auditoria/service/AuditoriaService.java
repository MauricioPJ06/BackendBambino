package com.bambino.auditoria.service;

import com.bambino.auditoria.dto.AuditoriaEventoResponse;
import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.entity.AuditoriaEventoEntity;
import com.bambino.auditoria.repository.AuditoriaEventoRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de AuditoriaService.
 */
public class AuditoriaService {

    private final AuditoriaEventoRepository auditoriaEventoRepository;
    private final UsuarioRepository usuarioRepository;

    public AuditoriaService(AuditoriaEventoRepository auditoriaEventoRepository,
                            UsuarioRepository usuarioRepository) {
        this.auditoriaEventoRepository = auditoriaEventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrar(String entidad,
                          String entidadId,
                          String accion,
                          AuditoriaActorTipo actorTipo,
                          Long idActor,
                          AuditoriaCanal canal,
                          String metadataJson) {
        AuditoriaEventoEntity e = new AuditoriaEventoEntity();
        e.setEntidad(entidad == null ? null : entidad.trim().toUpperCase(Locale.ROOT));
        e.setEntidadId(entidadId);
        e.setAccion(accion == null ? null : accion.trim().toUpperCase(Locale.ROOT));
        e.setActorTipo(actorTipo);
        e.setIdActor(idActor != null ? idActor : resolverActorDesdeSesion());
        e.setCanal(canal);
        e.setMetadataJson(normalizarMetadata(metadataJson));
        e.setFechaCreacion(LocalDateTime.now());
        auditoriaEventoRepository.save(e);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listar.
     * @param entidad parametro de entrada para la operacion
     * @param accion parametro de entrada para la operacion
     * @param actorTipo parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<AuditoriaEventoResponse> listar(String entidad, String accion, String actorTipo) {
        String entidadNorm = entidad == null || entidad.isBlank() ? null : entidad.trim().toUpperCase(Locale.ROOT);
        String accionNorm = accion == null || accion.isBlank() ? null : accion.trim().toUpperCase(Locale.ROOT);
        AuditoriaActorTipo actorTipoNorm = parseActorTipo(actorTipo);

        List<AuditoriaEventoEntity> eventos;
        if (entidadNorm != null && accionNorm != null) {
            eventos = auditoriaEventoRepository.findTop200ByEntidadAndAccionOrderByIdEventoDesc(entidadNorm, accionNorm);
        } else if (entidadNorm != null && actorTipoNorm != null) {
            eventos = auditoriaEventoRepository.findTop200ByEntidadAndActorTipoOrderByIdEventoDesc(entidadNorm, actorTipoNorm);
        } else if (entidadNorm != null) {
            eventos = auditoriaEventoRepository.findTop200ByEntidadOrderByIdEventoDesc(entidadNorm);
        } else if (actorTipoNorm != null) {
            eventos = auditoriaEventoRepository.findTop200ByActorTipoOrderByIdEventoDesc(actorTipoNorm);
        } else {
            eventos = auditoriaEventoRepository.findTop200ByOrderByIdEventoDesc();
        }

        return eventos.stream().map(this::toResponse).toList();
    }

    private AuditoriaEventoResponse toResponse(AuditoriaEventoEntity e) {
        return new AuditoriaEventoResponse(
            e.getIdEvento(),
            e.getEntidad(),
            e.getEntidadId(),
            e.getAccion(),
            e.getActorTipo() == null ? null : e.getActorTipo().name(),
            e.getIdActor(),
            e.getCanal() == null ? null : e.getCanal().name(),
            e.getMetadataJson(),
            e.getFechaCreacion()
        );
    }

    private Long resolverActorDesdeSesion() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
                return null;
            }
            UsuarioEntity usuario = usuarioRepository.findByEmail(authentication.getName().trim().toLowerCase(Locale.ROOT))
                .orElse(null);
            return usuario == null ? null : usuario.getIdUsuario();
        } catch (Exception ex) {
            return null;
        }
    }

    private String normalizarMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.isBlank()) {
            return "{}";
        }
        return metadataJson.trim();
    }

    private AuditoriaActorTipo parseActorTipo(String actorTipo) {
        if (actorTipo == null || actorTipo.isBlank()) {
            return null;
        }
        try {
            return AuditoriaActorTipo.valueOf(actorTipo.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }
}
