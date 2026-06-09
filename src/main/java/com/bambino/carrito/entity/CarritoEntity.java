package com.bambino.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "carrito")
/**
 * Clase que maneja la funcionalidad de CarritoEntity.
 */
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoCarrito estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, length = 20)
    private CanalCarrito canal;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Long idCarrito) { this.idCarrito = idCarrito; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public EstadoCarrito getEstado() { return estado; }
    public void setEstado(EstadoCarrito estado) { this.estado = estado; }
    public CanalCarrito getCanal() { return canal; }
    public void setCanal(CanalCarrito canal) { this.canal = canal; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
