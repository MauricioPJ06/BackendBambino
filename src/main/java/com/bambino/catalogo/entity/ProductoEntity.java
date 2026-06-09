package com.bambino.catalogo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
/**
 * Clase que maneja la funcionalidad de ProductoEntity.
 */
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaProductoEntity categoria;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(name = "visible_web", nullable = false)
    private Boolean visibleWeb;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoProducto estado;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "orden_visual", nullable = false)
    private Integer ordenVisual;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public CategoriaProductoEntity getCategoria() { return categoria; }
    public void setCategoria(CategoriaProductoEntity categoria) { this.categoria = categoria; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase; }
    public Boolean getVisibleWeb() { return visibleWeb; }
    public void setVisibleWeb(Boolean visibleWeb) { this.visibleWeb = visibleWeb; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Integer getOrdenVisual() { return ordenVisual; }
    public void setOrdenVisual(Integer ordenVisual) { this.ordenVisual = ordenVisual; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
