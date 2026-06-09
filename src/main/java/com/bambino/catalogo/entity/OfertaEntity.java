package com.bambino.catalogo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferta")
/**
 * Clase que maneja la funcionalidad de OfertaEntity.
 */
public class OfertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private Long idOferta;

    @Column(name = "nombre", nullable = false, length = 180)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoOferta tipo;

    @Column(name = "valor_descuento", precision = 10, scale = 2)
    private BigDecimal valorDescuento;

    @Column(name = "precio_especial", precision = 10, scale = 2)
    private BigDecimal precioEspecial;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 12)
    private EstadoOferta estado;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdOferta() { return idOferta; }
    public void setIdOferta(Long idOferta) { this.idOferta = idOferta; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoOferta getTipo() { return tipo; }
    public void setTipo(TipoOferta tipo) { this.tipo = tipo; }
    public BigDecimal getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(BigDecimal valorDescuento) { this.valorDescuento = valorDescuento; }
    public BigDecimal getPrecioEspecial() { return precioEspecial; }
    public void setPrecioEspecial(BigDecimal precioEspecial) { this.precioEspecial = precioEspecial; }
    public EstadoOferta getEstado() { return estado; }
    public void setEstado(EstadoOferta estado) { this.estado = estado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
