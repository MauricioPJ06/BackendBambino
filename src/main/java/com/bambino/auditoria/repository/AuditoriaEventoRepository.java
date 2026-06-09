package com.bambino.auditoria.repository;

import com.bambino.auditoria.entity.AuditoriaEventoEntity;
import com.bambino.auditoria.entity.AuditoriaActorTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de AuditoriaEventoRepository.
 */
public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEventoEntity, Long> {
    List<AuditoriaEventoEntity> findTop200ByOrderByIdEventoDesc();
    List<AuditoriaEventoEntity> findTop200ByEntidadOrderByIdEventoDesc(String entidad);
    List<AuditoriaEventoEntity> findTop200ByEntidadAndAccionOrderByIdEventoDesc(String entidad, String accion);
    List<AuditoriaEventoEntity> findTop200ByActorTipoOrderByIdEventoDesc(AuditoriaActorTipo actorTipo);
    List<AuditoriaEventoEntity> findTop200ByEntidadAndActorTipoOrderByIdEventoDesc(String entidad, AuditoriaActorTipo actorTipo);
    Page<AuditoriaEventoEntity> findByEntidad(String entidad, Pageable pageable);
    Page<AuditoriaEventoEntity> findByAccion(String accion, Pageable pageable);
    Page<AuditoriaEventoEntity> findByActorTipo(AuditoriaActorTipo actorTipo, Pageable pageable);
    Page<AuditoriaEventoEntity> findByEntidadAndAccion(String entidad, String accion, Pageable pageable);
    Page<AuditoriaEventoEntity> findByEntidadAndActorTipo(String entidad, AuditoriaActorTipo actorTipo, Pageable pageable);
    Page<AuditoriaEventoEntity> findByAccionAndActorTipo(String accion, AuditoriaActorTipo actorTipo, Pageable pageable);
    Page<AuditoriaEventoEntity> findByEntidadAndAccionAndActorTipo(String entidad, String accion, AuditoriaActorTipo actorTipo, Pageable pageable);
}
