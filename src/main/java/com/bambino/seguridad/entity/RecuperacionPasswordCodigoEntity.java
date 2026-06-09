package com.bambino.seguridad.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacion_password_codigo")
/**
 * Clase que maneja la funcionalidad de RecuperacionPasswordCodigoEntity.
 */
public class RecuperacionPasswordCodigoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo")
    private Long idCodigo;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "correo", nullable = false, length = 190)
    private String correo;

    @Column(name = "codigo", length = 10)
    private String codigo;

    @Column(name = "codigo_hash", length = 255)
    private String codigoHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoRecuperacionCodigo estado;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdCodigo() { return idCodigo; }
    public void setIdCodigo(Long idCodigo) { this.idCodigo = idCodigo; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigoHash() { return codigoHash; }
    public void setCodigoHash(String codigoHash) { this.codigoHash = codigoHash; }
    public EstadoRecuperacionCodigo getEstado() { return estado; }
    public void setEstado(EstadoRecuperacionCodigo estado) { this.estado = estado; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
