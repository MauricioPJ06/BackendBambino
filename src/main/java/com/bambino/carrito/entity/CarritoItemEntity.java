package com.bambino.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carrito_item")
/**
 * Clase que maneja la funcionalidad de CarritoItemEntity.
 */
public class CarritoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_item")
    private Long idCarritoItem;

    @Column(name = "id_carrito", nullable = false)
    private Long idCarrito;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioSnapshot;

    @Column(name = "descuento_unitario_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuentoUnitarioSnapshot;

    @Column(name = "observacion", length = 220)
    private String observacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdCarritoItem() { return idCarritoItem; }
    public void setIdCarritoItem(Long idCarritoItem) { this.idCarritoItem = idCarritoItem; }
    public Long getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Long idCarrito) { this.idCarrito = idCarrito; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitarioSnapshot() { return precioUnitarioSnapshot; }
    public void setPrecioUnitarioSnapshot(BigDecimal precioUnitarioSnapshot) { this.precioUnitarioSnapshot = precioUnitarioSnapshot; }
    public BigDecimal getDescuentoUnitarioSnapshot() { return descuentoUnitarioSnapshot; }
    public void setDescuentoUnitarioSnapshot(BigDecimal descuentoUnitarioSnapshot) { this.descuentoUnitarioSnapshot = descuentoUnitarioSnapshot; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
