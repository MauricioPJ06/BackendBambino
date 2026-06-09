package com.bambino.configuracion.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_media")
/**
 * Clase que maneja la funcionalidad de ConfiguracionMediaEntity.
 */
public class ConfiguracionMediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media")
    private Long idMedia;

    @Column(name = "clave", nullable = false, length = 80, unique = true)
    private String clave;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "descripcion", length = 220)
    private String descripcion;

    @Column(name = "tipo", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoMedia tipo;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "public_id", length = 220)
    private String publicId;

    @Column(name = "version_tag", length = 120)
    private String versionTag;

    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdMedia() { return idMedia; }
    public void setIdMedia(Long idMedia) { this.idMedia = idMedia; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TipoMedia getTipo() { return tipo; }
    public void setTipo(TipoMedia tipo) { this.tipo = tipo; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getVersionTag() { return versionTag; }
    public void setVersionTag(String versionTag) { this.versionTag = versionTag; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
