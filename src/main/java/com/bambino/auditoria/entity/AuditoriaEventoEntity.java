package com.bambino.auditoria.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_evento")
/**
 * Clase que maneja la funcionalidad de AuditoriaEventoEntity.
 */
public class AuditoriaEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "entidad", nullable = false, length = 80)
    private String entidad;

    @Column(name = "entidad_id", nullable = false, length = 80)
    private String entidadId;

    @Column(name = "accion", nullable = false, length = 80)
    private String accion;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_tipo", length = 20)
    private AuditoriaActorTipo actorTipo;

    @Column(name = "id_actor")
    private Long idActor;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", length = 20)
    private AuditoriaCanal canal;

    @Column(name = "metadata_json", columnDefinition = "json")
    private String metadataJson;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    public String getEntidadId() { return entidadId; }
    public void setEntidadId(String entidadId) { this.entidadId = entidadId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public AuditoriaActorTipo getActorTipo() { return actorTipo; }
    public void setActorTipo(AuditoriaActorTipo actorTipo) { this.actorTipo = actorTipo; }
    public Long getIdActor() { return idActor; }
    public void setIdActor(Long idActor) { this.idActor = idActor; }
    public AuditoriaCanal getCanal() { return canal; }
    public void setCanal(AuditoriaCanal canal) { this.canal = canal; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
