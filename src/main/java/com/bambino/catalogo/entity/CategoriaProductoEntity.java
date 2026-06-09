package com.bambino.catalogo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categoria_producto")
/**
 * Clase que maneja la funcionalidad de CategoriaProductoEntity.
 */
public class CategoriaProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(name = "descripcion", length = 220)
    private String descripcion;

    @Column(name = "orden_visual", nullable = false)
    private Integer ordenVisual;

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

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getOrdenVisual() { return ordenVisual; }
    public void setOrdenVisual(Integer ordenVisual) { this.ordenVisual = ordenVisual; }
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
